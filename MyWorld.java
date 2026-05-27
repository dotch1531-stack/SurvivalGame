import greenfoot.*;

public class MyWorld extends World
{
    // ===== INVENTORY =====
    public boolean inventoryOpen = false;

    private InventoryScreen inventoryScreen;
    
    // ===== CRAFTING =====
    public boolean craftingMenuOpen = false;
    
    private CraftingScreen craftingScreen;
    
    public SwordButton swordButton;
    public AxeButton axeButton;
    public PicaxeButton picaxeButton;
    
    public CommitButton commitButton;

    // ===== WORLD =====
    public static final int TILE_SIZE = 40;

    private int worldSeed;

    public int cameraX = 0;
    public int cameraY = 0;

    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int waterFrame = 0;
    private int animationTimer = 10;

    Player player = new Player();

    // ===== TILE TYPES =====
    public static final int GRASS = 0;
    public static final int ROCK = 2;
    public static final int WATER = 3;

    // ===== BIOMES =====
    public static final int BIOME_GRASS = 0;
    public static final int BIOME_STONE = 1;
    public static final int BIOME_WATER = 2;

    // ===== CACHE =====
    private java.util.HashMap<String, Integer> tileCache =
        new java.util.HashMap<>();

    private java.util.HashMap<String, Integer> biomeCache =
        new java.util.HashMap<>();

    // ===== TREE CACHE =====
    private java.util.HashSet<String> generatedTreeTiles =
    new java.util.HashSet<>();
        
    private java.util.HashSet<String> spawnedObjects =
    new java.util.HashSet<>();
    
    
    

    /**
     * Constructor for objects of class MyWorld.
     */
    public MyWorld()
    {
        super(800, 800, 1);

        worldSeed = Greenfoot.getRandomNumber(1000000);

        tileSet = new GreenfootImage("tileset.png");

        tiles = new GreenfootImage[6];

        // ===== LOAD TILES =====
        for(int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);

            tiles[i].drawImage(
                tileSet,
                -i * TILE_SIZE,
                0
            );
        }

