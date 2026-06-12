import greenfoot.*;

public class CaveInteriorWorld extends MyWorld
{
    public static CaveInteriorWorld instance;
    private MyWorld returnWorld;

    // ===== TILE SYSTEM =====
    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int[][] caveMap = new int[100][100];
    private boolean generated = false;

    public static final int TILE_SIZE = 40;

    // ===== CAMERA =====
    public int cameraX = 0;
    public int cameraY = 0;
    private int spawnWorldX;
    private int spawnWorldY;

    // ===== PLAYER =====
    public Player player;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public CaveInteriorWorld(Player playerCave, MyWorld worldToReturnTo, int spawnX, int spawnY)
    {
        returnWorld = worldToReturnTo;
        player = playerCave;

        spawnWorldX = spawnX;
        spawnWorldY = spawnY;

        tileSet = new GreenfootImage("tileset.png");
        tiles = new GreenfootImage[6];

        for(int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);
            tiles[i].drawImage(tileSet, -i * TILE_SIZE, 0);
        }

        addObject(player, 400, 400);

        generateCave();
        generateCaveWalls();
        spawnStoneNodes();
        spawnAtSafePosition();
    }

    // =========================================================
    // CAVE GENERATION
    // =========================================================
    private void generateCave()
    {
        for (int y = 0; y < 100; y++)
        {
            for (int x = 0; x < 100; x++)
            {
                // optischer Rand bleibt stabil (nur für Grafik)
                if (x == 0 || y == 0 || x == 99 || y == 99)
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

    private void spawnAtSafePosition()
    {
        int[] safe = findSafeSpawn(
                spawnWorldX / TILE_SIZE,
                spawnWorldY / TILE_SIZE
            );

        cameraX = safe[0] * TILE_SIZE - 400;
        cameraY = safe[1] * TILE_SIZE - 400;
    }

    public int[] findSafeSpawn(int tileX, int tileY)
    {
        int radius = 10;

        for(int r = 0; r <= radius; r++)
        {
            for(int dx = -r; dx <= r; dx++)
            {
                for(int dy = -r; dy <= r; dy++)
                {
                    int x = tileX + dx;
                    int y = tileY + dy;

                    if(x < 0 || y < 0 || x >= 100 || y >= 100)
                        continue;

                    if(caveMap[x][y] == 1) // 1 = DIRT / frei
                    {
                        return new int[]{x, y};
                    }
                }
            }
        }

        return new int[]{tileX, tileY};
    }
    // =========================================================
    // SOLID BORDER (OBJECTS)
    // =========================================================
    private void generateCaveWalls()
    {
        int size = 100;

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
        for  (int i = 0; i < 60; i++)
        {
            int x = Greenfoot.getRandomNumber(size);
            int y = Greenfoot.getRandomNumber(size);

            int radius = Greenfoot.getRandomNumber(8) + 6;

            carveCircle(x, y, radius);
        }

        // =========================================================
        // 3. BREITE TUNNEL (DER WICHTIGSTE TEIL)
        // =========================================================

        for (int i = 0; i < 90; i++)
        {
            int x = Greenfoot.getRandomNumber(size);
            int y = Greenfoot.getRandomNumber(size);

            int length = Greenfoot.getRandomNumber(12) + 8;

            int dir = Greenfoot.getRandomNumber(4);

            int width = Greenfoot.getRandomNumber(2) + 1;

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
                if (x <= 1 || y <= 1 || x >= 98 || y >= 98)
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

                if (tx <= 1 || ty <= 1 || tx >= 98 || ty >= 98)
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

    public void act()
    {
        handleInventory();
        handleCraftingMenu();
        handleMovement();
        updateObjects();
        renderWorld();
        if(Greenfoot.isKeyDown("e"))
        {

            returnWorld.cameraX = returnWorld.lastCameraX;
            returnWorld.cameraY = returnWorld.lastCameraY;

            Greenfoot.setWorld(returnWorld);

            returnWorld.addObject(player, 400, 400);

            Greenfoot.delay(30);
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
        // ===== WORLD OBJECTS =====
        java.util.List<WorldObject> objects = getObjects(WorldObject.class);

        for(WorldObject obj : objects)
        {
            obj.updateScreenPosition(cameraX, cameraY);

            if(obj.getX() >= 799 || obj.getX() <= 0 || obj.getY() >= 799 || obj.getY() <= 0)
            {
                obj.getImage().setTransparency(0);
            }
            else
            {
                obj.getImage().setTransparency(255);
            }
        }

        // ===== ENTITIES =====
        java.util.List<Entity> entities = getObjects(Entity.class);

        for(Entity ent : entities)
        {
            ent.updateScreenPosition(cameraX, cameraY);

            if(ent.getX() >= 799 || ent.getX() <= 0 || ent.getY() >= 799 || ent.getY() <= 0)
            {
                ent.getImage().setTransparency(0);
            }
            else
            {
                ent.getImage().setTransparency(255);
            }
        }

        // ===== STRUCTURES =====
        java.util.List<Structures> structures = getObjects(Structures.class);

        for(Structures s : structures)
        {
            if(s != null)
            {
                s.setLocation(
                    s.worldX - cameraX,
                    s.worldY - cameraY
                );

                if(s.getX() >= 799 || s.getX() <= 0 || s.getY() >= 799 || s.getY() <= 0)
                {
                    s.getImage().setTransparency(0);
                }
                else
                {
                    s.getImage().setTransparency(255);
                }
            }
        }

        // ===== BREAK PROGRESS =====
        java.util.List<BreakProgress> bars = getObjects(BreakProgress.class);

        for(BreakProgress bar : bars)
        {
            if(bar.getX() >= 799 || bar.getX() <= 0 || bar.getY() >= 779 || bar.getY() <= 40)
            {
                bar.getImage().setTransparency(0);
            }
            else
            {
                bar.getImage().setTransparency(255);
            }
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

                if (worldX < 0 || worldY < 0 || worldX >= 100 || worldY >= 100)
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

    private void spawnStoneNodes()
    {
        for (int y = 0; y < 100; y++)
        {
            for (int x = 0; x < 100; x++)
            {
                // nur in Steinbereichen
                if (caveMap[x][y] == 2)
                {
                    // kleine Chance damit es nicht komplett voll ist
                    if (Greenfoot.getRandomNumber(100) < 8)
                    {
                        StoneNode stone = new StoneNode();

                        stone.worldX = x * TILE_SIZE + TILE_SIZE / 2;
                        stone.worldY = y * TILE_SIZE + TILE_SIZE / 2;

                        addObject(stone,
                            stone.worldX - cameraX,
                            stone.worldY - cameraY);
                    }
                }
            }
        }
    }
}

