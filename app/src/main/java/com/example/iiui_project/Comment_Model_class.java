package com.example.iiui_project;

public class Comment_Model_class {
    private  String Commend_id = "";
    private  String PostKey   = "";
    private  String PersonKey  = "";
    private  String PersonNameWhoComment  = "" ;
    private  String PersonPic  = "";
    private  String comment_description   = "";
    private  String comment_time   = "";

    public Comment_Model_class() {
    }

    public Comment_Model_class(String commend_id, String postKey, String personKey, String personNameWhoComment, String personPic, String comment_description, String comment_time) {
        Commend_id = commend_id;
        PostKey = postKey;
        PersonKey = personKey;
        PersonNameWhoComment = personNameWhoComment;
        PersonPic = personPic;
        this.comment_description = comment_description;
        this.comment_time = comment_time;
    }

    public String getCommend_id() {
        return Commend_id;
    }

    public String getPostKey() {
        return PostKey;
    }

    public String getPersonKey() {
        return PersonKey;
    }

    public String getPersonNameWhoComment() {
        return PersonNameWhoComment;
    }

    public String getPersonPic() {
        return PersonPic;
    }

    public String getComment_description() {
        return comment_description;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setCommend_id(String commend_id) {
        Commend_id = commend_id;
    }

    public void setPostKey(String postKey) {
        PostKey = postKey;
    }

    public void setPersonKey(String personKey) {
        PersonKey = personKey;
    }

    public void setPersonNameWhoComment(String personNameWhoComment) {
        PersonNameWhoComment = personNameWhoComment;
    }

    public void setPersonPic(String personPic) {
        PersonPic = personPic;
    }

    public void setComment_description(String comment_description) {
        this.comment_description = comment_description;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
