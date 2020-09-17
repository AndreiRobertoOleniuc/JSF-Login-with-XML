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

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getNachricht() {
        return nachricht;
    }

    public String doLogin() throws JDOMException, IOException, ParseException {
        check = new LoginDAO();
        if (check.check(username, password)) {
            this.loggedIn = true;
            return "/secured/welcome?faces-redirect=true";
        } else {
            return "/faces/start";
        }
    }

    public void setEintrage() throws JDOMException, IOException, ParseException {
        check = new LoginDAO();
        eintrage = check.getData();
    }

    public void eintragen() throws JDOMException, IOException {
        check = new LoginDAO();
        check.setEintrag(username, nachricht);
    }

    public List<Eintrag> getEintrage() {
        return eintrage;
    }

    public String doLogOut() throws JDOMException, IOException, ParseException {
        return "/start";
    }

    // Getters & Setters
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

}
