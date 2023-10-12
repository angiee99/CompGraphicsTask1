package rasterops;

import model.Line;
import model.Point;
import rasterization.Raster;


public class LinerTrivial implements Liner {
    public void drawLine(Raster rastr, double x1, double y1, double x2, double y2, int color){
        if(Math.abs(y2 - y1) < Math.abs(x2 - x1)){// osa y = x
            if(x1 > x2){//swap
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }

            final double k = (y2 - y1) / (x2 - x1);
            int x = (int)Math.round(x1);
            double y = y1;

            do{
                rastr.setColor(color, x, (int)Math.round(y));
                x += 1;
                y += k;
            } while (x <= x2);
        }
        else{ // ridici osa x
            if(y1 > y2){ //swap
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            double k = (y2 - y1) / (x2 - x1);
            double x = x1;
            int y = (int)Math.round(y1);
            do{
                rastr.setColor(color, (int)Math.round(x), y);
                x += 1/k;
                y += 1;
            } while (y <= y2);
        }
    }

    public void drawLine(Raster rastr, Point p1, Point p2, int color){
        drawLine(rastr, p1.x, p1.y, p2.x, p2.y, color);
    }

    public void drawLine(Raster rastr, Line line){
        drawLine(rastr, line.getX1(), line.getY1(), line.getX2(), line.getY2(), line.getColor());
    }

    public void drawStrictLine(Raster rastr,  Point p1, Point p2, int color){
        Line lineTodraw = getStrictLine(p1, p2, color);
        drawLine(rastr, lineTodraw);

    }
    //based on starting and ending point it is decided if the line will be
    // - horizontal
    // - vertical
    // - diagonal
    public Line getStrictLine( Point p1, Point p2, int color){
//        final double k = Math.abs((p2.y - p1.y) / (p2.x - p1.x));

        double dy = p2.y - p1.y;
        double dx = (p2.x - p1.x);
        double d = dy/dx;
        final double k = Math.abs(d);
        System.out.println("k = " + k);
        if(k == 1.00000 || k == 0 || p1.x == p2.x){
            return new Line(p1, p2, color);
        }

        Point endPoint;
        if(k <= 0.41){
            // horizontal line
            System.out.println("horizontal");
            endPoint = new Point(p2.x, p1.y);
        }
        else if((k > 0.41 && k < 1) || (k > 1 && k < 2.41)){
            //diagonal
            System.out.println("diagonal");
            System.out.println("d =" + d);
//            endPoint = new Point(p2.x, ((p2.x - p1.x)*d + p1.y));
            endPoint = new Point(p2.x, (p1.y - Math.abs(dx)));
        }
        else if(k >= 2.41){
            //vertical

            endPoint = new Point(p1.x, p2.y);
        }
        else endPoint = new Point(0, 0);

        Line strictLine = new Line(p1, endPoint, color);

        System.out.println("Starting point: " + p1.x + " " + p1.y + " : " + p2.x + " " + p2.y);
        System.out.println(p1.x + " " + p1.y + " : " + endPoint.x + " " + endPoint.y);
        return strictLine;
    }
//    public void drawHorizontalLine(Raster rastr, Line line){}
//    public void drawVerticalLine(Raster rastr, Line line){}
//    public void drawDiagonalLine(Raster rastr, Line line){}
}