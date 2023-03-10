package com.projetinfo732.fr.interfaceGraphique.pages;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.projetinfo732.fr.Enseignement.Module.Cour.Cour;
import com.projetinfo732.fr.Personne.Eleve;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Calendar extends Parent {

	private final LocalTime heureMini = LocalTime.of(8, 0);
	private final Duration tempsSeparation = Duration.ofMinutes(30);
	private final LocalTime heureMax = LocalTime.of(20, 0);
	
	private final List<Emplacement> timeSlots = new ArrayList<>();
	
	public Calendar(Stage primaryStage,Eleve eleve) {
		
		Duration tem = Duration.ofMinutes(45);
		
		GridPane calendrier = new GridPane();
		calendrier.setPrefSize(800,900);
		calendrier.setPadding(new Insets(10, 0, 0, 0));
		//calendrier.setGridLinesVisible(true);
		//calendrier.gridLinesVisibleProperty();
		
		LocalDate today = LocalDate.now();
		LocalDate debutSemaine = today.minusDays(today.getDayOfWeek().getValue() - 1) ;
		LocalDate finSemaine = debutSemaine.plusDays(4);
		
		 Button backButton = new Button("Back");
	        backButton.setOnAction(event -> {
	        	AccueilEleve accueilEleve = new AccueilEleve(primaryStage,eleve);
	            Scene scene = new Scene(accueilEleve);
	            scene.getStylesheets().add(getClass().getResource("css/accueil.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        });
	
	        // Add the back button to the top left corner of the root node
	        calendrier.add(backButton,0,0);
	        calendrier.setAlignment(Pos.TOP_LEFT);
		
		
		HashMap<DayOfWeek, HashMap<Cour,List<Object[]>>> values = eleve.getPlanningOfWeek();
		
		for(DayOfWeek day : values.keySet()) {
			
			HashMap<Cour,List<Object[]>> cour_values = values.get(day);
			for(Cour cour : cour_values.keySet()) {
				int count = 0;
				for(Object[] obj : values.get(day).get(cour)) {
					
					Emplacement emplacement = new Emplacement(day,(String) obj[1], cour.getModule().getCouleurEDT());
					if(count == 0) {
						emplacement.setEmplacementBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
					            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
					            CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));
					}
					else if(count == values.get(day).get(cour).size()-1){
						emplacement.setEmplacementBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
					            BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
					            CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));
					}
					else {
						emplacement.setEmplacementBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
					            BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
					            CornerRadii.EMPTY, new BorderWidths(1), Insets.EMPTY)));
					}
					
					timeSlots.add(emplacement);
							
					calendrier.add(emplacement.getView(), emplacement.getDayOfWeek().getValue(), (int) obj[0]+1);
					count += 1;
				}
			}
		}
		
		//headers:
		
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E\nMMM d");
		
		for (LocalDate date = debutSemaine; ! date.isAfter(finSemaine); date = date.plusDays(1)) {
			Label label = new Label(date.format(dayFormatter));
			label.setPadding(new Insets(1));
			label.setPrefSize(150,20);
			label.setTextAlignment(TextAlignment.CENTER);
			GridPane.setHalignment(label, HPos.CENTER);
			calendrier.add(label, date.getDayOfWeek().getValue(), 0);
		}
		
		int slotIndex = 1 ;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
		for (LocalDateTime startTime = today.atTime(heureMini);!startTime.isAfter(today.atTime(heureMax)); startTime = startTime.plus(tempsSeparation)) {
			Label label = new Label(startTime.format(timeFormatter));
			label.setPadding(new Insets(0,0,20,0));
			GridPane.setHalignment(label, HPos.RIGHT);
			calendrier.add(label, 0, slotIndex);
			slotIndex++ ;
		}
		
		ScrollPane scroller = new ScrollPane(calendrier);
		
		
        // Add the scroll pane to the root node
        this.getChildren().add(scroller);
		
	}
	
public static class Emplacement {
	
		private final String description;
	    private final DayOfWeek dayOfWeek;
	    private final VBox view;
	    private final Border border = null;
		
		public Emplacement(DayOfWeek dayOfWeek, String description,String color) {
			
		        this.dayOfWeek = dayOfWeek;
		        this.description = description;
		        this.view = new VBox();
		        Label descriptionText = new Label(description);
		        view.getChildren().add(descriptionText);
		        view.setPrefSize(150,20);
		        view.setAlignment(Pos.CENTER);
		        view.setStyle("-fx-background-color: " + color + ";");
		    }
			
			public DayOfWeek getDayOfWeek() {
		        return dayOfWeek;
		    }

		    public Node getView() {
		        return view;
		    }
		    
		    public void setEmplacementBorder(Border border) {
		    	this.view.setBorder(border);
		    }
		    
		}
		
}