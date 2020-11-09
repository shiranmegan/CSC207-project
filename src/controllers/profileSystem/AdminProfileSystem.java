package controllers.profileSystem;

import controllers.IController;
import usecases.AdminManager;
import usecases.TradeManager;
import usecases.UserManager;

/**
 * AdminProfileSystem is for Admin to access admin's profile
 */
public class AdminProfileSystem extends AccountProfileSystem implements IController {

    /**
     * Constructor for the admin profile system
     * @param userId adminId
     * @param adm AdminManager
     * @param um UserManager
     * @param tm TradeManager
     * @param passwordLengthLimit minimum password length
     */
    public AdminProfileSystem(String userId, AdminManager adm, UserManager um, TradeManager tm,
                              int passwordLengthLimit) {
        super(userId, adm, um, tm, passwordLengthLimit);
    }

    /**
     * Present the main menu for the Admin profile system
     */
    @Override
    public void presentMainMenu(){
        getAp().adminManageProfileMenu();
    }

    /**
     * Present additional menu for Admin
     * @param selection the input entered by user
     */
    @Override
    public void specificMenu(String selection) {
        getUp().invalidInputMessage();
    }

    /**
     * Switch the IAccountManager to AdminManager
     */
    @Override
    public void switchAccountManager(){
        setIam(getAm());
    }

}
