package oop2017;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @author Juhani Vähä-Mäkilä
 * CC BY-ND-NC
 * @version 0.6
 *@.ClassInvariant dates!=null
 */
class Kalenteri extends Thread implements Serializable {
	

	private static final long serialVersionUID = 13L;
	private List<Date> dates;
	private boolean running;
	//Eventin ja Taskin tulostuksen muotoilua varten.
	static final DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	private static final HashMap<LocalDate,Date> datemap=luoMap();
	
	/**
	 * Normaali tapa luoda kalenteri. Ei alusta dates listaa, koska se palautetaan toisaalla.
	 */
	public Kalenteri() {
		this.dates=null;
	}

	/**
	 * Vain testikäytössä. Alustaa myös uuden tyhjän dates listan.
	 * @param a
	 */
	public Kalenteri(boolean a){
		this.dates=new ArrayList<Date>();
		
	}
	
	//*********************
	//Getterit ja setterit*
	//*********************
	/**
	 * Palauttaa määritellyn päivän.
	 * @param date 
	 * @return Määritelty Date olio.
	 */
	public Date getDate(LocalDate date){
		//return dates.get(i);
		return datemap.get(date);
	}
	/**
	 * Palautaa päivälistan pituuden.
	 * @return Päivälistan pituus.
	 */
	int getSize() {
		return dates.size();
	}
	/**
	 * Kontrolloi säikeen suoritusta.
	 * @param b
	 */
	public void setRunning(boolean b) {
		this.running=b;
	}
	
