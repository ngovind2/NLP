// AVLTree<K,V> implements AVLTreeInterface<K,V>, where K and V are generic types for key and value attributes.

//package calculatenr;

import java.util.Iterator;
import java.util.LinkedList;

public class AVLTree<K,V> implements AVLTreeInterface<K,V> {

	// Instance variables
	private Comparator treeComparator; 
	private Position<K,V> root;
	private int size;


	/*     CONSTRUCTOR     */

	// Constructor initializes an empty tree.
	public AVLTree(Comparator inputComparator) {
		
		root = new AVLnode<K,V>(null, null, null, null);		// Tree initialized with empty root node 
		treeComparator = inputComparator;						// Comparator object to compare keys of entries
		size = 0; 												// Number of entries in tree
	}


	/*     METHODS    */

    // Returns the position of the left child of p.
    public Position<K,V> left(Position<K,V> p) { return ((AVLnode<K,V>) p).left(); }


    // Returns the postition of the right child of p.
    public Position<K,V> right(Position<K,V> p) { return ((AVLnode<K,V>) p).right(); }


	// Returns true if node at position p is external; false otherwise.
    public boolean external(Position<K,V> p) {
    	
    	if (left(p) == null && right(p) == null && p.element() == null)
        	return true;
        
        return false;
    }
    

    // Returns the dictionary entry associated with the input key. If key is not in the tree, returns null.
    public DictEntry<K,V> find(K key) {

    	DictEntry<K,V> entry; 
    	
    	// Perform inorder traversal & store entries in list; get list iterator
    	Iterator<DictEntry<K,V> > iter = inOrder();

    	while (iter.hasNext()) {
			
			// Store each entry in list
			entry = iter.next();

			// Compare each entry's key with argument key & return match if it occurs
			if (treeComparator.compare(entry.key(), key) == 0) 
    			return entry;
    	}
    	// No match found
    	return null; 
    }


    // Delete entry with key K. Throws exception if key is not present in tree.
    public DictEntry<K,V> remove(K key) throws AVLTreeException {

    	DictEntry<K,V> entry = find(key);										// Get entry of node that is to be removed; entry will be returned
    	if (entry == null)														// Throw exception if key is not present in tree (no removal) 
			throw new AVLTreeException("This entry does not exist."); 

    	AVLnode<K,V> node = (AVLnode<K,V>) NodeSearch(key, root);				// Find node containing matching entry
    	AVLnode<K,V> parent = (AVLnode<K,V>) TreeDelete(key, node); 			// Perform deletion & store parent of node that was deleted

	    size--;																	// Decrement size of tree (number of entries)

	    
	    AVLnode<K,V> check = parent;											// Removal is complete; check if height-balance property needs restoring
		while (check != null) {													// Traverse up the tree, beginning right above deletion point
			check.resetHeight(); 												// Reset node's height in case it may have changed 

			if (Math.abs(check.left().getHeight() - check.right().getHeight()) > 1) {		// Compare heights of left & right subtrees	

				check = (AVLnode<K,V>) TriNodeRestructure(tallerChild(tallerChild(check)),tallerChild(check),check); 	// Restructure if height difference > 1 
				
				check.left().resetHeight(); 									// Reset heights for rebalanced node & subtrees 
				check.right().resetHeight();	
				check.resetHeight();
			}
			check = check.parent();												// Continue checking height-balance property for all ancestors
		}
		return entry;															// Return entry of removed node
    }

  
    // Finds the entry with the input key, and changes the value of this entry to valueNew.         
    public void modifyValue(K key, V valueNew) throws AVLTreeException {

    	DictEntry<K,V> entry = find(key);									// Find entry with specified key

    	if (entry == null)													// Throw exception is entry does not exist (not in tree)
    		throw new AVLTreeException("This entry does not exist.");
    	else
    		entry.changeValue(valueNew);									// Reset value of entry

    }
    

    // Inserts new entry in the dictionary. If the entry with the input key already exists in the tree, throws AVLTreeException.                                                             
    public void insertNew(K key, V value) throws AVLTreeException { 

		AVLnode<K,V> node = (AVLnode<K,V>) TreeInsert(key, value);				// Return position for insertion; if null, key already exists in tree
		
		if (node == null)														// Throw exception if key already exists in tree (no insertion) 
			throw new AVLTreeException("This entry already exists."); 
		
		size++;																	// Increment size of tree
		
		AVLnode<K,V> check = node;												// Insertion is complete; check if height-balance property needs restoring
		while (check != null) {													// Traverse up the tree, beginning at insertion point 
			check.resetHeight(); 												// Reset the node's height in case it may have changed 

			if (Math.abs(check.left().getHeight() - check.right().getHeight()) > 1) {	// Compare heights of left & right subtrees
				
				check = (AVLnode<K,V>) TriNodeRestructure(tallerChild(tallerChild(check)),tallerChild(check), check); 	// Restructure if height difference > 1 
				
				check.left().resetHeight(); 									// Reset heights for rebalanced node & subtrees 
				check.right().resetHeight();	
				check.resetHeight();		
				break; 															// Exit while loop since tree is balanced after 1 restructuring								
			}															
			check = check.parent();												// Otherwise, continue checking height-balance property for ancestors
		} 
    }


