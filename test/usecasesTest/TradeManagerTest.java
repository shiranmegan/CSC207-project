package usecasesTest;

import org.junit.*;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;
import usecases.*;
import entities.*;

import static org.junit.Assert.*;

public class TradeManagerTest {
    private TradeManager tm;
    private Trade t1;
    private Trade t2;
    private Trade t11;

    @Before
    public void setup() {
        List<Integer> tradeManagerList = new ArrayList<>();
        tradeManagerList.add(1);
        tradeManagerList.add(1);
        tradeManagerList.add(1);

        List<String> i1 = new ArrayList<>();
        i1.add("i1");
        i1.add("i2");

        List<String> i2 = new ArrayList<>();
        i2.add("i3");

        List<String> i3 = new ArrayList<>();
        i3.add("i4");

        this.t1 = new Trade("u1", "u2", i1);
        this.t2 = new Trade("u3", "u4", i2);
        this.t11 = new Trade("u1", "u2", i3);
        t1.setTradeId("t1");
        t2.setTradeId("t2");
        t11.setTradeId("t11");
        t1.setDuration(30);
        t2.setDuration(-1);
        t11.setDuration(-1);
        t1.setDateCreated(LocalDate.now());
        t2.setDateCreated(LocalDate.now().minusDays(30));
        t11.setDateCreated(LocalDate.now().minusDays(3));

        List<Trade> listTrade = new ArrayList<>();
        listTrade.add(t1);
        listTrade.add(t2);
        listTrade.add(t11);

        this.tm = new TradeManager(tradeManagerList, listTrade);
    }

    @Test
    public void testGetTradeIdToTrade() {
        List<Integer> tradeManagerList = new ArrayList<>();
        tradeManagerList.add(1);
        tradeManagerList.add(1);
        tradeManagerList.add(1);

        List<Trade> listTrade = new ArrayList<>();
        listTrade.add(t1);
        listTrade.add(t2);

        TradeManager copytm = new TradeManager(tradeManagerList, listTrade);
        HashMap<String, Trade> testhm = new HashMap<>();
        testhm.put("t1", t1);
        testhm.put("t2", t2);
        assertEquals(copytm.getTradeIdToTrade(), testhm);
    }

    @Test
    public void testSetCancelledThreshold() {
        tm.setCancelledThreshold(10);
        assertEquals(tm.getCancelledThreshold(), 10);
    }

    @Test
    public void testSetWeeklyTradeLimit() {
        tm.setWeeklyTradeLimit(10);
        assertEquals(tm.getWeeklyTradeLimit(), 10);
    }

    @Test
    public void testSetBorrowDiff() {
        tm.setBorrowDiff(10);
        assertEquals(tm.getBorrowDiff(), 10);
    }

    @Test
    public void testAddTrade() {
        List<String> i3 = new ArrayList<>();
        i3.add("i10");
        Trade t3 = new Trade("u10", "u11", i3);
        t3.setTradeId("t3");
        tm.addTrade(t3.getTradeId(), t3);
        assert(tm.getTradeIdToTrade().containsKey("t3"));
        assert(tm.getTradeIdToTrade().containsValue(t3));
    }

    @Test
    public void testFindTrade() {
        List<String> i3 = new ArrayList<>();
        i3.add("i10");
        Trade t3 = new Trade("u10", "u11", i3);
        t3.setTradeId("t3");
        tm.addTrade(t3.getTradeId(), t3);
        assertEquals(tm.findTrade("t3"), t3);
    }

    @Test
    public void testCreateNewTrade() {
        List<String> i3 = new ArrayList<>();
        i3.add("i10");
        String tradeId = tm.createNewTrade("u3", "u4", i3);
        assertEquals(tm.getTradeIdToTrade().size(), 4);
        assert(tm.getTradeIdToTrade().containsKey(tradeId));
        assert(tm.getTradeIdToTrade().containsValue(tm.findTrade(tradeId)));
    }

