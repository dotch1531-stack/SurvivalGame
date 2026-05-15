import greenfoot.*;

public class MyWorld extends World
{
    // ===== INVENTORY =====
    public boolean inventoryOpen = false;

    private InventorySlots inventorySlot1;
    private InventoryScreen inventoryScreen;

    // ===== WORLD =====
    public static final int TILE_SIZE = 40;

    private int worldSeed;

    public int cameraX = 0;
    public int cameraY = 0;

    private GreenfootImage grass;
    private GreenfootImage rock;
    private GreenfootImage water;

    private Wood wood;

    public MyWorld()
    {
        super(800, 800, 1);

        worldSeed = Greenfoot.getRandomNumber(1000000);

        grass = new GreenfootImage("grass.png");
        rock  = new GreenfootImage("rock.png");
        water = new GreenfootImage("water.png");
    }

    // ===== MAIN LOOP =====
    public void act()
    {
        handleInventory();

        if(!inventoryOpen)
        {
            handleMovement();
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

                if(tile == 0) img = grass;
                else if(tile == 1) img = rock;
                else img = water;

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
        int n = x * 73856093
              ^ y * 19349663
              ^ worldSeed;

        n = Math.abs(n);

        // ===== LAKES =====
        double lake = Math.sin(n * 0.00001) + Math.cos(n * 0.00002);
        lake = (lake + 2) / 4.0;

        if(lake > 0.80)
            return 2;

        // ===== RIVER (FIXED: NO SCREEN DEPENDENCY) =====
        double riverWave =
            Math.sin((y + worldSeed) * 0.02) * 60;

        double riverX = 400 + riverWave;

        if(Math.abs(x - riverX) < 3)
            return 2;

        // ===== ROCK BIOME =====
        double terrain =
            Math.sin(x * 0.06 + worldSeed * 0.001) +
            Math.cos(y * 0.06 - worldSeed * 0.001);

        terrain = (terrain + 2) / 4.0;

        double rockNoise =
            Math.sin(x * 0.08 + worldSeed) *
            Math.cos(y * 0.08 - worldSeed);

        if(terrain > 0.72 && rockNoise > 0.2)
            return 1;

        return 0;
    }

    // ===== INVENTORY SYSTEM (UNCHANGED) =====
    public void handleInventory()
    {
        if(Greenfoot.isKeyDown("tab") && !inventoryOpen)
        {
            inventoryOpen = true;

            inventoryScreen = new InventoryScreen();
            addObject(inventoryScreen, 400, 400);

            inventorySlot1 = new InventorySlots();
            addObject(inventorySlot1, 400, 400);

            inventoryScreen.getItemsInventory();

            Greenfoot.delay(1000);
        }
        else if(Greenfoot.isKeyDown("tab") && inventoryOpen)
        {
            inventoryOpen = false;

            inventoryScreen.firstRead = false;
            inventoryScreen.setItemsInventory();

            removeObject(inventoryScreen);
            //removeObject(inventorySlot1);   

            Greenfoot.delay(1000);
        }
    }

    public void drawWood()
    {
        wood = new Wood();
        addObject(wood, 100, 90);
    }
}