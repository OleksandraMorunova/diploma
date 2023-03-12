package com.diploma.assistant;

import org.junit.Test;

import static org.junit.Assert.*;

import com.diploma.assistant.view.ui.activity.auth.sign_up_2.CheckString;

public class CheckStringTest {

    @Test
    public void testPassword() {
        assertTrue(CheckString.password("Password1@")); // Test valid password
        assertFalse(CheckString.password("Password@"));// Test password with no number
        assertFalse(CheckString.password("PASSWORD1@"));// Test password with no lower case
        assertFalse(CheckString.password("password1@")); // Test password with no upper case
        assertFalse(CheckString.password("Passwo d1@"));   // Test password with whitespace
        assertFalse(CheckString.password("Password1")); // Test password with no special character
        assertFalse(CheckString.password("Pass1@")); // Test password with incorrect length (less than 8 characters)
        assertFalse(CheckString.password("ThisIsALongPassword1@")); // Test password with incorrect length (more than 16 characters)
    }

    @Test
    public void testMethod2() {
        assertTrue("123456".chars().allMatch(Character::isDigit)); // Test valid number
        assertFalse("12345a".chars().allMatch(Character::isDigit)); // Test string with non-digits
        assertTrue("".chars().allMatch(Character::isDigit)); // Test empty string
        assertFalse(" ".chars().allMatch(Character::isDigit)); // Test password with whitespace
        assertFalse(" 123456 ".chars().allMatch(Character::isDigit)); // Test string with spaces
        assertFalse("12345@".chars().allMatch(Character::isDigit)); // Test string with special characters
    }
}