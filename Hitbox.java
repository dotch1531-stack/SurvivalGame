import greenfoot.*;
import java.util.List;

public class Hitbox extends Actor
{
    public Hitbox()
    {
        setImage("attackHitbox.png");
    }

    public void checkHits(int DamageEntity, int DamageObject)
    {
        List<Entity> entities = getIntersectingObjects(Entity.class);
        List<WorldObject> objects = getIntersectingObjects(WorldObject.class);

        for(Entity e : entities)
        {
            e.damage(1 * DamageEntity);
        }
        
        for(WorldObject o : objects)
        {
            o.damage(1 * DamageObject);
        }
    }
}