import greenfoot.*;

public class LoadingWorld extends World
{
    private int timer = 0;

    private Player player;
    private MyWorld returnWorld;

    public LoadingWorld(Player p, MyWorld world)
    {
        super(800, 800, 1);

        player = p;
        returnWorld = world;

        addObject(new LoadingScreen(), 400, 400);
    }

    public void act()
{
    timer++;

    if(timer > 1)
    {
        int spawnX = returnWorld.cameraX + 400;
        int spawnY = returnWorld.cameraY + 400;

        Greenfoot.setWorld(new CaveInteriorWorld(
                player,
                returnWorld,
                spawnX,
                spawnY
            )
        );
    }
}
}