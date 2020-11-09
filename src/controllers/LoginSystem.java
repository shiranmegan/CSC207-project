package controllers;
import gateways.*;
import presenters.LoginPresenter;
import usecases.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LoginSystem is a System to login
 *
 * passwordLengthLimit is the minimum password length
 * lp is the presenter
 * ug is the gateway for User
 * adm is the gateway for Admin
 * um is the use case for User
 * adm is the use case for Admin
 * iam is the abstract use case for Account
 * ic is to switch mainmenus
 * bs is to build Systems
 */
public class LoginSystem extends MenuSelector implements IController {
    private final int passwordLengthLimit;
    
    private final LoginPresenter lp = new LoginPresenter();

    private final UserGateway ug = new UserGateway();
    private final AdminGateway ag = new AdminGateway();

    private UserManager um;
    private AdminManager adm;

    private IAccountManager iam;
    private IController ic;
    private SystemBuilder sb;

    /**
     * Constructor for LoginSystem
     * @param um UserManager to be used
     * @param adm AdminManager to be used
     * @param passwordLengthLimit current minimum length for the password
     */
    public LoginSystem(UserManager um, AdminManager adm, int passwordLengthLimit) {
        this.um = um;
        this.adm = adm;
        this.passwordLengthLimit = passwordLengthLimit;
        this.sb = new SystemBuilder("@Guest");
    }

