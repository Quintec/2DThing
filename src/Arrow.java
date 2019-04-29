import java.awt.Container;
public class Arrow extends Weapon {

    public Arrow(Character s, int d, Container jc) {
        super("Arrow", 10, s, d, jc, 32, 32);
    }

   @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 1; i < 54; i++) {
            if (i == 1)
                this.init();
            else {
              if (this.dir==0) {
                this.wep.y += 3;
              }
              else if (this.dir==1) {
                this.wep.x-=3;
              }
              else if (this.dir==2) {
                this.wep.x += 3;
              }
              else if (this.dir==3) {
                this.wep.y -=3;
              }
                if (i % 9 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png", this.dir);
                }
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
