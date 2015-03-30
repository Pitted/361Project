import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class ChronoTimerGUI{
	/**
	 * 
	 */
	private ArrayList<Competitor> racing = new ArrayList<>();
	private static final long serialVersionUID = 1L;
	public static void main(String[] args){
		new ChronoTimerGUI();
	}
	public ChronoTimerGUI(){
		JFrame frame = new JFrame();
		JPanel pane = new JPanel();
		frame.add(pane);
		JPanel channelPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JPanel displayPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(channelPanel,BorderLayout.PAGE_START);
		pane.add(inputPanel, BorderLayout.LINE_END);
		pane.add(controlPanel, BorderLayout.LINE_START);
		pane.add(displayPanel, BorderLayout.CENTER);
		
		channelPanel.setLayout(new BoxLayout(channelPanel, BoxLayout.Y_AXIS));
		JPanel startChannels = new JPanel();
		JPanel finishChannels = new JPanel();
		startChannels.setLayout(new FlowLayout());
		finishChannels.setLayout(new FlowLayout());
		
		startChannels.add(new JLabel("Start"));
		finishChannels.add(new JLabel("Finish"));
		JButton[] strtchnlButtons = new JButton[]{new JButton()};
		strtchnlButtons[0].setText("1");
		strtchnlButtons[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(ChronoTimer.isArmed(1))
					ChronoTimer.disarmChannel(1);
				else
					ChronoTimer.armChannel(1);
				
			}
		});
		JButton[] fnchnlButtons = new JButton[]{new JButton()};
		fnchnlButtons[0].setText("2");
		fnchnlButtons[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(ChronoTimer.isArmed(2))
					ChronoTimer.disarmChannel(2);
				else
					ChronoTimer.armChannel(2);
				
			}
		});
		channelPanel.add(startChannels); channelPanel.add(strtchnlButtons[0]);
		channelPanel.add(finishChannels); channelPanel.add(fnchnlButtons[0]);
		
		 /***********Command Panel Buttons*************/
        JButton btnPwr = new JButton("Power");
        JButton btnDNF = new JButton("DNF");
        JButton btnStart = new JButton("Start");
        JButton btnEnd = new JButton("End");
        JButton btnCan = new JButton("Cancel");
        JButton btnPnt = new JButton("Print");

        // Tool Tips
        btnPwr.setToolTipText("Shuts down the Chronotimer system");
        btnDNF.setToolTipText("Racers yet to finish will be counted as did not finish");
        btnStart.setToolTipText("Start the current race");
        btnEnd.setToolTipText("Ends current race");
        btnCan.setToolTipText("Cancels current racer");
        btnPnt.setToolTipText("Prints the last recorded race");
        
        // Action Commands
        btnPwr.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(!ChronoTimer.getPower())ChronoTimer.powerOn();
                else ChronoTimer.powerOff();
            }
        });        
                
        btnStart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ChronoTimer.start();
            }
        });    
        
        btnEnd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ChronoTimer.finish();
            }
        });    
        
        btnCan.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ChronoTimer.cancel();
            }
        });    
        
        btnDNF.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ChronoTimer.dnf();
            }
        });    
        
        btnPnt.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ChronoTimer.print();
            }
        });    
        
        // Setting up layout
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
        
        controlPanel.add(btnPwr);
        controlPanel.add(btnStart);
        controlPanel.add(btnEnd);
        controlPanel.add(btnCan);
        controlPanel.add(btnDNF);
        controlPanel.add(btnPnt);
        
        /*********End of Command Panel Properties****/
		
		String [] raceType = new String[]{"IND", "PARIND", "GRP", "PARGRP"};
		JComboBox<String> raceTypes = new JComboBox<>(raceType);
		JButton create = new JButton("Set");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// will implement this when Chronotimer is finished.
				
			}
		});
		final JTextField racerNum = new JTextField("Enter Racer Bib #");
		JButton addRacer = new JButton("Add");
		addRacer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					ChronoTimer.addCompetitor(new Competitor(Integer.parseInt(racerNum.getText())));
				}
				catch(Exception ex){
					racerNum.setText("ERR");
				}
			}
		});
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.add(raceTypes);
		inputPanel.add(create);
		inputPanel.add(racerNum);
		inputPanel.add(addRacer);
		
		
		
		frame.setVisible(true);
	}
	
	private String print(){
		String str = new String();
		
		
		
		
		return str;
	}
	
	
	
}