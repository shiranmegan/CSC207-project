package usecases;

import java.util.*;
import entities.*;

/**
 * TradeManager is a use case for Trade
 *
 * tih is a helper class
 * tvh is a helper class
 * cancelledThreshold is the threshold for incomplete transaction.
 * weeklyTradeLimit is the threshold for weekly Trade limit.
 * borrowDiff is the threshold for the difference between the number of borrowing and number of lending.
 * tradeIdToTrade is to store tradeId and Trade.
 */
public class TradeManager {

    private TradeValidatorHelper tvh = new TradeValidatorHelper();
    private TradeInfoHelper tih = new TradeInfoHelper();
    private int cancelledThreshold;
    private int weeklyTradeLimit;
    private int borrowDiff;
    private Map<String, Trade> tradeIdToTrade = new HashMap<>();

    /**
     * Creates a TradeManager with editThreshold, cancelledThreshold, openThreshold, weeklyTradeLimit, and borrowDiff
     * based on the tradeManagerList and stores the HashMap of tradeId to Trade from the database.
     * @param listThresholds List of thresholds from the ThresholdGateway
     * @param listTrade List of Trades
     */
    public TradeManager(List<Integer> listThresholds, List<Trade> listTrade) {
        this.cancelledThreshold = listThresholds.get(0);
        this.weeklyTradeLimit = listThresholds.get(1);
        this.borrowDiff = listThresholds.get(2);
        for (Trade trade: listTrade) {
            this.tradeIdToTrade.put(trade.getTradeId(), trade);
        }
    }

    /**
     * Getters for cancelledThreshold
     * @return an int of cancelledThreshold
     */
    public int getCancelledThreshold() {
        return this.cancelledThreshold;
    }

    /**
     * Getters for weeklyTradeLimit
     * @return an int of weeklyTradeLimit
     */
    public int getWeeklyTradeLimit() {
        return this.weeklyTradeLimit;
    }

    /**
     * Getters for borrowDiff
     * @return an int of borrowDiff
     */
    public int getBorrowDiff() {
        return this.borrowDiff;
    }

    /**
     * Getters for tradeIdToTrade
     * @return a Map of tradeId and Trade
     */
    public Map<String, Trade> getTradeIdToTrade() {
        return this.tradeIdToTrade;
    }

    /**
     * Setters for cancelledThreshold
     * @param cancelledThreshold new cancelledThreshold
     */
    public void setCancelledThreshold(int cancelledThreshold) {
        this.cancelledThreshold = cancelledThreshold;
    }

    /**
     * Setters for weeklyTradeLimit
     * @param weeklyTradeLimit new weeklyTradeLimit
     */
    public void setWeeklyTradeLimit(int weeklyTradeLimit) {
        this.weeklyTradeLimit = weeklyTradeLimit;
    }

    /**
     * Setters for borrowDiff
     * @param borrowDiff new borrowDiff
     */
    public void setBorrowDiff(int borrowDiff) {
        this.borrowDiff = borrowDiff;
    }

    /**
     * Add a tradeId and Trade to the Map of tradeIdToTrade
     * @param tradeId The trade ID
     * @param trade The Trade object corresponding from the trade ID
     */
    public void addTrade(String tradeId, Trade trade) {
        getTradeIdToTrade().put(tradeId, trade);
    }

    /**
     * Request (and created) a Trade and adding the new Trade into the tradeIdToTrade
     * @param userId1 The requester
     * @param userId2 The receiver
     * @param itemIds The List of item IDs to be traded
     * @param duration The duration of the Trade
     */
    public void requestTrade(String userId1, String userId2, List<String> itemIds, int duration) {
        String tradeId = createNewTrade(userId1, userId2, itemIds);
        findTrade(tradeId).setDuration(duration);
    }

    /**
     * Find a Trade based on the tradeId
     * @param tradeId the Trade ID
     * @return Trade object corresponding to the Trade ID
     */
    public Trade findTrade(String tradeId) {
        return getTradeIdToTrade().get(tradeId);
    }

