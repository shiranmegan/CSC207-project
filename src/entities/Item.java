package entities;

import entities.ItemStatus;

import java.io.Serializable;
import java.util.UUID;

public class Item implements Serializable {

    /**
     * Item is an entity
     *
     * name: The name of the item.
     * id: The unique identifier of the item.
     * description: The description of the item.
     * status: The status of the item in terms of availability.
     */
    private String name;
    private String id;
    private String description;
    private entities.ItemStatus status;

    /**
     * Constructor for "Item." Newly constructed Items automatically have status set to "available."
     * @param inputName The assigned Item name.
     * @param inputDescription  The assigned Item description.
     */
    public Item(String inputName, String inputDescription) {
        name = inputName;
        UUID inputId = UUID.randomUUID();
        id = String.valueOf(inputId);
        description = inputDescription;
        status = entities.ItemStatus.UNCHECKED;
    }

    /**
     * Overloaded constructor for "Item," for when no description is provided.
     * @param inputName The assigned Item name.
     */
    public Item(String inputName) {
        this(inputName, "");
    }

    /**
     * Gets the item name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Gets the item id.
     * @return id
     */
    public String getId() { return id; }

    /**
     * Gets the item description.
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Gets the item status.
     * @return status
     */
    public ItemStatus getStatus() { return status; }

    /**
     * Sets the item name.
     * @param inputName the input name.
     */
    public void setName(String inputName) { name = inputName; }

    /**
     * Sets the item id.
     * @param inputId the input id.
     */
    public void setId(String inputId) { id = inputId; }

    /**
     * Sets the item description.
     * @param inputDescription the item description.
     */
    public void setDescription(String inputDescription) { description = inputDescription; }

    /**
     * Sets the item status.
     * @param inputStatus the item status.
     */
    public void setStatus(entities.ItemStatus inputStatus) { status = inputStatus; }

    /**
     * Returns a string version of the item.
     * @return string
     */
    @Override
    public String toString() {
        return "Name: " + name + "\n" + "Description: " + description + "\nStatus: " + status.toString();
    }


}
