import greenfoot.*;

public abstract class Passiv_Entity extends Entity
{
    // ===== MOVEMENT =====
    protected int dirX;
    protected int dirY;

    protected int walkTimer;
    protected int idleTimer;

    protected int speed;
    protected int[] allowedTiles;

    public Passiv_Entity()
    {
        dirX = 0;
        dirY = 0;

        walkTimer = 0;
        idleTimer = 0;

        speed = 1;

        // Standard: Gras
        allowedTiles = new int[]
        {
            MyWorld.GRASS
        };
    }

    @Override
    public void act()
    {
        // Entity bleibt komplett unangetastet
        super.act();

        if (!onScreen())
            return;

        naturalMovement();
    }

    // ===== NATURAL MOVEMENT =====
    protected void naturalMovement()
    {
        // ===== IDLE =====
        if (idleTimer > 0)
        {
            idleTimer--;
            return;
        }

        // ===== MOVE (WORLD SPACE) =====
        int nextX = worldX + dirX * speed;
        int nextY = worldY + dirY * speed;

        if(canMoveTo(nextX, nextY))
        {
            worldX = nextX;
            worldY = nextY;
        }
        else
        {
            dirX = Greenfoot.getRandomNumber(3) - 1;
            dirY = Greenfoot.getRandomNumber(3) - 1;
        }

        walkTimer--;

        // ===== SMALL RANDOM DIRECTION CHANGE =====
        if (Greenfoot.getRandomNumber(100) < 3)
        {
            dirX += Greenfoot.getRandomNumber(3) - 1;
            dirY += Greenfoot.getRandomNumber(3) - 1;

            dirX = clamp(dirX);
            dirY = clamp(dirY);
        }

        // ===== NEW ACTION =====
        if (walkTimer <= 0)
        {
            int action = Greenfoot.getRandomNumber(100);

            if (action < 35)
            {
                idleTimer = 40 + Greenfoot.getRandomNumber(80);
                dirX = 0;
                dirY = 0;
            }
            else
            {
                walkTimer = 60 + Greenfoot.getRandomNumber(120);

                dirX = Greenfoot.getRandomNumber(3) - 1;
                dirY = Greenfoot.getRandomNumber(3) - 1;
            }
        }
    }
    //Can Walk current tile
    protected boolean canWalkOn(int tile)
    {
        for(int allowed : allowedTiles)
        {
            if(allowed == tile)
                return true;
        }

        return false;
    }
    //Can Walk next tile
    protected boolean canMoveTo(int worldX, int worldY)
    {
        MyWorld world = (MyWorld)getWorld();

        if(world == null)
            return false;

        int tileX = Math.floorDiv(worldX, MyWorld.TILE_SIZE);
        int tileY = Math.floorDiv(worldY, MyWorld.TILE_SIZE);

        int tile = world.getTile(tileX, tileY);

        return canWalkOn(tile);
    }
    // ===== HELPER =====
    protected int clamp(int value)
    {
        if (value > 1) return 1;
        if (value < -1) return -1;
        return value;
    }
}