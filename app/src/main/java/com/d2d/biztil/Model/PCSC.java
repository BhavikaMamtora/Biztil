package com.d2d.biztil.Model;

/**
 * Created by NK on 11-Feb-2017.
 */

public class PCSC {
    int id;
    String name;

    public PCSC(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
