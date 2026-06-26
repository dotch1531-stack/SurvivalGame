import greenfoot.*;
import java.util.function.Supplier;
import java.util.*;

// crashout counter: 8
// chatgpt beleidigt: 22
// warum auf Wasser: 6

public class MyWorld extends World
{

    // ===== INVENTORY =====
    public boolean inventoryOpen = false;

    public InventoryScreen inventoryScreen;

    public Axe axe;
    public Iron iron;
    public Pickaxe pickaxe;
    public Stone stone;
    public Sword sword;
    public Wood wood;
    public Leaf leaf;
    public Rope rope;
    public SteakCooked steakCooked;
    public SteakRaw steakRaw;

    public ArrayList<Item> itemsArray = new ArrayList<>();

    // ===== CRAFTING =====
    public boolean craftingMenuOpen = false;
    public int currentButton;
    public int currentCraftingPage;

    private boolean cWasDown = false;

    public CraftingScreen craftingScreen;

    public SwordButton swordButton;
    public AxeButton axeButton;
    public PicaxeButton picaxeButton;
    public RopeButton ropeButton;

    public TreeMap<String, CraftButtons> craftButtons;
    public ArrayList<CraftButtons> alreadyDrawnCraftButtons = new ArrayList<>();

    public CommitButton commitButton;
    public UpButton upButton;
    public DownButton downButton;

    // ===== WORLD =====
    public static final int TILE_SIZE = 40;
    public static MyWorld instance;

    private boolean tentSpawned = false;
    private int tentTileX;
    private int tentTileY;

    private int worldSeed;
    public int cameraX = 0;
    public int cameraY = 0;

    public int lastCameraX;
    public int lastCameraY;

    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;

    private int waterFrame = 0;
    private int animationTimer = 10;

    Player player = new Player();

    Entity ent;

    // ===== TILE TYPES =====
    public static final int GRASS = 0;
    public static final int DIRT = 1;
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

    public java.util.List<WorldObject> getNearbyWorldObjects(int range)
    {
        return getObjects(WorldObject.class);
    }

    /**
     * Constructor for objects of class MyWorld.
     */

    //FROM HERE...

    public MyWorld()
    {
        super(800, 800, 1);
        
        instance = this;


        worldSeed = Greenfoot.getRandomNumber(1000000);

        tileSet = new GreenfootImage("tileset.png");

        tiles = new GreenfootImage[6];
        
        setPaintOrder(Hotbar.class);

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
        new TentInteriorWorld();

        addObject(player, 400, 400);
        tentSpawned = false;
        
        //hotbar
        Hotbar hotbar = new Hotbar();
        addObject(hotbar, 400, 750);
    }
    
    public void gonnaReturn(){instance = this;}

    public boolean playerNearTent()
    {
        int px = cameraX + player.getX();
        int py = cameraY + player.getY();

        for(Structures s : getObjects(Structures.class))
        {
            if(s instanceof Tent)
            {
                int dx = px - s.worldX;
                int dy = py - s.worldY;

                return (dx*dx + dy*dy) < (120 * 120);
            }
        }

        return false;
    }

