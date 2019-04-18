import java.awt.*;
import javax.swing.*;

public class Boomerang extends Weapon {

    public Boomerang(Character s, int d, Container jc) {
        super("Boomerang", s, d, jc, 20, 20);
    }

    @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 0; i < 100; i++) {
            if (i == 0)
                this.init();
            else {
                this.wep.x += 2;
                if (i % 16 == 0) {
                    this.stage++;
                    this.wep.setImage(this.type + this.stage + ".png");
                    this.wep.resizeTo(20, 20);
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

    @Override
    public boolean activate() {
        return true;//filler
    }

}
