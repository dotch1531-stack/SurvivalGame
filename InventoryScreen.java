import greenfoot.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileWriter;
import org.json.*;

public class InventoryScreen extends Actor
{
    public boolean firstRead = false;

    protected TreeMap<String, Integer> inventory = new TreeMap<>();

    private final String INVENTORY_PATH =
        "items/inventar/inventory.json";

    public void act()
    {

    }

    public void getItemsInventory()
    {
        inventory.clear();

        try
        {
            String jsonText = new String(
                Files.readAllBytes(
                    Paths.get(INVENTORY_PATH)
                )
            );

            // Prevent crash if file empty
            if(jsonText.trim().isEmpty())
            {
                jsonText = "{}";
            }

            JSONObject inventoryJSON =
                new JSONObject(jsonText);

            for(String key : inventoryJSON.keySet())
            {
                inventory.put(
                    key,
                    inventoryJSON.getInt(key)
                );
            }

            firstRead = true;

            System.out.println("Inventory loaded:");
            System.out.println(inventory);
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

        updateVisualItems();
    }

    public void setItemsInventory()
    {
        JSONObject json = new JSONObject(inventory);

        try
        {
            FileWriter writer =
                new FileWriter(INVENTORY_PATH);

            writer.write(json.toString(4));

            writer.flush();
            writer.close();

            System.out.println("Inventory saved.");
        }
        catch(IOException e)
        {
            System.out.println("Could not save inventory.");
            e.printStackTrace();
        }
    }

    public void updateVisualItems()
    {
        MyWorld w = (MyWorld)getWorld();

        if(w == null)
            return;

        if(inventory.getOrDefault("Holz", 0) > 0)
        {
            w.drawWood();
        }
    }

    public void addItem(String item, int amount)
    {
        inventory.put(
            item,
            inventory.getOrDefault(item, 0) + amount
        );
    }

    public void removeItem(String item, int amount)
    {
        int current =
            inventory.getOrDefault(item, 0);

        current -= amount;

        if(current <= 0)
        {
            inventory.remove(item);
        }
        else
        {
            inventory.put(item, current);
        }
    }

    public int getItemAmount(String item)
    {
        return inventory.getOrDefault(item, 0);
    }
}