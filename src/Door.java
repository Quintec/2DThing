import java.awt.Container;
import javax.swing.*;
public class Door extends Sprite {
    
    private boolean closed;
    private boolean locked;
    
    private Container parent;
    
    public Door(int xc, int yc, SpriteLoc sl, Container c) {
        super(xc, yc, sl);
        this.closed = sl == SpriteLoc.DOOR1;
        this.locked = false;
        
        this.parent = c;
    }
    
    public void toggle() {
        new DoorWorker().execute();
    }
    
    private class DoorWorker extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {
            if (closed) {
                for (int i = 2; i < 5; i++) {
                    try { Thread.sleep(30);} catch (InterruptedException ex) {}
                    SpriteLoc curr = SpriteLoc.valueOf("DOOR" + i);
                    Game.map[x][y] = curr;
                    Door.this.setImage(Sprite.getImageAt(curr));
                    Door.this.parent.revalidate();
                    Door.this.parent.repaint();
                }
            } else {
                for (int i = 3; i > 0; i--) {
                    try { Thread.sleep(30);} catch (InterruptedException ex) {}
                    SpriteLoc curr = SpriteLoc.valueOf("DOOR" + i);
                    Game.map[x][y] = curr;
                    Door.this.setImage(Sprite.getImageAt(curr));
                    Door.this.parent.revalidate();
                    Door.this.parent.repaint();
                }
            }
            return null;
        }
        
        protected void done() {
            closed = !closed;
        }
    }
}
