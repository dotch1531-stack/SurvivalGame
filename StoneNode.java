import greenfoot.*;
import java.util.*;

public class StoneNode extends WorldObject

{
    
    public StoneNode()
    {
        
        drops = List.of("Stein");
        dropAmount = 1;
        setImage("stoneNode.png");
        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 20;
        hitboxHeight = 10;

        // ===== MOVEMENT =====

        breakable = true;

        maxHealth = 15;
        health = maxHealth;
    }
}