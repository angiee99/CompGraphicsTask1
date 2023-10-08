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
    private int pointx;
    private int pointy;
    private LinerTrivial liner = new LinerTrivial();
    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);

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
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    for(int i = 0; i < 50; i++){
                        img.setColor(0xffff00, img.getWidth()/2 + i, img.getHeight()/2);
                    }
                    present(panel.getGraphics());
                };
            }
        });

        pointx = img.getWidth()/2;
        pointy = img.getHeight()/2;
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                liner.drawLine(img, pointx, pointy, e.getX(), e.getY(), 0xff0000);
                present(panel.getGraphics());
                pointx = e.getX();
                pointy = e.getY();
            }
        });

        //Not working
//        panel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                liner.drawLine(img, pointx, pointy, e.getX(), e.getY(), 0xff0000);
//                present(panel.getGraphics());
//            }
//        });
    }
    public void clear() {
        img.clear(0x2f2f2f);
//		Graphics gr = img.getGraphics();
//		gr.setColor(new Color(0x2f2f2f));
//		gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void present(Graphics graphics) {
        img.present(graphics);
//		graphics.drawImage(img, 0, 0, null);
    }

    public void draw(int x, int y, int rgb ) {
//		clear();
        img.setColor(rgb, x, y);
    }

    public void start() {
//		LinerTrivial liner = new LinerTrivial();
        liner.drawLine(img,300, 400, 300, 200, 0xff0000); //test vertical line

        panel.repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}
