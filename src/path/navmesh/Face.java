package path.navmesh;

import math.Vec2;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import path.finder.a_star.Node;

/**
 * Face class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Face {

    private final NavMesh2D navMesh;
    
    private final List<Vec2> points = new ArrayList<>();

    private final Vec2 pointInsideFace = new Vec2(0, 0);
    private final List<Vec2> edgePoints = new ArrayList<>();
    
    private Node<Vec2> nodeInsideFace;
    private final List<Node<Vec2>> edgeNodes = new ArrayList<>();

    private final Polygon polygonTmp = new Polygon();
    
    public Face(NavMesh2D navMesh, List<Vec2> ps) {
        this.navMesh = navMesh;
        points.addAll(ps);
        create();
    }

    private void create() {
        updatePointInsideFaceToBarycenter();
        nodeInsideFace = new Node<>(pointInsideFace);

        for (int i = 0; i < points.size(); i++) {
            Vec2 a = points.get(i);
            Vec2 b = points.get((i + 1) % points.size());
            Node<Vec2> nodeEdge = navMesh.getEdge(a, b);
            Vec2 pointEdge  = nodeEdge.getObj();
            edgeNodes.add(nodeEdge);
            edgePoints.add(pointEdge);
            pointEdge.x = a.x / 2 + b.x / 2;
            pointEdge.y = a.y / 2 + b.y / 2;
        }
    }
    
    public List<Vec2> getPoints() {
        return points;
    }

    public Vec2 getPointInsideFace() {
        return pointInsideFace;
    }

    public List<Vec2> getEdgePoints() {
        return edgePoints;
    }

    public Node<Vec2> getNodeInsideFace() {
        return nodeInsideFace;
    }

    public List<Node<Vec2>> getEdgeNodes() {
        return edgeNodes;
    }
    
    public void updatePointInsideFaceToBarycenter() {
        pointInsideFace.x = 0;
        pointInsideFace.y = 0;
        for (int i = 0; i < points.size(); i++) {
            Vec2 vertex = points.get(i);
            pointInsideFace.x += vertex.x / points.size();
            pointInsideFace.y += vertex.y / points.size();
        }
    }
    
    public boolean isInside(double x, double y) {
        polygonTmp.reset();
        for (Vec2 point : points) {
            polygonTmp.addPoint((int) point.x, (int) point.y);
        }
        return polygonTmp.contains(x, y);
    }

    @Override
    public String toString() {
        return "Face{" + "navMesh=" + navMesh + ", points=" + points 
            + ", pointInsideFace=" + pointInsideFace + ", edgePoints=" 
            + edgePoints + ", nodeInsideFace=" + nodeInsideFace 
            + ", edgeNodes=" + edgeNodes + ", polygonTmp=" + polygonTmp + '}';
    }
    
}
