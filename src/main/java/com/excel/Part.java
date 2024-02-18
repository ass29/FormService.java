package com.excel;

public class Part {

    private String partdes;
    private double price;

    public Part(String partdes, double price) {
        this.partdes = partdes;
        this.price = price;
    }

    public String getPartdes () {return partdes;}

    public void setPartdes (String partdes) {this.partdes = partdes;}

    public double getPrice () {return price;}

    public void setPrice (double price) {this.price = price;}

    @Override
    public String toString ()
    {
        return "Part{" + "partdes='" + partdes + '\'' + ", price=" + price + '}';
    }
}
