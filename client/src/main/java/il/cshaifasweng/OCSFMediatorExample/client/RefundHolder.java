package il.cshaifasweng.OCSFMediatorExample.client;

public class RefundHolder {
    private String orderID;
    private String refund;

    public RefundHolder(int orderID, double refund) {
        this.orderID = Integer.toString(orderID);
        this.refund = Double.toString(refund);
    }

    public String getOrderID() { return orderID; }

    public void setOrderID(String orderID) { this.orderID = orderID; }

    public String getRefund() { return refund; }

    public void setRefund(String refund) { this.refund = refund; }
}
