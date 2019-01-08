package com.example.documents.busapp.AllDrivers;

public class Buss {
    public String busNum;
    public String AvaSeats;
    String id ;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Buss() {
    }

    public Buss(String busNum, String avaSeats) {
        this.busNum = busNum;
        this.AvaSeats = avaSeats;

    }


    public String getBusNum() {
        return busNum;
    }

    public String getAva_seats() {
        return AvaSeats;
    }


    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public void setAva_seats(String ava_seats) {
        AvaSeats = ava_seats;
    }


    @Override
    public String toString() {
        return "Buss{" +
                "busNum='" + busNum + '\'' +
                ", Ava_seats='" + AvaSeats+ '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
