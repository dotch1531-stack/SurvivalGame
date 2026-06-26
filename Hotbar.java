import greenfoot.*;

public class Hotbar extends InventoryScreen
{
    private GreenfootImage img;
    public int currentSlot;

    public Hotbar()
    {
        img = new GreenfootImage("hotbar.png");
        img.scale(300, 75);
        setImage(img);
    }

    public void changeSlot(int slot)
    {
        GreenfootImage newImg = new GreenfootImage("Hotbar/Hotbar_"+ slot +".png");
        newImg.scale(300, 75);
        currentSlot = slot;
        setImage(newImg);
    }

    public void act()
    {
        if(Greenfoot.isKeyDown("1")){changeSlot(1);}
        else if(Greenfoot.isKeyDown("2")){changeSlot(2);}
        else if(Greenfoot.isKeyDown("3")){changeSlot(3);}
        else if(Greenfoot.isKeyDown("4")){changeSlot(4);}
    }
}