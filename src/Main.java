import controllers.SystemBuilder;

import java.io.FileNotFoundException;

/**
 * The class that contains the main method
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        SystemBuilder bs = new SystemBuilder("");
        bs.buildSystem("LoginSystem").run();
    }
}
