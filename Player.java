import greenfoot.*;
public class Player extends Actor
{
    GreenfootImage spriteSheet;
    
    MyWorld world = (MyWorld)getWorld();

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
    int hitboxHeight = 90;

    public Player()
    {
        spriteSheet = new GreenfootImage("Player/JoeIdle.png");
    }
    
    public void hitCheck() {

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
    
        if(!Greenfoot.mousePressed(null)) return;
    
        int mx = mouse.getX();
        int my = mouse.getY();
    
        
        int cameraX = ((MyWorld)getWorld()).cameraX;
        int cameraY = ((MyWorld)getWorld()).cameraY;
        
        
        int worldMouseX = mouse.getX() + cameraX;
        int worldMouseY = mouse.getY() + cameraY;
        
        
        
        int px = 400 + cameraX;
        int py = 400 + cameraY;
        
        int left = Math.min(px, mx);
        int top = Math.min(py, my);
    
        int width = Math.abs(px - mx);
        int height = Math.abs(py - my);
        
        int worldLeft = left + cameraX;
        int worldTop = top + cameraY;
        int worldRight = worldLeft + width;
        int worldBottom = worldTop + height;
    
        for (Entity e : getWorld().getObjects(Entity.class)) {

            int ex = e.worldX;
            int ey = e.worldY;
        
            if (ex >= Math.min(px, worldMouseX) &&
                ex <= Math.max(px, worldMouseX) &&
                ey >= Math.min(py, worldMouseY) &&
                ey <= Math.max(py, worldMouseY) && ((MyWorld)getWorld()).getNearbyEntitys(120).contains(e))
            {
                e.damage(1);
            }
        }
}
    
    
    public void act()
    {
        movementAnimation();
        hitCheck();
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
