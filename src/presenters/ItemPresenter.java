package presenters;

import entities.Item;

import java.util.List;

public class ItemPresenter implements IPresenter {

    // ==== Common presenter method ====

    /**
     * Print prompt asking for input from user
     */
    @Override
    public void selectOptionPrompt() {
        System.out.println("Please select an option by entering the corresponding number");
    }

    /**
     * Prints menu to go back to upper level menu
     */
    @Override
    public void goBackToUpperMenu() {
        System.out.println("Please enter 0 to go back to upper level menu");
    }


    /**
     * The message presented if the input value for a menu is out of range.
     */
    @Override
    public void invalidInputMessage() {
        System.out.println("The input you entered is invalid, please try again!");
    }

    // === End ===

    // ==== sub methods ====

    /**
     * The menu prompt for one item.
     */
    public void addItemWishlistMenu(){
        selectOptionPrompt();
        System.out.println("1. Add item to wishlist");
        goBackToUpperMenu();
    }

    /**
     * The menu prompt for the items
     */
    public void itemInventoryMenu() {
        selectOptionPrompt();
        System.out.println("1. Remove item");
        System.out.println("2. Edit item name");
        System.out.println("3. Edit item description");
        goBackToUpperMenu();
    }

    /**
     * remove item from wishlist
     */
    public void removeItemWishlistMenu(){
        selectOptionPrompt();
        System.out.println("1. Remove item from wishlist");
        goBackToUpperMenu();
    }

    /**
     * remove item from inventory list
     */
    public void removeItemInventoryMenu(){
        selectOptionPrompt();
        System.out.println("1. Remove item from inventory");
        goBackToUpperMenu();
    }

    /**
     * The prompt for entering an item to be added to the inventory,
     */
    public void addItemName(){
        System.out.println("Please enter the name of the item");
        goBackToUpperMenu();
    }

    /**
     * The prompt to add the description for a given item being added to the inventory.
     */
    public void addItemDescription(){
        System.out.println("Please enter the description of the item");
        goBackToUpperMenu();
    }

    /**
     * The confirmation message if the item is successfully added to the inventory.
     */
    public void itemCreationSuccess(){
        System.out.println("Item successfully added to the system for review!");
    }

    /**
     * The confirmation message if the item is successfully removed from the inventory.
     */
    public void removalSuccessMessage(){
        System.out.println("Item successfully removed!");
    }

    /**
     * The confirmation message if the item is failed to be removed from the inventory
     */
    public void removalFailMessage() {
        System.out.println("This item cannot be removed from inventory.");
    }

    /**
     * The confirmation message if the item is successfully added to the wishlist.
     */
    public void addToWishlistMessage(){
        System.out.println("Item successfully added to your wishlist!");
    }

    /**
     * The confirmation message if the item is successfully removed from the wishlist.
     */
    public void removeFromWishlistMessage(){
        System.out.println("Item successfully removed from wishlist!");
    }

    /**
     * Print prompt when user entered an invalid item name
     */
    public void invalidItemName() {
        System.out.println("The name entered contains invalid characters, please try again!");
    }

    /**
     * Print prompt when user entered an invalid description
     */
    public void invalidDescription() {
        System.out.println("The description entered contains invalid characters, please try again!");
    }

    /**
     * Prompt information that edit is success
     */
    public void editItemNameSuccess() {
        System.out.println("Item name successfully edited!");
    }

    /**
     * Prompt information that item description is successfully edited
     */
    public void editItemDescriptionSuccess() {
        System.out.println("Item description successfully edited!");
    }

    /**
     * Get item information
     * @param items the list of items
     * @param owner the corresponding owner of the item
     */
    public void getItemInfo(List<Item> items, List<String> owner) {
        System.out.println("== List of Items ==");
        if (items.isEmpty()) {
            System.out.println("No items available");
        } else {
            System.out.println("Please select an item to be viewed by entering the corresponding number!");
            int i = 1;
            for (int j = 0; j < items.size(); j++) {
                System.out.println(i + ". " + items.get(j));
                System.out.println("Owned by " + owner.get(j));
                i++;
            }
            goBackToUpperMenu();
        }
    }

    /**
     * Prints the requested items menu
     */
    public void requestedItemMenu() {
        selectOptionPrompt();
        System.out.println("1. Remove item");
        System.out.println("2. Approve item");
        goBackToUpperMenu();
    }

    /**
     * Prints the requested items menu
     */
    public void removeItemMenu() {
        selectOptionPrompt();
        System.out.println("1. Remove item");
        goBackToUpperMenu();
    }

    /**
     * Get list of information
     * @param listInfo a list of object that contains information
     * @param <T> any kind of object
     */
    public <T> void getListInfo(List<T> listInfo) {
        if (listInfo.isEmpty()) {
            System.out.println("Sorry! There is nothing to view!");
        } else {
            System.out.println("Please select an item to be viewed by entering the corresponding number!");
            int i = 1;
            for (T info: listInfo) {
                System.out.println(i + ". " + info);
                i++;
            }
            goBackToUpperMenu();
        }
    }

    /**
     * Prompt that item cannot add to the wishlist
     */
    public void failedToAddMessage(){
        System.out.println("This item cannot be added to your wishlist, please try another item!");
    }
    // === End ===
}
