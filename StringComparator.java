// StringComparator implements the Comparator interface. It compares two String objects based on lexographical order.
//package calculatenr;

// Returns negative integer if a < b, 0 if a = b, and positive integer if a > b.
public class StringComparator implements Comparator {
	
	public int compare(Object a, Object b) throws ClassCastException {
		
        // Cast arguments into String type
        String obj1 = (String) a; 
		String obj2 = (String) b;

        // If either string is null, assume null < string
    	if (obj1 == null) return -1;
    	else if (obj2 == null)  return 1;
    	
        // Otherwise, perform comparison based on lexigraphical order
        // Negative integer if a < b; 0 if a = b; positive integer if a > b
        else return obj1.compareTo(obj2);

	}

}