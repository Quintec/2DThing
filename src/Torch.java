import java.awt.Container;
import javax.swing.*;
public class Torch extends Sprite implements Animated {
    
    private Container parent;
    private int stage;
    private boolean up;
    
    private int count;
    
    public Torch(int xc, int yc, Container c) {
        super(xc, yc, SpriteLoc.TORCH0);
        this.parent = c;
        this.stage = 0;
        this.up = true;
        this.count = 1;
    }

    @Override
    public void animate() {
        if (count == 0) {
            if (up)
                stage++;
            else
                stage--;
            if (stage == 2 || stage == 0)
                up = !up;
            Game.map[x][y] = SpriteLoc.valueOf("TORCH" + stage);
            Torch.this.setImage(Sprite.getImageAt(SpriteLoc.TORCH0.getX() + stage, SpriteLoc.TORCH0.getY()));
            Torch.this.parent.revalidate();
            Torch.this.parent.repaint();
        }
        count = (count + 1) % 10;
    }

}
