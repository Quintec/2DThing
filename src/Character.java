import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.*;
import java.io.*;
import javax.imageio.ImageIO;


public class Character extends Sprite {
    
    public static final int CHAR_SIZE = 32;
    
    public static final int MAX_MP = 100;
    public static final int MAX_HP = 100;
    
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
        
    private int dir;
    private boolean still;
    private int toggle;
    
    private int mp;
    private int hp;
    
    private CharLoc loc;
    
    private static BufferedImage spriteSheet;
    
    static {
        try {
            spriteSheet = ImageIO.read(new File("SpriteSheetAll32.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load spritesheet at SpriteSheetAll32.png"); 
        }
    }
    
    public Character(int xc, int yc, CharLoc cl) {
        super(xc, yc, null, 32, 32);
        this.dir = RIGHT;
        this.still = true;
        this.toggle = 0;
        
        this.mp = MAX_MP;
        this.hp = MAX_HP;
        
        this.loc = cl;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (still) {
            this.img = spriteSheet.getSubimage(CHAR_SIZE * loc.getX(), CHAR_SIZE * (loc.getY() + dir), CHAR_SIZE, CHAR_SIZE);
        } else {
            this.img = spriteSheet.getSubimage(CHAR_SIZE * (loc.getX() + 1 + toggle), CHAR_SIZE * (loc.getY() + dir), CHAR_SIZE, CHAR_SIZE);
        }
        
        super.paintComponent(g);
    }
        
    public void fireWeapon(String name) {//TODO: Specify direction
        try {
            Class<?> cl = Class.forName(name);
            Constructor<?> c = cl.getConstructor(new Class[]{Character.class, int.class, Container.class});
            Weapon w = (Weapon)c.newInstance(this, 0, this.getParent());
            if (w.activate())
                w.execute();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Error loading weapon: " + e.getMessage());
        }
        /*Boomerang b = new Boomerang(this, x, y, 0, this.getParent());
        if (b.activate())
            b.execute();*/
    }
    
    public void toggle() {
        toggle = 1 - toggle;
    }
    
    public int getDir() {
        return dir;
    }
    
    public void setDir(int d) {
        dir = d;
    }
    
    public boolean isStill() {
        return still;
    }
    
    public void setStill(boolean b) {
        still = b;
    }
        
    public int getMP() {
        return mp;
    }
    
    public void incMP(int m) {
        if (mp + m <= MAX_MP)
            mp += m;
    }
    
    public void setMP(int m) {
        if (m <= MAX_MP)
            mp = m;
    }
    
    public int getHP() {
        return hp;
    }
    
    public void incHP(int h) {
        if (hp + h <= MAX_HP)
            hp += h;
    }
    
    public void setHP(int h) {
        if (h <= MAX_HP)
            hp = h;
    }
}
