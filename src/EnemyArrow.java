import java.awt.Container;
import java.awt.Rectangle;
import java.util.Iterator;
public class EnemyArrow extends Weapon {

    public EnemyArrow(Character s, int d, Container jc) {
        super("EnemyArrow", 0, s, d, jc, 32, 32, 10);
    }

   @Override
    protected Object doInBackground() throws Exception {
        int i = 0;
        while (true) {
            i++;
            if (i == 1) {
                this.init();
                if (this.checkHits()) {
                    return null;
                }
            }
            else {
              if (this.dir == Character.DOWN) {
                if (!this.wep.incY(3))
                    return null;
              }
              else if (this.dir == Character.LEFT) {
                if (!this.wep.incX(-3))
                    return null;
              }
              else if (this.dir == Character.RIGHT) {
                  if (!this.wep.incX(3))
                      return null;
              }
              else if (this.dir == Character.UP) {
                  if (!this.wep.incY(-3))
                      return null;
              }
                /*if (i % 9 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png", this.dir);
                }*/
            }
            if (this.checkHits()) {
                return null;
            }
            
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }

    }
    
    protected boolean checkHits() {
        Rectangle me = this.wep.getBounds();
        me.grow(-12, 0);
        me.translate(6,0);
        Iterator<Enemy> it = Game.enemies.iterator();
        if (Game.main.getBounds().intersects(me))
        {
          Game.main.incHP(-this.getDmg());
          return true;
        }
        return false;
        
        /*while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getBounds().intersects(me)) {
                //System.out.println("arrow hit");
               // System.out.println(this.getDmg());
                e.incHP(-this.getDmg());
              //  System.out.println(e.getHP());
                if (e.getHP() <= 0) {
                    it.remove();
                    this.parent.remove(e);
                    Game.mapPanel.unders.remove(e);
                }
                
                return true;
            }
        }
        
        return false;*/
    }
    
}
