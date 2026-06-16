import greenfoot.*;

public class IntroWorld extends World
{
    private int timer = 0;

    private Helicopter heli;

    public IntroWorld()
    {
        super(800,800,1);

        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        heli = new Helicopter();

        addObject(heli,-150,250);
    }

    public void act()
    {
        timer++;

        drawScene();

        if(timer > 700)
        {
            Greenfoot.setWorld(new MyWorld());
        }
        if (Greenfoot.isKeyDown("space")){
            timer=1000;
        }

    }
    private void drawScene()
    {
        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        if(timer < 120)
        {
            drawText("Tag 1",350,400);
        }

        else if(timer < 240)
        {
            drawText("Flug über dem Pazifik",250,400);
        }

        else if(timer < 450)
        {
            heli.setLocation(
                -150 + (timer-240)*3,
                250
            );
        }

        else if(timer < 550)
        {
            drawText("TRIEBWERKSAUSFALL",220,100);

            heli.setLocation(
                480,
                250
            );
        }

        else if(timer < 700)
        {
            heli.setRotation(45);

            heli.setLocation(
                480 + (timer-550)*2,
                250 + (timer-550)*2
            );
        }
    }

    private void drawText(String text,int x,int y)
    {
        GreenfootImage img =
            new GreenfootImage(text,40,Color.WHITE,Color.BLACK);

        getBackground().drawImage(img,x,y);
    }
}