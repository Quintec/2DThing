import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.lang.reflect.*;

public class Sprite extends JComponent{
    
    protected int x;
    protected int y;
    
    protected Image img;
    
    public Sprite(int xc, int yc, String path) {
        this.x = xc;
        this.y = yc;
        
        try {
            this.img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        this.setLocation(0, 0);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Image getImage() {
        return img;
    }
 
    public void setImage(Image i) {
        img = i;
    }
    
    public void setX(int ax) {
        x = ax;
    }
    
    public void setY(int ay) {
        y = ay;
    }
    
    public void incX(int nx) {
        x += nx;
    }
    
    public void incY(int ny) {
        y += ny;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        //System.out.println("painting");
        super.paintComponent(g);
        this.setLocation(x, y);
        Graphics2D g2 = (Graphics2D) g;
        //g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        g2.drawImage(img, 0, 0, null);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }
    
}
