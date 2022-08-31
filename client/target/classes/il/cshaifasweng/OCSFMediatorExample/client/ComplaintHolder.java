package il.cshaifasweng.OCSFMediatorExample.client;

public class ComplaintHolder {
    private String userID;
    private String orderID;
    private String content;
    private String date;
    private int complaintID;
    private double price;

    public ComplaintHolder(int orderId, int userId, String content, String dateTime, int complaintID, double price) {
        this.userID = String.valueOf(userId);
        this.orderID = String.valueOf(orderId);
        this.content = content;
        this.date = dateTime;
        this.complaintID = complaintID;
        this.price = price;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
