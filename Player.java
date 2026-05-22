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
    
    int hitboxWidth = 12;
    int hitboxHeight = 18;

    public Player()
    {
        spriteSheet = new GreenfootImage("Player/JoeIdle.png");
    }

    public void act()
    {
        movementAnimation();
        
        if(Greenfoot.isKeyDown("e")){Greenfoot.delay(10);}
    }
    
    public void inWater(){up = "UpWater";down = "DownWater";left = "LeftWater";right = "RightWater";idle = "IdleWater";}
    public void notInWater(){up = "Up";down = "Down";left = "Left";right = "Right";idle = "Idle";}
    
    public void movementAnimation()
    {
        if(Greenfoot.isKeyDown("w")){animate(up);}
        else if(Greenfoot.isKeyDown("s")){animate(down);}
        else if(Greenfoot.isKeyDown("a")){animate(left);}
        else if(Greenfoot.isKeyDown("d")){animate(right);}
        else {setImage(new GreenfootImage("Player/Joe"+idle+".png"));}
    }
    
    public boolean isNearObject(Class<? extends Actor> cls, int radius)
    {
        return !getObjectsInRange(radius, cls).isEmpty();
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
}
