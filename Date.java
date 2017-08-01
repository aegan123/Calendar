package oop2017;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Juhani Vähä-Mäkilä
 * CC BY-ND-NC
 * @version 0.1
 */
final class Date implements Serializable {


	private static final long serialVersionUID = 8L;
	private List<Event> events;
	private List<Task> tasks;
	private LocalDate date;

	/**
	 * Alustaa olion halutulle päivämäärälle.
	 * @param chronoLocalDate Haluttu päivämäärä.
	 */
	public Date(Temporal chronoLocalDate) {
		this.events=new ArrayList<Event>();
		this.tasks=new ArrayList<Task>();
		this.date=(LocalDate) chronoLocalDate;
	}
	/**
	 * Palauttaa tapahtumalistan koon.
	 * @return Listan koko.
	 */
	public int getEventSize() {
		return events.size();
	}
	/**
	 * Palauttaa tehtävälistan koon.
	 * @return Listan koko.
	 */
	public int getTaskSize() {
		return tasks.size();
	}
	/**
	 * Palauttaa määritellyn tapahtuman listasta.
	 * @param i Mitä listan alkiota halutaan.
	 * @return Määritelty event olio.
	 */
	public Event getEvent(int i) {
		return events.get(i);
	}
	/**
	 * Palauttaa määritellyn tehtävän listasta.
	 * @param i Mitä listan alkiota halutaan.
	 * @return Määritelty task olio.
	 */
	public Task getTask(int i) {
		return tasks.get(i);
	}
	/**
	 * Palauttaa päivämäärän.
	 * @return Päivämäärä, jota olio edustaa.
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * Lisää uuden tapahtuman.
	 * @param event Lisättävä tapahtuma.
	 */
	public void add(Event event) {
		this.events.add(event);
	}
	/**
	 * Lisää uuden tehtävän.
	 * @param task Lisättävä tehtävä.
	 */
	public void add(Task task){
		this.tasks.add(task);
	}
	/**
	 * Poistaa määritellyn tapahtuman.
	 * @param event Poistettava event.
	 */
	public void remove(Event event){
		//this.events.remove(i);
		events.remove(event);
	}
	/**
	 * Poistaa määritellyn tehtävän..
	 * @param task Poistettava task.
	 */
	public void remove(Task task){
		//this.tasks.remove(i);
		tasks.remove(task);
	}
	/**
	 * Palauttaa koko tehtävälistan.
	 * @return Koko tehtävälista.
	 */
	ArrayList<Task> getTasks() {
		return (ArrayList<Task>) this.tasks;
	}
	/**
	 * Palauttaa koko tapahtumalistan.
	 * @return Koko tapahtumalista.
	 */
	ArrayList<Event> getEvents() {
		return (ArrayList<Event>) this.events;
	}
	/**
	 * Kahden päivämäärän yhtäsuuruuden vertaus. 
	 * @return True jos on muuten false.
	 */
	@Override
	public boolean equals (Object ob) {
		return date.equals((LocalDate) ob);
		
	}
	/**
	 * Tekstuaalinen muotoilu oliosta.
	 * {@literal Muotoa dd.mm.yyyy.}
	 */
	@Override
	public String toString() {
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
}