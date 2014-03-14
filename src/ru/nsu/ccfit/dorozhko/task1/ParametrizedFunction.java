package ru.nsu.ccfit.dorozhko.task1;

import ru.nsu.ccfit.dorozhko.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static java.lang.Math.floor;

/**
 * Created by Anton on 10.03.14.
 */
public class ParametrizedFunction {
    public ParametrizedFunction() {
        final MainFrame mainFrame = new MainFrame("ParametrizedFunction");
        BufferedImage bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

        JMenu jMenu = new JMenu("<html><u>П</u>римитивы</html>");
        jMenu.setMnemonic(KeyEvent.VK_G);
        mainFrame.addMenu(jMenu);
        mainFrame.setVisible(true);

        AbstractAction contacts = new AbstractAction("Контакты", MainFrame.createImageIcon("/images/contact.png")) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(mainFrame, "name: Ice \n mail: fff@gmail.com\n ");
            }
        };
        mainFrame.addAction(contacts);
        mainFrame.getAboutMenu().add(contacts);

        AbstractAction aboutAction = new AbstractAction("О программе", MainFrame.createImageIcon("/images/help_info2.png")) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(mainFrame, "Лабораторная работа №0");
            }
        };
        mainFrame.addAction(aboutAction);
        mainFrame.getAboutMenu().add(aboutAction);
        AbstractAction exitAction = new AbstractAction("Выход", MainFrame.createImageIcon("/images/exit.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        mainFrame.addAction(exitAction);
        mainFrame.getFileMenu().add(exitAction);

        mainFrame.addCanvas(new ParametrizedFunctionPanel());
    }

    private class ParametrizedFunctionPanel extends JPanel {
        private int x0 = 400;
        private int y0 = 300;
        private int unitsX = 10;
        private int unitsY = 10;
        private int maxX = 100;
        private int minX = -100;
        private int maxY = 100;
        private int minY = -100;
        private Point2D mousePt;

        {
            setSize(800, 600);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mousePt = e.getPoint();
                    repaint();
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    x0 += e.getX() - mousePt.getX();
                    y0 += e.getY() - mousePt.getY();
                    mousePt = e.getPoint();
                    repaint();
                }
            });

            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    System.out.println(e.getUnitsToScroll());
                    if (unitsX - e.getUnitsToScroll() > 0) {
                        unitsX -= e.getUnitsToScroll();
                    }
                    if (unitsY - e.getUnitsToScroll() > 0) {
                        unitsY -= e.getUnitsToScroll();
                    }
                    repaint();

                }
            });
        }

        public void setMaxX(int maxX) {
            this.maxX = maxX;
        }

        public void setMinX(int minX) {
            this.minX = minX;
        }

        public void setMaxY(int maxY) {
            this.maxY = maxY;
        }

        public void setMinY(int minY) {
            this.minY = minY;
        }

        public void setX0(int x0) {
            this.x0 = x0;
        }

        public void setY0(int y0) {
            this.y0 = y0;
        }

        private int calcX(double t) {
            return (int) floor(t * t / (t * t - 1));
        }

        private int calcY(double t) {
            return (int) floor((t * t + 1) / (t + 2));
        }

        private void putPixel(int x, int y, Graphics g) {
            g.fillRect(x0 + x * unitsX, y0 + y * unitsY, unitsX, unitsY);
            System.out.println((x0 + x * unitsX) + ';' + (y0 + y * unitsY) + ';' + unitsX + ';' + unitsY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintGrid(g);
            maxX = ((int) getSize().getWidth() - x0) / unitsX;
            minX = -x0 / unitsX;
            maxY = ((int) getSize().getHeight() - y0) / unitsX;
            minY = -y0 / unitsX;

            g.setColor(Color.BLUE);

            Elispse elispse = new Elispse(x0, y0, 50, 10);
            elispse.paintElipse(g);
        }

        private void paintGrid(Graphics g) {
            Dimension size = getSize();

            g.setColor(Color.LIGHT_GRAY);
            System.out.println("width = " + size.getWidth() + "; height = " + size.getHeight());
            for (int x = x0; x < size.getWidth(); x += unitsX) {
                g.drawLine(x, 0, x, (int) size.getHeight());
            }
            for (int x = x0; x > 0; x -= unitsX) {
                g.drawLine(x, 0, x, (int) size.getHeight());
            }

            for (int y = y0; y < size.getHeight(); y += unitsY) {
                g.drawLine(0, y, (int) size.getWidth(), y);
            }
            for (int y = y0; y > 0; y -= unitsY) {
                g.drawLine(0, y, (int) size.getWidth(), y);
            }

            g.setColor(Color.BLACK);
            for (int y = 0; y < (int) size.getHeight(); y++) {
                g.fillRect(x0, y, unitsX, unitsY);
            }
            for (int x = 0; x < (int) size.getWidth(); x++) {
                g.fillRect(x, y0, unitsX, unitsY);
            }
        }

        private class Elispse {
            private int width = 20;
            private int height = 20;
            private int x0 = 50;
            private int y0 = 50;

            public Elispse(int originX, int originY, int width, int height) {
                x0 = originX;
                y0 = originY;
                this.width = width;
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getX0() {
                return x0;
            }

            public void setX0(int x0) {
                this.x0 = x0;
            }

            public int getY0() {
                return y0;
            }

            public void setY0(int y0) {
                this.y0 = y0;
            }

            public void paintElipse(Graphics g) {
                int a2 = width * width;
                int b2 = height * height;
                int fa2 = 4 * a2, fb2 = 4 * b2;
                int x, y, sigma;

                 /* first half */
                for (x = 0, y = height, sigma = 2 * b2 + a2 * (1 - 2 * height); b2 * x <= a2 * y; x++) {
                    putPixel(x, y, g);
                    putPixel(-x, y, g);
                    putPixel(x, -y, g);
                    putPixel(-x, -y, g);

                    if (sigma >= 0) {
                        sigma += fa2 * (1 - y);
                        y--;
                    }
                    sigma += b2 * ((4 * x) + 6);
                }

                /* second half */
                for (x = width, y = 0, sigma = 2 * a2 + b2 * (1 - 2 * width); a2 * y <= b2 * x; y++) {
                    putPixel(x, y, g);
                    putPixel(-x, y, g);
                    putPixel(x, -y, g);
                    putPixel(-x, -y, g);
                    if (sigma >= 0) {
                        sigma += fb2 * (1 - x);
                        x--;
                    }
                    sigma += a2 * ((4 * y) + 6);
                }
            }
        }
    }

    public static void main(final String... args) {
        ParametrizedFunction parametrizedFunction = new ParametrizedFunction();
    }
}
