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
    
    /*
    public void hitCheck()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
    
        if (mouse == null || !Greenfoot.mousePressed(null))
            return;
    
        MyWorld world = (MyWorld)getWorld();
    
        int px = 400;
        int py = 400;
    
        int mx = mouse.getX();
        int my = mouse.getY();
    
         ===== ATTACK SETTINGS =====
        double range = 120 range;
        double coneAngle = Math.toRadians(45) /*hit width;
    
        
        double dirX = mx - px;
        double dirY = my - py;
    
        // normalize direction
        double dirLength = Math.sqrt(dirX * dirX + dirY * dirY);
    
        if (dirLength == 0)
            return;
    
        dirX /= dirLength;
        dirY /= dirLength;
    
        // precompute cosine threshold
        double cosThreshold = Math.cos(coneAngle / 2);
    
        // squared range (faster than sqrt)
        double rangeSquared = range * range;
    
        double offset = 40; // distance in front of player

        double originX = px + dirX * offset;
        double originY = py + dirY * offset;
        
        // get nearby entities ONCE
        java.util.List<Entity> nearby =
            world.getNearbyEntitys((int)range);
    
        for (Entity e : nearby)
        {
            double dx = e.getX() - originX;
            double dy = e.getY() - originY;
    
            // ===== DISTANCE CHECK =====
            double distSquared = dx * dx + dy * dy;
    
            if (distSquared > rangeSquared)
                continue;
    
            // normalize entity direction
            double dist = Math.sqrt(distSquared);
    
            if (dist == 0)
                continue;
    
            dx /= dist;
            dy /= dist;
    
            // ===== DOT PRODUCT =====
            double dot = dx * dirX + dy * dirY;
    
            // inside cone
            if (dot >= cosThreshold)
            {
                
                e.damage(1 /*damage);
            }
        }

        // ===== GET WORLD OBJECTS =====
    java.util.List<WorldObject> nearbyObjects =
        world.getNearbyWorldObjects((int)range);

    for (WorldObject o : nearbyObjects)
    {
        double dx = o.worldX - originX;
        double dy = o.worldY - originY;

        double distSquared = dx * dx + dy * dy;

        if (distSquared > rangeSquared)
            continue;

        double dist = Math.sqrt(distSquared);

        if (dist == 0)
            continue;

        dx /= dist;
        dy /= dist;

        double dot = dx * dirX + dy * dirY;

        if (dot >= cosThreshold)
        {
            o.damage(1);
        }
    }
    }*/
    
        public void hitCheck()
        {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        if (!Greenfoot.mousePressed(null)) return;
    
        GreenfootImage bg = getWorld().getBackground();
    
        int px = 400;
        int py = 400;
    
        int mx = mouse.getX();
        int my = mouse.getY();
    
        // direction
        double dirX = mx - px;
        double dirY = my - py;
    
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) return;
    
        dirX /= len;
        dirY /= len;
    
        // ===== SETTINGS =====
        double range = 120;
        double coneAngle = Math.toRadians(45);
        double offset = 40;
        
        
    
        
        for (Entity e : getWorld().getObjects(Entity.class))
        {
            double ex = e.getX();
            double ey = e.getY();
            
            double originX = px + dirX;
            double originY = py + dirY;
            
            double cosThreshold = Math.cos(coneAngle / 2);
        
            double leftAngle = Math.atan2(dirY, dirX) - coneAngle / 2;
            double rightAngle = Math.atan2(dirY, dirX) + coneAngle / 2;
        
            int leftX = (int)(originX + Math.cos(leftAngle) * range);
            int leftY = (int)(originY + Math.sin(leftAngle) * range);
        
            int rightX = (int)(originX + Math.cos(rightAngle) * range);
            int rightY = (int)(originY + Math.sin(rightAngle) * range);
            
            
            int[] xHitScreen = {(int)Math.floor(originX), rightX, leftX};
            int[] yHitScreen = {(int)Math.floor(originY), rightY, leftY};
        
            if (objectsAndEbntitysInHitscreen(
            ex, ey,
            originX, originY,
            dirX, dirY,
            range,
            cosThreshold))
            {
                e.damage(1);
            }
        }
        
        for (WorldObject o : getWorld().getObjects(WorldObject.class))
        {
            double ox = o.getX();
            double oy = o.getY();
            
            double originX = px + dirX * offset;
            double originY = py + dirY * offset;
            
            double cosThreshold = Math.cos(coneAngle / 2);
        
            double leftAngle = Math.atan2(dirY, dirX) - coneAngle / 2;
            double rightAngle = Math.atan2(dirY, dirX) + coneAngle / 2;
        
            int leftX = (int)(originX + Math.cos(leftAngle) * range);
            int leftY = (int)(originY + Math.sin(leftAngle) * range);
        
            int rightX = (int)(originX + Math.cos(rightAngle) * range);
            int rightY = (int)(originY + Math.sin(rightAngle) * range);
            
            
            int[] xHitScreen = {(int)Math.floor(originX), rightX, leftX};
            int[] yHitScreen = {(int)Math.floor(originY), rightY, leftY};
        
            if (objectsAndEbntitysInHitscreen(
                ox, oy,
                originX, originY,
                dirX, dirY,
                range,
                cosThreshold))
        {
            o.damage(1);
            }
        }
    }

    public boolean objectsAndEbntitysInHitscreen(
    double ox, double oy,
    double originX, double originY,
    double dirX, double dirY,
    double range,
    double cosThreshold)
{
    double dx = ox - originX;
    double dy = oy - originY;

    double distSquared = dx * dx + dy * dy;

    if (distSquared > range * range)
        return false;

    double dist = Math.sqrt(distSquared);
    if (dist == 0)
        return true;

    dx /= dist;
    dy /= dist;

    double dot = dx * dirX + dy * dirY;

    return dot >= cosThreshold;
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
