import java.awt.Point;

public class Location
{
  double x,y;
  public Location(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  public Location(Point p)
  {
    x = p.getX();
    y = p.getY();
  }
  
  public double getX()
  {
    return x;
  }
  public double getY()
  {
    return y;
  }
  
  public double getX2()
  {
    return x*x;
  }
  public double getY2()
  {
    return y*y;
  }
  
  public void setX(double x2)
  {
    x = x2;
  }
  public void setY(double y2)
  {
    y = y2;
  }
  
  public void translate(double dx, double dy)
  {
    x+=dx;
    y+=dy;
  }
  
  public void dilate(double rx, double ry)
  {
    x*=rx;
    y*=ry;
  }
}