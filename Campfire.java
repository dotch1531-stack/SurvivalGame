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
            
            if(!inventoryScreen.checkIfItemCanBeRemoved("SteakRoh", 1)){
                return;
            }
            
            inventoryScreen.removeItemsNoCheck("SteakRoh", 1);
            
            Greenfoot.delay(5);
            
            //Textur feuer ändern
            
            inventoryScreen.addItem("SteakGekocht", 1);
            
            solid = true;
        }
    }
}