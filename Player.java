import greenfoot.*;

public class Player extends Actor
{
    GreenfootImage spriteSheet;

    int frame = 0;
    int frameHeight = 120;
    int frameWidth = 120;
    int totalFrames = 2;

    int animationSpeed = 12;
    int counter = 0;

    String up = "Up";
    String down = "Down";
    String left = "Left";
    String right = "Right";
    String idle = "Idle";

    int hitboxWidth = 20;
    int hitboxHeight = 55;

    public int worldX;
    public int worldY;

    public boolean solid = true;

    public int collisionRadius = 30;

    public boolean breakable = true;

    public double maxHealth = 15000000;
    public double health = maxHealth;

    public BreakProgress progress;

    Hitbox hitbox = new Hitbox();

    public Player()
    {
        spriteSheet = new GreenfootImage("Player/JoeIdle.png");
    }

    public void addedToWorld(World world)
    {
        world.addObject(hitbox, getX(), getY());
    }
    
    public void act()
    {
        movementAnimation();
        updateHitbox();
        hitCheck();
    }
    
    

    
    
    public void updateHitbox()
    {
    
        MouseInfo mouse = Greenfoot.getMouseInfo();
    
        if(mouse == null) return;
    
        int mx = mouse.getX();
        int my = mouse.getY();
    
        double dx = mx - getX();
        double dy = my - getY();
    
        double angle = Math.atan2(dy, dx);
    
        int offset = 60;
    
        int hx = (int)(getX() + Math.cos(angle) * offset);
        int hy = (int)(getY() + Math.sin(angle) * offset);
    
        hitbox.setLocation(hx, hy);
        hitbox.setRotation((int)Math.toDegrees(angle) + 90);
    }
    
        
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
            world.addObject(progress, 400, 440);
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
    
        
    
    //ANIMATE WORKS - DO NOT TOUCH
    public void inWater(){up = "UpWater";down = "DownWater";left = "LeftWater";right = "RightWater";idle = "IdleWater";}
    public void notInWater(){up = "Up";down = "Down";left = "Left";right = "Right";idle = "Idle";}
    
    public void movementAnimation()
    {
        if(Greenfoot.isKeyDown("w")){animate(up);}
        else if(Greenfoot.isKeyDown("s")){animate(down);}
        else if(Greenfoot.isKeyDown("a")){animate(left);}
        else if(Greenfoot.isKeyDown("d")){animate(right);}
        else if(idle == "IdleWater") {animate(idle);}
        else {setImage(new GreenfootImage("Player/Joe"+idle+".png"));}
    }
    
    
    
    public void animate(String where)
    {
        spriteSheet = new GreenfootImage("Player/Joe" + where + ".png");
        
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
    
    
    //HITTING WORKS - DO NOT TOUCH
    public void hitCheck()
    {
        if(Greenfoot.mousePressed(null))
        {
            hitbox.checkHits();
        }
    }
    

}
