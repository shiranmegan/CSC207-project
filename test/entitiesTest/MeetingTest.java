package entitiesTest;

import entities.Meeting;

import java.time.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MeetingTest {

    private Meeting m;

    @Before
    public void setup() {
        this.m = new Meeting(LocalDate.now(), "PekoraLand");
    }

    @Test
    public void testSetId() {
        m.setMeetingId("123");
        assertEquals(m.getMeetingId(), "123");
    }

    @Test
    public void testSetTime() {
        LocalDate newDate = LocalDate.now().plusDays(3);
        m.setTime(newDate);
        assertEquals(m.getTime(), newDate);
    }

    @Test
    public void testSetPlace() {
        m.setPlace("Hololive");
        assertEquals(m.getPlace(), "Hololive");
    }

    @Test
    public void testSetUsersTurn() {
        boolean[] ut = new boolean[2];
        ut[0] = true;
        ut[1] = false;
        m.setUsersTurns(ut);
        assertEquals(m.getUsersTurn(), ut);
    }

    @Test
    public void testSetUsersEditCount() {
        int[] in = new int[2];
        in[0] = 2;
        in[1] = 3;
        m.setUsersEditCount(in);
        assertEquals(m.getUsersEditCount(), in);
    }
}
