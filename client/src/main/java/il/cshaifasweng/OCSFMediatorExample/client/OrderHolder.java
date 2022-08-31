package il.cshaifasweng.OCSFMediatorExample.client;

public class OrderHolder {
    private String orderID;
    private String dateTime;
    private String finalPrice;
    private String flowers;
    private String status;
    private String receiverName;
    private String address;
    private String name;
    private String email;

    public OrderHolder(int orderID, String dateTime, double finalPrice, String flowers, int status) {
        this.orderID = Integer.toString(orderID);
        this.dateTime = dateTime;
        this.finalPrice = Double.toString(finalPrice);

        this.flowers = flowers;

        if(status == 1) {
            this.status = "Active";
        }
        else if(status == 2) {
            this.status = "Supplied";
        }
        else if(status == 3) {
            this.status = "Cancelled";
        }
    }

    public OrderHolder(int orderID, String receiverName, String flowers, String dateTime,
                       String address, int where, String name, String email) {
        this.orderID = Integer.toString(orderID);
        this.receiverName = receiverName;
        this.flowers = flowers;
        this.dateTime = dateTime;
        this.name = name;
        this.email = email;

        // "Haifa", "Tel Aviv", "New york", "Eilat", "London" "Delivery"
        if(where == 1) {
            this.address = "Pick Up Haifa";
        }
        else if (where == 2) {
            this.address = "Pick Up Tel Aviv";
        }
        else if (where == 3) {
            this.address = "Pick Up New York";
        }
        else if (where == 4) {
            this.address = "Pick Up Eilat";
        }
        else if (where == 5) {
            this.address = "Pick Up London";
        }
        else if (where == 6) {
            this.address = address;
        }
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getFlowers() {
        return flowers;
    }

    public void setFlowers(String flowers) {
        this.flowers = flowers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