    /**
     * The method that runs the LoginSystem
     * @throws FileNotFoundException when file is not found
     */
    @Override
    public void run() throws FileNotFoundException {
        boolean systemOpened = true;
        Scanner input = new Scanner(System.in);
        do {
            readFile();
            lp.mainMenuOptions();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    lp.closingMessage();
                    systemOpened = false;
                    break;
                case "1": // "1. Register"
                    registerMenu();
                    updateFile();
                    break;
                case "2": // "2. Login"
                    loginMenu();
                    break;
                default:
                    lp.invalidInputMessage();
                    run();
                    break;
            }
        } while (systemOpened);
    }

    private void registerMenu() {
        lp.registerOptions();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // "1. Register as a user"
                userRegisterHelper();
                break;
            default:
                lp.invalidInputMessage();
                registerMenu();
                break;
        }
    }

    private void userRegisterHelper() {
        switchAccountManager("User");
        lp.userRegistrationMessage();

        String username = getNewUsername();
        if (username.equals(getReturnStr())) {
            registerMenu();
        } else {
            proceedPassword(username);
        }
    }

    private void proceedPassword(String username) {
        String password = getNewPassword();
        if (password.equals(getReturnStr())) {
            registerMenu();
        } else {
            proceedUser(username, password);
        }
    }

    private void proceedUser(String username, String password) {
        String email = getEmail();
        if (email.equals(getReturnStr())) {
            registerMenu();
        } else {
            String address = getAddress();
            if (address.equals(getReturnStr())) {
                registerMenu();
            } else {
                um.createNewUser(username, password, email, address);
                lp.registrationResultMessage();
            }
        }
    }

    private String getNewUsername() {
        lp.newUsernamePrompt();
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        while ((um.checkUsername(username) || containsInvalidString(username))
                && !username.equals(getReturnStr())) {
            lp.usernameHasBeenTakenPrompt();
            username = input.nextLine();
        }
        return username;
    }

    private String getNewPassword() {
        lp.newPasswordPrompt(this.passwordLengthLimit);
        Scanner input = new Scanner(System.in);
        String password = input.nextLine();
        while ((password.length() < this.passwordLengthLimit || containsInvalidString(password))
                && !password.equals(getReturnStr())) {
            lp.invalidPasswordMessage();
            password = input.nextLine();
        }
        return password;
    }

    private String getEmail() {
        lp.generalPromptMessage("email");
        Scanner input = new Scanner(System.in);
        String email = input.nextLine();
        String regex = "^(\\w+)[\\.\\w+]*@(\\w{2,6})[\\.\\w+]+$";
        Pattern emailPattern = Pattern.compile(regex);
        Matcher emailMatcher = emailPattern.matcher(email);
        while ((!emailMatcher.matches() || containsInvalidString(email))
                && !email.equals(getReturnStr())) {
            lp.invalidInputMessage();
            email = input.nextLine();
            emailMatcher = emailPattern.matcher(email);
        }
        return email;
    }

    private String getAddress() {
        lp.generalPromptMessage("country");
        String country = getCountry();

        if (country.equals(getReturnStr())) {
            return country;
        }

        lp.generalPromptMessage("city");
        String city = getCity();

        if (city.equals(getReturnStr())) {
            return city;
        } else {
            return city + "-" + country;
        }
    }

    private String getCountry() {
        Scanner input = new Scanner(System.in);
        String country = input.nextLine();
        while (containsInvalidString(country) && !country.equals(getReturnStr())) {
            lp.invalidInputMessage();
            country = input.nextLine();
        }
        return country;
    }

    private String getCity() {
        Scanner input = new Scanner(System.in);
        String city = input.nextLine();
        while (containsInvalidString(city) && !city.equals(getReturnStr())) {
            lp.invalidInputMessage();
            city = input.nextLine();
        }
        return city;
    }

    private void loginMenu() throws FileNotFoundException{
        lp.loginOptions();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;
            case "1": // "1. Login as Admin"
                lp.generalLoginMessage("Admin");
                loginHelper("ADMIN");
                break;
            case "2": // "2. Login as User"
                lp.generalLoginMessage("User");
                loginHelper("USER");
                break;
            case "3":
                loginHelper("GUEST");
                break;
            default:
                lp.invalidInputMessage();
                loginMenu();
                break;
        }
    }

    private void loginHelper(String option) throws FileNotFoundException {
        if (option.equalsIgnoreCase("GUEST")) {
            switchSystem(option, "@guest");
            this.ic.run();
        } else {
            loginUsernamePassword(option);
        }
    }

    private void loginUsernamePassword(String option) throws FileNotFoundException {
        switchAccountManager(option);
        lp.generalPromptMessage("username");
        String username = getUsername();
        if (username.equals(getReturnStr())) {
            loginMenu();
        } else {
            lp.generalPromptMessage("password");
            String password = getPassword(username);
            if (password.equals(getReturnStr())) {
                loginMenu();
            } else {
                switchSystem(option, username);
                this.ic.run();
            }
        }
    }

    private String getUsername() {
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        while (invalidUsername(username)) {
            lp.usernameNotFoundPrompt();
            username = input.nextLine();
        }
        return username;
    }

    private boolean invalidUsername(String username) {
        if (!iam.checkUsername(username)) {
            return !username.equals(getReturnStr());
        } else {
            return false;
        }
    }

    private String getPassword(String username) {
        Scanner input = new Scanner(System.in);
        String password = input.nextLine();
        while (invalidPassword(username, password)) {
            if (password.equalsIgnoreCase(getReturnStr())) return getReturnStr();
            lp.invalidPasswordMessage();
            password = input.nextLine();
        }
        return password;
    }

    private boolean invalidPassword(String username, String password) {
        if (!iam.checkPassword(username, password)) {
            return !password.equals(getReturnStr());
        } else {
            return false;
        }
    }

    private void switchAccountManager(String option) {
        if (option.equalsIgnoreCase("ADMIN")) {
            iam = adm;
        } else if (option.equalsIgnoreCase("USER") || option.equalsIgnoreCase("GUEST")){
            iam = um;
        }
    }

    private void switchSystem (String option, String username) {
        if (option.equals("ADMIN")) {
            this.sb = new SystemBuilder(adm.adminIdByUsername(username));
            this.ic = sb.buildSystem("Admin");
        } else if (option.equalsIgnoreCase("user")) {
            this.sb = new SystemBuilder(um.userIdByUsername(username));
            this.ic = sb.buildSystem("User");
        } else {
            this.sb = new SystemBuilder("@guest");
            this.ic = sb.buildSystem("User");
        }
    }

    private void updateFile() {
        ug.updateUserInfo(um);
        ag.updateAdminInfo(adm);
    }

    private void readFile() {
        um = sb.buildUserManager();
        adm = sb.buildAdminManager();
    }
}
