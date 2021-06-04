package ru.netology.web.data;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class UserInfo {

        private String login;
        private String password;
        private List<CardInfo> cardList;

    }

    public static class CardInfo {

        private String cardNumber;
        private long cardBalance;
        private CardInfo(String cardNumber, long cardBalance) {
            this.cardNumber = cardNumber;
            this.cardBalance = cardBalance;
        }

    }

    public static UserInfo getUserInfo() {

        UserInfo user = new UserInfo("vasya", "qwerty123", new ArrayList<>());

        user.cardList.add(new CardInfo("5559 0000 0000 0001", 0));
        user.cardList.add(new CardInfo("5559 0000 0000 0002", 0));
        return user;

    }

    public static String getCardNumber(UserInfo user, int index) {

        return user.cardList.get(index).cardNumber;

    }

    public static String getWrongCardNumber(UserInfo user) {

        return user.cardList.get(0).cardNumber.substring(4);

    }

    public static void setUserCardBalance(UserInfo user, int cardIndex, long cardBalance) {

        user.cardList.get(cardIndex).cardBalance = cardBalance;

    }

    public static long getUserCardBalance(UserInfo user, String cardNumber) {

        for (CardInfo cardInfo : user.cardList) {
            if (cardInfo.cardNumber.equals(cardNumber)) {
                return cardInfo.cardBalance;
            }
        }
        return 0;

    }

    public static long getValidTransferAmount(UserInfo user, String cardNumber) {

        for (CardInfo cardInfo : user.cardList) {
            if (cardInfo.cardNumber.equals(cardNumber)) {
                return cardInfo.cardBalance / 10;
            }
        }
        return 0;

    }

    public static long getInValidTransferAmount(UserInfo user, String cardNumber) {

        for (CardInfo cardInfo : user.cardList) {
            if (cardInfo.cardNumber.equals(cardNumber)) {
                return (long) (cardInfo.cardBalance * 1.1);
            }
        }
        return 0;

    }

    @Value
    public static class VerificationCode {

        private String code;

    }

    public static VerificationCode getVerificationCodeFor(UserInfo userInfo) {

        return new VerificationCode("12345");

    }

}
