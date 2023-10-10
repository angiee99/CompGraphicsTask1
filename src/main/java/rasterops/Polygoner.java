package rasterops;

import model.Point;

public interface Polygoner {
    void drawEdge(Point p1, Point p2);
    void addVertex(Point p);

//    void moveVertex(Point oldPos, Point newPos);

    //TODO
}
