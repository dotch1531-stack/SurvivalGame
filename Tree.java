import greenfoot.*;
import java.util.*;

public class Tree extends WorldObject
{
    // ===== MOVEMENT =====
    private int dirX;
    private int dirY;


    private int speed;
     

    public Tree()
    {
        drops = List.of("Blatt", "Holz");
        dropAmount = Greenfoot.getRandomNumber(2)+1;
        
        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 20;
        hitboxHeight = 45;

        // ===== MOVEMENT =====
        dirX = 0;
        dirY = 0;

        speed = 1;

        breakable = true;

        maxHealth = 15;
        health = maxHealth;

    }
        
    
    
}