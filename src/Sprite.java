import java.awt.Image;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Sprite {
    private int x;
    private int y;
    
    private Image img;
    
    public Sprite(int xc, int yc, String path) throws IOException{
        this.x = xc;
        this.y = yc;
        this.img = ImageIO.read(new File(path));
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
    
    public void incX(int nx) {
        x += nx;
    }
    
    public void incY(int ny) {
        y += ny;
    }
    
    public void setImage(Image i) {
        img = i;
    }
}
