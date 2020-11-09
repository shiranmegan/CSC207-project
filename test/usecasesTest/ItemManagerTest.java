//package usecasesTest;
//
//import entities.Item;
//import org.junit.Test;
//import usecases.ItemManager;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.junit.Assert.*;
//
//
//
//public class ItemManagerTest{
//    Item item1 = new Item("book", "This is a book");
//    Item item2 = new Item("pen", "This is a pen");
//    Item item3 = new Item("pen", "This is a pen");
//    Item item4 = new Item("pencil");
//    Item item5 = new Item("computer", "This is a computer");
//    Item item6 = new Item("phone", "");
//    ArrayList<Item> itemList = new ArrayList<Item>(Arrays.asList(item1, item2, item3, item4));
//    ItemManager im = new ItemManager(itemList);
//
//    @Test
//    public void testConstructor(){
//        assertEquals(im.getAllIds().size(), 4);
//        ItemManager temp = new ItemManager();
//        assertEquals(temp.getAllIds().size(), 0);
//    }
//
//    @Test
//    public void testAddItem(){
//        im.addItem(item5);
//        assertEquals(im.getAllIds().size(), 5);
//        im.addItem(item5);
//        assertEquals(im.getAllIds().size(), 5);
//        im.addItem(item6);
//        assertEquals(im.getAllIds().size(), 6);
//    }
//
//    @Test
//    public void testGetAllIds() {
//        itemList.add(item5);
//        itemList.add(item6);
//        ArrayList<String> allIds = im.getAllIds();
//        assertEquals(allIds.size(), 6);
//        for (Item item: itemList) {
//            assertTrue(allIds.contains(item.getId()));
//        }
//    }
//
//    public void testCreateNewItem(){
//
//    }
//
//    public void testFindItem(){
//
//    }
//
//    public void testGetItemString(){
//
//    }
//
//    public void testGetItemName(){
//
//    }
//
//    public void testGetItemStatus(){
//
//    }
//
//    public void testGetAllIdsWithNames(){
//
//    }
//
//    public void testToString(){
//
//    }
//
//}
