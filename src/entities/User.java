package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user account in the system
 *
 * inventory: List of item IDs owned by User
 * wishlist: List of item IDs wished to be owned by User
 * status: The status of the User
 * cityAddress: User's address in terms of city-country
 * email: User's email
 */
public class User extends Account implements Serializable {
    private List<String> inventory = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private List<String> wishlist = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private UserStatus status;  // an enum indicating the current status of the User
    private String cityAddress;
    private String email;

    /**
     * Creates an instance of User
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password, String cityAddress, String email) {
        super(username, password);
        setId("@User" + getId());
        status = UserStatus.GOOD;
        this.cityAddress = cityAddress;
        this.email = email;
    }

    /**
     * Returns the user's inventory of items, represented by unique IDs
     * @return an List of Strings representing the user's inventory of item IDs
     */
    public List<String> getInventory() {
        return inventory;
    }

    /**
     * Returns the user's wishlist of items, represented by unique IDs
     * @return an list of Strings representing the user's wishlist of item IDs
     */
    public List<String> getWishlist() {
        return wishlist;
    }

    /**
     * Gets the user's status
     * @return the UserStatus enum that is associated with this user's status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the user's inventory list to a given ArrayList of item IDs
     * @param inventory a List of item IDs that this user's inventory will be set to
     */
    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    /**
     * Sets the user's wishlist list to a given ArrayList of item IDs
     * @param wishlist a List of item IDs that this user's wishlist will be set to
     */
    public void setWishlist(List<String> wishlist) {
        this.wishlist = wishlist;
    }

    /**
     * Adds an item ID to the user's inventory if it doesn't already exist
     * @param itemId the item ID of an Item that this user is willing to lend
     * @return true iff the item ID is added to the user's inventory
     */
    public boolean addToInventory(String itemId) {
        if (inventory.contains(itemId)) {
            return false;
        } else {
            inventory.add(itemId);
            return true;
        }
    }

    /**
     * Removes an item ID from the user's inventory if it exists
     * @param itemId the item ID of an Item that this user would like to remove from their inventory
     * @return true iff the item ID is present in and removed from the user's inventory
     */
    public boolean removeFromInventory(String itemId) {
        if (!inventory.contains(itemId)) {
            return false;
        } else {
            inventory.remove(itemId);
            return true;
        }
    }

    /**
     * Adds an item ID to the user's wishlist if it doesn't already exist
     * @param itemId the item ID of an Item that this user is wishing to borrow
     * @return true iff the item ID is added to the user's wishlist
     */
    public boolean addToWishlist(String itemId) {
        if (wishlist.contains(itemId)) {
            return false;
        } else {
            wishlist.add(itemId);
            return true;
        }
    }

    /**
     * Removes an item ID from the user's wishlist if it exists
     * @param itemId the item ID of an Item that this user would like to remove from their wishlist
     * @return true iff the item ID is present in and removed from the user's wishlist
     */
    public boolean removeFromWishlist(String itemId) {
        if (!wishlist.contains(itemId)) {
            return false;
        } else {
            wishlist.remove(itemId);
            return true;
        }
    }

    /**
     * Sets the user's status
     * @param status the new status of the user
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Sets the user's address
     * @param cityAddress the new address for User
     */
    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    /**
     * Gets the address of a User
     * @return the String representation of the User's address
     */
    public String getCityAddress() {
        return this.cityAddress;
    }

    /**
     * Sets the user's email
     * @param email the new email for User
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the email of a User
     * @return the String representation of the User's email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns a String that describes this user by their username and current status
     * @return a String representation of the user
     */
    @Override
    public String toString() {
        return "Username: " + getUsername() + "\nAccount type: User \nAccount status: " + status + "\nCity: " +
                getCityAddress() + "\nEmail: " + getEmail();
    }

}
