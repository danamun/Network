package player.list;

/**
 *
 * @author Paymon
 */
public class Node<T> {
    public T item;
    public Node<T> next;
    public Node<T> prev;
    
    public Node(T item) {
        this.item = item;
    }
}
