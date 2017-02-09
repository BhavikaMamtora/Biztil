package com.d2d.biztil.Webservice;

/**
 * Created by Bhavika on 06-Feb-17.
 */

public
class Web_url_api {

    public static final String Basic_Url = "http://biztil.com/android/";
    public static  String url="";


    public static final
    String loginUrlApi(String username, String password) {

        // http://biztil.com/android/signin.php?username=design2developindia&password=123456

         url = Basic_Url + "signin"
                     + ".php?" + Json_keys.USERNAME + "="
                     + username + "&" + Json_keys
                             .PASSWORD + "=" + password;

        return url;
    }

    public static final
    String signupUrlApi(
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

}
