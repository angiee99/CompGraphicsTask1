package rasterops;

import model.Point;
import model.Polygon;
import rasterization.Raster;
import rasterization.RasterBI;

import java.util.Optional;

public class PolygonerTrivial implements Polygoner{
    private Polygon polygon;
    private Liner liner;
    private int color;
    private int bgcolor;
    private Raster raster;
    public PolygonerTrivial(Raster raster, int color, int bgcolor){
        polygon = new Polygon();
        liner = new LinerTrivial();
        this.raster = raster;
        this.color = color;
        this.bgcolor = bgcolor;
    }

    public Polygon getPolygon(){
        return this.polygon;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void drawEdge(Raster raster, Point p1, Point p2, int color) {
        liner.drawLine(raster, p1.x, p1.y, p2.x, p2.y, color);
        // change it to accept Points and so on
    }

    @Override
    public void addVertex(Raster raster, Point p) {
        int vertexCount = polygon.getVertexCount();
        System.out.println(polygon.getVertexCount());
        if (vertexCount == 0){
            addFirstVertex(p);
        }
        else if(vertexCount == 1){
            drawEdge(raster, p, polygon.getLastAddedVert(), this.color);
            polygon.addVertex(p);
        }
        else{
            Point prev = polygon.getVertByPosition(polygon.getVertexCount() - 2);
            if(vertexCount > 2) {
                drawEdge(raster, polygon.getLastAddedVert(), prev, this.bgcolor);
            }

            drawEdge(raster, p, polygon.getLastAddedVert(), this.color);
            drawEdge(raster, p, prev, this.color);

            polygon.addVertex(p);
        }
    }

    public boolean isPolVertex(Point p){
        return polygon.isVertex(p);
    }

    public void deleteVertex(Point p){
        System.out.println("delete Vertx invoked");
        Point prev1, prev2;
        int pIndex = polygon.getVertexIndex(p).get(); // may make trouble

        if(pIndex > 0){
            prev1 = polygon.getVertByPosition(pIndex-1);
            drawEdge(this.raster, prev1, p, this.bgcolor);

            if(pIndex > 1){
                prev2 = polygon.getVertByPosition(pIndex-2);
                drawEdge(this.raster, prev2, p, this.bgcolor);

                drawEdge(this.raster, prev1, prev2, this.color);
            }
        }

        polygon.removeVertex(p);
    }

    private void addFirstVertex(Point p){
        polygon.addVertex(p);
    }
}