        addObject(player, 400, 400);

    }
    
    public void generateVisibleObjects()
    {
        int startX = Math.floorDiv(cameraX, TILE_SIZE) - 10;
        int startY = Math.floorDiv(cameraY, TILE_SIZE) - 10;
    
        int endX = startX + 40;
        int endY = startY + 40;
    
        for(int x = startX; x < endX; x++)
        {
            for(int y = startY; y < endY; y++)
            {
                String key = x + "," + y;
    
                if(spawnedObjects.contains(key))
                    continue;
    
                spawnedObjects.add(key);
    
                int biome = getBiome(x, y);
    
                // 🌳 TREES
                if(biome == BIOME_GRASS && Greenfoot.getRandomNumber(1000) < 8)
                {
                    spawnObject(new Tree(), x, y);
                }
    
                /* 🪨 ROCKS (example)
                if(biome == BIOME_STONE && Greenfoot.getRandomNumber(1000) < 5)
                {
                    spawnObject(new Rock(), x, y);
                }
                */
            }
        }
    }
    
    public void spawnObject(WorldObject obj, int tileX, int tileY)
    {
        obj.worldX = tileX * TILE_SIZE + TILE_SIZE / 2;
        obj.worldY = tileY * TILE_SIZE + TILE_SIZE / 2;
    
        addObject(obj,
            obj.worldX - cameraX,
            obj.worldY - cameraY
        );
    }
    
    public void updateObjects()
    {
        java.util.List<WorldObject> objects = getObjects(WorldObject.class);
        for(WorldObject obj : objects)
        {
            obj.updateScreenPosition(cameraX, cameraY);
            
            
            if(obj.getX() >= 799 || obj.getX()<=0 || obj.getY() >= 799 || obj.getY()<=0){obj.getImage().setTransparency(0);}else{obj.getImage().setTransparency(255);}

        }
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
    
        return false;
    }

    public void act()
    {
        handleInventory();
        handleCraftingMenu();
        
        updateObjects();
        
        if(!inventoryOpen && !craftingMenuOpen)
        {
            handleMovement();
            updateWaterAnimation();
            renderWorld();
        }
        
        generateVisibleObjects();
        updateObjects();
        
        for(WorldObject obj : getNearbyObjects(80))
        {
            if(obj != null && obj.breakable && Greenfoot.isKeyDown("e"))
            {
                obj.damage(1);
                Greenfoot.delay(10);
            }
        }

        // ===== WATER DETECTION =====
        if(getCurrentTile() == WATER)
            player.inWater();
        else
            player.notInWater();
    }

    // ===== MOVEMENT =====
    public void handleMovement()
    {
        int speed = 4;
    
        int newX = cameraX;
        int newY = cameraY;
    
        if(Greenfoot.isKeyDown("w")) newY -= speed;
        if(Greenfoot.isKeyDown("s")) newY += speed;
        if(Greenfoot.isKeyDown("a")) newX -= speed;
        if(Greenfoot.isKeyDown("d")) newX += speed;
    
        if(!collidesWithSolid(newX, cameraY))
            cameraX = newX;
    
        if(!collidesWithSolid(cameraX, newY))
            cameraY = newY;
    }

    

    // ===== PLAYER TILE =====
    public int getPlayerTileX()
    {
        return Math.floorDiv(cameraX + player.getX(), TILE_SIZE);
    }

    public int getPlayerTileY()
    {
        return Math.floorDiv(cameraY + player.getY(), TILE_SIZE);
    }

    public int getCurrentTile()
    {
        return getTile(getPlayerTileX(), getPlayerTileY());
    }

    // ===== CURRENT BIOME =====
    public int getCurrentBiome()
    {
        return getBiome(getPlayerTileX(), getPlayerTileY());
    }

    // ===== WORLD RENDERING =====
    public void renderWorld()
    {
        getBackground().setColor(Color.BLACK);
        getBackground().fill();

        // EXTRA TILES FIXES EDGE OUTLINE
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

                int tile = getTile(worldX, worldY);

                GreenfootImage img;

                // ===== WATER ANIMATION =====
                if(tile == WATER)
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

    // ===== BIOME GENERATION =====
    public int getBiome(int x, int y)
    {
        String key = x + "," + y;

        if(biomeCache.containsKey(key))
            return biomeCache.get(key);

        double continent =
            Math.sin((x + worldSeed) * 0.003) +
            Math.cos((y - worldSeed) * 0.003);

        continent = (continent + 2) * 0.25;

        double detail =
            Math.sin(x * 0.02) *
            Math.cos(y * 0.02);

        detail = (detail + 1) * 0.5;

        double value =
            continent * 0.75 +
            detail * 0.25;

        double river =
            400 +
            Math.sin((y + worldSeed) * 0.02) * 80 +
            Math.sin(y * 0.05) * 20;

        int biome;

        if(Math.abs(x - river) < 4 || value < 0.30)
        {
            biome = BIOME_WATER;
        }
        else if(value > 0.62)
        {
            biome = BIOME_STONE;
        }
        else
        {
            biome = BIOME_GRASS;
        }

        biomeCache.put(key, biome);

        return biome;
    }
    
    public java.util.List<WorldObject> getNearbyObjects(int radius)
    {
        java.util.List<WorldObject> nearby = new java.util.ArrayList<>();
    
        int playerWorldX = cameraX + player.getX();
        int playerWorldY = cameraY + player.getY();
    
        int r2 = radius * radius;
    
        for(WorldObject obj : getObjects(WorldObject.class))
        {
            int dx = obj.worldX - playerWorldX;
            int dy = obj.worldY - playerWorldY;
    
            if(dx * dx + dy * dy <= r2)
            {
                nearby.add(obj);
            }
        }
    
        return nearby;
    }

    // ===== TILE GENERATION =====
    public int getTile(int x, int y)
    {
        String key = x + "," + y;

        if(tileCache.containsKey(key))
            return tileCache.get(key);

        int biome = getBiome(x, y);

        int tile;

        switch(biome)
        {
            case BIOME_WATER:
                tile = WATER;
                break;

            case BIOME_STONE:
                tile = ROCK;
                break;

            default:
                tile = GRASS;
                break;
        }

        tileCache.put(key, tile);

        return tile;
    }

    // ===== INVENTORY & CRAFTING SYSTEM =====
    public void handleInventory()
    {
        if(Greenfoot.isKeyDown("tab") && !inventoryOpen)
        {
            inventoryOpen = true;

            inventoryScreen = new InventoryScreen();
            addObject(inventoryScreen, 400, 400);

            inventoryScreen.getItemsInventory(true);

            Greenfoot.delay(20);
        }
        else if(Greenfoot.isKeyDown("tab") && inventoryOpen)
        {
            inventoryOpen = false;

            inventoryScreen.firstRead = false;
            inventoryScreen.setItemsInventory();

            removeObject(inventoryScreen);

            Greenfoot.delay(20);
        }
    }
    
    public void handleCraftingMenu()
    {
        if(Greenfoot.isKeyDown("c") && !craftingMenuOpen)
        {
            craftingMenuOpen = true;

            craftingScreen = new CraftingScreen();
            addObject(craftingScreen, 400, 400);
            
            swordButton = new SwordButton();
            addObject(swordButton, 145, 70);
            
            axeButton = new AxeButton();
            addObject(axeButton, 145, 215);
            
            picaxeButton = new PicaxeButton();
            addObject(picaxeButton, 145, 365);
            
            drawCommitCraft();

            Greenfoot.delay(20);
        }
        else if(Greenfoot.isKeyDown("c") && craftingMenuOpen)
        {
            craftingMenuOpen = false;

            removeObject(craftingScreen);
            removeObject(swordButton);
            removeObject(axeButton);
            removeObject(picaxeButton);
            deleteCommitCraft();

            Greenfoot.delay(20);
        }
    }
    public void drawCommitCraft(){
        commitButton = new CommitButton();
        addObject(commitButton, 550, 700);
    }
    public void deleteCommitCraft(){
        removeObject(commitButton);
    }

    // ===== WATER ANIMATION =====
    public void updateWaterAnimation()
    {
        animationTimer++;

        if(animationTimer >= 15)
        {
            animationTimer = 0;

            waterFrame++;

            if(waterFrame > 2)
                waterFrame = 0;
        }
    }
}