    @Test
    public void testCreateReverseTrade() {
        String tradeId = tm.createReverseTrade("t1");
        Trade trade = tm.findTrade(tradeId);
        assertEquals(trade.getRequester(), "u2");
        assertEquals(trade.getReceiver(), "u1");
        assertEquals(trade.getDuration(), -1);
        assertEquals(trade.getStatus(), TradeStatus.ACCEPTED);
        assertEquals(trade.getTradeId(), "@returnt1");
        assert(tm.getTradeIdToTrade().containsKey("@returnt1"));
        assert(tm.getTradeIdToTrade().containsValue(trade));
    }

    @Test
    public void testRequestTrade() {
        List<String> i3 = new ArrayList<>();
        i3.add("i10");
        tm.requestTrade("u3", "u4", i3, 15);
        assertEquals(tm.getTradeIdToTrade().size(), 4);

        tm.getTradeIdToTrade().remove("t1", t1);
        tm.getTradeIdToTrade().remove("t2", t2);
        tm.getTradeIdToTrade().remove("t11", t11);

        Map<String, Trade> hm1 = tm.getTradeIdToTrade();
        List<String> ar1 = new ArrayList<>();
        List<Trade> ar2 = new ArrayList<>();

        for (HashMap.Entry<String, Trade> entry: hm1.entrySet()) {
            ar1.add(entry.getKey());
            ar2.add(entry.getValue());
        }

        String tradeId1 = ar1.get(0);
        Trade trade1 = ar2.get(0);

        assertEquals(tm.findTrade(tradeId1), trade1);
        assertEquals(trade1.getRequester(),"u3");
        assertEquals(trade1.getReceiver(), "u4");
        assertEquals(trade1.getItemIds(), i3);
        assertEquals(trade1.getDuration(), 15);
    }

    @Test
    public void testRejectTrade() {
        tm.rejectTrade("t2");
        assertEquals(t2.getStatus(), TradeStatus.REJECTED);
    }

    @Test
    public void testAcceptTrade() {
        tm.acceptTrade("t2");
        assertEquals(t2.getStatus(), TradeStatus.ACCEPTED);
    }

    @Test
    public void testMeetingConfirmed() {
        tm.meetingConfirmed("t2");
        assertEquals(t2.getStatus(), TradeStatus.CONFIRMED);
    }

    @Test
    public void testRequesterConfirmTrade() {
        t1.setStatus(TradeStatus.CONFIRMED);
        tm.confirmTrade("t1", "u1");
        assertEquals(t1.getStatus(), TradeStatus.REQUESTERCONFIRMED);
        tm.confirmTrade("t1", "u2");
        assertEquals(t1.getStatus(), TradeStatus.COMPLETED);
    }

    @Test
    public void testReceiverConfirmTrade() {
        t2.setStatus(TradeStatus.CONFIRMED);
        tm.confirmTrade("t2", "u4");
        assertEquals(t2.getStatus(), TradeStatus.RECEIVERCONFIRMED);
        tm.confirmTrade("t2", "u3");
        assertEquals(t2.getStatus(), TradeStatus.COMPLETED);
    }

    @Test
    public void testCompleteTrade() {
        tm.completeTrade("t2");
        assertEquals(t2.getStatus(), TradeStatus.COMPLETED);
    }

    @Test
    public void testCancelTrade() {
        tm.cancelTrade("t2");
        assertEquals(t2.getStatus(), TradeStatus.CANCELLED);
    }

    @Test
    public void testCheckBorrowDiffNoTrade() {
        assert(tm.checkBorrowDiff("u10"));
    }

    @Test
    public void testCheckBorrowDiffLess() {
        tm.completeTrade("t1");
        tm.completeTrade("t2");

        assert(tm.checkBorrowDiff("u1"));
        assert(tm.checkBorrowDiff("u2"));
    }

    @Test
    public void testCheckBorrowDiffMore() {
        tm.completeTrade("t1");
        tm.completeTrade("t2");
        tm.completeTrade("t11");

        assert(!tm.checkBorrowDiff("u1"));
        assert(tm.checkBorrowDiff("u2"));
    }

    @Test
    public void testCheckTradeLimit() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        List<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u2", "u1", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        tm.findTrade(tGh).setStatus(TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);

