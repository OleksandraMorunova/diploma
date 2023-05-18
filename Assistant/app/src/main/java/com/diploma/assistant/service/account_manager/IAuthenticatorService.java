package com.diploma.assistant.service.account_manager;

import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;

public interface IAuthenticatorService<ID> {
    void savedTokenToSharedPreferences(ID preferencesName, ID key, Set<ID> value );
    void savedString(ID preferencesName, ID key, ID value);
    void clear(ID key, ID preferencesName);
    Set<ID> getTokenFromSharedPreferences(ID key, ID preferencesName);
    ID getElementFromSet(ID kayValue, ID key, ID preferencesName);
    Jwt<Header, Claims> getTokenProperty(ID token);
}
