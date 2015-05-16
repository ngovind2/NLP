// Dictionary entries that hold key-value pairs of generic type.
//package calculatenr;

public class DictEntry<K,V> {

	// Instance variables
	private K key; 
	private V value;
	
	// Constructor
	public DictEntry(K key, V value) {
		this.key = key;
		this.value = value; 
	}

	// Returns entry's key
	public K key() {
		return this.key;
	} 

	// Returns entry's value
	public V value() {
		return this.value;
	}

	// Sets new value for specified entry
	public void changeValue(V newVal) {
		this.value = newVal;
	}

}