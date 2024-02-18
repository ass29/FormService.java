package com.excel;

public class CategorySummary {

    private String categoryName;
    private double total;

    public CategorySummary(String categoryName, double total) {
        this.categoryName = categoryName;
        this.total = total;
    }


    public String getCategoryName ()
    {
        return categoryName;
    }

    public void setCategoryName (String categoryName)
    {
        this.categoryName = categoryName;
    }

    public double getTotal ()
    {
        return total;
    }

    public void setTotal (double total)
    {
        this.total = total;
    }

    @Override
    public String toString ()
    {
        return "CategorySummary{" + "categoryName='" + categoryName + '\'' + ", total=" + total + '}';
    }
}
