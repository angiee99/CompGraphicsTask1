package rasterops;

import model.Point;
import rasterization.Raster;

public interface Polygoner {
    void drawEdge(Raster raster, Point p1, Point p2, int color);
    void addVertex(Raster raster, Point p);

//    void moveVertex(Point oldPos, Point newPos);

    //TODO
}
