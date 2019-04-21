
public enum CharLoc {
    BOY(0, 8), GIRL(3, 8), SKELETON(6, 8),
    OOZE(0, 12), BAT(3, 12), GHOST(6, 12), SPIDER(9, 12);
    
    private int x;
    private int y;
    
    public int getX(){return x;}
    public int getY(){return y;}
    
    private CharLoc(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
