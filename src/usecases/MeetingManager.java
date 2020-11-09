package usecases;

import entities.Meeting;

import java.time.*;
import java.util.*;

/**
 * MeetingManager is a use case for Meeting
 *
 * editsThreshold is the threshold for maximum number of edits for any meeting
 * meetingIdToMeeting is to store meetingId and Meeting.
 */
public class MeetingManager {
    private int editsThreshold;
    private Map<String, Meeting> meetingIdToMeeting = new HashMap<>();

    /**
     * Constructor for MeetingManager. Creates a MeetingManager with editsThreshold and meetingIdToMeeting, which is
     * based on the meetingManagerList and stores the HashMap of meetingId to Meeting from the database.
     *
     * @param editsThreshold editsThreshold
     * @param listMeeting meetingList
     */
    public MeetingManager(int editsThreshold, List<Meeting> listMeeting) {
        this.editsThreshold = editsThreshold;
        for (Meeting Meeting : listMeeting) {
            this.meetingIdToMeeting.put(Meeting.getMeetingId(), Meeting);
        }
    }

    /**
     * Gets the limit for the number of times a User can edit a Meeting.
     * @return editsThreshold.
     */
    public int getEditsThreshold() {
        return this.editsThreshold;
    }

    /**
     * Sets the limit for the number of times a User can edit a Meeting.
     * @param suggestedEdits int of new threshold for editing a Meeting.
     */
    public void setEditsThreshold(int suggestedEdits) {
        this.editsThreshold = suggestedEdits;
    }

    /**
     * Gets meetingIdToMeeting.
     * @return a Map of meetingIds to Meeting.
     */
    public Map<String, Meeting> getMeetingIdToMeeting() {
        return this.meetingIdToMeeting;
    }

    /**
     * Sets meetingIdToMeeting.
     * @param meetingIdToMeeting a Map of meetingIds to Meeting
     */
    public void setMeetingIdToMeeting(Map<String, Meeting> meetingIdToMeeting) {
        this.meetingIdToMeeting = meetingIdToMeeting;
    }

    /**
     * Gets a Meeting based on meetingId.
     * @return the Meeting corresponding to meetingId
     */
    public Meeting findMeeting(String meetingId) {
        return getMeetingIdToMeeting().get(meetingId);
    }

    /**
     * Adds a Meeting and its meetingId to the MeetingManager.
     * @param meetingId a Meeting id.
     * @param Meeting a corresponding Meeting to the Meeting id.
     */
    public void addMeeting(String meetingId, Meeting Meeting) {
        getMeetingIdToMeeting().put(meetingId, Meeting);
    }

    /**
     * Creates a new Meeting based on time and place adds it to the MeetingManager.
     * @param time a suggested time for the Meeting.
     * @param place a suggested place for the Meeting.
     * @return the String of the meetingId of the Meeting.
     */
    public String createNewMeeting(LocalDate time, String place) {
        Meeting Meeting = new Meeting(time, place);
        addMeeting(Meeting.getMeetingId(), Meeting);
        return (Meeting.getMeetingId());
    }

    /**
     * Checks whether userId (based on userNum) can edit meetingId
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the num for a userId
     * @return true iff userId can edit the Meeting.
     */
    public boolean userCanEdit(String meetingId, int userNum) {
        Meeting Meeting = findMeeting(meetingId);
        return Meeting.getUsersEditCount()[userNum] < getEditsThreshold();
    }

    /**
     * Edits the meetingId with new time and place based on userNum
     * @param meetingId Meeting id of the Meeting.
     * @param time a suggested time for the Meeting.
     * @param place a suggested place for the Meeting.
     * @param userNum the userNum
     * @return true iff the meetingId has been successfully edited
     */
    public boolean editTimePlace(String meetingId, LocalDate time, String place, int userNum) {
        Meeting Meeting = findMeeting(meetingId);
        if (userCanEdit(meetingId, userNum)) {
            Meeting.setTime(time);
            Meeting.setPlace(place);
            changeTurns(meetingId, userNum);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether it's userId's turn (based on userNum) to edit meetingId
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the userNum
     * @return ture iff it's User's turn to edit
     */
    public boolean usersTurn(String meetingId, int userNum) {
        return findMeeting(meetingId).getUsersTurn()[userNum];
    }

    /**
     * Confirms the meetingId based on userId (which depends on the userNum)
     * @param meetingId Meeting id of the Meeting.
     * @param userNum the userNum given
     */
    public void confirmTimePlace(String meetingId, int userNum) {
        Meeting Meeting = findMeeting(meetingId);
        Meeting.getUsersTurn()[userNum] = false;
    }

    /**
     * Gets whether or not it a meetingId is confirmed by both Users
     * @param meetingId Meeting id of the Meeting.
     * @return true iff meetingId has been confirmed.
     */
    public boolean isMeetingIdConfirmed(String meetingId) {
        Meeting Meeting = findMeeting(meetingId);
        return !Meeting.getUsersTurn()[0] && !Meeting.getUsersTurn()[1];
    }

    /**
     * Gets the place of a Meeting based on meetingId
     * @param meetingId Meeting id of the Meeting.
     * @return a String of the place of the Meeting.
     */
    public String getPlaceByMeetingId(String meetingId) {
        return findMeeting(meetingId).getPlace();
    }

    /**
     * Gets the time of a Meeting based on meetingId
     * @param meetingId Meeting id of the Meeting.
     * @return a LocalDate of the time of the Meeting.
     */
    public LocalDate getTimeByMeetingId(String meetingId) {
        return findMeeting(meetingId).getTime();
    }

    /**
     * Checks whether meeting has been created based on meetingId
     * @param meetingId meetingId to be checked
     * @return true iff there is no meeting made
     */
    public boolean isNoMeeting(String meetingId) {
        return meetingId.equalsIgnoreCase("No Meeting");
    }

    private void changeTurns(String meetingId, int userNum) {
        Meeting Meeting = findMeeting(meetingId);
        if (Meeting.getUsersTurn()[0] && !Meeting.getUsersTurn()[1]) {
            Meeting.getUsersEditCount()[userNum]++;
            Meeting.getUsersTurn()[0] = false;
            Meeting.getUsersTurn()[1] = true;
        } else if (!Meeting.getUsersTurn()[0] && Meeting.getUsersTurn()[1]) {
            Meeting.getUsersEditCount()[userNum]++;
            Meeting.getUsersTurn()[0] = true;
            Meeting.getUsersTurn()[1] = false;
        } else if (Meeting.getUsersTurn()[0] && Meeting.getUsersTurn()[1]) {
            Meeting.getUsersTurn()[userNum] = false;
            Meeting.getUsersEditCount()[userNum]++;
        }
    }
}
