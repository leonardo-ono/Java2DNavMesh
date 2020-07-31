package path.finder.a_star;

import java.util.ArrayList;
import java.util.List;

/**
 * Node class.
 * 
 * @param <T>
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Node<T> implements Comparable<Node> {

    public static enum State { UNVISITED, OPEN, CLOSED };
    
    private final T obj;
    private State state = State.UNVISITED;
    private boolean blocked = false;
    
    private double g; // cost
    private double h; // heuristic
                      // f = g + h
    
    private Node backPathNode;
    private final List<Edge<T>> edges = new ArrayList<>();

    public Node(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public State getState() {
        return state;
    }

    void setState(State state) {
        this.state = state;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public double getG() {
        return g;
    }

    void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    void setH(double h) {
        this.h = h;
    }

    public Node getBackPathNode() {
        return backPathNode;
    }

    public void setBackPathNode(Node backPathNode) {
        this.backPathNode = backPathNode;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    // f(n) = g(n) + h(n) -> cost + heuristic
    public double getF() {
        return g + h;
    }

    public void retrievePath(List<Node<T>> path) {
        if (backPathNode != null) {
            backPathNode.retrievePath(path);
        }
        path.add(this);
    }
    
    @Override
    public int compareTo(Node o) {
        double dif = getF() - o.getF();
        return dif == 0 ? 0 : dif > 0 ? 1 : -1;
    }    

    @Override
    public String toString() {
        return "Node{" + "id=" + obj + ", state=" + state + ", g=" + g + ", h=" 
            + h + ", parentNode=" + backPathNode + ", edges=" + edges + '}';
    }
    
}
