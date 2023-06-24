package org.example;

public class Lead {

    private long chatId;
    private String name;
    private int supportStatus;
    private String phone;

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
