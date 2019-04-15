import javax.swing.*;
public class Game {
    private JFrame frame;
    private Window window;
    
    public static void main(String[] args) {
        new Game().start();
    }
    
    public Game() {
        frame = new JFrame();
        window = new Window();
        
        frame.getContentPane().add(window);
    }
    
    public void start() {
        
    }
}
