package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.ChainManager;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.StoreManager;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

public class EntityHolder {
    // MYSQL tables: 0 = user, 1 = employee, 2 = storeM, 3 = chainM
    private static int table;
    private static User user;
    private static Employee employee;
    private static StoreManager storeM;
    private static ChainManager chainM;
    private static int id;

    public static void setTable(int index) {
        table = index;
    }

    public static int getTable() {
        return table;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static User getUser() {
        return user;
    }

    public static void setEmployee(Employee newEmployee) {
        employee = newEmployee;
    }

    public static Employee getEmployee() {
        return employee;
    }

    public static void setStoreM(StoreManager newStoreM) {
        storeM = newStoreM;
    }

    public static StoreManager getStoreM() {
        return storeM;
    }

    public static void setChainM(ChainManager newChainM) {
        chainM = newChainM;
    }

    public static ChainManager getChainM() {
        return chainM;
    }

    public static void setID(int ID) {
        id = ID;
    }

    public static int getID() {
        return id;
    }
}
