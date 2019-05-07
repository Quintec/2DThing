import java.util.*;
import java.awt.*;

public abstract class Enemy extends Character {
    
    public Character target;
    public HashSet<Enemy> enemies;
    private int hitDmg;
    
    protected Location loc;
  protected Location prevLoc;
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int dmg, int hp, HashSet<Enemy> en) {
        super(xc, yc, sl);
        this.target = main;
        this.hitDmg = dmg;
        this.setHP(hp);
        this.enemies = en;
    }
    
    public abstract void update();
    
    public int getHitDmg() {
        return hitDmg;
    }
    
    public boolean overlapsOtherEnemies()
    {
      Rectangle me = this.getBounds();
      for (Enemy e: enemies)
      {
        if (e!=this)
        {
        Rectangle re = e.getBounds();
        if (re.intersects(me))
          return true;
        }
      }
      return false;
    }
    
    public void moveTo(Location newLoc)
    {
      loc = newLoc;
      this.x = (int) loc.getX();
      this.y = (int) loc.getY();
    }
    
    public Location getPrevLoc()
    {
     return prevLoc;
    }
}