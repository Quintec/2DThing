import java.util.*;

public class BasicEnemy extends Enemy {
  private final double SPEED = 0.5;
  private int currDir = -1;
  

    public BasicEnemy(int xc, int yc, SpriteLoc sl, Character main, HashSet<Enemy> en) {
        super(xc, yc, sl, main, 10, 30, en);
        loc = new Location(xc,yc);  
        prevLoc = new Location(xc,yc);  

    }

    @Override
    public void update() {
        int dx = this.target.x-this.x;
        int dy = this.target.y-this.y;
        int xOld = this.x;
        int yOld = this.y;
        loc.translate(SPEED*dx/Math.sqrt(dx*dx+dy*dy),SPEED*dy/Math.sqrt(dx*dx+dy*dy));
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
        //if (xOld==this.x)
          //loc.setX(xOld);
        //if (yOld==this.y)
          //loc.setY(yOld);
        
        /*if (Math.random()<0.5)
          this.incX((int)(SPEED*dx/Math.abs(dx)));
        else
          this.incY((int)(SPEED*dy/Math.abs(dy)));*/
    }
    
    public boolean othersMove(Location l, boolean xDir) {
        for (Enemy e : Game.enemies) {
            if (e.getBounds().contains(l.getPoint()) && !e.willMove(xDir))
                return false;
        }
        return true;
    }

    @Override
    public boolean willMove(boolean xDir) {
        int dx = this.target.x-this.x;
        int dy = this.target.y-this.y;
        
        Location temp = new Location(loc.getX(), loc.getY());
        temp.translate(SPEED*dx/Math.sqrt(dx*dx+dy*dy),SPEED*dy/Math.sqrt(dx*dx+dy*dy));
        if (xDir && Math.abs(dx) > 0 && this.canSetX((int)temp.getX()) && !overlapsOtherEnemies())
            return true;
        if (!xDir && Math.abs(dy) > 0 && this.canSetY((int)temp.getY()) && !overlapsOtherEnemies())
            return true;
        return false;
    }
   
    @Override
    public void death() {
        Game.gold += 2;
    }
}
