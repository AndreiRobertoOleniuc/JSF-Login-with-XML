/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.nielsdavidandrei.loginjsf;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.jdom2.JDOMException;

/**
 *
 * @author Alexander Flick
 */
@Named(value = "loginUserBean")
@SessionScoped
public class LoginUserBean implements Serializable {

    @Size(min = 1, message = "Please enter the Username")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username format is Wrong")
    private String username;

    @Size(min = 1, message = "Please enter the Password")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password format is Wrong")
    private String password;

    @Size(min = 1, message = "Please enter the Message")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Message format is Wrong")
    private String nachricht;

    private LoginDAO check;
    private List<Eintrag> eintrage;
    private boolean loggedIn;

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
