package com.octa.phlposcasu.Utils;

import java.util.Date;

public class UserModel {
//    private  String firstName, lastName, phoneNumber,avatar, uid, state, regdate, pointsexpiry;
//    private String hbcredit, points, chips, birthmonth, bmlinked;
//
//    private String AddressLandMark, AddressTitle, CompleteAddress, Lang, Lat, cards, cash, reffcode;

    private String firstName, lastName, phoneNumber, avatar, uid, email, password, planType, cTransaction, dID;
    private int asks, bonus;
    private Date resetDate, planExpiration;

    public UserModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getcTransaction() {
        return cTransaction;
    }

    public void setcTransaction(String cTransaction) {
        this.cTransaction = cTransaction;
    }

    public int getAsks() {
        return asks;
    }

    public void setAsks(int asks) {
        this.asks = asks;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public Date getResetDate() {
        return resetDate;
    }

    public void setResetDate(Date resetDate) {
        this.resetDate = resetDate;
    }

    public String getdID() {
        return dID;
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public Date getPlanExpiration() {
        return planExpiration;
    }

    public void setPlanExpiration(Date planExpiration) {
        this.planExpiration = planExpiration;
    }
}
