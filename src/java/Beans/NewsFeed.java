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
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@SessionScoped
@ManagedBean
public class NewsFeed implements Serializable {

    @ManagedProperty(value="#{currentUser}")
    private CurrentUser currentUser;
    private CachedRowSet crs;
    private ArrayList<Photo> feedPhotos;
    private int profileUserID;
    
    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public int getProfileUserID() {
        return profileUserID;
    }

    public void setProfileUserID(int profileUserID) {
        this.profileUserID = currentUser.getUserID();
    }

    public ArrayList<Photo> getFeedPhotos() {
        feedPhotos = new ArrayList<>();
        try{
            crs.setCommand("select * from followings,photos where followerUserID=? and userid != ?");
            System.out.println("hi: "+currentUser.getUserID());
            crs.setInt(1, currentUser.getUserID());
            crs.setInt(2, currentUser.getUserID());
            crs.execute();
            while(crs.next()){
                Photo p = new Photo();
                p.setUserID(crs.getInt("userid"));
                p.setPhotoID(crs.getInt("photoID"));
                p.setLocation(crs.getString("location"));
                p.setCaption(crs.getString("caption"));
                p.setPhotoSrc(crs.getString("photosrc"));
                p.setPrice(crs.getDouble("price"));
                feedPhotos.add(p);
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return feedPhotos;
    }

    public void setFeedPhotos(ArrayList<Photo> feedPhotos) {
        this.feedPhotos = feedPhotos;
    }
    public NewsFeed() {
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
