import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;
public class Game{
    private JFrame frame;
    
    public static void main(String[] args) {
        new Game().start();
    }
    
    public Game() {
        frame = new JFrame();  
        
        frame.getContentPane();
    }
    
    public void start() {
        
    }

    private class KeyboardListener implements KeyListener {
        private HashSet<Integer> pressed;
        
        public KeyboardListener() {
            pressed = new HashSet<Integer>();
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
}
