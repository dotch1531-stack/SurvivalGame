import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Pickaxe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WoodKey extends Item
{
    /**
     * Act - do whatever the Pickaxe wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public WoodKey(boolean forHotbar){
        GreenfootImage img = new GreenfootImage("InventorySprites/HolzSchluessel.png");
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
        return "HolzSchluessel";
    }
}
