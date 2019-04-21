import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class Game {

    private JFrame frame;
    private JPanel control;
    private DrawPanel pad;
    private MPBar mpBar;
    private HPBar hpBar;
    private JLabel mpLabel;
    private JLabel hpLabel;
    private Character main;

    private static final int IM = JComponent.WHEN_FOCUSED;

    private HashSet<Integer> keys;
    private int time;

    public static final int CIRCLE = 0;
    public static final int HOR_LINE = 1;
    public static final int VERT_LINE = 2;
    public static final int SQUARE = 3;

    public static final int SPEED = 1;
    public static final int REGEN_RATE = 1;

    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;
    //public static final int DRAW_WIDTH = 300;
    public static final int DRAW_HEIGHT = 300;
    public static final int BAR_HEIGHT = 30;

    public static final Color MP_BG = new Color(128, 159, 255);
    public static final Color MP_FILL = new Color(0, 64, 255);

    public static final Color HP_BG = new Color(255, 128, 128);
    public static final Color HP_FILL = new Color(255, 0, 0);

    public static void main(String[] args) throws IOException {
        new Game().start();
    }

    public Game() throws IOException {
        time = 0;

        frame = new JFrame();
        control = new JPanel();
        pad = new DrawPanel();

        mpBar = new MPBar();
        mpLabel = new JLabel("MP: " + Character.MAX_MP + "/" + Character.MAX_MP);
        mpLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        mpLabel.setForeground(Color.WHITE);
        mpBar.add(mpLabel);

        hpBar = new HPBar();
        hpLabel = new JLabel("HP: " + Character.MAX_HP + "/" + Character.MAX_HP);
        hpLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        hpLabel.setForeground(Color.WHITE);
        hpBar.add(hpLabel);

        main = new Character(0, 0, "ball.png", 20, 20);

        pad.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createLoweredBevelBorder()));

        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.add(pad);
        control.add(mpBar);
        control.add(hpBar);

        frame.setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + DRAW_HEIGHT + 2 * BAR_HEIGHT));
        frame.getContentPane().add(control, BorderLayout.SOUTH);
        frame.getContentPane().add(main);

        initBindings();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private void initBindings() {
        keys = new HashSet<Integer>();

        main.getInputMap(IM).put(KeyStroke.getKeyStroke("LEFT"), "left");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("UP"), "up");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("RIGHT"), "right");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("DOWN"), "down");

        main.getInputMap(IM).put(KeyStroke.getKeyStroke("A"), "left");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("W"), "up");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("D"), "right");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("S"), "down");

        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released LEFT"), "rleft");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released UP"), "rup");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released RIGHT"), "rright");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released DOWN"), "rdown");

        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released A"), "rleft");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released W"), "rup");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released D"), "rright");
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("released S"), "rdown");

        main.getActionMap().put("left", new ActionWrapper((e)->keys.add(37)));
        main.getActionMap().put("up", new ActionWrapper((e)->keys.add(38)));
        main.getActionMap().put("right", new ActionWrapper((e)->keys.add(39)));
        main.getActionMap().put("down", new ActionWrapper((e)->keys.add(40)));

        main.getActionMap().put("rleft", new ActionWrapper((e)->keys.remove(37)));
        main.getActionMap().put("rup", new ActionWrapper((e)->keys.remove(38)));
        main.getActionMap().put("rright", new ActionWrapper((e)->keys.remove(39)));
        main.getActionMap().put("rdown", new ActionWrapper((e)->keys.remove(40)));
    }

    public void start() {
        while (true) {
            try {Thread.sleep(10);} catch (InterruptedException ex) {}
            if (keys.contains(37) || keys.contains(65)) {//left
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

            if (pad.ml.finishedShape()) {
                int shape = this.getShape(pad.ml.getDragged());
                //System.out.println(shape);
                if (shape == Game.CIRCLE) {
                    main.fireWeapon("Boomerang");
                } else if (shape == Game.HOR_LINE) {
                    main.fireWeapon("Arrow");
                }
            }

            if (++time % 100 == 0) {
                time = 0;
                main.incMP(REGEN_RATE);
            }

            frame.repaint();
        }
    }

    public int getShape(ArrayList<Location> points) {
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
            scores[3] += Math.pow(Math.max(Math.abs(p.getX()), Math.abs(p.getY())) - .95, 2) / points.size();
        }
        int min = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < scores[min]) {
                min = i;
            }
            //System.out.println(i+": "+scores[i]);
        }
        if (scores[min]>0.05)
            return -1;
        return min;
    }

    public class ActionWrapper extends AbstractAction {

        ActionListener al;

        public ActionWrapper(ActionListener a) {
            this.al = a;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            al.actionPerformed(e);
        }
    }

    private class MPBar extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(MP_BG);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(MP_FILL);
            g.fillRect(0, 0, (int) (main.getMP() * 1.0 / Character.MAX_MP * this.getWidth()), this.getHeight());
            mpLabel.setText("MP: " + main.getMP() + "/" + Character.MAX_MP);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, BAR_HEIGHT);
        }
    }

    private class HPBar extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(HP_BG);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(HP_FILL);
            g.fillRect(0, 0, (int) (main.getHP() * 1.0 / Character.MAX_HP * this.getWidth()), this.getHeight());
            mpLabel.setText("MP: " + main.getHP() + "/" + Character.MAX_HP);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, BAR_HEIGHT);
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));  
            ArrayList<Location> points = ml.accessDragged();
            if (points.size()>0)
            {
                Location prev = points.get(0);
                for (Location p : points) {
                    g.drawLine((int) p.getX(), (int) p.getY(), (int) prev.getX(), (int) prev.getY());
                    prev = p;
                }
            }
        }

        public void drawShape(int x, int y) {
            //graphics = this.getGraphics();
            graphics.setColor(Color.RED);
            graphics.fillOval(x, y, 5, 5);
        }

        private class MouseShapeListener implements MouseListener, MouseMotionListener {

            private ArrayList<Location> clicked;
            private boolean stillClicking;

            public MouseShapeListener() {
                clicked = new ArrayList<Location>();
                stillClicking = false;
            }

            public ArrayList<Location> getDragged() {
                ArrayList<Location> temp = new ArrayList<Location>(clicked);
                clicked.clear();
                return temp;
            }

            public ArrayList<Location> accessDragged() {
                ArrayList<Location> temp = new ArrayList<Location>(clicked);
                return temp;
            }

            public boolean finishedShape() {
                if (!stillClicking) {
                    if (clicked.size() > 10) {
                        return true;
                    } else {
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