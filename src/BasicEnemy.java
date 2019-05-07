import java.util.*;

public class BasicEnemy extends Enemy {
  private final double SPEED = 0.5;
  

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
