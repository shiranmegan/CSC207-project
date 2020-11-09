package entities;

/**
 * An enum class for the status for Trades
 *
 * REQUESTED means the current status of the Trade is requested by the requester
 * ACCEPTED means the Trade is accepted by the receiver
 * REJECTED means the Trade is rejected by the receiver
 * CONFIRMED means the meeting for the Trade has been confirmed from both users
 * REQUESTERCONFIRMED means the Trade has been confirmed to be happened in real life by the requester
 * RECEIVERCONFIRMED means the Trade has been confirmed to be happened in real life by the receiver
 * COMPLETED means both users have confirmed that the Trade has happened in real life
 * CANCELLED means the Trade is cancelled after the Trade is accepted
 */

public enum TradeStatus {
    REQUESTED,
    ACCEPTED,
    REJECTED,
    CONFIRMED,
    REQUESTERCONFIRMED, // Need to change for clarifying the meaning
    RECEIVERCONFIRMED,
    COMPLETED,
    CANCELLED
}
