import greenfoot.*;

public class CaveInteriorWorld extends World
{
    public static CaveInteriorWorld instance;

    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int[][] caveMap = new int[200][200];
    private boolean generated = false;

    public static final int TILE_SIZE = 40;

    public int cameraX = 0;
    public int cameraY = 0;

    public Player player;

    public CaveInteriorWorld(Player playerCave)
    {
        super(800, 800, 1);

        instance = this;

        tileSet = new GreenfootImage("tileset.png");

        tiles = new GreenfootImage[6];

        for(int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);
            tiles[i].drawImage(tileSet, -i * TILE_SIZE, 0);
        }

        // PLAYER ÜBERNEHMEN (NICHT NEU ERZEUGEN!)
        player = playerCave;
        addObject(player, 400, 400);

    }
    // =========================
    // FLOOR
    // =========================

    private GreenfootImage getTile(int index)
    {
        GreenfootImage img = new GreenfootImage(TILE_SIZE, TILE_SIZE);
        img.drawImage(tileSet, -index * TILE_SIZE, 0);
        return img;
    }

    // =========================
    // ACT LOOP
    // =========================
    public void act()
    {
        handleMovement();
        updateObjects();
        renderWorld();

        if (Greenfoot.isKeyDown("e"))
        {
            MyWorld.instance.addObject(player, 400, 400);
            Greenfoot.setWorld(MyWorld.instance);
            Greenfoot.delay(10);
        }
    }

    // CAMERA MOVEMENT 
    public void handleMovement()
    {
        int speed = 4;

        if (Greenfoot.isKeyDown("w")) cameraY -= speed;
        if (Greenfoot.isKeyDown("s")) cameraY += speed;
        if (Greenfoot.isKeyDown("a")) cameraX -= speed;
        if (Greenfoot.isKeyDown("d")) cameraX += speed;
    }

    // WORLD UPDATE 
    public void updateObjects()
    {
        for (WorldObject obj : getObjects(WorldObject.class))
        {
            obj.setLocation(
                obj.worldX - cameraX,
                obj.worldY - cameraY
            );
        }

        for (Entity e : getObjects(Entity.class))
        {
            e.setLocation(
                e.worldX - cameraX,
                e.worldY - cameraY
            );
        }

        for (Structures s : getObjects(Structures.class))
        {
            s.setLocation(
                s.worldX - cameraX,
                s.worldY - cameraY
            );
        }

    }

    public void renderWorld()
    {
        if(!generated)
            generateCave();

        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        int tilesX = 24;
        int tilesY = 24;

        int startX = Math.floorDiv(cameraX, TILE_SIZE);
        int startY = Math.floorDiv(cameraY, TILE_SIZE);

        int offsetX = -(cameraX - startX * TILE_SIZE);
        int offsetY = -(cameraY - startY * TILE_SIZE);

        for(int y = 0; y < tilesY; y++)
        {
            for(int x = 0; x < tilesX; x++)
            {
                int worldX = startX + x;
                int worldY = startY + y;

                if(worldX < 0 || worldY < 0 || worldX >= 200 || worldY >= 200)
                    continue;

                int tileId = caveMap[worldX][worldY];

                GreenfootImage tile = tiles[tileId];

                getBackground().drawImage(
                    tile,
                    x * TILE_SIZE + offsetX,
                    y * TILE_SIZE + offsetY
                );
            }
        }
    }

    private void generateCave()
    {
        for(int y = 0; y < 200; y++)
        {
            for(int x = 0; x < 200; x++)
            {
                int r = Greenfoot.getRandomNumber(100);

                if(r < 90)
                    caveMap[x][y] = 2; // ROCK
                else
                    caveMap[x][y] = 1; // DIRT
            }
        }

        generated = true;
    }
}