import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileWriter;
import org.json.*;

/**
 * Write a description of class InventoryScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InventoryScreen extends Actor
{
    
    boolean firstRead = false;
    private TreeMap<String, Integer> inventory = new TreeMap<>();
    
    /**
     * Act - do whatever the InventoryScreen wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(!firstRead){
            getItemsInventory();
        }
        if(Greenfoot.isKeyDown("tab")){
            firstRead = false;
            setItemsInventory();
            Greenfoot.setWorld(new MyWorld());
        }
        Greenfoot.delay(3);
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
    private void setItemsInventory(){
        JSONObject json = new JSONObject(inventory);
        
        try{
            FileWriter writer = new FileWriter("items/inventar/inventory.json");

            writer.write(json.toString(4));
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
