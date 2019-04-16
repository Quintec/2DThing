
import java.io.IOException;

public abstract class Enemy extends Sprite {
    
    private Sprite target;
    
    public Enemy(int xc, int yc, String path, Sprite main) throws IOException {
        super(xc, yc, path);
        this.target = main;
    }
    
    public abstract void update();
}