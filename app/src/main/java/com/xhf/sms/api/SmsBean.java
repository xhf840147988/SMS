package com.xhf.sms.api;


public class SmsBean {
    String name;
    String phone;
    String imei;
    String ip;

    public SmsBean(String name, String phone, String imei, String ip) {
        this.name = name;
        this.phone = phone;
        this.imei = imei;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
