package presenters;
import entities.Item;
import entities.Meeting;
import entities.Trade;

import java.time.*;
import java.util.*;

public class TradePresenter implements IPresenter {

    // ==== Common presenter method ====

    /**
     * Prints chooseOptionPrompt
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

    /**
     * Message when the user made an invalid input
     */
    @Override
    public void invalidInputMessage() {
        System.out.println("The input you entered is invalid, please try again!");
    }

    // === End ===

    // ==== main menu and 3 direct Menu presenter methods ====

    /**
     * User will see different menus based on their status
     *
     * @param s string representation of status
     */
    public void getSelectionMenu(String s) {
        System.out.println("== Manage Trades ==");
        selectOptionPrompt();
        System.out.println("1. Display all your trades");
        System.out.println("2. Display recent items traded or common traders");
        if (!s.equalsIgnoreCase("Bad")) {
            System.out.println("3. Request for a trade");
            System.out.println("4. Manage current trades ");
        }
        goBackToUpperMenu();
    }

    /**
     * List all types of trades that can be selected by the user
     */
    public void getAllTypeOptions() {
        System.out.println("== Display Trades ==");
        selectOptionPrompt();
        System.out.println("1. Borrowing trades from another user");
        System.out.println("2. Lending trades to another user");
        System.out.println("3. All trades");
        System.out.println("4. Two way trades (borrow and lend simultaneously)"); // Two-Way Trade
        System.out.println("5. Requested");
        System.out.println("6. Accepted");
        System.out.println("7. Rejected");
        System.out.println("8. Cancelled");
        System.out.println("9. Confirmed");
        System.out.println("10. Completed");
        goBackToUpperMenu();
    }

    /**
     * Menu option to choose which type of information to display
     */
    public void getRecentSelectionNotification() {
        System.out.println("== Display Recent Items/Top Traders ==");
        System.out.println("What kind of information do you want to display?");
        selectOptionPrompt();
        System.out.println("1. Recent items for borrow trade");
        System.out.println("2. Recent items for lend trade");
        System.out.println("3. Recent items for borrow with lend trade");
        System.out.println("4. Top traders you traded with");
        goBackToUpperMenu();
    }

    /**
     * Prints the request trade notification
     */
    public void getRequestTradeNotification() {
        System.out.println("== Trade Request ==");
        System.out.println("Which type of trades do you want to make?");
        selectOptionPrompt();
        System.out.println("1. One-way-trade" + "\n" + "2. Two-way-trade");
        goBackToUpperMenu();
    }


    // === End ===

    // ==== sub methods ====

    /**
     * Message before displaying a specified type of trade
     *
     * @param s string representation of trade type
     */
    public void getTypeTradeNotification(String s) {
        System.out.println("Below are the trades of type: " + s);
    }

    /**
     * Display a string representation of all items in the itemList
     *
     * @param itemList a list of Item
     */
    public void getItemListInfo(String userId1, String userId2, List<Item> itemList) {
        System.out.println("== List of Items ==");
        if (itemList.isEmpty()) {
            System.out.println("No items can be listed");
        } else {
            if (userId1.equals(userId2)) {
                System.out.println("Please enter the number option to choose the item from your inventory that you " +
                        "would like to lend: ");
            } else {
                System.out.println("Please enter the number option to choose the item that you would like to borrow: ");
            }

            getListInfo(itemList);
            goBackToUpperMenu();
        }
    }

    public <T> void getListInfo(List<T> infoList) {
        int i = 1;
        for (T info : infoList) {
            System.out.println(i + ". " + info);
            i++;
        }
        if (infoList.isEmpty()) {
            System.out.println("Sorry! There is nothing to be viewed!");
        }
    }

    /**
     * Display recommended items to be borrowed
     * @param itemList a list of Item
     */
    public void getItemRecommendation(List<Item> itemList) {
        System.out.println("== Item Recommendation ==");
        if (itemList.isEmpty()) {
            System.out.println("No recommendation can be made");
        } else {
            System.out.println("Here are some items that you may want to lend to the user: ");
            getListInfo(itemList);
        }
    }

