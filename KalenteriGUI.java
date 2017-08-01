package oop2017;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class KalenteriGUI extends JFrame implements Runnable, WindowListener 
{

	private static final long serialVersionUID = 2L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();
	private JFrame frame;
	private JButton uusi_event;
	private JButton uusi_task;
	private JButton edit_event;
	private JButton edit_task;
	private JButton del_event;
	private JButton del_task;
	private JButton quit;
	private JPanel paneeli;
	
	public KalenteriGUI() {
		uusi_event=new JButton("Lisää tapahtuma");
		uusi_task=new JButton("Lisää tehtävä");
		edit_event=new JButton("Muokkaa tapahtumaa");
		edit_task=new JButton("Muokkaa tehtävää");
		del_event=new JButton("Poista");
		del_task=new JButton("Poista");
		quit=new JButton("Lopeta");
		paneeli=new JPanel();
	}

	
	void showIt() {
		this.setVisible(true);
	}


	@Override
	public void run() {
		frame=new JFrame();
		setSize((int)(width/4*1.5), (int)(width/4*1.5));
		setLocation((int)width/4, (int)height/4);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Kalenteri");
		frame.add(paneeli);
		frame.getContentPane().add(quit);
		//frame.pack();
		showIt();
		
		
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		OopHarkka.sulje();
		//System.exit(0);
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		//Oop_harkka.sulje();
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
