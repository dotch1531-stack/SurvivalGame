import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// produzierte Fehler durch falsches back-end: 2

public class CraftingScreen extends Actor
{   
    private TreeMap<String, Integer> itemsNeeded = new TreeMap<>();
    private int stackSize;
    private String itemToBeCrafted;
    
    private int[] itemsNeededCountNumberX = {400, 407, 600, 607};
    
    private boolean selectedCraft = false;
    
    private InventoryScreen inventoryScreen;

    public CraftingScreen()
    {
        inventoryScreen = new InventoryScreen();
    }
    
    public void act()
    {   
        handleCraftButtons();
    }
    
    public void handleCraftButtons(){
        MyWorld world = (MyWorld)getWorld();
        
        
        //recipe button
        if(Greenfoot.mousePressed(world.swordButton)){
            selectedCraft = true;
            selectedCraftItem("Schwert");
            Greenfoot.delay(20);
        }
        if(Greenfoot.mousePressed(world.axeButton)){
            selectedCraft = true;
            selectedCraftItem("Axt");
            Greenfoot.delay(20);
        }
        if(Greenfoot.mousePressed(world.picaxeButton)){
            selectedCraft = true;
            selectedCraftItem("Spitzhacke");
            Greenfoot.delay(20);
        }
        
        
        //craft button
        if(Greenfoot.mousePressed(world.commitButton) && selectedCraft){
            craftItem();
            selectedCraft = false;
            Greenfoot.delay(20);
        }
    }
    
    private void craftItem(){
        boolean canCraft = true;
        
        for(String key : itemsNeeded.keySet()){
            System.out.println(key + " " + itemsNeeded.get(key));
            if(!inventoryScreen.checkIfItemCanBeRemoved(key, itemsNeeded.get(key))){
                System.out.println("Nicht genügend Items für dieses Rezept");
                canCraft = false;
            }
        }
        
        if(canCraft){
            for(String key : itemsNeeded.keySet()){
                inventoryScreen.removeItems(key, itemsNeeded.get(key));
            }
            
            //parameters = Item, amount, stackSize
            inventoryScreen.addItem(itemToBeCrafted, 1, stackSize);
            
            System.out.println("Item erfolgreich gecrafted");
        }
    }
    
    private void selectedCraftItem(String itemToCraft){
        getItemsNeeded(itemToCraft);
        
        itemToBeCrafted = itemToCraft;
    }
    
    public void getItemsNeeded(String item){
        itemsNeeded.clear();
        
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            
            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }
            
            JSONObject itemsJSON = new JSONObject(jsonText);
            
            stackSize = itemsJSON.getInt("stackSize");
            
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
        
        System.out.println(itemsNeeded);
    }
}
