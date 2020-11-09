package gateways;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Gateway is an abstract class to read/save file
 */
public abstract class Gateway {

    public Gateway() {

    }

    private String getSerByFileName(String serName){
        String result;
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String pathName = currentWorkingDir.normalize().toString();
        if (serName.equals("database/User.ser")) {
            result = pathName + "/src/database/User.ser";
        } else if(serName.equals("database/Item.ser")) {
            result = pathName + "/src/database/Item.ser";
        }else if(serName.equals("database/Admin.ser")) {
            result = pathName + "/src/database/Admin.ser";
        }else if(serName.equals("database/Meeting.ser")){
            result = pathName + "/src/database/Meeting.ser";
        }else if(serName.equals("database/Threshold.ser")){
            result = pathName + "/src/database/Threshold.ser";
        }else{
            result = pathName + "/src/database/Trade.ser";
        }
        return result;
    }

    /**
     * Updates serList to fileName
     * @param serList list of T objects
     * @param fileName the fileName
     * @param <T> Generics type of the Object
     */
    public <T> void updateInfo(List<T> serList, String fileName) {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(serList);
            out.flush();
            out.close();
            file.close();
        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    /**
     * Helper to actually return the list of Object T
     * @param fileName the fileName
     * @param <T> Generics of any types that will be returned
     * @return list of Object T
     */
    public <T> List<T> helperGetInfo(String fileName) {
        List finalList = new ArrayList();
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(getSerByFileName(fileName));
            ObjectInputStream in = new ObjectInputStream(file);
            finalList = (List) in.readObject();
            in.close();
            file.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return finalList;
    }

    /**
     * Get info from file
     * @param <T> Generics of any types of information that will be returned
     * @return list of the objects T
     */
    public abstract <T> List<T> getInfo();
}