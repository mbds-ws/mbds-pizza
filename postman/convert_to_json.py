#!/usr/bin/env python3
"""Convert Postman VS Code YAML collections to Postman Collection v2.1 JSON."""

import json
import os
import re
import uuid
from pathlib import Path

try:
    import yaml
except ImportError:
    print("PyYAML is required. Install it with: pip install pyyaml")
    exit(1)

COLLECTIONS_DIR = Path(__file__).parent / "collections"
ENVIRONMENTS_DIR = Path(__file__).parent / "environments"
OUTPUT_DIR = Path(__file__).parent / "json_export"


def load_yaml(path):
    with open(path, "r", encoding="utf-8") as f:
        return yaml.safe_load(f)


def parse_url(url_raw):
    """Parse a raw URL string into Postman's URL object with host, path, and query."""
    url_obj = {"raw": url_raw}

    # Split off query string
    query_params = []
    base = url_raw
    if "?" in url_raw:
        base, qs = url_raw.split("?", 1)
        for param in qs.split("&"):
            if "=" in param:
                k, v = param.split("=", 1)
                query_params.append({"key": k, "value": v})
            else:
                query_params.append({"key": param, "value": ""})

    # Split protocol
    if "://" in base:
        protocol, rest = base.split("://", 1)
        url_obj["protocol"] = protocol
    else:
        rest = base

    # Split host and path
    parts = rest.split("/")
    host_part = parts[0] if parts else rest
    path_parts = parts[1:] if len(parts) > 1 else []

    # Host might contain port
    url_obj["host"] = host_part.split(".")  if "." in host_part and "{{" not in host_part else [host_part]
    url_obj["path"] = path_parts

    # Port
    if ":" in host_part and "{{" not in host_part:
        host_no_port, port = host_part.rsplit(":", 1)
        url_obj["host"] = [host_no_port]
        url_obj["port"] = port

    if query_params:
        url_obj["query"] = query_params

    return url_obj


def build_auth(auth_data):
    """Convert YAML auth to Postman v2.1 auth format."""
    if auth_data is None:
        return None

    # Handle list format (collection-level)
    if isinstance(auth_data, list):
        auth_data = auth_data[0]

    auth_type = auth_data.get("type", "noauth")
    if auth_type == "noauth":
        return {"type": "noauth"}
    if auth_type == "bearer":
        token = auth_data.get("credentials", {}).get("token", "")
        return {
            "type": "bearer",
            "bearer": [{"key": "token", "value": token, "type": "string"}],
        }
    return None


def build_request_item(request_path):
    """Convert a single YAML request file to a Postman item."""
    data = load_yaml(request_path)
    if not data or data.get("$kind") != "http-request":
        return None

    name = request_path.stem.replace(".request", "")
    method = data.get("method", "GET")
    url_raw = data.get("url", "")

    # Build URL object with host/path breakdown for Postman
    url_obj = parse_url(url_raw)

    # Build headers
    headers = []
    for key, value in (data.get("headers") or {}).items():
        headers.append({"key": key, "value": value})

    # Build body
    body_data = data.get("body")
    body = None
    if body_data and body_data.get("content"):
        body = {"mode": "raw", "raw": body_data["content"], "options": {"raw": {"language": "json"}}}

    # Build scripts (events)
    events = []
    for script in data.get("scripts") or []:
        event_type = "test" if script.get("type") == "afterResponse" else "prerequest"
        events.append(
            {
                "listen": event_type,
                "script": {
                    "type": "text/javascript",
                    "exec": script.get("code", "").split("\n"),
                },
            }
        )

    # Build auth
    auth = build_auth(data.get("auth"))

    item = {"name": name, "request": {"method": method, "header": headers, "url": url_obj}}

    if body:
        item["request"]["body"] = body
    if auth:
        item["request"]["auth"] = auth
    if events:
        item["event"] = events

    return item, data.get("order", 9999)


def convert_collection(collection_dir):
    """Convert one collection folder to Postman v2.1 JSON."""
    collection_name = collection_dir.name
    definition_path = collection_dir / ".resources" / "definition.yaml"

    definition = {}
    if definition_path.exists():
        definition = load_yaml(definition_path) or {}

    # Gather and sort request files
    request_files = sorted(collection_dir.glob("*.request.yaml"))
    items_with_order = []
    for rf in request_files:
        result = build_request_item(rf)
        if result:
            item, order = result
            items_with_order.append((order, item))

    items_with_order.sort(key=lambda x: x[0])
    items = [item for _, item in items_with_order]

    # Build collection
    collection = {
        "info": {
            "name": collection_name,
            "description": definition.get("description", ""),
            "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
        },
        "item": items,
    }

    # Collection-level auth
    auth = build_auth(definition.get("auth"))
    if auth:
        collection["auth"] = auth

    return collection


def convert_environment(env_path):
    """Convert a YAML environment to Postman environment JSON."""
    data = load_yaml(env_path)
    if not data:
        return None

    values = []
    for v in data.get("values") or []:
        values.append(
            {
                "key": v.get("key", ""),
                "value": v.get("value", ""),
                "enabled": v.get("enabled", True),
                "type": "default",
            }
        )

    return {
        "name": data.get("name", env_path.stem),
        "values": values,
        "_postman_variable_scope": "environment",
    }


def main():
    OUTPUT_DIR.mkdir(exist_ok=True)

    # Convert collections
    collection_dirs = [
        d for d in COLLECTIONS_DIR.iterdir() if d.is_dir() and not d.name.startswith(".")
    ]
    for coll_dir in sorted(collection_dirs):
        collection = convert_collection(coll_dir)
        out_path = OUTPUT_DIR / f"{coll_dir.name}.postman_collection.json"
        with open(out_path, "w", encoding="utf-8") as f:
            json.dump(collection, f, indent=2, ensure_ascii=False)
        print(f"✓ {out_path.name}")

    # Convert environments
    for env_file in sorted(ENVIRONMENTS_DIR.glob("*.environment.yaml")):
        env = convert_environment(env_file)
        if env:
            out_path = OUTPUT_DIR / f"{env_file.stem}.postman_environment.json"
            with open(out_path, "w", encoding="utf-8") as f:
                json.dump(env, f, indent=2, ensure_ascii=False)
            print(f"✓ {out_path.name}")

    print(f"\nDone! JSON files are in: {OUTPUT_DIR}")
    print("Import them in Postman Desktop: File → Import → select the JSON files.")


if __name__ == "__main__":
    main()
