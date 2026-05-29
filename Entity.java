import greenfoot.*;

public abstract class Entity extends Actor
{
    public int worldX;
    public int worldY;

    // ===== COLLISION =====
    public boolean solid = false;
    
    public int collisionRadius = 30;

    public int hitboxWidth = 240;
    public int hitboxHeight = 120;

    // ===== BREAKING SYSTEM =====
    public boolean breakable = true;

    public double maxHealth = 1;
    public double health = 1;

    public BreakProgress progress;
    
    GreenfootImage spriteSheet;
    
    int animationSpeed = 12;
    int counter = 0;
    
    int frame = 0;
    int totalFrames = 2;
    
    public int lastWorldX;
    public int lastWorldY;
    
    public int lastX;
    public int lastY;

    // ===== SCREEN POSITION =====
    public void updateScreenPosition(int cameraX, int cameraY)
    {
        setLocation(worldX - cameraX, worldY - cameraY);
    
        if (progress != null)
        {
            progress.updateScreenPosition(cameraX, cameraY, worldX, worldY);
        }
    }

    public void Animation(String entity)
    {
        int dx = getX() - lastX;
        int dy = getY() - lastY;

    
        if (Math.abs(dx) > Math.abs(dy))
        {
            if (dx > 0)
            {
                animate("Right", entity, 120, 120);
            }
            else if (dx < 0)
            {
                animate("Left", entity, 120, 120);
            }
        }
        else
        {


            setImage(new GreenfootImage(
                "Animals/" + entity + "/" + entity + "Idle.png"
            ));
            
        }
    
        lastX = getX();
        lastY = getY();
    }
    
    // ===== DAMAGE SYSTEM =====
    public void damage(double amount)
    {
        if (!breakable)
            return;
    
        health -= amount;
    
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
    
    public void animate(String where, String image, int frameWidth, int frameHeight)
    {
        spriteSheet = new GreenfootImage("Animals/" + image +"/" + image + where + ".png");
        
        counter++;

        if(counter >= animationSpeed)
        {
            counter = 0;

            // Create image for one frame
            GreenfootImage frameImage =
                new GreenfootImage(frameWidth, frameHeight);

            // Copy part of sprite sheet
            frameImage.drawImage(
                spriteSheet,
                -frame * frameHeight,0
            );

            setImage(frameImage);

            // Next frame
            frame++;

            if(frame >= totalFrames)
            {
                frame = 0;
            }
        }
    }
    
    public boolean onScreen()
    {
        return getX() >= 2 &&
               getX() <= 798 &&
               getY() >= 2 &&
               getY() <= 798;
    }
    
}