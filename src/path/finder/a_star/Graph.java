package path.finder.a_star;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import static path.finder.a_star.Node.State.*;

/**
 * Graph class.
 * 
 * @param <T>
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Graph<T> {

    private final List<Node<T>> nodes = new ArrayList<>();
    private final Heuristic<T> heuristic;
    
    public Graph(Heuristic<T> heuristic) {
        this.heuristic = heuristic;
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public List<Node<T>> getNodes() {
        return nodes;
    }

    public Heuristic<T> getHeuristic() {
        return heuristic;
    }
    
    public void link(Node a, Node b, double cost) {
        Edge edge = new Edge(cost, a, b);
        a.addEdge(edge);
        b.addEdge(edge);
    }
    
    public void findPath(Node<T> start, Node<T> target, List<Node<T>> path) {
        nodes.forEach(node -> {
            node.setState(UNVISITED);
            node.setBackPathNode(null);
            node.setG(Double.MAX_VALUE);
        });
        
        start.setG(0);
        start.setH(heuristic.calculate(start, target, start));
        
        PriorityQueue<Node<T>> openNodes = new PriorityQueue<>();
        openNodes.add(start);
        
        while (!openNodes.isEmpty()) {
            Node<T> currentNode = openNodes.poll();
            currentNode.setState(CLOSED);
            
            // target node found !
            if (currentNode == target) {
                path.clear();
                target.retrievePath(path);
                return;
            }            
            
            currentNode.getEdges().forEach((edge) -> {
                Node<T> neighborNode = edge.getOppositeNode(currentNode);
                double neighborG = currentNode.getG() + edge.getG();
                if (!neighborNode.isBlocked() 
                        && neighborG < neighborNode.getG()) {
                    
                    neighborNode.setBackPathNode(currentNode);
                    neighborNode.setG(neighborG);
                    double h = heuristic.calculate(start, target, neighborNode);
                    neighborNode.setH(h);
                    if (!openNodes.contains(neighborNode)) {
                        openNodes.add(neighborNode);
                        neighborNode.setState(OPEN);
                    }
                }
            });
        }
    }
    
}
