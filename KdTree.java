/* *****************************************************************************
 *  Name: Junwoo Lee
 *  Date: 6/3/2020
 *  Description: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 *  I have done all the coding by myself
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;
    private Point2D nearP;


    private class Node {
        private Point2D p;
        public Node lb;
        public Node rt;
        public RectHV rectHV;

        private Node(Point2D p) {
            this.p = p;
        }

    }


    public KdTree() {
        root = null;
        size = 0;

    }                                              // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }               // is the set empty?

    public int size() {
        return size;
    }                       // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(p);
            root.rectHV = new RectHV(0, 0, 1, 1);
            size++;
        }
        else insertbyX(p, root);

    }          // add the point to the set (if it is not already in the set)

    private void insertbyX(Point2D p, Node node) {

        if (p.x() < node.p.x()) {
            if (node.lb == null) {
                node.lb = new Node(p);
                node.lb.rectHV = new RectHV(node.rectHV.xmin(), node.rectHV.ymin(), node.p.x(),
                                            node.rectHV.ymax());
                size++;
            }
            else insertbyY(p, node.lb);
        }
        if (p.x() >= node.p.x()) {
            if (p.equals(node.p)) ;
            else if (node.rt == null) {
                node.rt = new Node(p);
                node.rt.rectHV = new RectHV(node.p.x(), node.rectHV.ymin(), node.rectHV.xmax(),
                                            node.rectHV.ymax());
                size++;
            }

            else insertbyY(p, node.rt);

        }
    }

    private void insertbyY(Point2D p, Node node) {

        if (p.y() < node.p.y()) {
            if (node.lb == null && !node.p.equals(p)) {
                node.lb = new Node(p);
                node.lb.rectHV = new RectHV(node.rectHV.xmin(), node.rectHV.ymin(),
                                            node.rectHV.xmax(), node.p.y());
                size++;
            }
            else insertbyX(p, node.lb);
        }
        if (p.y() >= node.p.y()) {
            if (p.equals(node.p)) ;
            else if (node.rt == null) {
                node.rt = new Node(p);
                node.rt.rectHV = new RectHV(node.rectHV.xmin(), node.p.y(), node.rectHV.xmax(),
                                            node.rectHV.ymax());
                size++;
            }
            else insertbyX(p, node.rt);
        }
    }


    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return searchX(p, root);

    }

    private boolean searchX(Point2D p, Node node) {
        if (node == null) return false;

        if (p.equals(node.p)) return true;

        if (p.x() < node.p.x()) {
            return searchY(p, node.lb);
        }
        if (p.x() >= node.p.x()) {
            return searchY(p, node.rt);
        }
        return false;

    }

    private boolean searchY(Point2D p, Node node) {
        if (node == null) return false;

        if (p.equals(node.p)) return true;


        if (p.y() < node.p.y()) {
            return searchX(p, node.lb);
        }
        if (p.y() >= node.p.y()) {
            return searchX(p, node.rt);
        }
        return false;

    }
    // does the set contain point p?

    public void draw() {
        draw(root);
    }                         // draw all points to standard draw

    private void draw(Node node) {

        if (node != null) {
            node.p.draw();
            draw(node.lb);
            draw(node.rt);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> inrect = new ArrayList<>();
        rangeX(root, rect, inrect);
        return inrect;
    }        // all points that are inside the rectangle (or on the boundary)

    private void rangeX(Node node, RectHV rect, ArrayList<Point2D> inrect) {
        if (node == null) ;
        else {
            if (rect.contains(node.p)) inrect.add(node.p);
            if (rect.xmin() < node.p.x() && node.lb != null) rangeY(node.lb, rect, inrect);
            if (rect.xmax() >= node.p.x() && node.rt != null) rangeY(node.rt, rect, inrect);
        }
    }

    private void rangeY(Node node, RectHV rect, ArrayList<Point2D> inrect) {
        if (node == null) ;
        else {
            if (rect.contains(node.p)) inrect.add(node.p);
            if (rect.ymin() < node.p.y() && node.lb != null) rangeX(node.lb, rect, inrect);
            if (rect.ymax() >= node.p.y() && node.rt != null) rangeX(node.rt, rect, inrect);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        nearP = root.p;
        nearX(p, root);
        return nearP;
    }             // a nearest neighbor in the set to point p; null if the set is empty


    private boolean updateChampion(Point2D p, Node node) {
        if (node != null && p.distanceSquaredTo(node.p) < p.distanceSquaredTo(nearP)) {
            nearP = node.p;
            return true;
        }
        else return false;
    }

    private void nearX(Point2D p, Node node) {
        if (node != null) {
            updateChampion(p, node);
            if (p.x() < node.p.x()) {
                nearY(p, node.lb);

                if (node.rt != null
                        && node.rt.rectHV.distanceSquaredTo(p) < p
                        .distanceSquaredTo(nearP))
                    nearY(p, node.rt);
            }

            else {
                nearY(p, node.rt);

                if (node.lb != null
                        && node.lb.rectHV.distanceSquaredTo(p) < p
                        .distanceSquaredTo(nearP))
                    nearY(p, node.lb);
            }
        }
    }


    private void nearY(Point2D p, Node node) {
        if (node != null) {
            updateChampion(p, node);
            if (p.y() < node.p.y()) {
                nearX(p, node.lb);

                if (node.rt != null
                        && node.rt.rectHV.distanceSquaredTo(p) < p
                        .distanceSquaredTo(nearP))
                    nearX(p, node.rt);
            }

            else {
                nearX(p, node.rt);
                if (node.lb != null
                        && node.lb.rectHV.distanceSquaredTo(p) < p
                        .distanceSquaredTo(nearP))
                    nearX(p, node.lb);
            }
        }
    }


    public static void main(String[] args) {

    }
}
