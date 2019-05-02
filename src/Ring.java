import java.awt.*;

public class Ring extends Weapon {

    public Ring(Character s, int d, Container jc) {
        super("Ring", 10, s, d, jc, 144, 144);
    }

    @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 0; i < 60; i++) {
            if (i == 0)
            {
                this.init();
                this.wep.x-=56;
                this.wep.y-=40;
        }
            else {
                if (i % 5 == 0) {
                    this.stage++;
                    this.wep.setImage("Ring" + this.stage + ".png");
                    //this.wep.resizeTo(32+7*i,32+7*i);
                    //System.out.println("Hi");
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
