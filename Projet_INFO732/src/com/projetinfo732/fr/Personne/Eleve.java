package com.projetinfo732.fr.Personne;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.projetinfo732.fr.Enseignement.Promo;
import com.projetinfo732.fr.Enseignement.UE;
import com.projetinfo732.fr.Enseignement.Module.Module;
import com.projetinfo732.fr.Enseignement.Module.Travail;
import com.projetinfo732.fr.Enseignement.Module.Cour.Cour;

public class Eleve extends Personne {

	private Promo promo;
	private HashMap<Module, HashMap<Travail, double[]>> informations = new HashMap<>();
	String login;
	Date date_arrivee;
	
	public Eleve(String nom, String prenom, Promo promo, String login, Date date_arrivee) {
		super(nom, prenom, date_arrivee);
		this.promo = promo;
		this.login = login;
	}
	
	public void addUE(UE ue) {
		
		List<Module> modules = ue.getModules();
		
		for(Module module : modules) {
			if(this.informations.containsKey(module)) return;
			
			this.informations.put(module, new HashMap<>());
		}
		
	}
	
	public Set<Module> getAllModules() {
		return this.informations.keySet();
	}
	
	public void addNote(Module module, Travail travail, double note, double coeff) {
		
		if(!this.informations.containsKey(module)) return;
		
		HashMap<Travail, double[]> module_informations = this.informations.get(module);
				
		double[] infos = new double[]{note, coeff} ;
		
		module_informations.put(travail, infos);
		
		this.informations.put(module, module_informations);
		
	}
	
	public HashMap<Travail, double[]> getInfosFromModule(Module module) {
		
		return this.informations.get(module);
		
	}
	
	public Collection<double[]> getInfosFromAllTravaux(Module module) {
		
		return this.informations.get(module).values();
		
	}
	
	public List<Double> getNotesFromModule(Module module) {
		
		HashMap<Travail, double[]> module_informations = this.informations.get(module);
		
		List<Double> liste = new ArrayList<>();
		
		for(double[] d : module_informations.values()) {
			liste.add(d[0]);
		}
		
		return liste;
		
	}
	
	public Double getMeanOfModule(Module module) {
		
		Collection<double[]> infos = this.getInfosFromAllTravaux(module);
				
		double sum = 0;
		double sum_coeff = 0.0;
		
		for(double[] d : infos) {
			
			double note = d[0];
			double coeff = d[1];
			
			sum += note * coeff;
			sum_coeff += coeff;
			
		}
		
		return sum / sum_coeff;
		
	}
	
	public Double getGlobalMean() {
		
		double sum = 0.0;
		double count = 0.0;
		
		for(Module module : getAllModules()){
			
			if(getMeanOfModule(module) != null) {
				sum += getMeanOfModule(module);
				count ++;
			}
			
		}
		
		double mean = sum / count;
		
		return Math.round(mean*100.0)/100.0;
	}

	public Promo getPromo() {
		return this.promo;
	}

	public void setpromo(Promo promo) {
		this.promo = promo;
	}

	public HashMap<Module, HashMap<Travail, double[]>> getInformations() {
		return informations;
	}

