package controllers.tradeSystem;

import java.util.*;
import java.lang.*;

import controllers.IController;
import controllers.SystemBuilder;
import entities.UserStatus;
import gateways.*;
import presenters.TradePresenter;
import presenters.UserPresenter;
import usecases.*;

/**
 * TradeSystem is a controller dealing with trades.
 * tm is the UseCase Class TradeManager.
 * um is the UseCase Class UserManager.
 * im is the UseCase Class ItemManager.
 * mm is the UseCase Class MeetingManager.
 * tp is the Presenter for Trade.
 * dts is the Controller for displaying trades.
 * rts is the Controller for requesting trades.
 * dats is the controller for accepting, rejecting or completing a trade.
 * userId is the id of the User who is currently using the system.
 * ig is the Gateway Class for updating information of Item.
 * tg is the Gateway Class for updating information of Trade.
 * ug is the Gateway Class for updating information of User.
 * mg is the Gateway Class for updating information of User.
 */
public class TradeSystem implements IController {
    private TradeManager tm;
    private UserManager um;
    private ItemManager im;
    private MeetingManager mm;

    private TradePresenter tp = new TradePresenter();
    private UserPresenter up = new UserPresenter();

    private DisplayTradeHelper dts;
    private RequestTradeHelper rts;
    private DoActionTradeHelper dats;
    private String userId;

    private TradeGateway tg = new TradeGateway();
    private ItemGateway ig = new ItemGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();

    private SystemBuilder sb;

    /**
     * The constructor for TradeSystem
     * @param userId is the id of the User who is currently using the system
     * @param um is the UserManager
     * @param tm is the TradeManager
     * @param im is the ItemManager
     * @param mm is the MeetingManager
     */
    public TradeSystem(String userId, UserManager um, TradeManager tm, ItemManager im, MeetingManager mm) {
        this.tm = tm;
        this.mm = mm;
        this.um = um;
        this.im = im;
        this.userId = userId;
        this.dts = new DisplayTradeHelper(userId, tm, im, um, mm);
        this.rts = new RequestTradeHelper(userId, tm, im, um, mm);
        this.dats = new DoActionTradeHelper(userId, tm, im, um, mm);
        this.sb = new SystemBuilder(userId);
    }

    /**
     * The method that runs the TradeSystem
     */
    @Override
    public void run() {
        boolean systemOpened;
        do {
            readFile();
            if (!userId.equalsIgnoreCase("@Guest") &&
                    !String.valueOf(um.statusByUserId(userId)).equals("GOOD")) {
                tp.getSelectionMenu("Bad");
                systemOpened = helperGeneralSwitch("BAD");
            } else {
                tp.getSelectionMenu("Good");
                systemOpened = helperGeneralSwitch("GOOD");
            }
            //updateFile();
        } while(systemOpened);
    }

    // General prompts display for everyone
    private boolean helperGeneralSwitch(String status) {
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                return false;
            case "1": // 1. Display Trades
                if (!isGuest()) {
                    dts.helperDisplaySelection();
                }
                return true;
            case "2": // 2. Display something recent
                if (!isGuest()) {
                    dts.helperDisplayRecent();
                }
                return true;
            default:
                return helperGoodSwitch(selection, status);
        }
    }

    // Specified prompts only for Good User to have access into it
    private boolean helperGoodSwitch(String selection, String status) {
        if (!isGuest() && status.equalsIgnoreCase("GOOD")) {
            switch (selection) {
                case "3": // Request a trade
                    if (!validIncompleteTrade(userId)) {
                        updateFile();
                        tp.violateIncompleteTrade();
                    } else if (!tm.checkTradeLimit(userId)) {
                        tp.violateTradeLimit();
                        updateFile();
                    } else {
                        rts.helperRequestTrade();
                    }
                    break;
                case "4": // Manage a trade
                    if (!availableActionTrade(userId).isEmpty()) {
                        dats.helperActionToTrade(availableActionTrade(userId));
                    } else {
                        tp.noTradeActionAvailable();
                    }
                    break;
                default:
                    tp.invalidInputMessage();
                    break;
            }
        } else {
            tp.invalidInputMessage();
        }
        return true;
    }

    private boolean validIncompleteTrade(String userId) {
        if (!tm.checkIncompleteTrade(userId)) {
            um.changeStatus(userId, UserStatus.FLAGGED);
            ug.updateUserInfo(um);
            return false;
        } else {
            return true;
        }
    }

    private List<String> availableActionTrade(String userId) {
        List<String> validActions = tm.availableActionTrade(userId);
        // Remove tradeId when the trade status is Accepted and the userId is not eligible to edit Meeting
        validActions.removeIf(tradeId -> String.valueOf(tm.getTradeStatusByTradeId(tradeId)).equals("ACCEPTED") &&
                !mm.usersTurn(tm.getMeetingIdByTradeId(tradeId), tm.getUserNum(tradeId, userId)));
        return validActions;
    }

    private boolean isGuest(){
        if (userId.equalsIgnoreCase("@Guest")) {
            up.needToLogin();
            return true;
        }
        return false;
    }

    private void updateFile() {
        ug.updateUserInfo(um);
        ig.updateItemInfo(im);
        tg.updateTradeInfo(tm);
        mg.updateMeetingInfo(mm);
    }

    private void readFile() {
        im = sb.buildItemManager();
        tm = sb.buildTradeManager();
        um = sb.buildUserManager();
        mm = sb.buildMeetingManager();
    }


}


