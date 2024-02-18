package com.excel;

import java.util.UUID;

public class Component {
    private UUID partid;
    private String partdes;
    private double price;
    private String category;

    public Component(UUID partid, String partdes, double price, String category) {
        this.partid = partid;
        this.partdes = partdes;
        this.category = category;
        this.price = price;
    }


    public UUID getPartid () {return partid;}

    public void setPartid (UUID partid) {this.partid = partid;}

    public String getPartdes () {return partdes;}

    public void setPartdes (String partdes) {this.partdes = partdes;}

    public String getCategory () {return category;}

    public void setCategory (String category) {this.category = category;}

    public double getPrice () {return price;}

    public void setPrice (double price) {this.price = price;}

    @Override
    public String toString ()
    {
        return "Component{" + "partid=" + partid + ", partdes='" + partdes + '\'' + ", price=" + price + ", category='" + category + '\'' + '}';
    }
}
