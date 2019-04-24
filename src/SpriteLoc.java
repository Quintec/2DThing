public enum SpriteLoc {
    BOY(0, 8), GIRL(3, 8), SKELETON(6, 8),
    OOZE(0, 12), BAT(3, 12), GHOST(6, 12), SPIDER(9, 12),
    
    FLOOR(8, 6), 
    WALL_VERTICAL(0, 8), WALL(0, 9), WALL_CORNER_BOTTOM(0, 10), WALL_CORNER_TOP(0, 11);
    
    private int x;
    private int y;
    
    public int getX(){return x;}
    public int getY(){return y;}
    
    private SpriteLoc(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
