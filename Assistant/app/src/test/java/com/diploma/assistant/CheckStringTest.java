package com.diploma.assistant;

import org.junit.Test;

import static org.junit.Assert.*;

import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;

public class CheckStringTest {

    @Test
    public void testPassword() {
        assertTrue(CheckStringLine.password("Password1@")); // Test valid password
        assertFalse(CheckStringLine.password("Password@"));// Test password with no number
        assertFalse(CheckStringLine.password("PASSWORD1@"));// Test password with no lower case
        assertFalse(CheckStringLine.password("password1@")); // Test password with no upper case
        assertFalse(CheckStringLine.password("Passwo d1@"));   // Test password with whitespace
        assertFalse(CheckStringLine.password("Password1")); // Test password with no special character
        assertFalse(CheckStringLine.password("Pass1@")); // Test password with incorrect length (less than 8 characters)
        assertFalse(CheckStringLine.password("ThisIsALongPassword1@")); // Test password with incorrect length (more than 16 characters)
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