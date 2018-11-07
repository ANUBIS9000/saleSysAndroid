package com.example.anubis9000.saleandorid.model;

/**
 * Created by anubis9000 on 2017/12/28.
 */

public class CallInfoVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    String sellerId;
    String sellerName;
    String schoolId;
    String linkMan;
    String linkManJob;
    String customerId;
    String productId;
    String customerPhone;
    String sellerPhone;
    String callOutStartTime;
    String fwdAnswerTime;
    String callEndTime;
    String waveName;
    String callId;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setFwdAnswerTime(String fwdAnswerTime) {
        this.fwdAnswerTime = fwdAnswerTime;
    }

    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    public void setWaveName(String waveName) {
        this.waveName = waveName;
    }

    public void setCallOutStartTime(String callOutStartTime) {
        this.callOutStartTime = callOutStartTime;
    }

    public String getCallOutStartTime() {
        return callOutStartTime;
    }

    public String getFwdAnswerTime() {
        return fwdAnswerTime;
    }

    public String getCallEndTime() {
        return callEndTime;
    }

    public String getWaveName() {
        return waveName;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public void setLinkManJob(String linkManJob) {
        this.linkManJob = linkManJob;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public String getLinkManJob() {
        return linkManJob;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}
