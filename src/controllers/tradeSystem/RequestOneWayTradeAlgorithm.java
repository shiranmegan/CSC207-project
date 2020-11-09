package controllers.tradeSystem;

import entities.Item;
import entities.ItemStatus;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RequestOneWayTradeAlgorithm is an algorithm for one way request trade
 *
 * w
 */
public class RequestOneWayTradeAlgorithm extends RequestTradeAlgorithm {

    RequestOneWayTradeAlgorithm(String userId, TradeManager tm, ItemManager im, UserManager um,
                                List<String> allValidUsernames) {
        super(userId, tm, im, um, allValidUsernames);
    }

    /**
     * Find potential trade with username for one way trade
     * @return list of info (userId and list of itemIds)
     */
    @Override
    public List<List<String>> helperTradeUsername() {
        List<List<String>> result = new ArrayList<>();
        String userId2 = getUsername();
        List<String> usernameList = new ArrayList<>();
        usernameList.add(userId2);
        result.add(usernameList);

        if (!userId2.equals(getReturnStr())) {
            String itemId2 = getItem(userId2);
            List<String> itemIds = new ArrayList<>();
            itemIds.add(itemId2);
            result.add(itemIds);
        }
        return result;
    }

    /**
     * Find potential trade with itemId for one way trade
     * @return list of info (userId and list of itemIds)
     */
    @Override
    public List<List<String>> helperTradeItem() {
        List<List<String>> result = new ArrayList<>();
        List<String> resultItemList = new ArrayList<>();
        List<String> resultUserIdList = new ArrayList<>();
        List<String> itemIds = getAllAvailableItems();
        String itemId2 = inputOption(itemIds);
        while (!itemId2.equalsIgnoreCase(getReturnStr()) && getTm().haveRequested(itemId2, getUserId())) {
            getTp().getHaveRequested();
            itemId2 = inputOption(itemIds);
        }

        if (itemId2.equalsIgnoreCase(getReturnStr())) {
            resultItemList.add(getReturnStr());
        } else {
            resultItemList.add(itemId2);
            resultUserIdList.add(getUm().findUserByItemInventory(itemId2));
        }
        result.add(resultUserIdList);
        result.add(resultItemList);
        return result;
    }

    /**
     * Find potential trade by relying on recommendation for one way trade
     * @return list of recommendation trade (which consists of list of info (userId, list of itemIds, and duration)
     */
    @Override
    public List<List<List<String>>> recommendTrade() {
        Map<String, Integer> potentialItemIds = new ConcurrentHashMap<>();
        List<List<List<String>>> recommendedTrade = new ArrayList<>();
        List<String> domainAvailableItems = getAllAvailableItems();
        for (String itemId: domainAvailableItems) {
            potentialItemIds.put(itemId, 1);
        }

        recommendWishlist(potentialItemIds);
        recommendRecentItems(potentialItemIds);
        recommendTopTraders(potentialItemIds);

        List<String> recommendedItems = new ArrayList<>();
        List<String> basicItems = new ArrayList<>();
        helperChoosingTopRecommendation(recommendedItems, basicItems, potentialItemIds);

        updaterRecommendedTrade(recommendedTrade, recommendedItems, basicItems);
        return recommendedTrade;
    }

    // Fill in the recommendedTrade while its length is less than numRecommend
    private void updaterRecommendedTrade(List<List<List<String>>> recommendedTrade, List<String> recommendedItems,
                                         List<String> basicItems) {
        addRecommendedItems(recommendedTrade, recommendedItems);
        addBasicItems(recommendedTrade, basicItems);
    }

    // Packaged the potential recommended trade into owner, list of itemId, and duration, then add it to
    // recommendedTrade
    private void addRecommendedItems(List<List<List<String>>> recommendedTrade, List<String> recommendedItems) {
        while (recommendedTrade.size() < getNumRecommend() && !recommendedItems.isEmpty()) {
            String itemId = recommendedItems.get(0);
            String owner = getUm().findUserByItemInventory(itemId);

            List<List<String>> infoPackage1 = helperAddItems(itemId, owner, "Permanent");
            List<List<String>> infoPackage2 = helperAddItems(itemId, owner, "Temporary");

            recommendedTrade.add(infoPackage1);
            recommendedTrade.add(infoPackage2);
            recommendedItems.remove(0);
        }
    }

