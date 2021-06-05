package cn.xinill.ttms.utils;

/**
 * @Author: Xinil
 * @Date: 2021/2/9 22:22
 */

public enum SMSTemplateID{

    LOG_ON(869021,"LOG_ON"),
    LOG_IN(869703,"LOG_IN"),
    UPDATE(869020,"UPDATE");

    private final int code;
    private final String desc;

    SMSTemplateID(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}