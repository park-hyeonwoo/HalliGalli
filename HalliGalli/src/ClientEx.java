import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ClientEx {
	   public static void main(String[] args) {
		Client c=new Client();
		Thread th=new Thread(c);
		c.t=th;
	   }   
}

class Client extends JFrame implements Runnable{
	Thread t;
	
    String serverIP = "localhost";
    
    String myNum = "P0";
    String UserNames[] = {"대기중..","대기중..","대기중..","대기중.."};
    
    Socket ClientSocket = null;
    
    private BufferedReader in;
    private PrintWriter out;
    
    GUI game = new GUI();
    
    public Client() {
    		
    	   setTitle("Halli Galli");
		   setSize(1000,800);
		   setResizable(false);
		   
		   JPanel home = new JPanel();                    // 홈 화면 패널

		   ImageIcon server1Image = new ImageIcon("images\\server.png");
		   Image server2Image = server1Image.getImage();
		   Image server3Image = server2Image.getScaledInstance(150,50,Image.SCALE_SMOOTH);
		   ImageIcon server4Image = new ImageIcon(server3Image);
		   JLabel server1 = new JLabel(server4Image);
		   JTextField server2 = new JTextField();
		   
		   ImageIcon name1Image = new ImageIcon("images\\nickname.png");
		   Image name2Image = name1Image.getImage();
		   Image name3Image = name2Image.getScaledInstance(150,50,Image.SCALE_SMOOTH);
		   ImageIcon name4Image = new ImageIcon(name3Image);
		   JLabel name1 = new JLabel(name4Image);
		   JTextField name2 = new JTextField();
		   
		   ImageIcon login1Image = new ImageIcon("images\\login.png");
		   Image login2Image = login1Image.getImage();
		   Image login3Image = login2Image.getScaledInstance(150,120,Image.SCALE_SMOOTH);
		   ImageIcon login4Image = new ImageIcon(login3Image);
		   JButton loginBtn = new JButton(login4Image);
		   
		   ImageIcon loginr1Image = new ImageIcon("images\\loginrollover.png");
		   Image loginr2Image = loginr1Image.getImage();
		   Image loginr3Image = loginr2Image.getScaledInstance(150,120,Image.SCALE_SMOOTH);
		   ImageIcon loginr4Image = new ImageIcon(loginr3Image);
		   
		   ImageIcon loginp1Image = new ImageIcon("images\\loginpressed.png");
		   Image loginp2Image = loginp1Image.getImage();
		   Image loginp3Image = loginp2Image.getScaledInstance(150,120,Image.SCALE_SMOOTH);
		   ImageIcon loginp4Image = new ImageIcon(loginp3Image);
		   
		   loginBtn.setBorderPainted(false);
		   loginBtn.setContentAreaFilled(false);
		   loginBtn.setFocusPainted(false);
		   loginBtn.setRolloverIcon(loginr4Image);
		   loginBtn.setPressedIcon(loginp4Image);
		   
		   ImageIcon back1Image = new ImageIcon("images\\background.png");
		   Image back2Image = back1Image.getImage();
		   Image back3Image = back2Image.getScaledInstance(1000,800,Image.SCALE_SMOOTH);
		   ImageIcon back4Image = new ImageIcon(back3Image);
		   JLabel background = new JLabel(back4Image);
		   
		   home.setLayout(null);
		   server1.setBounds(280,560,150,50);
		   server2.setBounds(440,560,150,50);
		   name1.setBounds(280,630,150,50);
		   name2.setBounds(440,630,150,50);
		   loginBtn.setBounds(600,560,150,120);
		   background.setBounds(0,0,1000,800);
		   
		   home.add(server1);
		   home.add(server2);
		   home.add(name1);
		   home.add(name2);
		   home.add(loginBtn);
		   home.add(background);
		   add(home);
    	
		   loginBtn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		          String serverIP = server2.getText();
		          
		          EnterGame(serverIP);
		          
		          t.start();
		          
		          String nickname = name2.getText();
			  	  out.println(myNum + "__ID__" + nickname);
			  	  out.flush();
		      
		          remove(home);
		          add(game);
		          nameUpdate();
		          }});
		   
		   game.flipBtn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		  	         out.println("BUTTONPRESSED");
		  	         out.flush();
		          }});
		   
		   game.answer.addKeyListener(new KeyAdapter() {
			      public void keyPressed(KeyEvent e) {
			         if (e.getKeyChar() == KeyEvent.VK_ENTER) {
		    		         	String EnteredWord = game.answer.getText();
		    		         	if(!isBlank(EnteredWord)) {
			    		         out.println("SAY__" + EnteredWord);
		    		         	}
		    		            out.flush();
			             }
			         }});
		   
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   setVisible(true);
    }
    public void setmyNum(String str)
    {
    	this.myNum=str;
    }
   
    public static boolean isBlank(String s) {
   	 int count = 0;
   	 for(int i = 0; i<s.length(); i++) {
   		 if(s.charAt(i)==32)
   			 count++;
   	 }
   	 if(count == s.length())
   		 return true;
   	 else
   		 return false;
    }
   
    public void EnterGame(String serverIP){
    	
    		try {
    			ClientSocket = new Socket(serverIP,9999);
    			in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
    			out = new PrintWriter(ClientSocket.getOutputStream());       
    	}
    
    		catch(IOException e){
    			System.out.println(e.getMessage());
    	}
    	
    }
    
    	
    public void nameUpdate() {
    		game.p0.setText(UserNames[0]);
    		game.p1.setText(UserNames[1]);
    		game.p2.setText(UserNames[2]);
    		game.p3.setText(UserNames[3]);
    		game.p0.setFont(new Font("고딕체", Font.BOLD, 24));
    		game.p1.setFont(new Font("고딕체", Font.BOLD, 24));
    		game.p2.setFont(new Font("고딕체", Font.BOLD, 24));
    		game.p3.setFont(new Font("고딕체", Font.BOLD, 24));
    		game.revalidate();
    		game.repaint();
    	}
    
    	public void run() {
        	String input_command;
        	String[] temp;
    		
    		try {			
    				while((input_command=in.readLine()) != null) {
    					    temp = input_command.split("__");
    					    if(temp[0].equals("USERNUM"))
    					    {
    					    	this.setmyNum(temp[1]);
    					    }
    						if(temp[0].equals("CONNECT")) {
    							for(int i=0;i<temp.length-1;i++) {
        							UserNames[i]= temp[i+1];
    							}
    							nameUpdate();
    						}	
    						
    						if(temp[1].equals("TURN")) {
    						   switch (temp[0]) {
    						   case "P0": 
    							   		game.remove(game.turn);
    							   		game.setLayout(null);
    						      		game.turn.setBounds(0,0,500,400);
    							   		game.add(game.turn,new Integer(2));
    							   		game.revalidate();
    							   		game.repaint();
    							   		break;
    						   
    						   case "P1": 
							   			game.remove(game.turn);
							   			game.setLayout(null);
							   			game.turn.setBounds(500,0,500,400);
							   			game.add(game.turn,new Integer(2));
							   			game.revalidate();
							   			game.repaint();
							   			break;
    						   
    						   case "P2": 
							   			game.remove(game.turn);
							   			game.setLayout(null);
							   			game.turn.setBounds(0,400,500,400);
							   			game.add(game.turn,new Integer(2));
							   			game.revalidate();
							   			game.repaint();
							   			break;
    						   
    						   case "P3": 
							   			game.remove(game.turn);
							   			game.setLayout(null);
							   			game.turn.setBounds(500,400,500,400);
							   			game.add(game.turn,new Integer(2));
							   			game.revalidate();
							   			game.repaint();
							   			break;
    						   	}
    					   }
    						if(temp[1].equals("DIE")) {
    							switch(temp[0]) {
    							
    							case "P0" :
    								   game.remove(game.p0);
    								   game.remove(game.c0);
    								   game.remove(game.p0card);
    								   game.remove(game.cardback0);
   							   		   game.setLayout(null);
   							   		   game.p0dead.setBounds(0,0,500,400);
    								   game.add(game.p0dead,new Integer(1));
   							   		   game.revalidate();
   							   		   game.repaint();
   							   		   break;
    								
    							case "P1" :
 								   	   game.remove(game.p1);
 								   	   game.remove(game.c1);
 								   	   game.remove(game.p1card);
 								   	   game.remove(game.cardback1);
							   		   game.setLayout(null);
							   		   game.p1dead.setBounds(500,0,500,400);
							   		   game.add(game.p1dead,new Integer(1));
							   		   game.revalidate();
							   		   game.repaint();
							   		   break;
							   		  
    							case "P2" :
 								       game.remove(game.p2);
 								       game.remove(game.c2);
 								       game.remove(game.p2card);
 								       game.remove(game.cardback2);
							   		   game.setLayout(null);
							   		   game.p2dead.setBounds(0,400,500,400);
							   		   game.add(game.p2dead,new Integer(1));
							   		   game.revalidate();
							   		   game.repaint();
							   		   break;
    								
    							case "P3" :
 								       game.remove(game.p3);
 								       game.remove(game.c3);
 								       game.remove(game.p3card);
 								       game.remove(game.cardback3);
							   		   game.setLayout(null);
							   		   game.p3dead.setBounds(500,400,500,400);
							   		   game.add(game.p3dead,new Integer(1));
							   		   game.revalidate();
							   		   game.repaint();
							   		   break;
    							}
    						}	
    						
    						if(temp[1].equals("OPEN")) {
    							switch(temp[0]) {
    							
    							case "P0" : 
    								switch(temp[2]) {
    								
    								case "BANANA" :
    									switch(temp[3]) {
    									
    									case "1" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\banana1-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "2" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\banana2-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "3" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\banana3-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "4" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\banana4-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "5" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\banana5-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									} break;
    								case "BERRY" :
    									switch(temp[3]) {
    									
    									case "1" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\berry1-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "2" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\berry2-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "3" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\berry3-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "4" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\berry4-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "5" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\berry5-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									} break;
    								case "LIME" :
    									switch(temp[3]) {
    									
    									case "1" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\lime1-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "2" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\lime2-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "3" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\lime3-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "4" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\lime4-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "5" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\lime5-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									} break;

    								case "GRAPE" :
    									switch(temp[3]) {
    									
    									case "1" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\grape1-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "2" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\grape2-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "3" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\grape3-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "4" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\grape4-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;
    									case "5" :
    										game.remove(game.p0card);
    										game.p0card1Image = new ImageIcon("images\\grape5-1.png");
    										game.p0card2Image = game.p0card1Image.getImage();
    										game.p0card3Image = game.p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
    										game.p0card4Image = new ImageIcon(game.p0card3Image);
    										game.p0card = new JLabel(game.p0card4Image);
    									   	game.setLayout(null);
    									   	game.p0card.setBounds(250,150,200,200);
       										game.add(game.p0card,new Integer(3));
       										game.revalidate();
       										game.repaint();
       										break;	
    									} break;	
    								} break;
    								
    				//////////////////////////////////////////////////					
    							case "P1" : 
    								switch(temp[2]) {
    								
    								case "BANANA" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\banana1-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\banana2-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "3" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\banana3-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "4" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\banana4-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\banana5-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								case "BERRY" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\berry1-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\berry2-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "3" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\berry3-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\berry4-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\berry5-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								case "LIME" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\lime1-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\lime2-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "3" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\lime3-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\lime4-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\lime5-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								case "GRAPE" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\grape1-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\grape2-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "3" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\grape3-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\grape4-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p1card);
											game.p1card1Image = new ImageIcon("images\\grape5-2.png");
											game.p1card2Image = game.p1card1Image.getImage();
											game.p1card3Image = game.p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p1card4Image = new ImageIcon(game.p1card3Image);
											game.p1card = new JLabel(game.p1card4Image);
											game.setLayout(null);
											game.p1card.setBounds(550,150,200,200);
   											game.add(game.p1card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								} break;
    							
    			//////////////////////////////////////////					
    							case "P2" :
    								switch(temp[2]) {
    								
    								case "BANANA" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\banana1-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\banana2-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "3" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\banana3-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "4" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\banana4-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\banana5-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								case "BERRY" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\berry1-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\berry2-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "3" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\berry3-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "4" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\berry4-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "5" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\berry5-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									} break;
    								case "LIME" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\lime1-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "2" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\lime2-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "3" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\lime3-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\lime4-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\lime5-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;							
    									} break;
    								case "GRAPE" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\grape1-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "2" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\grape2-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "3" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\grape3-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "4" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\grape4-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									case "5" :
											game.remove(game.p2card);
											game.p2card1Image = new ImageIcon("images\\grape5-3.png");
											game.p2card2Image = game.p2card1Image.getImage();
											game.p2card3Image = game.p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p2card4Image = new ImageIcon(game.p2card3Image);
											game.p2card = new JLabel(game.p2card4Image);
									   		game.setLayout(null);
									   		game.p2card.setBounds(250,450,200,200);
   											game.add(game.p2card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;
    									} break;			
    								} break;
    				/////////////////////////////////////////							
    								
    							case "P3" :
    								switch(temp[2]) {
    								
    								case "BANANA" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\banana1-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\banana2-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "3" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\banana3-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "4" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\banana4-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\banana5-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;			
    									} break;
    								case "BERRY" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\berry1-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\berry2-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "3" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\berry3-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "4" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\berry4-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\berry5-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									} break;
    								case "LIME" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\lime1-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\lime2-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "3" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\lime3-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\lime4-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									case "5" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\lime5-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									} break;
    								case "GRAPE" :
    									switch(temp[3]) {
    									
    									case "1" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\grape1-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "2" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\grape2-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "3" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\grape3-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "4" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\grape4-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;	
    									case "5" :
											game.remove(game.p3card);
											game.p3card1Image = new ImageIcon("images\\grape5-4.png");
											game.p3card2Image = game.p3card1Image.getImage();
											game.p3card3Image = game.p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
											game.p3card4Image = new ImageIcon(game.p3card3Image);
											game.p3card = new JLabel(game.p3card4Image);
									   		game.setLayout(null);
									   		game.p3card.setBounds(550,450,200,200);
   											game.add(game.p3card,new Integer(3));
   											game.revalidate();
   											game.repaint();
   											break;		
    									} break;
    								} break;
    							}
    						}
    						
    						if(temp[1].equals("SAY")) {
    							switch(temp[0]) {
    							case "P0" :
    								game.remove(game.bell);
    								game.answer.setText("");
    								game.p0say2.setText(temp[2]);
							   		game.setLayout(null);
    								game.p0say2.setBounds(120,0,132,88);
    								game.p0say1.setBounds(120,5,132,88);
    					            game.bellpush.setBounds(400,330,200,100);
    								game.add(game.p0say2,new Integer(3));
    								game.add(game.p0say1,new Integer(3));
    								game.add(game.bellpush,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.bellpush);
	    								game.remove(game.p0say2);
	    								game.remove(game.p0say1);
								   		game.setLayout(null);
								   		game.bell.setBounds(400,250,200,200);
								   		game.add(game.bell,new Integer(3));
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P1" :
    								game.remove(game.bell);
    								game.answer.setText("");
    								game.p1say2.setText(temp[2]);
							   		game.setLayout(null);
    								game.p1say2.setBounds(740,0,132,88);
    								game.p1say1.setBounds(740,5,132,88);
    					            game.bellpush.setBounds(400,330,200,100);
    								game.add(game.p1say2,new Integer(3));
    								game.add(game.p1say1,new Integer(3));
    								game.add(game.bellpush,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.bellpush);
	    								game.remove(game.p1say2);
	    								game.remove(game.p1say1);
								   		game.setLayout(null);
								   		game.bell.setBounds(400,250,200,200);
								   		game.add(game.bell,new Integer(3));
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P2" :	
    								game.remove(game.bell);
    								game.answer.setText("");
    								game.p2say2.setText(temp[2]);
							   		game.setLayout(null);
    								game.p2say2.setBounds(120,665,132,88);
    								game.p2say1.setBounds(120,670,132,88);
    					            game.bellpush.setBounds(400,330,200,100);
    								game.add(game.p2say2,new Integer(3));
    								game.add(game.p2say1,new Integer(3));
    								game.add(game.bellpush,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.bellpush);
	    								game.remove(game.p2say2);
	    								game.remove(game.p2say1);
								   		game.setLayout(null);
								   		game.bell.setBounds(400,250,200,200);
								   		game.add(game.bell,new Integer(3));
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P3" :
    								game.remove(game.bell);
    								game.answer.setText("");
    								game.p3say2.setText(temp[2]);
							   		game.setLayout(null);
    								game.p3say2.setBounds(740,665,132,88);
    								game.p3say1.setBounds(740,670,132,88);
    					            game.bellpush.setBounds(400,330,200,100);
    								game.add(game.p3say2,new Integer(3));
    								game.add(game.p3say1,new Integer(3));
    								game.add(game.bellpush,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.bellpush);
	    								game.remove(game.p3say2);
	    								game.remove(game.p3say1);
								   		game.setLayout(null);
								   		game.bell.setBounds(400,250,200,200);
								   		game.add(game.bell,new Integer(3));
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;		
    							}
    						}

    						if(temp[1].equals("WIN")) {
    							switch(temp[0]) {
    							
    							case "P0" :
 								       game.remove(game.bell);
 								       game.remove(game.answer);
 								       game.remove(game.paper);
 								       game.remove(game.word);
 								       game.remove(game.p0);
 								       game.remove(game.c0);
 								       game.remove(game.p0card);
 								       game.remove(game.cardback0);
							   		   game.setLayout(null);
							   		   game.p0win.setBounds(0,0,500,400);
							   		   game.add(game.p0win,new Integer(1));
							   		   game.revalidate();
							   		   game.repaint();
							   		   break;
								
							case "P1" :
								      game.remove(game.bell);
								      game.remove(game.answer);
								      game.remove(game.paper);
								      game.remove(game.word);
								   	  game.remove(game.p1);
								   	  game.remove(game.c1);
								   	  game.remove(game.p1card);
								   	  game.remove(game.cardback1);
								   	  game.setLayout(null);
							   		  game.p1win.setBounds(500,0,500,400);
							   		  game.add(game.p1win,new Integer(1));
							   		  game.revalidate();
							   		  game.repaint();
							   		  break;
							   		  
							case "P2" :
								      game.remove(game.bell);
								      game.remove(game.answer);
								      game.remove(game.paper);
								      game.remove(game.word);
								      game.remove(game.p2);
								      game.remove(game.c2);
								      game.remove(game.p2card);
								      game.remove(game.cardback2);
								   	  game.setLayout(null);
							   		  game.p2win.setBounds(0,400,500,400);
							   		  game.add(game.p2win,new Integer(1));
							   		  game.revalidate();
							   		  game.repaint();
							   		  break;
								
							case "P3" :
								      game.remove(game.bell);
								      game.remove(game.answer);
								      game.remove(game.paper);
								      game.remove(game.word);
								   	  game.remove(game.p3);
								   	  game.remove(game.c3);
								   	  game.remove(game.p3card);
								   	  game.remove(game.cardback3);
								   	  game.setLayout(null);
							   		  game.p3win.setBounds(500,400,500,400);
							   		  game.add(game.p3win,new Integer(1));
							   		  game.revalidate();
							   		  game.repaint();
							   		  break;
    							}	
    						}
    						
    						if(temp[1].equals("PENALTY")) {
    							switch(temp[0]) {
    							case "P0" :
							   		game.setLayout(null);
    					            game.penalty.setBounds(272,5,100,100);
    								game.add(game.penalty,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.penalty);
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P1" :
							   		game.setLayout(null);
    					            game.penalty.setBounds(620,5,100,100);
    								game.add(game.penalty,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.penalty);
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P2" :	
							   		game.setLayout(null);
    					            game.penalty.setBounds(272,658,100,100);
    								game.add(game.penalty,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.penalty);
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P3" :
							   		game.setLayout(null);
    					            game.penalty.setBounds(620,658,100,100);
    								game.add(game.penalty,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.penalty);
										game.revalidate();
										game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;		
    							}
    						}
    						
    						if(temp[1].equals("BUTTONON")) {
						   		game.setLayout(null);
    							game.flipBtn.setBounds(417,650,168,59);
						   		game.add(game.flipBtn,new Integer(3));
						   		game.revalidate();
						   		game.repaint();
    						}
    						
    						if(temp[1].equals("BUTTONOFF")) {
    							game.remove(game.flipBtn);
						   		game.revalidate();
						   		game.repaint();
    						}
    						
    						
    						if(temp[1].equals("GETCARDS")) {
    							game.remove(game.p0card);
    							game.remove(game.p1card);
    							game.remove(game.p2card);
    							game.remove(game.p3card);
    							switch(temp[0]) {
    							case "P0" :
							   		game.setLayout(null);
    					            game.getcards.setBounds(272,5,100,100);
    								game.add(game.getcards,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.getcards);
								   		game.revalidate();
								   		game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P1" :
							   		game.setLayout(null);
    					            game.getcards.setBounds(620,5,100,100);
    								game.add(game.getcards,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.getcards);
								   		game.revalidate();
								   		game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P2" :	
							   		game.setLayout(null);
    					            game.getcards.setBounds(272,658,100,100);
    								game.add(game.getcards,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.getcards);
								   		game.revalidate();
								   		game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;	
    							case "P3" :
							   		game.setLayout(null);
    					            game.getcards.setBounds(620,658,100,100);
    								game.add(game.getcards,new Integer(3));
									game.revalidate();
									game.repaint();
									try {
										Thread.sleep(1000);
		   								game.remove(game.getcards);
								   		game.revalidate();
								   		game.repaint();
									}
									catch(InterruptedException e) {
										System.out.println(e.getMessage());
									}
									break;		
    							}
    						}
    						
    					 if(temp[1].equals("CURRENTNUM")) {
    							game.c0.setText("남은 카드: "+temp[2]);
    							game.c1.setText("남은 카드: "+temp[3]);
    							game.c2.setText("남은 카드: "+temp[4]);
    							game.c3.setText("남은 카드: "+temp[5]);
    							
    							if(temp[2].equals("0")) {
    								game.remove(game.cardback0);
    							}
    							
    							if(temp[3].equals("0")) {
    								game.remove(game.cardback1);
    							}
    							
    							if(temp[4].equals("0")) {
    								game.remove(game.cardback2);
    							}
    							
    							if(temp[5].equals("0")) {
    								game.remove(game.cardback3);
    							}
						   		game.revalidate();
						   		game.repaint();		
    						}
    						
    						if(temp[1].equals("CURRENTWORD")) {	
    							game.word.setText(temp[2]);
						   		game.revalidate();
						   		game.repaint();	
    						}
    				}
    		}
    						catch(IOException e) {
    							System.out.println(e.getMessage());
    						}		
    				}
    			}
	
	///////////////////////////////////////////////////////////////////////////////
	
	class GUI extends JLayeredPane {
		   ImageIcon paper1Image = new ImageIcon("images\\paper.png");
		   Image paper2Image = paper1Image.getImage();
		   Image paper3Image = paper2Image.getScaledInstance(180,120,Image.SCALE_SMOOTH);
		   ImageIcon paper4Image = new ImageIcon(paper3Image);
		   JLabel paper = new JLabel(paper4Image);
		   JLabel word = new JLabel();
		   
		   ImageIcon bell1Image = new ImageIcon("images\\bell.png");
		   Image bell2Image = bell1Image.getImage();
		   Image bell3Image = bell2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
		   ImageIcon bell4Image = new ImageIcon(bell3Image);
		   JLabel bell = new JLabel(bell4Image);
		   JTextField answer = new JTextField();
		   
           ImageIcon bellpush1Image = new ImageIcon("images\\bellpush.png");
           Image bellpush2Image = bellpush1Image.getImage();
           Image bellpush3Image = bellpush2Image.getScaledInstance(200,120,Image.SCALE_SMOOTH);
           ImageIcon bellpush4Image = new ImageIcon(bellpush3Image);
           JLabel bellpush = new JLabel(bellpush4Image);
		   
   		   JLabel p0 = new JLabel();
   		   JLabel p1 = new JLabel();
   		   JLabel p2 = new JLabel();
   		   JLabel p3 = new JLabel();
		   
		   JLabel c0 = new JLabel("남은 카드: 14");
		   JLabel c1 = new JLabel("남은 카드: 14");
		   JLabel c2 = new JLabel("남은 카드: 14");
		   JLabel c3 = new JLabel("남은 카드: 14");
		   
		   ImageIcon say11Image = new ImageIcon("images\\say1.png");
		   Image say12Image = say11Image.getImage();
		   Image say13Image = say12Image.getScaledInstance(132,88,Image.SCALE_SMOOTH);
		   ImageIcon say14Image = new ImageIcon(say13Image);
		   JLabel p0say1 = new JLabel(say14Image);
		   JLabel p0say2 = new JLabel();
		   JLabel p2say1 = new JLabel(say14Image);
		   JLabel p2say2 = new JLabel();
		   
		   ImageIcon say21Image = new ImageIcon("images\\say2.png");
		   Image say22Image = say21Image.getImage();
		   Image say23Image = say22Image.getScaledInstance(132,88,Image.SCALE_SMOOTH);
		   ImageIcon say24Image = new ImageIcon(say23Image);
		   JLabel p1say1 = new JLabel(say24Image);
		   JLabel p1say2 = new JLabel();
		   JLabel p3say1 = new JLabel(say24Image);
		   JLabel p3say2 = new JLabel();
		   
		   ImageIcon penalty1Image = new ImageIcon("images\\penalty.png");
		   Image penalty2Image = penalty1Image.getImage();
		   Image penalty3Image = penalty2Image.getScaledInstance(100,100,Image.SCALE_SMOOTH);
		   ImageIcon penalty4Image = new ImageIcon(penalty3Image);
		   JLabel penalty = new JLabel(penalty4Image);
			
		   ImageIcon getcards1Image = new ImageIcon("images\\getcards.png");
		   Image getcards2Image = getcards1Image.getImage();
		   Image getcards3Image = getcards2Image.getScaledInstance(100,100,Image.SCALE_SMOOTH);
		   ImageIcon getcards4Image = new ImageIcon(getcards3Image);
		   JLabel getcards = new JLabel(getcards4Image);
		   
		   ImageIcon p0card1Image = new ImageIcon("images\\banana1-1.png");
		   Image p0card2Image = p0card1Image.getImage();
		   Image p0card3Image = p0card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
		   ImageIcon p0card4Image = new ImageIcon(p0card3Image);
		   JLabel p0card = new JLabel(p0card4Image);
		   
		   ImageIcon p1card1Image = new ImageIcon("images\\banana1-1.png");
		   Image p1card2Image = p1card1Image.getImage();
		   Image p1card3Image = p1card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
		   ImageIcon p1card4Image = new ImageIcon(p1card3Image);
		   JLabel p1card = new JLabel(p1card4Image);
		   
		   ImageIcon p2card1Image = new ImageIcon("images\\banana1-1.png");
		   Image p2card2Image = p2card1Image.getImage();
		   Image p2card3Image = p2card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
		   ImageIcon p2card4Image = new ImageIcon(p2card3Image);
		   JLabel p2card = new JLabel(p2card4Image);
		   
		   ImageIcon p3card1Image = new ImageIcon("images\\banana1-1.png");
		   Image p3card2Image = p3card1Image.getImage();
		   Image p3card3Image = p3card2Image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
		   ImageIcon p3card4Image = new ImageIcon(p3card3Image);
		   JLabel p3card = new JLabel(p3card4Image);
		   
		   ImageIcon cardback1Image = new ImageIcon("images\\cardbacks.png");
		   Image cardback2Image = cardback1Image.getImage();
		   Image cardback3Image = cardback2Image.getScaledInstance(120,180,Image.SCALE_SMOOTH);
		   ImageIcon cardback4Image = new ImageIcon(cardback3Image);
		   JLabel cardback0 = new JLabel(cardback4Image);
		   JLabel cardback1 = new JLabel(cardback4Image);
		   JLabel cardback2 = new JLabel(cardback4Image);
		   JLabel cardback3 = new JLabel(cardback4Image);
		   
		   ImageIcon flip1Image = new ImageIcon("images\\flip.png");
		   Image flip2Image = flip1Image.getImage();
		   Image flip3Image = flip2Image.getScaledInstance(168,59,Image.SCALE_SMOOTH);
		   ImageIcon flip4Image = new ImageIcon(flip3Image);
		   JButton flipBtn = new JButton(flip4Image);
		   
		   ImageIcon flipr1Image = new ImageIcon("images\\fliprollover.png");
		   Image flipr2Image = flipr1Image.getImage();
		   Image flipr3Image = flipr2Image.getScaledInstance(168,59,Image.SCALE_SMOOTH);
		   ImageIcon flipr4Image = new ImageIcon(flipr3Image);
		   
		   ImageIcon flipp1Image = new ImageIcon("images\\flippressed.png");
		   Image flipp2Image = flipp1Image.getImage();
		   Image flipp3Image = flipp2Image.getScaledInstance(168,59,Image.SCALE_SMOOTH);
		   ImageIcon flipp4Image = new ImageIcon(flipp3Image);
		   
		   ImageIcon turn1Image = new ImageIcon("images\\turn.png");
		   Image turn2Image = turn1Image.getImage();
		   Image turn3Image = turn2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon turn4Image = new ImageIcon(turn3Image);
		   JLabel turn = new JLabel(turn4Image);
		   
		   ImageIcon p0dead1Image = new ImageIcon("images\\p0dead.png");
		   Image p0dead2Image = p0dead1Image.getImage();
		   Image p0dead3Image = p0dead2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p0dead4Image = new ImageIcon(p0dead3Image);
		   JLabel p0dead = new JLabel(p0dead4Image);
		   
		   ImageIcon p1dead1Image = new ImageIcon("images\\p1dead.png");
		   Image p1dead2Image = p1dead1Image.getImage();
		   Image p1dead3Image = p1dead2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p1dead4Image = new ImageIcon(p1dead3Image);
		   JLabel p1dead = new JLabel(p1dead4Image);
		   
		   ImageIcon p2dead1Image = new ImageIcon("images\\p2dead.png");
		   Image p2dead2Image = p2dead1Image.getImage();
		   Image p2dead3Image = p2dead2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p2dead4Image = new ImageIcon(p2dead3Image);
		   JLabel p2dead = new JLabel(p2dead4Image);
		   
		   ImageIcon p3dead1Image = new ImageIcon("images\\p3dead.png");
		   Image p3dead2Image = p3dead1Image.getImage();
		   Image p3dead3Image = p3dead2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p3dead4Image = new ImageIcon(p3dead3Image);
		   JLabel p3dead = new JLabel(p3dead4Image);
		   
		   ImageIcon p0win1Image = new ImageIcon("images\\p0win.png");
		   Image p0win2Image = p0win1Image.getImage();
		   Image p0win3Image = p0win2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p0win4Image = new ImageIcon(p0win3Image);
		   JLabel p0win = new JLabel(p0win4Image);
		   
		   ImageIcon p1win1Image = new ImageIcon("images\\p1win.png");
		   Image p1win2Image = p1win1Image.getImage();
		   Image p1win3Image = p1win2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p1win4Image = new ImageIcon(p1win3Image);
		   JLabel p1win = new JLabel(p1win4Image);
		   
		   ImageIcon p2win1Image = new ImageIcon("images\\p2win.png");
		   Image p2win2Image = p2win1Image.getImage();
		   Image p2win3Image = p2win2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p2win4Image = new ImageIcon(p2win3Image);
		   JLabel p2win = new JLabel(p2win4Image);
		   
		   ImageIcon p3win1Image = new ImageIcon("images\\p3win.png");
		   Image p3win2Image = p3win1Image.getImage();
		   Image p3win3Image = p3win2Image.getScaledInstance(500,400,Image.SCALE_SMOOTH);
		   ImageIcon p3win4Image = new ImageIcon(p3win3Image);
		   JLabel p3win = new JLabel(p3win4Image);
		   
		   ImageIcon board1Image = new ImageIcon("images\\board.png");
		   Image board2Image = board1Image.getImage();
		   Image board3Image = board2Image.getScaledInstance(1000,800,Image.SCALE_SMOOTH);
		   ImageIcon board4Image = new ImageIcon(board3Image);
		   JLabel board = new JLabel(board4Image);
		   
		   public GUI() {       // 게임 화면 패널
		   word.setFont(new Font("고딕체", Font.BOLD, 30));
		   word.setHorizontalAlignment(JLabel.CENTER);
		   c0.setFont(new Font("고딕체", Font.BOLD, 20));
		   c1.setFont(new Font("고딕체", Font.BOLD, 20));
		   c2.setFont(new Font("고딕체", Font.BOLD, 20));
		   c3.setFont(new Font("고딕체", Font.BOLD, 20));
		   p0say2.setFont(new Font("고딕체", Font.BOLD, 20));
		   p0say2.setHorizontalAlignment(JLabel.CENTER);
		   p1say2.setFont(new Font("고딕체", Font.BOLD, 20));
		   p1say2.setHorizontalAlignment(JLabel.CENTER);
		   p2say2.setFont(new Font("고딕체", Font.BOLD, 20));
		   p2say2.setHorizontalAlignment(JLabel.CENTER);
		   p3say2.setFont(new Font("고딕체", Font.BOLD, 20));
		   p3say2.setHorizontalAlignment(JLabel.CENTER);
		   flipBtn.setRolloverIcon(flipr4Image);
		   flipBtn.setPressedIcon(flipp4Image);
		   flipBtn.setBorderPainted(false);
		   flipBtn.setContentAreaFilled(false);
		   flipBtn.setFocusPainted(false);
		   
		   setLayout(null);
		   word.setBounds(420,15,160,100);
		   paper.setBounds(410,-5,180,120);
		   bell.setBounds(400,250,200,200);
		   p0.setBounds(15,0,200,100);
		   p1.setBounds(880,0,200,100);
		   p2.setBounds(15,670,200,100);
		   p3.setBounds(880,670,200,100);
		   c0.setBounds(60,100,150,75);
		   c1.setBounds(820,100,150,75);
		   c2.setBounds(60,620,150,75);
		   c3.setBounds(820,620,150,75);
		   cardback0.setBounds(60,160,120,180);
		   cardback1.setBounds(820,160,120,180);
		   cardback2.setBounds(60,450,120,180);
		   cardback3.setBounds(820,450,120,180);
		   answer.setBounds(440,450,120,40);
		   board.setBounds(0,0,1000,800);
		   
		   add(word,new Integer(3));
		   add(paper,new Integer(3));
		   add(bell,new Integer(3));
   		   add(p0,new Integer(3));
   		   add(p1,new Integer(3));
   		   add(p2,new Integer(3));
   		   add(p3,new Integer(3));
		   add(c0,new Integer(3));
		   add(c1,new Integer(3));
		   add(c2,new Integer(3));
		   add(c3,new Integer(3));
		   add(cardback0,new Integer(3));
		   add(cardback1,new Integer(3));
		   add(cardback2,new Integer(3));
		   add(cardback3,new Integer(3));
		   add(answer,new Integer(3));
		   add(board,new Integer(0));
		   }
		}