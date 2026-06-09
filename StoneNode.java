import greenfoot.*;

public class StoneNode extends WorldObject

{
    
    public StoneNode()
    {
        setImage("stoneNode.png");
        solid = true;
        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 20;
        hitboxHeight = 15;

        // ===== MOVEMENT =====

        breakable = true;

        maxHealth = 15;
        health = maxHealth;
    }
}