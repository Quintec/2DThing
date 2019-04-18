import java.awt.*;
import java.lang.reflect.*;


public class Character extends Sprite {
        
    private int mp;
    
    public Character(int xc, int yc, String path) {
        super(xc, yc, path);
        this.mp = 0;
    }
        
    public void fireWeapon(String name) {//TODO: Specify direction
        try {
            Class<?> cl = Class.forName(name);
            Constructor<?> c = cl.getConstructor(new Class[]{Sprite.class, int.class, int.class, int.class, Container.class});
            Weapon w = (Weapon)c.newInstance(this, this.x, this.y, 0, this.getParent());
            if (w.activate())
                w.execute();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        /*Boomerang b = new Boomerang(this, x, y, 0, this.getParent());
        if (b.activate())
            b.execute();*/
    }
        
    public int getMP() {
        return mp;
    }
    
    public void incMP(int m) {
        mp += m;
    }
    
    public void setMP(int m) {
        mp = m;
    }
}
