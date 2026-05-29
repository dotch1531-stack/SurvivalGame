import greenfoot.*;

public class Fellow_Survivor extends Aggressiv_Entity
{

    public Fellow_Survivor()
    {
        // ===== IMAGE =====
        setImage("Animals/fellow.png");

        // ===== HITBOX =====
        hitboxWidth = 240;
        hitboxHeight = 120;

        // ===== HEALTH =====
        maxHealth = 15;
        health = maxHealth;

        breakable = true;
    }

    // =========================================================
    // ANIMATION
    // =========================================================

    private void animate()
    {

    }
}