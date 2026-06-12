import greenfoot.*;
import java.util.*;

public class Duck extends Passiv_Entity
{
    public Duck()
    {
        // ===== IMAGE =====
        setImage("Animals/duck/duckWater/duckIdle.png");

        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

        // ===== HITBOX =====
        hitboxWidth = 40;
        hitboxHeight = 40;

        // ===== MOVEMENT =====
        speed = 1;

        // ===== STATUS =====
        breakable = true;
        maxHealth = 5;
        health = maxHealth;

        dirX = 0;
        dirY = 0;
        
        //=====Area=====
        walkTimer = 0;
        idleTimer = 0;
        allowedTiles = new int[]
        {
            MyWorld.WATER,
            MyWorld.GRASS
        };
    }
    
    @Override
        public void act()
        {
            if (!onScreen())
            return;

            super.act();          // Entity (Animation etc.)
            naturalMovement();    // Passiv_Entity Bewegung
            
            Animation("duck", 40, 40);
        }
}