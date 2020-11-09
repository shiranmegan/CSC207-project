package gateways;

import entities.Meeting;
import usecases.MeetingManager;

import java.util.*;

/**
 * MeetingGateway is a Gateway for Meeting
 */
public class MeetingGateway extends Gateway {

    public MeetingGateway() {

    }

    /**
     * Updates MeetingManager mm to meeting.ser
     * @param mm MeetingManager to be updated
     */
    public void updateMeetingInfo(MeetingManager mm) {
        String fileName = "src/database/Meeting.ser";
        List<Meeting> serList = new ArrayList<>();
        Map<String, Meeting> mmMeetingIdToMeeting = mm.getMeetingIdToMeeting();
        for (Map.Entry<String, Meeting> entry: mmMeetingIdToMeeting.entrySet()){
            serList.add(entry.getValue());
        }
        updateInfo(serList, fileName);
    }

    /**
     * Get information from meeting.ser
     * @return List of Meetings from .ser
     */
    @Override
    public List<Meeting> getInfo() {
        return helperGetInfo("database/Meeting.ser");
    }

}