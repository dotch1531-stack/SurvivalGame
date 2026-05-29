import greenfoot.*;

public class Cow extends Passiv_Entity
{
    public Cow()
    {
        // ===== IMAGE =====
        setImage("Animals/cow/cowIdle.png");

        // ===== POSITION =====
        worldX = 0;
        worldY = 0;

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

        walkTimer = 0;
        idleTimer = 0;
    }

    @Override
    public void act()
    {
        if (!onScreen())
            return;

        super.act();          // Entity (Animation etc.)
        naturalMovement();    // Passiv_Entity Bewegung

        Animation("cow");
    }
}