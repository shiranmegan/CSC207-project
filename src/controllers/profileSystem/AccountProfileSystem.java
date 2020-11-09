package controllers.profileSystem;

import controllers.IController;
import controllers.MenuSelector;
import controllers.SystemBuilder;
import entities.UserStatus;
import presenters.AdminPresenter;
import presenters.LoginPresenter;
import presenters.UserPresenter;
import usecases.AdminManager;
import usecases.IAccountManager;
import usecases.TradeManager;
import usecases.UserManager;

import gateways.*;

import java.util.*;

/**
 * AccountProfileSystem is to manage the corresponding Account
 *
 * up is User Presenter
 * lp is Login Presenter
 * ap is Admin Presenter
 * iam is the IAccountManager
 * ug is the Gateway for User
 * ag is the Gateway for Admin
 * tg is the Gateway for Trade
 * sb is to build Systems
 * adm is AdminManager
 * um is UserManager
 * tm is TradeManager
 * userId is the accountId accessing the AccountProfileSystem
 * passwordLengthLimit is the minimum password length
 */
public abstract class AccountProfileSystem extends MenuSelector implements IController {

    private final UserPresenter up = new UserPresenter();
    private final LoginPresenter lp = new LoginPresenter();
    private final AdminPresenter ap = new AdminPresenter();
    private IAccountManager iam;

    private final UserGateway ug = new UserGateway();
    private final AdminGateway ag = new AdminGateway();
    private final TradeGateway tg = new TradeGateway();

    private final SystemBuilder sb;

    private AdminManager adm;
    private UserManager um;
    private TradeManager tm;
    private final String userId;
    private final int passwordLengthLimit;

    /**
     * Constructor on the Account Profile system
     * @param userId the accountId accessing the AccountProfileSystem
     * @param adm AdminManager
     * @param um UserManager
     * @param tm TradeManager
     * @param passwordLengthLimit minimum password length
     */
    public AccountProfileSystem(String userId, AdminManager adm, UserManager um, TradeManager tm,
                                int passwordLengthLimit) {
        this.userId = userId;
        this.adm = adm;
        this.um = um;
        this.tm = tm;
        this.passwordLengthLimit = passwordLengthLimit;
        this.sb = new SystemBuilder(userId);
    }

    /**
     * Run the AccountProfileSystem
     */
    public void run() {
        boolean systemOpened = true;
        do {
            readFile();
            switchAccountManager();
            presentMainMenu();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    systemOpened = false;
                    break;
                case "1": //view profile
                    viewProfile();
                    break;
                case "2": //1. Change my username"
                    changeUsername();
                    break;
                case "3": //2. Change my password
                    changePassword();
                    break;
                default:
                    specificMenu(selection);
                    break;
            }
            updateFile();
        } while (systemOpened);
    }

    /**
     * Present the correct Main Menu for each subclass
     */
    public abstract void presentMainMenu();

    /**
     * Specified menu for each subclass
     * @param selection the input from user
     */
    public abstract void specificMenu(String selection);

    private void updateFile() {
        ug.updateUserInfo(um);
        ag.updateAdminInfo(adm);
        tg.updateTradeInfo(tm);
    }

    private void readFile() {
        um = sb.buildUserManager();
        adm = sb.buildAdminManager();
        tm = sb.buildTradeManager();
    }

    /**
     * View profile of a given Account
     */
    protected void viewProfile() {
        if (!userId.equalsIgnoreCase("@Guest")) {
            up.viewProfile(iam.findAccountById(userId));
        } else {
            up.needToLogin();
        }
    }

    /**
     * Change the username of a given account
     */
    protected void changeUsername() {
        if (userId.equalsIgnoreCase("@guest")) {
            up.needToLogin();
            return;
        }
        String newUsername = getNewUsername();
        if (!newUsername.equals(getReturnStr())) {
            iam.changeUsername(userId, newUsername);
            ap.updateSuccessMessage();
        }
    }

    /**
     * Change the password of a given account
     */
    protected void changePassword() {
        if (userId.equalsIgnoreCase("@guest")) {
            up.needToLogin();
            return;
        }
        String password = getNewPassword();
        if (!password.equalsIgnoreCase(getReturnStr())) {
            iam.changePassword(userId, password);
            ap.updateSuccessMessage();
            }
    }

    private String getNewUsername() {
        lp.newUsernamePrompt();
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        while ((iam.checkUsername(username) || containsInvalidString(username) &&
                !username.equalsIgnoreCase(getReturnStr()))) {
            ap.invalidNewUsernameMessage();
            username = input.nextLine();
        }
        return username;
    }

    private String getNewPassword() {
        lp.newPasswordPrompt(this.passwordLengthLimit);
        Scanner input = new Scanner(System.in);
        String password = input.nextLine();
        while ((password.length() < this.passwordLengthLimit || containsInvalidString(password)) &&
                !password.equalsIgnoreCase(getReturnStr())) {
            lp.invalidPasswordMessage();
            password = input.nextLine();
        }
        return password;
    }

    /**
     * Switch the correct IAccountManager
     */
    public abstract void switchAccountManager();

    /**
     * Get UserPresenter
     * @return up
     */
    protected UserPresenter getUp() {
        return this.up;
    }

    /**
     * Get AdminManager
     * @return am
     */
    protected AdminManager getAm(){
        return this.adm;
    }

    /**
     * Set IAccountManager
     * @param iam set the correct Manager
     */
    protected void setIam(IAccountManager iam){
        this.iam = iam;
    }

    /**
     * Get UserManager
     * @return um
     */
    protected UserManager getUm() {
        return this.um;
    }

    /**
     * Get TradeManager
     * @return tm
     */
    protected TradeManager getTm() {
        return this.tm;
    }

    /**
     * Get AdminPresenter
     * @return ap
     */
    protected AdminPresenter getAp(){return this.ap;}

    /**
     * Get accountId
     * @return accountId
     */
    protected String getUserId() {
        return this.userId;
    }

    /**
     * Checks whether the account is Frozen
     * @return true iff frozen
     */
    protected boolean isFrozen(){
        return getUm().findAccountById(getUserId()).getStatus().equals(UserStatus.FROZEN);
    }
    
}
