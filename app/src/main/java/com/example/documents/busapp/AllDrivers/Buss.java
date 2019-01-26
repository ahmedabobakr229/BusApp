package com.example.documents.busapp.AllDrivers;

public class Buss {
    public String busNum;
    public String AvaSeats;
    public String Sta;



    String id ;

    public Buss(String numBus, String avaSeats , String sta) {
        this.busNum = numBus;
        this.AvaSeats = avaSeats;
        this.Sta = sta ;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Buss() {
    }


    public String getBusNum() {
        return busNum;
    }

    public String getAva_seats() {
        return AvaSeats;
    }

    public String getSta() {
        return Sta;
    }

    public void setSta(String sta) {
        Sta = sta;
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
                ", AvaSeats='" + AvaSeats + '\'' +
                ", Sta='" + Sta + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
