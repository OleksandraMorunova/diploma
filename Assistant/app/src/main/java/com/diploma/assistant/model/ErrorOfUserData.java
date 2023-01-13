package com.diploma.assistant.model;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorOfUserData {
    private final static String EMAIL_PATTERN =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validUserName(String name){
        return name != null && !name.isEmpty() && StringUtils.isAllUpperCase(String.valueOf(name.charAt(0)))
                && StringUtils.isAllLowerCase(String.valueOf(name.substring(1)));
    }

    public boolean validEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return notNullAndEmpty(email) && matcher.matches();
    }

    public boolean validPhone(String phone) {
        return notNullAndEmpty(phone) && StringUtils.isNumeric(phone) &&  String.valueOf(phone.charAt(0)).equals("0")
                && phone.length() == 10;
    }

    private boolean notNullAndEmpty(String value){
        return value!= null && !value.isEmpty();
    }

}
