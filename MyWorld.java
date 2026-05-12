import greenfoot.*;
import java.util.*;
import org.json.simple.JSONObject;

public class MyWorld extends World
{
    private TreeMap<String, Integer> inventory = new TreeMap<>();
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        
    }
    public void act(){
        getItems();
    }
    private void getItems(){
        inventory.put("Holz", 20);
        inventory.put("Stein", 1);
        
        for(String i : inventory.keySet()){
            System.out.println(i + " , " + inventory.getOrDefault(i, 0));
        }
    }
}
