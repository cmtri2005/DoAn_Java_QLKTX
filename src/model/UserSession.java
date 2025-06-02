// File: model/UserSession.java (tạo mới)
package model;

public class UserSession {
    private static String cccd;
    private static String mssv;

    public static String getCccd() {
        return cccd;
    }
    public static String getMssv() {
        return mssv;
    }
    public static void setCccd(String value) {
        cccd = value;
    }
    public static void setMssv(String value) {
        mssv = value;
    }
}