    // Returns true if no entries in the tree, false otherwise. 
    public boolean isEmpty() { return size == 0; }


    // Returns the root of the tree. 
    public Position<K,V> giveRoot() { return root; }

  
    // Returns the height of the tree.
    public int treeHeight() { return ((AVLnode<K,V>) root).getHeight(); }

    
    // Performs inorder traversal to collect entries & returns an iterator over all entries
    public Iterator<DictEntry<K,V>> inOrder() { 

    	LinkedList<DictEntry<K,V>> entries = new LinkedList<DictEntry<K,V>>(); 	// List stores entries 
		getEntriesinOrder(root, entries);										// Perform inorder traversal & store entries in list
    	return entries.listIterator();											// Return iterator
    }

    
    // Returns the number of entries in the tree.
    public int size() { return size; }



    /*     HELPER METHODS     */

  
  	// Inserts an entry into tree. 
    private Position<K,V> TreeInsert(K key, V value) {

		AVLnode<K,V> node = (AVLnode<K,V>)TreeSearch(key, root);		// Find position for insertion; if null, key already exists in tree
		if (node == null)												
			return null;												// Return null if key already exists in tree (no insertion) 
		insertAtExternal(node, key, value); 							// Insert (key,value) at external node
		return node;													// Return position where insertion has just taken place
	}


	// Removes an entry from tree. 
	private Position<K,V> TreeDelete(K key, Position<K,V> p) {
 		
 		AVLnode<K,V> node = (AVLnode<K,V>) p; 							// Node whose entry will be removed
 		AVLnode<K,V> parentNode;										// Parent of node that will be deleted
	    
	    if (node == null)												// Throw exception if key is not present in tree (no removal) 
			return null;
		
		if (external(node.left())) {									// If left child of node is a leaf
			parentNode = node.parent();
			removeAtExternal(node.left());								// Remove node & left leaf child; relink right child to node's parent
			return parentNode;											// Return parent of internal node that was deleted
		}

		else if (external(node.right())) {								// If right child of node is a leaf
			parentNode = node.parent(); 
			removeAtExternal(node.right());								// Remove node & right leaf child; relink left child to node's parent
			return parentNode;											// Return parent of internal node that was deleted
		}
		else {
																		// If node in question (node1) has internal children,
			DictEntry<K,V> nextEntry = null; 							// swap entries with node that has leaf children (node2) & delete node2
			Iterator<DictEntry<K,V>> iter = inOrder();					// Get iterator over all entries in dictionary
			while (iter.hasNext()) {									// Find entry that follows node1's entry in inorder traversal
				if (node.getEntry() == iter.next() && iter.hasNext())
					nextEntry = iter.next(); 
			}
			node.setEntry(nextEntry);									// Copy this entry into node1; tree order preserved

			AVLnode<K,V> nextNode = (AVLnode<K,V>) NodeSearch(nextEntry.key(), node.right()); 	// Find node2; store its parent
			parentNode = nextNode.parent();						
			removeAtExternal(nextNode.left());							// Delete node2 & its left leaf child
		}

		return parentNode; 												// Return parent of node2 (node that was deleted)

	}


	// Traverses tree, beginning at position p, in search of insertion point (external node).
	private Position<K,V> TreeSearch(K key, Position<K,V> p) {

		if (external(p))												// External node is insertion point (no node contains key yet)
			return p; 															
		else if (treeComparator.compare(key, p.element().key()) < 0)	// Current node has a key larger than argument key; search left subtree
			return TreeSearch(key, left(p));							
		else if (treeComparator.compare(key, p.element().key()) > 0)	// Current node has a key smaller than argument key; search right subtree
			return TreeSearch(key, right(p));							
		else															// Otherwise, current node has a key equal to argument key; key already exists
			return null;												
	}


	// Insert an entry at position p, initially an external node.
	private void insertAtExternal(Position<K,V> p, K key, V value) {

		AVLnode<K,V> node = (AVLnode<K,V>) p; 

		node.setEntry(new DictEntry<K,V>(key, value));					// Store entry in
		node.setLeft(new AVLnode<K,V>(null, node, null, null));			// Create leaf for left child & right child
		node.setRight(new AVLnode<K,V>(null, node, null, null));
	}


	// Remove external node p & its parent node. Relink sibling to grandparent.
	private void removeAtExternal(Position<K,V> node) {
		
		AVLnode<K,V> leaf = (AVLnode<K,V>) node;
		AVLnode<K,V> nodeToRelink;
		
		AVLnode<K,V> p = leaf.parent(); 				

		if (leaf == p.left())							// Determine sibling to relink to grandparent
			nodeToRelink = p.right(); 					
		else 
			nodeToRelink = p.left();

		if (p == root) {								// If node being removed is the root, set new root 
			nodeToRelink.setParent(null);
			root = nodeToRelink;
		}
		else {											// Otherwise, determine how parent is linked to grandparent
			if (p == p.parent().left())					// Replace parent with sibling as grandparent's left/right child
				p.parent().setLeft(nodeToRelink);	
			else 										
				p.parent().setRight(nodeToRelink);
			nodeToRelink.setParent(p.parent());			// Set grandparent as sibling's new parent
		}
	}


