import greenfoot.*;

public abstract class WorldObject extends Actor
{
    public int worldX;
    public int worldY;

    // ===== COLLISION =====
    public boolean solid = false;

    public int collisionRadius = 30;

    public int hitboxWidth = 20;
    public int hitboxHeight = 20;

    // ===== BREAKING SYSTEM =====
    public boolean breakable = false;

    public int maxHealth = 1;
    public int health = 1;

    public BreakProgress progress;

    // ===== SCREEN POSITION =====
    public void updateScreenPosition(int cameraX, int cameraY)
    {
        setLocation(worldX - cameraX, worldY - cameraY);

        // keep progress bar attached
        if(progress != null)
        {
            progress.setLocation(getX(), getY() + 40);
        }
    }

    // ===== DAMAGE SYSTEM =====
    public void damage(int amount)
    {
        if(!breakable)
            return;

        health -= amount;

        MyWorld world = (MyWorld)getWorld();
        if(world == null)
            return;

        // create progress bar once
        if(progress == null)
        {
            progress = new BreakProgress();

            world.addObject(
                progress,
                getX(),
                getY() + 40
            );
        }

        // clamp health
        if(health < 0)
            health = 0;

        // calculate stage from health %
        int totalStages = 5;

        double percent =
            (double)(maxHealth - health) / (double)maxHealth;

        int stage = (int)(percent * (totalStages - 1));

        progress.setStage(stage);

        // destroy object when dead
        if(health <= 0)
        {
            if(progress != null)
            {
                world.removeObject(progress);
            }

            world.removeObject(this);
        }
    }

    // ===== PROXIMITY CHECK =====
    public boolean isNear(int x, int y)
    {
        int dx = x - worldX;
        int dy = y - worldY;

        return dx * dx + dy * dy <
               collisionRadius * collisionRadius;
    }
}