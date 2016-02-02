//Kelsey Maclin

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class LazyDeleteLinkedList<T> implements LazyDeleteList<T>, Iterable<T> {
	
    private class Node<T> {
    	
    	private Node<T> next;
    	private Node<T> prev;
    	private boolean valid;
    	private T data;
    	
    	public Node(T d) {
    		data = d;
    		valid = true;
    		next = null;
    		prev = null;
    	}
    }
    
    private class LazyListIterator<T> implements Iterator<T> {
    	
    	private Node<T> pointer;
    	
    	public LazyListIterator() {
    		pointer = (Node<T>) head;
    	}
    	
    	public boolean hasNext() {
    		return pointer != null;
    	}
    	
    	public T next() {
    		if (hasNext()) {
    			T ret = pointer.data;
    			pointer = pointer.next;
    			return ret;
    		} else {
    			throw new NoSuchElementException();
    		}
    	}
    	
    	public void remove() {
    		throw new UnsupportedOperationException();
    	}
    }
    
    private Node<T> head, tail;
    private int count = 0, countdlt = 0;
    private Stack<Node<T>> stack;
    
	public LazyDeleteLinkedList() {
		head = null;
		tail = null;
		Stack<Node<T>> s = new Stack<>();
		stack = s;
	}
	
     public boolean isEmpty() {
    	 return count == 0;
     }
     
     public int size() {
    	 return count;
     }
     
     /**
      * Add a new element to the list.  This element is placed into a deleted node,
      * or if none exists at the end of the list.
      * @param data the data element to add to the list
      */
     public void add(T data) {
    	 if (!(stack.empty())) {
    		 Node<T> node = stack.pop();
    		 node.valid = true;
    		 node.data = data;
    		 countdlt--;
    		 count++;
    	 } else {	 
    		 Node<T> node = new Node<>(data);   	 
    		 if (head == null) {
    		     head = node;
    		     tail = node;
    		 } else {
    		     tail.next = node;
    		     node.prev = tail;
    		     tail = node;
    		 }
    		 count++;
    	 }
     } 	 
     
     /**
      * Remove all the deleted nodes and ensure list contains only undeleted nodes.
      * @return the number of nodes removed (should count all nodes 
      *              that are removed from the list
      */
     
     public int compress() {
    	//do contains method also. delete all the javadocs.
    	for (int i = 0; i < countdlt; i++) {
    		Node<T> dltNode = stack.pop();   		
    		if (head == tail) {
    			head = null;
    			tail = null;
    		} else if (dltNode.prev == null) {
    			head = dltNode.next;
    			(dltNode.next).prev = null;
    		} else if (dltNode.next == null) {
    			tail = dltNode.prev;
    			(dltNode.prev).next = null;
    		} else {
    			(dltNode.prev).next = dltNode.next;
    			(dltNode.next).prev = dltNode.prev;
    		}
    	}
    	int finalcount = countdlt;
    	countdlt = 0;
    	return finalcount;
     }
     
     /**
      * Remove everything from the list
      */
     
     public void clear() {
    	 head = null;
    	 tail = null;
    	 count = 0;
    	 countdlt = 0;
    	 stack = new Stack<Node<T>>();
     }
     
     /**
      * Checks whether the list contains a certain value
      * @param data the item to check for
      * @return true if list has this item and it is undeleted, false otherwise
      */
     public boolean contains(T data) {
    	 Node<T> current = head;
    	 
    	 while (current != null) {
    		 if ((current.data).equals(data) && current.valid == true) {
    			 return true;
    		 }
    		 current = current.next;
    	 }
    	 return false;
     }
     
     /**
      * Removes an item from the list using lazy deletion (the node is marked deleted, but
      * not actually removed.  If duplicate items are present, this removes the first one found.
      * 
      * @param data the item being deleted
      * @return true if item was in the list and undeleted, false otherwise.
      */
     
    public boolean remove(T data) {
        Node<T> current = head;
        
    	while (current != null) {
    		 if ((current.data).equals(data)) {
    			 if (current.valid != false) {
    			     current.valid = false;
    			     stack.push(current);
    			     countdlt++;
    			     count--;
    			     return true;
    			 }
    		 }
    		 current = current.next;
    	}
    	return false;
    }
     
     /**
      * 
      * @return the number of deleted nodes in the list that are available for use
      */
    public int deletedNodeCount() {
    	 return countdlt;
    }
     
     /**
      * @return the iterator for this collection.  Asking for an iterator causes a compress of the collection.
      */
    public Iterator<T> iterator() {
    	 LazyListIterator<T> iter = new LazyListIterator<T>();
    	 return iter;
    }
}
