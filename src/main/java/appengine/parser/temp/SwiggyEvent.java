package appengine.parser.temp;

import com.google.gson.Gson;

public class SwiggyEvent {

    public String event;
    public String screen_name;
    public String object_name;
    public String source;
    public String eventNumber;
    public String user_swuid;
    public String object_postion;
    public String context;
    public String ts;
    public String platform_name;
    public String user_sid;
    public String object_value;
    public String swiggy_user_id;
    public String referrer;
    public String app_version_code;
    public String sw_app_version_code;
    public String is_test_event;
    public String sequence_number;
    public String id;
    public String latitude;
    public String longitude;

    public String toJSON() {
       return new Gson().toJson(this);
    }
}
