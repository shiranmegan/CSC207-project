//package entitiesTest;
//
//import org.junit.*;
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertTrue;
//
//import entities.*;
//
//import java.util.ArrayList;
//// import java.util.List;
//// import java.util.Arrays;
//
//public class UserTest {
//    User u1 = new User("wei1", "1Wei");
//    User u2 = new User("wei2", "2Wei");
//    User u3 = new User("wei3", "3Wei");
//
//    @Test
//    public void testChangeUsername() {
//        String u1Id = u1.getId();
//        u1.setUsername("weiwei1");
//
//        assertNotEquals(u1.getUsername(), "wei1");
//        assertEquals(u1.getUsername(), "weiwei1");
//        assertEquals(u1.getId(), u1Id);  // user ID should not change
//    }
//
//    @Test
//    public void testChangePassword() {
//        u1.setPassword("0Wei");
//
//        assertNotEquals(u1.getPassword(), "1Wei");
//        assertEquals(u1.getPassword(), "0Wei");
//    }
//
//    @Test
//    public void testIdUniqueness() {
//        assertNotEquals(u1.getId(), u2.getId());
//    }
//
//    @Test
//    public void testInitialStatus() {
//        assertEquals(u1.getStatus(), UserStatus.GOOD);
//    }
//
//    @Test
//    public void testRemoveFromEmptyInventory() {
//        assertFalse(u1.removeFromInventory("item1"));
//    }
//
//    @Test
//    public void testInventoryAddAndRemove() {
//        assertTrue(u1.addToInventory("item1"));
//
//        assertTrue(u1.addToInventory("item2"));
//        assertTrue(u1.addToInventory("item3"));
//
//        assertTrue(u2.addToInventory("item2"));
//        assertTrue(u2.addToInventory("item3"));
//        assertTrue(u2.addToInventory("item4"));
//
//
//        assertTrue(u1.getInventory().contains("item3"));
//        assertTrue(u2.getInventory().contains("item4"));
//
//        assertTrue(u1.removeFromInventory("item1"));
//        assertFalse(u1.getInventory().contains("item1"));
//    }
//
//    @Test
//    public void testRemoveNonExistentFromInventory() {
//        assertFalse(u1.removeFromInventory("item4"));
//    }
//
//    @Test
//    public void testRemoveFromEmptyWishlist() {
//        assertFalse(u1.removeFromWishlist("item1"));
//    }
//
//    @Test
//    public void testWishlistAddRemove() {
//        assertTrue(u1.addToWishlist("item99"));
//        assertTrue(u1.addToWishlist("item98"));
//        assertTrue(u1.addToWishlist("item97"));
//
//        assertTrue(u2.addToWishlist("item98"));
//        assertTrue(u2.addToWishlist("item97"));
//        assertTrue(u2.addToWishlist("item96"));
//
//        assertTrue(u1.getWishlist().contains("item97"));
//        assertTrue(u2.getWishlist().contains("item96"));
//
//        assertTrue(u1.removeFromWishlist("item99"));
//        assertFalse(u1.getWishlist().contains("item99"));
//    }
//
//    @Test
//    public void testRemoveNonExistentFromWishlist() {
//        assertFalse(u1.removeFromWishlist("item96"));
//    }
//
//    @Test
//    public void testSetStatus() {
//        u1.setStatus(UserStatus.FLAGGED);
//
//        assertEquals(u1.getStatus(), UserStatus.FLAGGED);
//    }
//
//}
