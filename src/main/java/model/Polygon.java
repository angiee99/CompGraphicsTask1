package model;

import rasterops.PolygonerTrivial;

import java.util.ArrayList;
import java.util.Optional;

public class Polygon {
    private ArrayList<Point> vertices;
    public Polygon(){
        vertices = new ArrayList<Point>();
    }

    public void addVertex(Point newVert){
        vertices.add(newVert);
    }
    public void removeVertex(Point vertex){
        vertices.remove(vertex);
    }

//    public Optional<Point> getFisrtVertex(){
//        if(vertices.size() > 0)
//            return Optional.of(vertices.get(0));
//        return Optional.empty();
//
//    }

//    public Optional<Point> getLastAddedVert(){
//        if(vertices.size() > 0)
//            return Optional.of(vertices.get(vertices.size() - 1));
//        return Optional.empty();
//    }
    public Point getFisrtVertex(){
        return vertices.get(0);
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
        return vertices.contains(p);
    }

    public int getVertexCount(){
        return vertices.size();
    }
}
