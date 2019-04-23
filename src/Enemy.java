public abstract class Enemy extends Sprite {
    
    private Character target;
    
    public Enemy(int xc, int yc, String path, Character main, int w, int h) {
        super(xc, yc, path, w, h);
        this.target = main;
    }
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main, int w, int h) {
        super(xc, yc, sl, w, h);
        this.target = main;
    }
    
    public Enemy(int xc, int yc, String path, Character main) {
        super(xc, yc, path, SPRITE_SIZE, SPRITE_SIZE);
        this.target = main;
    }
    
    public Enemy(int xc, int yc, SpriteLoc sl, Character main) {
        super(xc, yc, sl, SPRITE_SIZE, SPRITE_SIZE);
        this.target = main;
    }
    
    public abstract void update();
}