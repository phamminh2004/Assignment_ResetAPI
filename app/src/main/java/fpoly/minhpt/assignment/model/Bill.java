package fpoly.minhpt.assignment.model;

import java.util.Date;

public class Bill {
    public String _id;
    public String nameCustomer;
    public String address;
    public String phone;
    public int price;
    public Date date;
    public Boolean status;

    public Bill() {
    }

    public Bill(String _id, String nameCustomer, String address, String phone, int price, Date date, Boolean status) {
        this._id = _id;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.date = date;
        this.status = status;
    }
}
