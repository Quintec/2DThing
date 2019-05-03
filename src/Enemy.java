public abstract class Enemy extends Sprite {
    
    public Character target;
    
    private int hitDmg;
    
    public Enemy(int xc, int yc, String path, Character main, int w, int h, int dmg) {
        super(xc, yc, path, w, h);
        this.target = main;
        this.hitDmg = dmg;
    }
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int w, int h, int dmg) {
        super(xc, yc, sl, w, h);
        this.target = main;
        this.hitDmg = dmg;
    }
    
    public Enemy(int xc, int yc, String path, Character main, int dmg) {
        super(xc, yc, path, SPRITE_SIZE, SPRITE_SIZE);
        this.target = main;
        this.hitDmg = dmg;
    }
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int dmg) {
        super(xc, yc, sl, SPRITE_SIZE, SPRITE_SIZE);
        this.target = main;
        this.hitDmg = dmg;
    }
    
    public abstract void update();
    
    public int getHitDmg() {
        return hitDmg;
    }
    
}