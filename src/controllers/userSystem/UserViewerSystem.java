package controllers.userSystem;

import controllers.IController;
import usecases.*;

import java.util.Scanner;

/**
 * UserViewerSystem is a menu to view users
 */
public class UserViewerSystem extends UserSystem implements IController {

    /**
     * Constructor for UserViewerSystem
     * @param userId the accountId
     * @param um UserManager
     * @param im ItemManager
     * @param tm TradeManager
     */
    public UserViewerSystem(String userId, UserManager um, ItemManager im, TradeManager tm) {
        super(userId, um, im, tm);
    }

    /**
     * Specific menu for user
     * @param selection input from generalMenu
     */
    @Override
    public void specificMenu(String selection) {
        getUp().invalidInputMessage();
    }

    /**
     * Prints user menu options
     */
    @Override
    public void presentMainMenu(){
        getUp().userViewerMenu();
    }

    /**
     * Select item options for itemId and userId based on s
     * @param s Options to be chosen
     * @param userId userId to be managed
     * @param itemId itemId to be managed
     */
    @Override
    public void selectItemOption(String s, String userId, String itemId){
        getIp().addItemWishlistMenu();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // Add to wishlist
                if (!isGuest()) {
                    if (getUm().addToWishlist(getAccountId(), itemId)) {
                        getIp().addToWishlistMessage();
                    } else {
                        getIp().failedToAddMessage();
                    }
                }
                break;
            default:
                getIp().invalidInputMessage();
                break;
        }
    }

    private boolean isGuest(){
        if (getAccountId().equalsIgnoreCase("@Guest")){
            getUp().needToLogin();
            return true;
        }
        return false;
    }

}

