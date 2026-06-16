import greenfoot.*;

public class IntroWorld extends World
{
    private int timer = 0;
    private int state = 0;

    private Helicopter heli;

    private GreenfootSound windSound       =  new GreenfootSound("wind.mp3");
    private GreenfootSound engineSound     =  new GreenfootSound("heli_engine.mp3");
    private GreenfootSound crashSound      =  new GreenfootSound("crash.mp3");
    private GreenfootSound engineFailSound =  new GreenfootSound("engine_fail.mp3");
    private GreenfootSound wakeUpSound     =  new GreenfootSound("wake_up.mp3");
    
    public IntroWorld()
    {
        super(800, 800, 1);

        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        heli = new Helicopter();
        addObject(heli, -200, 300);
    }

    public void act()
    {
        timer++;

        switch(state)
        {
            case 0: flight(); break;
            case 1: engineFailure(); break;
            case 2: crash(); break;
            case 3: blackout(); break;
            case 4: wakeUp(); break;
        }

        if (Greenfoot.isKeyDown("space"))
        {
            engineSound.stop();
            windSound.stop();
            crashSound.stop();
            engineFailSound.stop();
            Greenfoot.setWorld(new MyWorld());
        }
    }

    // ✈️ FLUG
    private void flight()
    {
        if (timer == 1)
        {
            windSound.playLoop();
            engineSound.playLoop();
        }

        heli.setLocation(-200 + timer * 3, 300);

        drawText("Flug über dem Pazifik...", 250, 400);

        if (timer > 250)
        {
            state = 1;
            timer = 0;
        }
    }

    // ⚠️ ENGINE FAILURE
    private void engineFailure()
    {
        if (timer == 1)
        {
            engineFailSound.play();
            engineSound.stop();
        }

        drawText("TRIEBWERKSAUSFALL!", 220, 100);

        heli.setRotation(Greenfoot.getRandomNumber(10) - 5);
        heli.setLocation(heli.getX(), heli.getY() + 2);

        if (timer > 120)
        {
            state = 2;
            timer = 0;
        }
    }

    // 💥 CRASH
    private void crash()
    {
        if (timer == 1)
        {
            crashSound.play();
            windSound.stop();
        }

        //heli.setRotation(45);
        heli.setLocation(heli.getX() + 1, heli.getY() + 5);

        getBackground().setColor(Color.BLACK);
        getBackground().fill();


        if (timer > 120)
        {
            state = 3;
            timer = 0;
        }
    }

    // ⚫ BLACKOUT
    private void blackout()
    {
        crashSound.stop();
        engineFailSound.stop();
        

        if (timer == 1)
        {
            heli.getImage().setTransparency(0);
            wakeUpSound.play();
            getBackground().setColor(Color.BLACK);
            getBackground().fill();
        }

            if (timer < 100)
        {
            
        }
        else
        {
            state = 4;
            timer = 0;
        }
    }

    // 😵 AUFWACHEN → GAME START
    private void wakeUp()
    {
        if (timer == 1)
        {
            
        }

        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        if (timer < 80)
        {
            getBackground().setColor(Color.DARK_GRAY);
            
        }
        
        
        else if (timer < 640)
        {
            
        }
        else
        {
            wakeUpSound.stop();
            Greenfoot.setWorld(new MyWorld());
        }
    }

    // 🧾 TEXT HELPER
    private void drawText(String text, int x, int y)
    {
        GreenfootImage img = new GreenfootImage(text, 40, Color.WHITE, new Color(0,0,0,0));
        getBackground().drawImage(img, x, y);
    }
}