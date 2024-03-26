import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class GraphGUI extends JFrame implements MouseListener, ActionListener {

    private JRadioButton addvertex;
    private JRadioButton addedge;
    private JRadioButton removevertex;
    private JRadioButton removeedge;
    private JRadioButton movevertex;
    private JPanel panel;
    JButton help, addalledges;

    private ArrayList<Vertex> vertexes;
    private ArrayList<Line> lines;
    private Vertex old, current;

    private int diameter = 10;


    public GraphGUI() {
        super("Graph GUI");

        vertexes = new ArrayList<Vertex>();
        lines = new ArrayList<Line>();

        addMouseListener(this);
        panel = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        addvertex = new JRadioButton();
        addvertex.setText("Add vertex");

        addedge = new JRadioButton();
        addedge.setText("Add Edge");

        removevertex = new JRadioButton();
        removevertex.setText("Remove Vertex");
        removeedge = new JRadioButton();
        removeedge.setText("Remove Edge");
        movevertex = new JRadioButton();
        movevertex.setText("Move Vertex");
        addalledges = new JButton("Add All Edges");
        addalledges.addActionListener(this);
        JButton connectedcomponents = new JButton("Connected Components");
        JButton showcutvertices = new JButton("Show Cut Vertices");
        help = new JButton("Help");
        help.addActionListener(this);
        ButtonGroup radiogroup = new ButtonGroup();
        radiogroup.add(addvertex);
        radiogroup.add(addedge);
        radiogroup.add(removevertex);
        radiogroup.add(removeedge);
        radiogroup.add(movevertex);
        radiogroup.add(addalledges);
        radiogroup.add(connectedcomponents);
        radiogroup.add(showcutvertices);
        radiogroup.add(help);

        panel.add(addvertex);
        panel.add(addedge);
        panel.add(removevertex);
        panel.add(removeedge);
        panel.add(movevertex);
        panel.add(addalledges);
        panel.add(connectedcomponents);
        panel.add(showcutvertices);
        panel.add(help);

        getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        setVisible(true);

    }

    public void showHelp() {
        JFrame f = new JFrame("Help");
        f.setSize(500, 500);
        f.setVisible(true);
        JLabel label = new JLabel("add vertex to add a point and connect edges");
        label.setSize(500, 500);
        label.setLocation(0, 0);
        f.add(label);

    }

    public void paint(Graphics g) { //This basically helps with creating the dots for the vertexes
        super.paintComponents(g);
        g.setColor(Color.red);

        for (Vertex v : vertexes) {
            g.fillOval(v.x - (diameter / 2), v.y - (diameter / 2), diameter, diameter);
        }
        if (lines.size() != 0) {
            for (Line l : lines) {
                g.drawLine(l.ver1.x, l.ver1.y, l.ver2.x, l.ver2.y);
            }
        }

        if (old != null && movevertex.isSelected()){
            g.setColor(Color.GREEN);
            g.fillOval(old.x - (diameter / 2), old.y - (diameter / 2), diameter, diameter);
        }
    }

    public static void main(String args[]) { //main method
        new GraphGUI();

    }

    public double getDist(Vertex v1, Vertex v2) {
        return Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y)); //distance formula given 2 vertexes
    }

    public void mouseClicked(MouseEvent m) {  // This method gets called when the user clicks the panel

        if (addvertex.isSelected()) { // this helps us to add vertexes thru the radio button addvertex
            vertexes.add(new Vertex(m.getX(), m.getY()));
        }

        if (removevertex.isSelected()) {
            int x = m.getX();
            int y = m.getY();
            Vertex c = new Vertex(x, y);
            int threshold = diameter;
            double smallest = 10000;
            int toRemove = -1;

            for (int i = 0; i < vertexes.size(); i++) {
                double dist = getDist(c, vertexes.get(i));

                if (dist < smallest) {
                    smallest = dist;
                    toRemove = i;
                }
            }

            if (smallest < threshold) {
                Vertex rem = vertexes.get(toRemove);
                vertexes.remove(toRemove);

                Iterator<Line> i = lines.iterator(); //Helps with the edge

                while (i.hasNext()) {
                    Line e = i.next();
                    if (e.ver1.equals(rem) || e.ver2.equals(rem)) {
                        i.remove();
                    }
                }

                repaint();

            }

        }

        if (addedge.isSelected()) {

            int x = m.getX();
            int y = m.getY();

            Vertex c = new Vertex(x, y);

            int threshold = diameter;
            double smallest = 10000;
            int toRemove = -1;

            for (int i = 0; i < vertexes.size(); i++) {
                double dist = getDist(c, vertexes.get(i));

                if (dist < smallest) {
                    smallest = dist;
                    toRemove = i;
                }
            }

            Vertex cur = null;
            if (smallest < threshold) {
                cur = vertexes.get(toRemove);
            }

            if (cur == null) {
                return;
            }

            old = current;
            current = cur;

            if (old != null && current != null) {
                if (!old.equals(current)) {
                    boolean found = false;
                    for (int i = 0; i < lines.size(); i++) {

                        if (lines.get(i).Exists(old, current)) {
                            found = true;
                        }

                    }

                    if (!found) {
                        lines.add(new Line(old, current));
                    }

                }
            }

        }

        if (removeedge.isSelected()) {
            int x = m.getX();
            int y = m.getY();

            Vertex c = new Vertex(x, y);

            int threshold = diameter;
            double smallest = 10000;
            int toRemove = -1;

            for (int i = 0; i < vertexes.size(); i++) {
                double dist = getDist(c, vertexes.get(i));

                if (dist < smallest) {
                    smallest = dist;
                    toRemove = i;
                }
            }

            Vertex cur = null;
            if (smallest < threshold) {
                cur = vertexes.get(toRemove);
            }

            if (cur == null) {
                return;
            }

            old = current;
            current = cur;


            if (old != null && current != null) {
                if (!old.equals(current)) {
                    boolean found = false;
                    int n = -1;
                    for (int i = 0; i < lines.size(); i++) {

                        if (lines.get(i).Exists(old, current)) {
                            found = true;
                            n = i;
                            break;
                        }

                    }

                    if (found) {
                        lines.remove(n);
                    }

                }
            }

        }

        if (movevertex.isSelected()){
            int x = m.getX();
            int y = m.getY();

            Vertex c = new Vertex(x, y);

            if (old != null){
                old.x = x;
                old.y = y;
                old = null;
                repaint();
                return;
            }

            int threshold = diameter;
            double smallest = 10000;
            int toRemove = -1;

            for (int i = 0; i < vertexes.size(); i++) {
                double dist = getDist(c, vertexes.get(i));

                if (dist < smallest) {
                    smallest = dist;
                    toRemove = i;
                }
            }

            Vertex cur = null;
            if (smallest < threshold) {
                cur = vertexes.get(toRemove);
            }

            if (cur == null) {
                return;
            }

            if (old == null){
                old = cur;
            }
        }

        repaint();
    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void mousePressed(MouseEvent test) {
    }

    public void mouseReleased(MouseEvent gk) {
    }

    class Vertex {

        int x;
        int y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object m) {
            if (!(m instanceof Vertex)) {
                return false;
            }

            Vertex v = (Vertex) m;
            return v.x == x && v.y == y;
        }

    }

    class Line {

        Vertex ver1, ver2;

        public Line(Vertex ver1, Vertex ver2) {
            this.ver1 = ver1;
            this.ver2 = ver2;

        }

        public boolean Exists(Vertex v1, Vertex v2) {//check if v1 is equal to v2 and
            return (ver1.equals(v1) && ver2.equals(v2)) || (ver1.equals(v2) && ver2.equals(v1));
        }
    }

    public void actionPerformed(ActionEvent e) {

        //help button is clicked
        if (e.getSource().equals(help)) {
            showHelp();

        }

        if (e.getSource().equals(addalledges)) {

            for (int i = 0; i < vertexes.size() - 1; i++) {
                for (int j = i + 1; j < vertexes.size(); j++) {
                    lines.add(new Line(vertexes.get(i), vertexes.get(j)));
                }
            }

            repaint();
        }

    }

}

//get panel east side, edge class , vertex class , move vertex class and then remove part.
//done



