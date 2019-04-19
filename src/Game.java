
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class Game {

    private JFrame frame;
    private JFrame control;
    private DrawPanel pad;
    private KeyboardListener kbl;
    private Character main;

    public static final int CIRCLE = 0;
    public static final int HOR_LINE = 1;
    public static final int VERT_LINE = 2;
    public static final int SQUARE = 3;

    private static final int SPEED = 1;
    
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;
    //public static final int DRAW_WIDTH = 300;
    public static final int DRAW_HEIGHT = 300;

    public static void main(String[] args) throws IOException {
        new Game().start();
    }

    public Game() throws IOException {
        frame = new JFrame();
        control = new JFrame();
        pad = new DrawPanel();
        
        pad.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), 
                                                         BorderFactory.createLoweredBevelBorder()));

        frame.setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + DRAW_HEIGHT));
        main = new Character(0, 0, "ball.png", 20, 20);
        frame.getContentPane().add(pad, BorderLayout.SOUTH);
        frame.getContentPane().add(main);

        //control.setContentPane(pad);

        kbl = new KeyboardListener();
        frame.addKeyListener(kbl);

        //frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
       /* control.setLocationRelativeTo(frame);
        control.setSize(new Dimension(300, 300));
        control.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        control.setVisible(true);*/
    }

    public void start() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            HashSet<Integer> keys = kbl.getPresssed();
            if (keys.contains(37) || keys.contains(65)) {//left
                //System.out.println("37");
                main.incX(-SPEED);
            }
            if (keys.contains(38) || keys.contains(87)) {//up
                main.incY(-SPEED);
            }
            if (keys.contains(39) || keys.contains(68)) {//right
                main.incX(SPEED);
            }
            if (keys.contains(40) || keys.contains(83)) {//down
                main.incY(SPEED);
            }
            //System.out.println(main.getX() + ", " + main.getY());
            if (pad.ml.finishedShape()) {
                int shape = this.getShape(pad.ml.getDragged());
                if (shape == Game.CIRCLE)
                    main.fireWeapon("Boomerang");
                //System.out.println(this.getShape(pad.ml.getDragged()));
            }

            frame.repaint();
            //control.repaint();
            pad.repaint();
        }
    }

    public int getShape(HashSet<Location> points) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (Location p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        double avgX = (minX + maxX) / 2;
        double avgY = (minY + maxY) / 2;
        maxX -= avgX;
        maxY -= avgY;
        for (Location p : points) {
            p.translate(-avgX, -avgY);
            p.dilate(1 / Math.max(maxX, maxY), 1 / Math.max(maxX, maxY));
        }

        double[] scores = new double[4];
        for (Location p : points) {
            // System.out.println(p.getX() + ", " + p.getY());
            //System.out.println(Math.pow((Math.sqrt(p.getX2()+p.getY2())-1),2)/points.size());
            scores[0] += Math.pow((Math.sqrt(p.getX2() + p.getY2()) - 1), 2) / points.size();
            scores[1] += p.getY2() / points.size();
            scores[2] += p.getX2() / points.size();
            scores[3] += Math.pow(Math.max(p.getX2(), p.getY2()) - 1, 2) / points.size();
        }
        int min = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < scores[min]) {
                min = i;
            }
            //System.out.println(i+": "+scores[i]);
        }
        //if (min>0.3)
        //return -1;
        return min;
    }

    private class KeyboardListener implements KeyListener {

        private HashSet<Integer> pressed;

        public KeyboardListener() {
            pressed = new HashSet<Integer>();
        }

        private HashSet<Integer> getPresssed() {
            return pressed;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            /*if (e.getKeyCode() == 32) {
                main.fireWeapon("Boomerang");
            } else {*/
                pressed.add(e.getKeyCode());
           // }
          // System.out.println(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressed.remove(e.getKeyCode());
        }
    }

    private class DrawPanel extends JPanel {
        
        
        private Graphics graphics;
        private MouseShapeListener ml;

        public DrawPanel() {
            ml = new MouseShapeListener();
            this.addMouseListener(ml);
            this.addMouseMotionListener(ml);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, DRAW_HEIGHT);
        }

        @Override
        public void paintComponent(Graphics g) {
            graphics = g;
            for (Location p : ml.accessDragged()) {
                this.drawShape((int) p.getX(), (int) p.getY());
            }
        }

        public void drawShape(int x, int y) {
            //graphics = this.getGraphics();
            graphics.setColor(Color.RED);
            graphics.fillOval(x, y, 10, 10);
        }

        private class MouseShapeListener implements MouseListener, MouseMotionListener {

            private HashSet<Location> clicked;
            private boolean stillClicking;

            public MouseShapeListener() {
                clicked = new HashSet<Location>();
                stillClicking = false;
            }

            public HashSet<Location> getDragged() {
                HashSet<Location> temp = new HashSet<Location>(clicked);
                clicked.clear();
                return temp;
            }
            
            public HashSet<Location> accessDragged() {
                HashSet<Location> temp = new HashSet<Location>(clicked);
                return temp;
            }

            public boolean finishedShape() {
                if (!stillClicking) {
                    if (clicked.size() > 10)
                        return true;
                    else {
                        clicked.clear();
                        return false;
                    }
                }
                return false;
               // return (!stillClicking) && clicked.size() > 10;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                clicked.add(new Location(e.getPoint()));
                //System.out.println(e.getX() + ", " + e.getY());

            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                stillClicking = true;
                clicked.clear();
                clicked.add(new Location(e.getPoint()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //System.out.println("released");
                stillClicking = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        }
    }

}
