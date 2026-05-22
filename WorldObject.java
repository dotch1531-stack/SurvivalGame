import greenfoot.*;

public abstract class WorldObject extends Actor
{
    public int worldX;
    public int worldY;
    
    public boolean solid = false;
    // size used for collision (override if needed)
    public int collisionRadius = 30;
    
    public int hitboxWidth;
    public int hitboxHeight;

    public void updateScreenPosition(int cameraX, int cameraY)
    {
        setLocation(worldX - cameraX, worldY - cameraY);
    }

    public boolean isNear(int x, int y)
    {
        int dx = x - worldX;
        int dy = y - worldY;
        return dx * dx + dy * dy < collisionRadius * collisionRadius;
    }
}