package com.yatai.suningfiredepartment.util;

import android.text.TextUtils;

public class Validator {

    public static boolean validUsername(String username) {
        return !TextUtils.isEmpty(username);
    }

    public static boolean validPassword(String password) {
        return !TextUtils.isEmpty(password);
    }
}
