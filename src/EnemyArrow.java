import java.awt.Container;
import java.awt.Rectangle;
import java.util.Iterator;
public class EnemyArrow extends Weapon {
    
    private boolean hit;

    public EnemyArrow(Character s, int d, Container jc) {
        super("Fireball", 0, s, d, jc, 32, 32, 10);
        this.hit = false;
    }

   @Override
    protected Object doInBackground() throws Exception {
        for (int i = 1; i < 162; i++) {
            if (i == 1) {
                this.init();

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
              
              if (i % 18 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png", this.dir);
               }
                /*if (i % 9 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png", this.dir);
                }*/
                
            }
            
            if (!hit) {
                this.checkHits();
            }
            
            
            try { Thread.sleep(3);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        return null;
    }
    
    @Override
    protected boolean checkHits() {
        Rectangle me = this.wep.getBounds();
        //me.grow(-12, 0);
        //me.translate(6,0);
        if (Game.main.getBounds().intersects(me))
        {
          Game.main.incHP(-this.getDmg());
          this.hit = true;
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
