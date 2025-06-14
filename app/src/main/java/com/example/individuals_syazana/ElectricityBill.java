package com.example.individuals_syazana;

public class ElectricityBill {
    String id;
    String month;
    int unit;
    double rebate;
    double TtlCharge;
    double FnlCost;

    public ElectricityBill(){} //ni untuk database

    public ElectricityBill(String id, String month, int unit, double rebate, double TtlCharge, double FnlCost){
        this.id=id;
        this.month=month;
        this.unit=unit;
        this.rebate=rebate;
        this.TtlCharge=TtlCharge;
        this.FnlCost=FnlCost;
    }

    public String getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public int getUnit() {
        return unit;
    }

    public double getRebate() {
        return rebate;
    }

    public double getTtlCharge() {
        return TtlCharge;
    }

    public double getFnlCost() {
        return FnlCost;
    }
}
