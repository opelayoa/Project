package com.tiendas3b.almacen.util;

/**
 * Created by dfa on 22/02/2016.
 */
public class MailUtil {

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
