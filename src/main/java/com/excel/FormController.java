package com.excel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FormController {

    private FormService formService;

    static Statement stmt = null;
    static Connection conn = null;
    static ResultSet resultSet = null;


    public FormController (FormService formService) {
        this.formService = formService;
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute Component c) throws SQLException {

        System.out.println("");

        conn = SpringBootApp.getDBConnection();
        stmt = conn.createStatement();

        UUID uuid = UUID.randomUUID();
        c.setPartid(uuid);

        BigDecimal bigDecimal = new BigDecimal(c.getPrice());
        double correctPrice = bigDecimal.doubleValue();
        c.setPrice(correctPrice);

        formService.createComponentsTable();
        formService.createCategoriesTable();


        if(!formService.ifPartExists(c))
        {
            formService.insertComponent(c);
        }

        conn.commit();

        return "success";
    }

    @GetMapping("/edit")
    public ModelAndView editView(@RequestParam UUID partid) throws SQLException {

        ModelAndView mav = new ModelAndView("update-template");
        mav.addObject("componentObj", formService.getComponentByID(partid));

        return mav;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Component updatedComponent) throws SQLException {

        BigDecimal bigDecimal = new BigDecimal(updatedComponent.getPrice());
        double correctPrice = bigDecimal.doubleValue();
        updatedComponent.setPrice(correctPrice);

        formService.updateComponentByID(updatedComponent);

        conn.commit();

        return "success";
    }

    @GetMapping("/delete")
    public ModelAndView delete(@RequestParam UUID partid) throws SQLException {

        conn = SpringBootApp.getDBConnection();

        ModelAndView mav = new ModelAndView("delete-template");

        formService.deleteByID(partid);

        conn.commit();

        return mav;
    }

    @GetMapping("/")
    public ModelAndView displayComponentsView() throws SQLException {

        ModelAndView mav = new ModelAndView("components-template");

        conn = SpringBootApp.getDBConnection();

        formService.createComponentsTable();
        formService.createCategoriesTable();

        mav.addObject("sortLowestToHighest", "lowestToHighest");
        mav.addObject("sortHighestToLowest", "highestToLowest");

        mav.addObject("components", formService.queryComponents());
        mav.addObject("categories", formService.queryCategories());
        mav.addObject("totalPrice", formService.queryTotalPriceForAllParts());

        formService.alterPrecision();

        conn.commit();

        return mav;
    }

    @GetMapping("/sortAllPartsAlphabetically")
    public ModelAndView sortAlphabeticallyView() throws SQLException {

        ModelAndView mav = new ModelAndView("sort-alphabetically-template");

        mav.addObject("sortedComponentsAlphabetically", formService.sortComponentsAlphabetically());

        mav.addObject("sortLowestToHighest", "lowestToHighest");
        mav.addObject("sortHighestToLowest", "highestToLowest");
        mav.addObject("components", formService.queryComponents());
        mav.addObject("categories", formService.queryCategories());
        mav.addObject("totalPrice", formService.queryTotalPriceForAllParts());

        return mav;
    }

    @GetMapping("/sortCategoryAlphabetically")
    public ModelAndView sortCategoryAlphabeticallyView(@RequestParam String category) throws SQLException {


        ModelAndView mav = new ModelAndView("sort-category-alphabetically-template");

        mav.addObject("componentsByCategoryAlphabetically", formService.sortCategoryAlphabetically(category));
        mav.addObject("categories", formService.queryCategories());
        mav.addObject("totalCategoryPrice", formService.queryTotalPriceByCategory(category));

        return mav;

    }

    @GetMapping("/sortCategoryByPrice")
    public ModelAndView sortCategoryByPriceView(@RequestParam String category, @RequestParam String sort) throws SQLException {

        ModelAndView mav = new ModelAndView("sort-category-by-price-template");

        mav.addObject("sortedCategoryComponentsByPrice", formService.sortCategoryByPrice(category, sort));
        mav.addObject("categories", formService.queryCategories());
        mav.addObject("totalCategoryPrice", formService.queryTotalPriceByCategory(category));

        return mav;
    }

    @GetMapping("/sortPrice")
    public ModelAndView displayComponentsSortedView(@RequestParam String sort) throws SQLException {

        System.out.println(sort);

        ModelAndView mav = new ModelAndView("sortby-template");

        mav.addObject("categories", formService.queryCategories());
        mav.addObject("sortedComponentsByPrice", formService.sortPrices(sort));
        mav.addObject("totalPrice", formService.queryTotalPriceForAllParts());

        return mav;
    }

    @GetMapping("/category")
    public ModelAndView categoryView(@RequestParam String categoryname) throws SQLException {

        ModelAndView mav = new ModelAndView("components-by-categories-template");

        mav.addObject("componentsByCategory", formService.queryComponentsByCategory(categoryname));
        mav.addObject("categories", formService.queryCategories());
        mav.addObject("totalCategoryPrice", formService.queryTotalPriceByCategory(categoryname));

        conn.commit();

        return mav;
    }

    @GetMapping("/statistics")
    public ModelAndView statisticsView() throws SQLException {

        ModelAndView mav = new ModelAndView("statistics-template");

        mav.addObject("totalPrice", formService.queryTotalPriceForAllParts());
        mav.addObject("avgPriceForAllParts", formService.queryAvgPriceForAllParts());
        mav.addObject("medianPrice", formService.queryMedianForAllParts());
        mav.addObject("totalNumParts", formService.queryTotalNumParts());
        mav.addObject("mostExpensivePart", formService.queryMostExpensivePart());
        mav.addObject("leastExpensivePart", formService.queryLeastExpensivePart());
        mav.addObject("allCategoryTotals", formService.queryTotalForEachCategory());

        return mav;

    }


    @PostMapping("/submit")
    public String handleSubmit(@RequestParam Map<String, String> formData) throws Exception {

        formService.createComponentsTable();
        formService.createCategoriesTable();

        Set<String>existingParts = new HashSet<>();
        try
        {
            conn = SpringBootApp.getDBConnection();
            stmt = conn.createStatement();

            String queryExistingComponentsSql =     "SELECT * " +
                                                    "FROM Components";

            resultSet = stmt.executeQuery(queryExistingComponentsSql);
            while(resultSet.next())
            {
                String partName = resultSet.getString("partdes");
                existingParts.add(removeWhitespaces(partName).toLowerCase());
            }
        }

        catch (SQLException e)
        {
            throw new SQLException();

        }

        // begin parsing
        for (Map.Entry<String, String> entry : formData.entrySet())
        {
            String textBoxData = entry.getValue();
            String[]splitted = textBoxData.split("\n");
            String categoryName = splitted[0];

            categoryName = categoryName.trim();
            if(categoryName.endsWith(":"))
            {
                categoryName = categoryName.substring(0, categoryName.length() - 1);
            }

            for(String elem : splitted)
            {
                String[]splitted2 = elem.split("=");

                if(splitted2.length > 1)
                {
                    String part = splitted2[0];
                    part = part.trim();

                    String stringPrice = splitted2[1];

                    stringPrice = stringPrice.trim();
                    if(stringPrice.endsWith("$"))
                    {
                        stringPrice = stringPrice.substring(0, stringPrice.length() - 1);
                    }

                    float price = Float.parseFloat(stringPrice);
                    System.out.println("Part: " + part + " Price: " + price);

                    try
                    {
                        conn = SpringBootApp.getDBConnection();
                        stmt = conn.createStatement();

                        UUID partID = UUID.randomUUID();

                        if(!existingParts.contains(removeWhitespaces(part).toLowerCase()))
                        {
                            existingParts.add(removeWhitespaces(part));
                            String insertIntoComponentsSql =    "INSERT INTO Components" +
                                                                " VALUES " +
                                                                "('" + partID + "'" + ", " + "'" + removeWhitespaces(part).replace("'", "''") + "'" + ", " + price + "," + "'" + removeWhitespaces(categoryName).replace("'", "''") + "'" + ");";

                            stmt.executeUpdate(insertIntoComponentsSql);
//                            conn.commit();
                        }
                    }
                    catch (SQLException e)
                    {

                        System.out.println("Error code: " + e.getSQLState());
                        throw new SQLException();
                    }

                }
            }
        }

        conn.commit();

        return "success";
    }

    public static String removeWhitespaces(String input) {
        String regex = "\\s{2,}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(matcher.find())
        {
            input = input.replaceAll(regex, " ");
            return input.trim();
        }
        return input.trim();
    }

}

