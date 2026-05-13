import greenfoot.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import org.json.*;

public class MyWorld extends World
{
    boolean firstRead = false;
    private TreeMap<String, Integer> inventory = new TreeMap<>();
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        
    }
    public void act(){
        if(!firstRead){
            getItemsInventory();
        }
    }
    private void getItemsInventory(){
        try{
            String json = new String(Files.readAllBytes(Paths.get("items/inventar/inventory.json")));
            
            JSONObject inventoryJSON = new JSONObject(json);
            
            for(String i : inventoryJSON.keySet()){
                inventory.put(i, inventoryJSON.getInt(i));
            }
            firstRead = true;
            System.out.println(inventory);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
