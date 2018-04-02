package ca.dal.cs.web.cs_prepguide;

/**
 * Created by vamshikrishnamoogala on 2018-04-01.
 */

public class Comment {
    String commentContent;
    String commentedByUser;
    String commentedByUserName;
    String postId;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Comment(String commentContent, String commentedByUser, String commentedByUserName, String postId) {
        this.commentContent = commentContent;
        this.commentedByUser = commentedByUser;
        this.commentedByUserName = commentedByUserName;
        this.postId = postId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentedByUser() {
        return commentedByUser;
    }

    public void setCommentedByUser(String commentedByUser) {
        this.commentedByUser = commentedByUser;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentedByUserName() {
        return commentedByUserName;
    }

    public void setCommentedByUserName(String commentedByUserName) {
        this.commentedByUserName = commentedByUserName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentContent='" + commentContent + '\'' +
                ", commentedByUser='" + commentedByUser + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
