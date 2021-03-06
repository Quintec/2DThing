import java.awt.*;
import java.util.*;

public class Ring extends Weapon {

    public Ring(Character s, int d, Container jc) {
        super("Ring", 25, s, d, jc, 144, 144, 20);
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
    
    protected boolean checkHits() {  //TODO: DO NOT SNAP TO GRID
        Iterator<Enemy> it = Game.enemies.iterator();
        HashSet<Location> adj = new HashSet<Location>();
        
        Location l = Game.main.getGridLoc();
        adj.add(l);
        
        
        if (Game.main.isUnder() || (Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) - 1][(int)(l.getY()) - 1]) && Sprite.WALKABLE.contains(Game.map[(int)(l.getX())][(int)(l.getY())])))
            adj.add(l.getTranslated(-1, -1));
        if (Game.main.isUnder() || (Sprite.WALKABLE.contains(Game.map[(int)(l.getX())][(int)(l.getY()) - 1]) && Sprite.WALKABLE.contains(Game.map[(int)(l.getX())][(int)(l.getY())])))
            adj.add(l.getTranslated(0, -1));
        if (Game.main.isUnder() || (Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) + 1][(int)(l.getY()) - 1]) && Sprite.WALKABLE.contains(Game.map[(int)(l.getX())][(int)(l.getY())])))
            adj.add(l.getTranslated(1, -1));
    
        if (Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) + 1][(int)(l.getY())]))
            adj.add(l.getTranslated(1, 0));
        if (Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) - 1][(int)(l.getY())]))
            adj.add(l.getTranslated(-1, 0));
        
        if (!Game.main.isUnder() || Sprite.WALKABLE.contains(Game.map[(int)(l.getX())][(int)(l.getY()) + 1]))
            adj.add(l.getTranslated(0, 1));
        if (!Game.main.isUnder() || Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) - 1][(int)(l.getY()) + 1]))
            adj.add(l.getTranslated(-1, 1));
        if (!Game.main.isUnder() || Sprite.WALKABLE.contains(Game.map[(int)(l.getX()) + 1][(int)(l.getY()) + 1]))
            adj.add(l.getTranslated(1, 1));
        
        
        /*Location l = new Location(Game.main.getLocation());
        System.out.println(l);
        adj.add(l.snapToGrid());
        adj.add(l.getTranslated(Character.SPRITE_SIZE, 0).snapToGrid());
        adj.add(l.getTranslated(-Character.SPRITE_SIZE, 0).snapToGrid());
        adj.add(l.getTranslated(0, Character.SPRITE_SIZE).snapToGrid());
        adj.add(l.getTranslated(0, -Character.SPRITE_SIZE).snapToGrid());
        adj.add(l.getTranslated(Character.SPRITE_SIZE, Character.SPRITE_SIZE).snapToGrid());
        adj.add(l.getTranslated(Character.SPRITE_SIZE, -Character.SPRITE_SIZE).snapToGrid());
        adj.add(l.getTranslated(-Character.SPRITE_SIZE, Character.SPRITE_SIZE).snapToGrid());
        adj.add(l.getTranslated(-Character.SPRITE_SIZE, -Character.SPRITE_SIZE).snapToGrid());*/
        
        while (it.hasNext()) {
            Enemy e = it.next();
            Location el = e.getGridLoc();
            if (adj.contains(el) && !this.hits.contains(e)) {
                //System.out.println("ringhit");
                e.incHP((int)(-this.getDmg() * Game.DAMAGE_MULTIPLIER));
                this.hits.add(e);
                if (e.getHP() <= 0) {
                    e.death(it);
                }
            }
        }
        
        return false;
    }
}
