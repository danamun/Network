package player.list;

import java.util.ArrayList;

/**
 * a mutable doubly linked list (no sentinel, not circular)
 * @author Paymon
 */
public class List<T> {
    
    protected Node<T> head;
    protected Node<T> tail;
    
    public long size;
    
    public List() {
        size = 0;
    }

    /**
     *  DList1() constructor for a one-node DList1.
     */
    public List(Object a) {
      head = new Node(a);
      tail = head;
      size = 1;
    }

    /**
     *  insertFront() inserts an item at the front of a DList1.
     */
    public void insertFront(Object i) {
      if (head == null) {
          head = new Node(i);
          tail = head;
      }
      else {
          Node<T> front = new Node(i);
          front.next = head;
          head.prev = front;
          head = front;
      }
      size++;
    }

    /**
     *  removeFront() removes the first item (and node) from a DList1.  If the
     *  list is empty, do nothing.
     */
    public void removeFront() {
      if (size == 1) {
          head = null;
          tail = null;
          size = 0;
      }
      else if (size > 1) {
          head = head.next;
          head.prev = null; 
          size--;
      }
    }

    public void insertEnd(Object i) {
        if (tail == null) {
            // might as well
            insertFront(i);
        }
        else {
          Node<T> end = new Node(i);
          tail.next = end;
          end.prev = tail;
          tail = end;

          size++;
        }
    }

    public void insertAfter(Node n1, Node n2) {
          n1.next.prev = n2;
          n2.prev = n1;
          n2.next = n1.next;
          n1.next = n2;
          size++;
    }
    
    /*
     * returns the object at index i
     */
    public T get(int i) {
        if (i < 0 || i > size-1) {
            throw new IndexOutOfBoundsException("i = " + i);
        }
        
        Node<T> curr = head;
        
        while (i > 0) {
            i--;
            curr = curr.next;
        }
        return curr.item;
    }
}
