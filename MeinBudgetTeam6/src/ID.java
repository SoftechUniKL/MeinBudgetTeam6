import java.util.concurrent.atomic.AtomicLong;

public class ID {
	    private static long counter = 0;
	 
	    public static long nextId() {
	        return ++counter;    
	    }
	    
	    public static void setBId( long n){
	    	counter = n;
	    }
}
