import greenfoot.*;
import java.util.*;

public class CaveInteriorWorld extends MyWorld
{
    public static CaveInteriorWorld instance;

    // ===== TILE SYSTEM =====
    private GreenfootImage tileSet;
    private GreenfootImage[] tiles;
    private GreenfootImage StoneNode;

    private int[][] caveMap = new int[100][100];
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

        

    }
    
    
    public void act()
    {
        handleMovement();
        renderWorld();
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
        if (x <10 || y < 9 || x >= MAP_WIDTH -10 || y >= MAP_HEIGHT - 9)        {
            return WALL;
        }
    
        if (x < WALL_THICKNESS ||
            y < WALL_THICKNESS ||
            x >= MAP_WIDTH - WALL_THICKNESS ||
            y >= MAP_HEIGHT - WALL_THICKNESS)
        {
            return WALL;
        }
    
        return FLOOR;
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
    
                GreenfootImage img;
    
                if (tile == WALL)
                {
                    img = tiles[2]; // rock tile
                }
                else
                {
                    img = tiles[0]; // cave floor tile
                }
    
                getBackground().drawImage(
                    img,
                    x * TILE_SIZE + offsetX,
                    y * TILE_SIZE + offsetY
                );
            }
        }
    }


}