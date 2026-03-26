package mg.pizza.wsrest.exception;

public class PizzaUnavailableException extends RuntimeException{
    public PizzaUnavailableException(String message){
        super(message);
    }    
}
