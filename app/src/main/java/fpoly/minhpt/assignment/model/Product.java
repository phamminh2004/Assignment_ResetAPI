package fpoly.minhpt.assignment.model;

import java.io.Serializable;

public class Product implements Serializable {
    public String _id;
    public String ProductName;
    public int Price;
    public String Image;
    public String Description;

    public Product() {
    }

    public Product(String _id, String productName, int price, String image, String description) {
        this._id = _id;
        ProductName = productName;
        Price = price;
        Image = image;
        Description = description;
    }
}