    public boolean playerNearCave()
    {
        int playerWorldX = cameraX + player.getX();
        int playerWorldY = cameraY + player.getY();

        for(Structures s : getObjects(Structures.class))
        {
            if(s instanceof Cave)
            {
                int dx = playerWorldX - s.worldX;
                int dy = playerWorldY - s.worldY;

                if(dx*dx + dy*dy < 120 * 120)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void findTentSpawnLocation()
    {
        int px = getPlayerTileX();
        int py = getPlayerTileY();

        // Bevorzugte Position: 4 Tiles über dem Spieler
        int preferredX = px;
        int preferredY = py - 4;

        if(isValidTentSpot(preferredX, preferredY))
        {
            tentTileX = preferredX;
            tentTileY = preferredY;

            spawnTent(tentTileX, tentTileY);
            tentSpawned = true;

            return;
        }

        // Falls dort Wasser
        // Suche nächste gültige Tile 

        int maxRadius = 100;

        for(int r = 1; r <= maxRadius; r++)
        {
            for(int dx = -r; dx <= r; dx++)
            {
                for(int dy = -r; dy <= r; dy++)
                {
                    int tx = preferredX + dx;
                    int ty = preferredY + dy;

                    if(isValidTentSpot(tx, ty))
                    {
                        tentTileX = tx;
                        tentTileY = ty;

                        spawnTent(tx, ty);
                        tentSpawned = true;

                        return;
                    }
                }
            }
        }

        // Notfall-Fallback
        spawnTent(preferredX, preferredY);
        tentSpawned = true;
    }

    private boolean isValidTentSpot(int tx, int ty)
    {
        int tile = getTile(tx, ty);
        int biome = getBiome(tx, ty);

        boolean allowedTile =
            tile == GRASS ||
            tile == ROCK;

        boolean allowedBiome =
            biome == BIOME_GRASS ||
            biome == BIOME_STONE;

        return allowedTile && allowedBiome;
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

                //TREES
                spawnObjects(biome,x,y,BIOME_GRASS,8000, Tree::new);

                //COW HERDS
                spawnFrendlyHerds(biome, x, y, BIOME_GRASS,3,1000, Cow::new);

                //PIG HERDS
                //spawnFrendlyHerds(biome, x, y, BIOME_GRASS,2,1000, Pig::new);

                //Duck
                spawnFrendlyHerds(biome, x, y, BIOME_WATER,2,1000, Duck::new);

                //Fellow Survivor 
                spawnFrendlyHerds(biome,x,y,BIOME_STONE,1,1000, Fellow_Survivor::new);

                //Guard
                spawnFrendlyHerds(biome,x,y,BIOME_STONE,1,500, Guard::new);

                //Cave
                spawnStructures(biome,x,y,BIOME_STONE, 1000, Cave::new);
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

    public void spawnStructure(Structures str, int tileX, int tileY)
    {
        str.worldX = tileX * TILE_SIZE + TILE_SIZE / 2;
        str.worldY = tileY * TILE_SIZE + TILE_SIZE / 2;

        addObject(str,
            str.worldX - cameraX,
            str.worldY - cameraY
        );
    }

    public void spawnEntity(Entity ent, int tileX, int tileY)
    {
        ent.worldX = tileX * TILE_SIZE + TILE_SIZE / 2;
        ent.worldY = tileY * TILE_SIZE + TILE_SIZE / 2;

        addObject(ent,
            ent.worldX - cameraX,
            ent.worldY - cameraY
        );
    }

    public void spawnFrendlyHerds(
    int biome,
    int x,
    int y,
    int desiredBiome,
    int herdSize,
    int spawnChance,
    EntityFactory factory
    )
    {
        if(biome == desiredBiome && Greenfoot.getRandomNumber(1000000) < spawnChance)
        {
            for(int i = 0; i < herdSize; i++)
            {
                int offsetX = Greenfoot.getRandomNumber(5) - 2;
                int offsetY = Greenfoot.getRandomNumber(5) - 2;

                int herdTileX = x + offsetX;
                int herdTileY = y + offsetY;

                String entKey = herdTileX + "," + herdTileY + "_entity";

                if(spawnedObjects.contains(entKey)) continue;

                if(getBiome(herdTileX, herdTileY) == desiredBiome)
                {
                    spawnedObjects.add(entKey);

                    spawnEntity(
                        factory.create(),
                        herdTileX,
                        herdTileY
                    );
                }
            }
        }
    }

    public void spawnObjects(int biome,
    int x,
    int y,
    int desiredBiome,
    int spawnChance,
    ObjectFactory factory)
    {
        if(biome == desiredBiome && Greenfoot.getRandomNumber(1000000) < spawnChance)
        {
            spawnObject(factory.create(), x,y);
        }
    }

    public void spawnStructures(int biome,
    int x,
    int y,
    int desiredBiome,
    int spawnChance,
    StructureFactory factory)
    {
        if(biome == desiredBiome && Greenfoot.getRandomNumber(1000000) < spawnChance)
        {
            spawnStructure(factory.create(), x,y);
        }
    }

    public void spawnTent(int tileX, int tileY)
    {
        // Ein neues Zelt-Objekt erzeugen (Actor/Struktur in deiner Welt)
        Tent tent = new Tent();

        // Weltkoordinaten berechnen:
        // tileX * TILE_SIZE = linke obere Ecke des Tiles
        // + TILE_SIZE/2 = Mitte des Tiles (Zelt soll zentriert stehen)
        tent.worldX = tileX * TILE_SIZE + TILE_SIZE / 2;
        tent.worldY = tileY * TILE_SIZE + TILE_SIZE / 2;

        // Das Zelt in der Welt platzieren:
        // worldX/worldY = absolute Weltposition
        // cameraX/cameraY = Kameraverschiebung
        // worldX - cameraX = Bildschirmposition
        addObject(
            tent,
            tent.worldX - cameraX,
            tent.worldY - cameraY
        );

        // Merken, dass das Zelt gespawnt wurde,
        // damit es nicht mehrfach erzeugt wird
        tentSpawned = true;
    }

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

            if(dx < playerHitboxWidth + s.hitboxWidth &&
            dy < playerHitboxHeight + s.hitboxHeight)
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
        if(Greenfoot.isKeyDown("e") && playerNearTent()==true)
        {
            lastCameraX = cameraX;
            lastCameraY = cameraY;

            Greenfoot.setWorld(TentInteriorWorld.instance);

            Greenfoot.delay(30);
        }
        if(Greenfoot.isKeyDown("e") && playerNearCave())
        {
            lastCameraX = cameraX;
            lastCameraY = cameraY;

            Greenfoot.setWorld(
                new LoadingWorld(player, this)
            );

            Greenfoot.delay(20);
        }
        if(!inventoryOpen && !craftingMenuOpen)
        {
            handleMovement();
            updateWaterAnimation();
            renderWorld();
            if (!tentSpawned) {
                findTentSpawnLocation(); 
            }
        }

        generateVisibleObjects();
        updateObjects();

        for(WorldObject obj : getNearbyObjects(80))
        {
            if(obj != null && obj.breakable && Greenfoot.mousePressed(null))
            {
                obj.damage(1);
                Greenfoot.delay(10);
            }
        }

        // ===== WATER DETECTION =====
        if(getCurrentTile() == WATER)
            player.inWater();
            
        else player.notInWater();
        
        
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

    public java.util.List<Entity> getNearbyEntitys(int radius)
    {
        java.util.List<Entity> nearby = new java.util.ArrayList<>();

        int playerWorldX = cameraX + player.getX();
        int playerWorldY = cameraY + player.getY();

        int r2 = radius * radius;

        for(Entity obj : getObjects(Entity.class))
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

    // UNTIL HERE EVERYTHING WORKS

    // ^
    // | BODENLOSER KOMMENTAR DA OBEN

    // ===== INVENTORY & CRAFTING SYSTEM =====
    public void handleInventory()
    {
        if(Greenfoot.isKeyDown("tab") && !inventoryOpen)
        {
            showInventoryScreen(true);
        }
        else if(Greenfoot.isKeyDown("tab") && inventoryOpen)
        {
            removeInventoryScreen(true);
        }
    }

    public void showInventoryScreen(boolean delay){
        inventoryOpen = true;

        inventoryScreen = new InventoryScreen();
        addObject(inventoryScreen, 400, 400);

        inventoryScreen.getItemsInventory(true);

        if(delay){
            Greenfoot.delay(20);   
        }
    }

    public void removeInventoryScreen(boolean delay){
        inventoryOpen = false;

        inventoryScreen.firstRead = false;
        inventoryScreen.setItemsInventory();

        for(Item i : itemsArray){
            removeObject(i);
        }

        removeObject(inventoryScreen);

        if(delay){
            Greenfoot.delay(20);   
        }
    }

    public void updateInventoryScreen(){
        removeInventoryScreen(false);
        showInventoryScreen(false);
    }

    public void handleCraftingMenu()
    {
        if(Greenfoot.isKeyDown("c") && !craftingMenuOpen)
        {
            craftingMenuOpen = true;

            currentCraftingPage = 1;

            craftingScreen = new CraftingScreen();
            addObject(craftingScreen, 400, 400);

            craftButtons = new TreeMap<String, CraftButtons>(Map.of(
                    "Schwert",      new SwordButton(),
                    "Axt",          new AxeButton(),
                    "Spitzhacke",   new PicaxeButton(),
                    "Seil",         new RopeButton(),
                    "Placeholder1", new PlaceholderButton1(),
                    "Pfeil",        new ArrowButton()
                )); 

            changeCraftPage();
            
            Greenfoot.delay(20);
        }
        else if(Greenfoot.isKeyDown("c") && craftingMenuOpen)
        {
            craftingMenuOpen = false;
            alreadyDrawnCraftButtons.clear();

            removeObject(craftingScreen);
            removeObject(downButton);
            removeObject(upButton);
            
            deleteCommitCraft();
            
            for(String i : craftButtons.keySet()){
                removeObject(craftButtons.get(i));
            }
            

            Greenfoot.delay(20);
        }
    }

    public void drawInventoryItems(String item, int x, int y){
        Map<String, Supplier<Item>> itemFactory = Map.ofEntries(
                Map.entry("Axt", Axe::new),
                Map.entry("Eisen", Iron::new),
                Map.entry("Spitzhacke", Pickaxe::new),
                Map.entry("Stein", Stone::new),
                Map.entry("Schwert", Sword::new),
                Map.entry("Holz", Wood::new),
                Map.entry("Blatt", Leaf::new),
                Map.entry("Seil", Rope::new),
                Map.entry("SteakGekocht", SteakCooked::new),
                Map.entry("SteakRoh", SteakRaw::new),
                Map.entry("Feder", Feather::new),
                Map.entry("Pfeil", Arrow::new),
                Map.entry("SteinSpitzhacke", StonePickaxe::new),
                Map.entry("HolzSchluessel", WoodKey::new)
            );

        Supplier<Item> supplier = itemFactory.get(item);

        if (supplier != null) {
            Item itemObj = supplier.get();
            addObject(itemObj, x, y);
            itemsArray.add(itemObj);
        }
    }

    public void drawCraftButtons(int pageNumber){
        int loop = 0;
        int drawIndex = 0;
        for(String item : craftButtons.keySet()){
            if(craftingScreen.checkIfItemsNeededWereFound(item) && loop >= ((pageNumber * 5) - 5)){
                if(loop >= (pageNumber * 5)){
                    currentButton = loop;
                    break;
                }
                addObject(craftButtons.get(item), 145, 70 + (145 * drawIndex));
                alreadyDrawnCraftButtons.add(craftButtons.get(item));
                drawIndex++;
            }
            loop++;
        }
    }

    public void changeCraftPage(){
        removeObject(upButton);
        removeObject(downButton);

        for(CraftButtons buttonObject : alreadyDrawnCraftButtons){
            removeObject(buttonObject);
        }
        if(currentCraftingPage > 1){
            drawUpButton();
        }

        int maxPages = (int) Math.ceil(craftButtons.size() / 5.0);
        if (currentCraftingPage < maxPages) {
            drawDownButton();
        }

        updateCommitCraft(false);

        drawCraftButtons(currentCraftingPage);
        Greenfoot.delay(20);
    }

    public void drawCommitCraft(boolean pressable){
        commitButton = new CommitButton(pressable);
        addObject(commitButton, 550, 700);
    }

    public void deleteCommitCraft(){
        removeObject(commitButton);
    }

    public void updateCommitCraft(boolean pressable){
        if(commitButton != null){
            deleteCommitCraft();
        }
        drawCommitCraft(pressable);
    }    

    public void drawUpButton(){
        upButton = new UpButton();
        addObject(upButton, 309, 20);
    }

    public void drawDownButton(){
        downButton = new DownButton();
        addObject(downButton, 309, 780);
    }
}