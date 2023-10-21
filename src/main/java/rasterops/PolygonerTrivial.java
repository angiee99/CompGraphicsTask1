package rasterops;

import model.Point;
import model.Polygon;
import rasterization.Raster;

import java.util.Optional;

public class PolygonerTrivial implements Polygoner{
    private Polygon polygon;
    private Liner liner;
    private int color;
    private int bgcolor;
    private Raster raster;
    public PolygonerTrivial(Raster raster, int color, int bgcolor){
        polygon = new Polygon();
        liner = new LinerDDAII();
        this.raster = raster;
        this.color = color;
        this.bgcolor = bgcolor;
    }
    @Override
    public void drawPolygon(){
        for (int i = 0; i < polygon.getVertexCount() -1; i++) {
            drawEdge(polygon.getVertex(i), polygon.getVertex(i+1), this.color);
        }
        if(polygon.getVertexCount() > 2){
            drawEdge(polygon.getVertex(polygon.getVertexCount() -1),
                    polygon.getVertex(0), this.color);
        }
    }
    @Override
    public void drawEdge( Point p1, Point p2, int color) {
        liner.drawLine(this.raster, p1.x, p1.y, p2.x, p2.y, color);
    }

    @Override
    public void addVertex(Point p){ //dont need raster
        polygon.addVertex(p);
    }
    public Optional<Point> isPolVertex(Point p){
        for (int i = 0; i < polygon.getVertexCount(); i++) {
            Point curr = polygon.getVertex(i);
            if(polygon.isCloseEnough(p,curr)){
                return Optional.of(curr);
            }
        }
        return  Optional.empty();
    }
    public void deleteVertex(Point p){
        polygon.removeVertex(p);
    }
    public void resetPolygon(){
        polygon.clear();
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
}
