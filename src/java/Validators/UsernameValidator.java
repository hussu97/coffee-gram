/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validators;

import Beans.Singleton;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
@FacesValidator("com.Validators.UsernameValidator")
public class UsernameValidator implements Validator{

    CachedRowSet crs;
    public UsernameValidator(){
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
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try{
            crs.setCommand("select * from users where username=?");
            crs.setString(1, value.toString());
            crs.execute();
            while(crs.next()){
                FacesMessage msg = new FacesMessage("Username validation failed.", "Username already exists");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
