package controllers.ItemSystem;

import controllers.IController;
import controllers.MenuSelector;
import controllers.SystemBuilder;
import entities.Item;
import entities.ItemStatus;
import gateways.ItemGateway;
import gateways.TradeGateway;
import gateways.UserGateway;
import presenters.AdminPresenter;
import presenters.ItemPresenter;
import presenters.UserPresenter;
import usecases.*;

import java.util.*;

/**
 * ItemSystem is a System to deal with Items
 *
 * ig is a gateway that reads and updates information of items
 * ug is a gateway that reads and updates information of users
 * ip is a presenter that is responsible for printing menu in this system
 * im ts the ItemManager responsible for keeping track of all items in the system
 * um is the UserManager responsible for keeping track of all users in the system
 * currentUserId is the userId of the User currently using the system
 */
public abstract class ItemSystem extends MenuSelector implements IController {
    private final ItemGateway ig = new ItemGateway();
    private final UserGateway ug = new UserGateway();
    private final TradeGateway tg = new TradeGateway();
    private final ItemPresenter ip = new ItemPresenter();
    private final AdminPresenter ap = new AdminPresenter();
    private final UserPresenter up = new UserPresenter();

    private final SystemBuilder sb;

    private ItemManager im;
    private UserManager um;
    private TradeManager tm;

    private final String currentUserId;

    /**
     * Constructor for ItemSystem
     * @param userId Id of the current user invoking ItemSystem
     * @param um UserManager
     */
    public ItemSystem(String userId, UserManager um, ItemManager im, TradeManager tm) {
        this.im = im;
        this.tm = tm;
        currentUserId = userId;
        this.um = um;
        this.sb = new SystemBuilder(userId);
    }

    /**
     * Run method for ItemSystem
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
                case "0":
                    systemOpened = false;
                    break;
                case "1": // view All items
                    viewItems();
                    break;
                default:
                    specificMenu(selection);
                    break;
            }
            updateFile();
        } while (systemOpened);
    }

    /**
     * Prints the correct main menu prompts
     */
    public abstract void presentMainMenu();

    private void viewItems() {
        List<String> allItemIds = im.getAllItems();
        allItemIds.removeIf(itemIds -> im.getItemStatus(itemIds).equals(ItemStatus.REMOVED));
        sendInfoToPresenter(allItemIds);
        String currentItemId = inputOption(allItemIds);
        if (!currentItemId.equalsIgnoreCase(getReturnStr())) {
            viewItemsOption(currentItemId);
        }
    }

    /**
     * View options available for currentItemId
     * @param currentItemId the itemId to be viewed for action options
     */
    public abstract void viewItemsOption(String currentItemId);

    /**
     * Additional specified menu
     * @param selection the input chosen from user
     */
    public abstract void specificMenu(String selection);

    // ==== GETTERS ====

    /**
     * Get ItemPresenter
     * @return ip
     */
    protected ItemPresenter getIp() {
        return this.ip;
    }

    /**
     * Get AdminPresenter
     * @return ap
     */
    protected AdminPresenter getAp() {
        return this.ap;
    }

    /**
     * Get UserPresenter
     * @return up
     */
    protected UserPresenter getUp() {
        return this.up;
    }

    /**
     * Get ItemManager
     * @return im
     */
    protected ItemManager getIm() {
        return this.im;
    }

    /**
     * Get UserManager
     * @return um
     */
    protected UserManager getUm() {
        return this.um;
    }

    /**
     * Get currentUserId
     * @return currentUserId
     */
    protected String getCurrentUserId() {
        return this.currentUserId;
    }

    // ==== END OF GETTERS ====

    /**
     * Remove itemId from inventory of corresponding User
     * @param itemId the itemId to be removed
     */
    protected void removeItemFromInventory(String itemId){
        String userId = um.findUserByItemInventory(itemId);
        if (!im.getItemStatus(itemId).equals(ItemStatus.UNAVAILABLE)) {
            tm.rejectAllTradeWithItemId(userId, itemId);
            um.removeFromInventory(userId, itemId);
            im.removeItemFromSystem(itemId);
            ip.removalSuccessMessage();
        }
        else {
            ip.removalFailMessage();
        }
    }

    /**
     * Sending the correct info about itemIds to be processed by presenter
     * @param listItemIds the list of itemIds to be printed
     */
    protected void sendInfoToPresenter(List<String> listItemIds) {
        List<Item> item = new ArrayList<>();
        List<String> owner = new ArrayList<>();
        for (String itemIds: listItemIds) {
            item.add(im.findItem(itemIds));
            owner.add(um.usernameById((um.findUserByItemInventory(itemIds))));
        }
        ip.getItemInfo(item, owner);
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

