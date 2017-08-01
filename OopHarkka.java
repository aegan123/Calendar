package oop2017;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author Juhani Vähä-Mäkilä
 * CC BY-ND-NC
 * @version 0.5
 *
 */
public class OopHarkka {
	
	//Kontrolloi main-threadin suoritusta, kun gui on päällä.
	private static boolean running;
	//Vain yksi kalenteri ja jotta guin puolelta olisi helppo kutsua.
	static Kalenteri k1;
	//Jotta voi kätevästi käyttää eri puolilla cli:tä.
	private static final Scanner in=new Scanner(System.in);
	private static final LocalDate today=LocalDate.now();
	static boolean verbose=false;
	
	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		//Mahdollisten komentoriviparametrien käsittely
		switch (args.length) {
		case (0):
			//System.out.println(args.length);
			break;
		case (1):
			//System.out.println(args.length);
			if (args[0].equals("verbose")) {verbose=true;}
			if (args[0].equals("gui")){guiKalenteri();}
			if (args[0].equals("testi")) {k1=new Kalenteri(true);KalenteriTesti.teeTestit(true);System.exit(0);}
			if (args[0].equals("testi2")) {k1=new Kalenteri();KalenteriTesti.teeTestit(false);System.exit(0);}
			if (args[0].equals("testi3")) {KalenteriTesti.vertaaTestit();KalenteriTesti.delFile();System.exit(0);}
			break;
		case(2):
			//System.out.println(args.length);
			if (args[0].equals("testi")&&args[1].equals("verbose")) {verbose=true;k1=new Kalenteri(true);KalenteriTesti.teeTestit(true);System.exit(0);}
			if (args[0].equals("testi2")&&args[1].equals("verbose")) {verbose=true;k1=new Kalenteri();KalenteriTesti.teeTestit(false);System.exit(0);}
			break;
		default:
			break;
		}
		startCal();
		cliKalenteri();
		sulje();
		}

	private static void startCal() {
		k1=new Kalenteri();
		k1.setDaemon(true);
		k1.start();
	}
	/**
	 * Sulkeet säikeet, tallentaa datan ja lopettaa ohjelman.
	 */
	static void sulje() {
		try {
			if (verbose) System.out.println("Lopetetaan säikeet. Tallennetaan data. Lopetetaan ohjelma.");
			running=false;
			k1.setRunning(false);
			k1.join(5000);
			k1.tallenna();
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {System.exit(1);}
		}
	
	
	/**
	 * Luo guin ja näyttää sen. Jää odottamaan lopetusta.
	 */
	private static void guiKalenteri() {
		startCal();
		KalenteriGUI gui= new KalenteriGUI();
		//gui.showIt();
		new Thread(gui).start();
		running=true;
		Thread.currentThread().setPriority(1);
		do {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (running);
		
	}
	
	//**********************************************
	//Komentorivikäyttöliittymään liittyvät metodit*
	//**********************************************
	
	/**
	 * Ohjelman aloitusvalikko.
	 */
	private static void cliKalenteri() {
		clear();
		System.out.println("Tervetuloa käyttämään kalenteria.\n");
		today();
		boolean lopetus=false;
		while (!lopetus){
			System.out.println("Mitä haluat tehdä?\n\n\t1) Tarkastele merkintöjä.\n\t2) Lisää uusi merkintä.\n\t3) Muokkaa olemassa olevaa.\n\t4) Poista merkintä.\n\t9) Poista kaikki merkinnät.\n\n\t0) Lopeta sovellus.\n");
			int valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (1):
			view();
			break;
		case (2):
			add();
			break;
		case (3):
			edit();
			break;
		case (4):
			delete();
			break;
		case (9):
			delAll();
			clear();
			break;
		case (0):
			lopetus=true;
			System.out.println("Tallennetaan tiedot ja lopetetaan. Tässä voi kestää hetki.");
			break;
		default:
			lopetus=true;
			System.out.println("Tallennetaan tiedot ja lopetetaan. Tässä voi kestää hetki.");
			break;
		}
		}
	}
	/**
	 * Tyhjentää koko kalenterin, jos ja vain jos käyttäjä niin haluaa.
	 */
	private static void delAll() {
		System.out.print("VAROITUS!\nTämä tyhjentää kaikki tapahtumat ja tehtävät kalenterista!\nHaluatko varmasti jatkaa? (k/e) ");
		if (in.nextLine().toLowerCase().charAt(0)=='k') {
			System.out.print("Viimeinen varoitus!\nHaluatko varmasti poistaa kaiken? (k/e) ");
			if (in.nextLine().toLowerCase().charAt(0)=='k') {System.out.println("Poistetaan kaikki merkinnät...");k1.reset();}
		}
		
	}

	/**
	 * Tulostaa tiedon kuluvasta päivästä.
	 */
	private static void today(){
		//System.out.println("Tänään on "+today.getDayOfMonth()+"."+today.getMonthValue()+"."+today.getYear());
		System.out.println("Tänään on "+today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
	}
	
	/**
	 * Pyytää käyttäjältä numeron (valittavat numerot on näytetty ennen kutsua). Syötteen tulee olla luku ja nollaa suurempi.
	 * @return Käyttäjän syöttämä numero.
	 */
	private static int valinta() {
		boolean ok=true;
		int valinta = -1;
		while (ok){
			System.out.print("Anna valinta: ");
			String input=in.nextLine();
			try {
				valinta=Integer.parseInt(input); }
			catch (NumberFormatException e) {
				System.out.println("Virheellinen syöte. Syötä numero.");
				continue;
			}
			if (valinta<0) {System.out.println("Virhe. Luvun tulee olla nollaa suurempi."); continue;}
			ok=false;
		}
		return valinta;
	}
	
	/**
	 * Valikko tietueiden poistamista varten.
	 */
	private static void delete() {
		today();
		System.out.println("\n\t1) Poista tehtäviä.\n\t2) Poista tapahtumia.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (1):
			delTask();
			break;
		case (2):
			delEvent();
			break;
		case (3):
			break;
		default:
			break;
		}
		
		clear();
	}
	
	/**
	 * Poistaa käyttäjän määrittelemän tapahtumatietueen.
	 */
	private static void delEvent() {
		today();
		System.out.println("\nMinkä päivän tapahtumia haluat poistaa?\n\t1) Tämän päivän.\n\t2) Jonkun muun päivän.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		Date temp=null;
		switch (valinta) {
		case (1):
			//temp=k1.getDate(k1.indeksi(LocalDateTime.of(today, LocalTime.of(0, 0))));
			temp=k1.getDate(today);
		if (verbose) System.out.println(temp);
		for (int i=1;i<=temp.getEventSize();i++) {
			System.out.println("\t"+i+") "+temp.getEvent(i));
		}
			valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			k1.delete(temp.getEvent(valinta));
			temp=null;
			break;
		case (2):
			//temp=k1.getDate(k1.indeksi(LocalDateTime.of(userDate(), LocalTime.of(0, 0))));
			temp=k1.getDate(userDate());
		if (verbose) System.out.println(temp);
		for (int i=1;i<=temp.getEventSize();i++) {
			System.out.println("\t"+i+") "+temp.getEvent(i));
		}
			valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			k1.delete(temp.getEvent(valinta));
			temp=null;
			break;
		case (0):
			delete();
			break;
		default:
			break;
		}
	}


	/**
	 * Poistaa käyttäjän määrittelemän tehtävätietueen.
	 */
	private static void delTask() {
		today();
		System.out.println("\nMinkä päivän tehtäviä haluat poistaa?\n\t1) Tämän päivän.\n\t2) Jonkun muun päivän.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		Date temp=null;
		switch (valinta) {
		case (1):
			//temp=k1.getDate(k1.indeksi(LocalDateTime.of(today, LocalTime.of(0, 0))));
			temp=k1.getDate(today);
		if (verbose) System.out.println(temp);
		for (int i=1;i<=temp.getTaskSize();i++) {
			System.out.println("\t"+i+") "+temp.getTask(i));
		}
			valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			k1.delete(temp.getTask(valinta));
			temp=null;
			break;
		case (2):
			//temp=k1.getDate(k1.indeksi(LocalDateTime.of(userDate(), LocalTime.of(0, 0))));
			temp=k1.getDate(userDate());
		if (verbose) System.out.println(temp);
		for (int i=1;i<=temp.getEventSize();i++) {
			System.out.println("\t"+i+") "+temp.getTask(i));
		}
			valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			k1.delete(temp.getTask(valinta));
			temp=null;
			break;
		case (0):
			delete();
			break;
		default:
			break;
		}
		
	}


	/**
	 * Valikko tietueiden muokkaamiseen.
	 */
	private static void edit() {
		System.out.println("\t1) Muokkaa tapahtumia.\n\t2) Muokkaa tehtäviä.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (2):
			editTask();
			break;
		case (1):
			editEvent();
			break;
		case (3):
			break;
		default:
			break;
		}
		
		clear();
	}
	
	/**
	 * Valikko tapahtumien muokkaukseen.
	 */
	private static void editEvent() {
		today();
		System.out.println("\nMinkä päivän tapahtumia haluat muokata?\n\t1) Tämän päivän.\n\t2) Jonkun muun päivän.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (1):
			eventEdit(today);
			break;
		case (2):
			eventEdit(userDate());
			break;
		case (0):
			edit();
			break;
		default:
			break;
		}
		
	}
	/**
	 * Muokkaa käyttäjän valitsemaan tapahtumaa käyttäjän syöttämillä tiedoilla.
	 * Tarkistaa onko aloituspäivä ennen lopetuspäivää.
	 * @param date Haluttu päivämäärä.
	 */
	private static void eventEdit(LocalDate date) {
		Date temp_date=null;
		LocalDateTime alku = null,loppu = null;
		LocalDate temp=null;
		LocalTime temp2=null;
		String otsikko=null;
		Event event=null;
		boolean ok=true,done=false;
		//temp_date=k1.getDate(k1.indeksi(LocalDateTime.of(date, LocalTime.of(0, 0))));
		temp_date=k1.getDate(date);
		if (verbose) System.out.println(temp_date);
		if (temp_date!=null) {
		System.out.println("Valitse mitä tapahtumaa haluat muokata.\n");
		for (int i=1;i<=temp_date.getEventSize();i++) {
			System.out.println(i+") "+temp_date.getEvent(i-1));
		}
			int valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			event=temp_date.getEvent(valinta-1);
			//while(!done) {
			System.out.println("\t1) Muokkaa kaikkia tietoja.\n\t2) Muokkaa nimeä.\n\t3) Muokkaa aloitusaikaa.\n\t4) Muokkaa lopetusaikaa.\n\t5) Aseta kokopäiväiseksi.\n\t6) Poista kokopäiväisyys ja anna uusi aloitus- ja lopetusaika.\n\n\t0) Palaa takaisin.");
			int valinta2=valinta();
			if (verbose) System.out.println("Valinta2: "+valinta2);
			switch (valinta2) {
			case (1):
				today();
				System.out.print("\nAnna uusi nimi: ");
				otsikko=in.nextLine();
				while (ok) {
					System.out.println("\nAsetetaan uusi aloitusaika: ");
					temp=userDate();
					temp2=userTime();
					alku=LocalDateTime.of(temp, temp2);
					System.out.println("\nAsetetaan uusi lopetusaika: ");
					temp=userDate();
					temp2=userTime();
					loppu=LocalDateTime.of(temp, temp2);
					if (!testDates(alku,loppu)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
					ok=false;
				}
				if (verbose) System.out.println("uusi otsikko: "+otsikko+" , "+alku+" , "+loppu);
				k1.editEvent(event, alku, loppu, otsikko);
				done=true;
				break;
			case (2):
				System.out.print("Anna uusi nimi: ");
				otsikko=in.nextLine();
				if (verbose) System.out.println("uusi otsikko: "+otsikko);
				k1.editEvent(event, otsikko);
				break;
			case (3):
				today();
			while(ok) {
				System.out.println("\n\nAsetetaan uusi aloitusaika: ");
				temp=userDate();
				if (event.isAllDay()) temp2=LocalTime.of(0, 0);
				else temp2=userTime();
				alku=LocalDateTime.of(temp, temp2);
				if (!testDates(alku,event.getEnd_date())) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
			}
				if (verbose) System.out.println("uusi aika: "+alku);
				k1.editEvent(event, alku, null);
				done=true;
				break;
			case (4):
				today();
				while (ok){
				System.out.println("\n\nAsetetaan uusi lopetusaika: ");
				temp=userDate();
				if (event.isAllDay()) temp2=LocalTime.of(0, 0);
				else temp2=userTime();
				loppu=LocalDateTime.of(temp, temp2);
				if (!testDates(event.getStart_date(),loppu)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
				}
				if (verbose) System.out.println("uusi aika: "+loppu);
				k1.editEvent(event, null, loppu);
				done=true;
				break;
			case (5):
				k1.editEvent(temp_date.getEvent(valinta-1), true);
				done=true;
				break;
			case (6):
				today();
			while(ok){
				System.out.println("\n\nAsetetaan uusi aloitusaika: ");
				temp=userDate();
				temp2=userTime();
				alku=LocalDateTime.of(temp, temp2);
				System.out.println("\nAsetetaan uusi lopetusaika: ");
				temp=userDate();
				temp2=userTime();
				loppu=LocalDateTime.of(temp, temp2);
				if (!testDates(event.getStart_date(),loppu)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
			}
				if (verbose) System.out.println(alku+" , "+loppu);
				k1.editEvent(event, alku,loppu,false);
				done=true;
				break;
			case (0):
				done=true;
				break;
			default:
				done=true;
				break;
			}
		}
		else {System.out.println("Halutulla päivällä ei ole tapahtumia.");}
		
			temp=null;temp2=null;;alku=null;loppu=null;otsikko=null;temp_date=null;event=null;
		}
	
	/**
	 * Valikko tehtävien muokkaamiseen.
	 */
	private static void editTask() {
		today();
		System.out.println("\nMinkä päivän tehtäviä haluat muokata?\n\t1) Tämän päivän.\n\t2) Jonkun muun päivän.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (1):
			taskEdit(today);
			break;
		case (2):
			taskEdit(userDate());
			break;
		case (0):
			edit();
			break;
		default:
			break;
		}
		
	}
	/**
	 * Muokkaa käyttäjän valitsemaa tehtävää käyttäjän syöttämillä tiedoilla.
	 * Tarkistaa onko aloituspäivä ennen lopetuspäivää.
	 * @param date Haluttu päivämäärä.
	 */
	private static void taskEdit(LocalDate date) {
		Date temp_date=null;
		LocalDateTime alku = null,loppu = null;
		LocalDate temp=null;
		LocalTime temp2=null;
		String otsikko=null;
		Task task=null;
		boolean ok=true;
		temp_date=k1.getDate(date);
		if (verbose) System.out.println(temp_date);
		if (temp_date!=null){
		for (int i=1;i<=temp_date.getTaskSize();i++) {
			System.out.println(i+") "+temp_date.getTask(i-1));
		}
			int valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			task=temp_date.getTask(valinta-1);
			System.out.println("\t1) Muokkaa kaikkia tietoja.\n\t2) Muokkaa nimeä.\n\t3) Muokkaa aloitusaikaa.\n\t4) Muokkaa lopetusaikaa.\n\n\t0) Palaa takaisin.");
			int valinta2=valinta();
			if (verbose) System.out.println("Valinta2: "+valinta2);
			switch (valinta2) {
			case (1):
				today();
				System.out.print("\nAnna uusi nimi: ");
				otsikko=in.nextLine();
				while(ok) {
				System.out.println("\nAsetetaan uusi aloitusaika: ");
				temp=userDate();
				temp2=userTime();
				alku=LocalDateTime.of(temp, temp2);
				System.out.println("\nAsetetaan uusi lopetusaika: ");
				temp=userDate();
				temp2=userTime();
				loppu=LocalDateTime.of(temp, temp2);
				if (!testDates(task.getStart_date(),loppu)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
				}
				if (verbose) System.out.println("uusi otsikko: "+otsikko+" , "+alku+" , "+loppu);
				k1.editTask(task, alku, loppu, otsikko);
				break;
			case (2):
				System.out.print("Anna uusi nimi: ");
				otsikko=in.nextLine();
				if (verbose) System.out.println("uusi otsikko: "+otsikko);
				k1.editTask(task, otsikko);
				break;
			case (3):
				today();
			while(ok){
				System.out.println("\n\nAsetetaan uusi aloitusaika: ");
				temp=userDate();
				temp2=userTime();
				alku=LocalDateTime.of(temp, temp2);
				if (!testDates(alku,task.getEnd_date())) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
			}
				if (verbose) System.out.println("uusi aika: "+alku);
				k1.editTask(task, alku, null);
				break;
			case (4):
				today();
			while(ok){
				System.out.println("\n\nAsetetaan uusi lopetusaika: ");
				temp=userDate();
				temp2=userTime();
				loppu=LocalDateTime.of(temp, temp2);
				if (!testDates(task.getStart_date(),loppu)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
				ok=false;
			}
				if (verbose) System.out.println("uusi aika: "+loppu);
				k1.editTask(task, null, loppu);
				break;
			case (0):
				break;
			default:
				break;
			}}
		else {System.out.println("Halutulla päivällä ei ole tehtäviä.");}
			
		
			temp=null;temp2=null;;alku=null;loppu=null;otsikko=null;temp_date=null;task=null;task=null;
		
	}
	
	/**
	 * Valikko tietueiden lisäämiseen.
	 */
	private static void add() {
		System.out.println("\t1) Lisää tapahtuma.\n\t2) Lisää tehtävä.\n\n\t0) Palaa takaisin.");
		int valinta=valinta();
		if (verbose) System.out.println("Valinta: "+valinta);
		switch (valinta) {
		case (1):
			addEvent();
			break;
		case (2):
			addtask();
			break;
		case (0):
			break;
		default:
			break;
		}
	
		clear();
	}
	/**
	 * Lisää uuden tehtävän oikean päivän kohdalle käyttäjän syöttämillä tiedoilla.
	 */
	private static void addtask() {
		boolean ok=true;
		LocalDateTime start=null,end=null;
		today();
		System.out.print("\nAnna tehtävälle nimi: ");
		String otsikko=in.nextLine();
		while(ok){
		LocalDate date=userDate();
		LocalTime time=userTime();
		start=LocalDateTime.of(date, time);
		date=userDate();
		time=userTime();
		end=LocalDateTime.of(date, time);
		if (!testDates(start,end)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
		ok=false;
		}
		if (verbose) System.out.println("start == "+start+" , end == "+end+" , otsikko == "+otsikko);
		k1.addTask(otsikko, start, end);
		
	}


	/**
	 * Lisää uuden tapahtuman oikean päivän kohdalle käyttäjän syöttämillä tiedoilla.
	 */
	private static void addEvent() {
		String input=null;
		LocalDate date=null;
		LocalDateTime start=null, end=null;
		boolean ok=true;
		today();
		System.out.print("\nAnna tapahtumalle nimi: ");
		String otsikko=in.nextLine();
		System.out.print("\nOnko tapahtuma koko päivän kestävä? (k/e): ");
		input=in.nextLine();
		if (input.toLowerCase().charAt(0)=='k') {
			while(ok){
			date=userDate();
			start=LocalDateTime.of(date, LocalTime.of(0, 0));
			date=userDate();
			end=LocalDateTime.of(date, LocalTime.of(0, 0));
			if (!testDates(start,end)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
			ok=false;
			}
			if (verbose) System.out.println("start == "+start+" , end == "+end+" , otsikko == "+otsikko);
			k1.addEvent(otsikko, start, end, false, true);
		} else {
			LocalTime time=null;
			today();
			while(ok){
		date=userDate();
		time=userTime();
		start=LocalDateTime.of(date, time);
		date=userDate();
		time=userTime();
		end=LocalDateTime.of(date, time);
		if (!testDates(start,end)) {System.out.println("Virhe! Aloituspäivän tulee olla ennen lopetuspäivää.");continue;}
		ok=false;
		}
		if (verbose) System.out.println("start == "+start+" , end == "+end+" , otsikko == "+otsikko);
		k1.addEvent(otsikko, start, end);}
		
	}


	/**
	 * Valikko tietueiden katselua varten.
	 */
	private static void view() {
		boolean lopeta=false;
		while (!lopeta){
			System.out.println("\t1) Katso tämän päivän tapahtumat ja tehtävät.\n\t2) Katso jonkun muun päivän tapahtumat ja tehtävät.\n\n\t0) Palaa takaisin.");
			int valinta=valinta();
			if (verbose) System.out.println("Valinta: "+valinta);
			switch (valinta) {
				case (1):
					katseleNyt(today);
					System.out.println();
					break;
				case (2):
					katseleNyt(userDate());
					System.out.println();
					break;
				case (0):
					lopeta=true;
					break;
				default:
					lopeta=true;
					break;
		}
		}
		clear();
	}
	/**
	 * Pyytää käyttäjältä tiettyä päivämäärää.
	 * @return LocalDate olio, jossa käyttäjän syöttämä päivämäärä.
	 */
	private static LocalDate userDate() {
		System.out.print("Anna päivämäärä (muodossa pp.kk.vvvv): ");
		String input = null;
		StringTokenizer temp=null;
		int dayOfMonth = 0,month = 0,year = 0;
		LocalDate date=null;
		while (true) {
			input = in.nextLine();
			temp=new StringTokenizer(input, "."); 			
			try {
				dayOfMonth=Integer.parseInt(temp.nextToken());
				month=Integer.parseInt(temp.nextToken());
				year=Integer.parseInt(temp.nextToken());
			}
			catch (NumberFormatException e) {
				System.out.print("Virheellinen päivämäärä. Anna uusi: ");
				continue;
			}
			catch (NoSuchElementException g) {
				System.out.print("Virheellinen syöte. Yritä uudestaan: ");
				continue;
			}
			try {
				date=LocalDate.of(year, month, dayOfMonth);
			}
			catch (DateTimeException f) {
				System.out.print("Virheellinen päivämäärä. Yritä uudestaan: ");
				continue;
			}
			break;
			
		}
		return date;
		
	}
	/**
	 * Pyytää käyttäjältä tiettyä kellonaikaa.
	 * @return LocalTime olio, jossa käyttäjän syöttämä päivämäärä.
	 */
	private static LocalTime userTime() {
		System.out.print("Anna kellonaika (muodossa hh.mm): ");
		String input = null;
		StringTokenizer temp=null;
		int hour = 0,min = 0;
		LocalTime time=null;
		while (true) {
			input = in.nextLine();
			temp=new StringTokenizer(input, "."); 			
			try {
				hour=Integer.parseInt(temp.nextToken());
				min=Integer.parseInt(temp.nextToken());
			}
			catch (NumberFormatException e) {
				System.out.print("Virheellinen aika. Anna uusi: ");
				continue;
			}
			catch (NoSuchElementException g) {
				System.out.print("Virheellinen syöte. Yritä uudestaan: ");
				continue;
			}
			try {
				time=LocalTime.of(hour, min);
			}
			catch (DateTimeException f) {
				System.out.print("Virheellinen aika. Yritä uudestaan: ");
				continue;
			}
			break;
			
		}
		return time;
	}
	/**
	 * Tulostaa ruudulle tietyn päivän tietueiden tiedot.
	 * @param date Haluttu päivämäärä.
	 */
	private static void katseleNyt(LocalDate date) {
		System.out.println("Haetaan tietoja....");
		//boolean done=false;
		ArrayList<Event> temp=null;
		ArrayList<Task> temp2=null;
	/*	int i=0;
		do {
			try {
			if (date.equals(k1.getDate(i).getDate())) { 
				temp=k1.getDate(i).getEvents();
				temp2=k1.getDate(i).getTasks();
				done=true;
				}}
			catch (IndexOutOfBoundsException | NullPointerException d) {
				if (verbose) System.out.println("Haluttu Date ei olemassa.");
				done=true;
			}
			i++;
		}while(!done); */
		Date tempdate=k1.getDate(date);
		if (tempdate!=null) {
			temp=tempdate.getEvents();
			temp2=tempdate.getTasks(); }
		else {if (verbose) System.out.println("Haluttu Date ei olemassa.");}
		if (temp==null|| temp.isEmpty()) {System.out.println("Ei tapahtumia.");} else {
		System.out.println("Päivän tapahtumat:\n");
		if (verbose) System.out.println("Event size: "+temp.size());
		for (int j=0;j<temp.size();j++) {
			System.out.println(temp.get(j));
		}}
		if (temp2==null||temp2.isEmpty()) {System.out.println("Ei tehtäviä."); } else {
		System.out.println("Päivän tehtävät:\n");
		if (verbose) System.out.println("Task size: "+temp2.size());
		for (int j=0;j<temp2.size();j++) {
			System.out.println(temp2.get(j));
		}}
		
	}
	/**
	 * Testaa onko aloituspäivämäärä ennen lopetuspäivämäärää tai aloituspäivä==lopetuspäivä.
	 * @param start Aloituspäivä.
	 * @param end Lopetuspäivä.
	 * @return True jos on. Muuten false.
	 */
	private static boolean testDates(ChronoLocalDateTime<?> start, ChronoLocalDateTime<?> end) {
		return start.compareTo(end)<0 || start.isEqual(end);
	}
	/**
	 * Luo illuusion, että ruutu tyhjenisi tulostamalla reilusti rivinvaihtoja.
	 */
	private static void clear() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
	}	
}