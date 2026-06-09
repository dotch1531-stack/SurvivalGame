import greenfoot.*;
import java.util.*;

public class Guard extends Aggressiv_Entity
{

    public Guard()
    {
        // ===== IMAGE =====
        setImage("Animals/guard/guardIdle.png");

        // ===== HITBOX =====
        hitboxWidth = 240;
        hitboxHeight = 120;

        // ===== HEALTH =====
        maxHealth = 15;
        health = maxHealth;

        breakable = true;
    }

    @Override
    public void act()
    {
        if (!onScreen())
            return;

        super.act();          // Entity (Animation etc.)


        Animation("guard");
    }
}
