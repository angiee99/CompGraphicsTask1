import model.Point;
import rasterization.RasterBI;
import rasterops.LinerTrivial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import static java.lang.Math.floor;
import static java.lang.Math.round;

/// now with RasterBI as it implements our Rastr Interface
/**
 * trida pro kresleni na platno: zobrazeni pixelu
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class Canvas {

    private JFrame frame;
    private JPanel panel;
    private RasterBI img;
    private Point anchorPoint;
    private LinerTrivial liner;
    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);
        liner = new LinerTrivial();
        anchorPoint = new Point(-1, -1);
        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocusInWindow();
        clear();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_C){
                    img.clear(0x2f2f2f);
                    resetAnchorPoint();
                    img.present(panel.getGraphics());
                    //TODO add deletion of all data structures (Points, Lines, Polygones)
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // for polygon //TODO later
                if(anchorPoint.x != -1 && anchorPoint.y != -1){
                    liner.drawLine(img, anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                }
                present(panel.getGraphics());//
                anchorPoint.x = e.getX();
                anchorPoint.y = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(anchorPoint.x != -1 && anchorPoint.y!= -1){
                    liner.drawLine(img, anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                }
                img.present(panel.getGraphics());
                anchorPoint.x = e.getX();
                anchorPoint.y = e.getY();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                resetAnchorPoint();
            }
        });
    }
    private void resetAnchorPoint(){
        anchorPoint.x = -1;
        anchorPoint.y = -1;
    }

    public void clear() {
        img.clear(0x2f2f2f);
    }

    public void present(Graphics graphics) {
        img.present(graphics);
    }

    public void draw(int x, int y, int rgb ) {
        img.setColor(rgb, x, y);
    }

    public void start() {
//        liner.drawLine(img,300, 400, 300, 200, 0xff0000); //test vertical line
        panel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}
