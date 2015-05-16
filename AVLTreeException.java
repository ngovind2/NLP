// Implementation of AVLTreeExceptions that are thrown by AVLTree class.
//package calculatenr;

public class AVLTreeException extends Exception{
    
    // Constructs exception with null as detail message
    public AVLTreeException() {
        super();
    }
    
    // Constructs exception with specified detail message
    public AVLTreeException(String errMsg) { 
      super(errMsg); 
    }
    
    // Constructs exception with specified cause
    public AVLTreeException(Throwable cause) {
        super(cause);
    }

    // Constructs exception with specified cause and detail message
    public AVLTreeException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
    
} 