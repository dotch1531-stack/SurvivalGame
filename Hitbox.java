import greenfoot.*;
import java.util.List;

public class Hitbox extends Actor
{
    public Hitbox()
    {
        setImage("attackHitbox.png");
    }

    public void checkHits(int DamageEntity, int DamageObject,int DamageTree, int DamageStone, int DamageIron)
    {
        List<Entity> entities = getIntersectingObjects(Entity.class);
        List<WorldObject> objects = getIntersectingObjects(WorldObject.class);
        List<Tree> tree = getIntersectingObjects(Tree.class);
        List<StoneNode> stone = getIntersectingObjects(StoneNode.class);
        List<IronNode> iron = getIntersectingObjects(IronNode.class);

        for(Entity e : entities)
        {
            e.damage(1 * DamageEntity);
        }
        
        for(WorldObject o : objects)
        {
            o.damage(1 * DamageObject);
        }
        
        for(Tree t : tree)
        {
            t.damage(1 * DamageTree);
        }
        
        for(StoneNode s : stone)
        {
            s.damage(1 * DamageStone);
        }
        
        for(IronNode i : iron)
        {
            i.damage(1 * DamageIron);
        }
    }
}