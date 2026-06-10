import greenfoot.*;

public class CaveWall extends Structures

{
    
    public int hitboxWidth = 40;
    public int hitboxHeight = 40;
    public CaveWall()
    {
        setImage(new GreenfootImage(40, 40));
        getImage().setColor(Color.BLACK);
        getImage().fill();

        

        solid = true;
    }
}