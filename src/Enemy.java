
import java.io.IOException;

public abstract class Enemy extends Sprite {
    
    private Character target;
    
    public Enemy(int xc, int yc, String path, Character main, int w, int h) throws IOException {
        super(xc, yc, path, w, h);
        this.target = main;
    }
    
    public abstract void update();
}