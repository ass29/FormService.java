package com.excel;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FormService {

    Statement stmt = null;
    Connection conn = null;
    ResultSet resultSet = null;

    public Set<Component> queryComponents() throws SQLException {

        Set<Component> componentSet = new LinkedHashSet<>();
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =      "SELECT partid, partdes, CAST(price AS DECIMAL) AS displayPrice, category " +
                                "FROM Components";

            resultSet = stmt.executeQuery(query);
            while (resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partName = resultSet.getString("partdes");
                double price = resultSet.getDouble("displayPrice");
                String category = resultSet.getString("category");

                Component component = new Component(UUID.fromString(partid), partName, price, category);
                componentSet.add(component);

            }
        }
        catch (SQLException e)
        {

            return componentSet;
        }

        return componentSet;
    }

    public Set<String> queryCategories() throws SQLException {
        Set<String>categoriesSet = new LinkedHashSet<>();

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =  "SELECT DISTINCT TRIM(REGEXP_REPLACE(LOWER(category), '\s{2,}', ' ', 'g')) AS allDistinctCategories " +
                            "FROM components;";

            resultSet = stmt.executeQuery(query);
            while(resultSet.next())
            {
                String categoryName = resultSet.getString("allDistinctCategories");

                String modified = "";
                modified += Character.toUpperCase(categoryName.charAt(0));

                for(int i = 0; i < categoryName.length() - 1; i++)
                {
                    if(categoryName.charAt(i) == ' ')
                    {
                        modified += Character.toUpperCase(categoryName.charAt(i + 1));
                    }
                    else
                    {
                        modified += categoryName.charAt(i + 1);
                    }
                }

                categoriesSet.add(modified);
            }
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }

        return categoriesSet;
    }

    public boolean ifPartExists(Component p) throws SQLException {

        for(Component comp : queryComponents())
        {
            if(FormController.removeWhitespaces(comp.getPartdes().toLowerCase()).equals(FormController.removeWhitespaces(p.getPartdes().toLowerCase()).trim()))
            {
                return true;
            }
        }
        return false;
    }

    public void createComponentsTable() throws SQLException {

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String createComponentsTableSql =        "CREATE TABLE IF NOT EXISTS Components" +
                                                     "( " +
                                                     "PartID VARCHAR(36) NOT NULL," +
                                                     "PRIMARY KEY (PartID), " +
                                                     "PartDes VARCHAR(200)," +
                                                     "Price FLOAT NOT NULL," +
                                                     "Category VARCHAR(200) NOT NULL " +
                                                     ");";

            stmt.executeUpdate(createComponentsTableSql);
        }

        catch (SQLException e)
        {
            throw new SQLException();
        }

    }

    public void createCategoriesTable() throws SQLException {

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String createCategoriesTableSql =   "CREATE TABLE IF NOT EXISTS Categories" +
                                                "( " +
                                                "CategoryName VARCHAR(200)" +
                                                ");";
            stmt.executeUpdate(createCategoriesTableSql);
        }
        catch (SQLException e)
        {
            throw  new SQLException();
        }

    }

    public void insertComponent(Component component) throws SQLException {

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String insertComponent =    "INSERT INTO Components VALUES ('" +
                                        component.getPartid() + "', " +
                                        "'" + FormController.removeWhitespaces(component.getPartdes()).replace("'", "''") + "', " +
                                        component.getPrice() + ", " +
                                        "'" + FormController.removeWhitespaces(component.getCategory()).replace("'", "''") + "'" +
                                        ");";

            stmt.executeUpdate(insertComponent);

        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

    }

    public Component getComponentByID(UUID uuid) throws SQLException {

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String queryByID =          "SELECT * " +
                                        "FROM Components " +
                                        "WHERE partid = " + "'" + uuid + "'";
            resultSet = stmt.executeQuery(queryByID);
            
            Component component = null;
            if(resultSet.next())
            {
                String partdes = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");

                component = new Component(uuid, partdes, price, category);
            }

            return component;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }

    }

    public Set<Component> queryComponentsByCategory(String category) throws SQLException {

        Set<Component>res = new LinkedHashSet<>();

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String queryByCategory =        "SELECT * " +
                                            "FROM Components " +
                                            "WHERE TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) = TRIM(REGEXP_REPLACE(LOWER(" + "'" + category + "'" + "), '\\s{2,}', ' ', 'g'))";
            resultSet = stmt.executeQuery(queryByCategory);

            while (resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partName = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");

                Component component = new Component(UUID.fromString(partid), partName, price, category);
                res.add(component);
            }
        }
        catch (SQLException e)
        {
            return res;
        }

        return res;
    }

    public void updateComponentByID(Component updatedComponent) throws SQLException {

        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String updateStatement =    "UPDATE Components " +
                                        "SET partdes = '" + updatedComponent.getPartdes() + "', " +
                                        "price = " + updatedComponent.getPrice() + ", " +
                                        "category = '" + updatedComponent.getCategory() + "' " +
                                        "WHERE partid = " + "'" + updatedComponent.getPartid() + "'" + ";";

            stmt.executeUpdate(updateStatement);
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

    }
    public void deleteByID(UUID id) throws SQLException {
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String deleteStmt = "DELETE FROM components " +
                                "WHERE partid='" + id + "';";
            stmt.executeUpdate(deleteStmt);
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }
    }
    public Set<Component> sortPrices(String sortMethod) throws SQLException {

        Set<Component>sortedComponentsByPrice = new LinkedHashSet<>();
        try
        {

            String query = "";
            if(sortMethod.equals("lowestToHighest"))
            {
                query =     "SELECT * FROM components " +
                            "ORDER BY price ASC;";
            }
            else if(sortMethod.equals("highestToLowest"))
            {
                query =     "SELECT * FROM components " +
                            "ORDER BY price DESC;";
            }
            resultSet = stmt.executeQuery(query);
            while(resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partdes = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");

                Component component = new Component(UUID.fromString(partid), partdes, price, category);
                sortedComponentsByPrice.add(component);
            }
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
        return sortedComponentsByPrice;
    }

    public double queryTotalPriceForAllParts() throws SQLException {
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =  "SELECT SUM(price) as total " +
                            "FROM components;";

            resultSet = stmt.executeQuery(query);
            double total = 0.0;

            while(resultSet.next())
            {
                total = resultSet.getDouble("total");
            }
            return total;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }

    public double queryAvgPriceForAllParts() throws SQLException {
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =  "SELECT ROUND(AVG(price), 2) AS avgPrice " +
                            "FROM components;";

            resultSet = stmt.executeQuery(query);
            double avg = 0.0;

            while(resultSet.next())
            {
                avg = resultSet.getDouble("avgPrice");
            }
            return avg;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }

    public void alterPrecision() throws SQLException {
        try
        {
            String alter =  "ALTER TABLE components " +
                            "ALTER COLUMN price TYPE DECIMAL(18,2);";
            stmt.executeUpdate(alter);
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }
    public double queryTotalPriceByCategory(String category) throws SQLException {
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =              "SELECT SUM(price) AS total " +
                                        "FROM Components " +
                                        "WHERE TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) = TRIM(REGEXP_REPLACE(LOWER(" + "'" + category + "'" + "), '\\s{2}', ' ', 'g'))";


            resultSet = stmt.executeQuery(query);
            double total = 0.0;

            while(resultSet.next())
            {
                total = resultSet.getDouble("total");
            }
            return total;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }

    public double queryMedianForAllParts() throws SQLException {

        ArrayList<Double> prices = new ArrayList<>();
        for(Component c : sortPrices("lowestToHighest"))
        {
            prices.add(c.getPrice());
        }

        double median;
        int length = prices.size();

        if(length % 2 == 0)
        {
            int index1 = (length / 2) - 1;
            int index2 = length / 2;

            median = (prices.get(index1) + prices.get(index2)) / 2.0;
        }
        else
        {
            int index = length / 2;
            median = prices.get(index);
        }

        return median;
    }
    public int queryTotalNumParts() throws SQLException {
        try
        {
            String query = "SELECT COUNT(partid) AS num " +
                            "FROM components;";
            resultSet = stmt.executeQuery(query);

            int total = 0;
            while (resultSet.next())
            {
                total = resultSet.getInt("num");
            }
            return total;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }
    public Part queryMostExpensivePart() throws SQLException {

        try
        {
            String query =  "SELECT partdes, price " +
                            "FROM components " +
                            "ORDER BY price DESC " +
                            "LIMIT 1;";
            resultSet = stmt.executeQuery(query);

            Part part = null;
            while (resultSet.next())
            {
                String partdes = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");

                part = new Part(partdes, price);
            }
            return part;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }
    public Part queryLeastExpensivePart() throws SQLException {

        try
        {
            String query =  "SELECT partdes, price " +
                            "FROM components " +
                            "ORDER BY price ASC " +
                            "LIMIT 1;";
            resultSet = stmt.executeQuery(query);

            Part part = null;
            while (resultSet.next())
            {
                String partdes = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");

                part = new Part(partdes, price);
            }
            return part;
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
    }

    public Set<CategorySummary> queryTotalForEachCategory() throws SQLException {

        try
        {
            Set<CategorySummary>summaries = new LinkedHashSet<>();

            String query =  "SELECT DISTINCT TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) AS category, SUM(price) as total " +
                            "FROM components " +
                            "GROUP BY DISTINCT TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) " +
                            "ORDER BY category ASC;";
            resultSet = stmt.executeQuery(query);

            CategorySummary categorySummary = null;
            while (resultSet.next())
            {
                String categoryName = resultSet.getString("category");
                double total = resultSet.getDouble("total");

                String modified = "";
                modified += Character.toUpperCase(categoryName.charAt(0));

                for(int i = 0; i < categoryName.length() - 1; i++)
                {
                    if(categoryName.charAt(i) == ' ')
                    {
                        modified += Character.toUpperCase(categoryName.charAt(i + 1));
                    }
                    else
                    {
                        modified += categoryName.charAt(i + 1);
                    }
                }


                categorySummary = new CategorySummary(modified, total);
                summaries.add(categorySummary);
            }
            return summaries;
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }
    }

    public Set<Component> sortComponentsAlphabetically() throws SQLException {

        Set<Component> res = new LinkedHashSet<>();
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =      "SELECT partid, partdes, CAST(price AS DECIMAL) AS displayPrice, category " +
                                "FROM Components " +
                                "ORDER BY partdes ASC";

            resultSet = stmt.executeQuery(query);
            while (resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partName = resultSet.getString("partdes");
                double price = resultSet.getDouble("displayPrice");
                String category = resultSet.getString("category");

                Component component = new Component(UUID.fromString(partid), partName, price, category);
                res.add(component);

            }
        }
        catch (SQLException e)
        {

            return res;
        }

        return res;
    }

    public Set<Component> sortCategoryAlphabetically(String category) {

        Set<Component> res = new LinkedHashSet<>();
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String query =      "SELECT * " +
                                "FROM Components " +
                                "WHERE TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) = TRIM(REGEXP_REPLACE(LOWER(" + "'" + category + "'" + "), '\\s{2,}', ' ', 'g')) " +
                                "ORDER BY partdes ASC;";

            resultSet = stmt.executeQuery(query);
            while (resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partName = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");

                Component component = new Component(UUID.fromString(partid), partName, price, category);
                res.add(component);

            }
        }
        catch (SQLException e)
        {

            return res;
        }

        return res;
    }
    public Set<Component> sortCategoryByPrice(String categoryName, String sortMethod) throws SQLException {

        Set<Component>sortedComponentsByPrice = new LinkedHashSet<>();
        try
        {

            String query = "";
            if(sortMethod.equals("lowestToHighest"))
            {
                query =         "SELECT * FROM components " +
                                "WHERE TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) = TRIM(REGEXP_REPLACE(LOWER(" + "'" + categoryName + "'" + "), '\\s{2,}', ' ', 'g')) " +
                                "ORDER BY price ASC;";
            }
            else if(sortMethod.equals("highestToLowest"))
            {
                query =         "SELECT * FROM components " +
                                "WHERE TRIM(REGEXP_REPLACE(LOWER(category), '\\s{2,}', ' ', 'g')) = TRIM(REGEXP_REPLACE(LOWER(" + "'" + categoryName + "'" + "), '\\s{2,}', ' ', 'g')) " +
                                "ORDER BY price DESC;";
            }
            resultSet = stmt.executeQuery(query);
            while(resultSet.next())
            {
                String partid = resultSet.getString("partid");
                String partdes = resultSet.getString("partdes");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");

                Component component = new Component(UUID.fromString(partid), partdes, price, category);
                sortedComponentsByPrice.add(component);
            }
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
        return sortedComponentsByPrice;

    }
}
















