package entitiesTest;

import entities.Trade;
import entities.TradeStatus;
import java.util.*;
import java.time.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TradeTest {

    private Trade t;

    @Before
    public void setup() {
        String u1 = "u1";
        String u2 = "u2";
        String i1 = "i1";
        String i2 = "i2";
        ArrayList<String> i = new ArrayList<>();
        i.add(i1);
        i.add(i2);
        this.t = new Trade(u1, u2, i);
    }

    @Test
    public void testSetStatus() {
        t.setStatus(TradeStatus.ACCEPTED);
        assertEquals(t.getStatus(), TradeStatus.ACCEPTED);
    }

    @Test
    public void testSetTradeId() {
        t.setTradeId("@return123");
        assertEquals(t.getTradeId(), "@return123");
    }

    @Test
    public void testSetDateCreated() {
        t.setDateCreated(LocalDate.now());
        assertEquals(t.getDateCreated(), LocalDate.now());
    }

    @Test
    public void testSetDuration() {
        t.setDuration(30);
        assertEquals(t.getDuration(), 30);
    }

    @Test
    public void testSetRequester() {
        t.setRequester("Rushia");
        assertEquals(t.getRequester(), "Rushia");
    }

    @Test
    public void testSetReceiver() {
        t.setReceiver("Fubuki");
        assertEquals(t.getReceiver(), "Fubuki");
    }

    @Test
    public void testSetItemIds() {
        ArrayList<String> ii = new ArrayList<>();
        ii.add("i2");
        ii.add("i1");
        t.setItemIds(ii);
        assertEquals(t.getItemIds(), ii);
    }

    @Test
    public void testSetMeetingId() {
        t.setMeetingId("11");
        assertEquals(t.getMeetingId(), "11");
    }

    @Test
    public void testIsOneWay() {
        assert(!t.isOneWay());
    }
}
