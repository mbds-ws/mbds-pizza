# API REST de Vente de Pizzas avec Spring Boot

## 1. Présentation du projet

Ce mini projet consiste à développer une API REST de gestion de vente de pizzas avec Spring Boot.

L’application permet :
- l’authentification avec JWT
- la gestion des rôles ADMIN et CLIENT
- la gestion des catégories, ingrédients et pizzas
- la gestion des commandes
- l’historique des commandes du client connecté
- le filtrage des pizzas et des commandes
- l’ajout de HATEOAS sur certaines ressources
- la génération de statistiques sur les ventes

Le projet a été réalisé en binôme dans le cadre du mini projet Web Service REST.

---

## 2. Objectifs du projet

Les objectifs principaux sont :
- créer une API REST professionnelle avec Spring Boot
- manipuler plusieurs entités liées entre elles
- mettre en place le CRUD
- sécuriser les endpoints avec JWT
- gérer les rôles
- appliquer des filtres de recherche
- ajouter du HATEOAS
- créer des endpoints complexes avec agrégation

---

## 3. Technologies utilisées

- Java
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- H2 Database
- Swagger / OpenAPI
- Postman
- Lombok
- Maven

---

## 4. Fonctionnalités réalisées

### Authentification et sécurité
- inscription
- connexion
- génération et validation du JWT
- sécurisation des endpoints
- gestion des rôles ADMIN / CLIENT

### Gestion des catégories
- création d’une catégorie
- consultation de toutes les catégories
- consultation par identifiant
- modification
- suppression

### Gestion des ingrédients
- création d’un ingrédient
- consultation de tous les ingrédients
- consultation par identifiant
- modification
- suppression

### Gestion des pizzas
- création d’une pizza
- consultation de toutes les pizzas
- consultation par identifiant
- modification
- suppression
- filtre par nom
- filtre par nom de catégorie
- filtre par disponibilité

### Gestion des commandes
- création d’une commande avec liste de pizzas et quantités
- calcul du sous-total de chaque ligne de commande
- calcul du montant total de la commande
- consultation d’une commande par identifiant
- historique des commandes du client connecté
- filtre des commandes par statut
- filtre des commandes par date
- filtre des commandes par intervalle de dates

### Statistiques
- chiffre d’affaires par période :
  - ALL
  - TODAY
  - WEEK
  - MONTH
- top pizzas par période :
  - ALL
  - TODAY
  - WEEK
  - MONTH

### HATEOAS
- HATEOAS sur Pizza :
  - `GET /api/pizzas`
  - `GET /api/pizzas/{id}`
- HATEOAS sur Order :
  - `GET /api/orders/{id}`
  - `GET /api/orders/my-orders`

---

## 5. Entités principales

Le projet repose sur les entités suivantes :
- User
- Category
- Ingredient
- Pizza
- Order
- OrderItem

### Relations
- Category 1 → n Pizza
- Pizza n ↔ n Ingredient
- User 1 → n Order
- Order 1 → n OrderItem
- Pizza 1 → n OrderItem

---

## 6. Endpoints complexes (agrégation)

### Revenue
`GET /api/stats/revenue?period=ALL|TODAY|WEEK|MONTH`

Calcule le chiffre d’affaires sur la période demandée, en excluant les commandes annulées.

### Top pizzas
`GET /api/stats/top-pizzas?period=ALL|TODAY|WEEK|MONTH`

Retourne les pizzas les plus vendues sur la période demandée.

---

## 7. Lancement du projet

### Prérequis
- Java
- Maven
- IDE Java

### Étapes
1. Cloner le dépôt :
```bash
git clone https://github.com/mbds-ws/mbds-pizza.git
```
2. Ouvrir le projet dans l’IDE
3. Installer les dépendances :
```bash
mvn clean install
```
4. Lancer l’application :
```bash
mvn spring-boot:run
```

## 8. Base de données
Le projet utilise **H2 Database** comme base de données embarquée.
Les tables sont générées automatiquement via JPA / Hibernate.
Si la console H2 est activée, elle peut être accessible via une URL du type : `http://localhost:8080/h2-console`

## 9. Documentation Swagger
La documentation Swagger est accessible à l’adresse : `http://localhost:8080/swagger-ui/index.html`
Swagger permet :
- de visualiser les endpoints disponibles
- de lire leur description
- de tester directement l’API
- d’utiliser le JWT pour les endpoints protégés

## 10. Tests Postman
Une collection Postman est fournie pour tester l’API.
Elle contient les modules suivants :
- Auth
- Categories
- Ingredients
- Pizzas
- Orders
- Filters
- Stats
- HATEOAS

L’ordre conseillé des tests est :
1. Authentification
2. Catégories
3. Ingrédients
4. Pizzas
5. Commandes
6. Filtres
7. Statistiques
8. HATEOAS

## 11. Comptes de test
- **Admin** : compte par défaut
phone: `+261000000000`,
password: `1234`
- **Client** : à créer via inscription
