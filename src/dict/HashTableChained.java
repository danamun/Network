/* HashTableChained.java */

package dict;

import player.Move;
import list.DList;
import list.DListNode;
import list.InvalidNodeException;
import list.ListNode;


/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  protected DList<Move>[] bucket;
  protected int N;
  protected int n;



  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
	  //load factor n/N, makes the size of bucket a little bigger than the size estimate.
	  N = sizeEstimate + sizeEstimate/4;
	  
	  //makes N a prime number
	  while(N%2 == 0 || N%3 == 0){
		  N++;
	  }
	  
	  //creates an empty DList as each entry. 
	  n = 0;
	  bucket = new DList[N];
	  for(int i = 0; i < N; i++){
		  bucket[i] = new DList<Move>();
	  }
  }
  

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    N = 101;
    n = 0;
    
    bucket = new DList[N];
    for (int i = 0; i < N; i++){
    	bucket[i] = new DList<Move>();
    }
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
	int p = 16908799;
    int a = (int)(217 * code) % p;
    int b = (int)(217 * code) % p;
    int g = (((a * code) + b) %p ) % N;
    return Math.abs(g); 
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return n;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return n == 0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
	  int x = key.hashCode();
	  x = compFunction(x);
	 
	  Entry entry = new Entry();
	  entry.key = key;
	  entry.value = value;
	  
	  bucket[x].insertFront(entry);
	  
	  n++;
    return entry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    int x = key.hashCode();
    x = compFunction(x);
    ListNode tracer = bucket[x].front();
    
    try{
    while(tracer != null){
    	Entry entry = (Entry) tracer.item();
    	if(entry.key.equals(key)){
    		return entry;
    	}
    	tracer = tracer.next();
    }
    }
    catch(InvalidNodeException e){}
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
	  int x = key.hashCode();
	    x = compFunction(x);
	    ListNode tracer = bucket[x].front();
	    
	    try{
	    while(tracer != null){
	    	Entry entry = (Entry) tracer.item();
	    	if(entry.key.equals(key)){
	    		tracer.remove();
	    		n--;
	    		return entry;
	    	}
	    	tracer = tracer.next();
	    }
	    }
	    catch(InvalidNodeException e){}
	    return null;
	  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    for(int i = 0; i < N; i++){
    	bucket[i] = new DList<Move>();
    }
    n = 0;
  }
  
  public int collisions(){
	  int x = 0;
	  for(int i = 0; i < N; i++){
		  if(bucket[i].length() > 1){
			  
			  x++;
		  }
	  }
	  return x;
  }

}