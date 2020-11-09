package entities;

/**
 * an enum class that represents the four possible statuses a User can be in
 */
public enum UserStatus {
    /**
     * good-standing; allowed to request and perform trades
     */
    GOOD,

    /**
     * flagged automatically by the system for violating thresholds, and pending for review by an admin; prevented from initiating/receiving trades
     */
    FLAGGED,

    /**
     * a flagged User that was decided to be frozen by an admin; prevented from initiating/receiving trades
     */
    FROZEN,

    /**
     * a frozen User that has requested to be unfrozen, and pending for review by an admin; prevented from initiating/receiving trades
     */
    REQUESTED,

    /**
     * User that is on vacation is not able to do trades and not able to receive trade requests
     */
    VACATION
}