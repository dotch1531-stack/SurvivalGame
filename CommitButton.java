import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class CommitButton extends CraftingScreen
{
    public CommitButton(boolean pressable){
        if(pressable){
            GreenfootImage img = new GreenfootImage("Crafting/Craftbutton_Enabled.png");
            setImage(img);
        }
        else{
            GreenfootImage img = new GreenfootImage("Crafting/Craftbutton_Disabled.png");
            setImage(img);
        }
    }
    public void act()
    {
        // Add your action code here.
    }
}
