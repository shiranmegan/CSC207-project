package gateways;

import entities.*;
import usecases.TradeManager;

import java.util.*;

/**
 * TradeGateway is a Gateway for Trade
 */
public class TradeGateway extends Gateway {

    public TradeGateway() {

    }

    /**
     * Updates TradeManager tm to trade.ser
     * @param tm TradeManager to be updated
     */
    public void updateTradeInfo(TradeManager tm) {
        String fileName = "src/database/Trade.ser";
        List<Trade> serList = new ArrayList<>();
        Map<String, Trade> tmTradeIdToTrade = tm.getTradeIdToTrade();
        for (Map.Entry<String, Trade> entry: tmTradeIdToTrade.entrySet()){
            serList.add(entry.getValue());
        }
        updateInfo(serList, fileName);
    }

    /**
     * Get information from Trade.ser
     * @return list of Trades from .ser file
     */
    @Override
    public List<Trade> getInfo() {
        return helperGetInfo("database/Trade.ser");
    }

}