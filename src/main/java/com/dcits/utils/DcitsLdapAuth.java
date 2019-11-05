package com.dcits.utils;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class DcitsLdapAuth {

    public final String TDS_LDAP_URL = "LDAP://10.1.120.251:389/";

    public boolean ldapAuthentication(String ldapURL,
                               String userDN,
                               String UserPwd) {
        Hashtable env = new Hashtable();
        DirContext context;

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDN);
        env.put(Context.SECURITY_CREDENTIALS, UserPwd);

        try {
            context = new InitialDirContext(env);
            context.close();
            env.clear();
            return true;
        } catch (Exception ne) {
            return false;
        }
    }



}
