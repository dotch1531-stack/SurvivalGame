import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import org.json.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// produzierte Fehler durch falsches back-end: 3

public class CraftingScreen extends Actor
{   
    private TreeMap<String, Integer> itemsNeeded = new TreeMap<>();
    private int stackSize;
    private String itemToBeCrafted;
    
    private int[] itemsNeededCountNumberX = {400, 407, 600, 607};
    
    private boolean firstRead = false;
    
    private boolean selectedCraft = false;
    
    private InventoryScreen inventoryScreen;
    
    GreenfootImage zero = new GreenfootImage("Font/0.png");
    GreenfootImage one = new GreenfootImage("Font/1.png");
    GreenfootImage two = new GreenfootImage("Font/2.png");
    GreenfootImage three = new GreenfootImage("Font/3.png");
    GreenfootImage four = new GreenfootImage("Font/4.png");
    GreenfootImage five = new GreenfootImage("Font/5.png");
    GreenfootImage six = new GreenfootImage("Font/6.png");
    GreenfootImage seven = new GreenfootImage("Font/7.png");
    GreenfootImage eight = new GreenfootImage("Font/8.png");
    GreenfootImage nine = new GreenfootImage("Font/9.png");
    
    GreenfootImage[] numberArray = {zero, one, two, three, four, five, six, seven, eight, nine};
    
    int[] numbersX = {300, 317, 400, 417};
    int[] numbersY = {300, 300, 300, 300};
    
    private GreenfootImage baseImage;
    
    public CraftingScreen(){
        inventoryScreen = new InventoryScreen();
    }
    public void act()
    {   
        if(!firstRead){
            scaleImages();
        }
        
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
    
    private void scaleImages()
    {    
        for(int i = 0; i < 10; i++){
            numberArray[i].scale(15, 30);
        }
    }
    
    private void addNumbers(){       
        int loop = 0;
        for(String key : itemsNeeded.keySet()){
            if(itemsNeeded.getOrDefault(key, 0) > 0){
                String intToString = Integer.toString(itemsNeeded.getOrDefault(key, 0));
                
                if(intToString.length() < 2){
                    intToString = "0" + intToString;
                }
            
                for(int i = 0; i < intToString.length(); i++){
                    int number = Integer.parseInt(String.valueOf(intToString.charAt(i)));
                    getImage().drawImage(numberArray[number], numbersX[loop], numbersY[loop]);
                    loop += 1;
                }
            }
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
        
        addNumbers();
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
    public boolean checkIfItemsNeededWereFound(String item){
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            ArrayList<String> itemsNeededToBeFound = new ArrayList<String>();
            
            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }
            
            JSONObject itemsJSON = new JSONObject(jsonText);
            
            stackSize = itemsJSON.getInt("stackSize");
            
            JSONObject neededItemsJSON = itemsJSON.getJSONObject("recipe");
            for(String key : neededItemsJSON.keySet()){
                if(!inventoryScreen.itemExistsInInventory(key)){
                    return false;
                }
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
        return true;
    }
}
