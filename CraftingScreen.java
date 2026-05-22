import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;

public class CraftingScreen extends Actor
{   
    public void act()
    {   
        handleCraftButtons();
    }
    public void handleCraftButtons(){
        MyWorld world = (MyWorld)getWorld();
        
        if(Greenfoot.mousePressed(world.swordButton)){
            System.out.println("swordButton Pressed");
        }
    }
}
