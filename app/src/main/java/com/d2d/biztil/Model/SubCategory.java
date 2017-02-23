package com.d2d.biztil.Model;

/**
 * Created by Bhavika on 10-Feb-17.
 */

public class SubCategory {

    public String categoryid;
    public String sub_categoryid;
    public String name;


    public SubCategory(String categoryid,String sub_categoryid, String name) {
        this.categoryid = categoryid;
        this.sub_categoryid = sub_categoryid;
        this.name = name;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_categoryid() {
        return sub_categoryid;
    }

    public void setSub_categoryid(String sub_categoryid) {
        this.sub_categoryid = sub_categoryid;
    }
}
