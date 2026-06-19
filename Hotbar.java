import greenfoot.*;

public class Hotbar extends InventoryScreen
{
    private int hotbarSelected = 0;
    private GreenfootImage img;

    public Hotbar()
    {
        img = new GreenfootImage("hotbar.png");
        img.scale(300, 75);
        setImage(img);
    }

    public void changeSlot()
    {
        GreenfootImage newImg = new GreenfootImage("Hotbar/Hotbar_"+hotbarSelected+".png");
        newImg.scale(300, 75);
        setImage(newImg);
    }

    public void act()
    {
        for (int i = 1; i <= 4; i++)
        {
            if (Greenfoot.isKeyDown(String.valueOf(i)))
            {
                if (hotbarSelected != i)
                {
                    hotbarSelected = i;
                    changeSlot();
                }
                break;
            }
        }
    }
}