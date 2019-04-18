import java.awt.*;
import javax.swing.*;

public abstract class Weapon extends SwingWorker<Object, Object> {
    
    protected String type;
    protected Character user;
    protected Sprite wep;
    protected int stage;
    
    protected int width;
    protected int height;
    
    protected int dir;
    
    protected Container parent;
        
    public Weapon(String t, Character s, int d, Container jc, int w, int h) {
        this.type = t;
        this.user = s;
        this.stage = 1;
        
        this.width = w;
        this.height = w;
        
        this.dir = d;
        
        this.parent = jc;
    }
    
    public abstract boolean activate();//initialize weapon - subtract cost, etc. returns whether the weapon should fire or not
    
    public void init() {
        this.wep = new Sprite(this.user.getX(), this.user.getY(), type + stage + ".png", 20, 20);
        this.wep.resizeTo(width, height);
        this.parent.add(wep);
    }
    
    @Override
    protected abstract Object doInBackground() throws Exception;
    
    @Override
    protected abstract void done();
}