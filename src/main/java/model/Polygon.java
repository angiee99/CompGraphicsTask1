package model;

import rasterops.PolygonerTrivial;

import java.util.ArrayList;
import java.util.Optional;

public class Polygon {
    private ArrayList<Point> vertices;
    public Polygon(){
        vertices = new ArrayList<Point>();
    }

    public void addVertex(Point p) {
        if (getVertexCount() < 2) {
            vertices.add(p);
        } else if (getVertexCount() == 2) {
            addVertexInBetween(p, 1);
        } else {
            int[] edge = closestEdge(p);
            addVertexInBetween(p, edge[0] + 1);
        }
    }
    private int[] closestEdge(Point p){
        int[] closestEdge = new int[]{0, 1};
        double minDistance = distanceTo(p, getVertex(0), getVertex(1) );

        for (int i = 1; i < getVertexCount(); i++) {
            int[] currentEdge = new int[]{i, (i + 1) % getVertexCount()};

            Point p1 = getVertex(i);
            Point p2 = getVertex(currentEdge[1]); // Wrap around for the last edge

            double distance = distanceTo(p, p1, p2);

            if (distance < minDistance) {
                minDistance = distance;
                closestEdge = currentEdge;
            }
        }
        return closestEdge;
    }

    private double distanceTo(Point point, Point p1, Point p2) {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double x = point.x;
        double y = point.y;

        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;

        if (len_sq != 0) // zero length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void addVertexInBetween(Point newVert, int index){
        vertices.add(index, newVert);
    }

    public void removeVertex(Point p){
        vertices.remove(p);
    }
    public boolean isCloseEnough(Point p1, Point p2){
        double minDistance = 10;
        double distance = Math.sqrt(Math.abs((p2.x - p1.x)^2) + Math.abs((p2.y - p1.y)^2));
        return distance <= minDistance;
    }

    public Point getFisrtVertex(){
        return vertices.get(0);
    }

    public Point getVertex(int index){
            return vertices.get(index);
    }

    public Point getLastAddedVert(){
        return vertices.get(vertices.size() - 1);
    }

    public Point getVertByPosition(int pos){

        return vertices.get(pos);
    }

    public Optional<Integer> getVertexIndex(Point p){
        if(isVertex(p)){
            return Optional.of(vertices.indexOf(p));
        }
        return Optional.empty();
    }

    public boolean isVertex(Point p){
        return vertices.contains(p); // returns false because it is another object
    }

    public int getVertexCount(){
        return vertices.size();
    }

    public void clear(){
        vertices.clear();
    }

}
