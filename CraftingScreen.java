import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CraftingScreen extends Actor
{   
    private TreeMap<String, Integer> itemsNeeded = new TreeMap<>();
    
    private boolean selectedCraft = false;
    
    public void act()
    {   
        handleCraftButtons();
    }
    public void handleCraftButtons(){
        MyWorld world = (MyWorld)getWorld();
        
        if(Greenfoot.mousePressed(world.swordButton)){
            selectedCraft = true;
            craftItem("Schwert");
            Greenfoot.delay(20);
        }
        if(Greenfoot.mousePressed(world.commitButton) && selectedCraft){
            System.out.println("craft");
            selectedCraft = false;
        }
    }
    public void craftItem(String itemToCraft){
        getItemsNeeded(itemToCraft);
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
