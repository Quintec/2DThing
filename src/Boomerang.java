import java.awt.*;
import java.util.Iterator;

public class Boomerang extends Weapon {

    public Boomerang(Character s, int d, Container jc) {
        super("Boomerang", 10, s, d, jc, 32, 32, 10);
    }

    @Override
    protected Object doInBackground() throws Exception {
        int dist = 50;
        for (int i = 0; i < 50; i++) {
            if (i == 0)
                this.init();
            else {
                if (this.dir == Character.DOWN) {
                if (!this.wep.incY(3)) {
                    dist = i;
                    break;
                }
              }
              else if (this.dir == Character.LEFT) {
                if (!this.wep.incX(-3)) {
                    dist = i;
                    break;
                }
              }
              else if (this.dir == Character.RIGHT) {
                  if (!this.wep.incX(3)) {
                    dist = i;
                    break;
                }
              }
              else if (this.dir == Character.UP) {
                  if (!this.wep.incY(-3)) {
                    dist = i;
                    break;
                }
              }
                if (i % 10 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png");
                }
            }
            this.checkHits();
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        
        //System.out.println("second stage");
        this.stage = 1;
        this.wep.setImage(this.type + this.stage + ".png");
        this.parent.revalidate();
        this.parent.repaint();
        
        this.hits.clear();

        for (int i = dist; i >= 0; i--) {
            if (this.dir == Character.DOWN) {
                if (!this.wep.incY(-3))
                    return null;
              }
              else if (this.dir == Character.LEFT) {
                if (!this.wep.incX(3))
                    return null;
              }
              else if (this.dir == Character.RIGHT) {
                  if (!this.wep.incX(-3))
                      return null;
              }
              else if (this.dir == Character.UP) {
                  if (!this.wep.incY(3))
                      return null;
              }
            if (i % 10 == 0) {
                this.stage++;
                this.wep.setImage(this.type + this.stage + ".png");
            }
            if (this.checkHits())
                return null;
            
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        return null;
    }
    
    protected boolean checkHits() {
        Rectangle me = this.wep.getBounds();
        Iterator<Enemy> it = Game.enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getBounds().intersects(me) && !this.hits.contains(e)) {
                System.out.println("boomerang hit");
                e.incHP(-this.getDmg());
                this.hits.add(e);
                if (e.getHP() <= 0) {
                    it.remove();
                    this.parent.remove(e);
                }
            }
        }
        return false;
    }

}