	/**
	 * Tyhjentää dates listan.
	 */
	void reset() {
		this.dates.clear();
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Lisää uuden päivämäärän listaan.
	 * @param chronoLocalDate
	 */
	void add(Temporal chronoLocalDate){
		Date temp=new Date(chronoLocalDate);
		if (!datemap.containsKey(chronoLocalDate)) {
			datemap.put((LocalDate) chronoLocalDate, temp);
		}
		this.dates.add(temp);
		temp=null;
	}
	/**
	 * Palauttaa dates listan.
	 * @return dates lista.
	 */
	ArrayList<Date> getDates(){
		return (ArrayList<Date>) dates;
	}
	
	//*********************************************************
	//Tapahtumien ja tehtävien muokkaamiseen liittyvät metodit*
	//*********************************************************
	/**
	 * Asettaa tapahtumalle uuden aloitus- ja/tai lopetusajan.
	 * Asettaa muokatun tapahtuman oikean päivän alle.
	 * @param event Muutettava event.
	 * @param alku Uusi aloitusaika tai null.
	 * @param loppu Uusi lopetusaika tai null.
	 */
	public void editEvent(Event event, Temporal alku, Temporal loppu) {
		Temporal temp=null;
		if (!(event.getStart_date().equals(alku))){
			//dates.get(indeksi(event)).getEvents().remove(event);
			//dates.get(indeksi(alku)).add(event);
			if (alku==null){
				temp=event.getStart_date().toLocalDate();
			} else temp=((LocalDateTime) alku).toLocalDate();
			if (!datemap.containsKey(temp)) {
				this.add(temp);}
			datemap.get(temp).add(event);
			datemap.get(event.getStart_date().toLocalDate()).remove(event);
		}
		if (alku!=null && loppu!=null) {
			event.setEnd_date(loppu);
			event.setStart_date(alku);
		} else {
		if (alku!=null) event.setStart_date(alku);
		if (loppu!=null) event.setEnd_date(loppu);
		}
		
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa tapahtumalle uuden otsikon.
	 * @param event Muokattava tapahtuma.
	 * @param otsikko Uusi otsikko.
	 */
	public void editEvent(Event event, String otsikko) {
		event.setOtsikko(otsikko);
	}
	/**
	 * Asettaa tapahtumalle sekä uuden aloitus- ja lopetusajan että otsikon.
	 * Asettaa muokatun tapahtuman oikean päivän alle.
	 * @param event Muokattava tapahtuma
	 * @param alku Uusi aloitusaika.
	 * @param loppu Uusi lopetusaika.
	 * @param otsikko Uusi otsikko.
	 */
	public void editEvent(Event event, Temporal alku, Temporal loppu, String otsikko) {
		if (!(event.getStart_date().equals(alku))){
			//int i=indeksi(event);
			//dates.get(indeksi(event)).getEvents().remove(event);
			//dates.get(indeksi(alku)).add(event);
			if (!datemap.containsKey(((LocalDateTime) alku).toLocalDate())) {
				this.add(((LocalDateTime) alku).toLocalDate());}
			datemap.get(((LocalDateTime) alku).toLocalDate()).add(event);
			datemap.get(event.getStart_date().toLocalDate()).getEvents().remove(event);
			
		}
		event.setEnd_date(loppu);
		event.setStart_date(alku);
		event.setOtsikko(otsikko);
		
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa tapahtuman kokopäiväiseksi.
	 * @param event Muutettava event.
	 * @param allday True.
	 */
	public void editEvent(Event event, boolean allday){
		event.setAllDay(allday);
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa, että tapahtuma ei ole kokopäivän. Asettaa myös uudet alkamis- ja loppumisajan.
	 * Sijoittaa oikean päivä alle.
	 * @param event Muutettava event.
	 * @param start Uusi aloitusaika.
	 * @param end Uusi lopetusaika
	 * @param allday false
	 */
	public void editEvent(Event event, Temporal start,Temporal end, boolean allday){
		LocalDate temp=(LocalDate) ((LocalDateTime) start).toLocalDate();
		if (!(event.getStart_date().equals(start))){
			//dates.get(indeksi(event)).getEvents().remove(event);
			//dates.get(indeksi(start)).add(event);
			if (!datemap.containsKey(temp)) {this.add(temp);}
			datemap.get(temp).add(event);
			datemap.get(event.getStart_date().toLocalDate()).getEvents().remove(event);
			temp=null;
		}
		event.setAllDay(allday,start,end);
		
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa tehtävälle uuden aloitus- ja/tai lopetusajan.
	 * Sijoittaa oikean päivän alle.
	 * @param task Muutettava task.
	 * @param alku Uusi aloitusaika.
	 * @param loppu Uusi lopetusaika.
	 */
	public void editTask(Task task, Temporal alku, Temporal loppu){
		if (!(task.getStart_date().equals(alku))){
			//dates.get(indeksi(task)).getTasks().remove(task);
			//dates.get(indeksi(alku)).add(task);
			if (!datemap.containsKey(((LocalDateTime) alku).toLocalDate())) {this.add(((LocalDateTime) alku).toLocalDate());}
			datemap.get(((LocalDateTime) alku).toLocalDate()).add(task);
			datemap.get(task.getStart_date().toLocalDate()).getTasks().remove(task);
			
		}
		if (alku!=null && loppu!=null) {
			task.setEnd_date(loppu);
			task.setStart_date(alku);
		} else {
		if (alku!=null) task.setStart_date(alku);
		if (loppu!=null) task.setEnd_date(loppu);
		}
		
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa tehtävälle uuden nimen.
	 * @param task Tehtävä, jota muutetaan.
	 * @param otsikko Uusi nimi.
	 */
	public void editTask(Task task, String otsikko){
		task.setOtsikko(otsikko);
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Asettaa tehtävälle uuden nimen ja uuden aloitus- ja lopetusajan.
	 * Sijoittaa oikean päivän alle.
	 * @param task Tehtävä, jota muutetaan.
	 * @param alku Uusi aloitusaika.
	 * @param loppu Uusi lopetusaika.
	 * @param otsikko Uusi nimi..
	 */
	public void editTask(Task task, Temporal alku, Temporal loppu, String otsikko){
		if (!(task.getStart_date().equals(alku))){
			//dates.get(indeksi(task)).getTasks().remove(task);
			//dates.get(indeksi(alku)).add(task);
			if (!datemap.containsKey(((LocalDateTime) alku).toLocalDate())) {this.add(((LocalDateTime) alku).toLocalDate());}
			datemap.get(((LocalDateTime) alku).toLocalDate()).add(task);
			datemap.get(task.getStart_date().toLocalDate()).getTasks().remove(task);
			
		}
		task.setEnd_date(loppu);
		task.setStart_date(alku);
		task.setOtsikko(otsikko);
		
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//********************************************************
	//Tapahtumien ja tehtävien poistamiseen liittyvät metodit*
	//********************************************************
	/**
	 * Poistaa määritellyn tapahtuman.
	 * @param event Poistettava tapahtuma.
	 */
	public void delete(Event event){
		//this.dates.get(indeksi(event.getStart_date())).delEvent(indeksi(event));
		datemap.get(event.getStart_date().toLocalDate()).remove(event);
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Poistaa määritellyn tehtävän.
	 * @param task poistettava tehtävä.
	 */
	public void delete(Task task){
		//this.dates.get(indeksi(task.getStart_date())).delTask(indeksi(task));
		datemap.get(task.getStart_date().toLocalDate()).remove(task);
		try {
			tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//*******************************************************
	//Tapahtumien ja tehtävien lisäämiseen liittyvät metodit*
	//*******************************************************
	
	/**
	 * Lisää uuden tapahtuman oikean päivän alle.
	 * @param otsikko Tapahtuman otsikko.
	 * @param start Aloitusaika.
	 * @param end Lopetusaika.
	 */
	public void addEvent(String otsikko,Temporal start, Temporal end) {
		Temporal temp=((LocalDateTime) start).toLocalDate();
	/*	int i=0;
		try {
		i=indeksi(start);
		}
		catch (IndexOutOfBoundsException e) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
			dates.add(new Date(start.toLocalDate()));
		}
	if (i==dates.size()) {
				if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
				dates.add(new Date(start.toLocalDate()));
				}
	
		this.dates.get(i).add(new Event(otsikko,start,end));*/
		if (!datemap.containsKey(temp)) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa. Luodaan uusi.");
			this.add(temp);
		}
		datemap.get(temp).add(new Event(otsikko,start,end));
		try {
			this.tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Lisää uuden tapahtuman oikean päivän alle.
	 * @param otsikko Tapahtuman otsikko.
	 * @param start Aloitusaika.
	 * @param end Lopetusaika.
	 * @param muistutus true tai false.
	 * @param allday true
	 */
	public void addEvent(String otsikko,Temporal start, Temporal end,boolean muistutus,boolean allday) {
		Temporal temp=((LocalDateTime) start).toLocalDate();
		if (allday) {
			start=LocalDateTime.of((LocalDate) ((LocalDateTime) start).toLocalDate(), LocalTime.of(0, 0,0));
			end=LocalDateTime.of((LocalDate) ((LocalDateTime) start).toLocalDate(), LocalTime.of(0, 0,0));
		}
	
		/*	int i=0;
		try {
		i=indeksi(start);
		}
		catch (IndexOutOfBoundsException e) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
			dates.add(new Date(start.toLocalDate()));
		}
	if (i==dates.size()) {
		if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
		dates.add(new Date(start.toLocalDate()));
		}
		this.dates.get(i).add(new Event(otsikko,start,end,muistutus,allday)); */
		if (!datemap.containsKey(temp)) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa. Luodaan uusi.");
			this.add(temp);
		}
		datemap.get(temp).add(new Event(otsikko,start,end,muistutus,allday));
		try {
			this.tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Lisää uuden tehtävän oikean päivän alle.
	 * @param otsikko Tehtävän otsikko.
	 * @param start Aloitusaika.
	 * @param end Lopetusaika.
	 */
	public void addTask(String otsikko,Temporal start,Temporal end){
		Temporal temp=((LocalDateTime) start).toLocalDate();
	/*	int i=0;
		try {
		i=indeksi(start);
		}
		catch (IndexOutOfBoundsException e) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
			dates.add(new Date(start.toLocalDate()));
		}
		if (i==dates.size()) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa.");
			dates.add(new Date(start.toLocalDate()));
			}
		this.dates.get(i).add(new Task(otsikko,start, end)); */
		if (!datemap.containsKey(temp)) {
			if (OopHarkka.verbose) System.out.println("Date ei olemassa. Luodaan uusi.");
			this.add(temp);
		}
		datemap.get(temp).add(new Task(otsikko,start, end));
		try {
			this.tallenna();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Palauttaa kalenterin tilan tiedostosta ja tallentaa sen kerran minuutissa.
	 */
	@Override
	public void run() {
		this.running=true;
			try {
				palauta();
			} catch (IOException e1) {
				dates=new ArrayList<Date>();
			}
			catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setPriority(4);
			do {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					tallenna();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.gc();
				
			} while (running);
			}
	/**
	 * Palauttaa kalenterin tilan tiedostosta vain mikäli tiedosto on olemassa ja sitä voi lukea.
	 * Jos tiedostoa ei jostain syystä ole olemassa tai ei voi lukea luo uuden tyhjän dates listan.
	 * @throws IOException Jos tapahtuu i/o virhe (esim. tiedostoa ei löydy).
	 * @throws ClassNotFoundException Jos downcastingissa tulee virhe.
	 */
	private void palauta() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		List<Date> lista = null;
		File f = new File("kalenteri.dat"); 
		if (f.isFile() && f.canRead() ) {
			if (OopHarkka.verbose) System.out.println("\nPalautetaan tila.");
			ois = new ObjectInputStream(new FileInputStream(f));
			Object o=ois.readObject();
			if (o instanceof List<?>) {
				lista=(ArrayList<Date>) o;
			}
		synchronized (this){
		this.dates=lista; }
		ois.close();
		}
		else {dates=new ArrayList<Date>();}
		lista=null;
		f=null;
	}
	/**
	 * Tallentaa kalenterin tilan tiedostoon.
	 * @throws IOException Jos tapahtuu i/o virhe (esim. tiedostoa ei löydy).
	 */
	public void tallenna() throws IOException {
		if (OopHarkka.verbose) System.out.println("\nSuoritetaan kalenterin tallennus.");
		ObjectOutputStream oos = null;
		oos = new ObjectOutputStream(new FileOutputStream("kalenteri.dat"));
		try {
		synchronized (this.dates){
		oos.writeObject(this.dates); }}
		catch (NullPointerException e){}
		oos.close();
		//oos=null;
		tallenna2();
	}
	/**
	 * Tallentaa päivistä luodun hashmapin.
	 * @throws IOException
	 */
	private static void tallenna2() throws IOException {
		if (OopHarkka.verbose) System.out.println("\nSuoritetaan hashmapin tallennus.");
		ObjectOutputStream oos = null;
		oos = new ObjectOutputStream(new FileOutputStream("hashmap.dat"));
		try {
		synchronized (datemap){
		oos.writeObject(datemap); }}
		catch (NullPointerException e){}
		oos.close();
		
	}

	/**
	 * Hakee dates listasta sen alkion indeksin, jonka päivämäärä vastaa haettua.
	 * @param date Haettava päivämäärä.
	 * @return Listan indeksin numero.
	 */
	int indeksi(Temporal date) throws IndexOutOfBoundsException {
		  boolean done=false;
		  int i=0;
		  while(!done) {
			  
			if (dates.get(i).equals(((LocalDateTime) date).toLocalDate())) {
				done=true;
			}
			i++;
			}
		return i-1;
	  }
	
	/**
	 * Hakee dates listasta sen alkion indeksin, joka vastaa haettua.
	 * @param event Event, jossa haettava päivä.
	 * @return Listan indeksin numero, josta vastaavuus löytyy.
	 */
	 int indeksi(Event event) {
		 int i=-1;
			boolean done=false;
			Date temp=this.dates.get(indeksi(event.getStart_date()));
			do {
				i++;
				if (temp.getEvent(i).getOtsikko().equals(event.getOtsikko())) {
					done=true;
				}
			}while(!done);
			return i;
	 }
	 /**
	  * Hakee dates listasta sen alkion indeksin, joka vastaa haettua.
	  * @param task Task, jossa haettava päivä.
	  * @return Listan indeksin numero, josta vastaavuus löytyy.
	  */
	 int indeksi(Task task) {
		 int i=-1;
			boolean done=false;
			Date temp=this.dates.get(indeksi(task.getStart_date()));
			do {
				i++;
				if (temp.getTask(i).getOtsikko().equals(task.getOtsikko())) {
					done=true;
				}
			}while(!done);
			return i;
	 }
	 
	 /**
	  * Luo HashMapin Date-olioista nopeampaa hakua varten.
	  * @return Tiedostosta palautettu HashMap tai kokonaan uusi mikäli vanhaa ei ole.
	  */
		private static HashMap<LocalDate, Date> luoMap() {
			HashMap<LocalDate,Date> temp=null;
			ObjectInputStream ois = null;
			File f = new File("hashmap.dat");
			if (f.exists()){
			if (f.isFile() && f.canRead() ) {
				try {
					ois = new ObjectInputStream(new FileInputStream(f));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Object o=null;;
				try {
					o = ois.readObject();
				} 
				catch (InvalidClassException f1){return new HashMap<LocalDate,Date>();}
				catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				synchronized (datemap){
				temp=(HashMap<LocalDate, Date>) o;}
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			else temp=new HashMap<LocalDate,Date>();
			
			return temp;
		}
		
	  //***********************************
	  //Vain testauksessa käytetyt metodit*
	  //***********************************
	   /**
	   * Muuten sama kuin normaali addEvent() paitsi ei tallennusta.
	   * @param otsikko
	   * @param start
	   * @param end
	   * @param muistutus
	   * @param allday
	   */
	  void addEventTesti(String otsikko,Temporal start, Temporal end,boolean muistutus,boolean allday) {
		  	int i = 0;
		  	try {
		  		i=indeksi(start);
		  	}
		  	catch (IndexOutOfBoundsException e) {
		  		dates.add(new Date(((LocalDateTime) start).toLocalDate()));
		  	}
		  	if (i==dates.size()) {dates.add(new Date(((LocalDateTime) start).toLocalDate()));}
		  	if (allday) {
				start=LocalDateTime.of((LocalDate) ((LocalDateTime) start).toLocalDate(), LocalTime.of(0, 0,0));
				end=LocalDateTime.of((LocalDate) ((LocalDateTime) start).toLocalDate(), LocalTime.of(0, 0,0));
			}
			this.dates.get(i).add(new Event(otsikko,start,end,muistutus,allday));
		}
	  /**
	   * Muuten sama kuin normaali addTask() paitsi ei tallennusta.
	   * @param otsikko
	   * @param start
	   * @param end
	   */
	  void addTaskTesti(String otsikko,Temporal start,Temporal end){
		  int i = 0;
		  	try {
		  		i=indeksi(start);
		  	}
		  	catch (IndexOutOfBoundsException e) {
		  		dates.add(new Date(((LocalDateTime) start).toLocalDate()));
		  	}
		  	if (i==dates.size()) {dates.add(new Date(((LocalDateTime) start).toLocalDate()));}
			this.dates.get(i).add(new Task(otsikko,start, end));
		}
		/**
		 * Poistaa määritellyn tapahtuman.
		 * @param event Poistettava tapahtuma.
		 */
		public void deleteTesti(Event event){
			this.dates.get(indeksi(event.getStart_date())).remove(event);
		}
		/**
		 * Poistaa määritellyn tehtävän.
		 * @param task poistettava tehtävä.
		 */
		public void deleteTesti(Task task){
			this.dates.get(indeksi(task.getStart_date())).remove(task);
		}
		
		/**
		 * Asettaa tehtävälle uuden aloitus- ja/tai lopetusajan.
		 * @param task Muutettava task.
		 * @param alku Uusi aloitusaika.
		 * @param loppu Uusi lopetusaika.
		 */
		public void editTaskTesti(Task task, Temporal alku, Temporal loppu){
			if (alku!=null && loppu!=null) {
				task.setEnd_date(loppu);
				task.setStart_date(alku);
			} else {
			if (alku!=null) task.setStart_date(alku);
			if (loppu!=null) task.setEnd_date(loppu);
			}
			if (!(task.getStart_date().equals(alku))){
				int indeksi=indeksi(task);
				dates.remove(task);
				dates.get(indeksi).add(task);
			}
		}
		/**
		 * Asettaa tehtävälle uuden nimi ja
		 * uuden aloitus- ja lopetusajan.
		 * @param task Tehtävä, jota muutetaan.
		 * @param alku Uusi aloitusaika.
		 * @param loppu Uusi lopetusaika.
		 * @param otsikko Uusi nimi..
		 */
		public void editTaskTesti(Task task, Temporal alku, Temporal loppu, String otsikko){
			task.setEnd_date(loppu);
			task.setStart_date(alku);
			task.setOtsikko(otsikko);
			if (!(task.getStart_date().equals(alku))){
				dates.remove(task);
				dates.get(indeksi(task)).add(task);
			}
		}
		/**
		 * Asettaa tapahtumalle uuden aloitus- ja lopetusajan.
		 * @param event Muutettava event.
		 * @param alku Uusi aloitusaika.
		 * @param loppu Uusi lopetusaika.
		 */
		public void editEventTesti(Event event, Temporal alku, Temporal loppu) {
			if (alku!=null && loppu!=null) {
				event.setEnd_date(loppu);
				event.setStart_date(alku);
			} else {
			if (alku!=null) event.setStart_date(alku);
			if (loppu!=null) event.setEnd_date(loppu);
			}
			if (!(event.getStart_date().equals(alku))){
				dates.remove(event);
				dates.get(indeksi(event)).add(event);
			}
		}
		public void editEventTesti(Event event, Temporal alku, Temporal loppu, String otsikko) {
			event.setEnd_date(loppu);
			event.setStart_date(alku);
			event.setOtsikko(otsikko);
			if (!(event.getStart_date().equals(alku))){
				dates.remove(event);
				dates.get(indeksi(event)).add(event);
			}
		}
	  	/**
		 * Muuten sama kuin normaali tallenna() paitsi eri tiedosto.
		 * @throws IOException
		 */
		public void tallennaTesti() throws IOException {
			System.out.println("Suoritetaan tallennus.");
			ObjectOutputStream oos = null;
			oos = new ObjectOutputStream(new FileOutputStream("kalenteri_testi.dat"));
			synchronized (dates){
			oos.writeObject(this.dates); }
			oos.close();
			oos=null;
		}
		/**
		 * Muuten sama kuin normaali palauta() paitsi eri tiedosto.
		 * @throws FileNotFoundException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		void palautaTesti() throws FileNotFoundException, IOException, ClassNotFoundException {
			System.out.println("Suoritetaan palautus.");
			ObjectInputStream ois = null;
			List<Date> lista = null;
			File f = new File("kalenteri_testi.dat"); 
			if (f.isFile() && f.canRead() ) {
				ois = new ObjectInputStream(new FileInputStream("kalenteri_testi.dat"));
				Object o=ois.readObject();
				if (o instanceof List<?>) {
					lista=(ArrayList<Date>) o;
					}
			synchronized (this){
			this.dates=lista; }
				ois.close();
			}
			else {dates=new ArrayList<Date>();}
			lista=null;
		}
		/**
		 * Alustaa dates listan.
		 */
		void uusi(){
			dates=new ArrayList<Date>();
		}
		public Date getDateTesti(int i){
			return dates.get(i);
		}
}