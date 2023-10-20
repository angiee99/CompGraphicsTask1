import model.Line;
import model.Point;
import rasterization.RasterBI;
import rasterops.LinerDashed;
import rasterops.LinerStrict;
import rasterops.LinerTrivial;
import rasterops.PolygonerTrivial;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.*;

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
    private boolean Dpressed;
    private int dashedLineStep;
    private int stepChange;

    //struktury
    private ArrayList<Line> lineList;

    private ArrayList<Line> previewLine;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);
        liner = new LinerTrivial();
        dashedLiner = new LinerDashed();
        polygoner = new PolygonerTrivial(img, 0x008000, 0x2f2f2f);

        lineList = new ArrayList<Line>();
        previewLine = new ArrayList<Line>();
        anchorPoint = new Point(-1, -1);

        withShift = false;
        Dpressed = false;
        dashedLineStep = 10;
        stepChange = 0;

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
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearCanvas();
                }

                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    withShift = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    Dpressed = true;

                    panel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                        Runnable deleteVertex = new Runnable() {
                            @Override
                            public void run() {
                                Point curr = new Point(e.getX(), e.getY());
                                Optional<Point> closestPoint = polygoner.isPolVertex(curr);
                                if (!closestPoint.isEmpty()) {
                                    polygoner.deleteVertex(closestPoint.get());
                                }
                            }
                        };
                        if (Dpressed) {
                            change(deleteVertex);
                        }
                        }
                    });

//                    img.present(panel.getGraphics());
                }
            }

            public void clearCanvas() {
                img.clear(0x2f2f2f);
                resetAnchorPoint();
                lineList.clear();
                polygoner.resetPolygon();
                img.present(panel.getGraphics());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    withShift = false;
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    stepChange = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    stepChange = -1;
                }

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    Dpressed = false;
                }

            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // for polygon
                if (!Dpressed) {
                    Runnable newVertex = new Runnable() {
                        @Override
                        public void run() {
                            polygoner.addVertex(img, new Point(e.getX(), e.getY()));
                        }
                    };
                    change(newVertex);
//                    img.present(panel.getGraphics());
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                panel.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                            withShift = true;
                        }
                    }
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                            withShift = false;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            stepChange = 1;
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            stepChange = -1;
                        }
                    }
                });
                Runnable newPreviewLine = new Runnable() {
                    @Override
                    public void run() {
                        if (anchorPoint.x == -1 && anchorPoint.y == -1) {
                            anchorPoint.x = e.getX();
                            anchorPoint.y = e.getY();
                        } else if (anchorPoint.x != -1 && anchorPoint.y != -1) {
                            if (withShift) {
                                predrawStrictLine(anchorPoint, new Point(e.getX(), e.getY()));
                            } else {
                                predrawLine(anchorPoint, new Point(e.getX(), e.getY()));
                            }
                        }
                    }
                };
                change(newPreviewLine);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (anchorPoint.x != -1 && anchorPoint.y != -1) {
                    Line current;
                    if (withShift) {
                        current = new LinerStrict()
                                .getStrictLine(anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                    } else {
                        current = new Line(anchorPoint, new Point(e.getX(), e.getY()), 0xff0000);
                    }
                    lineList.add(current);
                    for (Line line : lineList) {
                        liner.drawLine(img, line);
                    }
                    img.present(panel.getGraphics());
                    resetAnchorPoint();
                }
            }
        });
    }

    public void draw(){
        for (Line line: lineList
             ) {
            liner.drawLine(img, line);
        }
        for (Line dLine: previewLine
             ) {
            dashedLiner.drawLine(img, dLine, dashedLineStep);
        }

        polygoner.drawPolygon();

        img.present(panel.getGraphics());
    }

    private void predrawLine(Point p1, Point p2) {
        if (stepChange == -1) {
            dashedLineStep -= 2;
        } else if (stepChange == 1) {
            dashedLineStep += 2;
        }
        if (dashedLineStep < 1) {
            resetDashedLineStep();
        }
        stepChange = 0;
        dashedLiner.drawLine(img, new Line(p1, p2, 0xff0000), dashedLineStep);

        if(previewLine.size() == 0){
            previewLine.add(new Line(p1, p2, 0xff0000));
        }
        else{
            previewLine.clear();
            previewLine.add(new Line(p1, p2, 0xff0000));
        }
        img.present(panel.getGraphics());
        dashedLiner.drawLine(img, new Line(p1, p2, 0x2f2f2f), dashedLineStep);
    }

    private void predrawStrictLine(Point p1, Point p2) {
        LinerStrict strictLiner = new LinerStrict();
        strictLiner.drawStrictLine(img, p1, p2, 0xff0000);
        img.present(panel.getGraphics());
        strictLiner.drawStrictLine(img, p1, p2, 0x2f2f2f);
    }

    private void resetAnchorPoint() {
        anchorPoint.x = -1;
        anchorPoint.y = -1;
    }

    private void resetDashedLineStep() {
        dashedLineStep = 10;
        stepChange = 0;
    }

    public void clear() {
        img.clear(0x2f2f2f);
    }

    public void present(Graphics graphics) {
        img.present(graphics);
    }

    public void start() {
        panel.repaint();
    }

    public void change(Runnable update){
        clear();
        update.run();
        draw();
//            interface runnable
    }
    Runnable test = () -> {

    };

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}
