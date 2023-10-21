package rasterops;

import model.Point;
import rasterization.Raster;

public interface Polygoner {
    void drawPolygon();
    void drawEdge(Point p1, Point p2, int color);
    void addVertex(Point p);

//    void moveVertex(Point oldPos, Point newPos);

    //TODO
}
