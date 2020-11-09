package controllers.profileSystem;

import controllers.IController;
import entities.UserStatus;
import usecases.AdminManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * UserProfileSystem is for User's to access user's profile
 */
public class UserProfileSystem extends AccountProfileSystem implements IController {

    /**
     * Constructor for the user profile system
     * @param userId the userId accessing the UserProfileSystem
     * @param adm AdminManager
     * @param um UserManager
     * @param tm TradeManager
     * @param passwordLengthLimit minimum password length
     */
    public UserProfileSystem(String userId, AdminManager adm, UserManager um, TradeManager tm,
                             int passwordLengthLimit) {
        super(userId, adm, um, tm, passwordLengthLimit);
        //switchAccountManager("USER");
    }

    /**
     * Present the main menu for the UserManageProfile
     */
    @Override
    public void presentMainMenu(){
        getUp().userManageProfile();
    }

    /**
     * Specific menu for the user profile drop down
     * @param selection input from the user
     */
    @Override
    public void specificMenu(String selection){
        switch (selection) {
            case "4": // Change my city address
                if (isGuest()) return;
                changeCityAddress();
                break;
            case "5": // Change my email address
                if (isGuest()) return;
                changeEmail();
                break;
            case "6": // Change my status
                if (isGuest()) return;
                changeStatusHelper();
                break;
            case "7": //request to be unfrozen
                if (isGuest()) return;
                unfreezeHelper(isFrozen());
                break;
            default:
                getUp().invalidInputMessage();
                break;
        }
    }

    private void changeStatusHelper() {
        if (getUm().findAccountById(getUserId()).getStatus().equals(UserStatus.GOOD)) {
            changeStatusVacation();
        } else if (getUm().findAccountById(getUserId()).getStatus().equals(UserStatus.VACATION)) {
            changeStatusGood();
        } else {
            getUp().unavailableOption();
            new Scanner(System.in).nextLine();
        }
    }

    private void unfreezeHelper(boolean isFrozen) {
        if (isFrozen) {
            requestUnfreeze();
        } else {
            getUp().unavailableOption();
        }
    }

    private boolean isGuest(){
        if (getUserId().equalsIgnoreCase("@guest")) {
            getUp().needToLogin();
            return true;
        }
        return false;
    }

    private void changeCityAddress() {
        if (!getTm().availableActionTrade(getUserId()).isEmpty()) {
            getUp().onGoingTradeWarningMessage();
            getUp().promptYesNo();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine().toUpperCase();
            switch (selection) {
                case "Y":
                    helperChangeCityAddress();
                    break;
                case "N":
                    break;
                default:
                    getUp().invalidInputMessage();
                    break;
            }
        } else {
            helperChangeCityAddress();
        }
    }

    private void helperChangeCityAddress() {
        getUp().generalPromptMessage("new country");
        String country = getCountry();

        if (!country.equals(getReturnStr())) {
            getUp().generalPromptMessage("new city");
            String city = getCity();

            if (!city.equals(getReturnStr())) {
                String address = city + "-" + country;
                getUm().findAccountById(getUserId()).setCityAddress(address);
                getUp().generalSuccessfulMessage("address");
            }
        }
    }

    private String getCountry() {
        Scanner input = new Scanner(System.in);
        String country = input.nextLine();
        while (containsInvalidString(country) && !country.equals(getReturnStr())) {
            getUp().invalidInputMessage();
            country = input.nextLine();
        }
        return country;
    }

    private String getCity() {
        Scanner input = new Scanner(System.in);
        String city = input.nextLine();
        while (containsInvalidString(city) && !city.equals(getReturnStr())) {
            getUp().invalidInputMessage();
            city = input.nextLine();
        }
        return city;
    }

    private void changeEmail() {
        getUp().generalPromptMessage("new email address");
        Scanner input = new Scanner(System.in);
        String email = input.nextLine();
        Pattern emailPattern = Pattern.compile("^(\\w+)[\\.\\w+]*@(\\w{2,6})[\\.\\w+]+$", Pattern.CASE_INSENSITIVE);
        while (!emailPattern.matcher(email).find()) {
            if (email.equals(getReturnStr())) return;
            getUp().invalidInputMessage();
            getUp().goBackToUpperMenu();
            email = input.nextLine();
        }
        getUm().findAccountById(getUserId()).setEmail(email);
        getUp().generalSuccessfulMessage("email");
    }

    private void changeStatusGood() {
        getUm().changeStatus(getUserId(), UserStatus.GOOD);
        getUp().changeStatusMessage("Available");
    }

    private void changeStatusVacation() {
        if (!getTm().availableActionTrade(getUserId()).isEmpty()) {
            getUp().onGoingTradeWarningMessage();
            getUp().promptYesNo();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine().toUpperCase();
            switch (selection) {
                case "Y":
                    getUm().changeStatus(getUserId(), UserStatus.VACATION);
                    getUp().changeStatusMessage("Vacation");
                    break;
                case "N":
                    break;
                default:
                    getUp().invalidInputMessage();
                    break;
            }
        } else {
            getUm().changeStatus(getUserId(), UserStatus.VACATION);
            getUp().changeStatusMessage("Vacation");
        }
    }

    private void requestUnfreeze() {
        boolean requestSent = getUm().unfreezeRequest(getUserId());
        if (requestSent) {
            getUp().unfreezeRequestSentMessage();
        } else {
            getUp().unfreezeRequestAlreadySentMessage();
        }
    }

    /**
     * To switch IAccountManager into UserManager
     */
    @Override
    public void switchAccountManager(){
        setIam(getUm());
    }
}
