package entities;

import java.io.Serializable;
import java.util.*;
import java.time.*;

/**
 * Trade is an entity
 *
 * tradeId is the Trade ID
 * status is the Trade status
 * duration is the duration of the Trade:
 *        - if the duration == -1, the Trade is permanent
 *        - if the duration > 0, the Trade is temporary (in days)
 * requester is the userId which requests the Trade (Assume can only request to borrow an item)
 * receiver is the userId which receives the Trade request
 * itemIds is the item IDs to be traded:
 *        - In one way trade, the item ID will be the item to be borrowed by the requester
 *        - In two way trade, itemIds.get(0) will be the item to be borrowed by the requester, which means
 *                            the item is currently owned by the receiver
 *                            itemIds.get(1) will be the item to be lent by the requester, which means
 *                            the item is currently owned by the requester
 * dateCreated is the current time when the Trade is created
 */
public class Trade implements Serializable {

    private String tradeId;
    private TradeStatus status;
    private int duration;
    private String meetingId; 
    private String requester;
    private String receiver;
    private List<String> itemIds;
    private LocalDate dateCreated;

    /**
     * toString Mechanism for Trade
     * @return information about Trade in String
     */
    public String toString() {
        return "Date created: " + dateCreated +
                "\nTrade status: " + status;
    }

    /**
     * The first constructor for Trade
     * @param userId1 The requester's userId
     * @param userId2 The receiver's userId
     * @param itemIds The itemIds to be traded (owned by the receiver / owned by the requester)
     */
    public Trade(String userId1, String userId2, List<String> itemIds) {
        UUID id = UUID.randomUUID();
        this.tradeId = String.valueOf(id);
        this.requester = userId1;
        this.receiver = userId2;
        this.itemIds = itemIds;
        this.dateCreated = LocalDate.now();
        this.meetingId = "No Meeting";
        this.status = TradeStatus.REQUESTED;
    }

    /**
     * Getters for tradeId of the Trade
     * @return a String of the tradeId of the Trade
     */
    public String getTradeId() {
        return this.tradeId;
    }

    /**
     * Getters for status of the Trade
     * @return a TradeStatus type of the status of the Trade
     */
    public TradeStatus getStatus() {
        return this.status;
    }

    /**
     * Getters for the duration of the Trade
     * @return an int of the duration of the Trade
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Getters for the requester of the Trade
     * @return a String of the userId of the requester
     */
    public String getRequester() {
        return this.requester;
    }

    /**
     * Getters for the receiver of the Trade
     * @return a String of the userId of the receiver
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * Getters for the itemIds of the Trade
     * @return an ArrayList of Strings of itemIds
     */
    public List<String> getItemIds() {
        return this.itemIds;
    }

    /**
     * Getters for the meetingId of the Trade
     * @return a String of meetingId
     */
    public String getMeetingId() {
        return this.meetingId;
    }

    /**
     * Getters for the dateCreated of the Trade
     * @return a LocalDate type of date when the Trade is created
     */
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Setters for the status of the Trade
     * @param status the status for Trade
     */
    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    /**
     * Setters for the tradeId of the Trade
     * @param tradeId the tradeId for trade
     */
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    /**
     * Setters for the dataCreated of the Trade
     * @param dateCreated the dateCreated for trade
     */
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    /**
     * Setters for the duration of the Trade
     * @param duration the duration for trade
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Setters for the requester of the Trade
     * @param userId1 the requester's userid
     */
    public void setRequester(String userId1) {
        this.requester = userId1;
    }

    /**
     * Setters for the receiver of the Trade
     * @param userId2 the receiver's userId
     */
    public void setReceiver(String userId2) {
        this.receiver = userId2;
    }

    /**
     * Setters for the ArrayList of itemIds of the Trade
     * @param itemIds the itemIds for the trade
     */
    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    /**
     * Setters for the meetingId of the Trade
     * @param meetingId the meetingId for the trade
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * Returns true if the Trade is a one way Trade, otherwise returns false
     * @return boolean whether it is a one way Trade or not
     */
    public boolean isOneWay(){
        return this.getItemIds().size() != 2;
    }

}

