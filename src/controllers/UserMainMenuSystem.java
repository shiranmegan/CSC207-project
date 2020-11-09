package controllers;

import presenters.UserPresenter;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * UserMainMenuSystem is the Main Menu for User
 *
 * up is the UserPresenter
 */
public class UserMainMenuSystem extends AccountMainMenuSystem implements IController {
    private final UserPresenter up = new UserPresenter();

    /**
     * Constructor of the UserMainMenuSystem
     * @param userId userId
     */
    public UserMainMenuSystem(String userId) {
        super(userId);
    }

    /**
     * Run the UserMainMenuSystem
     * @throws FileNotFoundException when file is not found
     */
    @Override
    public void run() throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        boolean systemOpened = true;
        do {
            up.mainMenuOptions();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    systemOpened = false;
                    break;
                case "1": //view my profile
                    getSb().buildSystem("ManageUserProfile").run();
                    break;
                case "2": //view other user account
                    getSb().buildSystem("UserViewerSystem").run();
                    break;
                case "3": //Manage Items
                    getSb().buildSystem("UserItemSystem").run();
                    break;
                case "4": //Manage trade
                    getSb().buildSystem("TradeSystem").run();
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (systemOpened);
    }
}
