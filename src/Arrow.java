import java.awt.Container;
import java.awt.Rectangle;
import java.util.Iterator;
public class Arrow extends Weapon {

    public Arrow(Character s, int d, Container jc) {
        super("Arrow", 10, s, d, jc, 32, 32, 10);
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
                if (!this.wep.incY(1))
                    return null;
              }
              else if (this.dir == Character.LEFT) {
                if (!this.wep.incX(-1))
                    return null;
              }
              else if (this.dir == Character.RIGHT) {
                  if (!this.wep.incX(1))
                      return null;
              }
              else if (this.dir == Character.UP) {
                  if (!this.wep.incY(-1))
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
            
            try { Thread.sleep(3);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }

    }
    
    protected boolean checkHits() {
        Rectangle me = this.wep.getBounds();
        me.grow(-12, 0);
        Iterator<Enemy> it = Game.enemies.iterator();
        //System.out.println(me);
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getBounds().intersects(me)) {
             //   System.out.println(e.getBounds());
               // System.out.println(e.getX() + ", " + e.getY());
               
                //System.out.println("arrow hit");
               // System.out.println(this.getDmg());
                e.incHP((int)(-this.getDmg() * Game.DAMAGE_MULTIPLIER));
              //  System.out.println(e.getHP());
                if (e.getHP() <= 0) {
                    
                     e.death(it);
                }
                
                return true;
            }
        }
        
        return false;
    }
    
}
