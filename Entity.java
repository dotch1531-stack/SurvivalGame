import greenfoot.*;

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

    public int hitboxWidth = 240;
    public int hitboxHeight = 120;

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

    // =========================
    // SCREEN POSITION
    // =========================
    public void updateScreenPosition(int cameraX, int cameraY)
    {
        setLocation(worldX - cameraX, worldY - cameraY);

        if (progress != null)
        {
            progress.updateScreenPosition(cameraX, cameraY, worldX, worldY);
        }
    }

    // =========================
    // ANIMATION CONTROLLER
    // =========================
    public void Animation(String entity)
{
    int dx = getX() - lastX;
    int dy = getY() - lastY;

    if (dx == 0 && dy == 0)
    {
        setIdle(entity);
    }
    else
    {
        // ===== DIAGONALS FIRST =====
        if (dx > 0 && dy < 0)
            animate("UpRight", entity, 120, 120);

        else if (dx < 0 && dy < 0)
            animate("UpLeft", entity, 120, 120);

        /*else if (dx > 0 && dy > 0)
            animate("DownRight", entity, 120, 120);

        else if (dx < 0 && dy > 0)
            animate("DownLeft", entity, 120, 120);
        */
        // ===== STRAIGHT DIRECTIONS =====
        else if (Math.abs(dx) > Math.abs(dy))
        {
            if (dx > 0)
                animate("Right", entity, 120, 120);
            else
                animate("Left", entity, 120, 120);
        }
        /*else
        {
            if (dy > 0)
                animate("DownLeft", entity, 120, 120);
            else
                animate("UpRight", entity, 120, 120);
        }*/
    }

    lastX = getX();
    lastY = getY();
}

    // idle fallback
    private void setIdle(String entity)
    {
        setImage(new GreenfootImage(
            "Animals/" + entity + "/" + entity + "Idle.png"
        ));
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
        String path = "Animals/" + image + "/" + image + where + ".png";

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