import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Feather here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Feather extends Item
{
    public Feather(){
        GreenfootImage img = new GreenfootImage("InventorySprites/Feder.png");
        img.scale(150, 150);
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public String getName(){
        return "Feder";
    }
}
