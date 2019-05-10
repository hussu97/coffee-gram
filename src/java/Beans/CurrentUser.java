/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Beans.Singleton;
import Hash.PasswordAuthentication;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@SessionScoped
@ManagedBean
public class CurrentUser implements Serializable{
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private int userID;
    private boolean priv;
    @ManagedProperty(value="#{photo}")
    private Photo newPost;
    private Photo post;
    private Part newPostPhoto;
    private String newPostExt;
    private CachedRowSet crs = null;
    private PasswordAuthentication pa;

    public CurrentUser() {
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            crs=RowSetProvider.newFactory().createCachedRowSet();
            System.out.println(Singleton.getInstance().getDB());
            crs.setUrl(Singleton.getInstance().getDB());
            crs.setUsername(Singleton.getInstance().getUser());
            crs.setPassword(Singleton.getInstance().getPassword());
            pa = new PasswordAuthentication();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Photo getPost() {
        return post;
    }

    public void setPost(Photo post) {
        this.post = post;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNewPostExt() {
        return newPostExt;
    }

    public void setNewPostExt(String newPostExt) {
        this.newPostExt = newPostExt;
    }

    public Part getNewPostPhoto() {
        return newPostPhoto;
    }

    public void setNewPostPhoto(Part newPostPhoto) {
        try {
            System.out.println("hi");
            InputStream in = null;
            this.newPostPhoto = newPostPhoto;
            in = newPostPhoto.getInputStream();
            newPostExt = getFileExtension(this.newPostPhoto).trim().toLowerCase();
            System.out.println(newPostExt);
            if(newPostExt.equals(".png")||newPostExt.equals(".jpg")){
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
                newPost.setPhotoSrc("IMG_"+sdf.format(new Date())+newPostExt);
                String destName = "C:\\Users\\hp\\One Drive\\OneDrive\\Documents\\NetBeansProjects\\coffee-gram-inc\\web\\resources\\images\\";
                Files.copy(in, new File(destName+newPost.getPhotoSrc()).toPath());
            } else{
                System.out.println(newPostExt);     
            }
            } catch (IOException ex) {
            Logger.getLogger(CurrentUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String getFileExtension(Part file) {
        String name = file.getSubmittedFileName();
            System.out.println(name);
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }   
    public Photo getNewPost() {
        return newPost;
    }

    public void setNewPost(Photo newPost) {
        this.newPost = newPost;
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
    
    public String createUser(){
        String securePassword = pa.hashPass(password.toCharArray());
        System.out.println(securePassword);
        try{
            crs.setCommand("insert into users (username,firstName,lastName,password,privacy) values (?,?,?,?,?)");
            crs.setString(1, username);
            crs.setString(2, firstName);
            crs.setString(3, lastName);
            crs.setString(4, securePassword);
            crs.setBoolean(5, true);
            crs.execute();
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "login";
    }
    public String createPost(){
        try{
            crs.setCommand("insert into photos (photosrc,caption,price,location,userID) values (?,?,?,?,?)");
            crs.setString(1, newPost.getPhotoSrc());
            crs.setString(2, newPost.getCaption());
            crs.setDouble(3, newPost.getPrice());
            crs.setString(4, newPost.getLocation());
            crs.setInt(5, userID);
            crs.execute();
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "newsfeed";
    }
    public String login(){
        try{
            crs.setCommand("select * from users where username=?");
            crs.setString(1, username);
            crs.execute();
            while(crs.next()){
                String token = crs.getString("password");
                if(!pa.authenticate(password.toCharArray(),token)){
                    throw new Exception("Passwords do not match");
                }
                firstName = crs.getString("firstName");
                userID = crs.getInt("userID");
                lastName = crs.getString("lastName");
                priv = crs.getBoolean("privacy");
            }
            if(crs.size()==0)
                return "login";
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "login";
        }
        return "newsfeed";
    }
    public String addPost(){
        newPost = new Photo();
        newPostPhoto = null;
        newPostExt = "";
        return "addPost.xhtml";
    }
    public String editPost(Photo p){
        post = p;
        return "editPost.xhtml";
    }
    public void updatePost()throws IOException{
        try{
            crs.setCommand("Update photos set caption=?,price=?,location=? where photoid=?");
            crs.setString(1, post.getCaption());
            crs.setDouble(2, post.getPrice());
            crs.setString(3, post.getLocation());
            crs.setInt(4, post.getPhotoID());
            crs.execute();
            crs.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String s = ((HttpServletRequest) ec.getRequest()).getRequestURI();
            int i = s.lastIndexOf('/');
            String res =  s.substring(0, i);
            System.out.println(res);
            ec.redirect(res+"/post.xhtml?photoID="+post.getPhotoID());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public String deletePost(){
        try{
            crs.setCommand("delete from photos where photoid=?");
            crs.setInt(1, post.getPhotoID());
            crs.execute();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "newsfeed.xhtml";
    }
}
