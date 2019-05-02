import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Game {

    public static JFrame frame;
    public static Map mapPanel;
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
    private int shape;
    private int comboTime;
    private boolean drawingSecond;
    
    public static final int NUM_SHAPES = 6;
    public static final int CIRCLE = 0;
    public static final int SQUARE = 1;
    public static final int LEFT_LINE = 3;
    public static final int RIGHT_LINE = 4;
    public static final int DOWN_LINE = 2;
    public static final int UP_LINE = 5;

    public static final int SPEED = 1;
    public static final int REGEN_RATE = 1;
    public static final int REGEN_TIME = 6;

    public static int BOARD_WIDTH = 320;
    public static int BOARD_HEIGHT = 320;
    
    public static final int COMBO_TIME = 90;
    //public static final int DRAW_WIDTH = 300;
    public static final int DRAW_HEIGHT = 300;
    public static final int BAR_HEIGHT = 30;

    public static final Color MP_BG = new Color(128, 159, 255);
    public static final Color MP_FILL = new Color(0, 64, 255);

    public static final Color HP_BG = new Color(255, 128, 128);
    public static final Color HP_FILL = new Color(255, 0, 0);
    
    public static SpriteLoc[][] map;
    private static HashMap<Location, Door> doors;

    public static void main(String[] args) throws IOException {
        /*map = new SpriteLoc[BOARD_HEIGHT / 32][BOARD_WIDTH / 32];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = SpriteLoc.FLOOR;
            }
        }*/
        new Game().start();
    }

    public Game() throws IOException {
        time = 0;
        shape = -1;
        comboTime = 0;
        drawingSecond = false;
        
        frame = new JFrame();
        mapPanel = new Map();
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

        main = new Character(64, 64, SpriteLoc.BOY);
        
        mapPanel.add(main);

        Border b = BorderFactory.createStrokeBorder(new BasicStroke(3), new Color(139, 69, 19));//brown

        TitledBorder tb = BorderFactory.createTitledBorder(b, "D-Pad");
        tb.setTitleJustification(TitledBorder.CENTER);
        tb.setTitleFont(new Font("Courier New", Font.BOLD, 16));
        pad.setBorder(tb);
       // pad.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
        //        BorderFactory.createLoweredBevelBorder()));

        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.add(pad);
        control.add(mpBar);
        control.add(hpBar);
        
        initMap();

        frame.setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + DRAW_HEIGHT + 2 * BAR_HEIGHT + 15 + 12));
        frame.getContentPane().add(control, BorderLayout.SOUTH);
        frame.getContentPane().add(mapPanel, BorderLayout.NORTH);
        //frame.getContentPane().add(main);
       
        initBindings();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

    }
    
    private void initMap() throws IOException{
        
        BufferedReader in = new BufferedReader(new FileReader("map.txt"));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int w = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());
        
        BOARD_WIDTH = w * 32;
        BOARD_HEIGHT = h * 32;
        
        map = new SpriteLoc[w][h];
        doors = new HashMap<Location, Door>();
        
        for (int i = 0; i < h; i++) {
            char[] line = in.readLine().toCharArray();
            for (int j = 0; j < w; j++) {
                switch (line[j]) {
                    case '+':
                        map[j][i] = SpriteLoc.WALL_CORNER_TOP;
                        break;
                    case 't':
                        map[j][i] = SpriteLoc.WALL_CORNER_BOTTOM;
                        break;
                    case '-':
                        map[j][i] = SpriteLoc.WALL_HORIZONTAL;
                        break;
                    case '|':
                        map[j][i] = SpriteLoc.WALL_VERTICAL;
                        break;
                    case 'd':
                        map[j][i] = SpriteLoc.DOOR1;
                        doors.put(new Location(j, i), new Door(j, i, SpriteLoc.DOOR1, frame));
                        break;
                    default:
                        map[j][i] = SpriteLoc.FLOOR;
                }
            }
        }
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

        main.getActionMap().put("left", new ActionWrapper((e)->keys.add(Character.LEFT)));
        main.getActionMap().put("up", new ActionWrapper((e)->keys.add(Character.UP)));
        main.getActionMap().put("right", new ActionWrapper((e)->keys.add(Character.RIGHT)));
        main.getActionMap().put("down", new ActionWrapper((e)->keys.add(Character.DOWN)));

        main.getActionMap().put("rleft", new ActionWrapper((e)->keys.remove(Character.LEFT)));
        main.getActionMap().put("rup", new ActionWrapper((e)->keys.remove(Character.UP)));
        main.getActionMap().put("rright", new ActionWrapper((e)->keys.remove(Character.RIGHT)));
        main.getActionMap().put("rdown", new ActionWrapper((e)->keys.remove(Character.DOWN)));
        
        main.getInputMap(IM).put(KeyStroke.getKeyStroke("F"), "interact");
        main.getActionMap().put("interact", new ActionWrapper((e) -> doors.get(new Location(8, 5)).toggle()));
    }

    public void start() {
        while (true) {
            try {Thread.sleep(10);} catch (InterruptedException ex) {}
            if (keys.contains(Character.LEFT)) {
                main.incX(-SPEED);
                if (!keys.contains(main.getDir()))
                    main.setDir(Character.LEFT);
            }
            if (keys.contains(Character.UP)) {
                main.incY(-SPEED);
                if (!keys.contains(main.getDir()))
                    main.setDir(Character.UP);
            }
            if (keys.contains(Character.RIGHT)) {
                main.incX(SPEED);
                if (!keys.contains(main.getDir()))
                    main.setDir(Character.RIGHT);
            }
            if (keys.contains(Character.DOWN)) {
                main.incY(SPEED);
                if (!keys.contains(main.getDir()))
                    main.setDir(Character.DOWN);
            }

            if (!drawingSecond&&pad.ml.finishedShape()) {
                shape = this.getShape(pad.ml.getDragged());
                System.out.println(shape);
                if (shape == Game.CIRCLE) {
                    main.fireWeapon("Ring");
                } else if (shape == Game.RIGHT_LINE) {
                    main.fireWeapon("Arrow",2);
                    comboTime = COMBO_TIME;
                }
                else if (shape == Game.LEFT_LINE) {
                    main.fireWeapon("Arrow",1);
                    comboTime = COMBO_TIME;
                }
                else if (shape == Game.DOWN_LINE) {
                    main.fireWeapon("Arrow",0);
                    comboTime = COMBO_TIME;
                }
                else if (shape == Game.UP_LINE) {
                    main.fireWeapon("Arrow",3);
                    comboTime = COMBO_TIME;
                }
            }

            if (comboTime>0)
            {
              comboTime--;
              if (pad.ml.isClicking())
              {
                drawingSecond = true;
                comboTime = COMBO_TIME;
              }
            }
            else
            {
              drawingSecond = false;
            }
            
            if (drawingSecond&&pad.ml.finishedShape())
            {
              int shape2 = this.getShape(pad.ml.getDragged());
              if (shape2==CIRCLE)
                main.fireWeapon("Boomerang",shape-2);
              else if (shape2==SQUARE)
              {
                main.fireWeapon("Arrow",0);
                main.fireWeapon("Arrow",1);
                main.fireWeapon("Arrow",2);
                main.fireWeapon("Arrow",3);
                System.out.println("Square");
              }
              else if (shape2!=-1)
              {
                main.fireWeapon("Arrow",shape2-2);
              }
                
              comboTime = 0;
              drawingSecond = false;
            }
            
            if (++time % REGEN_TIME == 0) {
                time = 0;
                main.incMP(REGEN_RATE);
            }
            
            if (time % 12 == 0)
                main.toggle();
            
            main.setStill(keys.isEmpty());

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

        double[] scores = new double[Game.NUM_SHAPES];
        for (Location p : points) {
            // System.out.println(p.getX() + ", " + p.getY());
            //System.out.println(Math.pow((Math.sqrt(p.getX2()+p.getY2())-1),2)/points.size());
            scores[CIRCLE] += Math.pow((Math.sqrt(p.getX2() + p.getY2()) - 0.95), 2) / points.size();
            scores[LEFT_LINE] += p.getY2() / points.size();
            scores[DOWN_LINE] += p.getX2() / points.size();
            scores[RIGHT_LINE] += p.getY2() / points.size();
            scores[UP_LINE] += p.getX2() / points.size();
            scores[SQUARE] += Math.pow(Math.max(Math.abs(p.getX()), Math.abs(p.getY())) - .95, 2) / points.size();
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
        if (min==LEFT_LINE)
        {
          if (points.get(0).getX()>points.get(points.size()-1).getX())
            return LEFT_LINE;
          return RIGHT_LINE;
        }
        if (min==DOWN_LINE)
        {
          if (points.get(0).getY()>points.get(points.size()-1).getY())
            return UP_LINE;
          return DOWN_LINE;
        }
        //System.out.println(min);
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
    
    public class Map extends JPanel {
        
        private ArrayList<Sprite> unders = new ArrayList<>();
        
        public void addUnder(Sprite s) {
            unders.add(s);
        }
        
        public void clearUnder() {
            unders.clear();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            
            if (unders.isEmpty()) {
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j].name().startsWith("DOOR")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            g.drawImage(doors.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        } else {
                            Image curr = Sprite.getImageAt(map[i][j]);
                            g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }
                       // System.out.println("drawn " + i + ", " + j);
                    }
                }
            } else {
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (!map[i][j].name().startsWith("WALL")){
                            if (map[i][j].name().startsWith("DOOR")) {
                                g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                                g.drawImage(doors.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            } else {
                                Image curr = Sprite.getImageAt(map[i][j]);
                                g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            }
                        }
                       // System.out.println("drawn " + i + ", " + j);
                    }
                }
                
                for (Sprite s : unders) {
                    g.drawImage(s.getImage(), s.getX(), s.getY(), null);
                }
                
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j].name().startsWith("WALL")){
                            Image curr = Sprite.getImageAt(map[i][j]);
                            g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }
                       // System.out.println("drawn " + i + ", " + j);
                    }
                }
            }
            
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, BOARD_HEIGHT + 12);
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
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());
            
            g2.setStroke(new BasicStroke(5));  
            g2.setColor(new Color(100, 100, 100));
            ArrayList<Location> points = ml.accessDragged();
            if (points.size()>0)
            {
                Location prev = points.get(0);
                for (Location p : points) {
                    g2.drawLine((int) p.getX(), (int) p.getY(), (int) prev.getX(), (int) prev.getY());
                    prev = p;
                }
            }
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
                    if (clicked.size() > 3) {
                        return true;
                    } else {
                        clicked.clear();
                        return false;
                    }
                }
                return false;
                // return (!stillClicking) && clicked.size() > 10;
            }
            
            public boolean isClicking() {
              return stillClicking;
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