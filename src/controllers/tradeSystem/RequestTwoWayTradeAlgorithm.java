package controllers.tradeSystem;

import entities.Item;
import entities.ItemStatus;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RequestTwoWayTradeAlgorithm is an algorithm for a two way trade request
 */
public class RequestTwoWayTradeAlgorithm extends RequestOneWayTradeAlgorithm {

    RequestTwoWayTradeAlgorithm(String userId, TradeManager tm, ItemManager im, UserManager um,
                                List<String> allValidUsernames) {
        super(userId, tm, im, um, allValidUsernames);
    }

    /**
     * Find potential trade with username for two way trade
     * @return list of info (userId and list of itemIds)
     */
    @Override
    public List<List<String>> helperTradeUsername() {
        List<List<String>> result = super.helperTradeUsername();
        if (!containReturnStr(result)) {
            getTp().getItemRecommendation(helperRecommendLendItem(result.get(0).get(0)));
            String itemId1 = getItem(getUserId());
            result.get(1).add(itemId1);
        }
        return result;
    }

    /**
     * Find potential trade with itemId for two way trade
     * @return list of info (userId and list of itemIds)
     */
    @Override
    public List<List<String>> helperTradeItem() {
        List<List<String>> result = super.helperTradeItem();
        if (!containReturnStr(result)) {
            getTp().getItemRecommendation(helperRecommendLendItem(result.get(0).get(0)));
            String itemId1 = getItem(getUserId());
            result.get(1).add(itemId1);
        }
        return result;
    }

    /**
     * Find potential trade by relying on recommendation for two way trade
     * @return list of recommendation trade (which consists of list of info (userId, list of itemIds, and duration)
     */
    @Override
    public List<List<List<String>>> recommendTrade() {
        List<List<List<String>>> recommendedTrade = super.recommendTrade();
        updateRecommendedTradeBasedOnLend(recommendedTrade);
        return recommendedTrade;
    }

    // Add items from userIds inventory to the recommendedTrade to offer lending trade
    private void updateRecommendedTradeBasedOnLend(List<List<List<String>>> recommendedTrade) {
        Map<String, Integer> recommendedLendItem = new ConcurrentHashMap<>();
        List<String> inventory = getUm().inventoryByUserId(getUserId());
        for (String itemId: inventory) {
            if (getIm().getItemStatus(itemId).equals(ItemStatus.AVAILABLE)) {
                recommendedLendItem.put(itemId, 1);
            }
        }

        for (Item item: helperRecommendLendItem(getUserId())) {
            String itemId = getIm().getItemIdByItem(item);
            if (recommendedLendItem.containsKey(itemId)) {
                recommendedLendItem.put(itemId, recommendedLendItem.get(itemId) + 5);
            }
        }

        List<String> recommendedLendList = new ArrayList<>();
        List<String> basicLendItems = new ArrayList<>();
        helperChoosingTopRecommendation(recommendedLendList, basicLendItems, recommendedLendItem);
        updaterLendItem(recommendedTrade, recommendedLendList, basicLendItems);
    }

    // Recommend item based on the userId's wishlist
    private List<Item> helperRecommendLendItem(String userId) {
        List<Item> al = new ArrayList<>();
        List<String> usersInventory = helperGetAvailableItemForUserId(getUserId());
        for (String itemId : getUm().wishlistByUserId(userId)) {
            if (usersInventory.contains(itemId)) {
                al.add(getIm().findItem(itemId));
            }
        }
        return al;
    }

    private void updaterLendItem(List<List<List<String>>> recommendedTrade, List<String> recommendedLendList,
                                 List<String> basicLendItems) {

        List<List<List<String>>> originalCopy = makeDeepCopyRecommendedTrade(recommendedTrade);

        if (!recommendedLendList.isEmpty()) {
            addBasedOnRecommendedLend(recommendedTrade, recommendedLendList, basicLendItems, originalCopy);

        } else {
            addBasedOnBasicItem(recommendedTrade, basicLendItems, originalCopy);
        }
    }

