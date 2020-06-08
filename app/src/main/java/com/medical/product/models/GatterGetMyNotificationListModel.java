package com.medical.product.models;

public class GatterGetMyNotificationListModel {

    private String id,user_id,vendor_id,other_id,sender,receiver,notification_type,message,admin_seen,vendor_seen,user_seen,add_date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdmin_seen() {
        return admin_seen;
    }

    public void setAdmin_seen(String admin_seen) {
        this.admin_seen = admin_seen;
    }

    public String getVendor_seen() {
        return vendor_seen;
    }

    public void setVendor_seen(String vendor_seen) {
        this.vendor_seen = vendor_seen;
    }

    public String getUser_seen() {
        return user_seen;
    }

    public void setUser_seen(String user_seen) {
        this.user_seen = user_seen;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }
}
