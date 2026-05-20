import greenfoot.*;
public class Player extends Actor
{
    GreenfootImage spriteSheet;

    int frame = 0;
    int frameHeight = 120;
    int frameWidth = 120;
    int totalFrames = 2;

    int animationSpeed = 15;
    int counter = 0;

    public Player()
    {
        spriteSheet = new GreenfootImage("JoeIdle.png");
    }

    public void act()
    {
        animate();
    }

    public void animate()
    {
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
