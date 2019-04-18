import java.awt.*;
import javax.swing.*;

public class Boomerang extends Weapon {

    public Boomerang(Character s, int sx, int sy, int d, Container jc) {
        super("Boomerang", s, sx, sy, d, jc);
    }
    
    @Override
    public void init() {
        this.parent.add(wep);
    }

    @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 0; i < 100; i++) {
            if (i == 0)
                this.init();
            try { Thread.sleep(10);} catch (InterruptedException ex) {}
        }
        return null;
    }

    @Override
    protected void done() {

    }

    @Override
    public boolean activate() {
        return true;//filler
    }

}