        String returntCd = tm.createReverseTrade(tCd);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);
        tm.setWeeklyTradeLimit(3);
        assert(!tm.checkTradeLimit("u1"));
        tm.setWeeklyTradeLimit(4);
        assert(tm.checkTradeLimit("u1"));
    }

    @Test
    public void testCheckIncompleteTrade() {
        assert(tm.checkIncompleteTrade("u1"));
        t1.setStatus(TradeStatus.CANCELLED);
        assert(!tm.checkIncompleteTrade("u1"));
    }

    @Test
    public void testGetAllStatusByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u2", "u1", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.REQUESTED);
        tm.findTrade("t11").setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tGh).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(returntCd).setStatus(TradeStatus.REQUESTERCONFIRMED);

        List<String> testReq = tm.getTradeWithStatusAndTypeByUserId("u1", "REQUESTED", "All");
        List<String> testComp = tm.getTradeWithStatusAndTypeByUserId("u1", "COMPLETED", "All");
        List<String> testAcc = tm.getTradeWithStatusAndTypeByUserId("u1", "ACCEPTED", "All");
        List<String> testCan = tm.getTradeWithStatusAndTypeByUserId("u1", "CANCELLED", "All");
        List<String> testReqCon = tm.getTradeWithStatusAndTypeByUserId("u1", "REQUESTERCONFIRMED", "All");

        assert(testReq.contains("t1"));
        assert(testComp.contains("t11"));
        assert(testComp.contains(tAb));
        assert(testAcc.contains(tCd));
        assert(testAcc.contains(tEf));
        assert(testAcc.contains(tGh));
        assert(testCan.isEmpty());
        assert(testReqCon.contains(returntCd));
    }

    @Test
    public void testGetAllCompletedBorrowTradeByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(TradeStatus.COMPLETED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "COMPLETED", "Borrow");
        assert(!testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntAb));
    }

    @Test
    public void testGetAllCompletedLendTradeByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "COMPLETED", "Lend");

        assert(!testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllBorrowTradeIdByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u4", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.CANCELLED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CONFIRMED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "All", "Borrow");

        assert(!testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllLendTradeIdByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u5", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.CANCELLED);
        tm.findTrade(tGh).setStatus(TradeStatus.CANCELLED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CONFIRMED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "All", "Lend");

        assert(!testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllTradeIdByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u5", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REQUESTED);
        tm.findTrade(tAb).setStatus(TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CONFIRMED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "All", "All");

        assert(testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(testList.contains(returntCd));
    }

    @Test
    public void testGetAllTwoTradeIdByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        List<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u3", "u1", ef);
        String tGh = tm.createNewTrade("u4", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CANCELLED);

        List<String> testList = tm.getTradeWithStatusAndTypeByUserId("u1", "All", "TwoWay");

        assert(testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllOneWayTradeId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u4", "u6", ab);
        String tCd = tm.createNewTrade("u10", "u21", cd);
        String tEf = tm.createNewTrade("u18", "u33", ef);
        String tGh = tm.createNewTrade("u44", "u15", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CANCELLED);

        Map<String, Trade> testHM = tm.getAllWayTradeId("OneWay");

        assert(!testHM.containsKey("t1"));
        assert(testHM.containsKey("t11"));
        assert(!testHM.containsKey(tAb));
        assert(testHM.containsKey(tCd));
        assert(!testHM.containsKey(tEf));
        assert(testHM.containsKey(tGh));
        assert(testHM.containsKey(returntCd));
    }

    @Test
    public void testGetAllTwoWayTradeId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u23", "u14", ab);
        String tCd = tm.createNewTrade("u19", "u26", cd);
        String tEf = tm.createNewTrade("u13", "u33", ef);
        String tGh = tm.createNewTrade("u46", "u11", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(TradeStatus.CANCELLED);

        Map<String, Trade> testHM = tm.getAllWayTradeId("TwoWay");

        assert(testHM.containsKey("t1"));
        assert(!testHM.containsKey("t11"));
        assert(testHM.containsKey(tAb));
        assert(testHM.containsKey(tCd));
        assert(!testHM.containsKey(tEf));
        assert(!testHM.containsKey(tGh));
        assert(testHM.containsKey(returntCd));
    }

    @Test
    public void testGetRecentItemsBorrowedByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u3", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u4", cd);
        String tEf = tm.createNewTrade("u1", "u33", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);

        List<String> test1 = tm.getRecentItemsByUserId("Borrow", "u1", 0);
        List<String> test2 = tm.getRecentItemsByUserId("Borrow", "u1", 2);
        List<String> test3 = tm.getRecentItemsByUserId("Borrow", "u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "g");
        assertEquals(test2.get(1), "c");
        assertEquals(test3.get(2), "e");
    }

    @Test
    public void testGetRecentItemsLendByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e"));
        List<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u1", "u3", ab);
        String tCd = tm.createNewTrade("u4", "u1", cd);
        String tEf = tm.createNewTrade("u5", "u1", ef);
        String tGh = tm.createNewTrade("u6", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);

        List<String> test1 = tm.getRecentItemsByUserId("Lend","u1", 0);
        List<String> test2 = tm.getRecentItemsByUserId("Lend","u1", 2);
        List<String> test3 = tm.getRecentItemsByUserId("Lend","u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "e");
        assertEquals(test2.get(1), "g");
        assertEquals(test3.get(2), "c");
    }

    @Test
    public void testGetRecentItemsInTwoWayByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        List<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u4", "u3", ab);
        String tCd = tm.createNewTrade("u4", "u1", cd);
        String tEf = tm.createNewTrade("u1", "u2", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.REQUESTED);
        tm.findTrade("t11").setStatus(TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);

        List<String> test1 = tm.getRecentItemsByUserId("TwoWay", "u1", 0);
        List<String> test2 = tm.getRecentItemsByUserId("TwoWay", "u1", 2);
        List<String> test3 = tm.getRecentItemsByUserId("TwoWay", "u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 4);
        assertEquals(test3.size(), 6);
        assertEquals(test2.get(0), "e");
        assertEquals(test2.get(1), "f");
        assertEquals(test2.get(2), "g");
        assertEquals(test2.get(3), "h");
        assertEquals(test3.get(4), "c");
        assertEquals(test3.get(5), "d");
    }

    @Test
    public void testGetTopTradersByUserId() {
        List<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> cd = new ArrayList<>(Arrays.asList("c"));
        List<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        List<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        List<String> ij = new ArrayList<>(Arrays.asList("i", "j"));
        List<String> kl = new ArrayList<>(Arrays.asList("k", "l"));
        String tAb = tm.createNewTrade("u1", "u4", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        String tIj = tm.createNewTrade("u1", "u3", ij);
        String tKl = tm.createNewTrade("u4", "u3", kl);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tIj).setDateCreated(LocalDate.now().minusDays(600));
        tm.findTrade(tKl).setDateCreated(LocalDate.now());
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(tIj).setStatus(TradeStatus.REQUESTED);
        tm.findTrade(tKl).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(TradeStatus.COMPLETED);

        List<String> test1 = tm.getTopTradersByUserId("u1", 0);
        List<String> test2 = tm.getTopTradersByUserId("u1", 2);
        List<String> test3 = tm.getTopTradersByUserId("u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "u2");
        assertEquals(test2.get(1), "u3");
        assertEquals(test3.get(2), "u4");
    }

    @Test
    public void testGetTradeStatusByUserId() {
        t1.setStatus(TradeStatus.REQUESTERCONFIRMED);
        assertEquals(tm.getTradeStatusByTradeId("t1"), TradeStatus.REQUESTERCONFIRMED);
    }

    @Test
    public void testGetReceiverByTradeId() {
        assertEquals(tm.getReceiverByTradeId("t1"), "u2");
    }

    @Test
    public void testGetRequesterByTradeId() {
        assertEquals(tm.getRequesterByTradeId("t1"), "u1");
    }

    @Test
    public void testGetItemIdsByTradeId() {
        assertEquals(tm.getItemIdsByTradeId("t1"), t1.getItemIds());
    }

    @Test
    public void testGetDurationByTradeId() {
        assertEquals(tm.getDurationByTradeId("t1"), t1.getDuration());
    }

    @Test
    public void testSetMeetingIdByTradeId() {
        tm.setMeetingIdByTradeId("t1", "123");
        assertEquals(tm.getMeetingIdByTradeId("t1"), "123");
    }

    @Test
    public void testGetMeetingIdByTradeId() {
        assertEquals(tm.getMeetingIdByTradeId("t1"), "No Meeting");
    }
}
