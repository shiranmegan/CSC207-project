package gateways;

import java.util.*;

import entities.Admin;
import usecases.AdminManager;

/**
 * AdminGateway is a Gateway class for Admin
 */
public class AdminGateway extends Gateway {

    public AdminGateway() {

    }

    /**
     * Updates AdminManager adm to admin.ser
     * @param adm AdminManager to be updated
     */
    public void updateAdminInfo(AdminManager adm) {
        String fileName = "src/database/Admin.ser";
        List<Admin> serList = new ArrayList<>();
        Map<String, Admin> admAdminIdToUser = adm.getAdminIdToUser();
        for (Map.Entry<String, Admin> entry: admAdminIdToUser.entrySet()){
            serList.add(entry.getValue());
        }
        updateInfo(serList, fileName);
    }

    /**
     * Get information from admin.ser
     * @return List of admins from .ser
     */
    @Override
    public List<Admin> getInfo() {
        return helperGetInfo("database/Admin.ser");
    }
}
