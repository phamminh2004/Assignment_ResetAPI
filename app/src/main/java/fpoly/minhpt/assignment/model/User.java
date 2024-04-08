package fpoly.minhpt.assignment.model;

import java.util.Date;

public class User {
    public String _id;
    public String email;
    public String name;
    public Date date;
    public String address;
    public String phone;
    public Boolean gender;

    public User() {
    }

    public User(String _id, String email, String name, Date date, String address, Boolean gender, String phone) {
        this._id = _id;
        this.email = email;
        this.name = name;
        this.date = date;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
    }
}