    /**
     * Display a string representation of all trades in the tradeList
     *
     * @param tradeList a list of Trade
     */
    public void getTradeListInfo(List<Trade> tradeList, List<String> duration, List<List<String>> usernameList,
                                 List<List<String>> itemNameList) {
        System.out.println("== List of Trades ==");
        if (tradeList.isEmpty()) {
            System.out.println("No trades available");
        } else {
            selectOptionPrompt();
            int i = 1;
            for (int j = 0; j < tradeList.size(); j++) {
                System.out.println(i + ". " + tradeList.get(j));
                System.out.println("Duration: " + duration.get(j));
                System.out.println("Requested by: " + usernameList.get(j).get(0));
                System.out.println("Received by: " + usernameList.get(j).get(1));
                StringBuilder itemNames = new StringBuilder();
                for (String itemName : itemNameList.get(j)) {
                    itemNames.append(itemName).append(", ");
                }
                String slicedItemNames = itemNames.substring(0, itemNames.length() - 2);
                System.out.println("Items being traded: " + slicedItemNames);
                i++;
            }
            goBackToUpperMenu();
        }
    }

    /**
     * Display all username in usernameList
     *
     * @param usernameList a list of string representing username
     */
    public void getAllUsername(List<String> usernameList) {
        System.out.println("== List of Usernames ==");
        if (usernameList.isEmpty()) {
            System.out.println("No usernames can be listed");
        } else {
            System.out.println("Please enter the number option to choose which username that you would like to trade "
                    + "with:");
            getListInfo(usernameList);
            goBackToUpperMenu();
        }
    }

    /**
     * Prints the duration notification
     */
    public void getDurationNotification() {
        System.out.println("Please enter the duration(how many days) for the trade: ");
        goBackToUpperMenu();
    }

    /**
     * Prompt which duration type of the trade notification
     */
    public void getDurationTypeTradeNotification() {
        System.out.println("Which trade duration that you would like to choose?");
        selectOptionPrompt();
        System.out.println("1. Permanent" + "\n" + "2. Temporary");
        goBackToUpperMenu();
    }

    /**
     * Menu option to accept the request or reject the request
     */
    public void getAcceptRejectNotification() {
        selectOptionPrompt();
        System.out.println("1. Accept the requested trade" + "\n2. Reject the requested trade");
        goBackToUpperMenu();
    }

    /**
     * Prompt the options of how the user like to make the trade
     */
    public void chooseTrade() {
        System.out.println("How would you like to make your trade?");
        selectOptionPrompt();
        System.out.println("1. Search by username");
        System.out.println("2. Search by items");
        System.out.println("3. Recommend me a quick trade");
        goBackToUpperMenu();
    }

    /**
     * Menu option to confirm the trade or view user's meeting
     */
    public void getSwitchConfirmOrMeetingNotification() {
        selectOptionPrompt();
        System.out.println("1. Confirm the trade");
        goBackToUpperMenu();
    }

    /**
     * Menu option to confirm trade or defer user's decision
     */
    public void getConfirmNotification() {
        System.out.println("Please enter Y to confirm trade or N to defer your decision");
    }

    /**
     * Message when successfully requested a trade
     *
     * @param s string representing type of trade
     */
    public void successfulRequestTradeMade(String s) {
        System.out.println("You have successfully requested a " + s + " trade");
    }

    /**
     * Prints the recommendation information of the trade
     * @param usernames the list of all usernames in the system
     * @param items the list of all items in the system
     * @param durations the duration of the trade
     */
    public void tradeRecommendationInformation(List<String> usernames, List<List<Item>> items, List<String> durations) {
        System.out.println("== Trade Recommendation ==");
        System.out.println("Here is/are some trade recommendations that you may want to make!");
        selectOptionPrompt();
        int i = 1;
        for (int j = 0; j < usernames.size(); j++) {
            System.out.println(i + ". "+ "Trader's username: " + usernames.get(j));
            System.out.println("\nItems to be traded: ");

            int k = 1;
            for (Item item: items.get(j)) {
                System.out.println(k + ". " + item);
                k++;
            }
            if (durations.get(j).equalsIgnoreCase("-1")) {
                System.out.println("\nDuration: Permanent\n");
            } else {
                System.out.println("\nDuration: " + durations.get(j) + " days\n");
            }
            System.out.println("-----------------------");
            i++;
        }
        goBackToUpperMenu();
    }

