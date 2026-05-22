import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CraftingScreen extends Actor
{   
    private TreeMap<String, Integer> itemsNeeded = new TreeMap<>();
    
    public void act()
    {   
        handleCraftButtons();
    }
    public void handleCraftButtons(){
        MyWorld world = (MyWorld)getWorld();
        
        if(Greenfoot.mousePressed(world.swordButton)){
            craftItem("Schwert");
            Greenfoot.delay(20);
        }
        if(Greenfoot.mousePressed(world.commitButton)){
            System.out.println("craft");
        }
    }
    public void craftItem(String itemToCraft){
        MyWorld world = (MyWorld)getWorld();
        
        getItemsNeeded(itemToCraft);
        world.drawCommitCraft();
    }
    public void getItemsNeeded(String item){
        itemsNeeded.clear();
        
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            
            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }
            
            JSONObject itemsJSON = new JSONObject(jsonText);
            
            JSONObject neededItemsJSON = itemsJSON.getJSONObject("recipe");
            for(String key : neededItemsJSON.keySet()){
                itemsNeeded.put(key, neededItemsJSON.getInt(key));
            }
        }
        catch(IOException e)
        {
            System.out.println("Could not read item file.");
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            System.out.println("Inventory JSON invalid.");
            e.printStackTrace();
        }
    }
}
