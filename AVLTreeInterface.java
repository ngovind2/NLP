//package calculatenr;

import java.util.Iterator;
public interface AVLTreeInterface<K,V>{
   
    // true if node at position p is external, false otherwise
    public boolean external(Position<K,V> p);
    
    // returns the postition of the left child of p
    public Position<K,V> left(Position<K,V> p);
    
    // returns the postition of the right child of p
    public Position<K,V> right(Position<K,V> p);
    
    // Returns the dictionary entry associated with the input key. If key is not in the tree returns null 
    public DictEntry<K,V> find(K key);

    // Delete entry with key K. Throws exception if key  is not present in the tree
    public DictEntry<K,V> remove(K key) throws AVLTreeException;
   
    // Finds the entry with the input key, and changes the value of this entry to valueNew 
    // If the entry with input key is not in the tree, throws AVLtreeException          
    public void modifyValue(K key, V valueNew ) throws AVLTreeException;
 	
    // inserts new entry in the dictionary. If the entry with the input key already exists in the 
    // tree, throws AVLTreeException                                                             
    public void insertNew(K key, V value) throws AVLTreeException;

    // Returns true if no entries in the tree, false otherwise 
    public boolean isEmpty();

    // Returns the root of the tree. 
    public Position<K,V> giveRoot();
  
    // returns the height of the tree
    public int treeHeight();
    
    // returns an iterator over all entries in the dictionary
    // "inOrder" traversal is performed
    public Iterator<DictEntry<K,V>> inOrder();
    
    // returns the number of entries in the tree
    public int size();
}


