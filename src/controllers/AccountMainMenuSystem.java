package controllers;

import java.io.FileNotFoundException;

/**
 * Abstract MainMenu for Account
 *
 * sb is to build Systems
 */
public abstract class AccountMainMenuSystem implements IController {
    private final SystemBuilder sb;

    /**
     * Constructor for the AccountMainMenuSystem
     * @param userId the accountId
     */
    public AccountMainMenuSystem(String userId) {
        this.sb = new SystemBuilder(userId);
    }

    /**
     * Run the System
     */
    public abstract void run() throws FileNotFoundException;

    /**
     * Get SystemBuilder
     * @return sb for SystemBuilder
     */
    protected SystemBuilder getSb() {
        return this.sb;
    }

}
