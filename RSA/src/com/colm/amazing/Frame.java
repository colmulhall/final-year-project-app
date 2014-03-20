package com.colm.amazing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Frame extends JFrame implements ActionListener
{
	JLabel enterMessage, encryptedMessage, decryptedMessage;
	JTextField enterHere, displayEncryptHere, displayDecryptHere;
	JButton pressEncrypt;
	
	public static void main (String[] args)
	{
		new Frame("RSA Algorithm");
	}
	
	public Frame(String title)
	{
		super(title);
		setSize(635,450);
		setLocationRelativeTo(null);  //opens the window at the center of the screen
		setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(createPane());
		setVisible(true);
	}
	
	//Container
    private Container createPane() 
    {
    	Container pane = new JPanel(new BorderLayout());
    	JPanel pane2 = new JPanel(new GridLayout(0,1));
		
    	//enter message
    	enterMessage = new JLabel("Enter message to encrypt:");
		pane2.add(enterMessage);
		enterHere = new JTextField();
		pane2.add(enterHere);
		
		//button to encrypt
		pressEncrypt = new JButton("Encrypt");
		pane2.add(pressEncrypt);
		pressEncrypt.addActionListener(this);
		
		//display encrypted message
		encryptedMessage = new JLabel("Encrypted message:");
		pane2.add(encryptedMessage);
		displayEncryptHere = new JTextField();
		pane2.add(displayEncryptHere);
		
		//display decrypted message
		decryptedMessage = new JLabel("Decrypted message:");
		pane2.add(decryptedMessage);
		displayDecryptHere = new JTextField();
		pane2.add(displayDecryptHere);
		
		pane.add(pane2);
		return pane;
	}
    
    //Action Listener
  	public void actionPerformed(ActionEvent e)
  	{
  		if(e.getSource() == pressEncrypt)
  		{
  			MyRSA rsa = new MyRSA();
  			String encryptThis = enterHere.getText();
  			BigInteger theMessage = new BigInteger(encryptThis.getBytes());
  			
  			//encrypt
  	        BigInteger encrypted = rsa.encrypt(theMessage);
  	        String s1 = encrypted.toString();
  	        displayEncryptHere.setText(s1);
  	        
  	        //decrypt
  	        BigInteger decrypted = rsa.decrypt(encrypted);
  	        String s2 = new String(decrypted.toByteArray());
  	        displayDecryptHere.setText(s2);
  		}
  	}
}