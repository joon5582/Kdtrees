/* *****************************************************************************
 *  Name: Junwoo Lee
 *  Date: 6/3/2020
 *  Description: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 *  I have done all the coding by myself
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointset;
    private int size;

    public PointSET() {
        pointset = new SET<>();
        size = 0;
    }// construct an empty set of points

    public boolean isEmpty() {
        return size == 0;
    }// is the set empty?

    public int size() {
        return size;
    }// number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!pointset.contains(p)) {
            pointset.add(p);
            size++;
        }
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointset.contains(p);
    }// does the set contain point p?

    public void draw() {
        for (Point2D p : pointset) {
            p.draw();
        }
    }// draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> inRect = new SET<>();
        for (Point2D p : pointset) {
            if (rect.contains(p))
                inRect.add(p);
        }
        return inRect;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointset.isEmpty()) return null;
        Point2D nearest = pointset.min();
        for (Point2D pinset : pointset) {
            if (p.distanceTo(pinset) <= p.distanceTo(nearest))
                nearest = pinset;
        }
        return nearest;


    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }             // unit testing of the methods (optional)
}
