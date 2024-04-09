package fpoly.minhpt.assignment.model;

import java.io.Serializable;

public class Cart implements Serializable {
    public String _id;
    public String ProductName;
    public int Price;
    public String Image;
    public int Quantity;
    public String ProductID;
    public Cart() {
    }

    public Cart(String _id, String productName, int price, String image, int quantity, String productID) {
        this._id = _id;
        ProductName = productName;
        Price = price;
        Image = image;
        Quantity = quantity;
        ProductID = productID;
    }
}
