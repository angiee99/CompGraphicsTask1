package rasterops;

import rasterization.Raster;

public interface Liner {
    public void drawLine(Raster rastr, double x1, double y1, double x2, double y2, int color);
}
