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
    
    public boolean firstRead = false;
    
    protected TreeMap<String, Integer> inventory = new TreeMap<>();
    
    
    /**
     * Falls ihr neue Items selber hinzufügen wollt:
     * 
     * Step by Step anleitung um neue Items im Inventar anzuzeigen (für dullies):
     * 
     * 1. Bild initialisieren (codestelle 1)
     * 2. Bild mit dazugehörigem schlüssel in die Map eintragen (codestelle 2 + Anweisungen an codestelle befolgen)
     * 3. Bildkoordinaten setzen (einfach als neue Integers in die arrays) (codestelle 3)
     * 4. Bild gegebenenfalls scallieren (codestelle 4)
     * 
     * Falls es nicht klappt (ihr zu schlecht seid), fragt einfach mich (der Gott der das geschrieben hat)
     * Noch ne ergänzung die bilder sollten am besten alle von Vecteezy sein und auf 150x150 scalliert sein,
     * weil die angegebenen Positionen gelten nur für die einzelnen Slots und nicht für die Items, die alphabetisch geordnet sind.
     * außerdem werden items die man nicht hat(also 0 sind) übergangen und nicht angezeigt
     */
    
    /*  1  */
    GreenfootImage woodImg = new GreenfootImage("wood.png");
    GreenfootImage stoneImg = new GreenfootImage("stein.png");
    GreenfootImage ironImg = new GreenfootImage("iron.png");
    
    TreeMap<String, GreenfootImage> images = new TreeMap<>();
    
    /*  3  */
    int[] x = {25, 225, 425};
    int[] y = {-5, -5, -5};
    
    
    /**
     * Act - do whatever the InventoryScreen wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        
    }
    public void getItemsInventory(){
        setImageMap();
        scaleImages();
        
        try{
            String json = new String(Files.readAllBytes(Paths.get("items/inventar/inventory.json")));
            JSONObject inventoryJSON = new JSONObject(json);
            
            for(String i : inventoryJSON.keySet()){
                inventory.put(i, inventoryJSON.getInt(i));
            }
            
            firstRead = true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        showItemsInventory();
    }
    public void setItemsInventory(){
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
    
    /*  2  */
    public void setImageMap(){
        /*  Schlüssel (erster Parameter) mus gleich mit JSON name(schlüssel) sein & zweiter Parameter das Bild  */
        images.put("Holz", woodImg);
        images.put("Stein", stoneImg);
        images.put("Eisen", ironImg);
    }
    
    /*  4  */
    public void scaleImages(){
        stoneImg.scale(150, 150);
        ironImg.scale(150, 150);
        woodImg.scale(150, 150);
    }
    
    
    public void showItemsInventory(){
        GreenfootImage img = getImage();
        
        int loop = 0;
        for(String i : inventory.keySet()){
            if(inventory.getOrDefault(i, 0) > 0){
                img.drawImage(images.get(i), x[loop], y[loop]);
                loop++;
            }
        }
    }
}
