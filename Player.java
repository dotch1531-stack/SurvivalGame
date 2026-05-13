import greenfoot.*;
public class Player extends Actor
{
    GreenfootImage spriteSheet;

    int frame = 0;
    int frameHeight = 64;
    int frameWidth = 64;
    int totalFrames = 4;

    int animationSpeed = 5;
    int counter = 0;

    public Player()
    {
        spriteSheet = new GreenfootImage("Characteranimation.png");
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
