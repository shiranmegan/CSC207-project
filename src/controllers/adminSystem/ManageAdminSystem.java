package controllers.adminSystem;

import controllers.IController;
import controllers.MenuSelector;
import controllers.SystemBuilder;
import gateways.AdminGateway;
import presenters.AdminPresenter;
import usecases.*;

import java.util.Scanner;

/**
 * This class is responsible for displaying all admins and register a new admin;
 * This class should only be accessible by an Admin
 *
 * adm is the AdminManager
 * ag is the Gateway for Admin
 * ap is the Admin Presenter
 * sb is to build the System
 * passwordLengthLimit is the minimum length for passwords
 */

public class ManageAdminSystem extends MenuSelector implements IController {
    private AdminManager adm;
    private final AdminGateway ag = new AdminGateway();
    private final AdminPresenter ap = new AdminPresenter();
    private SystemBuilder sb;
    private final int passwordLengthLimit;

    /**
     * Constructor of ManageAdminSystem
     * @param userId the adminId
     * @param adm the AdminManager
     * @param passwordLengthLimit the minimum length for passwords
     */
    public ManageAdminSystem(String userId, AdminManager adm, int passwordLengthLimit) {
        this.adm = adm;
        this.passwordLengthLimit = passwordLengthLimit;
        this.sb = new SystemBuilder(userId);
    }

    /**
     * Run the admin system
     */
    @Override
    public void run() {
        boolean systemOpened = true;
        do {
            readFile();
            ap.adminManageAdminMenu();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    systemOpened = false;
                    break;
                case "1": // 1. View all admins
                    viewAllAdmins();
                    break;
                case "2": // 2. Register new Admin
                    registerNewAdmin();
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
            updateFile();
        } while (systemOpened);
    }

    private void viewAllAdmins(){
        ap.getAdminsInfo(adm.getAccountList());
    }

    private void registerNewAdmin() {
        ap.newUsernamePrompt();
        String username = getNewUsername();
        if (username.equalsIgnoreCase(getReturnStr())) {
            ap.registrationTerminates();
        } else {
            ap.newPasswordPrompt();
            String password = getNewPassword();
            if (password.equalsIgnoreCase(getReturnStr())) {
                ap.registrationTerminates();
            } else {
                adm.addAdmin(username, password);
                ap.registrationSuccess();
            }
        }
    }

    private String getNewUsername() {
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        while ((adm.checkUsername(username) || containsInvalidString(username))
                && !username.equals(getReturnStr())) {
            ap.invalidNewUsernameMessage();
            username = input.nextLine();
        }
        return username;
    }

    private String getNewPassword() {
        Scanner input = new Scanner(System.in);
        String password = input.nextLine();
        while ((password.length() < this.passwordLengthLimit || containsInvalidString(password))
                && !password.equals(getReturnStr())) {
            ap.invalidNewPasswordMessage();
            password = input.nextLine();
        }
        return password;
    }


    private void updateFile() {
        ag.updateAdminInfo(adm);
    }

    private void readFile() {
        adm = sb.buildAdminManager();
    }

}
