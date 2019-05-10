import java.awt.Container;
import javax.swing.*;
public class Torch extends Sprite{
    
    private Container parent;
    private int stage;
    
    public Torch(int xc, int yc, Container c) {
        super(xc, yc, SpriteLoc.TORCH);
        this.parent = c;
        this.stage = 0;
    }
    
    public void ignite() {
        new TorchWorker().execute();
    }
    
    private class TorchWorker extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {
            while (true) {
                
            }
        }
    }
}
