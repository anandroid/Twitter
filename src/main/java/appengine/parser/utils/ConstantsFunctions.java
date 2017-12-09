package appengine.parser.utils;

import appengine.parser.objects.AccessToken;

import java.util.ArrayList;

/**
 * Created by anand.kurapati on 09/12/17.
 */
public class ConstantsFunctions {

    public static AccessToken getAccessTokenForId(String id) {
        for (AccessToken accessToken : ConstantsData.access_tokens) {
            if (accessToken.id.equals(id)) {
                return accessToken;
            }
        }
        return null;
    }

    public static ArrayList<AccessToken> getAccessTokensOfSameCategory(String pageId) {

        ArrayList<AccessToken> accessTokens = new ArrayList<>();
        String[] pageIdsOfSameCategory = ConstantsData.pageofSameCategoryMap.get(pageId);
        for (String pageIdOfSameCategory : pageIdsOfSameCategory) {
            accessTokens.add(getAccessTokenForId(pageIdOfSameCategory));
        }
        return accessTokens;
    }
}