    private void addBasedOnBasicItem(List<List<List<String>>> recommendedTrade,
                                     List<String> basicLendItems,
                                     List<List<List<String>>> originalCopy) {
        Random rand = new Random();
        int randomInt = rand.nextInt(basicLendItems.size());
        String itemId = basicLendItems.get(randomInt);
        for (List<List<String>> listListString : recommendedTrade) {
            listListString.get(1).add(itemId);
        }
        basicLendItems.remove(randomInt);

        removeUntilBasicEmpty(recommendedTrade, basicLendItems, originalCopy);
    }

    private void addBasedOnRecommendedLend(List<List<List<String>>> recommendedTrade,
                                           List<String> recommendedLendList, List<String> basicLendItems,
                                           List<List<List<String>>> originalCopy) {
        String itemId = recommendedLendList.get(0);
        for (List<List<String>> listListString : recommendedTrade) {
            listListString.get(1).add(itemId);
        }
        recommendedLendList.remove(0);


        removeUntilRecommendedEmpty(recommendedTrade, recommendedLendList, originalCopy);
        removeUntilBasicEmpty(recommendedTrade, basicLendItems, originalCopy);
    }

    private void removeUntilRecommendedEmpty(List<List<List<String>>> recommendedTrade,
                                             List<String> recommendedLendList, List<List<List<String>>> originalCopy) {
        while (recommendedTrade.size() < getNumRecommend() && !recommendedLendList.isEmpty()) {
            List<List<List<String>>> newCopy = makeDeepCopyRecommendedTrade(originalCopy);
            String itemId1 = recommendedLendList.get(0);
            for (List<List<String>> listListString : newCopy) {
                listListString.get(1).add(itemId1);
                recommendedTrade.add(listListString);
            }
            recommendedLendList.remove(0);
        }
    }

    private void removeUntilBasicEmpty(List<List<List<String>>> recommendedTrade,
                                       List<String> basicLendItems, List<List<List<String>>> originalCopy) {
        while (recommendedTrade.size() < getNumRecommend() && !basicLendItems.isEmpty()) {
            List<List<List<String>>> newCopy = makeDeepCopyRecommendedTrade(originalCopy);
            Random rand = new Random();
            int randomInt = rand.nextInt(basicLendItems.size());
            String itemId2 = basicLendItems.get(randomInt);
            for (List<List<String>> listListString : newCopy) {
                listListString.get(1).add(itemId2);
                recommendedTrade.add(listListString);
            }
            basicLendItems.remove(randomInt);
        }
    }

    private List<List<List<String>>> makeDeepCopyRecommendedTrade(List<List<List<String>>> recommendedTrade) {
        List<List<List<String>>> newCopyListListList = new ArrayList<>();
        for (List<List<String>> copyListList: recommendedTrade) {
            List<List<String>> newCopyListList = new ArrayList<>();
            for (List<String> copyList: copyListList) {
                List<String> newCopyList = new ArrayList<>(copyList);
                newCopyListList.add(newCopyList);
            }
            newCopyListListList.add(newCopyListList);
        }

        return newCopyListListList;
    }

    /**
     * Recommend recent items based on two way trades
     * @param potentialItemIds based on recent two way trades
     */
    @Override
    protected void recommendRecentItems(Map<String, Integer> potentialItemIds) {
        List<String> recentItems = getTm().getRecentItemsByUserId("TwoWay", getUserId(), getMaxNum());
        for (String itemId: recentItems) {
            if (potentialItemIds.containsKey(itemId)) {
                // The itemId will be scored +3 based on recently borrowed items
                int scoreRecentItems = 3;
                potentialItemIds.put(itemId, potentialItemIds.get(itemId) + scoreRecentItems);
            }
        }
    }

}
