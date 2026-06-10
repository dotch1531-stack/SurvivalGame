import greenfoot.*;
import java.util.*;

public class Cow extends Passiv_Entity
{
    public Cow()
    {
        
        drops = List.of("Blatt", "Holz");
        dropAmount = Greenfoot.getRandomNumber(2)+1;
        
        // ===== IMAGE =====
        setImage("Animals/cow/cowIdle.png");

        // ===== POSITION =====
        worldX = 0;
        worldY = 0;
        
        // ===== ANIMATION =====
        animationSpeed = 20;

        // ===== HITBOX =====
        hitboxWidth = 240;
        hitboxHeight = 120;

        // ===== MOVEMENT =====
        speed = 1;

        // ===== STATUS =====
        breakable = true;
        maxHealth = 15;
        health = maxHealth;

        dirX = 0;
        dirY = 0;
        
        //=====Area=====
        walkTimer = 0;
        idleTimer = 0;
        allowedTiles = new int[]
        {
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

        Animation("cow", 120, 120);
    }
}