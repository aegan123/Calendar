package oop2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Random;

//************************************************
//Testiaineiston läpikäymiseen tarkoitettu luokka*
//************************************************
class KalenteriTesti {
	//******************************************
	//Testiaineiston luomiseen käytetyt metodit*
	//******************************************
	/**
	 * Käynnistää testausmoodin.
	 * @param b True tai false riippuen kummalla testausparametrilla ohjelmaa kutsutaan.
	 */
	static void teeTestit(boolean b) {
		Random rnd=new Random();
		if (b) {
			luoTestit(rnd);
			tulostaTestit(b);
			muokkaaTestit(rnd);
			tulostaTestit(!b);
			poistaTestit(rnd);
			tulostaTestit(!b);
		}
		else{
			try {
				OopHarkka.k1.palautaTesti();
			} catch (IOException e) {
				OopHarkka.k1.uusi();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tulostaTestit(b);
			}
	}

	/**
	 * Suorittaa satunnaisten alkioiden poiston testidatasta.
	 * @param rnd Satunnaislukugeneraattori.
	 */
	private static void poistaTestit(Random rnd) {
		int[]arvot=arvo(rnd);
		System.out.println("\nPoistetaan tapahtumia.");
		int i=0,k=0;;
		while (i<4) {
			try {
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.deleteTesti(OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]));
			}
			catch (IndexOutOfBoundsException e) {
				if (OopHarkka.verbose) System.out.println("exeption: i == "+arvot[0]+" , j == "+arvot[1]);
				k++; arvot=arvo(rnd); if (k==10) break;;
				continue;
			}
		arvot=arvo(rnd);
		i++;
		}
		i=0;
		k=0;
		System.out.println("Poistetaan tehtäviä\n");
		while (i<4) {
			arvot=arvo(rnd);
		try {
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.deleteTesti(OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]));
		}
		catch (IndexOutOfBoundsException e) {
			if (OopHarkka.verbose) System.out.println("exeption: i == "+arvot[0]+" , j == "+arvot[1]);
			k++;arvot=arvo(rnd); if (k==10) break;
			continue;
		}
		i++;
		}
	}

	/**
	 * Muokkaa satunnaisia alkioita testidatasta.
	 * @param rnd Satunnaislukugeneraattori.
	 */
	private static void muokkaaTestit(Random rnd) {
		System.out.println("Muokataan tapahtumia.");
		int[]arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editEventTesti(OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]), testit(), testit());
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editEventTesti(OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]), testit(), testit(),OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]).getOtsikko()+"_Muutettu otsikko");
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editEventTesti(OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]), testit(), testit());
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editEventTesti(OopHarkka.k1.getDateTesti(arvot[0]).getEvent(arvot[1]), testit(), testit());
		//
		System.out.println("Muokataan tehtäviä.");
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editTaskTesti(OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]), testit(), testit());
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editTaskTesti(OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]), testit(), testit(),OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]).getOtsikko()+"_Muutettu otsikko");
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editTaskTesti(OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]), testit(), testit());
		arvot=arvo(rnd);
		if (OopHarkka.verbose) System.out.println("i == "+arvot[0]+" , j == "+arvot[1]);
		OopHarkka.k1.editTaskTesti(OopHarkka.k1.getDateTesti(arvot[0]).getTask(arvot[1]), testit(), testit());
		if (OopHarkka.verbose) System.out.println("dates.size == "+OopHarkka.k1.getSize());
	}
	/**
	 * Tuottaa validin Date ja task/event indeksiparin.
	 * @param rnd Satunnaislukugeneraattori
	 * @return Validi Date ja Task/Event indeksipari.
	 */
	private static int[] arvo(Random rnd) {
		int[] arvot=new int[2];
		arvot[0]=rnd.nextInt(OopHarkka.k1.getSize());
		arvot[1]=rnd.nextInt(OopHarkka.k1.getDateTesti(arvot[0]).getEventSize()+1);
		if (OopHarkka.verbose) System.out.println("arvot[0] == "+arvot[0]+" , arvot[1] == "+arvot[1]);
		while(!(arvot[1]>0)){
			arvot[0]=rnd.nextInt(OopHarkka.k1.getSize());
			arvot[1]=rnd.nextInt(OopHarkka.k1.getDateTesti(arvot[0]).getEventSize()+1);
			if (OopHarkka.verbose) System.out.println("arvot[0] == "+arvot[0]+" , arvot[1] == "+arvot[1]);
		}
		arvot[1]--;
		return arvot;
		
	}
	/**
	 * Tulostaa testidatan.
	 * @param b 
	 * @throws FileNotFoundException 
	 */
	private static void tulostaTestit(boolean b) {
		PrintWriter out=null;
		if (b)
			try {
				out= new PrintWriter("testi.txt");
			} catch (FileNotFoundException e1) {
				System.out.println("File not found.");
			}
		else
			try {
				out= new PrintWriter("testi2.txt");
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
			}
		
	    if (OopHarkka.verbose) tulostaExtra();
		System.out.println("Testiaineiston tulostus.\nTapahtumat:\n");
		out.println("Testiaineiston tulostus.\nTapahtumat:\n");
		for (int i=0;i<OopHarkka.k1.getSize();i++) {
			for (int j=0;j<OopHarkka.k1.getDateTesti(i).getEventSize();j++) {
			System.out.println(OopHarkka.k1.getDateTesti(i).getEvent(j).toString()); 
			out.println(OopHarkka.k1.getDateTesti(i).getEvent(j).toString());}
		}
		System.out.println("---------\nTehtävät:\n");
		out.print("---------\nTehtävät:\n");
		for (int i=0;i<OopHarkka.k1.getSize();i++){
			for (int j=0;j<OopHarkka.k1.getDateTesti(i).getTaskSize();j++) {
			System.out.println(OopHarkka.k1.getDateTesti(i).getTask(j).toString());
			out.println(OopHarkka.k1.getDateTesti(i).getTask(j).toString());}
		}
		out.close();
	}
	/**
	 * Extra OopHarkka.verbose tulostukset.
	 */
	private static void tulostaExtra() {
		System.out.println(OopHarkka.k1.getDates().toString());
	}
	/**
	 * Luo testidatan.
	 * @param rnd2 
	 */
	private static void luoTestit(Random rnd2) {
		System.out.println("Luodaan 10 testitapahtumaa ja 10 testitehtävää.");
		LocalDateTime start=null;
		LocalDateTime end=null;
		int i=1;
		while (i<11) {
			start=testit();
			end=testit();
			OopHarkka.k1.addEventTesti("Testi_event_"+i, start, end, rnd2.nextBoolean(), rnd2.nextBoolean());
			i++;
			
		}
		i=1;
		while (i<11) {
				start=testit();
				end=testit();
		
			OopHarkka.k1.addTaskTesti("Testi_task_"+i, start, end);
			i++;
		}
		
		try {
			OopHarkka.k1.tallennaTesti();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (OopHarkka.verbose) System.out.println("dates.size == "+OopHarkka.k1.getSize());
	}
	
	/**
	 * Tuottaa LocalDateTime olion käyttämällä satunnaislukuja.
	 * @throws DateTimeExeption Mikäli satunnaiset arvot eivät vastaa kelvollista päivämäärää/kellonaikaa.
	 * @return LocalDateTime olio.
	 */
	private static LocalDateTime testit2() throws DateTimeException {
		Random rnd = new Random();
		return LocalDateTime.of(rnd.nextInt(60)+2000, rnd.nextInt(60), rnd.nextInt(60), rnd.nextInt(60), rnd.nextInt(60));
	}
	/**
	 * Tuottaa LocalDateTime olion.
	 * @return Validi LocalDateTime olio.
	 */
	private static LocalDateTime testit() {
		LocalDateTime start=null;
		boolean done=false;
		do {
			try{
			start=testit2();
			}
			catch (DateTimeException e) {
				continue;
			}
			done=true;
		}while (!done);
		return start;
	}
	/**
	 * Vertaa eri testiajojen tuottamia tiedostoja keskenään.
	 * thx to: http://javaconceptoftheday.com/compare-two-text-files-in-java/
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	static void vertaaTestit() throws IOException, FileNotFoundException {
		BufferedReader reader1 = new BufferedReader(new FileReader("testi.txt"));
        BufferedReader reader2 = new BufferedReader(new FileReader("testi2.txt"));
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();
        boolean areEqual = true;
        int lineNum = 1;
        while (line1 != null || line2 != null)
        {
            if(line1 == null || line2 == null)
            {
                areEqual = false;
                break;
            }
            else if(! line1.equalsIgnoreCase(line2))
            {
                areEqual = false;
                break;
            }
            line1 = reader1.readLine();
            line2 = reader2.readLine();
            lineNum++;
        }
        if(areEqual)
        {
            System.out.println("Two files have same content.");
        }
        else
        {
            System.out.println("Two files have different content. They differ at line "+lineNum);
            System.out.println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum);
        }
        reader1.close();
        reader2.close();
    }
	/**
	 * Poistaa vertailussa käytetyt tiedostot.
	 */
	static void delFile() {
		File file1=null,file2=null,file3=null;;
		file1=new File("testi.txt");
		file2=new File("testi2.txt");
		//file3=new File("kalenteri_testi.dat");
		if (file1.exists()&&file1.canRead()) file1.delete(); else file1.deleteOnExit();
		if (file2.exists()&&file2.canRead()) file2.delete(); else file2.deleteOnExit();
		//if (file3.exists()&&file3.canRead()) file3.delete(); else file3.deleteOnExit();
	}

}