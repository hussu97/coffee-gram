/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Helpers.DateTimeConvertor;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author 
 */
public class Singleton {
    private static Singleton instance=null;
    private String DB;
    private String user;
    private String password;

    public Singleton() throws Exception
    {
        System.out.println("Singleton constructor called");
        if(instance==null)
            instance=this;
        else
            throw new Exception("Only one Singleton is allowed");
    }
    public static Singleton getInstance() {
        return instance;
    }

    public static void setInstance(Singleton instance) {
        Singleton.instance = instance;
    }

    public String getDB() {
        return DB;
    }

    public void setDB(String DB) {
        this.DB = DB;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
            
}