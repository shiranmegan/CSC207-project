package controllers.ItemSystem;

import controllers.IController;
import entities.ItemStatus;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.*;

/**
 * ManageItemSystem is to manage Items
 */
public class ManageItemSystem extends ItemSystem implements IController {

    /**
     * Constructor of the manageItemSystem
     * @param userId the adminId
     * @param um the UserManager
     * @param im the ItemManager
     * @param tm the TradeManager
     */
    public ManageItemSystem(String userId, UserManager um, ItemManager im, TradeManager tm) {
        super(userId, um, im, tm);
    }

    /**
     * Present the main menu of the admin manage item system
     */
    @Override
    public void presentMainMenu(){
        getAp().adminManageItemMenu();
    }

    /**
     * The specified menu for managing item
     * @param selection the input from the user
     */
    @Override
    public void specificMenu(String selection) {
        if ("2".equals(selection)) { // check requested item
            requestedItemInput();
        } else {
            getIp().invalidInputMessage();
        }
    }

    /**
     * The options for the doing actions to particular currentItemId
     * @param currentItemId the itemId to be viewed for action options
     */
    @Override
    public void viewItemsOption(String currentItemId) {
        getIp().removeItemMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            case "0":
                break;
            case "1":
                removeItemFromInventory(currentItemId);
                break;
            default:
                getIp().invalidInputMessage();
                viewItemsOption(currentItemId);
                break;
        }
    }

    private void requestedItemInput() {
        List<String> currentListOfItems = getIm().getIdsByStatus(ItemStatus.UNCHECKED);

        sendInfoToPresenter(currentListOfItems);
        if (!currentListOfItems.isEmpty()) {
            String currentItemId = inputOption(currentListOfItems);
            if (!currentItemId.equals(getReturnStr())) {
                requestedItemOption(currentItemId);
            }
        }
    }

    private void requestedItemOption(String currentItemId) {
        getIp().requestedItemMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            case "0":
                break;
            case "1":
                removeItemFromInventory(currentItemId);
                break;
            case "2":
                approveItem(currentItemId);
                break;
            default:
                getIp().invalidInputMessage();
                requestedItemOption(currentItemId);
                break;
        }
    }

    private void approveItem(String currentItemId) {
        getIm().makeItemAvailable(currentItemId);
    }

}
