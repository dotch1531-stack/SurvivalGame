import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Aggressiv_Entity here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Aggressiv_Entity extends Entity
{
    /**
     * Act - do whatever the Aggressiv_Entity wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
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
    
    private boolean justHit = false;

    // ===== SETTINGS =====
    private int patrolRadius = 2 * 120;
    private int detectionRange = 3 * 120;
    
    int hitTimer = 50;
    
    
    protected void addedToWorld(World world)
    {
        homeX = worldX;
        homeY = worldY;
    
        lastWorldX = worldX;
        lastWorldY = worldY;
    
        chooseRandomTarget();
    }

    // =========================================================
    // ACT
    // =========================================================

    public void act()
    {
        Player player = getPlayer();
    
        if(player == null || justHit){ justHit = false; return;}
    
        MyWorld world = (MyWorld)getWorld();
    
        double playerX = world.cameraX + player.getX();
        double playerY = world.cameraY + player.getY();
    
        double dx = playerX - worldX;
        double dy = playerY - worldY;
    
        double distanceToPlayer =
            Math.sqrt(dx * dx + dy * dy);
    
        if(distanceToPlayer <= detectionRange)
        {
            chasePlayer();
        }
        else
        {
            patrol();
        }
        if(distanceToPlayer <= 20){
            hitTimer--;
            
            if (hitTimer <= 0) {
                player.damage(1);
                hitTimer = 50; // reset if you want repeated damage
            }
        
        }
    
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
        MyWorld world = (MyWorld)getWorld();
        Player player = getPlayer();
    
        // REAL player world position
        double playerX = world.cameraX + player.getX();
        double playerY = world.cameraY + player.getY();
    
        double dx = playerX - worldX;
        double dy = playerY - worldY;
    
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        if(distance > 0)
        {
            dx /= distance;
            dy /= distance;
    
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
}
