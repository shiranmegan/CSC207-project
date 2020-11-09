package entities;

/**
 * an enum class that represents the statuses of an item in the system
 */
public enum ItemStatus {
    /**
     * an item waiting admin approval, which can be deleted from the system
     */
    UNCHECKED,
    /**
     * an approved item available for trade, which can be deleted from the system
     */
    AVAILABLE,
    /**
     * an approved item unavailable for trade, which can be deleted from the system
     */
    UNAVAILABLE,
    /**
     * an removed item should only be viewable in inventory, which cannot be deleted from the system
     */
    REMOVED
}