    // Packaged the potential basic trade into owner, list of itemId, and duration, however, the addition is randomized,
    // then add it to recommended Trade
    private void addBasicItems(List<List<List<String>>> recommendedTrade, List<String> basicItems) {
        while (recommendedTrade.size() < getNumRecommend() && !basicItems.isEmpty()) {
            Random rand = new Random();
            int randomInt = rand.nextInt(basicItems.size());
            String itemId = basicItems.get(randomInt);
            String owner = getUm().findUserByItemInventory(itemId);

            List<List<String>> infoPackage1 = helperAddItems(itemId, owner, "Permanent");
            List<List<String>> infoPackage2 = helperAddItems(itemId, owner, "Temporary");

            recommendedTrade.add(infoPackage1);
            recommendedTrade.add(infoPackage2);
            basicItems.remove(randomInt);
        }
    }

    // Helper to package the info
    private List<List<String>> helperAddItems(String itemId, String owner, String duration) {
        List<List<String>> infoPackage = new ArrayList<>();
        List<String> itemIds = new ArrayList<>();
        List<String> owners = new ArrayList<>();
        List<String> durationList = new ArrayList<>();

        itemIds.add(itemId);
        owners.add(owner);
        if (duration.equalsIgnoreCase("Permanent")) {
            durationList.add("-1");
        } else {
            durationList.add("30");
        }

        infoPackage.add(owners);
        infoPackage.add(itemIds);
        infoPackage.add(durationList);

        return infoPackage;
    }

    // The itemId will be scored +5 based on wishlist
    private void recommendWishlist(Map<String, Integer> potentialItemIds) {
        List<String> wishlist = getUm().wishlistByUserId(getUserId());
        for (String itemId: wishlist) {
            if (potentialItemIds.containsKey(itemId)) {
                int scoreWishlist = 5;
                potentialItemIds.put(itemId, potentialItemIds.get(itemId) + scoreWishlist);
            }
        }
    }

    /**
     * Recommend recent items based on borrowing trade
     * @param potentialItemIds based on recent borrow trades
     */
    protected void recommendRecentItems(Map<String, Integer> potentialItemIds) {
        List<String> recentItems = getTm().getRecentItemsByUserId("Borrow", getUserId(), getMaxNum());
        for (String itemId: recentItems) {
            if (potentialItemIds.containsKey(itemId)) {
                // The itemId will be scored +3 based on recently borrowed items
                int scoreRecentItems = 3;
                potentialItemIds.put(itemId, potentialItemIds.get(itemId) + scoreRecentItems);
            }
        }
    }

    // The itemId will be scored +3 if owned by top traders (with the userId)
    private void recommendTopTraders(Map<String, Integer> potentialItemIds) {
        List<String> topTraders = getTm().getTopTradersByUserId(getUserId(), getMaxNum());
        List<String> allValidIds = new ArrayList<>();
        for (String username: getAllValidUsernames()) {
            allValidIds.add(getUm().userIdByUsername(username));
        }
        topTraders.removeIf(trader -> !allValidIds.contains(trader));
        for (String trader: topTraders) {
            for (String itemId: getUm().inventoryByUserId(trader)) {
                if (potentialItemIds.containsKey(itemId)) {
                    int scoreTopTraders = 3;
                    potentialItemIds.put(itemId, potentialItemIds.get(itemId) + scoreTopTraders);
                }
            }
        }
    }

    // Only available items from valid usernames
    private List<String> getAllAvailableItems() {
        List<String> eligibleUsernames = getAllValidUsernames();
        List<Item> items = new ArrayList<>();
        List<String> itemIds = new ArrayList<>();
        helperAvailableItems(eligibleUsernames, items, itemIds);
        return itemIds;
    }

    // Adding items to itemIds and item only when the item is Available and have not been requested
    private void helperAvailableItems(List<String> eligibleUsernames, List<Item> items, List<String> itemIds) {
        for (String username: eligibleUsernames) {
            String userId = getUm().userIdByUsername(username);
            for (String itemId: getUm().inventoryByUserId(userId)) {
                if (getIm().getItemStatus(itemId).equals(ItemStatus.AVAILABLE) &&
                        !getTm().haveRequested(itemId, getUserId())) {
                    items.add(getIm().findItem(itemId));
                    itemIds.add(itemId);
                }
            }
        }
    }
}
