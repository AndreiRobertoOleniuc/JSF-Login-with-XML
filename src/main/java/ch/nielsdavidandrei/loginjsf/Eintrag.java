/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.nielsdavidandrei.loginjsf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Andrei Oleniuc
 */
public class Eintrag {

    private int id;
    private String name;
    private Date date;
    private String nachricht;

    public Eintrag(int id, String name, Date date, String nachricht) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.nachricht = nachricht;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

}
