/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import Beans.Photo;
import Beans.User;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author hp
 */
public class DBGenerators {
    public static ArrayList < Photo > buildPhotoList(CachedRowSet crs) throws SQLException {
        ArrayList <Photo> res = new ArrayList <> ();
        System.out.println("aaaa");
        while (crs.next()) {
            System.out.println("bbbb");
            Photo p = new Photo();
            p.setUserID(crs.getInt("userid"));
            p.setPhotoID(crs.getInt("photoID"));
            p.setLocation(crs.getString("locationname"));
            p.setLocationID(crs.getInt("locationid"));
            p.setCaption(crs.getString("caption"));
            p.setPhotoSrc(crs.getString("photosrc"));
            p.setPrice(crs.getDouble("price"));
            p.setTimestamp(crs.getTimestamp("ts"));
            p.setDate(DateTimeConvertor.timeStampToDate(p.getTimestamp()));
            res.add(p);
        }
        crs.close();
        for (Photo p: res) {
            crs.setCommand("select count(*) from likes where photoid=?");
            crs.setInt(1, p.getPhotoID());
            crs.execute();
            while (crs.next())
                p.setLikeCount(crs.getInt(1));
            crs.close();
            crs.setCommand("select count(*) from comments where photoid=?");
            crs.setInt(1, p.getPhotoID());
            crs.execute();
            while (crs.next())
                p.setCommentCount(crs.getInt(1));
            crs.close();
        }
        return res;
    }
    public static ArrayList < User > buildUserList(CachedRowSet crs) throws SQLException{
        ArrayList <User> res = new ArrayList <> ();
        System.out.println("dddd");
        while(crs.next()){
            System.out.println("eeee");
                User u = new User();
                u.setFirstName(crs.getString("firstName"));
                u.setLastName(crs.getString("lastName"));
                u.setPriv(crs.getBoolean("privacy"));
                u.setUserID(crs.getInt("userID"));
                u.setUsername(crs.getString("username"));
                u.setTs(crs.getTimestamp("ts"));
                u.setDateCreated(DateTimeConvertor.timeStampToDate(u.getTs()));
                res.add(u);
            }
        crs.close();
        for(User u:res){
            crs.setCommand("select count(*) from followings where followeruserid=?");
            crs.setInt(1, u.getUserID());
            crs.execute();
            while(crs.next())
                u.setFollowingCount(crs.getInt(1));
            crs.close();
            crs.setCommand("select count(*) from followings where followinguserid=?");
            crs.setInt(1, u.getUserID());
            crs.execute();
            while(crs.next())
                u.setFollowerCount(crs.getInt(1));
            crs.close();
        }
        return res;
    }
}