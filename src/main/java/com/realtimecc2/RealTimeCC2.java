package com.realtimecc2;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.realtimecc2.model")
@EnableJpaRepositories(basePackages = {"com.realtimecc2.dao"})
public class RealTimeCC2 extends Application
{

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    
    public static void main(String[] args)
    {
	launch(RealTimeCC2.class, args);
    }

    @Override
    public void init() throws Exception
    {
	springContext = SpringApplication.run(RealTimeCC2.class);
	rootNode = springContext.getBean(SpringFXMLLoader.class).load("/fxml/Login.fxml");
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
	primaryStage.setScene(new Scene(rootNode));
	primaryStage.show(); 
    }
    
    @Override
    public void stop()
    {
	springContext.close();
    }

}
