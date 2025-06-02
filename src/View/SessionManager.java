/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author trica
 */
public class SessionManager {
    private static Integer currentUserId = null;
    public static void setCurrentUserId(Integer userId){
        currentUserId = userId;
    }
    public static Integer getCurrentUserId(){
        return currentUserId;
    }
    public static void clearSession(){
        currentUserId = null;
    }
}
