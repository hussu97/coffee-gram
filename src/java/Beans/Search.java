/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp
 */
@ManagedBean
@SessionScoped
public class Search implements Serializable{
    private String searchText;
    private String searchType;
    private ArrayList<User> userResults;
    private ArrayList<Photo> locationResults;

    public Search() {
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public ArrayList<User> getUserResults() {
        return userResults;
    }

    public void setUserResults(ArrayList<User> userResults) {
        this.userResults = userResults;
    }

    public ArrayList<Photo> getLocationResults() {
        return locationResults;
    }

    public void setLocationResults(ArrayList<Photo> locationResults) {
        this.locationResults = locationResults;
    }
    
    public String search(){
        return "";
    }
    
}
