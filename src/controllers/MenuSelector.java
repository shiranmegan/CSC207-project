package controllers;

import presenters.ItemPresenter;

import java.util.*;
import java.util.Scanner;

/**
 * This is a class responsible for handling the menu when unknown number of objects are displayed
 * The input must have desired format for the class to run correctly
 * The input must match the output displayed for the client for the class to run correctly
 *
 * ip is the presenter
 * returnStr is an input to return to the upper menu
 * maxNum is the maximum int for an input
 * invalidStrings are set of Strings that are not allowed to be used for an input
 */
public class MenuSelector {
    private final ItemPresenter ip = new ItemPresenter();
    private final String returnStr = "0";
    private final int maxNum = 1000000;
    private final String[] invalidStrings = new String[] {",", "/", "-"};

    /**
     * This method is called when a list of objects corresponding to the list of ids get displayed for user to choose
     * This method will check if the input is valid; if yes, the currentId is set correspondingly; if not, a prompt
     * will continuously remind the user to enter a valid input
     * @param ids a list of ids representing the objects
     * @return the chosen id
     */
    public String inputOption(List<String> ids) {
        int num;
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        // check if the selection is within the range
        while (isBadInput(selection, ids.size())) {
            ip.invalidInputMessage();
            selection = input.nextLine();
        }
        num = Integer.parseInt(selection);
        // returns if the user want to go back
        if (num == 0) {
            return returnStr;
        } else {
            return ids.get(num - 1);
        }
    }

    private boolean isBadInput(String input, int numRange) {
        try {
            int selection = Integer.parseInt(input);
            if (selection < 0 || selection > numRange) {
                return true;
            }
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    /**
     * Get the returnStr
     * @return String returnStr
     */
    public String getReturnStr() {
        return this.returnStr;
    }

    /**
     * Get the maxNum
     * @return int maxNum
     */
    public int getMaxNum() {
        return this.maxNum;
    }

    /**
     * Checks whether there is invalid string
     * @param anyString String to be checked
     * @return true iff contains invalidString
     */
    public boolean containsInvalidString(String anyString) {
        for (String s: invalidStrings) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }

    /**
     * Get the integer input
     * @param numRange maximum integer to be inputted
     * @return int chosen by the user
     */
    public int getIntInput(int numRange) {
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        if (isBadInput(selection, numRange)) {
            ip.invalidInputMessage();
            return getIntInput(numRange);
        } else {
            return Integer.parseInt(selection);
        }
    }
}
