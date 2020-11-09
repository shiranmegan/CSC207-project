package controllers.adminSystem;

import controllers.IController;
import gateways.ThresholdGateway;
import presenters.AdminPresenter;
import controllers.MenuSelector;

import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for editing threshold in the system
 * Only Admin can access this
 *
 * ap is the Admin Presenter
 * tg is the Gateway for threshold
 * thresholds it the list of integers containing thresholds
 */

public class ManageThresholdSystem extends MenuSelector implements IController {
    private final AdminPresenter ap = new AdminPresenter();
    private final ThresholdGateway tg = new ThresholdGateway();

    private final List<Integer> thresholds;

    /**
     * Constructor for the manage the threshold system
     * @param thresholds list of integers of thresholds
     */
    public ManageThresholdSystem(List<Integer> thresholds) {
        this.thresholds = thresholds;
    }

    /**
     * run the manage threshold system
     */
    @Override
    public void run() {
        boolean systemOpened = true;
        do {
            ap.adminManageThresholdMenu();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                // Note: refer to ThresholdGateway for the index for each threshold
                case "0": //"0. Main Menu"
                    systemOpened = false;
                    break;
                case "1": //"1. Edit Meeting Cancellation Threshold"
                    ap.thresholdEditPrompt("Meeting Cancellation Threshold");
                    editThreshold(0);  // the lowest acceptable value for CancelThreshold is 1
                    break;
                case "2": //"2. Edit Weekly Trade Limit"
                    ap.thresholdEditPrompt("Weekly Trade Limit");
                    editThreshold(1);  // the lowest acceptable value for WeeklyTradeLimit is 1
                    break;
                case "3": //"3. Edit Borrowed-Lent Difference Threshold"
                    ap.thresholdEditPrompt("Borrowed-Lent Difference Threshold");
                    editThreshold(2);  // the lowest acceptable value for BorrowDifference is 1
                    break;
                case "4": //"4. Edit Meeting Edit Threshold"
                    ap.thresholdEditPrompt("Meeting Edit Threshold");
                    editThreshold(3);  // the lowest acceptable value for EditThershold is 1
                    break;
                case "5": //"5. Edit Minimum Password Length"
                    ap.thresholdEditPrompt("Minimum Password Length");
                    editThreshold(4);  // the lowest acceptable value for Password limit is 1
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
            updateFile();
        } while (systemOpened);
    }

    private void editThreshold(int thresholdIndex) {
        int newThreshold = getIntInput(getMaxNum());
        if (newThreshold > 0) {
            thresholds.set(thresholdIndex, newThreshold);
            ap.thresholdEditSuccessMessage();
        }
    }

    private void updateFile() {
        tg.updateThresholdInfo(thresholds);
    }
}
