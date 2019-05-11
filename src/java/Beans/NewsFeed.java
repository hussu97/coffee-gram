/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Helpers.DateTimeConvertor;
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
    
    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Photo> getFeedPhotos() {
        feedPhotos = new ArrayList<>();
        try{
            crs.setCommand("select * from followings,photos,users where photos.USERID=users.USERID and followerUserID=? and users.userid != ? and users.PRIVACY = false");
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
                p.setTimestamp(crs.getTimestamp("ts"));
                p.setDate(DateTimeConvertor.timeStampToDate(p.getTimestamp()));
                feedPhotos.add(p);
            }
            crs.close();
            for(Photo p:feedPhotos){
                crs.setCommand("select count(*) from likes where photoid=?");
                crs.setInt(1, p.getPhotoID());
                crs.execute();
                while(crs.next())
                    p.setLikeCount(crs.getInt(1));
                crs.close();
                crs.setCommand("select count(*) from comments where photoid=?");
                crs.setInt(1, p.getPhotoID());
                crs.execute();
                while(crs.next())
                    p.setCommentCount(crs.getInt(1));
                crs.close();
            }
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
