package com.itmifen.utility.annotation.entityvalid;

/**
 * @author
 */
public class ValidResultEntity {


    /**
     * 验证结果
     */
    private boolean succeed;


    /**
     * 验证结果说明
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }
}
