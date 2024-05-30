/*
* LYJ
* getter & setter
* */

package com.example.myapplication;

public class Student {
    private String name, dorm;
    private String phone, roomNum;

    private int penalty;
    private int access;
    private int stay_out;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getName()
    {
        return name;
    }
    public String getDorm()
    {
        return dorm;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setDorm(String dorm)
    {
        this.dorm = dorm;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }


    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public int getStay_out() {
        return stay_out;
    }

    public void setStay_out(int stay_out) {
        this.stay_out = stay_out;
    }
}
