public class BasicEnemy extends Enemy {

    public BasicEnemy(int xc, int yc, String path, Character main) {
        super(xc, yc, path, main);
    }
    
    public BasicEnemy(int xc, int yc, SpriteLoc sl, Character main) {
        super(xc, yc, sl, main);
    }

    @Override
    public void update() {
        
    }
}
