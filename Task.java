package oop2017;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
/**
 * @author Juhani Vähä-Mäkilä
 * CC BY-ND-NC
 * @version 0.5
 *@.ClassInvariant ((this.start!=null && this.end!=null) && this.otsikko!=null)
 */
final class Task implements Serializable {
	
	private static final long serialVersionUID = 7L;
	private LocalDateTime start, end;
	private String otsikko;

	/**
	 * 
	 * @param otsikko 
	 * @param start2
	 * @param end2
	 * @.pre true
	 * @.post (this.start==start && (this.end==end && this.otsikko==otsikko))
	 */
	public Task(String otsikko, Temporal start2, Temporal end2){
		this.start=(LocalDateTime) start2;
		this.end=(LocalDateTime) end2;
		this.otsikko=otsikko;
	}
	
	/**
	 * @return the start
	 *@.pre true
	 *@.post RESULT(this.start)
	 */
	LocalDateTime getStart_date() {
		return start;
	}

	/**
	 * Asettaa uuden aloitusajan.
	 * @param alku Uusi aloitusaika.
	 *@.pre start!=null
	 *@.post RESULT(this.start==start_date)
	 */
	void setStart_date(Temporal alku) {
		this.start = (LocalDateTime) alku;
	}

	/**
	 * Palauttaa lopetusajan.
	 * @return Lopetusajan.
	 */
	LocalDateTime getEnd_date() {
		return end;
	}

	/**
	 * Asettaa uuden lopetusjan.
	 * @param loppu Uusi lopetusaika.
	 * @.pre end!=null
	 * @.post (this.end==end_date)
	 */
	void setEnd_date(Temporal loppu) {
		this.end = (LocalDateTime) loppu;
	}
	/**
	 * Palauttaa tehtävän otsikon.
	 * @return Tehtävän otsikko.
	 */
	public String getOtsikko() {
		return this.otsikko;
	}
	/**
	 * Asettaa uuden otsikon.
	 * @param otsikko Uusi otsikko.
	 */
	public void setOtsikko(String otsikko) {
		this.otsikko=otsikko;
		
	}
	/**
	 * Tekstuaalinen muotoilu olion sisällöstä.
	 * {@literal Muotoa: otsikko:\nAloitusaika: start \nLopetusaika: end;}
	 */
	@Override
	public String toString() {
		return otsikko+":\nAloitusaika: "+start.format(Kalenteri.dateformat)+"\nLopetusaika: "+end.format(Kalenteri.dateformat);
	}

}