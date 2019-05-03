import java.awt.Container;
public class Arrow extends Weapon {

    public Arrow(Character s, int d, Container jc) {
        super("Arrow", 10, s, d, jc, 32, 32);
    }

   @Override
    protected Object doInBackground() throws Exception {
        int i = 0;
        while (true) {
            i++;
            if (i == 1)
                this.init();
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
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }

    }
    
}
