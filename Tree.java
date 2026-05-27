public class Tree extends WorldObject
{
    
    public Tree()
    {
        hitboxWidth = 20;
        hitboxHeight = 20;
        solid = true;
        breakable = true;

        maxHealth = 6;
        health = maxHealth;
    }
}