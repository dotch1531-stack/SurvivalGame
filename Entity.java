import greenfoot.*;
import java.util.*;

public abstract class Entity extends Actor
{
    
    
    // ===== WORLD POSITION =====
    public int worldX;
    public int worldY;

    public int lastWorldX;
    public int lastWorldY;

    public int lastX;
    public int lastY;

    // ===== COLLISION =====
    public boolean solid = false;
    public int collisionRadius = 30;

    public int hitboxWidth;
    public int hitboxHeight;

    // ===== BREAKING SYSTEM =====
    public boolean breakable = true;

    public double maxHealth = 1;
    public double health = 1;

    public BreakProgress progress;

    // ===== ANIMATION =====
    GreenfootImage spriteSheet;

    int animationSpeed = 12;
    int counter = 0;

    int frame = 0;
    int totalFrames = 2;

    public InventoryScreen inventory;
    
    public List<String> drops = Collections.emptyList();
    public int dropAmount = 1;
    
    int LocationX;
    int LocationY;
  
    int tile;
    // =========================
    // SCREEN POSITION
    // =========================
    public void updateScreenPosition(int cameraX, int cameraY)
    {
        
        setLocation(worldX - cameraX, worldY - cameraY);
        LocationX=worldX - cameraX;
        LocationY = worldY - cameraY;
        
        if (progress != null)
        {
            progress.updateScreenPosition(cameraX, cameraY, worldX, worldY);
        }
    }
    
    public void Drop(List<String> itemsToDrop, int amount)
    {
        inventory = new InventoryScreen();
        
        if(inventory == null)
        {
            return;
        }
    
        for(String item : itemsToDrop)
        {
            inventory.addItem(item, amount);
        }
    }

    // =========================
    // ANIMATION CONTROLLER
    // =========================
    public void Animation(String entity, int imageWidth, int imageHeight)
    {
        MyWorld world = (MyWorld)getWorld();
        
        int tileX = Math.floorDiv(LocationX, MyWorld.TILE_SIZE);
        int tileY = Math.floorDiv(LocationY, MyWorld.TILE_SIZE);
    
        tile = world.getTile(tileX, tileY);
        
        int dx = getX() - lastX;
        int dy = getY() - lastY;
    
        if (dx == 0 && dy == 0 && tile >= 3)
        {
            setIdle(entity, "Water");
        }
        if (dx == 0 && dy == 0 && tile < 3)
        {
            setIdle(entity, "");
        }
        else
        {
            // ===== DIAGONALS FIRST =====
            if (dx > 0 && dy < 0)
                animate("UpRight", entity, imageWidth, imageHeight);
    
            else if (dx < 0 && dy < 0)
                animate("UpLeft", entity, imageWidth, imageHeight);
    
            else if (dx > 0 && dy > 0)
                animate("DownRight", entity, imageWidth, imageHeight);
    
            else if (dx < 0 && dy > 0)
                animate("DownLeft", entity, imageWidth, imageHeight);
            
            // ===== STRAIGHT DIRECTIONS =====
            else if (Math.abs(dx) > Math.abs(dy))
            {
                if (dx > 0)
                    animate("Right", entity, imageWidth, imageHeight);
                else
                    animate("Left", entity, imageWidth, imageHeight);
            }
            else
            {
                if (dy > 0)
                    animate("DownRight", entity, imageWidth, imageHeight);
                else
                    animate("UpRight", entity, imageWidth, imageHeight);
            }
        }
    
        lastX = getX();
        lastY = getY();
    }

    // idle fallback
    private void setIdle(String entity, String WaterOrNot)
    {

        if ("Water".equals(WaterOrNot)){
        setImage(new GreenfootImage(
            "Animals/" + entity + "/" + entity + WaterOrNot + "/" + entity+ "Idle.png"
        ));}
        else{setImage(new GreenfootImage(
            "Animals/" + entity + "/"  + entity+ "Idle.png"
        ));}
    }
    
    

    // =========================
    // DAMAGE SYSTEM
    // =========================
    public void damage(double amount)
    {
        if (!breakable) return;

        health -= amount;

        if (health < 0)
            health = 0;

        MyWorld world = (MyWorld)getWorld();
        if (world == null) return;

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
            Drop(drops, dropAmount);
            world.removeObject(this);
        }
    }

    // =========================
    // PROXIMITY CHECK
    // =========================
    public boolean isNear(int x, int y)
    {
        int dx = x - worldX;
        int dy = y - worldY;

        return dx * dx + dy * dy <
               collisionRadius * collisionRadius;
    }

    // =========================
    // SPRITE ANIMATION
    // =========================
    public void animate(String where, String image, int frameWidth, int frameHeight)
    {
        // Load sprite sheet ONCE per animation direction
        String path;
        
        if(tile >= 3){path = "Animals/" + image + "/" + image + "Water" + "/" + image + where + ".png";}
        else{path = "Animals/" + image + "/" + image + where + ".png";}
        if(image == "duck"){totalFrames=1;}
        

        if (spriteSheet == null || !spriteSheet.getClass().equals(path))
        {
            spriteSheet = new GreenfootImage(path);
        }

        counter++;

        if (counter >= animationSpeed)
        {
            counter = 0;

            GreenfootImage frameImage =
                new GreenfootImage(frameWidth, frameHeight);

            frameImage.drawImage(
                spriteSheet,
                -frame * frameHeight, // FIXED (was frameHeight)
                0
            );

            setImage(frameImage);

            frame++;

            if (frame >= totalFrames)
            {
                frame = 0;
            }
        }
    }

    // =========================
    // SCREEN CHECK
    // =========================
    public boolean onScreen()
    {
        int w = getWorld().getWidth();
        int h = getWorld().getHeight();
    
        return getX() >= 7 &&
               getX() <= w - 7 &&
               getY() >= 7 &&
               getY() <= h - 7;
    }
}