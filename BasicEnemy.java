import java.io.*;
public class BasicEnemy implements Enemy
{
  private Sprite sprite, main;
  public BasicEnemy(Sprite main) 
  {
    this.main = main;
    try{
    sprite = new Sprite(0, 0, "ball.png");
    }
    catch (IOException E)
    {
      
    }
  }
  public void update()
  {
    
  }
}