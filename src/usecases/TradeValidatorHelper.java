package usecases;

import entities.Trade;
import entities.TradeStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeValidatorHelper {

    TradeValidatorHelper() {

    }

    boolean borrowDiffChecker(List<String> borrowTrade, List<String> lendTrade, int borrowDiff) {
        int borrowNum = borrowTrade.size();
        int lendNum = lendTrade.size();
        return borrowNum - lendNum < borrowDiff;
    }

    boolean tradeLimitChecker(String userId, Map<String, Trade> hmTrade, int weeklyTradeLimit) {
        int numTradeOneWeek = tradesInOneWeek(userId, hmTrade).size();
        return numTradeOneWeek < weeklyTradeLimit;
    }

    private List<String> tradesInOneWeek(String userId, Map<String, Trade> hmTrade) {
        List<String> ls = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : hmTrade.entrySet()) {
            if (helperTradesInOneWeek(trade.getValue(), userId)) {
                LocalDate date1 = trade.getValue().getDateCreated();
                LocalDate date2 = LocalDate.now();
                long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                int diffDays = Integer.parseInt(String.valueOf(daysBetween));
                if (diffDays < 7) {
                    ls.add(trade.getKey());
                }
            }
        }
        return ls;
    }

    private boolean helperTradesInOneWeek(Trade trade, String userId) {
        if (trade.getTradeId().startsWith("@return")) {
            return false;
        } else if (trade.getStatus().equals(TradeStatus.REJECTED)) {
            return false;
        } else if (trade.getRequester().equals(userId)) {
            // if the person requests the Trade (and not rejected)
            return true;
        } else if (trade.getReceiver().equals(userId) && !trade.getStatus().equals(TradeStatus.REQUESTED)) {
            return true;
        } else {
            return false;
        }
    }

    boolean incompleteTradeChecker(List<String> cancelledTrade, int cancelledThreshold) {
        return cancelledTrade.size() < cancelledThreshold;
    }

    List<String> availableActionTrade(List<Trade> allTradeByUserId, String userId) {
        List<String> al = new ArrayList<>();
        for (Trade trade: allTradeByUserId) {
            if (validActionTrade(trade, userId)) {
                al.add(trade.getTradeId());
            }
        }
        return al;
    }

    private boolean validActionTrade(Trade trade, String userId) {
        if (trade.getStatus().equals(TradeStatus.REQUESTED) && trade.getRequester().equals(userId)) {
            return false;
        } else if (trade.getStatus().equals(TradeStatus.REJECTED)) {
            return false;
        } else if (trade.getStatus().equals(TradeStatus.CANCELLED)) {
            return false;
        } else if (trade.getStatus().equals(TradeStatus.COMPLETED)) {
            return false;
        } else if (trade.getStatus().equals(TradeStatus.REQUESTERCONFIRMED) && trade.getReceiver().equals(userId)) {
            return true;
        } else if (trade.getStatus().equals(TradeStatus.RECEIVERCONFIRMED) && trade.getRequester().equals(userId)) {
            return true;
        } else {
            return true;
        }
    }

    boolean haveRequested(List<Trade> allTradeIdByUserId, String itemId, String userId) {
        for (Trade trade : allTradeIdByUserId) {
            // If the userId has requested the itemId previously and the trade has not been accepted/rejected
            if (trade.getStatus().equals(TradeStatus.REQUESTED) &&
                    trade.getRequester().equals(userId) &&
                    trade.getItemIds().contains(itemId)) {
                return true;
            }
        }
        return false;
    }

    boolean eligibleToConfirmTrade(Trade trade, String userId) {
        if (trade.getStatus().equals(TradeStatus.CONFIRMED)) {
            return true;
        } else if (trade.getStatus().equals(TradeStatus.REQUESTERCONFIRMED) && trade.getReceiver().equals(userId)) {
            return true;
        } else if (trade.getStatus().equals(TradeStatus.RECEIVERCONFIRMED) && trade.getRequester().equals(userId)) {
            return true;
        } else {
            return false;
        }
    }

    boolean eligibleToAcceptRejectTrade(Trade trade, String userId) {
        return trade.getStatus().equals(TradeStatus.REQUESTED) && trade.getReceiver().equals(userId);
    }
}
