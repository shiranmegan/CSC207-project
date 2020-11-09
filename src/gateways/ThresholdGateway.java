package gateways;

import java.util.List;

/**
 * ThresholdGateway is a Gateway for integer of thresholds
 */
public class ThresholdGateway extends Gateway{

    public ThresholdGateway() {

    }

    /**
     * Updates threshold.ser based on thresholdList
     * @param thresholdList list of thresholds
     */
    public void updateThresholdInfo(List<Integer> thresholdList) {
        String fileName = "src/database/Threshold.ser";
        updateInfo(thresholdList, fileName);
    }

    /**
     * Get thresholds from threshold.ser
     * @return list of integers of threshold info
     */
    @Override
    public List<Integer> getInfo() {
        return helperGetInfo("database/Threshold.ser");
    }
}
