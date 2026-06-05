import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Leaf here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Leaf extends Item
{
    public Leaf(){
        GreenfootImage img = new GreenfootImage("InventorySprites/Blatt.png");
        img.scale(150, 150);
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public String getName(){
        return "Blatt";
    }
}
