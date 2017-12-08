package appengine.parser.utils;


import appengine.parser.objects.AccessToken;
import appengine.parser.objects.UserCredentials;

/**
 * Created by anand.kurapati on 26/06/17.
 */
public class Constants {

    public static UserCredentials[] userCredentials = new UserCredentials[]{
            new UserCredentials("anandparser@yahoo.com","parsingparsing")
           /* new UserCredentials("aartidube588@gmail.com", "sdfertfg234"),
            // new UserCredentials("Nehasharma4379@gmail.com","sdfertfgtr67"),
            new UserCredentials("poojaverma4356@gmail.com", "fkdsljf893"),
            new UserCredentials("payalsharma9985@gmail.com", "ewewe45fg"),
            new UserCredentials("rupaliagrwal93@outlook.com", "gfghjlk567"),
            new UserCredentials("swetav9856@gmail.com", "gfghjlk567")*/
    };

    public static String default_access_token = "EAAYIsgwZAZAV4BAC13Q1AaMfmNYuwONB8AW0G6FCb48wr75Ch7ChrdYlX7kleWb1jYByZAZAC5iQB2acAxBVO6IlPrpZBuFZBSDJbby4WawfBVbCxmJb9UAu1ZAW3QzfpXNhzlMwCMUWQ8leag7doUjPFPRRHknTFg4beGqilqshIPruLrpZC6mC";

    public static AccessToken[] access_tokens = new AccessToken[]{
           //4g girl
            new AccessToken("EAAYIsgwZAZAV4BAMsKBZCpsVWM3KhXHzw6QFgA5oZCttbvMkZB6umo3IcOM0xnDdZB7qtbZAblkRx1GjPf3AZCo1K0Qu7M4LSgbuboSksn6JQnmPCqTvdAlKTLPWJh7PnACpZB4OiO3UbGqB2klFStwoQRqQ7n4GjG2FxzOS6iWR4uBII3gf7bSZAu",
                     AccessToken.ID_TYPE.PAGE),
            //priya dube
            new AccessToken("EAAYIsgwZAZAV4BAC13Q1AaMfmNYuwONB8AW0G6FCb48wr75Ch7ChrdYlX7kleWb1jYByZAZAC5iQB2acAxBVO6IlPrpZBuFZBSDJbby4WawfBVbCxmJb9UAu1ZAW3QzfpXNhzlMwCMUWQ8leag7doUjPFPRRHknTFg4beGqilqshIPruLrpZC6mC",
                    AccessToken.ID_TYPE.USER)/*,
            //Great Models
            new AccessToken("EAAYIsgwZAZAV4BAOCoGi19fxS9iYTq34rSZBBDUoSn9CUznElRZBUrKmxe2viIp8lIZAiZCcUtG0ZAgNcjvpGlRgZCkJZB7L00Y3ZCgZC1uX9zfDkrKa2UfUZCONWZCUaD0MaKFZBn1WjrZBVJKGXXzXNKCNgEz3CvjoZAQNa6qjIYlG5KJ3hf6D0CVDoZA5d",
                    AccessToken.ID_TYPE.PAGE)*/
    };

    public static String[] page_ids = new String[]{
            //Humourly yours
            "1899435196944780",
            //Sarcasm
            "1515871602074952"
    };

    public static String[] own_page_ids = new String[]{
             //4g girl
            "1755050324732167"
    };

    public static String to_be_promoted_page = own_page_ids[0];


}
