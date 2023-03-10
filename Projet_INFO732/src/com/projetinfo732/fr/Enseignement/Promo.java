package com.projetinfo732.fr.Enseignement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.projetinfo732.fr.Enseignement.Module.Module;
import com.projetinfo732.fr.Enseignement.Module.Travail;
import com.projetinfo732.fr.Personne.Eleve;

public class Promo {
	
	private int id;
	private Filiere filiere;
	private Date date;
	private List<Eleve> eleves = new ArrayList<>();
	
	public Promo(Filiere filiere, Date date, int id) {
		super();
		this.filiere = filiere;
		this.date = date;
		this.id = id;
	}
	
	public Filiere getFiliere() {
		return this.filiere;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return this.filiere.getNom();
	}
	
	public void addEleve(Eleve eleve) {
		this.eleves.add(eleve);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Eleve> getEleves() {
		return eleves;
	}

	public void setEleves(List<Eleve> eleves) {
		this.eleves = eleves;
	}

	public Double getPromoMeanOfModule(Module module){
		
		double sum = 0;
		double count = 0;
		
		for(Eleve eleve : eleves) {
			
			sum += eleve.getMeanOfModule(module);
			count++;
			
		}
		double mean = sum / count;
		
		return Math.round(mean*100.0)/100.0;
		
	}
	
	public Double getPromoMeanOfTravail(Travail travail){
		
		double sum = 0;
		double count = 0;
		
		for(Eleve eleve : eleves) {
			
			if(eleve.getNoteOfTravail(travail) != null) {
				sum += (double) eleve.getNoteOfTravail(travail);
				count++;
			}
			
			
		}
		double mean = sum / count;
		
		return Math.round(mean*100.0)/100.0;
		
	}
	
	public Double getPromoMeanOfUE(UE ue){
		
		double sum = 0;
		double count = 0;
		
		for(Eleve eleve : eleves) {
			
			sum += eleve.getMeanOfUe(ue);
			count++;
			
		}
		
		double mean = sum / count;
		
		return Math.round(mean*100.0)/100.0;
		
	}
	
	
	
	public Double getGlobalMean(){
		
		double sum = 0;
		double count = 0;
		
		for(Eleve eleve : eleves) {
			
			sum += eleve.getGlobalMean();
			count++;
			
		}
		double mean = sum / count;
		
		return Math.round(mean*100.0)/100.0;
		
	}
	
	
	
	

}
