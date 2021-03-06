/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Beans.Singleton;
import Helpers.DateTimeConvertor;
import Helpers.PasswordAuthentication;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
    @ManagedProperty(value="#{user}")
    private User userDetails;
    @ManagedProperty(value="#{photo}")
    private Photo newPost;
    private Photo post;
    private Part newPostPhoto;
    private String newPostExt;
    private ArrayList<SelectItem> locations;
    private CachedRowSet crs = null;
    private PasswordAuthentication pa;
    private boolean isLoggedIn;
    
    private UIComponent loginButton;
    private UIComponent registerButton;
    private UIComponent addCoffeeButton;
    private UIComponent editCoffeeButton;
    private String confirmPass;

    public UIComponent getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(UIComponent registerButton) {
        this.registerButton = registerButton;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public UIComponent getAddCoffeeButton() {
        return addCoffeeButton;
    }

    public void setAddCoffeeButton(UIComponent addCoffeeButton) {
        this.addCoffeeButton = addCoffeeButton;
    }

    public UIComponent getEditCoffeeButton() {
        return editCoffeeButton;
    }

    public void setEditCoffeeButton(UIComponent editCoffeeButton) {
        this.editCoffeeButton = editCoffeeButton;
    }
    

    public UIComponent getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(UIComponent loginButton) {
        this.loginButton = loginButton;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    public Photo getPost() {
        return post;
    }

    public void setPost(Photo post) {
        this.post = post;
    }
    

    public ArrayList<SelectItem> getLocations() {
        locations = new ArrayList<>();
        try{
            crs.setCommand("select * from locations");
            crs.execute();
            while(crs.next()){
                locations.add(new SelectItem(crs.getInt("locationid"),crs.getString("locationname"),""));
            }
            locations.sort((obj1,obj2)-> obj1.getLabel().compareTo(obj2.getLabel()));
        }catch(Exception e){
            
        }
        return locations;
    }

    public void setLocations(ArrayList<SelectItem> locations) {
        this.locations = locations;
    }

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
                String destName = "C:\\Users\\hp\\inc-images\\";
//                String destName = "C:\\Users\\hp\\One Drive\\OneDrive\\Documents\\NetBeansProjects\\coffee-gram-inc\\web\\resources\\images\\";
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
    //=============================================================
    //                          USER
    //============================================================
    public String createUser(){
        if(!confirmPass.equals(userDetails.getPassword())){
            FacesMessage message = new FacesMessage("Passwords do not match");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(registerButton.getClientId(context), message);
            return null;
        }
        String securePassword = pa.hashPass(userDetails.getPassword().toCharArray());
        System.out.println(securePassword);
        try{
            crs.setCommand("select * from users where username=?");
            crs.setString(1, userDetails.getUsername());
            crs.execute();
            while(crs.next()){
                FacesMessage message = new FacesMessage("Username already exists");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(registerButton.getClientId(context), message);
                return null;
            }
            crs.close();
            crs.setCommand("insert into users (username,firstName,lastName,password,privacy) values (?,?,?,?,?)");
            crs.setString(1, userDetails.getUsername());
            crs.setString(2, userDetails.getFirstName());
            crs.setString(3, userDetails.getLastName());
            crs.setString(4, securePassword);
            crs.setBoolean(5, true);
            crs.execute();
            crs.close();
            userDetails.setPassword("");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "login";
    }
    public void updateProfile()throws IOException{
        try{
            crs.setCommand("Update users set firstname=?,lastname=?,status=? where userid=?");
            crs.setString(1, userDetails.getFirstName());
            crs.setString(2, userDetails.getLastName());
            crs.setString(3,userDetails.getStatus());
            crs.setInt(4, userDetails.getUserID());
            crs.execute();
            crs.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String s = ((HttpServletRequest) ec.getRequest()).getRequestURI();
            int i = s.lastIndexOf('/');
            String res =  s.substring(0, i);
            System.out.println(res);
            ec.redirect(res+"/profile.xhtml?userID="+userDetails.getUserID());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    //=============================================================
    //                          REGISTRATION/AUTHENTICATION
    //============================================================
    public String clearRegister(){
        userDetails.setUsername("");
        userDetails.setFirstName("");
        userDetails.setLastName("");
        userDetails.setPassword("");
        confirmPass = "";
        return "register";
    }
    public String login(){
        try{
            crs.setCommand("select * from users where username=?");
            crs.setString(1, userDetails.getUsername());
            crs.execute();
            while(crs.next()){
                String token = crs.getString("password");
                if(!pa.authenticate(userDetails.getPassword().toCharArray(),token)){
                    FacesMessage message = new FacesMessage("Password is incorrect");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(loginButton.getClientId(context), message);
                    return null;
                }
                userDetails.setFirstName(crs.getString("firstName"));
                userDetails.setUserID(crs.getInt("userID"));
                userDetails.setLastName(crs.getString("lastName"));
                userDetails.setPriv(crs.getBoolean("privacy"));
                userDetails.setStatus(crs.getString("status"));
                userDetails.setTs(crs.getTimestamp("ts"));
                userDetails.setDateCreated(DateTimeConvertor.timeStampToDate(userDetails.getTs()));
            }
            if(crs.size()==0){
                FacesMessage message = new FacesMessage("Username is incorrect");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(loginButton.getClientId(context), message);
                return null;
            }
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "login";
        }
        setIsLoggedIn(true);
        return "newsfeed";
    }
    public String logout(){
        setIsLoggedIn(false);
        return "login";
    }
    
    //=============================================================
    //                          POSTS
    //============================================================
    public String addPost(){
        newPost = new Photo();
        newPostPhoto = null;
        newPostExt = "";
        return "add-post";
    }
    public String editPost(Photo p){
        post = p;
        return "edit-post";
    }
    public String createPost(){
        try{
            if(newPost.getLocationID()==0){
                if(newPost.getLocation().isEmpty()){
                    FacesMessage message = new FacesMessage("Please enter a location");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(addCoffeeButton.getClientId(context), message);
                    return null;
                }
                crs.setCommand("insert into locations (locationname) values (?)");
                crs.setString(1, newPost.getLocation());
                try{
                    crs.execute();
                }catch(Exception e){}
                crs.setCommand("select * from locations where locationname=?");
                crs.setString(1, newPost.getLocation());
                crs.execute();
                while(crs.next()){
                    newPost.setLocationID(crs.getInt("locationid"));
                }    
            }
            crs.setCommand("insert into photos (photosrc,caption,price,locationid,userID) values (?,?,?,?,?)");
            crs.setString(1, newPost.getPhotoSrc());
            crs.setString(2, newPost.getCaption());
            crs.setDouble(3, newPost.getPrice());
            crs.setInt(4, newPost.getLocationID());
            crs.setInt(5, userDetails.getUserID());
            crs.execute();
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "newsfeed";
    }
    public void updatePost()throws IOException{
        try{
            if(post.getLocationID()==0){
                System.out.println("hi");
                if(post.getLocation().isEmpty()){
                    FacesMessage message = new FacesMessage("Please enter a location");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(editCoffeeButton.getClientId(context), message);
                    return;
                }
                crs.setCommand("insert into locations (locationname) values (?)");
                crs.setString(1, post.getLocation());
                try{
                    crs.execute();
                }catch(Exception e){}
                crs.setCommand("select * from locations where locationname=?");
                crs.setString(1, post.getLocation());
                crs.execute();
                while(crs.next()){
                    post.setLocationID(crs.getInt("locationid"));
                }    
            }
            crs.setCommand("Update photos set caption=?,price=?,locationid=? where photoid=?");
            crs.setString(1, post.getCaption());
            crs.setDouble(2, post.getPrice());
            crs.setInt(3, post.getLocationID());
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
            crs.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "newsfeed";
    }
}
