import greenfoot.*;
import java.util.*;

public class duck extends Passiv_Entity
{
    public duck()
    {
        
        drops = List.of("SteakRoh","Feder");
        dropAmount = Greenfoot.getRandomNumber(1)+1;
        
        // ===== IMAGE =====
        setImage("Animals/duck/duckIdle.png");

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
            MyWorld.WATER
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