import greenfoot.*;
import java.util.*;

public class pig extends Passiv_Entity
{
    public pig()
    {
        // ===== IMAGE =====
        setImage("Animals/pig/pigIdle.png");

        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 120;
        hitboxHeight = 120;

        // ===== MOVEMENT =====
        speed = 1;

        // ===== STATUS =====
        breakable = true;
        maxHealth = 15;
        health = maxHealth;

        dirX = 0;
        dirY = 0;

        walkTimer = 0;
        idleTimer = 0;
        
        //=====Area=====
        allowedTiles = new int[]
        {
            MyWorld.GRASS,
            MyWorld.ROCK
        };
    }

    @Override
    public void act()
    {
        if (!onScreen())
            return;

        super.act();          // Entity (Animation etc.)
        naturalMovement();    // Passiv_Entity Bewegung

        Animation("pig", 120, 120);
    }
}