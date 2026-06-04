import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Rope here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rope extends Item
{
    
    public Rope(){
        GreenfootImage img = new GreenfootImage("InventorySprites/Seil.png");
        img.scale(150, 150);
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public String getName(){
        return "Seil";
    }
}
