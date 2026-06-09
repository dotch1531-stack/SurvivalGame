import greenfoot.*;

public class CaveInteriorWorld extends World
{
    public static CaveInteriorWorld instance;

    private GreenfootImage tileSet;
    private GreenfootImage tile;

    public static final int TILE_SIZE = 40;

    //Kamera
    public int cameraX = 0;
    public int cameraY = 0;

    public Player player;

public CaveInteriorWorld(Player playerCave)
{
    super(800, 800, 1);

    instance = this;
    this.player = playerCave;   
    tileSet = new GreenfootImage("tileset.png");

    tile = new GreenfootImage(TILE_SIZE, TILE_SIZE);
    tile.drawImage(tileSet, -2 * TILE_SIZE, 0);

    drawFloor();
  
    addPlayer(player);
}

private void drawFloor()
{
    GreenfootImage tileSet = new GreenfootImage("tileset.png");

    for(int x = 0; x < 20; x++)
    {
        for(int y = 0; y < 20; y++)
        {
            GreenfootImage tile;

            // 80% Stein, 20% DIRT
            int r = Greenfoot.getRandomNumber(100);

            if(r < 80)
            {
                tile = new GreenfootImage(TILE_SIZE, TILE_SIZE);
                tile.drawImage(tileSet, -2 * TILE_SIZE, 0); // ROCK
            }
            else
            {
                tile = new GreenfootImage(TILE_SIZE, TILE_SIZE);
                tile.drawImage(tileSet, -1 * TILE_SIZE, 0); // DIRT
            }

            getBackground().drawImage(tile, x * TILE_SIZE, y * TILE_SIZE);
        }
    }
}

    public void addPlayer(Player p)
    {
        addObject(p, 400, 400);
    }

    public void act(){
        if(player == null)
        {
            return;
        }
                if(Greenfoot.isKeyDown("e"))
        {
            Greenfoot.setWorld(MyWorld.instance);
            Greenfoot.delay(20);
        }
        handleMovement();
        updateCamera();
        updateObjects();
    }

    private void updateCamera()
{
    cameraX = player.getX() - 400;
    cameraY = player.getY() - 400;
}

    public void updateObjects()
    {
        if(player == null) return;

        for(Structures s : getObjects(Structures.class))
        {
            s.setLocation(
                s.worldX - cameraX,
                s.worldY - cameraY
            );
        }
    }

public void handleMovement()
{
    int speed = 4;

    int newX = cameraX;
    int newY = cameraY;

    if(Greenfoot.isKeyDown("w")) newY -= speed;
    if(Greenfoot.isKeyDown("s")) newY += speed;
    if(Greenfoot.isKeyDown("a")) newX -= speed;
    if(Greenfoot.isKeyDown("d")) newX += speed;

    cameraX = newX;
    cameraY = newY;
}

    public boolean collidesWithSolid(int newCameraX, int newCameraY)
    {
        int playerX = newCameraX + player.getX();
        int playerY = newCameraY + player.getY();

        int playerHitboxWidth = player.hitboxWidth;
        int playerHitboxHeight = player.hitboxHeight;

        for(WorldObject obj : getObjects(WorldObject.class))
        {
            if(!obj.solid)
                continue;

            int dx = Math.abs(playerX - obj.worldX);
            int dy = Math.abs(playerY - obj.worldY);

            if(dx < playerHitboxWidth + obj.hitboxWidth &&
            dy < playerHitboxHeight + obj.hitboxHeight)
            {
                return true;
            }
        }

        for(Entity entity : getObjects(Entity.class))
        {
            if(!entity.solid)
                continue;

            int dx = Math.abs(playerX - entity.worldX);
            int dy = Math.abs(playerY - entity.worldY);

            if(dx < playerHitboxWidth + entity.hitboxWidth &&
            dy < playerHitboxHeight + entity.hitboxHeight)
            {
                return true;
            }
        }
        for(Structures s : getObjects(Structures.class))
        {
            if(!s.solid)
                continue;

            int dx = Math.abs(playerX - s.worldX);
            int dy = Math.abs(playerY - s.worldY);

            if(dx < playerHitboxWidth + 60 &&
            dy < playerHitboxHeight + 20)
            {
                return true;
            }
        }

        return false;
    }
}