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
                // optischer Rand bleibt stabil (nur für Grafik)
                if (x == 0 || y == 0 || x == 199 || y == 199)
                {
                    caveMap[x][y] = 2; // ROCK
                }
                else
                {
                    int r = Greenfoot.getRandomNumber(100);

                    if (r < 90)
                        caveMap[x][y] = 2; // ROCK
                    else
                        caveMap[x][y] = 1; // DIRT
                }
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

        // =========================================================
        // 1. ALLES IST STEIN (voller Blockraum)
        // =========================================================
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                spawnWall(x, y);
            }
        }

        // =========================================================
        // 2. GROßE HÖHLEN AUSFRÄSEN
        // =========================================================
        for (int i = 0; i < 180; i++)
        {
            int x = Greenfoot.getRandomNumber(size);
            int y = Greenfoot.getRandomNumber(size);

            int radius = Greenfoot.getRandomNumber(8) + 6;

            carveCircle(x, y, radius);
        }

        // =========================================================
        // 3. BREITE TUNNEL (DER WICHTIGSTE TEIL)
        // =========================================================
        for (int i = 0; i < 220; i++)
        {
            int x = Greenfoot.getRandomNumber(size);
            int y = Greenfoot.getRandomNumber(size);

            int length = Greenfoot.getRandomNumber(12) + 8;

            int dir = Greenfoot.getRandomNumber(4);

            int width = Greenfoot.getRandomNumber(3) + 2; // >>> BREITE TUNNEL

            for (int l = 0; l < length; l++)
            {
                carveTunnel(x, y, width);

                if (dir == 0) x++;
                if (dir == 1) x--;
                if (dir == 2) y++;
                if (dir == 3) y--;
            }
        }
    }

    private void carveCircle(int cx, int cy, int radius)
    {
        for (int y = cy - radius; y <= cy + radius; y++)
        {
            for (int x = cx - radius; x <= cx + radius; x++)
            {
                if (x <= 1 || y <= 1 || x >= 198 || y >= 198)
                    continue;

                int dx = x - cx;
                int dy = y - cy;

                if (dx * dx + dy * dy <= radius * radius)
                {
                    removeWall(x, y);
                }
            }
        }
    }

    private void carveTunnel(int cx, int cy, int width)
    {
        for (int y = -width; y <= width; y++)
        {
            for (int x = -width; x <= width; x++)
            {
                int tx = cx + x;
                int ty = cy + y;

                if (tx <= 1 || ty <= 1 || tx >= 198 || ty >= 198)
                    continue;

                if (x * x + y * y <= width * width)
                {
                    removeWall(tx, ty);
                }
            }
        }
    }

    private void removeWall(int tileX, int tileY)
    {
        for (Structures s : getObjects(Structures.class))
        {
            if (s instanceof CaveWall)
            {
                if (s.worldX / TILE_SIZE == tileX &&
                s.worldY / TILE_SIZE == tileY)
                {
                    removeObject(s);
                    return;
                }
            }
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
        if (Greenfoot.isKeyDown("e"))
        {
            MyWorld.instance.addObject(player, 400, 400);
            Greenfoot.setWorld(MyWorld.instance);
            Greenfoot.delay(20);

        }
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