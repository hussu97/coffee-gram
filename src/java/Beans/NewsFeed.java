/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Helpers.DBGenerators;
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
        try{
            crs.setCommand("select * from followings,photos,users,locations where photos.USERID=users.USERID and photos.locationid=locations.locationid and followerUserID=? and users.userid != ? and users.PRIVACY = false");
            crs.setInt(1, currentUser.getUserID());
            crs.setInt(2, currentUser.getUserID());
            crs.execute();
            feedPhotos = DBGenerators.buildPhotoList(crs);
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
