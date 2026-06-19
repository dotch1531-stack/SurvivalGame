import greenfoot.*;

public class TentPlayer extends Actor
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
    
    
    

    public TentPlayer()
    {
        spriteSheet = new GreenfootImage("Player/JoeIdle.png");
        setImage(spriteSheet);
    }

    public void act()
    {
        moveInsideTent();
        movementAnimation();

        if(Greenfoot.isKeyDown("e"))
        {
            Greenfoot.setWorld(MyWorld.instance);
            Greenfoot.delay(20);
        }
    }

    private void moveInsideTent()
    {
        int speed = 4;

        int newX = getX();
        int newY = getY();

        if(Greenfoot.isKeyDown("w")) newY -= speed;
        if(Greenfoot.isKeyDown("s")) newY += speed;
        if(Greenfoot.isKeyDown("a")) newX -= speed;
        if(Greenfoot.isKeyDown("d")) newX += speed;

        // Grenzen des 3x3 Raums
        if(newX < 280) newX = 280;
        if(newX > 520) newX = 520;

        if(newY < 280) newY = 280;
        if(newY > 520) newY = 520;

        setLocation(newX, newY);
    }

    public void movementAnimation()
    {
        if(Greenfoot.isKeyDown("w")) animate(up);
        else if(Greenfoot.isKeyDown("s")) animate(down);
        else if(Greenfoot.isKeyDown("a")) animate(left);
        else if(Greenfoot.isKeyDown("d")) animate(right);
        else setImage(new GreenfootImage("Player/JoeIdle.png"));
    }

    public void animate(String where)
    {
        spriteSheet =
            new GreenfootImage("Player/Joe" + where + ".png");

        counter++;

        if(counter >= animationSpeed)
        {
            counter = 0;

            GreenfootImage frameImage =
                new GreenfootImage(frameWidth, frameHeight);

            frameImage.drawImage(
                spriteSheet,
                -frame * frameWidth,
                0
            );

            setImage(frameImage);

            frame++;

            if(frame >= totalFrames)
            {
                frame = 0;
            }
        }
    }
    

}