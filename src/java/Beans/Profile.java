/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Helpers.DBGenerators;
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
public class Profile implements Serializable{

    /**
     * Creates a new instance of Profile
     */
    private CachedRowSet crs = null;
    private User user;
    private int requestUserId;
    private ArrayList<User> following;
    private ArrayList<User> followers;

    public int getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(int requestUserId) {
        this.requestUserId = requestUserId;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Profile() {
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

    public ArrayList<User> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<User> following) {
        this.following = following;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }
    
    private void addUserDetails(){
        try{
            crs.setCommand("select * from users where userID=?");
            crs.setInt(1, requestUserId);
            crs.execute();
            while(crs.next()){
                user.setUserID(crs.getInt("userID"));
                user.setFirstName(crs.getString("firstname"));
                user.setLastName(crs.getString("lastname"));
                user.setPriv(crs.getBoolean("privacy"));
                user.setUsername(crs.getString("username"));
                user.setStatus(crs.getString("status"));
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void addUserPhotos(){
        try{
            crs.setCommand("select * from photos,locations where photos.locationid=locations.locationid and userID=?");
            crs.setInt(1, user.getUserID());
            crs.execute();
            ArrayList<Photo> photos = DBGenerators.buildPhotoList(crs);
            user.setProfilePhotos(photos);
            photos.sort((obj2,obj1)-> obj1.getTimestamp().compareTo(obj2.getTimestamp()));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void addFollowerList(){
        followers = new ArrayList<>();
        try{
            crs.setCommand("select * from users where userid in (select followeruserID from users,followings where followingUserID=userID and userID=?)");
            crs.setInt(1, user.getUserID());
            crs.execute();
            followers = DBGenerators.buildUserList(crs);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void addFollowingList(){
        following = new ArrayList<>();
        try{
            crs.setCommand("select * from users where userid in (select followinguserID from users,followings where followerUserID=userID and userID=?)");
            crs.setInt(1, user.getUserID());
            crs.execute();
            following = DBGenerators.buildUserList(crs);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void populate(){
        user = new User();
        addUserDetails();
        addUserPhotos();
        addFollowerList();
        addFollowingList();
    }
    public boolean isUserFollowed(int currentUserId){
        System.out.println(followers.size());
        for(User i:followers){
            System.out.println(i.getUserID());
            if(i.getUserID()==currentUserId){
                return true;
            }
        }
        return false;
    }
    public String addUserID(int id){
        return "profile.xhtml"; 
    }
}
