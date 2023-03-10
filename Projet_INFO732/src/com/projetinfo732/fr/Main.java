package com.projetinfo732.fr;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import com.projetinfo732.fr.handler.ConfigHandler;
import com.projetinfo732.fr.handler.MySQL;
import com.projetinfo732.fr.interfaceGraphique.pages.LoginForm;
import com.projetinfo732.fr.utils.Config;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
	
	/**
     * Instance d'un objet MySQL
     * 
     * @see MySQL#MySQL(String, String, String, String, String)
     */
	public static MySQL database;

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginForm LoginForm = new LoginForm(primaryStage);
		// Add a style sheet to the scene
        Scene scene = new Scene(LoginForm, 290, 220);
        scene.getStylesheets().add(getClass().getResource("interfaceGraphique/pages/css/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        Image image = new Image(getClass().getResourceAsStream("interfaceGraphique/Icon.jpg"));

        // Add the image as an icon for the window
        primaryStage.getIcons().add(image);
        
        primaryStage.show();	
	}
	
	 /**
     * Fonction principale du programme
     * @param args[]
     */
	public static void main(String[] args) {
		
		database = new MySQL(getConfig().getHost(), getConfig().getPort(), getConfig().getDatabase(), getConfig().getUser(), getConfig().getPassword());
		
		if(!connectToDatabase()) return;
		
		launch(args);
		
	}
	
	/* Retourne le fichier de configuration
    * 
    * @return Une instance de Config, qui correspond à un fichier configuration
    * 
    * @throws FileNotFoundException si le fichier est introuvable
    * @see Config
    */
	private static Config getConfig() {
		
		ConfigHandler handler = null;
		try {
			handler = ConfigHandler.getInstance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	    return handler.getConfig();
	}
	
	 /**
     * Fonction de connexion à la base de donnée
     * 
     * @return True si la connexion est réussie, False si elle a échoué
     * @throws ClassNotFoundException si la classe est introuvable
     * @throws SQLException si la requête sql n'a pas fonctionné
     * @see MySQL
     */
	private static boolean connectToDatabase(){
        
		 try
		    {
		      database.openConnection();
		      System.out.println("Connexion à la base de données réalisée avec succès.");
		      //InitTable.initTable();
		      return true;
		      
		    }
		    catch (ClassNotFoundException | SQLException e)
		    {
		      //new WarningGui("Connexion à la base de données échouée").setVisible(true);
		      System.out.println("Impossible de se connecter à la base de données :");
		      e.printStackTrace();
		    } 
		return false;
	}

}
