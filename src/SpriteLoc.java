public enum SpriteLoc {
    BOY(0, 8), GIRL(3, 8), SKELETON(6, 8),
    OOZE(0, 12), BAT(3, 12), GHOST(6, 12), SPIDER(9, 12),
    
    FLOOR(8, 6), FLOOR2(8,6), DOOR1(4, 1), DOOR2(5, 1), DOOR3(6, 1), DOOR4(7, 1),
    WALL_HORIZONTAL(8, 0), WALL_VERTICAL(9, 0), WALL_CORNER_BOTTOM(10, 0), WALL_CORNER_TOP(11, 0),
    
    STAIRS_EXIT(9,7), STAIRS_ENTER(10,7),
    
    WELL(10, 8), STATUE(12,8), TORCH0(13, 13), TORCH1(14, 13), TORCH2(15, 13);
    
    private int x;
    private int y;
    
    public int getX(){return x;}
    public int getY(){return y;}
    
    private SpriteLoc(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
