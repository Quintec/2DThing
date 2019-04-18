import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
public class Game{
  private JFrame frame;
  private KeyboardListener kbl;
  private Character main;
  
  private static int SPEED = 1;
  
  public static void main(String[] args) throws IOException{
    new Game().start();
  }
  
  public Game() throws IOException {
    frame = new JFrame();  
    
    frame.setSize(new Dimension(300, 300));
    main = new Character(0, 0, "ball.png");
    frame.getContentPane().add(main);
    
    kbl = new KeyboardListener();
    frame.addKeyListener(kbl);
    
    //frame.setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public void start() {
    while (true) {
      try { Thread.sleep(10);} catch (InterruptedException ex) {}
      HashSet<Integer> keys = kbl.getPresssed();
      if (keys.contains(32)) {//space
        //main.fireWeapon("Boomerang");
      }
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
      frame.repaint();
    }
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
      //use for press actions
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
      pressed.add(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      pressed.remove(e.getKeyCode());
    }
  }
  
  private class MouseShapeListener implements MouseListener, MouseMotionListener
  {
    private HashSet<Point> clicked;
    
    public MouseShapeListener()
    {
      clicked = new HashSet<Point>();
    }
    
    public HashSet<Point> getDragged()
    {
      HashSet<Point> temp = new HashSet<Point>(clicked);
      clicked.clear();
      return temp;
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
      clicked.add(new Point(e.getX(), e.getY())); 
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {
      clicked.clear();
      clicked.add(new Point(e.getX(), e.getY())); 
    }
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
    @Override
    public void mouseMoved(MouseEvent e) 
    {
    }
  }
}
