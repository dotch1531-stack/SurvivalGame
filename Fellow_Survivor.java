import greenfoot.*;

public class Fellow_Survivor extends Aggressiv_Entity
{
    // ===== SPEED =====
    private double speed = 1;
    private double chaseSpeed = 1.5;

    // ===== HOME POSITION =====
    private double homeX;
    private double homeY;

    // ===== TARGET =====
    private double targetX;
    private double targetY;

    // ===== TIMERS =====
    private int patrolTimer;

    // ===== SETTINGS =====
    private int patrolRadius = 4 * 120;
    private int detectionRange = 10 * 120;

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
    // SPAWN INIT
    // =========================================================

    protected void addedToWorld(World world)
    {
        // ECHTE Spawnposition speichern
        homeX = worldX;
        homeY = worldY;

        chooseRandomTarget();
    }

    // =========================================================
    // ACT
    // =========================================================

    public void act()
    {
        Player player = getPlayer();

        if(player == null) return;

        // Distanz zum Spieler
        double distanceToPlayer =
            Math.sqrt(worldX * worldX + worldY * worldY);

        // ===== SPIELER IN SICHT =====
        if(distanceToPlayer <= detectionRange)
        {
            chasePlayer();
        }
        // ===== RANDOM MOVEMENT =====
        else
        {
            patrol();
        }

        animate();
    }

    // =========================================================
    // PATROL
    // =========================================================

    private void patrol()
    {
        patrolTimer--;

        moveTowards(targetX, targetY, speed);

        double dx = targetX - worldX;
        double dy = targetY - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance < 20 || patrolTimer <= 0)
        {
            chooseRandomTarget();
        }
    }

    // =========================================================
    // RANDOM TARGET
    // =========================================================

    private void chooseRandomTarget()
    {
        patrolTimer =
        120 + Greenfoot.getRandomNumber(180);

        targetX =
        homeX +
        Greenfoot.getRandomNumber(
            patrolRadius * 2 + 1
        ) -
        patrolRadius;

        targetY =
        homeY +
        Greenfoot.getRandomNumber(
            patrolRadius * 2 + 1
        ) -
        patrolRadius;
    }

    // =========================================================
    // CHASE PLAYER
    // =========================================================

    private void chasePlayer()
    {
        // Spieler ist immer Bildschirmmitte
        double playerX = 0;
        double playerY = 0;

        double dx = playerX - worldX;
        double dy = playerY - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance > 0)
        {
            // Richtung normalisieren
            dx /= distance;
            dy /= distance;

            // Bewegung
            worldX += dx * chaseSpeed;
            worldY += dy * chaseSpeed;
        }
    }

    // =========================================================
    // MOVE
    // =========================================================

    private void moveTowards(
    double tx,
    double ty,
    double moveSpeed)
    {
        double dx = tx - worldX;
        double dy = ty - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance > 0)
        {
            dx /= distance;
            dy /= distance;

            worldX += dx * moveSpeed;
            worldY += dy * moveSpeed;
        }
    }

    // =========================================================
    // PLAYER
    // =========================================================

    private Player getPlayer()
    {
        if(getWorld().getObjects(Player.class).isEmpty())
        {
            return null;
        }

        return getWorld()
        .getObjects(Player.class)
        .get(0);
    }

    // =========================================================
    // ANIMATION
    // =========================================================

    private void animate()
    {

    }
}