/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@SessionScoped
@ManagedBean
public class User implements Serializable{
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private int userID;
    private boolean priv;
    private ArrayList<Photo> profilePhotos;
    private CachedRowSet crs = null;

    public User() {
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ArrayList<Photo> getProfilePhotos() {        
        return profilePhotos;
    }

    public void setProfilePhotos(ArrayList<Photo> profilePhotos) {
        this.profilePhotos = profilePhotos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }
}
