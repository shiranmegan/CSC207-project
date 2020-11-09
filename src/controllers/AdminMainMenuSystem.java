package controllers;

import presenters.AdminPresenter;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * AdminMainMenuSystem is a main menu for Admin
 *
 * ap is the Admin Presenter
 */
public class AdminMainMenuSystem extends AccountMainMenuSystem implements IController {
    private final AdminPresenter ap = new AdminPresenter();

    /**
     * Constructor for the Admin Main Menu System
     * @param userId adminId
     */
    AdminMainMenuSystem(String userId) {
        super(userId);
    }

    /**
     * Run the admin main menu system
     * @throws FileNotFoundException when file is not found
     */
    @Override
    public void run() throws FileNotFoundException {
        boolean systemOpened = true;
        do {
            // Presenter
            ap.mainMenuOptions();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    systemOpened = false;
                    break;
                case "1": //Manage your Profile
                    getSb().buildSystem("ManageAdminProfile").run();
                    break;
                case "2": //Manage other Users
                    getSb().buildSystem("ManageUserSystem").run();
                    break;
                case "3": //Manager other Admins
                    getSb().buildSystem("ManageAdminSystem").run();
                    break;
                case "4": //Manage Threshold
                    getSb().buildSystem("ManageThreshold").run();
                    break;
                case "5": //Manage Items in the system
                    getSb().buildSystem("ManageItemSystem").run();
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        } while (systemOpened);
    }

}
