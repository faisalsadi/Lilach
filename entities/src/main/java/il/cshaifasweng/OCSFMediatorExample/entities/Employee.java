package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee")

public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private String userName;
    // 1 active, 2 not active
    private int status;
    private String statusForTable;

    public Employee(String userName, String email, String password, int status) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public Employee() {
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
