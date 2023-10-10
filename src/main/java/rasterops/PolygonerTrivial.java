package rasterops;

import model.Point;
import model.Polygon;

public class PolygonerTrivial implements Polygoner{
    private Polygon polygon;
    PolygonerTrivial(){
        polygon = new Polygon();
    }
    public Polygon getPolygon(){
        return this.polygon;
    }
    @Override
    public void drawEdge(Point p1, Point p2) {

    }

    @Override
    public void addVertex(Point p) {
    // if p is 1st vertex
    }
    private void addFirstVertex(Point p){

    }

}
