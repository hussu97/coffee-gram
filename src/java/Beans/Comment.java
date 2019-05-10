/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@Named(value = "comment")
@Dependent
public class Comment {

    /**
     * Creates a new instance of Comment
     */
    private String text;
    private int userID;
    private int photoID;
    private int commentID;
    private String username;
    public void deleteComment(){
        try{
            crs.setCommand("delete from comments where commentid =?");
            crs.setInt(1, commentID);
            crs.execute();
            crs.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI()+"?photoID="+photoID);
        }catch(Exception e){
            System.out.println("errorAdComment: "+e.getMessage());
        }
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }
    CachedRowSet crs;
    public Comment() {
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
