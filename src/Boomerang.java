import java.awt.*;

public class Boomerang extends Weapon {

    public Boomerang(Character s, int d, Container jc) {
        super("Boomerang", 10, s, d, jc, 32, 32);
    }

    @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 0; i < 50; i++) {
            if (i == 0)
                this.init();
            else {
                this.wep.x += 3;
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

        for (int i = 0; i < 50; i++) {
            this.wep.x -= 3;
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
