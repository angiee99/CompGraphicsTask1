import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * trida pro kresleni na platno: zobrazeni pixelu
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class Canvas {

    private JFrame frame;
    private JPanel panel;
    private BufferedImage img;
    private int x;
    private int y;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        x = img.getWidth()/2;
        y = img.getHeight()/2;

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                draw();
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    y--;
                    paintCross(x, y, 0xffff00, "none");
                    paintCross(x, y+1, 0x000000, "up");
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    y++;
                    paintCross(x, y, 0xffff00, "none");
                    paintCross(x, y-1, 0x000000, "down");
                }
                else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    x--;
                    paintCross(x, y, 0xffff00, "none");
                    paintCross(x+1, y, 0x000000, "left");
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    x++;
                    paintCross(x, y, 0xffff00, "none");
                    paintCross(x-1, y, 0x000000, "right");
                }
                draw();
                panel.repaint();
            }

        });
    }
    public void paintCross(int midX, int midY, int color, String way){
        if(!way.equals("left") && !way.equals("right") ) draw(x+1, midY, color);
        if(!way.equals("left") && !way.equals("right") ) draw(x-1, midY, color);
        if(!way.equals("up"))    draw(midX, midY+1, color);
        if(!way.equals("down"))  draw(midX, midY-1, color);
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw() {
//		clear();
        img.setRGB(x, y, 0xffff00);
        panel.repaint();
    }
    public void draw(int xcord, int ycord, int color) {
//		clear();
        img.setRGB(xcord, ycord, color);
        panel.repaint();
    }

    public void start() {
        draw();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
    }

}
