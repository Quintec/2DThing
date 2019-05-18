import java.util.*;

public class RangedEnemy extends Enemy {
  private final double SPEED = 0.5;
  private int currDir = -1;
  private int coolDown;
  private final int COOL_DOWN = 200;

    public RangedEnemy(int xc, int yc, SpriteLoc sl, Character main, HashSet<Enemy> en) {
        super(xc, yc, sl, main, 10, 30, en);
        loc = new Location(xc,yc);  
        prevLoc = new Location(xc,yc);  
        coolDown = COOL_DOWN;
    }

    @Override
    public void update() {
        int dx = this.target.x-this.x;
        int dy = this.target.y-this.y;
        int xOld = this.x;
        int yOld = this.y;
        if (dx==0||Math.random()<0.3)
          loc.translate(0,SPEED*dy/Math.abs(dy));
        else if (dy==0||Math.random()<0.3)
          loc.translate(SPEED*dx/Math.abs(dx),0);
        else if (Math.abs(dx)<Math.abs(dy))
          loc.translate(SPEED*dx/Math.abs(dx),0);
        else
          loc.translate(0,SPEED*dy/Math.abs(dy));
        if (!this.setX((int)loc.getX()))//||overlapsOtherEnemies())
          loc.setX(xOld);
        else
          prevLoc.setX(xOld);
        if (!this.setY((int)loc.getY()))//||overlapsOtherEnemies())
          loc.setY(yOld);
        else
          prevLoc.setY(yOld);
        
        boolean change = true;
        if (currDir != -1) {
            change = !((currDir == Character.RIGHT && dx > 0 && Math.abs(dx) > Math.abs(dy)) ||
                    (currDir == Character.DOWN && dy > 0 && Math.abs(dy) > Math.abs(dx)) ||
                    (currDir == Character.LEFT && dx < 0 && Math.abs(dx) > Math.abs(dy)) ||
                    (currDir == Character.UP && dy < 0 && Math.abs(dy) > Math.abs(dx)));
        }
        if (change) {
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0)
                    this.setDir(Character.RIGHT);
                else
                    this.setDir(Character.LEFT);
            } else {
                if (dy > 0)
                    this.setDir(Character.DOWN);
                else
                    this.setDir(Character.UP);
            }
            
            currDir = this.getDir();
        }
        if (coolDown == 0)
        {
          this.fireWeapon("EnemyArrow",currDir);
          coolDown = COOL_DOWN;
        }
        else
          coolDown--;
        //if (xOld==this.x)
          //loc.setX(xOld);
        //if (yOld==this.y)
          //loc.setY(yOld);
        
        /*if (Math.random()<0.5)
          this.incX((int)(SPEED*dx/Math.abs(dx)));
        else
          this.incY((int)(SPEED*dy/Math.abs(dy)));*/
    }
   
    
}
