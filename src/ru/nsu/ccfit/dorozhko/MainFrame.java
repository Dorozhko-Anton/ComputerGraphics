package ru.nsu.ccfit.dorozhko;

import sun.awt.VerticalBagLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Anton on 24.02.14.
 */
public class MainFrame extends JFrame {
    private final JMenuBar menuBar;
    private final JToolBar toolBar;
    private final JMenu fileMenu;
    private final JMenu aboutMenu;
    private final JPanel displayPanel;

    /**
     * <p> Constructor creates main JFrame with some generic menu items like 'Exit' </p>
     *
     * @param title title of the frame
     */
    public MainFrame(String title) {
        super(title);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("<html><u>Ф</u>айл</html>");
        fileMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(fileMenu);

        aboutMenu = new JMenu("О программе");
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Лабораторная работа №0");
            }
        });
        menuBar.add(aboutMenu);

        JMenuItem exitMenu = new JMenuItem("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_D);
        exitMenu.setAccelerator(KeyStroke.getKeyStroke('D'));
        fileMenu.add(exitMenu);
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        Container container = new Container();
        container.setLayout(new BorderLayout());
        container.add(menuBar, BorderLayout.NORTH);


        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        this.addAction(new AbstractAction("Выход", MainFrame.createImageIcon("/images/exit.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        container.add(toolBar, BorderLayout.SOUTH);

        add(container, BorderLayout.NORTH);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        add(displayPanel, BorderLayout.CENTER);
    }

    /**
     * <p> Used to add menus from other labs </p>
     *
     * @param jMenu menu to add
     */
    public void addMenu(JMenu jMenu) {
        menuBar.add(jMenu);
    }

    /**
     * <p> Used to add actions to toolBar from other labs </p>
     *
     * @param action action to add
     */
    public void addAction(Action action) {
        toolBar.add(action);
    }


    /**
     *
     * @return get File menu to add menu items
     */
    public JMenu getFileMenu() {
        return fileMenu;
    }

    /**
     *
     * @return get About menu to add men items
     */
    public JMenu getAboutMenu() {
        return aboutMenu;
    }

    /**
     *
     * @param c - component you will draw on
     */
    public void addCanvas(Component c) {

        displayPanel.add(c, BorderLayout.CENTER);
        displayPanel.repaint();
    }

    /**
     *
     * @param path
     * @return  Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * <p> Method to test </p>
     */
    public static void CreateAndShowGUI() {
        MainFrame mainFrame = new MainFrame("Лабораторная работа №0");
        mainFrame.setVisible(true);
    }

    public static void main(String args[]) {
        CreateAndShowGUI();
    }
}
