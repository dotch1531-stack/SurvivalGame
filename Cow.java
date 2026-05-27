import greenfoot.*;

public class Cow extends WorldObject
{
    // ===== MOVEMENT =====
    private int dirX;
    private int dirY;

    private int walkTimer;
    private int idleTimer;

    private int speed;

    public Cow()
    {
        // ===== IMAGE =====
        setImage("cow.png");

        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== COLLISION =====

        // ===== BREAKABLE =====
        breakable = false;

        // ===== HITBOX =====
        hitboxWidth = 120;
        hitboxHeight = 60;

        // ===== MOVEMENT =====
        dirX = 0;
        dirY = 0;

        walkTimer = 0;
        idleTimer = 0;

        speed = 1;
    }

    public void act()
    {
        naturalMovement();

        animate();
    }

    // ===== NATURAL MOVEMENT =====
    private void naturalMovement()
    {
        // ===== IDLE =====
        if(idleTimer > 0)
        {
            idleTimer--;
            return;
        }

        // ===== MOVE =====
        int nextX = worldX + dirX * speed;
        int nextY = worldY + dirY * speed;

        MyWorld world = (MyWorld)getWorld();

        int tileX = nextX / MyWorld.TILE_SIZE;
        int tileY = nextY / MyWorld.TILE_SIZE;

        if(world.getTile(tileX, tileY) != MyWorld.WATER)
        {
            worldX = nextX;
            worldY = nextY;
        }
        else
        {
            // Wasser vermeiden → Richtung ändern
            dirX = Greenfoot.getRandomNumber(3) - 1;
            dirY = Greenfoot.getRandomNumber(3) - 1;
        }

        walkTimer--;

        // ===== SMALL DIRECTION CHANGES =====
        if(Greenfoot.getRandomNumber(100) < 3)
        {
            dirX += Greenfoot.getRandomNumber(3) - 1;
            dirY += Greenfoot.getRandomNumber(3) - 1;

            // LIMIT VALUES
            if(dirX > 1) dirX = 1;
            if(dirX < -1) dirX = -1;

            if(dirY > 1) dirY = 1;
            if(dirY < -1) dirY = -1;
        }

        // ===== CHOOSE NEW ACTION =====
        if(walkTimer <= 0)
        {
            int action = Greenfoot.getRandomNumber(100);

            // ===== STAND STILL =====
            if(action < 35)
            {
                idleTimer = 40 + Greenfoot.getRandomNumber(80);

                dirX = 0;
                dirY = 0;
            }
            // ===== WALK =====
            else
            {
                walkTimer = 60 + Greenfoot.getRandomNumber(120);

                dirX = Greenfoot.getRandomNumber(3) - 1;
                dirY = Greenfoot.getRandomNumber(3) - 1;

            }
        }
    }

    // ===== OPTIONAL ANIMATION =====
    private void animate()
    {
        // später animationen hier
    }

    // ===== DAMAGE =====
    public void damage(int amount)
    {
        // später hp system
    }
}