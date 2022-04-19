package ql.hoinghi.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(7));
    }

    public static boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