	public void setInformations(HashMap<Module, HashMap<Travail, double[]>> informations) {
		this.informations = informations;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPromo(Promo promo) {
		this.promo = promo;
	}

	public Object getNoteOfTravail(Travail travail) {
		
		Module module = travail.getModule();
		
		if( this.informations.get(module).get(travail) != null) {
			return this.informations.get(module).get(travail)[0];
		}
		
		return null;
	}

	public double getMeanOfUe(UE ue) {
		
		List<Module> modules = ue.getModules();
		
		double sum = 0;
		double count = 0;
		
		for(Module module : modules) {
			
			if(getMeanOfModule(module) != null) {
				sum += getMeanOfModule(module);
				count++;
			}
			
		}
		
		double mean = sum / count;
			
		return Math.round(mean*100.0)/100.0;
		
	}
	
	public List<Travail> getTravaux(){
		
		List<Travail> travaux = new ArrayList<>();
		
		for(Module module : getAllModules()) {
			
			travaux.addAll(module.getTravaux());
			
		}
		
		return travaux;
		
	}
	
	public HashMap<Travail, double[]> getLastNotes(){
		
		HashMap<Travail, double[]> map = new HashMap<>();
		
		for(Travail travail : getTravaux()) {
			
			Date date_travail = travail.getDate();
			
			Date now = new Date();
			
			
			if (date_travail.before(now)) {
				long diff = now.getTime() - date_travail.getTime();
				long days = diff / (24 * 60 * 60 * 1000);
				
				if (days <= 7) {
					
					if(this.informations.get(travail.getModule()).get(travail) != null) {
						map.put(travail, new double[] {this.informations.get(travail.getModule()).get(travail)[0], 					this.informations.get(travail.getModule()).get(travail)[1]});
					}
					
				}
			}
			
		}
		
		return map;
		
	}
	
	public HashMap<Travail, Date> getNextTravaux(){
		
		HashMap<Travail, Date> map = new HashMap<>();
		
		for(Travail travail : getTravaux()) {
			
			Date date_travail = travail.getDate();
			
			Date now = new Date();
			
			if (now.before(date_travail)) {
				
				long diff = date_travail.getTime() - now.getTime();
				long days = diff / (24 * 60 * 60 * 1000);
				
				if (days <= 7 && days >= 0) {
					map.put(travail, date_travail);
				}
				
				
			}
			
		}
		return map;
	}
	
	private HashMap<Travail, Date> getNextTravaux(Date date){
		
		HashMap<Travail, Date> map = new HashMap<>();
		
		for(Travail travail : getTravaux()) {
			
			Date date_travail = travail.getDate();
			
			if (date.before(date_travail)) {
				
				long diff = date_travail.getTime() - date.getTime();
				long days = diff / (24 * 60 * 60 * 1000);
				
				if (days <= 7 && days >= 0) {
					
					map.put(travail, date_travail);
				}
				
				
			}
			
		}
		return map;
	}
	
	private Date getLastMonday() {
		
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(now);
	    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	    int daysToSubtract = dayOfWeek - Calendar.MONDAY;
	    if (daysToSubtract < 0) {
	      daysToSubtract += 7;
	    }
	    cal.add(Calendar.DATE, -daysToSubtract);
	    Date previousMonday = cal.getTime();
	    return previousMonday;
		
	}
	
	private int getNumberOfWeek() {
		
		Date date = new Date(); 
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int week = cal.get(Calendar.WEEK_OF_YEAR);
		
	    return week;
	}
	
	private List<Date> getDaysOfWeek(){
		
		List<Date> dates = new ArrayList<>();
		
		Date monday = getLastMonday();
		
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(monday);
		
		for (int i = 0; i < 7; i++) {
			
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		return dates;
	}
	
	public HashMap<DayOfWeek, HashMap<Cour, List<Object[]>>> getPlanningOfWeek() {
		
		HashMap<DayOfWeek, HashMap<Cour, List<Object[]>>> map = new HashMap<>();
		  
		  for (Date date : getDaysOfWeek()) {
		    for (Module module : getAllModules()) {
		      for (Cour cour : module.getCours()) {
		    	  
		        Calendar cal1 = Calendar.getInstance();
		        cal1.setTime(date);
		        int year1 = cal1.get(Calendar.YEAR);
		        int month1 = cal1.get(Calendar.MONTH);
		        int day1 = cal1.get(Calendar.DATE);

		        Calendar cal2 = Calendar.getInstance();
		        cal2.setTime(cour.getDate());
		        int year2 = cal2.get(Calendar.YEAR);
		        int month2 = cal2.get(Calendar.MONTH);
		        int day2 = cal2.get(Calendar.DATE);
		        int day_number = cal2.get(Calendar.DAY_OF_WEEK);

		        if (year1 == year2 && month1 == month2 && day1 == day2) {
		        	
		          double heure_debut = cour.getHeure_debut();
		          
		          double duree = cour.getDuree();
		          
		          int middle = (int) (duree / 2);
		          
		          double val = heure_debut + middle;
		          
		          for(double i = heure_debut; i < heure_debut + duree; i+=0.5) {
		        	  
		        	  int id = (int) ((i-8) * 2);
		        	  
		        	  
		        	  String line = "";
		        	  
		        	  if(duree == 1.5) {
		        		  val = heure_debut + 0.5;
			          }
		        	  
		        	  if(((val - 8) * 2) == id) {
		        		  line = cour.getModule().getNom() + "\n" + cour.getCourenum().getNom();
		        	  } 
		        	  
		        	  
		        	  Object[] values = new Object[] {id, line};
		        	  
		        	  DayOfWeek dayOfWeek = null;
		        	  
		        	  switch (day_number) {
		        	  	case 0:
							dayOfWeek = DayOfWeek.SUNDAY;
							break;
						case 2:
							dayOfWeek = DayOfWeek.MONDAY;
							break;
						case 3:
							dayOfWeek = DayOfWeek.TUESDAY;
							break;
						case 4:
							dayOfWeek = DayOfWeek.WEDNESDAY;
							break;
						case 5:
							dayOfWeek = DayOfWeek.THURSDAY;
							break;
						case 6:
							dayOfWeek = DayOfWeek.FRIDAY;
							break;
						case 7:
							dayOfWeek = DayOfWeek.SATURDAY;
							break;
						}
		        	  
		        	  
		        	  if(!map.containsKey(dayOfWeek)){
		        		  map.put(dayOfWeek, new HashMap<Cour, List<Object[]>>());
		        	  }
		        	  
		        	  HashMap<Cour, List<Object[]>> res = map.get(dayOfWeek);
		        	  
		        	  if(res.containsKey(cour)) {
		        		  
		        		  List<Object[]> liste = map.get(dayOfWeek).get(cour);
		        		  
		        		  liste.add(values);
		        		  res.put(cour, liste);
		        		  
		        	  } else {
		        		  List<Object[]> liste = new ArrayList<>();
		        		  liste.add(values);
		        		  res.put(cour, liste);
		        	  }
		        	  
		        	  map.put(dayOfWeek, res);
		        	  
		        	  
		          }

		      }
		    }
		  }
		}
		  
		  return map;
	
	}
	
	public int[] getCharged() {
		
		Date date = getLastMonday();		
		
		int week = getNumberOfWeek();
		
		int amount = getNextTravaux(date).keySet().size();
		
		if(amount < 2) {
			amount =  1;
		} else if(amount < 3) {
			amount = 2;
		} else {
			amount = 3;
		}
		
		return new int[] {week, amount};
		
	}
	
	

}