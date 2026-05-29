import greenfoot.*;

public class Guard extends Aggressiv_Entity
{

    public Guard()
    {
        // ===== IMAGE =====
        setImage("Animals/guard.png");

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
