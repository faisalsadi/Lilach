package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")

public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String account;
    private String credit;
    private String cvv;
    private String email;
    private String identificationNumber;
    private String monthAndYear;
    private String password;
    private String phone;
    private double refund;
    private int status;
    private String storeOrNull;
    private String userName;
    private String statusForTable;

    public User(String name, String identificationNumber, String email, String phone, String credit,
                String monthAndYear, String cvv, String password, String account, String storeOrNull,
                double refund, int status) {
        super();
        this.userName = name;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.credit = credit;
        this.monthAndYear = monthAndYear;
        this.cvv = cvv;
        this.password = password;
        this.account = account;
        this.storeOrNull = storeOrNull;
        this.refund = refund;
        this.status = status;
    }

    public User() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getId() {
        return identificationNumber;
    }

    public void setId(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStoreOrNull() {
        return storeOrNull;
    }

    public void setStoreOrNull(String storeOrNull) {
        this.storeOrNull = storeOrNull;
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
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
