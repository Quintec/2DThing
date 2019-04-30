import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.*;
import javax.swing.*;

public class Sprite extends JComponent {
    
    public static final int SPRITE_SIZE = 32;
    
    public static final List<SpriteLoc> WALKABLE = Arrays.asList(SpriteLoc.FLOOR, SpriteLoc.DOOR4);
    
    protected int x;
    protected int y;
    
    protected int width;
    protected int height;
    
    protected Image img;
    
    protected static BufferedImage spriteSheet;
    
    static {
        try {
            spriteSheet = ImageIO.read(new File("SpriteSheetAll32.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load spritesheet at SpriteSheetAll32.png"); 
        }
    }
    
    public Sprite(int xc, int yc, String path) {
        this(xc, yc, path, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    public Sprite(int xc, int yc, SpriteLoc sl) {
        this(xc, yc, sl, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    public Sprite(int xc, int yc, String path, int w, int h) {
        this.x = xc;
        this.y = yc;
        
        this.width = w;
        this.height = h;
        
        if (path != null) {
            try {
                this.img = ImageIO.read(new File(path)).getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        
        this.setLocation(0, 0);
    }
    
    public Sprite(int xc, int yc, String path, int w, int h, int dir) {//necessary?
        this.x = xc;
        this.y = yc;
        
        this.width = w;
        this.height = h;
        
        if (path != null) {
            try {
                this.img = ImageIO.read(new File(path)).getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
            double rotationRequired;
            if (dir==0) {
              rotationRequired = Math.toRadians(90);
            }
            else if (dir==1) {
              rotationRequired = Math.toRadians(180);  
            }
            else if (dir==3) {
              rotationRequired = Math.toRadians(270);
            }
            else {
              rotationRequired = 0;
            }
            
            double locationX = width / 2;
            double locationY = height / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage bimage = new BufferedImage(this.img.getWidth(null), this.img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(this.img, 0, 0, null);
    bGr.dispose();
            this.img = op.filter(bimage, null);

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        
        this.setLocation(0, 0);
    }
    
    public Sprite(int xc, int yc, SpriteLoc sl, int w, int h) {
        this.x = xc;
        this.y = yc;
        
        this.width = w;
        this.height = h;
        
        this.img = spriteSheet.getSubimage(sl.getX() * SPRITE_SIZE, sl.getY() * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    public static Image getImageAt(SpriteLoc sl) {
        return spriteSheet.getSubimage(sl.getX() * SPRITE_SIZE, sl.getY() * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    public Image getImage() {
        return img;
    }
 
    public void setImage(Image i) {
        img = i;
    }
    
    public void setImage(String path) {
        try {
            this.img = ImageIO.read(new File(path)).getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public void setImage(String path, int dir) {
        try {
            this.img = ImageIO.read(new File(path)).getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
            double rotationRequired;
            if (dir==0) {
              rotationRequired = Math.toRadians(90);
            }
            else if (dir==1) {
              rotationRequired = Math.toRadians(180);  
            }
            else if (dir==3) {
              rotationRequired = Math.toRadians(270);
            }
            else {
              rotationRequired = 0;
            }
            
            double locationX = width / 2;
            double locationY = height / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage bimage = new BufferedImage(this.img.getWidth(null), this.img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(this.img, 0, 0, null);
            bGr.dispose();
            
            this.img = op.filter(bimage, null);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public void resizeTo(int w, int h) {
        this.img = this.img.getScaledInstance(w, h, Image.SCALE_DEFAULT);
    }
    
    public void setX(int ax) {
        incX(ax - x);
    }
    
    public void setY(int ay) {
        incY(ay - y);
    }
    
    public boolean incX(int nx) {
        int ax = x + nx;
        //System.out.println(ax);
        if (0 <= ax && ax <= Game.BOARD_WIDTH - this.width  && (WALKABLE.contains(Game.map[(ax + 6)/SPRITE_SIZE][y/SPRITE_SIZE]))
                                                            && (WALKABLE.contains(Game.map[(ax + 26)/SPRITE_SIZE][y/SPRITE_SIZE]))
                                                            && (WALKABLE.contains(Game.map[(ax)/SPRITE_SIZE + 1][y/SPRITE_SIZE]))
                                                            && (WALKABLE.contains(Game.map[(ax + 6)/SPRITE_SIZE][y/SPRITE_SIZE + 1]))) {
            x += nx;
            return true;
        }
        return false;
    }
    
    public boolean incY(int ny) {
        int ay = y + ny;
        //System.out.println(ay);
        if (0 <= ay && ay <= Game.BOARD_HEIGHT - this.height && (WALKABLE.contains(Game.map[(x + 6)/SPRITE_SIZE][ay/SPRITE_SIZE]))
                                                             && (WALKABLE.contains(Game.map[(x + 6)/SPRITE_SIZE][ay/SPRITE_SIZE + 1]))
                                                             && (WALKABLE.contains(Game.map[(x + 26)/SPRITE_SIZE][ay/SPRITE_SIZE]))) {
            y += ny;
            return true;
        }
        return false;
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
