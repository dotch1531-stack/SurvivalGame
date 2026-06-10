import greenfoot.*;

public class Campfire extends Actor
{
    public boolean solid = true;

    public Campfire()
    {
        setImage("campfire.png");
    }

    public void act()
    {
       cook();
    }

    public void cook(){
        if (Greenfoot.mouseClicked(this)){
            InventoryScreen inventoryScreen = new InventoryScreen();
            
            
        }
    }
}