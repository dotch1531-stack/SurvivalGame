import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Wood extends Item
{
    public Wood(){
        GreenfootImage img = new GreenfootImage("InventorySprites/Holz.png");
        img.scale(150, 150);
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public String getName(){
        return "Holz";
    }
}
