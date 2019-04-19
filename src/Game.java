
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class Game {
  
  private JFrame frame;
  private KeyboardListener kbl;
  private MouseShapeListener ml;
  private Character main;
  
  public final int CIRCLE = 0;
  public final int HOR_LINE = 1;
  public final int VERT_LINE = 2;
  public final int SQUARE = 3;
  
  private static int SPEED = 1;
  
  public static void main(String[] args) throws IOException {
    new Game().start();
  }
  
  public Game() throws IOException {
    frame = new JFrame();
    
    frame.setSize(new Dimension(300, 300));
    main = new Character(0, 0, "ball.png", 20, 20);
    frame.getContentPane().add(main);
    
    kbl = new KeyboardListener();
    frame.addKeyListener(kbl);
    
    ml = new MouseShapeListener();
    frame.addMouseListener(ml);
    frame.addMouseMotionListener(ml);
    
    //frame.setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public void start() {
    while (true) {
      try { Thread.sleep(10);} catch (InterruptedException ex) {}
      HashSet<Integer> keys = kbl.getPresssed();
      if (keys.contains(37) || keys.contains(65)) {//left
        //System.out.println("37");
        main.incX(-SPEED);
      }
      if (keys.contains(38) || keys.contains(87)) {//up
        main.incY(-SPEED);
      }
      if (keys.contains(39) || keys.contains(68)) {//right
        main.incX(SPEED);
      }
      if (keys.contains(40) || keys.contains(83)) {//down
        main.incY(SPEED);
      }
      
      if (ml.finishedShape())
      {
        int shape = getShape(ml.getDragged());
        
        
        System.out.println(shape);//testing
        
      }
      frame.repaint();
    }
  }
  
  public int getShape(HashSet<Location> points)
  {
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxX = -Double.MAX_VALUE;
    double maxY = -Double.MAX_VALUE;
    for (Location p: points)
    {
      minX = Math.min(minX, p.getX());
      minY = Math.min(minY, p.getY());
      maxX = Math.max(maxX, p.getX());
      maxY = Math.max(maxY, p.getY());
    }
    double avgX = (minX+maxX)/2;
    double avgY = (minY+maxY)/2;
    maxX-=avgX;
    maxY-=avgY;
    for (Location p: points)
    {
      p.translate(-avgX,-avgY);
      p.dilate(1/Math.max(maxX, maxY), 1/Math.max(maxX, maxY));
    }
    
    double[] scores = new double[4];
    for (Location p: points)
    {
     // System.out.println(p.getX() + ", " + p.getY());
      //System.out.println(Math.pow((Math.sqrt(p.getX2()+p.getY2())-1),2)/points.size());
      scores[0] += Math.pow((Math.sqrt(p.getX2()+p.getY2())-1),2)/points.size();
      scores[1] += p.getY2()/points.size();
      scores[2] += p.getX2()/points.size();
      scores[3] += Math.pow(Math.max(p.getX2(), p.getY2())-1,2)/points.size();
    }
    int min = 0;
    for (int i = 0; i < scores.length; i++)
    {
      if (scores[i]<scores[min])
        min = i;
      //System.out.println(i+": "+scores[i]);
    }
    //if (min>0.3)
      //return -1;
    return min;
  }
  
  private class KeyboardListener implements KeyListener {
    
    private HashSet<Integer> pressed;
    
    public KeyboardListener() {
      pressed = new HashSet<Integer>();
    }
    
    private HashSet<Integer> getPresssed() {
      return pressed;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
      
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 32) {
        main.fireWeapon("Boomerang");
      } else {
        pressed.add(e.getKeyCode());
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      pressed.remove(e.getKeyCode());
    }
  }
  
  private class MouseShapeListener implements MouseListener, MouseMotionListener {
    
    private HashSet<Location> clicked;
    private boolean stillClicking;
    
    public MouseShapeListener() {
      clicked = new HashSet<Location>();
      stillClicking = false;
    }
    
    public HashSet<Location> getDragged() {
      HashSet<Location> temp = new HashSet<Location>(clicked);
      clicked.clear();
      return temp;
    }
    
    public boolean finishedShape()
    {
      return (!stillClicking)&&clicked.size()>0;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
      clicked.add(new Location(e.getPoint()));
      System.out.println(e.getX()+", "+e.getY());
      Graphics g = frame.getContentPane().getGraphics();
      g.setColor(Color.RED);
      g.fillOval(e.getX(), e.getY(), 10, 10);
      frame.repaint();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
      stillClicking = true;
      clicked.clear();
      clicked.add(new Location(e.getPoint()));
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
      stillClicking = false;
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
  }
}
