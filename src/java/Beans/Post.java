/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@RequestScoped
@ManagedBean
public class Post implements Serializable {

    /**
     * Creates a new instance of Post
     */
    private int requestPhotoId;
    private Photo postPhoto;
    private User postAuthor;
    private int likes;
    @ManagedProperty(value="#{currentUser}")
    private CurrentUser currentUser;
    private boolean isLikedByCurrentUser;
    private ArrayList<Comment> comments;
    private CachedRowSet crs;
    private String newComment;

    public void addComment() throws IOException{
        try{
            System.out.println(crs);
            System.out.println(currentUser.getUserID());
            System.out.println(requestPhotoId);
            System.out.println(newComment);
            crs.setCommand("insert into comments (userid,photoid,text) values(?,?,?)");
            crs.setInt(1, currentUser.getUserID());
            crs.setInt(2, requestPhotoId);
            crs.setString(3, newComment);
            crs.execute();
            crs.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI()+"?photoID="+requestPhotoId);
        }catch(Exception e){
            System.out.println("errorAdComment: "+e.getMessage());
        }
    }
    public void deleteComment(int commentID){
        try{
            crs.setCommand("delete from comments where commentid =?");
            crs.setInt(1, commentID);
            crs.execute();
            crs.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI()+"?photoID="+requestPhotoId);
        }catch(Exception e){
            System.out.println("errorAdComment: "+e.getMessage());
        }
    }
    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String comment) {
        this.newComment = comment;
    }
    

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isIsLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setIsLikedByCurrentUser(boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }

    public User getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(User postAuthor) {
        this.postAuthor = postAuthor;
    }

    public int getRequestPhotoId() {
        return requestPhotoId;
    }

    public void setRequestPhotoId(int requestPhotoId) {
        this.requestPhotoId = requestPhotoId;
    }

    public Photo getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(Photo postPhoto) {
        this.postPhoto = postPhoto;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public String addSelectedPhoto(int photoID){
        postPhoto.setPhotoID(photoID);
        populate();
        return "post";
    }
    private void addPhotoDetails(){
        postPhoto = new Photo();
        postAuthor = new User();
        try{
            crs.setCommand("select * from photos,users where photos.USERID = users.USERID and photoid=?");
            crs.setInt(1, requestPhotoId);
            crs.execute();
            while(crs.next()){
                postPhoto.setPhotoID(crs.getInt("photoID"));
                postPhoto.setCaption(crs.getString("caption"));
                postPhoto.setLocation(crs.getString("location"));
                postPhoto.setPrice(crs.getDouble("price"));
                postPhoto.setUserID(crs.getInt("userid"));
                postPhoto.setPhotoSrc(crs.getString("photosrc"));
                postAuthor.setUserID(crs.getInt("userid"));
                postAuthor.setFirstName(crs.getString("firstname"));
                postAuthor.setLastName(crs.getString("lastName"));
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void addPhotoLikes(){
        likes = 0;
        isLikedByCurrentUser = false;
        try{
            crs.setCommand("select count(*) from likes where photoid=?");
            crs.setInt(1, postPhoto.getPhotoID());
            crs.execute();
            while(crs.next()){
                likes = crs.getInt(1);
            }
            crs.close();
            crs.setCommand("select * from likes where photoid = ?");
            crs.setInt(1, postPhoto.getPhotoID());
            crs.execute();
            while(crs.next()){
                int userID = crs.getInt("userid");
                if(userID==currentUser.getUserID()){
                    isLikedByCurrentUser = true;
                }
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void addPhotoComments(){
        comments = new ArrayList<>();
        try{            
            crs.setCommand("select * from comments,users where users.userid=comments.userid and photoid=?");
            crs.setInt(1, postPhoto.getPhotoID());
            crs.execute();
            while(crs.next()){
                Comment c= new Comment();
                c.setPhotoID(postPhoto.getPhotoID());
                c.setCommentID(crs.getInt("commentid"));
                c.setText(crs.getString("text"));
                c.setUserID(crs.getInt("userID"));
                c.setUsername(crs.getString("username"));
                comments.add(c);
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void populate(){
        addPhotoDetails();
        addPhotoLikes();
        addPhotoComments();
    }
    public Post() {
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            crs=RowSetProvider.newFactory().createCachedRowSet();
            System.out.println(Singleton.getInstance().getDB());
            crs.setUrl(Singleton.getInstance().getDB());
            crs.setUsername(Singleton.getInstance().getUser());
            crs.setPassword(Singleton.getInstance().getPassword());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
