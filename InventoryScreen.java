import greenfoot.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileWriter;
import org.json.*;

public class InventoryScreen extends Actor
{
    /*
     * Add item inventory : 
     * 1. add item as subclass of Item
     * 2. create variable in MyWorld
     * 3. increase index of itemArray in MyWorld
     * 4. in MyWorld add item to array under the switch cases
     * 5. add new case to the switch case (name needs to be in german mit großen anfangsbuchstaben wie in backend)
     */
    
    
    public boolean firstRead = false;

    protected TreeMap<String, Integer> inventory = new TreeMap<>();
    
    private final String INVENTORY_PATH = "items/inventar/inventory.json";
    
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
    
    GreenfootImage[] numberArray = {zero, one, two, three, four, five, six, seven, eight, nine};
    int[] numbersX = {123, 140, 323, 340, 523, 540, 723, 740, 123, 140, 323, 340, 523, 540, 723, 740};
    int[] numbersY = {130, 130, 130, 130, 130, 130, 130, 130, 310, 310, 310, 310, 310, 310, 310, 310};
    

    int[] x = {100, 300, 500, 700, 100, 300, 500, 700};
    int[] y = {75, 75, 75, 75, 260, 260, 260, 260};

    public void act()
    {
        itemPressed();
    }

    public void getItemsInventory(boolean executedToShowItemsInInventory)
    {
        inventory.clear();

        try
        {
            String jsonText = new String(Files.readAllBytes(Paths.get(INVENTORY_PATH)));

            // Prevent crash if file empty
            if(jsonText.trim().isEmpty())
            {
                jsonText = "{}";
            }

            JSONObject inventoryJSON = new JSONObject(jsonText);

            for(String key : inventoryJSON.keySet())
            {
                inventory.put(key, inventoryJSON.getInt(key));
            }

            firstRead = true;
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
        
        if(executedToShowItemsInInventory){
            scaleImages();
            showInventoryBackground();
            showItemsInventory();
            addNumbers();
        }
    }

    public void setItemsInventory()
    {
        JSONObject json = new JSONObject(inventory);

        try
        {
            FileWriter writer = new FileWriter(INVENTORY_PATH);

            writer.write(json.toString(4));

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Could not save inventory.");
            e.printStackTrace();
        }
    }

    public void addItem(String item, int amount)
    {
        getItemsInventory(false);
        int stackSize = getStackSize(item);
        
        if((inventory.getOrDefault(item, 0) + amount) > stackSize){
            inventory.put(item, stackSize);
            
            System.out.println("Items weggeworfen");
            
            //  überschuss muss hier ausgeworfen werden
            //  überschuss als neuen stack werten ist schwierig weil json parser keine doppelten werte zulassen und maps leider auch nicht   :(
            //  gesammte datenstrucktur müsste umgestellt werden + crafting system + gesammtes inventory system + schlechtere performance + fehleranfälliger
            //  daher sons of the forrest design
        }
        else{
            int countItems = 0;
            for(String key : inventory.keySet()){
                if(inventory.get(key) > 0){
                    countItems++;
                }
            }
            if(countItems < 12){
                inventory.put(item, inventory.getOrDefault(item, 0) + amount);
            }
            else{
                System.out.println("zu viele items im inventar");
            }
        }
        System.out.println(inventory);
        setItemsInventory();
    }
    public int getStackSize(String item){
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            
            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }
            
            JSONObject itemsJSON = new JSONObject(jsonText);
            
            
            return itemsJSON.getInt("stackSize");
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
        return 0;
    }

    public boolean checkIfItemCanBeRemoved(String item, int amount)
    {
        getItemsInventory(false);
        
        int current = inventory.getOrDefault(item, 0);

        current -= amount;
        
        if(current < 0){
            return false;
        }
        else{
            return true;
        }
    }
    
    public void removeItems(String item, int amount){
        getItemsInventory(false);
        
        int current = inventory.getOrDefault(item, 0);

        current -= amount;
        
        inventory.put(item, current);
        
        setItemsInventory();
    }

    public int getItemAmount(String item)
    {
        return inventory.getOrDefault(item, 0);
    }
    
    public boolean itemExistsInInventory(String item){
        getItemsInventory(false);
        if(inventory.get(item) == null){
            return false;
        }
        return true;
    }

    public void scaleImages()
    {    
        for(int i = 0; i < 10; i++){
            numberArray[i].scale(15, 30);
        }
    }

    public void showItemsInventory()
    {
        GreenfootImage img = getImage();
        MyWorld world = (MyWorld)getWorld();

        int loop = 0;

        for (String i : inventory.keySet())
        {
            if (inventory.getOrDefault(i, 0) > 0)
            {
                world.drawInventoryItems(i, x[loop], y[loop]);
                loop++;
            }
        }
    }
    
    public void addNumbers(){
        int loop = 0;
        for(String key : inventory.keySet()){
            if(inventory.getOrDefault(key, 0) > 0){
                String intToString = Integer.toString(inventory.getOrDefault(key, 0));
                
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
    
    public void itemPressed(){
        MyWorld world = (MyWorld)getWorld();
        
        Item clicked = null;

        for(Item item : world.itemsArray){
            if(item != null && Greenfoot.mousePressed(item)){
                clicked = item;
                break;
            }
        }

        if(clicked != null){
            removeItems(clicked.getName(), 1);
            world.updateInventoryScreen();
            
            //Items droppen lassen!!!
        }
    }
    
    public void showInventoryBackground(){
        int countItems = 0;
        for(String key : inventory.keySet()){
            if(inventory.getOrDefault(key, 0) > 0){
                countItems++;
            }
        }
        String countItemsString = Integer.toString(countItems);
        
        GreenfootImage img = new GreenfootImage("InventorySprites/inventarBackgrounds/" + countItemsString + ".png");
        setImage(img);
    }
}