    /**
     * Rejects a Trade based on tradeId. Sets the status of the Trade to REJECTED
     * @param tradeId the Trade ID
     */
    public void rejectTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.REJECTED);
    }

    /**
     * Accepts a Trade based on tradeId. Sets the status of the Trade to ACCEPTED
     * @param tradeId the Trade ID
     */
    public void acceptTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.ACCEPTED);
    }

    /**
     * The meeting for the Trade is confirmed based on tradeId. Sets the status of the Trade to CONFIRMED
     * @param tradeId the Trade ID
     */
    public void meetingConfirmed(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.CONFIRMED);
    }

    /**
     * Confirm that the meeting took place in real life based on tradeId (depending on whether the userId is
     * a requester/receiver
     * @param tradeId the Trade ID
     * @param userId the user ID that confirms the Trade
     */
    public void confirmTrade(String tradeId, String userId) {
        Trade trade = findTrade(tradeId);
        if (trade.getStatus().equals(TradeStatus.CONFIRMED) && trade.getRequester().equals(userId)) {
            trade.setStatus(TradeStatus.REQUESTERCONFIRMED);
        } else if (trade.getStatus().equals(TradeStatus.CONFIRMED) && trade.getReceiver().equals(userId)) {
            trade.setStatus(TradeStatus.RECEIVERCONFIRMED);
        } else if (trade.getStatus().equals(TradeStatus.REQUESTERCONFIRMED) ||
                trade.getStatus().equals(TradeStatus.RECEIVERCONFIRMED)) {
            completeTrade(tradeId);
        }
    }

    /**
     * Completes the trade based on tradeId. Sets the Trade status into COMPLETED
     * @param tradeId the Trade ID
     */
    public void completeTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.COMPLETED);
    }

    /**
     * Cancels a Trade (only happens when the editThreshold Meeting reached) based on tradeId.
     * Sets the Trade status into CANCELLED.
     * @param tradeId the Trade ID
     */
    public void cancelTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.CANCELLED);
    }

    /**
     * Checks whether the userId violates the borrowDiff threshold
     * @param userId the user ID
     * @return false iff the userId violates
     */
    public boolean checkBorrowDiff(String userId) {
        List<String> borrowList = getTradeWithStatusAndTypeByUserId(userId, "COMPLETED", "Borrow");
        List<String> lendList = getTradeWithStatusAndTypeByUserId(userId, "COMPLETED", "Lend");
        return tvh.borrowDiffChecker(borrowList, lendList, getBorrowDiff());
    }

    /**
     * Checks whether the userId has reached the tradeLimit threshold
     * @param userId the user ID
     * @return false iff the user ID has reached the tradeLimit
     */
    public boolean checkTradeLimit(String userId) {
        return tvh.tradeLimitChecker(userId, getTradeIdToTrade(), getWeeklyTradeLimit());
    }

    /**
     * Checks whether the userId has reached the cancelledThreshold
     * @param userId the user ID
     * @return false iff the user ID has exceeded the cancelledThreshold
     */
    public boolean checkIncompleteTrade(String userId) {
        List<String> cancelledTrade = getTradeWithStatusAndTypeByUserId(userId, "CANCELLED", "All");
        return tvh.incompleteTradeChecker(cancelledTrade, getCancelledThreshold());
    }

    /**
     * Get tradeIds requested/received by userId based on tradeStatus and tradeType provided
     * @param userId the userId of a User
     * @param tradeStatus whether the existing status or All
     * @param tradeType whether it's Borrow, Lend, OneWay, TwoWay, or All
     * @return List of tradeIds based on userId, tradeStatus, and tradeType
     */
    public List<String> getTradeWithStatusAndTypeByUserId(String userId, String tradeStatus, String tradeType) {
        tradeInfoHelperSetter();
        return tih.getTradeWithStatusAndTypeByUserId(userId, tradeStatus, tradeType);
    }

    /**
     * Get num, recentItem, based on trades completed by userId
     * @param tradeType whether it's Borrow, Lend, OneWay, TwoWay, or All
     * @param userId the userId of a User
     * @param num number of itemIds to be viewed
     * @return List of itemIds with length num based on tradeType and userId
     */
    public List<String> getRecentItemsByUserId(String tradeType, String userId, int num) {
        tradeInfoHelperSetter();
        return tih.getRecentItemsByUserId(tradeType, userId, num);
    }

    /**
     * Get map of tradeIds and Trades based on whichWay
     * @param whichWay OneWay or TwoWay
     * @return Map of tradeIds with Trades
     */
    public Map<String, Trade> getAllWayTradeId(String whichWay) {
        tradeInfoHelperSetter();
        return tih.getAllWayTradeId(whichWay);
    }

    /**
     * Returns num, top traders, that have traded with userId
     * @param userId the user ID
     * @param num the number of userIds to be viewed
     * @return a List of userIds
     */
    public List<String> getTopTradersByUserId(String userId, int num) {
        tradeInfoHelperSetter();
        return tih.getTopTradersByUserId(userId, num);
    }

    /**
     * Creates a new Trade and add it to the TradeManager
     * @param userId1 The requester's userId
     * @param userId2 The receiver's userId
     * @param itemIds The List of itemIds
     * @return a String of the created tradeId
     */
    public String createNewTrade(String userId1, String userId2, List<String> itemIds) {
        Trade newTrade;
        newTrade = new Trade(userId1, userId2, itemIds);

        addTrade(newTrade.getTradeId(), newTrade);
        return newTrade.getTradeId();
    }

    /**
     * Creates a returning Trade when the Trade is temporary
     * @param tradeId the old trade ID
     * @return the new "returning" trade ID
     */
    public String createReverseTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        Trade newTrade;
        newTrade = new Trade(trade.getReceiver(), trade.getRequester(), trade.getItemIds());

        newTrade.setStatus(TradeStatus.ACCEPTED);
        newTrade.setTradeId("@return" + trade.getTradeId());
        newTrade.setDuration(-1);
        addTrade(newTrade.getTradeId(), newTrade);

        return newTrade.getTradeId();
    }

    /**
     * Reject all the trades containing itemId and userId
     * @param userId the userId of the User
     * @param itemId the itemId of the Item
     */
    public void rejectAllTradeWithItemId(String userId, String itemId) {
        List<String> requestTrade = getTradeWithStatusAndTypeByUserId(userId, "REQUESTED", "All");
        for (String tradeId: requestTrade) {
            if (getItemIdsByTradeId(tradeId).contains(itemId)) {
                rejectTrade(tradeId);
            }
        }
    }

    /**
     * Get list of tradeIds that have available actions based on userId
     * @param userId the userId of the User
     * @return list of String of tradeIds
     */
    public List<String> availableActionTrade(String userId) {
        List<String> allTradeIds = getTradeWithStatusAndTypeByUserId(userId, "All", "All");
        List<Trade> allTrade = convertTradeIdsToTrades(allTradeIds);
        return tvh.availableActionTrade(allTrade, userId);
    }

    /**
     * Checks whether the userId has requested itemId previously
     * @param itemId the itemId of the Item
     * @param userId the userId of the User
     * @return true iff userId has requested
     */
    public boolean haveRequested(String itemId, String userId) {
        List<String> allTradeIds = getTradeWithStatusAndTypeByUserId(userId, "All", "All");
        List<Trade> allTrade = convertTradeIdsToTrades(allTradeIds);
        return tvh.haveRequested(allTrade, itemId, userId);
    }

    /**
     * Checks whether the userId is eligible to confirm tradeId.
     * @param tradeId the tradeId
     * @param userId the userId in the tradeId
     * @return true iff userId can confirm tradeId
     */
    public boolean eligibleToConfirmTrade(String tradeId, String userId) {
        return tvh.eligibleToConfirmTrade(findTrade(tradeId), userId);
    }

    /**
     * Checks whether the userId is eligible to accept/reject tradeId
     * @param tradeId the tradeId
     * @param userId the userId in the tradeId
     * @return true iff the current user is able to accept/reject tradeId
     */
    public boolean eligibleToAcceptRejectTrade(String tradeId, String userId) {
        return tvh.eligibleToAcceptRejectTrade(findTrade(tradeId), userId);
    }

    /**
     * Convert list of tradeIds into list of Trades
     * @param listTradeIds list containing the tradeIds
     * @return List of Trade objects
     */
    public List<Trade> convertTradeIdsToTrades(List<String> listTradeIds) {
        tradeInfoHelperSetter();
        return tih.convertTradeIdsToTrades(listTradeIds);
    }

    /**
     * Get the user num (which corresponds to the Meeting)
     * @param tradeId the tradeId
     * @param userId the userId in tradeId
     * @return int of the user num
     */
    public int getUserNum(String tradeId, String userId) {
        tradeInfoHelperSetter();
        return tih.getUserNum(tradeId, userId);
    }

    /**
     * Returns the trade status based on the tradeId
     * @param tradeId the trade ID
     * @return a TradeStatus type of the status of the Trade
     */
    public TradeStatus getTradeStatusByTradeId(String tradeId) {
        return findTrade(tradeId).getStatus();
    }

    /**
     * Returns a receiver's userId based on the tradeId
     * @param tradeId the trade ID
     * @return a String of receiver's userId
     */
    public String getReceiverByTradeId(String tradeId) {
        return findTrade(tradeId).getReceiver();
    }

    /**
     * Returns a requester's userId based on the tradeId
     * @param tradeId the trade ID
     * @return a String of requester's userId
     */
    public String getRequesterByTradeId(String tradeId) {
        return findTrade(tradeId).getRequester();
    }

    /**
     * Get itemIds based on the tradeId
     * @param tradeId the trade ID
     * @return a List of Strings of itemIds
     */
    public List<String> getItemIdsByTradeId(String tradeId) {
        return findTrade(tradeId).getItemIds();
    }

    /**
     * Get duration based on tradeId
     * @param tradeId the trade ID
     * @return an int of duration
     */
    public int getDurationByTradeId(String tradeId) {
        return findTrade(tradeId).getDuration();
    }

    /**
     * Set meetingId based on tradeId
     * @param tradeId the trade ID
     * @param meetingId the meetingID
     */
    public void setMeetingIdByTradeId(String tradeId, String meetingId) {
        findTrade(tradeId).setMeetingId(meetingId);
    }

    /**
     * Get meetingId based on tradeId
     * @param tradeId the trade ID
     * @return the String of meetingId
     */
    public String getMeetingIdByTradeId(String tradeId) {
        return findTrade(tradeId).getMeetingId();
    }

    private void tradeInfoHelperSetter() {
        tih.setTradeIdToTrade(getTradeIdToTrade());
    }
}