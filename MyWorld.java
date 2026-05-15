import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    public boolean inventoryOpen = false;
    
    private InventoryScreen inventoryScreen;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
    }
    public void act()
    {
        if(Greenfoot.isKeyDown("tab") && !inventoryOpen)
        {            
            inventoryOpen = true;

            inventoryScreen = new InventoryScreen();
            addObject(inventoryScreen, 400, 400);
            
            inventoryScreen.getItemsInventory();
            
            Greenfoot.delay(10);
        }
        else if(Greenfoot.isKeyDown("tab") && inventoryOpen)
        {   
            inventoryOpen = false;
            
            inventoryScreen.firstRead = false;
            inventoryScreen.setItemsInventory();

            removeObject(inventoryScreen);
            
            Greenfoot.delay(10);
        }
    }
}
