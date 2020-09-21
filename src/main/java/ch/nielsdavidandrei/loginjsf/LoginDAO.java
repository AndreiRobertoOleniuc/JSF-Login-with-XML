/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.nielsdavidandrei.loginjsf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final SAXBuilder builderBenutzer;
    private final SAXBuilder builderEintraege;
    private final File xmlFileBenutzer;
    private final File xmlFileEintraege;

    public LoginDAO() throws JDOMException, IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path + "WEB-INF\\Data.xml";
        xmlFileBenutzer = new File(path);
        String pathB = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        pathB = pathB + "WEB-INF\\Gaestebuch.xml";
        xmlFileEintraege = new File(pathB);
        builderBenutzer = new SAXBuilder();
        builderEintraege = new SAXBuilder();
    }

    //Check Correct
    public Boolean check(String name, String password) throws JDOMException, IOException, ParseException {
        Document doc = (Document) builderBenutzer.build(xmlFileBenutzer);
        Element users = doc.getRootElement();
        Element benutzer = users.getChild("users");
        List list = benutzer.getChildren("nutzer");
        for (int i = 0; i < list.size(); i++) {
            Element node = (Element) list.get(i);
            String xmlname = node.getChild("name").getText();
            String xmlpassword = node.getChild("password").getText();
            if (name.equals(xmlname) && password.equals(xmlpassword)) {
                return true;
            }
        }
        return false;
    }

    public List getData() throws JDOMException, IOException, ParseException {
        List<Eintrag> gue;
        gue = new ArrayList();
        Document doc = (Document) builderEintraege.build(xmlFileEintraege);
        Element gaeste = doc.getRootElement();
        Element guest = gaeste.getChild("gaestebuch");
        List list2 = guest.getChildren("gast");
        for (int i = 0; i < list2.size(); i++) {
            Element node = (Element) list2.get(i);
            int xmlid = Integer.parseInt(node.getChild("id").getText());

            String xmlname = node.getChild("name").getText();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String datumInString = node.getChildText("datum");
            String timeInString = node.getChildText("uhrzeit");
            Date xmldatum = formatter.parse(datumInString + " " + timeInString);

            String xmlnachricht = node.getChild("nachricht").getText();
            gue.add(new Eintrag(xmlid, xmlname, xmldatum, xmlnachricht));
        }
        return gue;
    }

    public boolean setEintrag(String name, String nachricht) throws JDOMException, IOException {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String datum = formatter.format(today);
        formatter = new SimpleDateFormat("HH:mm");
        String zeit = formatter.format(today);
        Document document = (Document) builderEintraege.build(xmlFileEintraege);
        Element gaeste = document.getRootElement();
        Element guest = gaeste.getChild("gaestebuch");
        Element gast = new Element("gast");

        gast.addContent(new Element("id").setText(Integer.toString(this.maxIdinXML() + 1)));
        gast.addContent(new Element("name").setText(name));
        gast.addContent(new Element("datum").setText(datum));
        gast.addContent(new Element("uhrzeit").setText(zeit));
        gast.addContent(new Element("nachricht").setText(nachricht));

        guest.addContent(gast);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, new FileWriter(xmlFileEintraege));
        return true;
    }

    //Hilfsmethode um max ID-Wert zu erhalten
    private int maxIdinXML() {
        //Vergleichswert setzen
        int tmp = Integer.MAX_VALUE;
        try {
            Document document = (Document) builderEintraege.build(xmlFileEintraege);
            Element rootNode = document.getRootElement();
            Element guest = rootNode.getChild("gaestebuch");
            List list = guest.getChildren("gast");
            Element node = (Element) list.get(0);
            tmp = Integer.parseInt(node.getChildText("id"));
            for (int i = 1; i < list.size(); i++) {
                node = (Element) list.get(i);
                if (tmp < Integer.parseInt(node.getChildText("id"))) {
                    tmp = Integer.parseInt(node.getChildText("id"));
                }
            }
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
}
