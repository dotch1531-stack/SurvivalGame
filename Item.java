import greenfoot.*;
import org.json.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Item extends Actor
{
    
    JSONObject itemJSON;
    
    public void act()
    {
        // Add your action code here.
    }
    
    //polymorphie, nicht entfernen
    public String getName(){
        return "Irgendwas ist schief gelaufen";
    }
    
    public int getDamage(String item){
        return getJSON(item).getInt("schaden");
    }
    
    
    public JSONObject getJSON(String item){
        JSONObject itemJSON;
        try{
            String jsonText = new String(Files.readAllBytes(Paths.get("items/" + item + ".json")));
            
            if(jsonText.trim().isEmpty()){
                jsonText = "{}";
            }
            
            itemJSON = new JSONObject(jsonText);
            return itemJSON;
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
        return null;
    }
}
