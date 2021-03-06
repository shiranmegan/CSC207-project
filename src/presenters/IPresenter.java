package presenters;

/**
 * An interface for all presenters
 */
public interface IPresenter {

    /**
     * Prints what to do to go back to the upper menu
     */
    void goBackToUpperMenu();

    /**
     * Prompts to select an option
     */
    void selectOptionPrompt();

    /**
     * Prints when invalid input has been made
     */
    void invalidInputMessage();

}