	// Returns position of the taller subtree's root.
	private Position<K,V> tallerChild(Position<K,V> p) {

		AVLnode<K,V> node = (AVLnode<K,V>) p;

		if (node.left().getHeight() > node.right().getHeight())			
			return node.left();											// If left subtree is taller, return root of left subtree
		if (node.left().getHeight() < node.right().getHeight())
			return node.right();										// If right subtree is taller, return root of right subtree
		else {															// If heights are equal, return child on same side of tree as the parent
			if (node == node.parent().left())
				return node.left();
			else
				return node.right();
		}										
	}


	// Restructures node & subtrees where an imbalance is found (subtree heights differ by > 1 level).
	// Returns position of the node to which subtrees are now rooted.
	private Position<K,V> TriNodeRestructure(Position<K,V> child, Position<K,V> parent, Position<K,V> grandparent) {
		
		AVLnode<K,V> a = null; AVLnode<K,V> b = null; AVLnode<K,V> c = null;
		AVLnode<K,V> x = (AVLnode<K,V>) child; AVLnode<K,V> y = (AVLnode<K,V>) parent; AVLnode<K,V> z = (AVLnode<K,V>) grandparent;


		// Determine which node will become the new left child (a), parent/subtree root (b), and right child (c)
		if (treeComparator.compare(z.getEntry().key(), x.getEntry().key()) <= 0 )			
			if (treeComparator.compare(x.getEntry().key(), y.getEntry().key()) <= 0 ) 
				{ a = z; b = x; c = y; }

		if (treeComparator.compare(z.getEntry().key(), x.getEntry().key()) >= 0 )
			if (treeComparator.compare(x.getEntry().key(), y.getEntry().key()) >= 0 ) 
				{ a = y; b = x; c = z; }
		
		if (treeComparator.compare(z.getEntry().key(), y.getEntry().key()) <= 0 )
			if (treeComparator.compare(y.getEntry().key(), x.getEntry().key()) <= 0 )
				{ a = z; b = y; c = x; }

		if (treeComparator.compare(z.getEntry().key(), y.getEntry().key()) >= 0 )
			if (treeComparator.compare(y.getEntry().key(), x.getEntry().key()) >= 0 ) 
				{ a = x; b = y; c = z; }
		
		// If imbalance occured at root, new parent is now the root
		if (z == root){							
			root = b; 															
			b.setParent(null);
		}

		// Otherwise, link old parent's parent (z's parent) to node replacing z	
		else {																									
			if (z.parent().left() == z) { 		
				z.parent().setLeft(b);
				b.setParent(z.parent());
			} 
			else { 
				z.parent().setRight(b);
				b.setParent(z.parent());
			}	
		}
			
		//  If new parent was originally the child node (x), reshift its old children to y & z (originally parent & grandparent nodes)
		if (b.left() != x && b.left() != y && b.left() != z) {
			a.setRight(b.left());
			b.left().setParent(a);
		}
		if (b.right() != x && b.right() != y && b.right() != z) {
			c.setLeft(b.right());
			b.right().setParent(c);
		}
		
		// Update parental links; a becomes b's left child and c becomes b's right child
		b.setLeft(a);
		a.setParent(b);

		b.setRight(c);
		c.setParent(b);

		return b; 
	}


	// Traverses tree, beginning at position p, in search of node with specified key.
	private Position<K,V> NodeSearch(K key, Position<K,V> p) {

		if (external(p))												// If external node is reached, key is not in tree
			return null; 
		if (treeComparator.compare(key, p.element().key()) == 0) 		// Key is found in tree
			return p; 														
		else if (treeComparator.compare(key, p.element().key()) < 0)	// Current node has a key larger than argument key
			return NodeSearch(key, left(p));							// Continue searching via left branch
		else															// Current node has a key smaller than argument key
			return NodeSearch(key, right(p));							// Continue searching via left branch		
	}


	// Performs inorder traversal, visiting every node (left child, parent, then right child) and storing its entry in a linked list.
	private void getEntriesinOrder(Position<K,V> node, LinkedList<DictEntry<K,V>> entries) {
 
		AVLnode<K,V> p = (AVLnode<K,V>) node; 

		if (external(p.left()) == false)				// Visit leftmost nodes first
			getEntriesinOrder(p.left(), entries); 		
		
		entries.add(p.element());						// Add each entry to linked list 
		
		if (external(p.right()) == false) 				// Visit rightmost nodes last
			getEntriesinOrder(p.right(), entries);
	}

} // End of class