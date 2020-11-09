package controllers.tradeSystem;
import controllers.MenuSelector;
import entities.Item;
import entities.Trade;
import presenters.*;
import usecases.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * DisplayTradeSystem is a helper class for TradeSystem to Display Trade information
 *
 * tm is the TradeManager
 * im is the ItemManager
 * um is the UserManager
 * mm is the MeetingManager
 * tp is the presenter for Trade
 * userId is the current user's userId
 */
public class DisplayTradeHelper extends MenuSelector {

    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private MeetingManager mm;
    private TradePresenter tp = new TradePresenter();
    private String userId;

    DisplayTradeHelper(String userId, TradeManager tm, ItemManager im, UserManager um, MeetingManager mm) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.mm = mm;
        this.userId = userId;
    }

    void helperDisplaySelection() {
        // Options for displaying Trades
        tp.getAllTypeOptions();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;

            case "1":
                // Borrow trades
                helperDisplayTrade( "Borrow",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "All", "Borrow"));
                break;

            case "2":
                // Lending trades
                helperDisplayTrade( "Lend",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "All", "Lend"));
                break;

            case "3":
                // All trades
                helperDisplayTrade( "All",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "All", "All"));
                break;

            case "4":
                // Borrowing and Lending trades
                helperDisplayTrade( "Borrow with Lend",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "All", "TwoWay"));
                break;

            case "5":
                // Requested trades
                helperDisplayTrade( "Requested",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "REQUESTED", "All"));
                break;

            case "6":
                // Accepted trades
                helperDisplayTrade( "Accepted",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "ACCEPTED", "All"));
                break;

            case "7":
                // Rejected trades
                helperDisplayTrade( "Rejected",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "REJECTED", "All"));
                break;

            case "8":
                // Cancelled trades
                helperDisplayTrade( "Cancelled",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "CANCELLED", "All"));
                break;

            case "9":
                // Meeting confirmed trades (doesn't include RequesterConfirmed/ReceiverConfirmed)
                helperDisplayTrade( "Confirmed",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "CONFIRMED", "All"));
                break;

            case "10":
                // Completed trades
                helperDisplayTrade("Completed",
                        tm.getTradeWithStatusAndTypeByUserId(this.userId, "COMPLETED", "All"));
                break;

            default:
                tp.invalidSelection();
                helperDisplaySelection();
        }
    }

    private void helperDisplayTrade(String presenterNotification, List<String> arrayList) {
        tp.getTypeTradeNotification(presenterNotification);
        getTradeListInfo(arrayList);
        if (!arrayList.isEmpty()){
            String tradeId = inputOption(arrayList);
            if (!tradeId.equals(getReturnStr())) {
                helperDisplayMeeting(tradeId);
            }
        }
    }

    private void helperDisplayMeeting(String tradeId) {
        String meetingId = tm.getMeetingIdByTradeId(tradeId);
        tp.viewMeetingNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // 1. View meeting
                if (mm.isNoMeeting(meetingId)) {
                    tp.noMeeting();
                } else {
                    tp.getMeetingInfo(mm.findMeeting(meetingId));
                }
                break;
            default:
                tp.invalidInputMessage();
                helperDisplayMeeting(tradeId);
                break;
        }
    }

    void helperDisplayRecent() {
        tp.getRecentSelectionNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;

            case "1":
                displayRecentBorrowItems();
                break;

            case "2":
                displayRecentLendItems();
                break;

            case "3":
                displayRecentTwoWayItems();
                break;

            case "4":
                displayTopTraders();
                break;

            default:
                tp.invalidSelection();
                helperDisplayRecent();
        }
    }

    private void displayRecentBorrowItems() {
        tp.promptNumRecentItems("for borrow trade");
        int num1 = getIntInput(getMaxNum());
        if (num1 != 0) {
            List<String> al1 = tm.getRecentItemsByUserId("Borrow", this.userId, num1);
            List<Item> itemList1 = new ArrayList<>();
            for (String itemId : al1) {
                itemList1.add(im.findItem(itemId));
            }
            tp.getListInfo(itemList1);
        }
    }

    private void displayRecentLendItems() {
        tp.promptNumRecentItems("for lend trade");
        int num2 = getIntInput(getMaxNum());
        if (num2 != 0) {
            List<String> al2 = tm.getRecentItemsByUserId("Lend", this.userId, num2);
            List<Item> itemList2 = new ArrayList<>();
            for (String itemId : al2) {
                itemList2.add(im.findItem(itemId));
            }
            tp.getListInfo(itemList2);
        }
    }

    private void displayRecentTwoWayItems() {
        tp.promptNumRecentItems("for borrow with lend trade");
        int num3 = getIntInput(getMaxNum());
        if (num3 != 0) {
            List<String> al3 = tm.getRecentItemsByUserId("TwoWay", this.userId, num3);
            List<Item> itemList3 = new ArrayList<>();
            for (String itemId : al3) {
                itemList3.add(im.findItem(itemId));
            }
            tp.getListInfo(itemList3);
        }
    }

    private void displayTopTraders() {
        tp.promptNumForTopTraders();
        int num4 = getIntInput(getMaxNum());
        if (num4 != 0) {
            List<String> al4 = tm.getTopTradersByUserId(this.userId, num4);
            List<String> usernameList = new ArrayList<>();
            for (String userId : al4) {
                usernameList.add(um.usernameById(userId));
            }
            tp.getListInfo(usernameList);
        }
    }
    
    private void getTradeListInfo(List<String> tradeIdList) {
        List<Trade> tradeList = new ArrayList<>();
        List<String> durationList = new ArrayList<>();
        List<List<String>> usernameList = new ArrayList<>();
        List<List<String>> itemNameList = new ArrayList<>();
        for (String tradeId: tradeIdList) {
            editVariousList(tradeList, durationList, usernameList, itemNameList, tradeId);
        }

        tp.getTradeListInfo(tradeList, durationList, usernameList, itemNameList);
    }

    // Parse the tradeId to send information that is printable for the Presenter (rather than printing Ids)
    private void editVariousList(List<Trade> tradeList, List<String> durationList, List<List<String>> usernameList,
                                 List<List<String>> itemNameList, String tradeId) {
        tradeList.add(tm.findTrade(tradeId));

        if (tm.getDurationByTradeId(tradeId) == -1) {
            durationList.add("Permanent");
        } else {
            durationList.add(tm.getDurationByTradeId(tradeId) + " days");
        }

        List<String> al1 = new ArrayList<>();
        al1.add(um.usernameById(tm.getRequesterByTradeId(tradeId)));
        al1.add(um.usernameById(tm.getReceiverByTradeId(tradeId)));
        usernameList.add(al1);

        List<String> al2 = new ArrayList<>();
        for (String itemId: tm.getItemIdsByTradeId(tradeId)) {
            al2.add(im.getItemNameByItemId(itemId));
        }
        itemNameList.add(al2);
    }
}