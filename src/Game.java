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
    private JPanel info;
    private JPanel underPanel;
    private DrawPanel pad;
    private MPBar mpBar;
    private HPBar hpBar;
    private XPBar xpBar;
    private JLabel mpLabel;
    private JLabel hpLabel;
    private JLabel xpLabel;
    
    public static Character main;
    private int mapX;
    private int mapY;
    
    public static int gold;
    public static int xp;
    public static int level;

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
    public static final int REGEN_TIME = 30;
    public static final int DAMAGE_FRAMES = 30;
    public static final int TOGGLE_FRAMES = 12;

    public static int BOARD_WIDTH = 320;
    public static int BOARD_HEIGHT = 320;
    
    public static final int COMBO_TIME = 90;
    //public static final int DRAW_WIDTH = 300;
    public static final int DRAW_HEIGHT = 300;
    public static final int BAR_HEIGHT = 30;
    public static final int DRAW_WIDTH = 500;

    public static final Color MP_BG = new Color(128, 159, 255);
    public static final Color MP_FILL = new Color(0, 64, 255);

    public static final Color HP_BG = new Color(255, 128, 128);
    public static final Color HP_FILL = new Color(255, 0, 0);
    
    public static final Color XP_BG = new Color(128, 128, 128);
    public static final Color XP_FILL = new Color(34, 139, 34);
    
    public static final int[] XP_LEVELS = new int[]{0, 10, 15, 20, 25, 30, 40, 50};
    
    public static SpriteLoc[][] map;
    
    private static HashMap<Location, Interactable> interactables;
    private static HashMap<Location, Animated> animables;

    public static HashSet<Enemy> enemies;
    
    public static void main(String[] args) throws IOException {
        /*map = new SpriteLoc[BOARD_HEIGHT / 32][BOARD_WIDTH / 32];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = SpriteLoc.FLOOR;
            }
        }*/
        gold = 0;
        new Game().start();
    }

    public Game() throws IOException {
        time = 0;
        shape = -1;
        comboTime = 0;
        drawingSecond = false;
        level = 1;
        
        frame = new JFrame();
        mapPanel = new Map();
        control = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(DRAW_WIDTH, DRAW_HEIGHT + 2 * BAR_HEIGHT );
            }
        };
        underPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(BOARD_WIDTH, DRAW_HEIGHT + 2 * BAR_HEIGHT );
            }
        };
        
        info = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(24 * 32 - DRAW_WIDTH - 12, DRAW_HEIGHT + 2 * BAR_HEIGHT);
            }
            
        };
        
        JLabel title = new JLabel("Stuff");
        title.setFont(new Font("Courier New", Font.BOLD, 16));
        info.add(title);
        
        pad = new DrawPanel();

        mpBar = new MPBar();
        mpLabel = new JLabel("MP: " + Character.MAX_MP + "/" + Character.MAX_MP);
        mpLabel.setFont(new Font("Pixelated", Font.BOLD, 18));
        mpLabel.setForeground(Color.WHITE);
        mpBar.add(mpLabel);

        hpBar = new HPBar();
        hpLabel = new JLabel("HP: " + Character.MAX_HP + "/" + Character.MAX_HP);
        hpLabel.setFont(new Font("Pixelated", Font.BOLD, 18));
        hpLabel.setForeground(Color.WHITE);
        hpBar.add(hpLabel);
        
        xpBar = new XPBar();
        xpLabel = new JLabel(" XP: " + Game.xp + "/" + Game.XP_LEVELS[level]);
        xpLabel.setFont(new Font("Pixelated", Font.PLAIN, 14));
        xpBar.setLayout(new BorderLayout());
        xpBar.add(xpLabel, BorderLayout.CENTER);
        
        info.add(xpBar);


        main = new Character(64, 32, SpriteLoc.BOY);
        mapPanel.add(main);

        enemies = new HashSet<Enemy>();
        animables = new HashMap<Location, Animated>();
        
        Border b = BorderFactory.createStrokeBorder(new BasicStroke(3), new Color(139, 69, 19));//brown

        TitledBorder tb = BorderFactory.createTitledBorder(b, "D-Pad");
        tb.setTitleJustification(TitledBorder.CENTER);
        tb.setTitleFont(new Font("Courier New", Font.BOLD, 16));
        pad.setBorder(tb);
        
        TitledBorder tb2 = BorderFactory.createTitledBorder(b, "Character Info");
        tb2.setTitleJustification(TitledBorder.CENTER);
        tb2.setTitleFont(new Font("Courier New", Font.BOLD, 16));
        info.setBorder(tb2);
       // pad.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
        //        BorderFactory.createLoweredBevelBorder()));

        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.add(pad);
        control.add(mpBar);
        control.add(hpBar);
        
        //underPanel.setBackground(Color.WHITE);
        underPanel.add(control, BorderLayout.WEST);
        underPanel.add(info, BorderLayout.CENTER);
        
        mapX = 0;
        mapY = 0;
        initMap("map"+mapX+","+mapY+".txt");
        //initMap();
        
        frame.setSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + DRAW_HEIGHT + 2 * BAR_HEIGHT + 15 + 12));
        frame.getContentPane().add(underPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(mapPanel, BorderLayout.NORTH);
        //frame.getContentPane().add(main);
       
        initBindings();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

    }
    
    private void initMap() throws IOException{
        
        BufferedReader in = new BufferedReader(new FileReader("map0,0.txt"));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int w = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());
        
        BOARD_WIDTH = w * 32;
        BOARD_HEIGHT = h * 32;
        
        map = new SpriteLoc[w][h];
        interactables = new HashMap<Location, Interactable>();
        
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
                        interactables.put(new Location(j, i), new Door(j, i, SpriteLoc.DOOR1, frame));
                        break;
                    case 'w':
                        map[j][i] = SpriteLoc.WELL;
                        break;
                    case 's':
                        map[j][i] = SpriteLoc.STATUE;
                        break;
                    case 'f':
                        map[j][i] = SpriteLoc.TORCH0;
                        Torch t = new Torch(j,i, main);
                        animables.put(new Location(j, i), t);
                        //mapPanel.add(t);
                        break;
                    case 'b':
                        Enemy en = new BasicEnemy(j*Character.SPRITE_SIZE,i*Character.SPRITE_SIZE,SpriteLoc.GHOST, main, enemies);
                        enemies.add(en);
                        mapPanel.add(en);
                    default:
                        map[j][i] = SpriteLoc.FLOOR;
                }
            }
        }
    }

    private void initMap(String fileName) throws IOException{
        
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int w = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());
        
        BOARD_WIDTH = w * 32;
        BOARD_HEIGHT = h * 32;
        
        map = new SpriteLoc[w][h];
        interactables = new HashMap<Location, Interactable>();
        
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
                        interactables.put(new Location(j, i), new Door(j, i, SpriteLoc.DOOR1, frame));
                        break;
                    case 'w':
                        map[j][i] = SpriteLoc.WELL;
                        break;
                    case 's':
                        map[j][i] = SpriteLoc.STATUE;
                        break;
                    case 'f':
                        map[j][i] = SpriteLoc.TORCH0;
                        Torch t = new Torch(j,i, main);
                        animables.put(new Location(j, i), t);
                        //mapPanel.add(t);
                        break;
                    case 'b':
                        Enemy en = new BasicEnemy(j*Character.SPRITE_SIZE,i*Character.SPRITE_SIZE,SpriteLoc.GHOST, main, enemies);
                        enemies.add(en);
                        mapPanel.add(en);
                        map[j][i] = SpriteLoc.FLOOR;
                        break;
                    case 'r':
                        en = new RangedEnemy(j*Character.SPRITE_SIZE,i*Character.SPRITE_SIZE,SpriteLoc.SKELETON, main, enemies);
                        enemies.add(en);
                        mapPanel.add(en);
                        map[j][i] = SpriteLoc.FLOOR;
                        break;
                    default:
                        map[j][i] = SpriteLoc.FLOOR;
                }
            }
        }
        if (mapX != 0)
            map[1][1] = SpriteLoc.STAIRS_ENTER;
        map[w - 2][h - 2] = SpriteLoc.STAIRS_EXIT;
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
        main.getActionMap().put("interact", new InterAction());
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
                //System.out.println(shape);
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
                //System.out.println("Square");
                main.incMP(20);
              }
              else if (shape2!=-1)
              {
                main.fireWeapon("Arrow",shape2-2);
              }
                
              comboTime = 0;
              drawingSecond = false;
            }
            
            if (++time % REGEN_TIME == 0) {
                main.incMP(REGEN_RATE);
            }
            
            if (time == TOGGLE_FRAMES * DAMAGE_FRAMES * REGEN_TIME)
                time = 0;
            
            if (time % TOGGLE_FRAMES == 0)
                main.toggle();
            
            main.setStill(keys.isEmpty());
            Rectangle me = main.getBounds();//TODO: REDO BOUNDS
            me.grow(-6, 0);
            
            enemies.stream().map((e) -> {
                e.update();
                return e;
            }).forEachOrdered((e) -> {
                boolean intersected = false;
                Rectangle re = e.getBounds();
                /*if (e.overlapsOtherEnemies()||intersected)
                {
                e.moveTo(e.getPrevLoc());
                }*/
                if (re.intersects(me)) {
                    intersected = true;
                    if (time % DAMAGE_FRAMES == 0) {
                        main.incHP(-e.getHitDmg());
                    }
                }
            });
         
          if (main.getHP() <= 0) {
              mapPanel.remove(main);
              frame.repaint();
              break;
          }
          for (Animated a : animables.values())
              a.animate();
          try {
            ArrayList<Enemy> tempEnemies = new ArrayList<Enemy>();
            int w = BOARD_WIDTH / 32;
            int h = BOARD_HEIGHT / 32;
           /* System.out.println("w: " + w);
            System.out.println("mx: " + main.getX());
            System.out.println("h: " + h);
            System.out.println("my: " + main.getY());*/
          if ((main.getX() + 16) / 32 == w - 2 && (main.getY() + 16) / 32 == h - 2)
          {
             // System.out.println("next");
            animables.clear();
            interactables.clear();
            for (Enemy e : enemies)
            {
             tempEnemies.add(e); 
            }
            mapX++;
            initMap("map"+mapX+","+mapY+".txt");
            
            for (Enemy e : tempEnemies)
            {
             mapPanel.remove(e);
             enemies.remove(e);
            }
            main.setX(64);
            main.setY(32);
            
          }
          else if (main.getX() / 32 <= 1 && main.getY() / 32 <= 1 && mapX > 0)
          {
            animables.clear();
            interactables.clear();
            for (Enemy e : enemies)
            {
             tempEnemies.add(e); 
            }
            enemies.clear();
            mapX--;
            initMap("map"+mapX+","+mapY+".txt");
            for (Enemy e : tempEnemies)
            {
              mapPanel.remove(e);
              enemies.remove(e);
            }
            main.setX(BOARD_WIDTH - 96);
            main.setY(BOARD_HEIGHT - 64);
            
          }
          /*else if (main.y>32*(map[0].length-1.2))
          {
            animables.clear();
            interactables.clear();
            for (Enemy e : enemies)
            {
             tempEnemies.add(e); 
            }
            enemies.clear();
            mapY++;
            initMap("map"+mapX+","+mapY+".txt");
            for (Enemy e : tempEnemies)
            {
              mapPanel.remove(e);
              enemies.remove(e);
            }
            main.y = 16;
          }
          else if (main.y<2)
          {
            animables.clear();
            interactables.clear();
            for (Enemy e : enemies)
            {
             tempEnemies.add(e); 
            }
            enemies.clear();
            mapY++;
            initMap("map"+mapX+","+mapY+".txt");
            for (Enemy e : tempEnemies)
            {
              mapPanel.remove(e);
              enemies.remove(e);
            }
            main.y = (int)(32*(map[0].length-1.6));
          }*/
          else 
          {
            //frame.revalidate();
            frame.repaint();
          }
          }
          catch (Exception e)
          {
          }
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
    
    public class InterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {//TODO: DO NOT SNAP TO GRID
            //System.out.println("ineract");
            HashSet<Location> facing = new HashSet<Location>();
            Location curr = main.getGridLoc();
            facing.add(curr);
            switch (main.getDir()) {
                case Character.LEFT:
                    facing.add(curr.getTranslated(-1, -1));
                    facing.add(curr.getTranslated(-1, 0));
                    facing.add(curr.getTranslated(-1, 1));
                    break;
                case Character.UP:
                    facing.add(curr.getTranslated(-1, -1));
                    facing.add(curr.getTranslated(0, -1));
                    facing.add(curr.getTranslated(1, -1));
                    facing.add(curr.getTranslated(-1, 0));
                    facing.add(curr.getTranslated(1, 0));
                    break;
                case Character.RIGHT:
                    facing.add(curr.getTranslated(1, -1));
                    facing.add(curr.getTranslated(1, 0));
                    facing.add(curr.getTranslated(1, 1));
                    break;
                case Character.DOWN:
                    facing.add(curr.getTranslated(-1, 1));
                    facing.add(curr.getTranslated(0, 1));
                    facing.add(curr.getTranslated(1, 1));
                    facing.add(curr.getTranslated(-1, 0));
                    facing.add(curr.getTranslated(1, 0));
                    break;
            }
            
            for (Location l : facing) {
                if (interactables.containsKey(l))
                    interactables.get(l).interact();
            }
        }
        
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
            return new Dimension(DRAW_WIDTH, BAR_HEIGHT);
        }
    }
    
    public class Map extends JPanel {
        
        public HashSet<Sprite> unders = new HashSet<>();
        
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
                        if (map[i][j]!=null&&interactables.containsKey(new Location(i, j))&&map[i][j].name().startsWith("DOOR")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            g.drawImage(interactables.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);       
                        } else if (map[i][j]!=null&&map[i][j].name().startsWith("TORCH")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            g.drawImage(animables.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            
                        } else if (map[i][j]!=null&&animables.containsKey(new Location(i, j))&&map[i][j].name().startsWith("TORCH")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            g.drawImage(animables.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        } else if (map[i][j]!=null&&(map[i][j].name().startsWith("WELL")||map[i][j].name().startsWith("STATUE")) || map[i][j].name().startsWith("STAIRS")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            g.drawImage(Sprite.getImageAt(map[i][j]), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }else {
                          if (Sprite.getImageAt(map[i][j])!=null) {
                            Image curr = Sprite.getImageAt(map[i][j]);
                            g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                          }
                        }
                       // System.out.println("drawn " + i + ", " + j);
                    }
                }
            } else {
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j]!=null&&!(map[i][j].name().startsWith("WALL") || map[i][j].name().startsWith("DOOR") || map[i][j].name().startsWith("TORCH"))){
                            Image curr = Sprite.getImageAt(map[i][j]);
                            g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }
                        if (map[i][j]!=null&&(map[i][j].name().startsWith("WELL")||map[i][j].name().startsWith("STATUE"))) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }
                        if (map[i][j].name().startsWith("STAIRS")) {
                            g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            Image curr = Sprite.getImageAt(map[i][j]);
                            g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                        }
                       //System.out.println("drawn " + i + ", " + j);
                    }
                }
                
                for (Sprite s : unders) {
                    g.drawImage(s.getImage(), s.getX(), s.getY(), null);
                    if (s instanceof Enemy) {
                        Enemy e = (Enemy) s;
                        Graphics2D g2 = (Graphics2D) g;
        
                        g.setColor(Color.RED);
                        g.fillRect(e.getX() + 2, e.getY(), Character.SPRITE_SIZE - 4, 4);

                        g.setColor(Color.GREEN);
                        g.fillRect(e.getX() + 2, e.getY(), (int) (e.getHP() * 1.0 / e.getMaxHP() * (Character.SPRITE_SIZE - 4)), 4);

                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(0.5f));
                        g2.drawRect(e.getX() + 2, e.getY(), Character.SPRITE_SIZE - 4, 4);
                    }
                }
                
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j]!=null&&(map[i][j].name().startsWith("WALL") || map[i][j].name().startsWith("DOOR") || map[i][j].name().startsWith("TORCH")|| map[i][j].name().startsWith("STATUE")|| map[i][j].name().startsWith("WELL"))) {
                            if (map[i][j]!=null&&interactables.containsKey(new Location(i, j))&&map[i][j].name().startsWith("DOOR")) {
                                g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                                g.drawImage(interactables.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            } else if (map[i][j]!=null&&animables.containsKey(new Location(i, j))&&map[i][j].name().startsWith("TORCH")) {
                                g.drawImage(Sprite.getImageAt(SpriteLoc.FLOOR), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                                g.drawImage(animables.get(new Location(i, j)).getImage(), i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                            } else {
                              if (Sprite.getImageAt(map[i][j])!=null) {
                                Image curr = Sprite.getImageAt(map[i][j]);
                                g.drawImage(curr, i * Sprite.SPRITE_SIZE, j * Sprite.SPRITE_SIZE, null);
                              }
                            }
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
            hpLabel.setText(" HP: " + main.getHP() + "/" + Character.MAX_HP);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(DRAW_WIDTH, BAR_HEIGHT);
        }
    }
    
    private class XPBar extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(XP_BG);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(XP_FILL);
            g.fillRect(0, 0, (int) (Game.xp * 1.0 / Game.XP_LEVELS[level] * this.getWidth()), this.getHeight());
            xpLabel.setText(" XP: " + Game.xp + "/" + Game.XP_LEVELS[level]);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(24 * 32 - DRAW_WIDTH - 32, BAR_HEIGHT / 2);
        }
    }

    private class DrawPanel extends JPanel {
        
        private MouseShapeListener ml;

        public DrawPanel() {
            ml = new MouseShapeListener();
            this.addMouseListener(ml);
            this.addMouseMotionListener(ml);
        }
        
        public void startDraw() {
            new FastDraw().execute();
        }
        
        private class FastDraw extends SwingWorker<Object, Object> {

            @Override
            protected Object doInBackground() throws Exception {
                while (true) {
                    DrawPanel.this.repaint();
                }
            }
            
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(DRAW_HEIGHT, DRAW_HEIGHT);
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
                    if (clicked.size() > 1) {
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