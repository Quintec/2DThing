import java.awt.*;
import javax.swing.*;

public abstract class Weapon extends SwingWorker<Object, Object> {
    
    protected String type;
    protected Character user;
    protected int mpReq;
    protected Sprite wep;
    protected int stage;
    
    protected int width;
    protected int height;
    
    protected int dir;//down: 0, left: 1, right: 2, up: 3
    
    protected Container parent;
        
    public Weapon(String t, int mpr, Character s, int d, Container jc, int w, int h) {
        this.type = t;
        this.user = s;
        this.stage = 1;
        this.mpReq = mpr;
        
        this.width = w;
        this.height = w;
        
        this.dir = d;
        
        this.parent = jc;
    }
    
    public boolean activate() {
        if (user.getMP() >= this.mpReq) {
            user.incMP(-this.mpReq);
            return true;
        } else {
            return false;
        }
    }
    
    public void init() {
        this.wep = new Sprite(this.user.getX(), this.user.getY(), type + stage + ".png", width, height);
        //this.wep.resizeTo(width, height);
        this.parent.add(wep);
    }
    
    @Override
    protected abstract Object doInBackground() throws Exception;
    
    @Override
    protected abstract void done();
}