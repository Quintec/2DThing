import java.util.*;
import java.awt.*;

public abstract class Enemy extends Character {
    
    public Character target;
    public Set<Enemy> enemies;
    private int hitDmg;
    private int maxHP;
    
    protected Location loc;
    protected Location prevLoc;
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int dmg, int hp, Set<Enemy> en) {
        super(xc, yc, sl);
        this.target = main;
        this.hitDmg = dmg;
        this.setHP(hp);
        this.maxHP = hp;
        this.enemies = en;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        g.setColor(Color.RED);
        g.fillRect(2, 0, SPRITE_SIZE - 4, 4);
        
        g.setColor(Color.GREEN);
        g.fillRect(2, 0, (int) (this.getHP() * 1.0 / this.maxHP * (SPRITE_SIZE - 4)), 4);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(0.5f));
        g2.drawRect(2, 0, SPRITE_SIZE - 4, 4);
        
       // System.out.println(this.getBounds());
        
        /*Graphics gg = this.getParent().getGraphics();
        gg.setColor(Color.RED);
        gg.drawRect(this.x, this.y - 10, SPRITE_SIZE, 5);
        gg.setColor(Color.GREEN);
        gg.drawRect(this.x, this.y - 10, (int) (this.getHP() * 1.0 / this.maxHP * SPRITE_SIZE), 5);*/
        //System.out.println("drawing hp");
    }
    
    public abstract void update();
    
    public int getHitDmg() {
        return hitDmg;
    }
    
    public int getMaxHP() {
        return maxHP;
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
    
    
    
    public abstract void death(Iterator<Enemy> it);
    
    public abstract boolean willMove(boolean xDir);
}