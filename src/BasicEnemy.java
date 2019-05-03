public class BasicEnemy extends Enemy {
  private final double SPEED = 25;
  
    public BasicEnemy(int xc, int yc, String path, Character main) {
        super(xc, yc, path, main);
    }
    
    public BasicEnemy(int xc, int yc, SpriteLoc sl, Character main) {
        super(xc, yc, sl, main);
    }

    @Override
    public void update() {
        int dx = this.target.x-this.x;
        int dy = this.target.y-this.y;
        this.incX((int)(SPEED*dx/Math.sqrt(dx*dx+dy*dy)));
        this.incY((int)(SPEED*dy/Math.sqrt(dx*dx+dy*dy)));
        
    }
}
