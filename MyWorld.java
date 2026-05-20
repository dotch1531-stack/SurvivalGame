import greenfoot.*;

public class MyWorld extends World
{
    //              BITTE KEINE MERGE-KONFLIKTE MEHR
    //              HAT NE GUTE HALBE STUNDE GEBRAUCHT DEN ZU LÖSEN

    // ===== INVENTORY =====
    public boolean inventoryOpen = false;

    private InventoryScreen inventoryScreen;

    // ===== WORLD =====
    public static final int TILE_SIZE = 40;

    private int worldSeed;

    public int cameraX = 0;
    public int cameraY = 0;

    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int waterFrame = 0;
    private int animationTimer = 1;
    
    

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        super(800, 800, 1);

        worldSeed = Greenfoot.getRandomNumber(1000000);

        tileSet = new GreenfootImage("tileset.png");

        tiles = new GreenfootImage[6];

        // Tiles aus dem Tileset schneiden
        for(int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);

            tiles[i].drawImage(
                tileSet,
                -i * TILE_SIZE,
                0
            );
        }
        Player player = new Player();
        addObject(player, 400, 400);
    }

    public void act()
    {
        handleInventory();

        if(!inventoryOpen)
        {
            handleMovement();
            updateWaterAnimation();
            renderWorld();
        }
    }

    // ===== MOVEMENT =====
    public void handleMovement()
    {
        int speed = 5;

        if(Greenfoot.isKeyDown("w")) cameraY -= speed;
        if(Greenfoot.isKeyDown("s")) cameraY += speed;
        if(Greenfoot.isKeyDown("a")) cameraX -= speed;
        if(Greenfoot.isKeyDown("d")) cameraX += speed;
    }

    // ===== WORLD RENDERING =====
    public void renderWorld()
    {
        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        int tilesX = 22;
        int tilesY = 22;

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

                int tile = getTile(worldX, worldY);

                GreenfootImage img;

                // Wasser animieren
                if(tile == 3)
                {
                    img = tiles[3 + waterFrame];
                }
                else
                {
                    img = tiles[tile];
                }

                getBackground().drawImage(
                    img,
                    x * TILE_SIZE + offsetX,
                    y * TILE_SIZE + offsetY
                );
            }
        }
    }

    // ===== FIXED TILE GENERATION =====
    public int getTile(int x, int y)
    {
        // ===== LARGE SCALE TERRAIN =====
        double largeNoise =
            Math.sin((x + worldSeed) * 0.01) +
            Math.cos((y - worldSeed) * 0.01);

        largeNoise = (largeNoise + 2) / 4.0;

        // ===== SMALL DETAIL NOISE =====
        double detailNoise =
            Math.sin(x * 0.08) *
            Math.cos(y * 0.08);

        detailNoise = (detailNoise + 1) / 2.0;

        // ===== COMBINED HEIGHT =====
        double height =
            largeNoise * 0.8 +
            detailNoise * 0.2;

        // ===== RIVER =====
        double river =
            400 +
            Math.sin((y + worldSeed) * 0.02) * 80 +
            Math.sin(y * 0.05) * 20;

        if(Math.abs(x - river) < 4)
            return 3;

        // ===== LAKES =====
        if(height < 0.28)
            return 3;

        // ===== ROCK BIOME =====
        double rockNoise =
            Math.sin(x * 0.15 + worldSeed) *
            Math.cos(y * 0.15 - worldSeed);

        if(height > 0.68 && rockNoise > -0.1)
            return 2;

        // ===== GRASS =====
        return 0;
    }

    // ===== INVENTORY SYSTEM =====
    public void handleInventory()
    {
        if(Greenfoot.isKeyDown("tab") && !inventoryOpen)
        {
            inventoryOpen = true;

            inventoryScreen = new InventoryScreen();
            addObject(inventoryScreen, 400, 400);

            inventoryScreen.getItemsInventory();

            Greenfoot.delay(10);
        }
        else if(Greenfoot.isKeyDown("tab") && inventoryOpen)
        {
            inventoryOpen = false;

            inventoryScreen.firstRead = false;
            inventoryScreen.setItemsInventory();

            removeObject(inventoryScreen);

            Greenfoot.delay(10);
        }
    }
    // Water animation
    public void updateWaterAnimation()
    {
        animationTimer++;

        if(animationTimer >= 120)
        {
            animationTimer = 0;

            waterFrame++;

            if(waterFrame > 2)
                waterFrame = 0;
        }
    }
}