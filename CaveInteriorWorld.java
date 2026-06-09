import greenfoot.*;

public class CaveInteriorWorld extends World
{
    public static CaveInteriorWorld instance;

    // ===== TILE SYSTEM =====
    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int[][] caveMap = new int[200][200];
    private boolean generated = false;

    public static final int TILE_SIZE = 40;

    // ===== CAMERA =====
    public int cameraX = 0;
    public int cameraY = 0;

    // ===== PLAYER =====
    public Player player;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public CaveInteriorWorld(Player playerCave)
    {
        super(800, 800, 1);

        instance = this;

        tileSet = new GreenfootImage("tileset.png");
        tiles = new GreenfootImage[6];

        // TILE LOADING
        for (int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);
            tiles[i].drawImage(tileSet, -i * TILE_SIZE, 0);
        }

        // PLAYER übernehmen
        player = playerCave;
        addObject(player, 400, 400);

        // CAVE GENERATION
        generateCave();
        generateCaveWalls();
    }

    // =========================================================
    // CAVE GENERATION
    // =========================================================
    private void generateCave()
    {
        for (int y = 0; y < 200; y++)
        {
            for (int x = 0; x < 200; x++)
            {
                int r = Greenfoot.getRandomNumber(100);

                if (r < 90)
                    caveMap[x][y] = 2; // ROCK
                else
                    caveMap[x][y] = 1; // DIRT
            }
        }

        generated = true;
    }

    // =========================================================
    // SOLID BORDER (OBJECTS)
    // =========================================================
    private void generateCaveWalls()
    {
        int size = 200;

        // TOP + BOTTOM
        for (int x = 0; x < size; x++)
        {
            spawnWall(x, 0);
            spawnWall(x, size - 1);
        }

        // LEFT + RIGHT
        for (int y = 0; y < size; y++)
        {
            spawnWall(0, y);
            spawnWall(size - 1, y);
        }
    }

    private void spawnWall(int tileX, int tileY)
    {
        CaveWall wall = new CaveWall();

        wall.worldX = tileX * TILE_SIZE + TILE_SIZE / 2;
        wall.worldY = tileY * TILE_SIZE + TILE_SIZE / 2;

        addObject(
            wall,
            wall.worldX - cameraX,
            wall.worldY - cameraY
        );
    }

    // =========================================================
    // ACT LOOP
    // =========================================================
    public void act()
    {
        handleMovement();
        updateObjects();
        renderWorld();
    }

    // =========================================================
    // CAMERA MOVEMENT
    // =========================================================
    public void handleMovement()
    {
        int speed = 4;

        int newCameraX = cameraX;
        int newCameraY = cameraY;

        if (Greenfoot.isKeyDown("w")) newCameraY -= speed;
        if (Greenfoot.isKeyDown("s")) newCameraY += speed;
        if (Greenfoot.isKeyDown("a")) newCameraX -= speed;
        if (Greenfoot.isKeyDown("d")) newCameraX += speed;

        // X check
        if (!collidesWithWall(newCameraX, cameraY))
        {
            cameraX = newCameraX;
        }

        // Y check
        if (!collidesWithWall(cameraX, newCameraY))
        {
            cameraY = newCameraY;
        }
    }

    // =========================================================
    // UPDATE OBJECTS
    // =========================================================
    public void updateObjects()
    {
        for (Structures s : getObjects(Structures.class))
        {
            s.setLocation(
                s.worldX - cameraX,
                s.worldY - cameraY
            );
        }

        for (Entity e : getObjects(Entity.class))
        {
            e.setLocation(
                e.worldX - cameraX,
                e.worldY - cameraY
            );
        }

        for (WorldObject o : getObjects(WorldObject.class))
        {
            o.setLocation(
                o.worldX - cameraX,
                o.worldY - cameraY
            );
        }
    }

    // =========================================================
    // RENDER
    // =========================================================
    public void renderWorld()
    {
        if (!generated) return;

        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        int tilesX = 24;
        int tilesY = 24;

        int startX = Math.floorDiv(cameraX, TILE_SIZE);
        int startY = Math.floorDiv(cameraY, TILE_SIZE);

        int offsetX = -(cameraX - startX * TILE_SIZE);
        int offsetY = -(cameraY - startY * TILE_SIZE);

        for (int y = 0; y < tilesY; y++)
        {
            for (int x = 0; x < tilesX; x++)
            {
                int worldX = startX + x;
                int worldY = startY + y;

                if (worldX < 0 || worldY < 0 || worldX >= 200 || worldY >= 200)
                    continue;

                int tile = caveMap[worldX][worldY];

                GreenfootImage img = tiles[tile];

                getBackground().drawImage(
                    img,
                    x * TILE_SIZE + offsetX,
                    y * TILE_SIZE + offsetY
                );
            }
        }
    }

    public boolean collidesWithWall(int newCameraX, int newCameraY)
    {
        int playerWorldX = newCameraX + 400;
        int playerWorldY = newCameraY + 400;

        for (Structures s : getObjects(Structures.class))
        {
            if (s instanceof CaveWall)
            {
                int dx = Math.abs(playerWorldX - s.worldX);
                int dy = Math.abs(playerWorldY - s.worldY);

                if (dx < 40 && dy < 40)
                    return true;
            }
        }

        return false;
    }
}