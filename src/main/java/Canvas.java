import model.Line;
import model.Point;
import rasterization.RasterBI;
import rasterops.LinerDashed;
import rasterops.LinerTrivial;
import rasterops.PolygonerTrivial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private LinerDashed dashedLiner;
    private PolygonerTrivial polygoner;

    private boolean withShift;

    //struktury
    private ArrayList<Line> lineList;
    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);
        liner = new LinerTrivial();
        dashedLiner = new LinerDashed();
        polygoner = new PolygonerTrivial(0x008000, 0x2f2f2f);

        lineList = new ArrayList<Line>();
        anchorPoint = new Point(-1, -1);
        withShift = false;


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
                    lineList.clear();

                    img.present(panel.getGraphics());
                    //TODO add deletion of all data structures (Points, Lines(done), Polygones)
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // for polygon //TODO later
                polygoner.addVertex(img, new Point(e.getX(), e.getY()));
                img.present(panel.getGraphics());
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                panel.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                            withShift = true;
                        }
                    }
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                            withShift = false;
                        }
                    }
                });
                if(anchorPoint.x == -1 && anchorPoint.y== -1){
                    anchorPoint.x = e.getX();
                    anchorPoint.y = e.getY();
                }
                else if(anchorPoint.x != -1 && anchorPoint.y!= -1){
                    if(withShift){
                        liner.drawStrictLine(img, anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                                                        //mb here with shift as an argument
                                                        // or make it an atribute of liner, so we can set it and yep
                        img.present(panel.getGraphics());
                        liner.drawStrictLine(img, anchorPoint, new Point(e.getX(), e.getY()), 0x2f2f2f);
                    }
                    else{
                        liner.drawLine(img, new Line(anchorPoint, new Point(e.getX(), e.getY()), 0xff0000));
                        img.present(panel.getGraphics());
                        liner.drawLine(img, new Line(anchorPoint, new Point(e.getX(), e.getY()), 0x2f2f2f));
                    }
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(anchorPoint.x != -1 && anchorPoint.y!= -1){
                    Line current;
                    if(withShift){
                        current = liner.getStrictLine(anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                    }
                    else{
                       current = new Line(anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                    }

                    lineList.add(current);
                    for (Line line: lineList) {
                        liner.drawLine(img, line);
                    }
                    img.present(panel.getGraphics());

                    resetAnchorPoint();
                }
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
//        dashedLiner.drawLine(img,300, 400, 600, 200, 0xff0000); //test dashed line
//        liner.drawStrictLine(img, new Point(400, 300), new Point(250, 400), 0xff0000);
        panel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}
