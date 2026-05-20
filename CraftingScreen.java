import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;

public class CraftingScreen extends Actor
{
    
    private GreenfootImage swordButton = new GreenfootImage("SwordButton.png");

    
    public void act()
    {
        // Add your action code here.
    }
    public void setCraftingImages(){
        getImage().drawImage(swordButton, 10, 10);
    }
}
