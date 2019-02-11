/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realtimecc2.gui;

import com.realtimecc2.SpringFXMLLoader;
import com.realtimecc2.dao.PersonRepository;
import com.realtimecc2.model.Personen;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author micim
 */
@Controller
public class LoginController
{

    @Autowired
    PersonRepository personen;

    @Autowired
    ApplicationContext springContext;

    @FXML
    private Label lblTitle;
    @FXML
    private Label lblBenutzer;
    @FXML
    private Label lblPasswort;
    @FXML
    private TextField txtBenutzer;
    @FXML
    private PasswordField txtPasswort;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnApply;

    /**
     * Initializes the controller class.
     */
    public void initialize()
    {
	txtBenutzer.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		UpdateUi();
	    }
	});

	txtPasswort.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		UpdateUi();
	    }
	});
	
    }

    @FXML
    private void cancel(ActionEvent event)
    {
	System.exit(0);
    }

    @FXML
    private void apply(ActionEvent event) throws IOException
    {
	Personen user = personen.getByBenutzerAndPasswort(txtBenutzer.getText(), txtPasswort.getText());
	if (user == null)
	{
	    Alert msg = new Alert(Alert.AlertType.ERROR, "Benutzername oder Passwort ist falsch!", ButtonType.OK);
	    msg.showAndWait();
	    event.consume();
	    txtBenutzer.requestFocus();
	    txtBenutzer.selectAll();
	} else
	{
	    Stage stage = (Stage) btnApply.getScene().getWindow();
	    if(user.getRolle().equals("Mitarbeiter"))
	    {
		Parent root = springContext.getBean(SpringFXMLLoader.class).load("/fxml/AnAbmeldung.fxml");
		AnAbmeldungController ctrl =(AnAbmeldungController) springContext.getBean(AnAbmeldungController.class);
		ctrl.setData(user);
		stage.hide();
		stage.setScene(new Scene(root));
		stage.show();
	    }
	    else
	    {
		Parent root = springContext.getBean(SpringFXMLLoader.class).load("/fxml/Personal.fxml");
		stage.hide();
		stage.setScene(new Scene(root));
		stage.show();
	    }
	}
    }

    private void UpdateUi()
    {
	btnApply.setDisable(txtBenutzer.getText().isEmpty() || txtPasswort.getText().isEmpty());
    }
}
