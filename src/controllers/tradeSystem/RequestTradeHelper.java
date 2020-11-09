package controllers.tradeSystem;
import controllers.MenuSelector;
import entities.Item;
import entities.UserStatus;
import presenters.TradePresenter;
import usecases.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import gateways.*;
/**
 * RequestTradeHelper is a helper for requesting trades.
 * userId is the id of the User who is currently using the system.
 * TradePresenter is the Presenter for Trade.
 * TradeManager is the UseCase Class TradeManager.
 * ItemManager is the UseCase Class ItemManager.
 * UserManager is the UseCase Class UserManager.
 * ItemGateway is the Gateway Class for updating information of Item.
 * TradeGateway is the Gateway Class for updating information of Trade.
 * UserGateway is the Gateway Class for updating information of User.
 */
public class RequestTradeHelper extends MenuSelector {

    private String userId;
    private TradePresenter tp = new TradePresenter();
    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private MeetingManager mm;
    private TradeGateway tg = new TradeGateway();
    private ItemGateway ig = new ItemGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();
    private RequestTradeAlgorithm rta;

    RequestTradeHelper(String userId, TradeManager tm, ItemManager im, UserManager um, MeetingManager mm) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.mm = mm;
        this.userId = userId;
    }

    /**
     * The helper function for RequestTrade
     */
    void helperRequestTrade() {
        if (getAllValidUsernames().isEmpty()) {
            tp.noUserAvailableForTrade();
        } else {
            tp.getRequestTradeNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    break;

                case "1": // One Way (Borrow/Lend) Trade request
                    if (!validBorrowDiff(this.userId)) {
                        tp.violateBorrowDiff();
                        um.changeStatus(this.userId, UserStatus.FLAGGED);
                    } else {
                        switchRequestTradeHelper("OneWay");
                        helperChooseTrade();
                    }
                    break;

                case "2": // Two Way (Borrow and Lend) Trade request
                    if (helperGetAvailableItemForUserId(this.userId).isEmpty()) {
                        tp.noAvailableItemNotification("your");
                    } else {
                        switchRequestTradeHelper("TwoWay");
                        helperChooseTrade();
                    }
                    break;

                default:
                    tp.invalidSelection();
                    helperRequestTrade();
                    break;
            }
            updateFile();
        }
    }

    private void switchRequestTradeHelper(String whichWay) {
        if (whichWay.equalsIgnoreCase("OneWay")) {
            rta = new RequestOneWayTradeAlgorithm(userId, tm, im, um, getAllValidUsernames());
        } else {
            rta = new RequestTwoWayTradeAlgorithm(userId, tm, im, um, getAllValidUsernames());
        }
    }

    private void helperChooseTrade() {
        tp.chooseTrade();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // Based on username
                List<List<String>> infoUsername = rta.helperTradeUsername();
                if (!containReturnStr(infoUsername)) {
                    helperRequestTemPerItem(infoUsername.get(0).get(0), infoUsername.get(1));
                }
                break;
            case "2": // Based on items available
                List<List<String>> infoItem = rta.helperTradeItem();
                if (!containReturnStr(infoItem)) {
                    helperRequestTemPerItem(infoItem.get(0).get(0), infoItem.get(1));
                }
                break;
            case "3": // Quick recommended trade
                List<List<List<String>>> infoRecommend = rta.recommendTrade();
                recommendationTrade(infoRecommend);
                break;
            default:
                tp.invalidInputMessage();
                helperChooseTrade();
                break;
        }
    }

    private boolean containReturnStr(List<List<String>> infoListList) {
        for (List<String> al: infoListList) {
            if (al.contains(getReturnStr())) {
                return true;
            }
        }
        return false;
    }

    // The recommended trade to be made by user
    private void recommendationTrade(List<List<List<String>>> infoRecommend) {
        tradeRecommendationInformation(infoRecommend);
        int input = getIntInput(infoRecommend.size());
        if (input != 0) {
            List<List<String>> potentialTrade = infoRecommend.get(input - 1);
            String userIdWanted = potentialTrade.get(0).get(0);
            List<String> itemIdsWanted = potentialTrade.get(1);
            int duration = Integer.parseInt(potentialTrade.get(2).get(0));

            tm.requestTrade(this.userId, userIdWanted, itemIdsWanted, duration);
            if (itemIdsWanted.size() == 2) {
                im.makeItemUnavailable(itemIdsWanted.get(1));
            }
            tp.successfulRequestTradeMade("");
        }
    }

    // Send info about the recommended trade to presenter
    private void tradeRecommendationInformation(List<List<List<String>>> infoRecommend) {
        List<String> usernames = new ArrayList<>();
        List<List<Item>> items = new ArrayList<>();
        List<String> durations = new ArrayList<>();
        for (List<List<String>> listListString: infoRecommend) {
            usernames.add(um.usernameById(listListString.get(0).get(0)));
            List<Item> itemMiniList = new ArrayList<>();
            for (String itemId: listListString.get(1)) {
                itemMiniList.add(im.findItem(itemId));
            }
            items.add(itemMiniList);
            durations.add(listListString.get(2).get(0));
        }
        tp.tradeRecommendationInformation(usernames, items, durations);
    }

    // User's that are good, same cityAddress, own at least 1 available item that have not been requested by userId
    private List<String> getAllValidUsernames() {
        String[] sa = um.findPlaceByUserId(this.userId).split("-");
        String city = sa[0].trim();
        String country = sa[1].trim();
        List<String> userIdsSamePlace = um.userIdsSameCity(city, country);
        userIdsSamePlace.remove(this.userId);
        userIdsSamePlace.removeIf(userId -> !um.statusByUserId(userId).equals(UserStatus.GOOD));
        List<String> usernames = new ArrayList<>();
        for (String userId: userIdsSamePlace) {
            if (!haveRequestedAllItem(um.inventoryByUserId(userId)) && im.hasAvailableItems(um.inventoryByUserId(userId))) {
                usernames.add(um.usernameById(userId));
            }
        }
        return usernames;
    }

    private void helperRequestTemPerItem(String userId2, List<String> al1){
        tp.getDurationTypeTradeNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                helperRequestTrade();
                break;
            case "1": // Permanent trade
                helperPermanent(userId2, al1);
                break;
            case "2": // Temporary trade
                helperTemporary(userId2, al1);
                break;
            default:
                tp.invalidSelection();
                helperRequestTemPerItem(userId2, al1);
        }
    }

    private void helperPermanent(String userId2, List<String> al1) {
        tm.requestTrade(this.userId, userId2, al1, -1);
        tp.successfulRequestTradeMade("permanent");
    }

    private void helperTemporary(String userId2, List<String> al1) {
        tp.getDurationNotification();
        int duration = getIntInput(getMaxNum());
        if (duration == 0) {
            helperRequestTrade();
        } else {
            tm.requestTrade(this.userId, userId2, al1, duration);
            tp.successfulRequestTradeMade("temporary");
            if (al1.size() == 2) {
                im.makeItemUnavailable(al1.get(1));
            }
        }
    }

    private List<String> helperGetAvailableItemForUserId(String userId){
        List<String> itemIdList = um.inventoryByUserId(userId);
        List<String> itemList = new ArrayList<>();
        for (String ItemId: itemIdList){
            if (String.valueOf(im.getItemStatus(ItemId)).equals("AVAILABLE")){
                itemList.add(ItemId);
            }
        }
        return itemList;
    }

    private boolean haveRequestedAllItem(List<String> itemList) {
        for (String itemId: itemList){
            if (!tm.haveRequested(itemId, userId)) {
                return false;
            }
        }
        return true;
    }

    private boolean validBorrowDiff(String userId) {
        if (!tm.checkBorrowDiff(userId)) {
            um.changeStatus(this.userId, UserStatus.FLAGGED);
            return false;
        } else {
            return true;
        }
    }

    private void updateFile() {
        ug.updateUserInfo(um);
        ig.updateItemInfo(im);
        tg.updateTradeInfo(tm);
        mg.updateMeetingInfo(mm);
    }
}
