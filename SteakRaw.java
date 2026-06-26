import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SteakRaw here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SteakRaw extends Item
{
    public SteakRaw(boolean forHotbar){
        GreenfootImage img = new GreenfootImage("InventorySprites/RawSteak.png");
        if(forHotbar){
            img.scale(50, 50);
        }
        else{
            img.scale(150, 150);    
        }
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public String getName(){
        return "SteakRoh";
    }
}
