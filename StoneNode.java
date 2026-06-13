import greenfoot.*;

public class StoneNode extends WorldObject

{
    
    public StoneNode()
    {
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