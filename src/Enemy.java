public abstract class Enemy extends Character {
    
    public Character target;
    
    private int hitDmg;
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int dmg, int hp) {
        super(xc, yc, sl);
        this.target = main;
        this.hitDmg = dmg;
        this.setHP(hp);
    }
    
    public abstract void update();
    
    public int getHitDmg() {
        return hitDmg;
    }
    
}