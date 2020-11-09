package presenters;

/**
 * The presenter that displays login and registration messages/prompts to the screen
 */
public class LoginPresenter implements IPresenter {

    // ==== Common presenter method====

    /**
     * Prints the invalid message message/prompt
     */
    @Override
    public void invalidInputMessage() {
        System.out.println("The input you entered is invalid, please try again!");
    }

    /**
     * Prints the select option prompt
     */
    @Override
    public void selectOptionPrompt() {
        System.out.println("Please select an option by entering the corresponding number: ");
    }

    /**
     * Menu option to go back to upper menu
     */
    @Override
    public void goBackToUpperMenu() {
        System.out.println("Please enter 0 to go back to upper level menu!");
    }

    // ==== End ====

    // ==== sub methods ====
    /**
     * Prints the option to login or register
     */
    public void mainMenuOptions() {
        welcomeMessage();
        selectOptionPrompt();
        System.out.println("1. Register");
        System.out.println("2. Login");
        quitPrompt();
    }

    /**
     * Prints the system welcome message
     */
    public void welcomeMessage() {
        System.out.println("Welcome to Group_0267's Trade System!");
    }

    /**
     * Prints register as a user option
     */
    public void registerOptions() {
        userRegistrationMessage();
        selectOptionPrompt();
        System.out.println("1. Register as a user");
        goBackToUpperMenu();
    }

    /**
     * Prints the login menu options
     */
    public void loginOptions() {
        selectOptionPrompt();
        System.out.println("1. Login as an admin");
        System.out.println("2. Login as a user");
        System.out.println("3. Login as a guest");
        goBackToUpperMenu();
    }

    /**
     * Menu option to quit the system
     */
    public void quitPrompt() {
        System.out.println("Enter 0 to quit!");
    }

    /**
     * Prints the system closing message
     */
    public void closingMessage() {
        System.out.println("Good bye! See you soon!");
    }

    /**
     * Prints the admin login title
     */
    public void generalLoginMessage(String s) {
        System.out.println("== " + s + " Login ==");
    }

    /**
     * Prints the username not found message
     */
    public void usernameNotFoundPrompt() {
        System.out.println("Username Not Found! Please enter another username!");
    }

    /**
     * Prints the user registration title
     */
    public void userRegistrationMessage() {
        System.out.println("== User Registration ==");
    }

    /**
     * Prints the new username prompt
     */
    public void newUsernamePrompt() {
        System.out.println("Please enter a new username: ");
        goBackToUpperMenu();
    }

    /**
     * Pritns the new password prompt
     *
     * @param length the minimum length of a password
     */
    public void newPasswordPrompt(int length) {
        System.out.println("Please enter new password ("+length+" or more characters): ");
        goBackToUpperMenu();
    }

    /**
     * Prints the invalid password message
     */
    public void invalidPasswordMessage() {
        System.out.println("Sorry, invalid Password! Please try again, or enter 0 to go back.");
    }

    /**
     * Prints successful registration
     */
    public void registrationResultMessage() {
       System.out.println("You have successfully registered!");
    }

    /**
     * Prints the username has already been taken prompt
     */
    public void usernameHasBeenTakenPrompt() {
        System.out.println("Username has already been selected. Please enter a new username: ");
    }

    /**
     * a general prompt message
     * @param prompt any possible prompt
     */
    public void generalPromptMessage(String prompt) {
        System.out.println("Please enter your " + prompt);
        goBackToUpperMenu();
    }
    // ==== End ====
}
