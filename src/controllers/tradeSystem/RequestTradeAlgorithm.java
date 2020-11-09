package controllers.tradeSystem;

import controllers.MenuSelector;
import entities.*;
import presenters.TradePresenter;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RequestTradeAlgorithm is a class containing algorithms to request a trade (except for determining trade duration)
 *
 * userId is the userId that wants to make a trade
 * allValidUsernames is usernames that are available to do a trade
 * tm is the TradeManager
 * im is the ItemManager
 * um is the UserManager
 * numPrinted is number of recommendation offered
 */
public abstract class RequestTradeAlgorithm extends MenuSelector {

    private String userId;
    private List<String> allValidUsernames;
    private TradePresenter tp = new TradePresenter();
    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private final int numRecommend = 10;

    RequestTradeAlgorithm(String userId, TradeManager tm, ItemManager im, UserManager um,
                          List<String> allValidUsernames) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.userId = userId;
        this.allValidUsernames = allValidUsernames;
    }

    /**
     * Get allValidUsernames
     * @return list of usernames
     */
    protected List<String> getAllValidUsernames() {
        return this.allValidUsernames;
    }

    /**
     * Get userId
     * @return userId
     */
    protected String getUserId() {
        return this.userId;
    }

    /**
     * Get TradePresenter
     * @return tp
     */
    protected TradePresenter getTp() {
        return this.tp;
    }

    /**
     * Get TradeManager
     * @return tm
     */
    protected TradeManager getTm() {
        return this.tm;
    }

    /**
     * Get ItemManager
     * @return im
     */
    protected ItemManager getIm() {
        return this.im;
    }

    /**
     * Get UserManager
     * @return um
     */
    protected UserManager getUm() {
        return this.um;
    }

    /**
     * Get numRecommend
     * @return numRecommend
     */
    protected int getNumRecommend() {
        return this.numRecommend;
    }

    /**
     * Find potential trade with username
     * @return list of info (userId and list of itemIds)
     */
    public abstract List<List<String>> helperTradeUsername();

    /**
     * Find potential trade with itemId
     * @return list of info (userId and list of itemIds)
     */
    public abstract List<List<String>> helperTradeItem();

    /**
     * Find potential trade by relying on recommendation
     * @return list of recommendation trade (which consists of list of info (userId, list of itemIds, and duration)
     */
    public abstract List<List<List<String>>> recommendTrade();

    /**
     * Get username to be traded with
     * @return username
     */
    protected String getUsername() {
        List<String> usernames = getAllValidUsernames();
        tp.getAllUsername(usernames);
        if (!usernames.isEmpty()) {
            String username2 = inputOption(usernames);
            if (username2.equals(getReturnStr())) {
                return getReturnStr();
            } else {
                return checkerForUsername(username2);
            }
        } else {
            return getReturnStr();
        }
    }

    private String checkerForUsername(String username2) {
        String userId2 = um.userIdByUsername(username2);
        List<String> itemList = helperGetAvailableItemForUserId(userId2);
        if (haveRequestedAllItem(itemList)) {
            tp.noAvailableItemNotification("this user's");
            return getUsername();
        } else {
            return userId2;
        }
    }

    /**
     * Get item to be traded with
     * @param userId userId that owns the inventory
     * @return the itemId
     */
    protected String getItem(String userId) {
        List<String> itemList = helperGetAvailableItemForUserId(userId);
        List<Item> itemObjectList = new ArrayList<>();
        for (String itemId: itemList) {
            itemObjectList.add(im.findItem(itemId));
        }

        tp.getItemListInfo(userId, this.userId, itemObjectList);
        String itemId2 = inputOption(itemList);

        if (!itemId2.equals(getReturnStr()) && tm.haveRequested(itemId2, userId)) {
            tp.getHaveRequested();
            return getItem(userId);
        } else {
            return itemId2;
        }
    }

    /**
     * Get available items from a userId
     * @param userId userId to be checked
     * @return list of itemIds that are available
     */
    protected List<String> helperGetAvailableItemForUserId(String userId){
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

    /**
     * Checks whether any of the list contains returnStr
     * @param infoListList list that is being checked
     * @return true iff contains returnStr
     */
    protected boolean containReturnStr(List<List<String>> infoListList) {
        for (List<String> al: infoListList) {
            if (al.contains(getReturnStr())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add and sorts returnAl with recommended itemIds in addition to adding ordinary itemIds to randomAl
     * @param returnAl the actual recommended itemIds
     * @param randomAl ordinary itemIds that are going to be randomized
     * @param potentialItemIds Map of itemIds and scoring
     */
    protected void helperChoosingTopRecommendation(List<String> returnAl, List<String> randomAl,
                                                 Map<String, Integer> potentialItemIds) {
        for (Map.Entry<String, Integer> item : potentialItemIds.entrySet()) {
            if (item.getValue() == 1) {
                randomAl.add(item.getKey());
                potentialItemIds.remove(item.getKey());
            }
        }

        while (!potentialItemIds.isEmpty()) {
            String rememberItemId = null;
            int rememberMaxTimes = 0;
            for (Map.Entry<String, Integer> item : potentialItemIds.entrySet()) {
                if (rememberMaxTimes < item.getValue()) {
                    rememberMaxTimes = item.getValue();
                    rememberItemId = item.getKey();
                }
            }
            returnAl.add(rememberItemId);
            potentialItemIds.remove(rememberItemId);
        }
    }

}
