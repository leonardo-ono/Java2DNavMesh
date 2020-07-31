package path.finder.a_star;

/**
 * Edge class.
 * 
 * @param <T>
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Edge<T> {

    private double g;
    private final Node<T> a;
    private final Node<T> b;

    public Edge(double g, Node<T> a, Node<T> b) {
        this.g = g;
        this.a = a;
        this.b = b;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public Node<T> getA() {
        return a;
    }

    public Node<T> getB() {
        return b;
    }
    
    public Node<T> getOppositeNode(Node<T> thisNode) {
        if (thisNode == a) {
            return b;
        }
        else if (thisNode == b) {
            return a;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Edge{" + "g=" + g + ", a=" + a + ", b=" + b + '}';
    }
    
}
