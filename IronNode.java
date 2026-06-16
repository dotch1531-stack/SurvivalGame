import greenfoot.*;

public class IronNode extends WorldObject

{
    
    public IronNode()
    {
        setImage("ironNode.png");
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