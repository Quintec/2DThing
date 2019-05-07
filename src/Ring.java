import java.awt.*;
import java.util.*;

public class Ring extends Weapon {

    public Ring(Character s, int d, Container jc) {
        super("Ring", 40, s, d, jc, 144, 144, 30);
    }

    @Override
    protected Object doInBackground() throws Exception {//TODO: Obey directions
        for (int i = 0; i < 60; i++) {
            if (i == 0)
            {
                this.init();
                this.wep.x -= 56;
                this.wep.y -= 56;
            }
            else {
                if (i == 20) {
                    this.wep.y += 10;
                }
                if (i % 5 == 0) {
                    this.stage++;
                    this.wep.setImage("Ring" + this.stage + ".png");
                    //this.wep.resizeTo(32+7*i,32+7*i);
                    //System.out.println("Hi");
                }
            }
            if (this.checkHits())
                return null;
            
            try { Thread.sleep(12);} catch (InterruptedException ex) {}
            this.parent.revalidate();
            this.parent.repaint();
        }
        return null;
    }

    @Override
    protected void done() {
        this.parent.remove(this.wep);
    }
    
    protected boolean checkHits() {
        Iterator<Enemy> it = Game.enemies.iterator();
        HashSet<Location> adj = new HashSet<Location>();
        
        Location l = new Location(Game.main.getX() / Character.SPRITE_SIZE, Game.main.getY() / Character.SPRITE_SIZE);
        adj.add(l);
        adj.add(l.getTranslated(1, 0));
        adj.add(l.getTranslated(-1, 0));
        adj.add(l.getTranslated(0, 1));
        adj.add(l.getTranslated(0, -1));
        adj.add(l.getTranslated(-1, -1));
        adj.add(l.getTranslated(-1, 1));
        adj.add(l.getTranslated(1, 1));
        adj.add(l.getTranslated(1, -1));
        
        while (it.hasNext()) {
            Enemy e = it.next();
            Location el = new Location(e.getX() / Character.SPRITE_SIZE, e.getY() / Character.SPRITE_SIZE);
            if (adj.contains(el) && !this.hits.contains(e)) {
                System.out.println("ringhit");
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
