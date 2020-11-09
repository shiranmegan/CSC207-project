package controllers;
import controllers.ItemSystem.ManageItemSystem;
import controllers.ItemSystem.UserItemSystem;
import controllers.adminSystem.ManageAdminSystem;
import controllers.profileSystem.AdminProfileSystem;
import controllers.adminSystem.ManageThresholdSystem;
import controllers.profileSystem.UserProfileSystem;
import controllers.tradeSystem.TradeSystem;
import controllers.userSystem.ManageUserSystem;
import controllers.userSystem.UserViewerSystem;
import gateways.*;
import usecases.*;

import java.util.ArrayList;
import java.util.List;

/**
 * SystemBuilder is to build Systems
 *
 * um is the UserManager
 * am is the AdminManager
 * tm is the TradeManager
 * im is the ItemManager
 * mm is the MeetingManager
 * threshold is the list of integer thresholds
 * userId is the accountId
 */
public class SystemBuilder {
    private UserManager um;
    private AdminManager am;
    private TradeManager tm;
    private ItemManager im;
    private MeetingManager mm;
    private List<Integer> threshold;
    private String userId;

    /**
     * Constructor of the SystemBuilder
     * @param userId accountId of any Accounts
     */
    public SystemBuilder(String userId) {
        ThresholdGateway thg = new ThresholdGateway();
        this.threshold = thg.getInfo();
        this.userId = userId;
    }

    /**
     * Build the User Manager
     * @return userManager
     */
    public UserManager buildUserManager(){
        UserGateway ug = new UserGateway();
        this.um = new UserManager(ug.getInfo());
        return this.um;
    }

    /**
     * Build the Admin Manager
     * @return AdminManager
     */
    public AdminManager buildAdminManager(){
        AdminGateway ag = new AdminGateway();
        this.am = new AdminManager(ag.getInfo());
        return this.am;
    }

    /**
     * Build the TradeManager
     * @return TradeManager
     */
    public TradeManager buildTradeManager(){
        TradeGateway tg = new TradeGateway();
        List<Integer> al = new ArrayList<>();
        al.add(this.threshold.get(0));
        al.add(this.threshold.get(1));
        al.add(this.threshold.get(2));
        this.tm = new TradeManager(al, tg.getInfo());
        return this.tm;
    }

    /**
     * Build the ItemManager
     * @return ItemManager
     */
    public ItemManager buildItemManager(){
        ItemGateway ig = new ItemGateway();
        this.im = new ItemManager(ig.getInfo());
        return this.im;
    }

    /**
     * Build the MeetingManager
     * @return MeetingManager
     */
    public MeetingManager buildMeetingManager(){
        MeetingGateway mg = new MeetingGateway();
        this.mm = new MeetingManager(this.threshold.get(3), mg.getInfo());
        return this.mm;
    }

    /**
     * Build the system
     * @param s The System ordered to be built
     * @return IController
     */
    public IController buildSystem(String s) {
        buildUserManager();
        buildAdminManager();
        buildItemManager();
        buildMeetingManager();
        buildTradeManager();
        int passwordLengthLimit = this.threshold.get(4);
        if (s.equalsIgnoreCase("User")) {
            return new UserMainMenuSystem(userId);
        } else if (s.equalsIgnoreCase("Admin")) {
            return new AdminMainMenuSystem(userId);
        } else if(s.equalsIgnoreCase("ManageAdminSystem")) {
            return new ManageAdminSystem(userId, am, passwordLengthLimit);
        } else if (s.equalsIgnoreCase("ManageThreshold")) {
            return new ManageThresholdSystem(threshold);
        } else if (s.equalsIgnoreCase("ManageUserProfile")) {
            return new UserProfileSystem(userId, am, um, tm, passwordLengthLimit);
        } else if (s.equalsIgnoreCase("ManageAdminProfile")) {
            return new AdminProfileSystem(userId, am, um, tm, passwordLengthLimit);
        }
        else if (s.equalsIgnoreCase("LoginSystem")) {
            return new LoginSystem(this.um, this.am, passwordLengthLimit);
        }
        else if (s.equalsIgnoreCase("TradeSystem")) {
            return new TradeSystem(userId, um, tm, im, mm);
        }
        else if (s.equalsIgnoreCase("UserItemSystem")) {
            return new UserItemSystem(userId, um, im, tm);
        }
        else if (s.equalsIgnoreCase("ManageItemSystem")) {
            return new ManageItemSystem(userId, um, im, tm);
        }
        else if (s.equalsIgnoreCase("UserViewerSystem")) {
            return new UserViewerSystem(userId, um, im, tm);
        }
        else if (s.equalsIgnoreCase("ManageUserSystem")) {
            return new ManageUserSystem(userId, um, im, tm);
        }
        else {
            return new LoginSystem(this.um, this.am, passwordLengthLimit);
        }
    }
}
