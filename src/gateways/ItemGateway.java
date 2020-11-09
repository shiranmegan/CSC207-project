package gateways;

import java.util.*;

import entities.*;
import usecases.ItemManager;

/**
 * ItemGateway is a Gateway class for Item
 */
public class ItemGateway extends Gateway {

    public ItemGateway() {

    }

    /**
     * Updates ItemManager im to item.ser
     * @param im ItemManager to be updated
     */
    public void updateItemInfo(ItemManager im) {
        String fileName = "src/database/Item.ser";
        List<Item> serList = new ArrayList<>();
        Map<String, Item> imIdToItem = im.getIdToItem();
        for (Map.Entry<String, Item> entry: imIdToItem.entrySet()){
            serList.add(entry.getValue());
        }
        updateInfo(serList, fileName);
    }

    /**
     * Get information from item.ser
     * @return List of Items from .ser
     */
    @Override
    public List<Item> getInfo() {
        return helperGetInfo("database/Item.ser");
    }

}

