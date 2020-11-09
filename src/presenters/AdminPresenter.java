package presenters;

import entities.Account;
import java.util.List;

/**
 * A presenter that displays the functionalities of AdminSystem
 */
public class AdminPresenter implements IPresenter {

    // ==== Common presenter method ====

    /**
     * Prints the select option prompt
     */
    @Override
    public void selectOptionPrompt() {
        System.out.println("Please select an option by entering the corresponding number: ");
    }

    /**
     * Prints the invalid input message
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
        System.out.println("Please enter 0 to go back to upper level menu");
    }

    // ==== End ====

    // ==== mainMenu and 5 direct Menu presenter methods ====

    /**
     * Prints the admin main menu
     */
    public void mainMenuOptions() {
        adminMainMenuMessage();
        selectOptionPrompt();
        System.out.println("1. Manage your profile");
        System.out.println("2. Manage other user accounts");
        System.out.println("3. Manage other admin accounts");
        System.out.println("4. Manage thresholds");
        System.out.println("5. Manage items in the system");
        goBackToUpperMenu();
    }

    /**
     * Prints the AdminManageProfile main menu
     */
    public void adminManageProfileMenu() {
        generalMenuMessage("Manage profile");
        selectOptionPrompt();
        System.out.println("1. View my profile");
        System.out.println("2. Change my username");
        System.out.println("3. Change my password");
        goBackToUpperMenu();
    }

    /**
     * Prints the manage user menu
     */
    public void adminManageUserMenu() {
        generalMenuMessage("Manage user accounts");
        selectOptionPrompt();
        System.out.println("1. View users");
        System.out.println("2. Manage flagged users");
        System.out.println("3. Manage frozen users");
        System.out.println("4. Manage unfreeze requests");
        System.out.println("Please enter 0 to log out!");;
    }

    /**
     * Prints the manage admin menu
     */
    public void adminManageAdminMenu() {
        generalMenuMessage("Manage Admin Accounts");
        selectOptionPrompt();
        System.out.println("1. View admin");
        System.out.println("2. Add admin");
        goBackToUpperMenu();
    }

    /**
     * Prints the manage threshold menu
     */
    public void adminManageThresholdMenu() {
        generalMenuMessage("Manage Thresholds");
        selectOptionPrompt();
        System.out.println("1. Edit meeting cancellation threshold");
        System.out.println("2. Edit weekly trade limit");
        System.out.println("3. Edit borrowed-lent difference threshold");
        System.out.println("4. Edit meeting edits threshold");
        System.out.println("5. Edit minimum password length");
        goBackToUpperMenu();
    }

    /**
     * Prints the manage items menu
     */
    public void adminManageItemMenu() {
        generalMenuMessage("Manage Items");
        selectOptionPrompt();
        System.out.println("1. Manage items");
        System.out.println("2. Manage item requests");
        goBackToUpperMenu();
    }
    // ==== End ====

    // ==== sub methods ====
    /**
     * Prints the admin login title
     */
    public void adminMainMenuMessage() {
        System.out.println("== Admin Main Menu ==");
    }

    /**
     * Prints the Manage profile title
     */
    public void generalMenuMessage(String s) {
        System.out.println("== " + s + " Menu ==");
    }


    /**
     * Prints the Manage profile title
     */
    public void generalUserMessage(String s) {
        System.out.println("== " + s + " User ==");
    }

    /**
     * Prints the update successful message
     */
    public void updateSuccessMessage() {
        System.out.println("Update successful! \n");
    }


    /**
     * Prints the new password prompt
     */
    public void newPasswordPrompt() {
        System.out.println("Please enter a new password.");
        goBackToUpperMenu();
    }

    /**
     * Prints the invalid password message/prompt
     */
    public void invalidNewPasswordMessage() {
        System.out.println("This password is too short or contains invalid" +
                " strings, please try again, or enter 0 to go back.");
    }

    /**
     * Prints the change username prompt
     */
    public void invalidNewUsernameMessage() {
        System.out.println("This username is occupied or contains invalid strings, please try again, or enter 0 to " +
                "go back.");
    }

    /**
     * Prints the change username prompt
     */
    public void newUsernamePrompt() {
        System.out.println("Please enter a new username.");
        goBackToUpperMenu();
    }

    /**
     * Prints the registration termination prompt
     */
    public void registrationTerminates() {
        System.out.println("Registration terminates.");
    }

    /**
     * Prints the successful registration prompt
     */
    public void registrationSuccess(){
        System.out.println("This Admin is successfully registered.");
    }

    /**
     * Prints the threshold edit prompt
     *
     * @param thresholdName the name of the threshold that is being updated
     */
    public void thresholdEditPrompt(String thresholdName) {
        System.out.println("Please enter new " + thresholdName + ", or 0 to cancel: ");
    }

    /**
     * Prints the threshold update success message
     */
    public void thresholdEditSuccessMessage() {
        System.out.println("Threshold is updated successfully!");
    }

    /**
     * Prints the Unfreeze the user
     */
    public void unfreezeUser(){
        System.out.println("1. Unfreeze this user");
    }

    /**
     * Prints the option of freeze/unfreeze the user
     */
    public void requestedUser() {
        selectOptionPrompt();
        System.out.println("1. Freeze this user");
        System.out.println("2. Unfreeze this user");
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
            int i = 1;
            for (T info: listInfo) {
                System.out.println(i + ". " + info);
                i++;
            }
        }
    }

    /**
     * Get list of admin information
     * @param listAccount list of account
     */
    public void getAdminsInfo(List<Account> listAccount) {
        System.out.println("== Admin Accounts ==");
        getListInfo(listAccount);
    }
    // ==== End ====

}