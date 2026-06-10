import greenfoot.*;
import java.util.*;

public class CaveInteriorWorld extends MyWorld
{
    public static CaveInteriorWorld instance;

    // ===== TILE SYSTEM =====
    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;
    private GreenfootImage stoneNode;
    private boolean generated = false;

    public static final int TILE_SIZE = 40;

    // ===== CAMERA =====
    public int cameraX = 0;
    public int cameraY = 0;

    // ===== PLAYER =====
    public Player player;
    
    private HashSet<String> generatedTiles = new HashSet<>();
    
    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;
    
    public static final int FLOOR = 0;
    public static final int WALL = 1;
    
    private static final int WALL_THICKNESS = 1;
    
    private int[][] caveMap = new int[MAP_WIDTH][MAP_HEIGHT];
    
    CaveWalls caveWalls = new CaveWalls();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public CaveInteriorWorld(Player playerCave)
    {
        instance = this;

        tileSet = new GreenfootImage("tileset.png");
        tiles = new GreenfootImage[6];

        // TILE LOADING
        for (int i = 0; i < 6; i++)
        {
            tiles[i] = new GreenfootImage(TILE_SIZE, TILE_SIZE);
            tiles[i].drawImage(tileSet, -i * TILE_SIZE, 0);
        }
        generateCave();
    }
    
    public void act()
    {
        handleMovement();
        renderWorld();
        updateObjects();
        
        caveWalls.setLocation(
        caveWalls.worldX - cameraX,
        caveWalls.worldY - cameraY
    );
    }
    
    public void handleMovement()
    {
        int speed = 4;
    
        if (Greenfoot.isKeyDown("w"))
            cameraY = Math.max(0, cameraY - speed);
    
        if (Greenfoot.isKeyDown("s"))
            cameraY = Math.min(
                MAP_HEIGHT * TILE_SIZE - getHeight(),
                cameraY + speed
            );
    
        if (Greenfoot.isKeyDown("a"))
            cameraX = Math.max(0, cameraX - speed);
    
        if (Greenfoot.isKeyDown("d"))
            cameraX = Math.min(
                MAP_WIDTH * TILE_SIZE - getWidth(),
                cameraX + speed
            );
    }
    
    public int getTile(int x, int y)
    {
        if(x < 0 || y < 0 ||
           x >= MAP_WIDTH  || y >= MAP_HEIGHT)
        {
            return WALL;
        }
    
        return caveMap[x][y];
    }
    
    public void renderWorld()
    {
        getBackground().setColor(Color.BLACK);
        getBackground().fill();
    
        int tilesX = getWidth() / TILE_SIZE + 2;
        int tilesY = getHeight() / TILE_SIZE + 2;
    
        int startX = Math.floorDiv(cameraX, TILE_SIZE);
        int startY = Math.floorDiv(cameraY, TILE_SIZE);
    
        int offsetX = -(cameraX - startX * TILE_SIZE);
        int offsetY = -(cameraY - startY * TILE_SIZE);
    
        for (int y = 0; y < tilesY; y++)
        {
            for (int x = 0; x < tilesX; x++)
            {
                int worldTileX = startX + x;
                int worldTileY = startY + y;
    
                int tile = getTile(worldTileX, worldTileY);
    
                GreenfootImage img = tiles [1];
    
                if (tile == WALL)
                {
                    img = null;
                }
                else
                {
                    img = tiles[1];
                }
                
                if(img != null)
                {
                    getBackground().drawImage(
                        img,
                        x * TILE_SIZE + offsetX,
                        y * TILE_SIZE + offsetY
                    );
                }
    
                getBackground().drawImage(
                    img,
                    x * TILE_SIZE + offsetX,
                    y * TILE_SIZE + offsetY
                );
            }
        }
    }
    
    private boolean isBorderWall(int x, int y)
    {
        if(caveMap[x][y] != WALL)
            return false;
    
        for(int dx = -1; dx <= 1; dx++)
        {
            for(int dy = -1; dy <= 1; dy++)
            {
                int nx = x + dx;
                int ny = y + dy;
    
                if(nx < 0 || ny < 0 ||
                   nx >= MAP_WIDTH || ny >= MAP_HEIGHT)
                    continue;
    
                if(caveMap[nx][ny] == FLOOR)
                    return true;
            }
        }
    
        return false;
    }
    
    private void generateStoneWalls()
    {
        for(int x = 0; x < MAP_WIDTH; x++)
        {
            for(int y = 0; y < MAP_HEIGHT; y++)
            {
                if(isBorderWall(x,y))
                {
                    CaveWalls wall = new CaveWalls();
    
                    wall.worldX = x * TILE_SIZE + TILE_SIZE/2;
                    wall.worldY = y * TILE_SIZE + TILE_SIZE/2;
    
                    addObject(
                        wall,
                        wall.worldX - cameraX,
                        wall.worldY - cameraY
                    );
                }
            }
        }
    }
    
    
    private void generateCave()
    {
        // Everything floor
        for(int x = 0; x < MAP_WIDTH; x++)
        {
            for(int y = 0; y < MAP_HEIGHT; y++)
            {
                caveMap[x][y] = FLOOR;
            }
        }
        

    
        generateBorders();
        generateStoneWalls();

    }
    
    private void generateBorders()
    {
        for(int x = 0; x < MAP_WIDTH; x++)
        {
            for(int y = 0; y < MAP_HEIGHT; y++)
            {
                int left =
                    15 + (int)(Math.sin(y * 0.15) * 3);
    
                int right =
                    MAP_WIDTH - 15 +
                    (int)(Math.cos(y * 0.12) * 3);
    
                int top =
                    15 + (int)(Math.sin(x * 0.12) * 3);
    
                int bottom =
                    MAP_HEIGHT - 15 +
                    (int)(Math.cos(x * 0.15) * 3);
    
                if(x < left ||
                   x > right ||
                   y < top ||
                   y > bottom)
                {
                    caveMap[x][y] = WALL;
                }
            }
        }
    }
    
    
    
    
    
    
    

}