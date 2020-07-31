package path.navmesh;

import math.Vec2;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import path.finder.a_star.Graph;
import path.finder.a_star.Node;

/**
 * NavMesh2D class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class NavMesh2D {
    
    private Graph<Vec2> graph;
    private final List<Vec2> vertices = new ArrayList<>();
    private final List<Face> faces = new ArrayList<>();
    private final Map<Edge, Node<Vec2>> edges = new HashMap<>();
    
    private double scaleFactor;
    private double translateX;
    private double translateY;

    private static final Edge EDGE_KEY = new Edge(null, null);

    public NavMesh2D() {
    }

    public Graph<Vec2> getGraph() {
        return graph;
    }

    public List<Vec2> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public Map<Edge, Node<Vec2>> getEdges() {
        return edges;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }
    
    public void load(String meshRes, double scaleFactor
            , double translateX, double translateY) throws Exception {
        
        graph = new Graph<>((start, target, current) -> {
            // heuristic function = linear distance
            double dx = target.getObj().x - current.getObj().x;
            double dy = target.getObj().y - current.getObj().y;
            return Math.sqrt(dx * dx + dy * dy);
        });
        
        vertices.clear();
        faces.clear();
        edges.clear();
        
        this.scaleFactor = scaleFactor;
        this.translateX = translateX;
        this.translateY = translateY;
        
        InputStream is = NavMesh2D.class.getResourceAsStream(meshRes);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("v ")) {
                parseVertex(line);
            }
            else if (line.startsWith("f ")) {
                parseFace(line);
            }
        }
        br.close();
        faces.forEach(face -> graph.addNode(face.getNodeInsideFace()));
        linkAllNodes();
    }

    private void linkAllNodes() {
        for (Face p : faces) {
            for (Node<Vec2> node : p.getEdgeNodes()) {
                Node<Vec2> a = node;
                Node<Vec2> b = p.getNodeInsideFace();
                double dx = a.getObj().x - b.getObj().x;
                double dy = a.getObj().y - b.getObj().y;
                double g = Math.sqrt(dx * dx + dy * dy);
                graph.link(a, b, g);
            }
        }
    }

    private void parseVertex(String line) {
        String[] data = line.split(" ");
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[3]);
        double stx = x * scaleFactor + translateX;
        double sty = y * scaleFactor + translateY;
        Vec2 v = new Vec2(stx, sty);
        vertices.add(v);
    }

    private void parseFace(String line) {
        String[] data = line.split(" ");
        List<Vec2> ps = new ArrayList<>();
        for (int i = 1; i < data.length; i++) {
            String[] data2 = data[i].split("/");
            int index = Integer.parseInt(data2[0]);
            ps.add(vertices.get(index - 1));
        }
        Face face = new Face(this, ps);
        faces.add(face);
    }

    public void findPath(Node<Vec2> start
            , Node<Vec2> target, List<Node<Vec2>> path) {
        
        path.clear();
        graph.findPath(start, target, path);
    }

    public static class Edge {

        public Vec2 a;
        public Vec2 b;

        public Edge(Vec2 a, Vec2 b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            // TODO: improve this later ?
            int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Edge other = (Edge) obj;
            if (Objects.equals(this.a, other.a) 
                    && Objects.equals(this.b, other.b)) {
                
                return true;
            }
            if (Objects.equals(this.a, other.b) 
                    && Objects.equals(this.b, other.a)) {
                
                return true;
            }
            return false;
        }
        
    }
    
    public Node<Vec2> getEdge(Vec2 a, Vec2 b) {
        EDGE_KEY.a = a;
        EDGE_KEY.b = b;
        
        if (edges.containsKey(EDGE_KEY)) {
            return edges.get(EDGE_KEY);
        }
        else {
            Edge edgeKey = new Edge(a, b);
            Vec2 v = new Vec2(0, 0);
            Node<Vec2> n = new Node(v);
            graph.addNode(n);
            edges.put(edgeKey, n);
            return n;
        }
    }

    @Override
    public String toString() {
        return "NavMesh2D{" + "graph=" + graph + ", vertices=" + vertices 
            + ", faces=" + faces + ", edges=" + edges + ", scaleFactor=" 
            + scaleFactor + ", translateX=" + translateX + ", translateY=" 
            + translateY + '}';
    }
    
}
