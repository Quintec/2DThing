import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
//TODO: PAINT COMPONENT IN SPRITE INSTEAD OF WINDOW
public class Sprite extends JComponent{
    private int x;
    private int y;
    
    private Image img;
    
    public Sprite(int xc, int yc, String path) throws IOException{
        this.x = xc;
        this.y = yc;
        this.img = ImageIO.read(new File(path));
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
    
    public void setImage(Image i) {
        img = i;
    }
    
    public void fireBoom() {
        
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
    
    private class Boomerang extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        protected void done() {
            
        }
    }
}
