import greenfoot.*;

public class TentInteriorWorld extends World
{

    public static TentInteriorWorld instance;

    public TentInteriorWorld()
    {
        super(800, 800, 1);
        instance = this;

        GreenfootImage bg = getBackground();

        bg.setColor(new Color(120, 90, 60));
        bg.fill();

        // 3x3 Raum zeichnen
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                bg.setColor(new Color(170,130,80));

                bg.fillRect(
                    280 + x * 80,
                    280 + y * 80,
                    80,
                    80
                );
            }
        }

        addObject(new TentPlayer(), 400, 400);
        instance = this;
    }

    public void act(){
        MyWorld.instance.handleInventory();
        MyWorld.instance.handleCraftingMenu();
    }
    
}