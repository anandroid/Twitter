package appengine.parser.objects;

/**
 * Created by anand.kurapati on 28/06/17.
 */
public class AccessToken {

    public String access_token;
    public ID_TYPE id_type;

    public AccessToken(String access_token, ID_TYPE id_type) {
        this.access_token = access_token;
        this.id_type = id_type;
    }

    public enum ID_TYPE {
        USER,
        PAGE
    }
}
