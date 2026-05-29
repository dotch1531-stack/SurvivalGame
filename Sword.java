import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sword extends Item
{
    private final String INVENTORY_PATH = "items/Schwert.json";
    
    private String jsonText;
    private JSONObject objectJSON;
    
    public Sword(){
        GreenfootImage img = new GreenfootImage("InventorySprites/Sword_Inventory.png");
        img.scale(150, 150);
        setImage(img);
    }
    public void act()
    {
        // Add your action code here.
    }
    public int getItemDamage(){
        getJsonFile();
        
        return objectJSON.getInt("schaden");
        
    }
    public void getJsonFile(){
        try{
            jsonText = new String(Files.readAllBytes(Paths.get(INVENTORY_PATH)));

            if(jsonText.trim().isEmpty())
            {
                jsonText = "{}";
            }

            objectJSON = new JSONObject(jsonText);
        }
        catch(IOException e)
        {
            System.out.println("Could not read inventory file.");
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            System.out.println("Inventory JSON invalid.");
            e.printStackTrace();
        }
    }
}
