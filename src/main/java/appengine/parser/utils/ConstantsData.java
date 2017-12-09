package appengine.parser.utils;


import appengine.parser.objects.AccessToken;
import appengine.parser.objects.UserCredentials;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anand.kurapati on 26/06/17.
 */
public class ConstantsData {

    public static final String HUMORLY_YOURS_PAGE_ID = "1899435196944780";
    public static final String SARCASM_PAGE_ID = "1515871602074952";

    public static final String FOURG_GIRL_PAGE_ID = "1755050324732167";

    public static final String PRIYA_DUBE_ID = "100023324076991";

    public static final String PAYALY_YADAV = "100018675767618";

    public static UserCredentials[] userCredentials = new UserCredentials[]{
            new UserCredentials("anandparser@yahoo.com", "parsingparsing"),
            new UserCredentials("payalyadav90566@gmail.com", "dfkler&67")
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
                    AccessToken.ID_TYPE.PAGE, FOURG_GIRL_PAGE_ID),
            //priya dube
            new AccessToken("EAAYIsgwZAZAV4BAC13Q1AaMfmNYuwONB8AW0G6FCb48wr75Ch7ChrdYlX7kleWb1jYByZAZAC5iQB2acAxBVO6IlPrpZBuFZBSDJbby4WawfBVbCxmJb9UAu1ZAW3QzfpXNhzlMwCMUWQ8leag7doUjPFPRRHknTFg4beGqilqshIPruLrpZC6mC",
                    AccessToken.ID_TYPE.USER, PRIYA_DUBE_ID),

            new AccessToken("EAAYIsgwZAZAV4BABo6SNALxUORHNQlEMMy8zze4u39MZAfW6XXaAU9ZCWPBbinaZAJ553CcBTizFbgEsXGPXbG0IRE8jprLxrpI4otjqNbfuZA2iCnqr0bVxaiLTAAXlOrfVlzI1tRJ3nkyNm0x2Sk9mV8HVzMRDNs00WNP5SQeaMZBX7nXHy4Xk5mjqTTwYEoZD",
                    AccessToken.ID_TYPE.USER, PAYALY_YADAV)
            /*

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

    public static Map<String, String[]> pageofSameCategoryMap = new HashMap<String, String[]>() {
        {
            put(HUMORLY_YOURS_PAGE_ID, new String[]{FOURG_GIRL_PAGE_ID});
            put(SARCASM_PAGE_ID, new String[]{FOURG_GIRL_PAGE_ID});
        }
    };


}
