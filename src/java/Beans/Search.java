/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Helpers.DBGenerators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

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
    private CachedRowSet crs;
    private Set<String> locationResultsNames;
   
    public Search() {
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

    public Set<String> getLocationResultsNames() {
        return locationResultsNames;
    }

    public void setLocationResultsNames(Set<String> locationResultsNames) {
        this.locationResultsNames = locationResultsNames;
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
        if(searchType.equals("location")){
            locationResults = new ArrayList<>();
            ArrayList<String> locationResultsNamesWithDup = new ArrayList<>();
            try{
                crs.setCommand("SELECT * FROM photos,locations WHERE photos.LOCATIONID=locations.LOCATIONID and LOWER(locationname) LIKE LOWER(?)");
                String searchQueryText = "%"+searchText+"%";
                crs.setString(1, searchQueryText);
                crs.execute();
                locationResults = DBGenerators.buildPhotoList(crs);
                for(Photo p:locationResults){
                    locationResultsNamesWithDup.add(p.getLocation());
                }
                locationResultsNamesWithDup.sort((obj1,obj2)->obj1.compareTo(obj2));
                locationResultsNames = new HashSet<String>(locationResultsNamesWithDup);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else{
            userResults = new ArrayList<>();
            try{
                System.out.println("aaaa");
                crs.setCommand("SELECT * FROM users WHERE LOWER(username) LIKE LOWER(?)");
                System.out.println("bbbb");
                String searchQueryText = "%"+searchText+"%";
                System.out.println("cccc");
                crs.setString(1, searchQueryText);
                crs.execute();
                userResults = DBGenerators.buildUserList(crs);
                userResults.sort((obj1,obj2)->obj1.getUsername().compareTo(obj2.getUsername()));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return "search.xhtml";
    }
    
}
