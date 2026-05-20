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

    public Player()
    {
        spriteSheet = new GreenfootImage("Player/JoeIdle.png");
    }

    public void act()
    {
        movement();
    }
    
    public void movement()
    {
        if(Greenfoot.isKeyDown("w")){animate("Up");}
        else if(Greenfoot.isKeyDown("s")){animate("Down");}
        else if(Greenfoot.isKeyDown("a")){animate("Left");}
        else if(Greenfoot.isKeyDown("d")){animate("Right");}
        else {setImage(new GreenfootImage("Player/JoeIdle.png"));}
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
