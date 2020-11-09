package usecases;

import entities.Trade;
import entities.TradeStatus;

import java.util.*;

public class TradeInfoHelper {

    private Map<String, Trade> tradeIdToTrade;

    TradeInfoHelper() {

    }

    List<String> getTradeWithStatusAndTypeByUserId(String userId, String tradeStatus, String tradeType) {
        List<String> allTradeIds = getAllTradeIdByUserId(userId);
        List<String> returnList = new ArrayList<>();
        if (tradeStatus.equalsIgnoreCase("All")) {
            // if "All" is chosen
            for (String tradeId: allTradeIds) {
                updateReturnListTradeType(userId, tradeType, returnList, tradeId);
            }

        } else {
            // based on status
            TradeStatus status = statusConverter(tradeStatus);
            for (String tradeId : allTradeIds) {
                if (findTrade(tradeId).getStatus().equals(status)) {
                    updateReturnListTradeType(userId, tradeType, returnList, tradeId);
                }
            }
        }
        return returnList;
    }

    private void updateReturnListTradeType(String userId, String tradeType, List<String> returnList, String tradeId) {
        Trade trade = findTrade(tradeId);
        if (tradeType.equalsIgnoreCase("Borrow")) {
            if (trade.getRequester().equals(userId) && !tradeId.startsWith("@return") && trade.isOneWay()) {
                returnList.add(tradeId);
            }
        } else if (tradeType.equalsIgnoreCase("Lend")) {
            if (trade.getReceiver().equals(userId) && !tradeId.startsWith("@return") && trade.isOneWay()) {
                returnList.add(tradeId);
            }
        } else if (tradeType.equalsIgnoreCase("OneWay")) {
            if (trade.isOneWay() && !tradeId.startsWith("@return")) {
                returnList.add(tradeId);
            }
        } else if (tradeType.equalsIgnoreCase("TwoWay")) {
            if (!trade.isOneWay() && !tradeId.startsWith("@return")) {
                returnList.add(tradeId);
            }
        } else {
            returnList.add(tradeId);
        }
    }

    private TradeStatus statusConverter(String tradeStatus) {
        Map<String, TradeStatus> hm = new HashMap<>();
        for (TradeStatus status : TradeStatus.values()) {
            hm.put(String.valueOf(status), status);
        }
        return hm.get(tradeStatus);
    }

    private List<String> getAllTradeIdByUserId(String userId) {
        List<String> al = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : this.tradeIdToTrade.entrySet()) {
            if ((trade.getValue().getReceiver().equals(userId) ||
                    trade.getValue().getRequester().equals(userId))) {
                al.add(trade.getKey());
            }
        }
        return al;
    }

    Map<String, Trade> getAllWayTradeId(String whichWay) {
        Map<String, Trade> hmap = new HashMap<>();
        for (Map.Entry<String, Trade> trade : this.tradeIdToTrade.entrySet()) {
            addBasedOnWhichWay(whichWay, hmap, trade);
        }
        return hmap;
    }

    private void addBasedOnWhichWay(String whichWay, Map<String, Trade> hmap, Map.Entry<String, Trade> trade) {
        if (whichWay.equalsIgnoreCase("OneWay")) {
            if (trade.getValue().isOneWay()) {
                hmap.put(trade.getKey(), trade.getValue());
            }
        }
        else if (whichWay.equalsIgnoreCase("TwoWay")){
            if (!trade.getValue().isOneWay()) {
                hmap.put(trade.getKey(), trade.getValue());
            }
        }
    }

    List<String> getRecentItemsByUserId(String tradeType, String userId, int num) {
        List<String> usedList = getTradeWithStatusAndTypeByUserId(userId, "COMPLETED", tradeType);

        List<Trade> al = new ArrayList<>();
        // Does not include returning trade
        for (String tradeId : usedList) {
            if (!tradeId.startsWith("@return")) {
                al.add(findTrade(tradeId));
            }
        }
        List<String> returnAl = new ArrayList<>();
        al.sort(new TradeComparator());

        for (int i = al.size() - 1; i >= 0 && num > 0; i--, num--) {
            returnAl.addAll(al.get(i).getItemIds());
        }
        return returnAl;
    }

    List<String> getTopTradersByUserId(String userId, int num) {
        List<String> as = getAllTradeIdByUserId(userId);
        List<String> al = new ArrayList<>();
        for (String tradeId : as) {
            if (String.valueOf(findTrade(tradeId).getStatus()).equals("COMPLETED") && !tradeId.startsWith("@return")) {
                al.add(tradeId);
            }
        }
        List<String> returnAl = new ArrayList<>();

        Map<String, Integer> userToNumTrades = helperGetTopTraders(userId, al);
        helperChoosingTopTraders(num, returnAl, userToNumTrades);
        return returnAl;
    }

    private void helperChoosingTopTraders(int num, List<String> returnAl, Map<String, Integer> userToNumTrades) {
        while (returnAl.size() != num && !userToNumTrades.isEmpty()) {
            String rememberPerson = null;
            int rememberMaxTimes = 0;
            for (Map.Entry<String, Integer> trader : userToNumTrades.entrySet()) {
                if (rememberMaxTimes < trader.getValue()) {
                    rememberMaxTimes = trader.getValue();
                    rememberPerson = trader.getKey();
                }
            }
            returnAl.add(rememberPerson);
            userToNumTrades.remove(rememberPerson);
        }
    }

    private Map<String, Integer> helperGetTopTraders(String userId, List<String> al) {
        Map<String, Integer> userToNumTrades = new HashMap<>();
        for (String tradeId : al) {
            Trade trade = findTrade(tradeId);
            String person;

            // Gets the corresponding userId that is not the userId given
            if (trade.getRequester().equals(userId)) {
                person = trade.getReceiver();
            } else {
                person = trade.getRequester();
            }

            // Count number of occurrence of the partner in trade of userId
            if (userToNumTrades.containsKey(person)) {
                userToNumTrades.put(person, userToNumTrades.get(person) + 1);
            } else {
                userToNumTrades.put(person, 1);
            }
        }
        return userToNumTrades;
    }

    void setTradeIdToTrade(Map<String, Trade> tradeIdToTrade) {
        this.tradeIdToTrade = tradeIdToTrade;
    }

    private Trade findTrade(String tradeId) {
        return this.tradeIdToTrade.get(tradeId);
    }

    List<Trade> convertTradeIdsToTrades(List<String> listTradeIds) {
        List<Trade> al = new ArrayList<>();
        for (String tradeId : listTradeIds) {
            al.add(findTrade(tradeId));
        }
        return al;
    }

    int getUserNum(String tradeIdInput, String userId) {
        if ((findTrade(tradeIdInput)).getRequester().equals(userId)) {
            return 0;
        } else {
            return 1;
        }
    }

    private class TradeComparator implements Comparator<Trade> {

        /**
         * Compares which trade is more recent
         *
         * @param t1 trade1
         * @param t2 trade2
         * @return integer indicating whether the trade is created earlier or after
         */
        @Override
        public int compare(Trade t1, Trade t2) {
            return t1.getDateCreated().compareTo(t2.getDateCreated());
        }
    }
}