import greenfoot.*;

public class Tree extends WorldObject
{
    // ===== MOVEMENT =====
    private int dirX;
    private int dirY;


    private int speed;

    public Tree()
    {
        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 20;
        hitboxHeight = 15;

        // ===== MOVEMENT =====
        dirX = 0;
        dirY = 0;

        speed = 1;

        breakable = true;

        maxHealth = 15;
        health = maxHealth;
    }
    
}