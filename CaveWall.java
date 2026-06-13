import greenfoot.*;

public class CaveWall extends Structures

{
    public CaveWall()
    {
        setImage(new GreenfootImage(40, 40));
        getImage().setColor(Color.BLACK);
        getImage().fill();
        hitboxWidth = 1;
        hitboxHeight = 1;

        

        solid = true;
    }
}