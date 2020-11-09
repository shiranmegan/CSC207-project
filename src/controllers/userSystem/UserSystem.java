package controllers.userSystem;

import controllers.IController;
import controllers.MenuSelector;
import controllers.SystemBuilder;
import presenters.ItemPresenter;
import presenters.UserPresenter;
import usecases.*;
import gateways.*;

import java.util.List;
import java.util.Scanner;

/**
 * UserSystem is a System for do Actions to User
 *
 * up is the User Presenter
 * ip is the Item Presenter
 * sb is to build Systems
 * um is UserManager
 * im is ItemManager
 * tm is TradeManager
 * ug is the Gateway for User
 * ig is the Gateway for Item
 * tg is the Gateway for Trade
 * accountId is the Account ID
 */
public abstract class UserSystem extends MenuSelector implements IController {
    private final UserPresenter up = new UserPresenter();
    private final ItemPresenter ip = new ItemPresenter();
    private final SystemBuilder sb;

    private UserManager um;
    private ItemManager im;
    private TradeManager tm;
    private final UserGateway ug = new UserGateway();
    private final ItemGateway ig = new ItemGateway();
    private final TradeGateway tg = new TradeGateway();

    private final String accountId;

    /**
     * Constructor of the UserSystem
     * @param accountId the account ID accessing
     * @param um the UserManager
     * @param im the ItemManager
     * @param tm the TradeManager
     */
    public UserSystem(String accountId, UserManager um, ItemManager im, TradeManager tm) {
        this.um = um;
        this.im = im;
        this.tm = tm;
        this.accountId = accountId;
        this.sb = new SystemBuilder(accountId);
    }

    /**
     * Run the UserSystem
     */
    @Override
    public void run() {
        boolean systemOpened = true;
        do {
            readFile();
            presentMainMenu();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0": // "0. Main Menu"
                    systemOpened = false;
                    break;
                case "1": // "1. View All User
                    viewAllUsers();
                    break;
                default:
                    specificMenu(selection);
                    break;
            }
            updateFile();
        } while (systemOpened);
    }

    /**
     * Which MainMenu to be presented
     */
    public abstract void presentMainMenu();

    /**
     * Specified Menu
     * @param selection the input from user
     */
    public abstract void specificMenu(String selection);

    private void viewAllUsers() {
        List<String> allUserIds = um.getAccountIdList();
        up.getAllUsers(um.getAccountList());
        if (!allUserIds.isEmpty()) {
            String currentUserId = inputOption(allUserIds);
            if (!currentUserId.equalsIgnoreCase(getReturnStr())) {
                viewUserOptions(currentUserId);
            }
        }
    }

    private void viewUserOptions(String userId) {
        boolean menuReturns = false;
        do {
            up.singleUserOptions();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    menuReturns = true;
                    break;
                case "1"://view inventory
                    viewInventory(userId);
                    break;
                case "2"://view wishlist
                    viewWishlist(userId);
                    break;
                default:
                    getIp().invalidInputMessage();
                    break;
            }
        } while (!menuReturns);
    }

    /**
     * View the Inventory of the userId
     * @param userId the userId to be viewed
     */
    protected void viewInventory(String userId) {
        List<String> inventoryIds = um.inventoryByUserId(userId);
        ip.getListInfo(im.giveListItem(inventoryIds));
        if (!inventoryIds.isEmpty()){
            String itemId = inputOption(inventoryIds);
            if (!itemId.equalsIgnoreCase(getReturnStr())) {
                selectItemOption("inventory", userId, itemId);
            }
        }
    }

    /**
     * View the Wishlist of the userId
     * @param userId the userId to be viewed
     */
    protected void viewWishlist(String userId) {
        List<String> wishlistIds = um.wishlistByUserId(userId);
        ip.getListInfo(im.giveListItem(wishlistIds));
        if (!wishlistIds.isEmpty()){
            String itemId = inputOption(wishlistIds);
            if (!itemId.equalsIgnoreCase(getReturnStr())) {
                selectItemOption("wishlist", userId, itemId);
            }
        }
    }

    /**
     * Show different options to manage userId and itemId
     * @param s the option for which to select
     * @param userId the userId to be managed
     * @param itemId the itemId to be managed
     */
    public abstract void selectItemOption(String s, String userId, String itemId);

    /**
     * Get User Presenter
     * @return up
     */
    protected UserPresenter getUp() {
        return this.up;
    }

    /**
     * Get Item Presenter
     * @return ip
     */
    protected ItemPresenter getIp() {
        return this.ip;
    }

    /**
     * Get User Manager
     * @return um
     */
    protected UserManager getUm() {
        return this.um;
    }

    /**
     * Get Item Manager
     * @return im
     */
    protected ItemManager getIm() {
        return this.im;
    }

    /**
     * Get Trade Manager
     * @return tm
     */
    protected TradeManager getTm() {
        return this.tm;
    }

    /**
     * Get Account ID
     * @return accountId
     */
    protected String getAccountId() {
        return this.accountId;
    }

    private void updateFile() {
        ug.updateUserInfo(um);
        ig.updateItemInfo(im);
        tg.updateTradeInfo(tm);

    }

    private void readFile() {
        um = sb.buildUserManager();
        im = sb.buildItemManager();
        tm = sb.buildTradeManager();
    }
}

