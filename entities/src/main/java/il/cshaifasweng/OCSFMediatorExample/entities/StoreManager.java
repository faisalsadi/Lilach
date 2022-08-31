package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "storeManager")

public class StoreManager implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private String storeName;
    private String userName;
    // 1 active, 2 not active
    private int status;
    private String statusForTable;

    public StoreManager(String userName, String email, String password, String storeName, int status) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.storeName = storeName;
        this.status = status;
    }

    public StoreManager() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusForTable() {
        if(status == 1) {
            statusForTable = "Active";
        }
        else {
            statusForTable = "Not Active";
        }

        return statusForTable;
    }

    public void setStatusForTable(String statusForTable) {
        this.statusForTable = statusForTable;
    }
}
