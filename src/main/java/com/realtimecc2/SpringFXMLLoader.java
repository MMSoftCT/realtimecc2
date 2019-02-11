/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author micim
 */
@Component
public class SpringFXMLLoader 
{
    @Autowired
    ApplicationContext springContext;
    
    public Parent load(String resourceName) throws IOException
    {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
	loader.setControllerFactory(springContext::getBean);
	return loader.load();
    }
}
