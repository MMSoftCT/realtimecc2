/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realtimecc2.gui;

import com.realtimecc2.Helper;
import com.realtimecc2.SpringFXMLLoader;
import com.realtimecc2.dao.ZeitRepository;
import com.realtimecc2.model.Personen;
import com.realtimecc2.model.Zeiten;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author amederake
 */
@Controller
public class AnAbmeldungController implements Initializable
{

    @Autowired
    ApplicationContext springContext;
    
    @Autowired
    ZeitRepository zeiten;

    // class variables
    private Personen ma;              // person object
    private Zeiten zt;                // timesheet
    @FXML
    private Label lblName;          // name of person
    @FXML
    private Button btnAktion;       // button for an/abmelden
    @FXML
    private Button btnStatistik;    // button for person statistik
    @FXML
    private Label taMessage;        // message label
    @FXML
    private Label lblTitle;
    @FXML
    private Button btnClose;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
	btnAktion.setOnAction((event) ->
	{
	    if (btnAktion.getText().equals("Anmelden"))
	    {
		setKommen();
	    } else
	    {
		setGehen();
	    }
	});

	btnStatistik.setOnAction((event) ->
	{
	    showStatistik();
	});

	taMessage.setText("Es liegen keine Nachrichten vor!");
    }

        @FXML
    private void close(ActionEvent event)
    {
	System.exit(0);
    }


    /**
     * set data
     *
     * @param p person object
     */
    public void setData(Personen p)
    {
	ma = p;
	lblName.setText(ma.getVorname() + " " + ma.getNachname());
	Date heute = Date.valueOf(LocalDate.now());
	zt = zeiten.getByPersonidAndDatum(p.getId(), heute);
	if (zt == null)
	{
	    zt = new Zeiten();
	    zt.setPersonid(p.getId());
	    zt.setDatum(heute);
	    zt.setKommen(Time.valueOf(LocalTime.now()));
	    zt.setGehen(Time.valueOf(LocalTime.now()));
	    btnAktion.setText("Anmelden");
	} else
	{
	    zt.setGehen(Time.valueOf(LocalTime.now()));
	    btnAktion.setText("Abmelden");
	}
	btnStatistik.setDisable(zeiten.findByPersonid(ma.getId()).isEmpty());
	double soll = calcSoll();
	double ist = calcIst();
	if (soll > 0.0 && ist > (soll * 1.1))
	{
	    taMessage.setText("Sie haben mehr als 10% Überstunden!");
	} else if (soll > 0.0 && ist < (soll * 0.9))
	{
	    taMessage.setText("Sie haben mehr als 10% Fehlzeiten!");
	} else
	{
	    taMessage.setText("Es liegen keine Nachrichten vor!");
	}
    }

    /**
     * Anmelden button action
     */
    private void setKommen()
    {
	zeiten.save(zt);
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("ComCave Zeiterfassung");
	alert.setHeaderText(null);
	alert.setContentText("Ihr Arbeitsbeginn wurde gespeichert!");

	alert.showAndWait();
	btnAktion.setDisable(true);
    }

    /**
     * Abmelden button action
     */
    private void setGehen()
    {
	zeiten.save(zt);
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("ComCave Zeiterfassung");
	alert.setHeaderText(null);
	alert.setContentText("Ihr Arbeitsende wurde gespeichert!");

	alert.showAndWait();
	btnAktion.setDisable(true);
    }

    /**
     * schom time statistik of given person
     *
     * @param get
     */
    private void showStatistik()
    {
	ArrayList<Zeiten> zd = (ArrayList) zeiten.findByPersonid(ma.getId());
	if (zd.size() > 0)
	{
	    try
	    {
		Dialog dlg = new Dialog<>();
		DialogPane pane = (DialogPane) springContext.getBean(SpringFXMLLoader.class).load("/fxml/StatDialog.fxml");
		dlg.setDialogPane(pane);
		StatDialogController ctrl = (StatDialogController) springContext.getBean(StatDialogController.class);
		ctrl.setData(zd, LocalDate.now());
		ctrl.setHeader("Zeitübersicht");
		ctrl.setName(ma.getVorname() + " " + ma.getNachname());
		dlg.initOwner(btnClose.getScene().getWindow());

		dlg.showAndWait();

	    } catch (IOException ex)
	    {
		Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    /**
     * calculate working time they should have
     * uses helper class methode
     *
     * @return woking time till now
     */
    private double calcSoll()
    {
	double ret = 0.0;
	LocalDate now = LocalDate.now();
	if (now.getDayOfMonth() > 1)
	{
	    LocalDate from = now.minusDays(now.getDayOfMonth() - 1);
	    ret = zeiten.findByPersonidAndDatumBetween(ma.getId(), Date.valueOf(from), Date.valueOf(now)).size() * 8.0;
	}
	return ret;
    }

    /**
     * calculate working time they have
     * uses helper class mthode
     *
     * @return aktual workingtime for this month
     */
    private double calcIst()
    {
	double ret = 0.0;
	LocalDate date = LocalDate.now();
	int count = date.getDayOfMonth();
	if (zeiten.findByPersonid(ma.getId()).size() > 0 && count > 1)
	{
	    for (int i = 1; i < count; i++)
	    {
		LocalDate tag = LocalDate.parse(String.format("%d-%02d-%02d", date.getYear(), date.getMonthValue(), i));
		Calendar cal = Calendar.getInstance();
		cal.setTime(Date.valueOf(date));
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		if (dow != Calendar.SUNDAY || dow != Calendar.SATURDAY)
		{
		    ret += Helper.getTimeForDay(zeiten.getByPersonidAndDatum(ma.getId(), Date.valueOf(tag)));
		}

	    }
	}
	return ret;
    }

}
