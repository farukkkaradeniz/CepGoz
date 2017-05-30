package com.cepgoz.kou.cepgoz.arama;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickBlox team
 */
public class DataHolder {

    public static ArrayList<QBUser> usersList;
    public static final String PASSWORD = "PASSWORD";

    private static QBUser currentUser;

    public static QBUser getLoggedUser() {
        return currentUser;
    }

    public static void setLoggedUser(QBUser currentUser) {
        DataHolder.currentUser = currentUser;
    }

    public static ArrayList<QBUser> getUsersList() {

        if (usersList == null) {
            usersList = new ArrayList<>();

            QBUser user = new QBUser("farukfk", PASSWORD);
            user.setId(7803414);
            user.setFullName("faruk karadeniz");
            usersList.add(user);
        }

        return usersList;
    }

    public static ArrayList<QBUser> createUsersList(List<QBUser> users) {
        usersList = new ArrayList<>();

        for(QBUser user : users){
            QBUser newUser = new QBUser(user.getLogin(), PASSWORD);
            newUser.setId(user.getId());
            newUser.setFullName(user.getFullName());
            usersList.add(newUser);
        }
        return usersList;
    }


    public static String getUserNameByID(Integer callerID) {
        for (QBUser user : getUsersList()) {
            if (user.getId().equals(callerID)) {
                return user.getFullName();
            }
        }
        return callerID.toString();
    }

    public static int getUserIndexByID(Integer callerID) {
        for (QBUser user : getUsersList()) {
            if (user.getId().equals(callerID)) {
                return usersList.indexOf(user);
            }
        }
        return -1;
    }

    public static List<QBUser> getUsers(){
        return usersList;
    }

    public static int getUserIndexByFullName(String fullName) {
        for (QBUser user : usersList) {
            if (user.getFullName().equals(fullName)) {
                return usersList.indexOf(user);
            }
        }
        return -1;
    }

    public static ArrayList<QBUser> getUsersByIDs(Integer... ids) {
        ArrayList<QBUser> result = new ArrayList<>();
        for (Integer userId : ids) {
            for (QBUser user : usersList) {
                if (userId.equals(user.getId())){
                    result.add(user);
                }
            }
        }
        return result;
    }
}
