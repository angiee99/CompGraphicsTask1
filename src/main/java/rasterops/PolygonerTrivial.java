package rasterops;

import model.Line;
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
    public void drawPolygon(){
        for (int i = 0; i < polygon.getVertexCount() -1; i++) {
            drawEdge(this.raster, polygon.getVertex(i), polygon.getVertex(i+1), this.color);
        }
        if(polygon.getVertexCount() > 2){
            drawEdge(this.raster, polygon.getVertex(polygon.getVertexCount() -1),
                    polygon.getVertex(0), this.color);
        }

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

    @Override
    public void drawEdge(Raster raster, Point p1, Point p2, int color) {
        liner.drawLine(raster, p1.x, p1.y, p2.x, p2.y, color);
        // change it to accept Points and so on
    }

    @Override
    public void addVertex(Raster raster, Point p){
        if(polygon.getVertexCount() == 0){
            polygon.addVertex(p);
        }
        else if(polygon.getVertexCount() == 1){
            drawEdge(raster, p, polygon.getVertex(0), this.color);
            polygon.addVertex(p);
        }
        else if(polygon.getVertexCount() == 2){
            drawEdge(raster, p, polygon.getVertex(0), this.color);
            drawEdge(raster, p, polygon.getVertex(1), this.color);
            polygon.addVertexInBetween(p, 1);
        }
        else {
            int[] edge = closestEdge(p);

            drawEdge(raster, polygon.getVertex(edge[0]), polygon.getVertex(edge[1]), this.bgcolor);
            drawEdge(raster, p, polygon.getVertex(edge[0]), this.color);
            drawEdge(raster, p, polygon.getVertex(edge[1]), this.color);

            polygon.addVertexInBetween(p, edge[0] + 1);
        }

    }
    private int[] closestEdge(Point p){
        int[] closestEdge = new int[]{0, 1};

        double minDistance = distanceTo(p ,polygon.getVertex(0), polygon.getVertex(1) );

        for (int i = 1; i < polygon.getVertexCount(); i++) {
            int[] currentEdge = new int[]{i, (i + 1) % polygon.getVertexCount()};

            Point p1 = polygon.getVertex(i);
            Point p2 = polygon.getVertex(currentEdge[1]); // Wrap around for the last edge

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


    public Optional<Point> isPolVertex(Point p){
        for (int i = 0; i < polygon.getVertexCount(); i++) {
            Point curr = polygon.getVertex(i);
            if(isCloseEnough(p,curr)){
                return Optional.of(curr);
            }
        }
        return  Optional.empty();
    }
    private boolean isCloseEnough(Point p1, Point p2){
        double minDistance = 10;
        double distance = Math.sqrt(Math.abs((p2.x - p1.x)^2) + Math.abs((p2.y - p1.y)^2));
        return distance <= minDistance;
    }

    public void deleteVertex(Point p){
        int count = polygon.getVertexCount();

        if(count == 1){
            polygon.removeVertex(p);
        }
        else if(count == 2){
            drawEdge(this.raster, polygon.getVertex(0), polygon.getVertex(1), this.bgcolor);
            polygon.clear();
        }
        else if(count > 2){
            int pIndex = polygon.getVertexIndex(p).get();
            Point before, after;
            if(pIndex == 0){
                 before =  polygon.getVertex(count -1);
            }
            else{
                 before =  polygon.getVertex(pIndex - 1 );
            }

            after  = polygon.getVertex(Math.abs(pIndex + 1) % count);

            drawEdge(this.raster, before, p, this.bgcolor);
            drawEdge(this.raster, p, after, this.bgcolor);

            drawEdge(this.raster, before, after, this.color);

            polygon.removeVertex(p);
        }
    }


}
