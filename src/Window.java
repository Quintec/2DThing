import java.awt.*;
import java.util.*;
import javax.swing.*;
public class Window extends JPanel {
    
    ArrayList<Sprite> sprites;
    
    public Window() {
        sprites = new ArrayList<Sprite>();
    }
    
    public void addSprite(Sprite s) {
        sprites.add(s);
    }
    
    public void removeSprite(Sprite s) {
        sprites.remove(s);
    }
    
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Sprite s : sprites) {
            g2.drawImage(s.getImage(), s.getX(), s.getY(), null);
        }
    }
}

