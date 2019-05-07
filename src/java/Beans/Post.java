/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
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
    private int likes;
    private ArrayList<Comment> comments;
    private CachedRowSet crs;

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
        return "viewPost.xhtml";
    }
    public void populate(){
        postPhoto = new Photo();
        System.out.println(requestPhotoId);
        try{
            crs.setCommand("select * from photos,users where photoid=?");
            crs.setInt(1, requestPhotoId);
            crs.execute();
            while(crs.next()){
                postPhoto.setPhotoID(requestPhotoId);
                postPhoto.setCaption(crs.getString("caption"));
                postPhoto.setLocation(crs.getString("location"));
                postPhoto.setPrice(crs.getDouble("price"));
                postPhoto.setUserID(crs.getInt("userid"));
                postPhoto.setPhotoSrc(crs.getString("photosrc"));
                System.out.println(postPhoto.getPhotoSrc());
            }
            crs.close();
            crs.setCommand("select count(*) from photos where photoid=?");
            crs.setInt(1, postPhoto.getPhotoID());
            while(crs.next()){
                likes = crs.getInt(1);
            }
            System.out.println(likes);
            crs.close();
            comments = new ArrayList<>();
            crs.setCommand("select * from comments where photoid=?");
            crs.setInt(1, postPhoto.getPhotoID());
            crs.execute();
            while(crs.next()){
                Comment c= new Comment();
                c.setPhotoID(postPhoto.getPhotoID());
                c.setCommentID(crs.getInt("commentid"));
                c.setText(crs.getString("text"));
                c.setUserID(crs.getInt("userID"));
                comments.add(c);
            }
        }catch(Exception e){}
    }
    public Post() {
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            crs=RowSetProvider.newFactory().createCachedRowSet();
            crs.setUrl("jdbc:derby://localhost:1527/coffee-gram");
            crs.setUsername("guest");
            crs.setPassword("1234");
        } catch(Exception e){}
    }
    
}
