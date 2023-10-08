package rasterops;

import rasterization.Raster;

import static java.lang.Math.abs;
import static java.lang.Math.min;

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

            //rasterize
            final double k = (y2 - y1) / (x2 - x1);
            System.out.println(k);
            final double q = y1 - k * x1;
            for (int c = (int) x1; c < x2; c++) {
                int r = (int) (k * c + q);
                rastr.setColor(color, c, r);
            }

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
            double q = y1 - k * x1;
            System.out.println(k);

            for (int r = (int) y1; r < y2; r++) {
                int c = (int) ((r - q)/k);
                if(x1 == x2){
                    c = (int)x1;
                }
                rastr.setColor(color, c, r);
            }
        }

    }
}