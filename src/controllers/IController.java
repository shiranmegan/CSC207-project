package controllers;

import java.io.FileNotFoundException;

/**
 * An interface for all Systems
 */
public interface IController {

    /**
     * Run system
     * @throws FileNotFoundException when file is not found
     */
    void run() throws FileNotFoundException;
}
