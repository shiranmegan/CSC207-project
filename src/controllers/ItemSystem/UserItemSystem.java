package controllers.ItemSystem;

import controllers.IController;
import presenters.UserPresenter;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.List;
import java.util.Scanner;

/**
 * UserItemSystem is to view options for the User
 *
 * up is the User Presenter
 */
public class UserItemSystem extends ItemSystem implements IController {

    private final UserPresenter up = new UserPresenter();

    /**
     * Constructor of the UserItemSystem
     * @param userId the userId that wants to manage userId's inventory/wishlist
     * @param um the UserManager
     * @param im the ItemManager
     * @param tm the TradeManager
     */
    public UserItemSystem(String userId, UserManager um, ItemManager im, TradeManager tm) {
        super(userId, um, im, tm);
    }

    /**
     * Present the main menu for the UserItemSystem
     */
    @Override
    public void presentMainMenu(){
        getUp().UserItemMainMenu();
    }

    /**
     * Specific menu for UserItemSystem
     * @param selection the input from user
     */
    @Override
    public void specificMenu(String selection) {
        switch (selection) {
            case "2":
                if (!isGuest()) {
                    addToInventory();
                }
                break;
            case "3":
                if (!isGuest()) {
                    viewInventory();
                }
                break;
            case "4":
                if (!isGuest()) {
                    viewWishlist();
                }
                break;
            default:
                getIp().invalidInputMessage();
                break;
        }
    }

    /**
     * View action options for the currentItemId
     * @param currentItemId the itemId to be viewed for action options
     */
    @Override
    public void viewItemsOption(String currentItemId) {
        getIp().addItemWishlistMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            case "0":
                break;
            case "1":
                if (!isGuest()) {
                    addToWishlist(currentItemId, getCurrentUserId());
                }
                break;
            default:
                getIp().invalidInputMessage();
                viewItemsOption(currentItemId);
                break;
        }
    }

    // ==== ADD TO INVENTORY ====

    private void addToInventory() {
        String itemName = getItemName();
        if (!itemName.equalsIgnoreCase(getReturnStr())) {
            String itemDescription = getItemDescription();
            if (!itemDescription.equalsIgnoreCase(getReturnStr())) {
                String itemId = getIm().createNewItem(itemName, itemDescription);
                getUm().addToInventory(getCurrentUserId(), itemId);
                getIp().itemCreationSuccess();
            }
        }
    }

    // ==== THE END ====


    // ==== VIEW INVENTORY ====

    private void viewInventory() {
        List<String> inventory = getUm().inventoryByUserId(getCurrentUserId());
        getIp().getListInfo(getIm().giveListItem(inventory));
        if (!inventory.isEmpty()) {
            String currentItemId = inputOption(inventory);
            if (currentItemId.equalsIgnoreCase(getReturnStr())) return;
            inventoryOption(currentItemId);
        }
    }

    private void inventoryOption(String currentItemId){
        getIp().itemInventoryMenu();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1":
                removeItemFromInventory(currentItemId);
                break;
            case "2":
                String itemName = getItemName();
                if (!itemName.equalsIgnoreCase(getReturnStr())) {
                    getIm().setItemName(currentItemId, itemName);
                    getIp().editItemNameSuccess();
                }
                break;
            case "3":
                String itemDescription = getItemDescription();
                if (!itemDescription.equalsIgnoreCase(getReturnStr())) {
                    getIm().setItemDescription(currentItemId, itemDescription);
                    getIp().editItemDescriptionSuccess();
                }
                break;
            default:
                getIp().invalidInputMessage();
                inventoryOption(currentItemId);
                break;
        }
    }

    private String getItemName() {
        Scanner input = new Scanner(System.in);
        getIp().addItemName();
        String itemName = input.nextLine();
        while (containsInvalidString(itemName) && !itemName.equalsIgnoreCase(getReturnStr())) {
            getIp().invalidItemName();
            itemName = input.nextLine();
        }
        return itemName;
    }

    private String getItemDescription() {
        Scanner input = new Scanner(System.in);
        getIp().addItemDescription();
        String itemDescription = input.nextLine();
        while (containsInvalidString(itemDescription) && !itemDescription.equalsIgnoreCase(getReturnStr())) {
            getIp().invalidDescription();
            itemDescription = input.nextLine();
        }
        return itemDescription;
    }

    // ==== THE END ====

    // ==== VIEW WISHLIST ====

    private void viewWishlist() {
        List<String> wishlist = getUm().wishlistByUserId(getCurrentUserId());
        getIp().getListInfo(getIm().giveListItem(wishlist));
        if (!wishlist.isEmpty()) {
            String currentItemId = inputOption(wishlist);
            if (currentItemId.equalsIgnoreCase(getReturnStr())) return;
            wishlistOption(currentItemId);
        }
    }

    private void wishlistOption(String currentItemId) {
        getIp().removeItemWishlistMenu();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1":
                getUm().removeFromWishlist(getCurrentUserId(), currentItemId);
                getIp().removeFromWishlistMessage();
                break;
            default:
                getIp().invalidInputMessage();
                wishlistOption(currentItemId);
                break;
        }
    }

    private void addToWishlist(String itemId, String userId){
        if (getUm().addToWishlist(userId, itemId)) {
            getIp().addToWishlistMessage();
        } else {
            getIp().failedToAddMessage();
        }
    }

    private boolean isGuest(){
        if (getCurrentUserId().equalsIgnoreCase("@Guest")){
            up.needToLogin();
            return true;
        }
        return false;
    }

    // ==== THE END ====

}
