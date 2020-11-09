//package entitiesTest;
//
//import entities.Item;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//public class ItemTest{
//    Item item1 = new Item("book", "This is a book");
//    Item item2 = new Item("pen", "This is a pen");
//    Item item3 = new Item("pen", "This is a pen");
//    Item item4 = new Item("pencil");
//    Item item5 = new Item("computer", "This is a computer");
//    Item item6 = new Item("phone", "");
//
//    @Test
//    public void testIdUniqueness() {
//        assertNotEquals(item2.getId(), item3.getId());
//    }
//
//    @Test
//    public void testStatus() {
//        assertEquals(item1.getStatus(), Item.ItemStatus.REQUESTED);
//        assertEquals(item2.getStatus(), Item.ItemStatus.REQUESTED);
//    }
//
//    @Test
//    public void testSetName() {
//        String id1 = item1.getId();
//        item1.setName("a CS book");
//        assertEquals(item1.getName(), "a CS book");
//        assertEquals(item1.getDescription(), "This is a book");
//        assertEquals(item1.getId(), id1);
//        assertEquals(item1.getStatus(), Item.ItemStatus.REQUESTED);
//    }
//
//    @Test
//    public void testSetStatus() {
//        item1.setStatus(Item.ItemStatus.AVAILABLE);
//        assertEquals(item1.getStatus(), Item.ItemStatus.AVAILABLE);
//        item2.setStatus(Item.ItemStatus.UNAVAILABLE);
//        assertEquals(item2.getStatus(), Item.ItemStatus.UNAVAILABLE);
//        item3.setStatus(Item.ItemStatus.AVAILABLE);
//        assertEquals(item1.getStatus(), item3.getStatus());
//    }
//
//    @Test
//    public void setDescription() {
//        item1.setDescription("This is a new book");
//        assertEquals(item1.getDescription(), "This is new book");
//        item2.setDescription("");
//        assertEquals(item2.getDescription(), "");
//        item4.setDescription("This is a pencil");
//        assertEquals(item4.getDescription(), "This is a pencil");
//    }
//
//    @Test
//    public void testToString() {
//        String id5 = item5.getId();
//        assertEquals(item5.toString(), id5 + "(Requested): computer, This is a computer");
//        String id6 = item6.getId();
//        assertEquals(item6.toString(), id6 + "(Requested): phone, ");
//    }
//}