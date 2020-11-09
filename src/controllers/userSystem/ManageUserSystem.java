package controllers.userSystem;

import controllers.IController;
import entities.Account;
import entities.ItemStatus;
import entities.UserStatus;
import presenters.AdminPresenter;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ManageUserSystem is to manager Users (meant for Admin)
 *
 * ap is the presenter for Admin
 */

public class ManageUserSystem extends UserSystem implements IController {
    private final AdminPresenter ap = new AdminPresenter();

    /**
     * Constructor of the ManagerUserSystem
     * @param userId the adminId
     * @param um the UserManager
     * @param im the ItemManager
     * @param tm the TradeManager
     */
    public ManageUserSystem(String userId, UserManager um, ItemManager im, TradeManager tm) {
        super(userId, um, im, tm);
    }

    /**
     * Show the specific menu for managing User
     * @param selection input from generalMenu
     */
    @Override
    public void specificMenu(String selection) {
        switch (selection) {
            case "2": //" Manage Flagged Users"
                ap.generalUserMessage("Flagged");
                viewUserByStatus(UserStatus.FLAGGED);
                break;
            case "3": //" Manage Frozen Users"
                ap.generalUserMessage("Frozen");
                viewUserByStatus(UserStatus.FROZEN);
                break;
            case "4": //" Manage Unfreeze Requests"
                ap.generalUserMessage("Requested");
                viewUserByStatus(UserStatus.REQUESTED);
                break;
            default:
                getIp().invalidInputMessage();
                break;
        }
    }

    /**
     * Present the main menu for the admin main menu
     */
    @Override
    public void presentMainMenu(){
        ap.adminManageUserMenu();
    }

    /**
     * Prompt the select Item options
     * @param s Options of what to be done
     * @param userId the userId to be managed
     * @param itemId the itemId to be managed
     */
    @Override
    public void selectItemOption(String s, String userId, String itemId) {
        if (s.equalsIgnoreCase("inventory")) {
            getIp().removeItemInventoryMenu();
        } else {
            getIp().removeItemWishlistMenu();
        }
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1":
                if (s.equalsIgnoreCase("wishlist")) {
                    getUm().removeFromWishlist(userId, itemId);
                    getIp().removalSuccessMessage();
                } else {
                    removeItemFromInventory(userId, itemId);
                }
                break;
            default:
                getIp().invalidInputMessage();
                selectItemOption(s, userId, itemId);
                break;
        }
    }

    private void viewUserByStatus(UserStatus status) {
        List<String> users = getUm().userIdsByStatus(status);

        List<Account> al = new ArrayList<>();
        for (String userIds: users) {
            al.add(getUm().findAccountById(userIds));
        }
        getUp().getListInfo(al);

        if(!al.isEmpty()) {
            String userId = inputOption(users);

            helperForViewUserByStatus(status, userId);
        }
    }

    private void helperForViewUserByStatus(UserStatus status, String userId) {
        if (!userId.equals(getReturnStr())) {
            if (status.equals(UserStatus.FLAGGED) || status.equals(UserStatus.REQUESTED)) {
                requestedUserHelper(userId);
            } else if (status.equals(UserStatus.FROZEN)) {
                unfreezeUserHelper(userId);
            }
        }
    }

    private void removeItemFromInventory(String userId, String itemId){
        if (getIm().getItemStatus(itemId) != ItemStatus.UNAVAILABLE) {
            getTm().rejectAllTradeWithItemId(userId, itemId);
            getUm().removeFromInventory(userId, itemId);
            getIm().removeItemFromSystem(itemId);
            getIp().removalSuccessMessage();
        }
        else {
            getIp().removalFailMessage();
        }
    }

    private void requestedUserHelper(String userId) {
        ap.requestedUser();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1":
                getUm().changeStatus(userId, UserStatus.FROZEN);
                break;
            case "2":
                getUm().changeStatus(userId, UserStatus.GOOD);
                break;
            default:
                ap.invalidInputMessage();
                requestedUserHelper(userId);
        }
    }

    private void unfreezeUserHelper(String userId) {
        ap.unfreezeUser();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1":
                getUm().changeStatus(userId, UserStatus.GOOD);
                break;
            default:
                ap.invalidInputMessage();
                unfreezeUserHelper(userId);
        }
    }
}
