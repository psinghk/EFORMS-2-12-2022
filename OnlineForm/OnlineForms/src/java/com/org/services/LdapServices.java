package com.org.services;

public interface LdapServices {
    public boolean emailValidate(String email);
    public boolean emailValidate_for_all_without_status(String email);
    public boolean uidValidate(String email);
}
