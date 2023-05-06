package com.diploma.assistant.service.account_manager;

import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;

public interface IAuthenticatorService<ID> {
    void savedTokenToSharedPreferences(ID preferences_name, ID key, Set<ID> value );
    void clear(ID key, ID preferences_name);
    Set<ID> getTokenFromSharedPreferences(ID key, ID preferences_name);
    ID getElementFromSet(ID kayValue, ID key, ID preferences_name);
    Jwt<Header, Claims> getTokenProperty(ID token);
}
