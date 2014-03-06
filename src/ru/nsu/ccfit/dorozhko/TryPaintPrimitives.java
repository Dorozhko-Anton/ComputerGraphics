package ru.nsu.ccfit.dorozhko;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Anton on 25.02.14.
 */
public class TryPaintPrimitives {

    /**
     * <p> Constructor creates lab surrounding: menus, toolBar, actions </p>
     *
     */
    public TryPaintPrimitives() {
        MainFrame mainFrame = new MainFrame("TryPaintPrimitives");
        BufferedImage bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

        // stubs
        JMenuItem save = new JMenuItem("Сохранить");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Заглушка");
            }
        });
        mainFrame.getFileMenu().add(save);
        JMenuItem contacts = new JMenuItem("<html><u>К</u>онтакты</html>");
        contacts.setMnemonic(KeyEvent.VK_R);
        contacts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "name: Ice \n mail: fff@gmail.com\n ");
            }
        });
        mainFrame.getAboutMenu().add(contacts);

        JMenu jMenu = new JMenu("<html><u>П</u>римитивы</html>");
        jMenu.setMnemonic(KeyEvent.VK_G);
        JLabel canvas = new JLabel(new ImageIcon(bufferedImage));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(canvas);

        BresenhamLine bresenhamLine = new BresenhamLine(mainFrame, canvas, bufferedImage);
        jMenu.add(bresenhamLine);
        mainFrame.addAction(bresenhamLine);

        mainFrame.addCanvas(scrollPane);
        mainFrame.addMenu(jMenu);
        mainFrame.setVisible(true);
    }

    /*
     * <p> <u> CAUTION! </u> <b> This action class is just for test and some fun </b>  </p>
     * <p> <i> BresenhamLine </i> is an <i> AbstractAction </i> that can draw and erase lines using Bresenham
     * algorithm. </p>
     *
     * @see AbstractAction
     */
    public class BresenhamLine extends AbstractAction {
        private final MainFrame mainFrame;
        private int xstart, ystart;
        private int xend, yend;
        private final JLabel jLabel;
        private Graphics g;

        /**
         *
         * @param mainFrame1 blank main frame
         */
        public BresenhamLine(MainFrame mainFrame1, JLabel canvas, BufferedImage bufferedImage) {
            super("Bresenham Line", MainFrame.createImageIcon("/images/line.png"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_B);
           // putValue(ACCELERATOR_KEY, KeyEvent.VK_B);
            this.mainFrame = mainFrame1;

            jLabel = canvas;
            g = bufferedImage.getGraphics();
        }

        // Этот код "рисует" все 9 видов отрезков. Наклонные (из начала в конец и из конца в начало каждый), вертикальный и горизонтальный - тоже из начала в конец и из конца в начало, и точку.
        private int sign(int x) {
            return (x > 0) ? 1 : (x < 0) ? -1 : 0;
            //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
        }
        /**
         * <p> draw line from (xstart, ystart) to (xend, yend) </p>
         * <p> setPixel made as g.drawLine(x, y, x, y); </p>
         * @param xstart - start X coordinate
         * @param ystart - start Y coordinate
         * @param xend   - end X coordinate
         * @param yend   - end Y coordinate
         */
        public void drawBresenhamLine(int xstart, int ystart, int xend, int yend)
        {
            int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

            dx = xend - xstart;//проекция на ось икс
            dy = yend - ystart;//проекция на ось игрек

            incx = sign(dx);
            /*
             * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
             * справа налево по иксу, то incx будет равен -1.
             * Это будет использоваться в цикле постороения.
             */
            incy = sign(dy);
            /*
             * Аналогично. Если рисуем отрезок снизу вверх -
             * это будет отрицательный сдвиг для y (иначе - положительный).
             */

            if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
            if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
            //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

            if (dx > dy)
            //определяем наклон отрезка:
            {
             /*
              * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
              * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
              * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
              * по y сдвиг такой отсутствует.
              */
                pdx = incx;
                pdy = 0;
                es = dy;
                el = dx;
            } else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
            {
                pdx = 0;
                pdy = incy;
                es = dx;
                el = dy;//тогда в цикле будем двигаться по y
            }

            x = xstart;
            y = ystart;
            err = el / 2;
            g.drawLine(x, y, x, y);//ставим первую точку
            //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

            for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
            {
                err -= es;
                if (err < 0) {
                    err += el;
                    x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                    y += incy;//или сместить влево-вправо, если цикл проходит по y
                } else {
                    x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                    y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
                }

                g.drawLine(x, y, x, y);
            }
        }

        /**
         * <p> draws line from (xstart, ystart) to (xend, yend) on graphics g </p>
         * <p> setPixel made as g.clearRect(x, y, 1, 1); and <b> this is BAD </b>.
         * <i> So doge. Such a mistake. So wrong. Wow. </i>
         * Instead we should save pixels where line was drawn and <b>restore</b> them </p>
         *
         * @param xstart - start X coordinate
         * @param ystart - start Y coordinate
         * @param xend  - end X coordinate
         * @param yend  - end Y coordinate
         */
        public void eraseBresenhamLine(int xstart, int ystart, int xend, int yend)
        {
            int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

            dx = xend - xstart;//проекция на ось икс
            dy = yend - ystart;//проекция на ось игрек

            incx = sign(dx);
            /*
             * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
             * справа налево по иксу, то incx будет равен -1.
             * Это будет использоваться в цикле постороения.
             */
            incy = sign(dy);
            /*
             * Аналогично. Если рисуем отрезок снизу вверх -
             * это будет отрицательный сдвиг для y (иначе - положительный).
             */

            if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
            if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
            //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

            if (dx > dy)
            //определяем наклон отрезка:
            {
             /*
              * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
              * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
              * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
              * по y сдвиг такой отсутствует.
              */
                pdx = incx;
                pdy = 0;
                es = dy;
                el = dx;
            } else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
            {
                pdx = 0;
                pdy = incy;
                es = dx;
                el = dy;//тогда в цикле будем двигаться по y
            }

            x = xstart;
            y = ystart;
            err = el / 2;
            g.drawLine(x, y, x, y);//ставим первую точку
            //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

            for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
            {
                err -= es;
                if (err < 0) {
                    err += el;
                    x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                    y += incy;//или сместить влево-вправо, если цикл проходит по y
                } else {
                    x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                    y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
                }

                g.clearRect(x, y, 1, 1);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.jLabel.addMouseListener(mouseInputListenerStart);
        }

        /**
         * mouseInputListenerStart - save (xstart, ystart), adds motionListener
         */
        private final MouseInputListener mouseInputListenerStart = new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xstart = e.getX();
                ystart = e.getY();
                jLabel.removeMouseListener(mouseInputListenerStart);
                jLabel.addMouseMotionListener(motionListener);
                jLabel.addMouseListener(mouseInputListenerEnd);

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        /**
         * motionListener - removes mouseInputListenerStart, saves old (xend, yend), captures new (xend, yend),
         *                  erase line to old (xend, yend), draws line to new (xend, yend)
         */
        private final MouseMotionListener motionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int xendOld = xend;
                int yendOld = yend;
                xend = e.getX();
                yend = e.getY();

                eraseBresenhamLine(xstart, ystart, xendOld, yendOld);
                drawBresenhamLine(xstart, ystart, xend, yend);
                jLabel.repaint();
            }
        };

        /**
         * mouseInputListenerEnd - removes motionListener, captures new (xend, yend),  draws line to new (xend, yend)
         */
        private final MouseInputListener mouseInputListenerEnd = new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jLabel.removeMouseMotionListener(motionListener);
                jLabel.removeMouseListener(mouseInputListenerEnd);
                xend = e.getX();
                yend = e.getY();
                drawBresenhamLine(xstart, ystart, xend, yend);
                jLabel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
    }

    public static void main(String args[]) {
        new TryPaintPrimitives();
    }
}
