import java.awt.*;

public class Boomerang extends Weapon {

    public Boomerang(Character s, int d, Container jc) {
        super("Boomerang", 10, s, d, jc, 32, 32);
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
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        
        //System.out.println("second stage");
        this.stage = 1;
        this.wep.setImage(this.type + this.stage + ".png");
        this.parent.revalidate();
        this.parent.repaint();

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
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        return null;
    }

    @Override
    protected void done() {
        this.parent.remove(this.wep);
    }

}
