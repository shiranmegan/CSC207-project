package presenters;

import entities.Account;

import java.util.List;

/**
 * A presenter that presents the functionalities of AccountSystem
 */
public class UserPresenter implements IPresenter {

    // ==== Common presenter method ====

    /**
     * Prints chooseOptionPrompt
     */
    @Override
    public void selectOptionPrompt() {
        System.out.println("Please select an option by entering the corresponding number!");
    }

    /**
     * Prints invalidInputMessage
     */
    @Override
    public void invalidInputMessage() {
        System.out.println("The input you entered is invalid, please try again!");
    }


    /**
     * Prints go back Message
     */
    @Override
    public void goBackToUpperMenu() {
        System.out.println("Please enter 0 to go back to upper level menu!");
    }
    // === End ===


    // ==== main menu and 3 direct Menu presenter methods ====

    /**
     * Prints the admin main menu
     */
    public void mainMenuOptions() {
        userMainMenuMessage();
        selectOptionPrompt();
        System.out.println("1. Manage your profile");
        System.out.println("2. View other user accounts");
        System.out.println("3. Manage items");
        System.out.println("4. Manage your trades");
        System.out.println("Please enter 0 to log out!");;
    }

    /**
     * Prints userMenuOptions
     */
    public void userManageProfile() {
        System.out.println("== Manage Profile ==");
        selectOptionPrompt();
        System.out.println("1. View my profile");
        System.out.println("2. Change my username");
        System.out.println("3. Change my password");
        System.out.println("4. Change my city address");
        System.out.println("5. Change my email address");
        System.out.println("6. Change my status");
        System.out.println("7. Request to be unfrozen");
        goBackToUpperMenu();
    }


    /**
     * Prints viewUsersMenuOptions
     */
    public void userViewerMenu(){
        System.out.println("== View User Account Menu ==");
        selectOptionPrompt();
        System.out.println("1. View all users");
        goBackToUpperMenu();
    }

    /**
     * Presents prompts for the main menu in the UserItemSystem.
     */
    public void UserItemMainMenu() {
        System.out.println("== Manage Items ==");
        selectOptionPrompt();
        System.out.println("1. View all items");
        System.out.println("2. Upload new item to inventory");
        System.out.println("3. View your inventory");
        System.out.println("4. View your wishlist");
        goBackToUpperMenu();
    }

    // ==== End ====


    // ==== sub methods ====
    /**
     * Prints the general message for user to enter
     */
    public void generalPromptMessage(String prompt) {
        System.out.println("Please enter your " + prompt);
        goBackToUpperMenu();
    }


    /**
     * Prints the user login title
     */
    public void userMainMenuMessage() {
        System.out.println("== User Main Menu ==");
    }

    /**
     * Prints unfreezeRequestSentMessage
     */
    public void unfreezeRequestSentMessage() {
        System.out.println("You have successfully sent a request to be unfrozen to the team of administrators. If an " +
                "administrator deems that your request is reasonable, your account status will be changed backed to " +
                "good-standing.");
    }

    /**
     * Prints unfreezeRequestAlreadySentMessage
     */
    public void unfreezeRequestAlreadySentMessage() {
        System.out.println("Your request to be unfrozen has already been sent. Please be patient, and if an " +
                "administrator deems that your request is reasonable, your account status will be changed backed to " +
                "good-standing.");
    }

    /**
     * Prompt option information of Y or N to defer decision
     */
    public void promptYesNo() {
        System.out.println("Please type Y to confirm and N to defer your decision");
    }

    /**
     * Prompt the warning message
     */
    public void onGoingTradeWarningMessage() {
        System.out.println("Warning! You have an on-going trade that may require you to do some actions!");
    }

    /**
     * Prints the status already change into
     * @param status the status of the user
     */
    public void changeStatusMessage(String status) {
        System.out.println("You have successfully changed your status into " + status);
    }

    /**
     * Prints the successful change on object
     * @param object any object available
     */
    public void generalSuccessfulMessage(String object) {
        System.out.println("You have successfully changed your " + object);
    }

    /**
     * Menu for user's option to view
     */
    public void singleUserOptions() {
        selectOptionPrompt();
        System.out.println("1. View user inventory.");
        System.out.println("2. View user wishlist.");
        goBackToUpperMenu();
    }

    /**
     * Notification that user need to login
     */
    public void needToLogin() {
        System.out.println("You need to login as a user in order to select this option!");
    }

    /**
     * Print view information
     * @param listInfo a list of object that contains information
     * @param <T> any kind of object
     */
    public <T> void getListInfo(List<T> listInfo) {
        if (listInfo.isEmpty()) {
            System.out.println("Sorry! There is nothing to view!");
        } else {
            System.out.println("Please select a user to be viewed by entering the corresponding number!");
            int i = 1;
            for (T info: listInfo) {
                System.out.println(i + ". " + info);
                i++;
            }
            goBackToUpperMenu();
        }
    }

    /**
     * Prints all user prompt
     * @param listUser the list of users
     */
    public void getAllUsers(List<Account> listUser) {
        System.out.println("== All Users ==");
        getListInfo(listUser);
    }

    /**
     * Print that account information
     * @param account the entity account
     */
    public void viewProfile(Account account) {
        System.out.println(account);
    }

    /**
     * Prints sorry message that option is unavailable for you
     */
    public void unavailableOption() {
        System.out.println("Sorry, this option is unavailable for you!");
    }

    // ==== End ====


}
