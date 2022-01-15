package top.hityx.cryptomall.models;

import java.util.Date;
import java.util.Objects;

public class Order {
    private int orderID;
    private String consumerUsername;
    private String sellerUsername;
    private int goodID;
    private String goodName;
    private  double goodPrice;
    private int goodNum;
    private double orderMoney;
    private Date orderTime;
    private boolean hasPayed;
    private boolean hasOnLoad; //发货状态
    private boolean hasConfirmDelivery; // 确认收货
    private String orderInfo; //其他信息

    public Order(int orderID, String consumerUsername, String sellerUsername, int goodID, String goodName, double goodPrice, int goodNum, double orderMoney, Date orderTime, boolean hasPayed, boolean hasOnLoad, boolean hasConfirmDelivery, String orderInfo) {
        this.orderID = orderID;
        this.consumerUsername = consumerUsername;
        this.sellerUsername = sellerUsername;
        this.goodID = goodID;
        this.goodName = goodName;
        this.goodPrice = goodPrice;
        this.goodNum = goodNum;
        this.orderMoney = orderMoney;
        this.orderTime = orderTime;
        this.hasPayed = hasPayed;
        this.hasOnLoad = hasOnLoad;
        this.hasConfirmDelivery = hasConfirmDelivery;
        this.orderInfo = orderInfo;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getConsumerUsername() {
        return consumerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public int getGoodID() {
        return goodID;
    }

    public String getGoodName() {
        return goodName;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public double getOrderMoney() {
        return orderMoney;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public boolean isHasPayed() {
        return hasPayed;
    }

    public boolean isHasOnLoad() {
        return hasOnLoad;
    }

    public boolean isHasConfirmDelivery() {
        return hasConfirmDelivery;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", consumerUsername='" + consumerUsername + '\'' +
                ", sellerUsername='" + sellerUsername + '\'' +
                ", goodID=" + goodID +
                ", goodName='" + goodName + '\'' +
                ", goodPrice=" + goodPrice +
                ", goodNum=" + goodNum +
                ", orderMoney=" + orderMoney +
                ", orderTime=" + orderTime +
                ", hasPayed=" + hasPayed +
                ", hasOnLoad=" + hasOnLoad +
                ", hasConfirmDelivery=" + hasConfirmDelivery +
                ", orderInfo='" + orderInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderID == order.orderID && goodID == order.goodID && Double.compare(order.goodPrice, goodPrice) == 0 && goodNum == order.goodNum && Double.compare(order.orderMoney, orderMoney) == 0 && hasPayed == order.hasPayed && hasOnLoad == order.hasOnLoad && hasConfirmDelivery == order.hasConfirmDelivery && consumerUsername.equals(order.consumerUsername) && sellerUsername.equals(order.sellerUsername) && goodName.equals(order.goodName) && orderTime.equals(order.orderTime) && Objects.equals(orderInfo, order.orderInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, consumerUsername, sellerUsername, goodID, goodName, goodPrice, goodNum, orderMoney, orderTime, hasPayed, hasOnLoad, hasConfirmDelivery, orderInfo);
    }
}
