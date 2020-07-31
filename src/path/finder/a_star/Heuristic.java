package path.finder.a_star;

/**
 * Heuristic functional interface.
 * 
 * @param <T>
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public interface Heuristic<T> {
   
    public double calculate(Node<T> start, Node<T> target, Node<T> current);
    
}
