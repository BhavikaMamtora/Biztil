package com.d2d.biztil.Model;

/**
 * Created by Bhavika on 15-Feb-17.
 */

public class Member_model {

    public String memberid, profilepic, company, rating, city, membercategory, friendstatus;

    public Member_model(String memberid, String profilepic, String company, String rating, String
            city, String membercategory, String friendstatus) {
        this.memberid = memberid;
        this.profilepic = profilepic;
        this.company = company;
        this.rating = rating;
        this.city = city;
        this.membercategory = membercategory;
        this.friendstatus = friendstatus;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMembercategory() {
        return membercategory;
    }

    public void setMembercategory(String membercategory) {
        this.membercategory = membercategory;
    }

    public String getFriendstatus() {
        return friendstatus;
    }

    public void setFriendstatus(String friendstatus) {
        this.friendstatus = friendstatus;
    }
}
