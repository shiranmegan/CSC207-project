package controllers.tradeSystem;

import controllers.MenuSelector;
import entities.UserStatus;
import gateways.*;
import presenters.TradePresenter;
import usecases.*;
import entities.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

/**
 * DoActionTradeHelper is a helper class for TradeSystem to do actions for Trade
 *
 * tm is the TradeManager
 * im is the ItemManager
 * um is the UserManager
 * mm is the MeetingManager
 * tp is the presenter for Trade
 * userId is the current user's userId
 * tradeIdInput is the chosen tradeId to be managed
 * tg is the gateway for Trade
 * ig is the gateway for Item
 * ug is the gateway for User
 * mg is the gateway for Meeting
 */
public class DoActionTradeHelper extends MenuSelector {

    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private MeetingManager mm;
    private TradePresenter tp = new TradePresenter();
    private String userId;
    private String tradeIdInput;
    private TradeGateway tg = new TradeGateway();
    private ItemGateway ig = new ItemGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();

    DoActionTradeHelper(String userId, TradeManager tm, ItemManager im, UserManager um, MeetingManager mm) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.mm = mm;
        this.userId = userId;
    }

    void helperActionToTrade(List<String> listTrade) {
        // Do action to existing Trade
        getTradeListInfo(listTrade);

        this.tradeIdInput = inputOption(listTrade);
        // Options of accept, reject, confirm a Trade
        if (!this.tradeIdInput.equals(getReturnStr())) {
            if (tm.eligibleToAcceptRejectTrade(tradeIdInput, this.userId)) {
                helperSwitchAcceptRejectTrade();
            } else if (eligibleToEditConfirm()) {
                helperEditConfirmSwitch(tm.getMeetingIdByTradeId(tradeIdInput));
            } else if (tm.eligibleToConfirmTrade(tradeIdInput, this.userId)) {
                helperSwitchConfirmTrade();
            } else {
                tp.noTradeActionAvailable();
            }
        }
        updateFile();
    }

    private boolean eligibleToEditConfirm() {
        if (mm.isNoMeeting(tm.getMeetingIdByTradeId(tradeIdInput))) {
            return false;
        } else {
            // Whether it's user's turn to edit meeting
            return mm.usersTurn(tm.getMeetingIdByTradeId(tradeIdInput), tm.getUserNum(tradeIdInput, this.userId));
        }
    }

    private void helperSwitchConfirmTrade() {
        tp.getSwitchConfirmOrMeetingNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // This confirms Trade
                helperConfirmTrade();
                break;
            default:
                tp.invalidSelection();
                helperSwitchConfirmTrade();
        }
    }

    private void helperSwitchAcceptRejectTrade() {
        tp.getAcceptRejectNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;

            case "1": // This is to accept Trade, but can't accept if too many incomplete trades and trades in one week
                if (!validIncompleteTrade(this.userId)) {
                    tp.violateIncompleteTrade();
                } else if (!tm.checkTradeLimit(this.userId)) {
                    tp.violateTradeLimit();
                } else {
                    helperInitiateMeeting();
                }
                break;
            case "2": // Reject trade
                tm.rejectTrade(this.tradeIdInput);
                if (!(tm.getItemIdsByTradeId(this.tradeIdInput).size() == 1)) {
                    im.makeItemAvailable(tm.getItemIdsByTradeId(this.tradeIdInput).get(1));
                }
                tp.successfulActionTrade("rejected");
                break;

            default:
                tp.invalidSelection();
                helperSwitchAcceptRejectTrade();
        }
    }

    private void helperInitiateMeeting() {
        String place = helperPromptPlace();
        if (!place.equals(getReturnStr())) {
            LocalDate dateTime = helperPromptTime();
            if (!dateTime.equals(LocalDate.now().minusDays(1))) {
                tm.setMeetingIdByTradeId(this.tradeIdInput, mm.createNewMeeting(dateTime, place));
                for (String itemId : tm.getItemIdsByTradeId(this.tradeIdInput)) {
                    im.makeItemUnavailable(itemId);
                }
                tm.acceptTrade(this.tradeIdInput);
                tp.successfulInitiateMeeting(dateTime, place);
                tp.successfulActionTrade("accepted");
            }
        }
    }

    private void helperConfirmTrade() {
        tp.getConfirmNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();

        switch (selection) {
            case "Y":
            case "y":
                if (String.valueOf(tm.getTradeStatusByTradeId(this.tradeIdInput)).equals("CONFIRMED")) {
                    tm.confirmTrade(this.tradeIdInput, this.userId);
                } else {
                    helperCompleteTrade();
                }
                tp.successfulActionTrade("confirmed");
                break;
            case "N":
            case "n":
                break;
            default:
                tp.invalidSelection();
                helperConfirmTrade();
                break;
        }
    }

    private void helperCompleteTrade() {
        helperRemoveFromWishlist(this.tradeIdInput);
        helperSwapInventories(this.tradeIdInput);
        tm.completeTrade(this.tradeIdInput);
        if (tm.getDurationByTradeId(this.tradeIdInput) > 0) {
            // if temporary trade, creates new returning trade
            String newTradeId = tm.createReverseTrade(this.tradeIdInput);

            int dur = tm.getDurationByTradeId(this.tradeIdInput);
            String oldMeetingId = tm.getMeetingIdByTradeId(this.tradeIdInput);
            LocalDate newTime = mm.getTimeByMeetingId(oldMeetingId).plusDays(dur);
            String place = mm.getPlaceByMeetingId(oldMeetingId);
            tm.setMeetingIdByTradeId(newTradeId, mm.createNewMeeting(newTime, place));

        } else {
            // if permanent trade
            for (String itemIds: tm.getItemIdsByTradeId(this.tradeIdInput)) {
                im.makeItemAvailable(itemIds);
            }
        }
    }

    private void helperSwapInventories(String tradeId) {
        String receiverId = tm.getReceiverByTradeId(tradeId);
        String requesterId = tm.getRequesterByTradeId(tradeId);
        List<String> itemIds = tm.getItemIdsByTradeId(tradeId);
        if (itemIds.size() == 1) {
            um.addToInventory(requesterId, itemIds.get(0));
            um.removeFromInventory(receiverId, itemIds.get(0));
        } else {
            um.addToInventory(requesterId, itemIds.get(0));
            um.removeFromInventory(receiverId, itemIds.get(0));

            um.addToInventory(receiverId, itemIds.get(1));
            um.removeFromInventory(requesterId, itemIds.get(1));
        }
    }

    private void helperRemoveFromWishlist(String tradeId) {
        // remove items from both users wishlist (depending on the type of trade)
        String itemId = tm.getItemIdsByTradeId(tradeId).get(0);
        String requester = tm.getRequesterByTradeId(tradeId);
        if (um.itemIdInWishlist(requester, itemId)) {
            um.removeFromWishlist(requester, itemId);
        }
        if (tm.getItemIdsByTradeId(tradeId).size() == 2) {
            String itemId2 = tm.getItemIdsByTradeId(tradeId).get(1);
            String receiver = tm.getReceiverByTradeId(tradeId);
            if (um.itemIdInWishlist(receiver, itemId2)) {
                um.removeFromWishlist(receiver, itemId2);
            }
        }
    }
    
    private boolean validIncompleteTrade(String userId) {
        if (!tm.checkIncompleteTrade(userId)) {
            um.changeStatus(this.userId, UserStatus.FLAGGED);
            return false;
        } else {
            return true;
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
            durationList.add(String.valueOf(tm.getDurationByTradeId(tradeId)) + " days");
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

    private void helperEditConfirmSwitch(String meetingId) {
        tp.getMeetingNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;

            case "1": // Edit meeting
                helperEditMeeting(meetingId);
                break;

            case "2": // Confirm meeting
                helperConfirmMeeting(meetingId);
                tp.getConfirmMeetingNotification();
                break;

            default:
                tp.invalidSelection();
                helperEditConfirmSwitch(meetingId);
        }
    }

    private void helperEditMeeting(String meetingId) {
        String place = helperPromptPlace();
        if (place.equals(getReturnStr())) {
            helperEditConfirmSwitch(meetingId);
        } else {
            LocalDate dateTime = helperPromptTime();
            if (dateTime.equals(LocalDate.now().minusDays(1))) {
                helperEditConfirmSwitch(meetingId);
            } else {
                proceedEditMeeting(meetingId, place, dateTime);
            }
        }
    }

    private void proceedEditMeeting(String meetingId, String place, LocalDate dateTime) {
        if (mm.editTimePlace(meetingId, dateTime, place, tm.getUserNum(this.tradeIdInput, this.userId))) {
            // if successfully edit the meeting
            mm.confirmTimePlace(meetingId, tm.getUserNum(this.tradeIdInput, this.userId));
            tp.successfulInitiateMeeting(dateTime, place);
        } else {
            tm.cancelTrade(this.tradeIdInput);
            tp.meetingCancelledNotification();

            for (String itemId : tm.getItemIdsByTradeId(this.tradeIdInput)) {
                im.makeItemAvailable(itemId);
            }

            if (tradeIdInput.startsWith("@return")) {
                // Both users get flagged if do not manage to return the borrowed/lend items
                um.changeStatus(tm.getReceiverByTradeId(this.tradeIdInput), UserStatus.FLAGGED);
                um.changeStatus(tm.getRequesterByTradeId(this.tradeIdInput), UserStatus.FLAGGED);
            }
        }
    }

    private String helperPromptPlace() {
        Scanner input = new Scanner(System.in);

        tp.promptMeetingPlace();
        String place = input.nextLine();

        while (containsInvalidString(place)) {
            tp.invalidItemName();
            place = input.nextLine();
        }
        if (place.equals(getReturnStr())) {
            return getReturnStr();
        } else {
            return place;
        }
    }

    private LocalDate helperPromptTime() {
        if (tradeIdInput.startsWith("@return")) {
            // Returning meeting time cannot be edited
            return mm.getTimeByMeetingId((tm.getMeetingIdByTradeId(tradeIdInput)));
        }
        else {
            Scanner input = new Scanner(System.in);
            tp.promptMeetingTime();
            String dateStr = input.nextLine();

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateTime = LocalDate.parse(dateStr, formatter);
                if (dateTime.isAfter(LocalDate.now())) {
                    return dateTime;
                } else {
                    tp.invalidDate();
                    return helperPromptTime();
                }
            } catch (DateTimeParseException e) {
                if (dateStr.equals(getReturnStr())) {
                    return LocalDate.now().minusDays(1);
                }
                tp.invalidInputMessage();
                return helperPromptTime();
            }
        }
    }

    private void helperConfirmMeeting(String meetingId) {
        mm.confirmTimePlace(meetingId, tm.getUserNum(this.tradeIdInput, this.userId));
        if (mm.isMeetingIdConfirmed(meetingId)) {
            tm.meetingConfirmed(this.tradeIdInput);
        }
    }

    private void updateFile() {
        ug.updateUserInfo(um);
        ig.updateItemInfo(im);
        tg.updateTradeInfo(tm);
        mg.updateMeetingInfo(mm);
    }
}
