package com.bookstore.utility;

import java.util.*;

/**
 * created by saikat on 4/17/19
 */
public class CountryList {


    public static final Set<String> countrySet = new HashSet<>();


    public static List<String> getCountryList() {


        String[] locals = Locale.getISOCountries();

        for (String code:locals
             ) {
            Locale obj = new Locale("",code);
            countrySet.add(obj.getDisplayCountry());
        }
        List<String> countryList = new ArrayList<>(countrySet);
        Collections.sort(countryList);
        return countryList;
    }


}
