package entities;
import java.io.Serializable;
import java.time.*;
import java.util.UUID;

public class Meeting implements Serializable {

    /**
     * Meeting is an entity
     *
     * meetingId: The unique identifier of the Meeting.
     * time: The time of the Meeting.
     * place: The place of the Meeting.
     * usersTurn: an indicator of whether or not it is a User's turn to edit a Meeting. It contains user1's turn
     * and user2's turn corresponding to requester and receiver (in Trade)
     * usersEditCount: the number of edits a user has made to a Meeting. It contains user1's edit count and
     * user2's edit count corresponding to requester and receiver (in Trade)
     */
    private String meetingId;
    private LocalDate time;
    private String place;
    private boolean[] usersTurn;
    private int[] usersEditCount;

    /**
     * Constructor for "Meeting." Newly constructed Meetings automatically have usersTurn both set
     * to false, and usersEditCount to 0. "
     * @param time The assigned Meeting time.
     * @param place  The assigned Meeting place.
     */
    public Meeting(LocalDate time, String place) {
        UUID id = UUID.randomUUID();
        this.meetingId = String.valueOf(id);
        this.time = time;
        this.place = place;
        this.usersTurn = new boolean[]{true, true};
        this.usersEditCount = new int[]{0, 0};
    }

    /**
     * toString for Meeting
     * @return information about Meeting in String
     */
    @Override
    public String toString() {
        return " Time: " + getTime() +
                "\n Place: " + getPlace() +
                "\n Requester can edit: " + getUsersTurn()[0] +
                "\n Receiver can edit: " + getUsersTurn()[1] +
                "\n Requester's number of edits: " + getUsersEditCount()[0] +
                "\n Receiver's number of edits: " + getUsersEditCount()[1];
    }

    /**
     * Gets the Meeting place.
     * @return place
     */
    public String getPlace() {
        return this.place;
    }

    /**
     * Sets the Meeting place.
     * @param suggestedPlace the suggested place.
     */
    public void setPlace(String suggestedPlace) {
        this.place = suggestedPlace;
    }

    /**
     * Gets whether or not it is a User's turn.
     * @return usersTurn
     */
    public boolean[] getUsersTurn() {
        return this.usersTurn;
    }

    /**
     * Sets the User's turns.
     * @param usersTurns the suggested turns.
     */
    public void setUsersTurns(boolean[] usersTurns) {
        this.usersTurn = usersTurns;
    }

    /**
     * Gets number of edits a User has made to a Meeting.
     * @return usersEditCount
     */
    public int[] getUsersEditCount() {
        return this.usersEditCount;
    }

    /**
     * Sets the User's edit count for this Meeting.
     * @param usersEditCount the suggested count.
     */
    public void setUsersEditCount(int[] usersEditCount) { this.usersEditCount = usersEditCount; }

    /**
     * Gets the meeting ID of a meeting
     * @return String of meeting ID
     */
    public String getMeetingId() {
        return this.meetingId;
    }

    /**
     * Sets the meeting ID of a meeting
     * @param meetingId the new meeting ID in String
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * Gets the Meeting time.
     * @return time
     */
    public LocalDate getTime() {
        return this.time;
    }

    /**
     * Sets the Meeting time.
     * @param suggestedTime the suggested time.
     */
    public void setTime(LocalDate suggestedTime) {
        this.time = suggestedTime;
    }
}




