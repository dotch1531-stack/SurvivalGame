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
    private String itemToBeCrafted;

    private int[] itemsNeededCountNumberX = {400, 407, 600, 607};

    private boolean firstRead = false;

    private boolean selectedCraft = false;

    private InventoryScreen inventoryScreen;

    GreenfootImage zero = new GreenfootImage("Font/PixelTransparent/0.png");
    GreenfootImage one = new GreenfootImage("Font/PixelTransparent/1.png");
    GreenfootImage two = new GreenfootImage("Font/PixelTransparent/2.png");
    GreenfootImage three = new GreenfootImage("Font/PixelTransparent/3.png");
    GreenfootImage four = new GreenfootImage("Font/PixelTransparent/4.png");
    GreenfootImage five = new GreenfootImage("Font/PixelTransparent/5.png");
    GreenfootImage six = new GreenfootImage("Font/PixelTransparent/6.png");
    GreenfootImage seven = new GreenfootImage("Font/PixelTransparent/7.png");
    GreenfootImage eight = new GreenfootImage("Font/PixelTransparent/8.png");
    GreenfootImage nine = new GreenfootImage("Font/PixelTransparent/9.png");
    
    GreenfootImage blankImg = new GreenfootImage("Crafting/removalBackground.png");

    GreenfootImage[] numberArray = {zero, one, two, three, four, five, six, seven, eight, nine};

    int[] numbersX = {600, 617, 600, 617, 600, 617, 600, 617, 600, 617};
    int[] numbersY = {100, 100, 200, 200, 300, 300, 400, 400, 500, 500};

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
        handleDownButton();
    }

    public void handleDownButton(){
        MyWorld world = (MyWorld)getWorld();

        if(Greenfoot.mousePressed(world.downButton)){
            world.currentCraftingPage +=1;
            world.changeCraftPage();
        }
        if(Greenfoot.mousePressed(world.upButton)){
            world.currentCraftingPage -=1;
            world.changeCraftPage();
        }
    }

    public void handleCraftButtons(){
        MyWorld world = (MyWorld)getWorld();

        //recipe button
        for(String item : world.craftButtons.keySet()){
            if(Greenfoot.mousePressed(world.craftButtons.get(item))){
                selectedCraft = true;
                selectedCraftItem(item);
                Greenfoot.delay(20);
            }
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
        getImage().drawImage(blankImg, 450, 30);
        
        int loop = 0;
        for(String key : itemsNeeded.keySet()){
            if(itemsNeeded.getOrDefault(key, 0) > 0){
                GreenfootImage img = new GreenfootImage("InventorySprites/" + key + ".png");
                img.scale(100,100);
                getImage().drawImage(img, (numbersX[loop] - 150), (numbersY[loop] - 35));

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
        if(checkItemsAreInInventory()){
            for(String key : itemsNeeded.keySet()){
                inventoryScreen.removeItemsNoCheck(key, itemsNeeded.get(key));
            }

            //parameters = Item, amount
            inventoryScreen.addItem(itemToBeCrafted, 1);

            System.out.println("Item erfolgreich gecrafted");

            MyWorld world = (MyWorld)getWorld();
            world.updateCommitCraft(false);
        }
    }

    private boolean checkItemsAreInInventory(){
        boolean canCraft = true;
        for(String key : itemsNeeded.keySet()){
            System.out.println(key + " " + itemsNeeded.get(key));
            if(!inventoryScreen.checkIfItemCanBeRemoved(key, itemsNeeded.get(key))){
                System.out.println("Nicht genügend Items für dieses Rezept");
                canCraft = false;
            }
        }
        return canCraft;
    }

    private void selectedCraftItem(String itemToCraft){
        MyWorld world = (MyWorld)getWorld();
        boolean canCraft = true;

        getItemsNeeded(itemToCraft);

        itemToBeCrafted = itemToCraft;

        if(checkItemsAreInInventory()){
            world.updateCommitCraft(true);
        }
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

    public boolean checkIfItemsNeededWereFound(String item){
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            ArrayList<String> itemsNeededToBeFound = new ArrayList<String>();

            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }

            JSONObject itemsJSON = new JSONObject(jsonText);

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
