import greenfoot.*;
import java.util.*;

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

    @Override
    public void act()
    {
        if (!onScreen())
            return;

        super.act();          // Entity (Animation etc.)


        //Animation("cow");
    }
}