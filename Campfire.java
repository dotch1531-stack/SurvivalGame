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
            inventoryScreen.addItem("SteakGekocht", 1);
            inventoryScreen.removeItem("SteakRoh", 1);
            Greenfoot.delay(5);
        }
    }
}