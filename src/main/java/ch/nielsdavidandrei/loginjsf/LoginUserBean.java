/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.nielsdavidandrei.loginjsf;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Alexander Flick
 */
@Named(value = "loginUserBean")
@SessionScoped
public class LoginUserBean implements Serializable {

    private String username;
    private String password;
    private boolean loggedIn;
    private String nachricht;
    LoginDAO check;
    List<Eintrag> eintrage;

    public String doLogin() throws JDOMException, IOException, ParseException {
        check = new LoginDAO();
        if (check.check(username, password)) {
            this.loggedIn = true;
            return "/secured/welcome?faces-redirect=true";
        } else {
            return "/faces/start";
        }
    }

    public void eintragen() throws JDOMException, IOException {
        new LoginDAO().setEintrag(username, this.getNachricht());
        this.setNachricht("");
    }

    // Getters & Setters
    public List<Eintrag> getEintrage() throws JDOMException, IOException, ParseException {
        this.eintrage = new LoginDAO().getData();
        return eintrage;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getNachricht() {
        return nachricht;
    }

}
