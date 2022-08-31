package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "flowers")
public class Flower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;
    private double discount;
    private String imageurl;
    private double price;
    private String color;
    private Boolean sale;
    private String description;
    private int serialNumber;
    // status: 1 = active, 2 = not active
    private int status;

    @ManyToOne
    private Catalog catalog;

    public Flower() {
        super();
        this.sale = false;
        this.discount = 0;
        status = 1;
    }

    public Flower(String name, String description, String type, String image,
                  String color, double price, int serialNumber) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.imageurl = image;
        this.color = color;
        this.price = price;
        this.serialNumber = serialNumber;
        this.sale = false;
        this.discount = 0;
        this.status = 1;
    }

    public Flower(String name, String description, String type, String image,
                  String color, double price, int serialNumber, boolean sale, double discount) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.imageurl = image;
        this.color = color;
        this.price = price;
        this.serialNumber = serialNumber;
        this.sale = sale;
        this.discount = discount;
        this.status = 1;
    }

    public Flower(String name, String description, String type, String image, String color,
                  double price, int serialNumber, boolean sale, double discount, int status) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.imageurl = image;
        this.color = color;
        this.price = price;
        this.serialNumber = serialNumber;
        this.sale = sale;
        this.discount = discount;
        this.status = status;
    }

    public String getImageurl() {
        return imageurl;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getDiscount() {
        return discount;
    }

    public double getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public Boolean getSale() {
        return sale;
    }

    public String getDescription() {
        return description;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
