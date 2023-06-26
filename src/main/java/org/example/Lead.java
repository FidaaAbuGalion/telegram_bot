package org.example;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.LinkedList;

public class Lead {

    private long chatId;
    private String name;
    private int supportStatus;

    public LinkedList<Integer> getTheNumberOfTheServicesYouUse() {
        return TheNumberOfTheServicesYouUse;
    }

    public long getChatId() {
        return chatId;
    }

    public void setTheNumberOfTheServicesYouUse(LinkedList<Integer> theNumberOfTheServicesYouUse) {
        TheNumberOfTheServicesYouUse = theNumberOfTheServicesYouUse;
    }

    public String getName() {
        return name;
    }

    private String phone;
    private LinkedList<Integer> TheNumberOfTheServicesYouUse ;

    public static final int SUPPORT_STATUS_UNKNOWN = 0;
    public static final int SUPPORT_STATUS_POSITIVE = 1;



    public static final int SUPPORT_STATUS_NEGATIVE = 2;

    public Lead(long chatId) {
        this.chatId = chatId;
        this.supportStatus = SUPPORT_STATUS_UNKNOWN;
    }

    public boolean isSupportStatusKnown() {
        return this.supportStatus != SUPPORT_STATUS_UNKNOWN ;
    }

    public void updateSupportStatus(String status){
        if (status.equals("Y")){
            this.supportStatus = SUPPORT_STATUS_POSITIVE;
        }else if (status.equals("N")){
            this.supportStatus = SUPPORT_STATUS_NEGATIVE;
        }
    }

    public boolean isPositive(){
        return this.supportStatus == SUPPORT_STATUS_POSITIVE;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}