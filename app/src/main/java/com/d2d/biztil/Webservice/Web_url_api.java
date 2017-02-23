package com.d2d.biztil.Webservice;

/**
 * Created by Bhavika on 06-Feb-17.
 */

public class Web_url_api {

    public static final String Basic_Url = "http://biztil.com/android/";
    public static       String url       = "";


    public static final String loginUrlApi(String username, String password) {

        // http://biztil.com/android/signin.php?username=design2developindia&password=123456

        url = Basic_Url + "signin"
                + ".php?" + Json_keys.USERNAME + "="
                + username + "&" + Json_keys
                .PASSWORD + "=" + password;

        return url;
    }

    public static final String signupUrlApi(
            String name, String email, String mobile, String gender, String password,
            String check1, String check2, String check3
    ) {

        // http://biztil.com/android/signup
        // .php?name=design2develop&email=design2developindia@gmail
        // .com&mobile=9558330150&gender=Male&password=123456&check1=Buyer&check2=&check3

        String url = Basic_Url + "signup"
                + ".php?" + Json_keys.NAME + "="
                + name + "&" + Json_keys.EMAIL + "="
                + email + "&" + Json_keys.MOBILE + "="
                + mobile + "&" + Json_keys.GENDER + "="
                + gender + "&" + Json_keys.PASSWORD + "="
                + password + "&" + Json_keys.CHECK1 + "="
                + check1 + "&" + Json_keys.CHECK2 + "="
                + check2 + "&" + Json_keys
                .CHECK3 + "=" + check3;

        return url;
    }


    public static final String personalInfoUrlApi(
            String country, String state, String city, String day, String month,
            String year, String uid
    ) {

        // https://biztil.com/android/savepersonalinfo
        // .php?country=105&state=1104&city=80&day=16&month=08&year=1988&uid=2

        String url = Basic_Url + "savepersonalinfo"
                + ".php?" + Json_keys.COUNTRY + "="
                + country + "&" + Json_keys.STATE + "="
                + state + "&" + Json_keys.CITY + "="
                + city + "&" + Json_keys.DAY + "="
                + day + "&" + Json_keys.MONTH + "="
                + month + "&" + Json_keys.YEAR + "="
                + year + "&" + Json_keys
                .UID + "=" + uid;

        return url;
    }

    public static final String businessInfoUrlApi(
            String businessname, String businesstype, String businesscategory, String uid
    ) {

        // https://biztil.com/android/savebusinessinfo
        // .php?businessname=Design%202%20Develop%20India&businesstype=Service%20Provider,
        // Supplier&businesscategory=13,14&uid=2

        String url = Basic_Url + "savebusinessinfo"
                + ".php?" + Json_keys.BUSINESSNAME + "="
                + businessname + "&" + Json_keys.BUSINESSTYPE + "="
                + businesstype + "&" + Json_keys.BUSINESSCATEGORY + "="
                + businesscategory + "&" + Json_keys
                .UID + "=" + uid;

        return url;
    }


    public static final String categoriesUrlApi() {

        //    https://biztil.com/android/categories.php

        String url = Basic_Url + "categories"
                + ".php";

        return url;
    }

    public static final String subCategoriesUrlApi(String catg_id) {

        //    http://biztil.com/android/subcategories.php?categoryid=1

        String url = Basic_Url + "subcategories"
                + ".php?"+ Json_keys
                .CATEGORYID + "=" + catg_id;

        return url;
    }

    public static final String otpVerificationUrlApi(String otp, String uid) {

        //    https://biztil.com/android/categories.php

        String url = Basic_Url + "verifyotp"
                + ".php?" + Json_keys.OTP + "="
                + otp + "&" + Json_keys
                .UID + "=" + uid;

        return url;
    }


    public static final String memberListingUrlApi(String uid) {

        //    http://biztil.com/android/memberlist.php?uid=2

        String url = Basic_Url + "memberlist"
                + ".php?" + Json_keys.UID + "="
                + uid;

        return url;
    }

    public static final String memberListingPaginationUrlApi(String uid,int page) {

        //    http://biztil.com/android/memberlist.php?uid=2

        String url = Basic_Url + "memberlist"
                + ".php?" + Json_keys.UID + "="
                + uid + "&" + Json_keys
                .PAGE + "=" + page;

        return url;
    }

    public static final String frndRequestUrlApi(String uid) {

        //    http://biztil.com/android/friendrequest.php?uid=2

        String url = Basic_Url + "friendrequest"
                + ".php?" + Json_keys.UID + "="
                + uid;

        return url;
    }

    public static final String frndRequestPaginationUrlApi(String uid,int page) {

        //    http://biztil.com/android/friendrequest.php?uid=2


        String url = Basic_Url + "friendrequest"
                + ".php?" + Json_keys.UID + "="
                + uid + "&" + Json_keys
                .PAGE + "=" + page;

        return url;
    }

}
