import java.awt.*;
import javax.swing.*;

public abstract class Weapon extends SwingWorker<Object, Object> {
    
    private String type;
    private Character user;
    
    private int startX;
    private int startY;
    private int dir;
    
    private Container parent;
        
    public Weapon(String t, Character s, int sx, int sy, int d, Container jc) {
        this.type = t;
        this.user = s;
        
        this.startX = sx;
        this.startY = sy;
        this.dir = d;
        
        this.parent = jc;
    }
    
    public abstract boolean activate();//initialize weapon - subtract cost, etc. returns whether the weapon should fire or not
    
    @Override
    protected abstract Object doInBackground() throws Exception;
    
    @Override
    protected abstract void done();
}