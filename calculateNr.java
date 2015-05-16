// Prints the number of words, from a given text file, that occur at varying frequencies (ie. N(r))
//package calculatenr;

import java.util.Iterator;

public class calculateNr {
	public static void main(String[] args) throws java.io.IOException, AVLTreeException {

		/*     FIRST TREE TO HOLD WORD-FREQUENCY ENTRIES     */

		AVLTree<String, Integer> t1 = new AVLTree<String, Integer>(new StringComparator());		
		
		FileWordRead allWords = null;							
		
		try {													
			allWords = new FileWordRead(args[0]);				// Get all words of file as a token sequence
		} catch (java.io.IOException e){ 
	        System.err.println("Error reading in file.");		// Exit program if IOException is thrown
	        System.exit(1); 
	    }

		Iterator<String> iter1 = allWords.getIterator();		// Get iterator for token sequence
		
		String word; 											
		DictEntry<String, Integer> entry; 						

		while (iter1.hasNext()) { 								// While there is a word in the sequence...
			word = iter1.next(); 								// Get the word
			
			try { 
				t1.insertNew(word, 1);							// Insert the word into the tree (initially, word has frequency of 1)
			} catch (AVLTreeException e) {						// If word already exists in tree, no insertion will occur (exception is thrown)
				entry = t1.find(word);							// Instead, find the existing entry & increment its frequency
				entry.changeValue(entry.value() + 1);			
			}		
		}


		/*     SECOND TREE TO HOLD FREQUENCY-N(r) ENTRIES     */

		AVLTree<Integer, Integer> t2 = new AVLTree<Integer, Integer>(new IntegerComparator());

		Iterator<DictEntry<String,Integer>> iter2 = t1.inOrder();	//  Get iterator over all entries in first tree (t1)

		DictEntry<String, Integer> entryT1; 						
		DictEntry<Integer, Integer> entryT2; 						

		while (iter2.hasNext() && !t1.isEmpty()) {					// While there's an entry in t1...
			entryT1 = iter2.next(); 								// Get the entry
			
			try { 
				t2.insertNew(entryT1.value(), 1);					// Insert the entry into t2 (initially, only 1 word has this frequency)
			} catch (AVLTreeException e) {							// If frequency already exists in tree, no insertion will occur
				entryT2 = t2.find(entryT1.value());					// Instead, find the existing entry in t2 & increment its value
				entryT2.changeValue(entryT2.value() + 1);			
			}

			try { 
				t1.remove(entryT1.key());							// After insertion into t2, remove the entry from t1
			} catch (AVLTreeException e){							// Exit program if AVLTreeException is thrown
				System.err.println("Error in removing entry.");
				System.exit(1);
			} 
		}

		/*     PRINT FREQUENCY-N(r) ENTRIES     */

		Iterator<DictEntry<Integer,Integer>> iter3 = t2.inOrder();	//  Get iterator over all entries in second tree (t2)
		DictEntry<Integer, Integer> nr;

		while (iter3.hasNext()) {

			nr = iter3.next();
			System.out.println("N(" + nr.key() + ") = " + nr.value());		// Print each key-value pair
		}

	} // End of main
} // End of class