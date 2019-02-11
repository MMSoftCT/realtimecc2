/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realtimecc2.gui;

import com.realtimecc2.SpringFXMLLoader;
import com.realtimecc2.dao.PersonRepository;
import com.realtimecc2.dao.ZeitRepository;
import com.realtimecc2.model.Personen;
import com.realtimecc2.model.Zeiten;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author micim
 */
@Controller
//@Scope("prototype")
public class PersonalController
{

    @Autowired
    PersonRepository personen;

    @Autowired
    ZeitRepository zeiten;

    @Autowired
    ApplicationContext springContext;

    @FXML
    private MenuBar mbMenu;
    @FXML
    private Menu mFile;
    @FXML
    private MenuItem miClose;
    @FXML
    private Menu mEdit;
    @FXML
    private MenuItem miAnlegen;
    @FXML
    private MenuItem miChange;
    @FXML
    private MenuItem miState;
    @FXML
    private Menu mHelp;
    @FXML
    private MenuItem miAbout;
    @FXML
    private TableView<Personen> tblPerson;
    @FXML
    private ContextMenu ctxMenu;
    @FXML
    private MenuItem cmAdd;
    @FXML
    private MenuItem cmChange;
    @FXML
    private SeparatorMenuItem cmSep;
    @FXML
    private MenuItem cmStat;
    @FXML
    private TableColumn<Personen, Long> colId;
    @FXML
    private TableColumn<Personen, String> colVorname;
    @FXML
    private TableColumn<Personen, String> colNachname;
    @FXML
    private TableColumn<Personen, String> colPosition;
    @FXML
    private TableColumn<Personen, String> colStandort;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    public void initialize()
    {
	initTable();
	initMenu();
	UpdateUi();
    }

    /**
     * initialize person table
     */
    private void initTable()
    {
	System.out.println("Initialize Table");
	tblPerson.setItems(FXCollections.observableArrayList((ArrayList) personen.findByRolle("Mitarbeiter")));
	colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
	colVorname.setCellValueFactory(new PropertyValueFactory<>("Vorname"));
	colNachname.setCellValueFactory(new PropertyValueFactory<>("Nachname"));
	colPosition.setCellValueFactory(new PropertyValueFactory<>("Position"));
	colStandort.setCellValueFactory(new PropertyValueFactory<>("Standort"));
	tblPerson.getSelectionModel().selectedItemProperty().
		addListener(new ChangeListener<Personen>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Personen> observable, Personen oldValue, Personen newValue)
		    {
			UpdateUi();
		    }
		});
    }

    /**
     * initialize menu item actions
     */
    private void initMenu()
    {
	miClose.setOnAction((event) ->
	{
	    System.exit(0);
	});

	miAnlegen.setOnAction((event) ->
	{
	    editPerson(new Personen());
	});

	cmAdd.setOnAction((event) ->
	{
	    editPerson(new Personen());
	});

	miChange.setOnAction((event) ->
	{
	    editPerson(tblPerson.getSelectionModel().selectedItemProperty().get());
	});

	cmChange.setOnAction((event) ->
	{
	    editPerson(tblPerson.getSelectionModel().selectedItemProperty().get());
	});

	miState.setOnAction((event) ->
	{
	    showStatistik(tblPerson.getSelectionModel().selectedItemProperty().get());
	});

	cmStat.setOnAction((event) ->
	{
	    showStatistik(tblPerson.getSelectionModel().selectedItemProperty().get());
	});

	miAbout.setOnAction((event) ->
	{
	    showAbout();
	});
    }

    /**
     * edit given person data
     *
     * @param get
     */
    private void editPerson(Personen get)
    {
	try
	{
	    Dialog<ButtonType> dlg = new Dialog<>();
	    DialogPane pane = (DialogPane) springContext.getBean(SpringFXMLLoader.class).load("/fxml/PersonDialog.fxml");
	    dlg.setDialogPane(pane);
	    PersonDialogController controler = (PersonDialogController) springContext.getBean(PersonDialogController.class);
	    controler.setData(get);
	    dlg.initOwner(mbMenu.getScene().getWindow());

	    Optional<ButtonType> result = dlg.showAndWait();

	    if (result.isPresent())
	    {
		if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
		{
		    personen.save(controler.getData());
		    tblPerson.setItems(FXCollections.observableArrayList((ArrayList) personen.findByRolle("Mitarbeiter")));
		    UpdateUi();
		}

	    }
	} catch (IOException ex)
	{
	    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * schom time statistik of given person
     *
     * @param get
     */
    private void showStatistik(Personen get)
    {
	ArrayList<Zeiten> zd = (ArrayList) zeiten.findByPersonid(get.getId());
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
		ctrl.setName(get.getVorname() + " " + get.getNachname());
		dlg.initOwner(mbMenu.getScene().getWindow());

		dlg.showAndWait();

	    } catch (IOException ex)
	    {
		Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	} else
	{
	    Message("Es liegen keine Aufzeichnungen vor!");
	}
    }

    /**
     * display about dialog
     */
    private void showAbout()
    {
	try
	{
	    Dialog dlg = new Dialog<>();
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Splash.fxml"));
	    DialogPane pane = loader.load();
	    dlg.setDialogPane(pane);
	    dlg.getDialogPane().getButtonTypes().setAll(ButtonType.OK);
	    dlg.initOwner(mbMenu.getScene().getWindow());

	    dlg.showAndWait();
	} catch (IOException ex)
	{
	    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * update meue and table
     */
    private void UpdateUi()
    {
	miChange.setDisable(tblPerson.getSelectionModel().selectedIndexProperty().get() < 0);
	cmChange.setDisable(tblPerson.getSelectionModel().selectedIndexProperty().get() < 0);
	miState.setDisable(tblPerson.getSelectionModel().selectedIndexProperty().get() < 0);
	cmStat.setDisable(tblPerson.getSelectionModel().selectedIndexProperty().get() < 0);
    }

     /**
     * show alert dialog with given message
     *
     * @param msg alert message
     */
    private void Message(String msg)
    {
	Alert dlg = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
	dlg.showAndWait();
    }


}
