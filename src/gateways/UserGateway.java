package gateways;

import java.util.*;

import entities.User;
import usecases.UserManager;

/**
 * UserGateway is a Gateway for User
 */
public class UserGateway extends Gateway {

    public UserGateway() {

    }

    /**
     * Updates UserManager um to user.ser
     * @param um UserManager to be updated
     */
    public void updateUserInfo(UserManager um) {
        String fileName = "src/database/User.ser";
        List<User> serList = new ArrayList<>();
        Map<String, User> umUserIdToUser = um.getUserIdToUser();
        for (Map.Entry<String, User> entry: umUserIdToUser.entrySet()){
            serList.add(entry.getValue());
        }
        updateInfo(serList, fileName);
    }

    /**
     * Get information from user.ser
     * @return list of Users from user.ser
     */
    @Override
    public List<User> getInfo() {
        return helperGetInfo("database/User.ser");
    }
}