    /**
     * Message when successfully complete an action to a trade
     *
     * @param s string representing trade action
     */
    public void successfulActionTrade(String s) {
        System.out.println("You have successfully " + s + " a trade");
    }

    /**
     * Message when successfully initiate a meeting
     *
     * @param time  the exact time the meeting will take place
     * @param place the place the meeting will happen
     */
    public void successfulInitiateMeeting(LocalDate time, String place) {
        System.out.println("You have successfully initiated a meeting at " + place + ", " + time);
    }

    /**
     * Message asking for number of traders the user want to view
     */
    public void promptNumForTopTraders() {
        System.out.println("Please enter the number of top traders that you want to view");
        goBackToUpperMenu();
    }

    /**
     * Message asking for number of recent items the user want to view
     *
     * @param s string representation of a number
     */
    public void promptNumRecentItems(String s) {
        System.out.println("Please enter the number of recent items " + s + "that you want to view");
        goBackToUpperMenu();
    }

    /**
     * Message when no trade action is available with the specified tradeID
     */
    public void noTradeActionAvailable() {
        System.out.println("No available trade actions can be made");
    }

    /**
     * Message when the user's borrowing exceeds lending by more than the threshold
     */
    public void violateBorrowDiff() {
        System.out.println("You have many more number of borrowing than number of lending");
    }

    /**
     * Message when the user has number of incomplete trade exceeding the threshold
     */
    public void violateIncompleteTrade() {
        System.out.println("You have too many cancelled trades");
    }

    /**
     * Message when the user has number of trade in a week exceeding the threshold
     */
    public void violateTradeLimit() {
        System.out.println("You have reached trade limit for this week");
    }

    /**
     * Message when the user made an invalid selection
     */
    public void invalidSelection() {
        System.out.println("Please enter the option according to the available options: ");
    }

    /**
     * Message when the user made an invalid meeting time
     */
    public void invalidDate() {
        System.out.println("Invalid date! You need to provide a meeting time that is after your current time");
    }

    /**
     * Message asking for meeting place
     */
    public void promptMeetingPlace() {
        System.out.println("Please enter the place you want to meet");
        goBackToUpperMenu();
    }

    /**
     * Message asking for meeting time
     */
    public void promptMeetingTime() {
        System.out.println("Please enter the time you want to meet in format of YYYY-MM-DD");
        goBackToUpperMenu();
    }

    /**
     * Menu option for meetings depending on the current status of user
     *
     */
    public void getMeetingNotification() {
        selectOptionPrompt();
        System.out.println("1. Edit your meetings");
        System.out.println("2. Confirm your meetings");
        goBackToUpperMenu();
    }

    /**
     * Menu option for display information about the meeting
     */
    public void viewMeetingNotification() {
        selectOptionPrompt();
        System.out.println("1. Display information about your meeting");
        goBackToUpperMenu();
    }

    /**
     * Prints notification of the meeting
     */
    public void noMeeting() {
        System.out.println("Meeting has not been made yet!");
    }

    /**
     * List the information of the given meeting
     *
     * @param meeting a Meeting
     */
    public void getMeetingInfo(Meeting meeting) {
        System.out.println("Here is the information of the meeting:");
        System.out.println(meeting);
    }

    /**
     * Message when a meeting is cancelled by the user
     */
    public void meetingCancelledNotification() {
        System.out.println("This meeting has been cancelled successfully!");
    }

    /**
     * Message when a meeting has been confirmed
     */
    public void getConfirmMeetingNotification(){
        System.out.println("This meeting has been confirmed successfully!");
    }

    /**
     * Message when the item is not in s inventory
     * @param s the user
     */
    public void noAvailableItemNotification(String s) {
        System.out.println("No available items in " + s + " inventory.");
    }

    /**
     * Message when you have requested the item previously
     */
    public void getHaveRequested() {
        System.out.println("You have requested to trade with this item previously!");
    }

    /**
     * Message when there is an invalid character in the input
     */
    public void invalidItemName() {
        System.out.println("The name you entered contains invalid characters, please try again.");
    }

    /**
     * Prints the sorry message of area not available
     */
    public void noUserAvailableForTrade() {
        System.out.println("Sorry! Currently, no user in your area is available to trade");
    }
    // === End ===
}