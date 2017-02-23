package com.d2d.biztil.Model;

/**
 * Created by Bhavika on 10-Feb-17.
 */

public class FrndRequest {

    public String memberid;
    public String profilepic,membername,city,timeago;

    public FrndRequest(String memberid, String profilepic, String membername, String city, String
            timeago) {
        this.memberid = memberid;
        this.profilepic = profilepic;
        this.membername = membername;
        this.city = city;
        this.timeago = timeago;
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

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }
}
