package rasterops;

import model.Point;
import model.Polygon;
import rasterization.Raster;

public class PolygonerTrivial implements Polygoner{
    private Polygon polygon;
    private Liner liner;
    private int color;
    private int bgcolor;
    public PolygonerTrivial(int color, int bgcolor){
        polygon = new Polygon();
        liner = new LinerTrivial();
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
    private void addFirstVertex(Point p){
        polygon.addVertex(p);
    }

}
