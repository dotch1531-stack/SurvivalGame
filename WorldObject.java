import greenfoot.*;
import java.util.*;

public abstract class WorldObject extends Actor
{
    public int worldX;
    public int worldY;

    // ===== COLLISION =====
    public boolean solid = true;
    
    public int collisionRadius = 30;

    public int hitboxWidth = 240;
    public int hitboxHeight = 120;

    // ===== BREAKING SYSTEM =====
    public boolean breakable = true;

    public double maxHealth = 1;
    public double health = 1;

    public BreakProgress progress;
    
    public InventoryScreen inventory = new InventoryScreen();
    
    public List<String> drops = Collections.emptyList();
    public int dropAmount = 1;  
    


    // ===== SCREEN POSITION =====
    public void updateScreenPosition(int cameraX, int cameraY)
    {
        setLocation(worldX - cameraX, worldY - cameraY);
    
        if (progress != null)
        {
            progress.updateScreenPosition(cameraX, cameraY, worldX, worldY);
        }
    }
    
    public void Drop(List<String> itemsToDrop, int amount)
    {
        for(String item : itemsToDrop){inventory.addItem(item,amount);}
    }

    // ===== DAMAGE SYSTEM =====
    public void damage(double amount)
    {
        if (!breakable)
            return;
    
        health -= amount;
        Drop(drops, dropAmount);
        if (health < 0)
            health = 0;
    
        MyWorld world = (MyWorld)getWorld();
        if (world == null)
            return;
    
        if (progress == null)
        {
            progress = new BreakProgress();
            world.addObject(progress, worldX, worldY + 40);
        }
    
        int totalStages = 17;
    
        double percent =
            (double)(maxHealth - health) / (double)maxHealth;
    
        int stage = (int)(percent * (totalStages - 1));
    
        if (progress != null)
        {
            progress.setStage(stage);
        }
    
        if (health <= 0)
        {
            if (progress != null)
            {
                world.removeObject(progress);
                progress = null;
            }
            
            
            world.removeObject(this);

            
            return;
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