package View;

import path.navmesh.NavMesh2D;
import path.navmesh.Face;
import math.Vec2;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import path.finder.a_star.Node;

/**
 * 2D Nav Mesh / A* Path Finding Test
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com);
 */
public class View extends JPanel {
    
    private NavMesh2D navMesh;
    private Face start;
    private Face target;
    private List<Node<Vec2>> path = new ArrayList<>();

    private Stroke stroke = new BasicStroke(10
        , BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private boolean drawLinkNodes = false;
    private boolean drawNodes = false;
    
    public View() {
    }
    
    public void start() {
        navMesh = new NavMesh2D();
        addKeyListener(new KeyHandler());
        addMouseListener(new MouseHandler());
        loadNavMesh();
    }

    private void loadNavMesh() {
        try {
            navMesh.load("/res/path_test.obj", 50, 230, 300);
        } catch (Exception ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }
    
    private void draw(Graphics2D g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        // draw all faces
        navMesh.getFaces().forEach(face 
            -> drawFace(g, face, Color.LIGHT_GRAY, true, drawNodes, drawNodes));
        
        drawVertices(g);
        
        if (drawLinkNodes) {
            drawAllLinks(g);
        }
        
        drawStartTarget(g);
        
        if (!path.isEmpty()) {
            drawPath(g);
        }
        
        drawExplanations(g);
    }
    
    private final Polygon polygonTmp = new Polygon();

    public void drawFace(Graphics2D g, Face face
        , Color color, boolean fill, boolean drawP, boolean drawEdges) {
        
        polygonTmp.reset();
        for (Vec2 point : face.getPoints()) {
            polygonTmp.addPoint((int) point.x, (int) point.y);
        }
        
        if (fill) {
            g.setColor(color);
            g.fill(polygonTmp);
        }
        g.setColor(Color.BLACK);
        g.draw(polygonTmp);
        
        if (drawP) {
            g.setColor(Color.MAGENTA);
            g.fillOval((int) (face.getPointInsideFace().x - 3)
                , (int) (face.getPointInsideFace().y - 3), 6, 6);
        }

        if (drawEdges) {
            g.setColor(Color.GREEN);
            for (Vec2 edgeAB : face.getEdgePoints()) {
                g.fillOval((int) (edgeAB.x - 3), (int) (edgeAB.y - 3), 6, 6);
            }
        }
    }
        
    private void drawVertices(Graphics2D g) {
        g.setColor(Color.RED);
        navMesh.getVertices().forEach(vertex 
            -> g.fillOval((int) (vertex.x - 3), (int) (vertex.y - 3), 6, 6));
    }
    
    private void drawAllLinks(Graphics2D g) {
        g.setColor(Color.CYAN);
        for (Face p : navMesh.getFaces()) {
            Node<Vec2> n = p.getNodeInsideFace();
            for (path.finder.a_star.Edge<Vec2> e : n.getEdges()) {
                Vec2 a = e.getA().getObj();
                Vec2 b = e.getB().getObj();
                g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
            }
        }
    }

    public void drawStartTarget(Graphics2D g) {
        if (start != null) {
            g.setColor(Color.GREEN);
            g.fillOval((int) (start.getPointInsideFace().x - 10)
                , (int) (start.getPointInsideFace().y - 10), 20, 20);
        }
        if (target != null) {
            g.setColor(Color.RED);
            g.fillOval((int) (target.getPointInsideFace().x - 10)
                , (int) (target.getPointInsideFace().y - 10), 20, 20);
        }
    }
    
    public void drawPath(Graphics2D g) {
        Stroke os = g.getStroke();
        g.setStroke(stroke);
        
        g.setColor(Color.BLUE);
        
        for (int i = 0; i < path.size() - 1; i++) {
            Node<Vec2> na = path.get(i);
            Node<Vec2> nb = path.get(i + 1);
            Vec2 a = na.getObj();
            Vec2 b = nb.getObj();
            g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
        }
        
        g.setStroke(os);
    }
    

    private void drawExplanations(Graphics2D g) {
        int ex = 520;
        g.setColor(Color.BLACK);
        g.drawString("LEFT MOUSE BUTTON = START", ex, 460);
        g.drawString("RIGHT MOUSE BUTTON = TARGET", ex, 490);
        g.drawString("'N' key - show / hide Nodes", ex, 520);
        g.drawString("'L' key - show / hide Link between Nodes", ex, 550);
    }
    
    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_L) {
                drawLinkNodes = !drawLinkNodes;
            }
            else if (e.getKeyCode() == KeyEvent.VK_N) {
                drawNodes = !drawNodes;
            }
            repaint();
        }
        
    }
    
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                selectStartNode(e.getX(), e.getY());
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                selectTargetNode(e.getX(), e.getY());
            }
            startSearchingPath();
            repaint();
        }
        
    }
    
    private void selectStartNode(int x, int y) {
        for (Face face : navMesh.getFaces()) {
            if (face.isInside(x, y)) {
                if (start != null) {
                    start.updatePointInsideFaceToBarycenter();
                }
                start = face;
                face.getPointInsideFace().x = x;
                face.getPointInsideFace().y = y;
            }
        }
    }

    private void selectTargetNode(int x, int y) {
        for (Face face : navMesh.getFaces()) {
            if (face.isInside(x, y)) {
                if (target != null) {
                    target.updatePointInsideFaceToBarycenter();
                }
                target = face;
                face.getPointInsideFace().x = x;
                face.getPointInsideFace().y = y;
            }
        }
    }
    
    private void startSearchingPath() {
        if (start == null || target == null) {
            return;
        }
        path.clear();
        navMesh.findPath(start.getNodeInsideFace()
            , target.getNodeInsideFace(), path);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setPreferredSize(new Dimension(800, 600));
            JFrame frame = new JFrame();
            frame.setTitle("Java 2D Nav Mesh / A* Path Finding Test");
            frame.getContentPane().add(view);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            view.requestFocus();
            view.start();
        });
    }
    
}
