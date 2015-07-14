package me.josephzhu.appinfocenter.site.entity;

import java.io.Serializable;

public class LoginUser implements Serializable {

    private static final long serialVersionUID = -4006794753824335207L;

    private int               id;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    private String            email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
