package oop2017;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
/**
 * @author Juhani Vähä-Mäkilä
 * CC BY-ND-NC
 * @version 0.5
 *@.ClassInvariant (this.start!=null && this.end!=null) && otsikko!=null
 */
final class Event implements Serializable  {
	
	private static final long serialVersionUID = 8L;
	private boolean muistutus, allDay;
	private LocalDateTime start, end;
	private String otsikko;

	/**
	 * 
	 * @param o Tapahtuman nimi.
	 * @param start2 Aloitusaika.
	 * @param end2 Lopetusaika.
	 * @param muistutus true tai false
	 * @param allday true tai false
	 * @.post RESULT(this.start==start && ((this.end==end && this.otsikko=o) && (this.allDay=allday && this.muistutus==muistutus)))
	 */
	public Event(String o,Temporal start2, Temporal end2, boolean muistutus, boolean allday) {
		this.start=(LocalDateTime) start2;
		this.end=(LocalDateTime) end2;
		this.muistutus=muistutus;
		this.setAllDay(allday);
		this.otsikko=o;
		
	} 
	/**
	 * 
	 * @param o Tapahtuman nimi.
	 * @param start2 Aloitusaika.
	 * @param end2 Lopetusaika.
	 * @.post RESULT(this.start==start && ((this.end==end && this.otsikko=o) && (this.allDay=false && this.muistutus==false)))
	 */
	public Event(String o,Temporal start2, Temporal end2) {
		this.start=(LocalDateTime) start2;
		this.end=(LocalDateTime) end2;
		this.muistutus=false;
		this.allDay=false;
		this.otsikko=o;		
	}
	
	/**Palauttaa tiedon onko event koko päivän kestävä.
	 * @return True, jos on. Muuten False.
	 */
	boolean isAllDay() {
		return allDay;
	}
	/**
	 * Asettaa tapahtuman kokopäiväiseksi samalla nollaten aloitus- ja lopetusajat.
	 * @param allDay true
	 */
	void setAllDay(boolean allDay) {
			this.allDay = allDay;
			start=LocalDateTime.of(start.toLocalDate(), LocalTime.of(0, 0));
			end=LocalDateTime.of(end.toLocalDate(), LocalTime.of(0, 0,0));
	}
	/**
	 * Poistaa kokopäivä statuksen ja asettaa uuden aloitus- ja lopetusajan.
	 * @param allDay false
	 * @param start2 Uusi aloitusaika.
	 * @param end2 uusi lopetusaika.
	 */
	void setAllDay(boolean allDay,Temporal start2, Temporal end2) {
		this.allDay = allDay;
		this.start=(LocalDateTime) start2;
		this.end=(LocalDateTime) end2;
	}
	/**
	 * Palauttaa tiedon siitä onko tapahtumalla muistutusta.
	 * @return Onko muistutusta.
	 *@.pre true
	 *@.post RESULT(this.muistutus)
	 */
	boolean onkoMuistutus() {
		return muistutus;
	}

	/**
	 * Asetaa tapahtumalle muistutuksen.
	 * @param onkoMuistutus Asettaa muistutuksen tilan.
	 * @.pre true
	 * @.post RESULT(this.muistutus==onkoMuistutus)
	 */
	void setOnkoMuistutus(boolean onkoMuistutus) {
		this.muistutus = onkoMuistutus;
	}

	/**
	 * Palauttaa tapahtuman lopetusajan.
	 * @return Eventin lopetusaika ja päivä.
	 */
	LocalDateTime getEnd_date() {
		return end;
	}

	/**
	 * Asettaa lopetusajan.
	 * @param loppu Uusi lopetusaika.
	 */
	void setEnd_date(Temporal loppu) {
		this.end = (LocalDateTime) loppu;
	}

	/**
	 * Palauttaa lopetusajan.
	 * @return Lopetusaika.
	 */
	LocalDateTime getStart_date() {
		return start;
	}

	/**
	 * Asettaa uuden aloitusajan.
	 * @param alku Uusi aloitusaika.
	 */
	void setStart_date(Temporal alku) {
		this.start = (LocalDateTime) alku;
	}
	/**
	 * Palauttaa tapahtuman otsikon.
	 * @return Tapahtuman otsikon.
	 */
	String getOtsikko() {
		return this.otsikko;
	}
	public void setOtsikko(String otsikko) {
		this.otsikko=otsikko;
		
	}
	/**
	 * Tekstuaalinen muotoilu olion sisällöstä.
	 * {@literal Muotoa: otsikko :\nAloitusaika: start\nLopetusaika: end \nMuistutus: muistutus\nKoko Päivä: allDay;}
	 */
	@Override
	public String toString() {
		if (muistutus && !allDay) {
			return otsikko+":\nAloitusaika: "+start.format(Kalenteri.dateformat)+"\nLopetusaika: "+end.format(Kalenteri.dateformat)+"\nMuistutus: Kyllä\nKoko Päivä: Ei";
		}
		if (allDay && !muistutus) {
			return otsikko+":\nAloitusaika: "+start.format(Kalenteri.dateformat)+"\nLopetusaika: "+end.format(Kalenteri.dateformat)+"\nMuistutus: Ei\nKoko Päivä: Kyllä";
		}
		if (allDay && muistutus) {
			return otsikko+":\nAloitusaika: "+start.format(Kalenteri.dateformat)+"\nLopetusaika: "+end.format(Kalenteri.dateformat)+"\nMuistutus: Kyllä\nKoko Päivä: Kyllä";
		}
		return otsikko+":\nAloitusaika: "+start.format(Kalenteri.dateformat)+"\nLopetusaika: "+end.format(Kalenteri.dateformat)+"\nMuistutus: Ei\nKoko Päivä: Ei";
	}

}