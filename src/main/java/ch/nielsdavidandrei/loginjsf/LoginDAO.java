/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.nielsdavidandrei.loginjsf;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Andrei Oleniuc
 */
public class LoginDAO {

    private ArrayList<User> allUser = new ArrayList<>();
    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    private List list;

    private ArrayList<Eintrag> gaeste = new ArrayList<>();
    String path2 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    private List list2;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public LoginDAO() throws JDOMException, IOException {
    }

    public Boolean check(String name, String password) throws JDOMException, IOException, ParseException {
        path = path + "WEB-INF\\Data.xml";
        Document doc = new SAXBuilder().build(path);
        Element users = doc.getRootElement();
        Element benutzer = users.getChild("users");
        list = benutzer.getChildren("nutzer");
        for (int i = 0; i < list.size(); i++) {
            Element node = (Element) list.get(i);
            int xmlid = Integer.parseInt(node.getChild("id").getText());
            String xmlname = node.getChild("name").getText();
            String xmlpassword = node.getChild("password").getText();
            allUser.add(new User(xmlid, xmlname, xmlpassword));
            if (name.equals(xmlname) && password.equals(xmlpassword)) {
                return true;
            }
        }
        return false;
    }

    public List<Eintrag> getData() throws JDOMException, IOException, ParseException {
        path2 = path2 + "WEB-INF\\Gaestebuch.xml";
        Document doc = new SAXBuilder().build(path2);
        Element gaeste = doc.getRootElement();
        Element guest = gaeste.getChild("gaestebuch");
        list2 = guest.getChildren("gast");
        for (int i = 0; i < list2.size(); i++) {
            Element node = (Element) list2.get(i);
            int xmlid = Integer.parseInt(node.getChild("id").getText());
            String xmlname = node.getChild("name").getText();
            Date xmldatum = format.parse(node.getChild("datum").getText() + " " + node.getChild("uhrzeit").getText());
            String xmlnachricht = node.getChild("nachricht").getText();
            this.gaeste.add(new Eintrag(xmlid, xmlname, xmldatum, xmlnachricht));
        }
        return this.gaeste;
    }

    public void setEintrag(String name, String nachricht) throws JDOMException, IOException {
        path2 = path2 + "WEB-INF\\Gaestebuch.xml";
        Document doc = new SAXBuilder().build(path2);
        Element gaeste = doc.getRootElement();
        Element guest = gaeste.getChild("gaestebuch");
        Element gast = new Element("gast");

        //ID
        Element id = new Element("id");
        int ident = guest.getChildren("gast").size() + 2;
        String ausgabeid = Integer.toString(ident);
        id.addContent(ausgabeid);
        //Name
        Element namexml = new Element("name");
        namexml.addContent(name);
        //Datum 
        Element datum = new Element("datum");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        datum.addContent(dtf.format(now));
        //Uhrzeit
        Element zeit = new Element("uhrzeit");
        DateTimeFormatter form = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime smallTime = LocalDateTime.now();
        zeit.addContent(form.format(smallTime));
        //Nachricht
        Element eintrag = new Element("nachricht");
        eintrag.addContent(nachricht);
        //Link them
        gast.addContent(id);
        gast.addContent(namexml);
        gast.addContent(datum);
        gast.addContent(zeit);
        gast.addContent(eintrag);
        //Link more
        guest.addContent(gast);

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(doc, new FileWriter("C:\\Users\\Andrei Oleniuc\\Desktop\\LoginJSF\\src\\main\\webapp\\WEB-INF\\Gaestebuch.xml"));
    }
}
