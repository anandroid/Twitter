package appengine.parser.facebook;

import appengine.parser.utils.StringUtils;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.restfb.types.StoryAttachment;

import java.util.List;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

/**
 * Created by anand.kurapati on 28/06/17.
 */
public class DetailedPost extends Post {



    public DetailedPost(Post post, FacebookClient facebookClient) {

        try {
            copyProperties(this, post);
        }
        catch (Exception e){

        }
        com.restfb.Connection<StoryAttachment> postAttachments = facebookClient.fetchConnection(post.getId() + "/attachments", com.restfb.types.StoryAttachment.class);
        Post.Attachments attachments = new Post.Attachments();
        for (List<StoryAttachment> myStoryConnectionPage : postAttachments) {
            for (StoryAttachment storyAttachment : myStoryConnectionPage) {
                attachments.addData(storyAttachment);
            }
        }
        this.setAttachments(attachments);

    }

    public boolean hasAttachment() {

        if (getAttachments() != null && getAttachments().getData() != null && getAttachments().getData().size() > 0)
            return true;

        return false;
    }

    public boolean hasDescription() {

        if (hasAttachment()) {

            if (StringUtils.isNonEmpty(getAttachments().getData().get(0).getDescription())) {

                return true;
            }
        }

        return false;
    }

    public String getFirstPictureLink() {

        if (hasAttachment()) {

            return getAttachments().getData().get(0).getMedia().getImage().getSrc();
        }
        return "";
    }

    public String getDescription() {
        if (hasAttachment()) {
            if (hasDescription()) {
                return getAttachments().getData().get(0).getDescription();
            }
        }
        return "";
    }


}
