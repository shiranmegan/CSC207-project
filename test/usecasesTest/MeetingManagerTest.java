package usecasesTest;

import org.junit.*;
import java.util.*;
import java.time.*;
import usecases.*;
import entities.*;

import static org.junit.Assert.*;

public class MeetingManagerTest {
    private MeetingManager mm;
    private Meeting m1;
    private Meeting m2;

    @Before
    public void setup() {
        this.m1 = new Meeting(LocalDate.now(), "PekoraLand");
        this.m2 = new Meeting(LocalDate.now().plusDays(15), "Minecraft");
        m1.setMeetingId("m1");
        m2.setMeetingId("m2");

        List<Meeting> listMeeting = new ArrayList<>();
        listMeeting.add(m1);
        listMeeting.add(m2);

        this.mm = new MeetingManager(1, listMeeting);
        mm.setEditsThreshold(1);
        Map<String, Meeting> hm = new HashMap<>();
        hm.put("m1", m1);
        hm.put("m2", m2);
        mm.setMeetingIdToMeeting(hm);
    }

    @Test
    public void testGetMeetingIdToMeeting() {
        List<Meeting> listMeeting = new ArrayList<>();
        listMeeting.add(m1);
        listMeeting.add(m2);

        MeetingManager copymm = new MeetingManager(1, listMeeting);
        HashMap<String, Meeting> testhm = new HashMap<>();
        testhm.put("m1", m1);
        testhm.put("m2", m2);
        assertEquals(copymm.getMeetingIdToMeeting(), testhm);
    }

    @Test
    public void testAddMeeting() {
        Meeting m3 = new Meeting(LocalDate.now(), "place");
        m3.setMeetingId("m3");
        mm.addMeeting("m3", m3);
        assert(mm.getMeetingIdToMeeting().containsKey("m3"));
        assert(mm.getMeetingIdToMeeting().containsValue(m3));
    }

    @Test
    public void testFindMeeting() {
        Meeting m3 = new Meeting(LocalDate.now(), "place");
        m3.setMeetingId("m3");
        mm.addMeeting("m3", m3);
        assertEquals(mm.findMeeting("m3"), m3);
    }

    @Test
    public void testCreateNewMeeting() {
        String meetingId = mm.createNewMeeting(LocalDate.now(), "place");
        Meeting m = mm.findMeeting(meetingId);
        assert(mm.getMeetingIdToMeeting().containsKey(meetingId));
        assert(mm.getMeetingIdToMeeting().containsValue(m));
    }

    @Test
    public void testUserCanEdit() {
        m1.getUsersEditCount()[1]++;
        assert(mm.userCanEdit("m1", 0));
        assert(!mm.userCanEdit("m1", 1));
    }

    @Test
    public void testCantEditTimePlace() {
        LocalDate correctTime = LocalDate.now().plusDays(3);
        String correctPlace = "Hololive";
        m1.getUsersEditCount()[1]++;
        assert(!mm.editTimePlace("m1", correctTime, correctPlace, 1));
        assertEquals(m1.getTime(), LocalDate.now());
        assertEquals(m1.getPlace(), "PekoraLand");
    }

    @Test
    public void testFirstEditTimePlace() {
        LocalDate correctTime = LocalDate.now().plusDays(3);
        String correctPlace = "Hololive";
        assert(mm.editTimePlace("m1", correctTime, correctPlace, 0));
        assertEquals(m1.getTime(), correctTime);
        assertEquals(m1.getPlace(), correctPlace);
        assert(!m1.getUsersTurn()[0]);
        assert(m1.getUsersTurn()[1]);
    }

    @Test
    public void testLater0EditTimePlace() {
        LocalDate wrongTime = LocalDate.now().plusDays(5);
        String wrongPlace = "Necro";
        LocalDate correctTime = LocalDate.now().plusDays(3);
        String correctPlace = "Hololive";
        assert(mm.editTimePlace("m1", wrongTime, wrongPlace, 1));
        assert(!mm.editTimePlace("m1", wrongTime, wrongPlace, 1));
        assert(mm.editTimePlace("m1", correctTime, correctPlace, 0));
        assertEquals(m1.getTime(), correctTime);
        assertEquals(m1.getPlace(), correctPlace);
        assert(!m1.getUsersTurn()[0]);
        assert(m1.getUsersTurn()[1]);
    }

    @Test
    public void testLater1EditTimePlace() {
        LocalDate wrongTime = LocalDate.now().plusDays(5);
        String wrongPlace = "Necro";
        LocalDate correctTime = LocalDate.now().plusDays(3);
        String correctPlace = "Hololive";
        assert(mm.editTimePlace("m1", wrongTime, wrongPlace, 0));
        assert(!mm.editTimePlace("m1", wrongTime, wrongPlace, 0));
        assert(mm.editTimePlace("m1", correctTime, correctPlace, 1));
        assertEquals(m1.getTime(), correctTime);
        assertEquals(m1.getPlace(), correctPlace);
        assert(!m1.getUsersTurn()[1]);
        assert(m1.getUsersTurn()[0]);
    }

    @Test
    public void testUsersTurn() {
        boolean[] tf = new boolean[]{true, false};
        m1.setUsersTurns(tf);
        assert(mm.usersTurn("m1", 0));
        assert(!mm.usersTurn("m1", 1));
    }

    @Test
    public void testConfirmTimePlace() {
        assert(m1.getUsersTurn()[0]);
        assert(m1.getUsersTurn()[1]);
        mm.confirmTimePlace("m1", 0);
        assert(!m1.getUsersTurn()[0]);
        mm.confirmTimePlace("m1", 1);
        assert(!m1.getUsersTurn()[1]);
    }

    @Test
    public void testIsMeetingIdConfirmed() {
        assert(!mm.isMeetingIdConfirmed("m1"));

        boolean[] tf = new boolean[]{true, false};
        m1.setUsersTurns(tf);
        assert(!mm.isMeetingIdConfirmed("m1"));

        boolean[] ft = new boolean[]{false, true};
        m1.setUsersTurns(ft);
        assert(!mm.isMeetingIdConfirmed("m1"));

        boolean[] ff = new boolean[]{false, false};
        m1.setUsersTurns(ff);
        assert(mm.isMeetingIdConfirmed("m1"));
    }

    @Test
    public void testGetPlaceByMeetingId() {
        assertEquals(mm.getPlaceByMeetingId("m1"), "PekoraLand");
    }

    @Test
    public void testGetTimeByMeetingId() {
        assertEquals(mm.getTimeByMeetingId("m1"), LocalDate.now());
    }
}
