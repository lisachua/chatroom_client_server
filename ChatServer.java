import java.io.*;
import java.util.Vector;
import java.net.Socket;
import java.net.ServerSocket;

public class ChatServer {
	
    // vector to store active clients 
    static Vector<ClientHandler> clientList = new Vector<>(); 
    // counter for clients 
    static int numberClients = 0;

	// function main
	public static void main( String[] args ) throws Exception, IOException {
		
		// initialize clientSocket and serverSocket to null
		Socket clientSocket = null;
		ServerSocket serverSocket = null;
		
		// takes a single command-line argument as port
		int portNumber = Integer.parseInt(args[0]);	
		
		try { 
			serverSocket = new ServerSocket(portNumber);
			
			while( true ) { //server listener in a forever loop
				
				// create client socket when client accepts
				clientSocket = serverSocket.accept();
				
				// create PrintWriter and BufferedReader objects
				PrintWriter out = new PrintWriter(clientSocket.
							getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader
						(clientSocket.getInputStream()));
				
				// create a new thread for new client
				System.out.println("Creating a new thread for new client..."); 
				
		        ClientHandler clientThread = new ClientHandler(clientSocket, 
		        		"Client_" + numberClients, out, in); 

		        // add client to active clients list 
		        clientList.add(clientThread); 

		        // start the thread. 
		        clientThread.start(); 

		        // increment number of clients
		        numberClients++; 
				
		        System.out.println("New client added to chat. Active clients: " 
		        		+ numberClients); 
		        
			} // END while( true )
		} 
		// catch block
		catch ( IOException e ) {
			//clientSocket.close(); // close clientSocket
			e.printStackTrace();
		}
		
   
	} // END function main
	

} // END class ChatChatServer




class ClientHandler extends Thread {
	
		private Socket socket;
	    private String nickName; 
	    boolean isActive; 
	    
	    final PrintWriter out; 
	    final BufferedReader in; 
	   
	    // constructor 
	    public ClientHandler(Socket s, String name, PrintWriter out, 
	    		BufferedReader in) {
	        
	        this.socket = s; 
	        this.nickName = name;
	        this.isActive = true;
	        
	        this.out = out; 
	        this.in = in; 

	    } 
	  
	    @Override
	    public void run() { 
	  
	        String received = "";
	        int loopCount = 0;
	        
	        try {
	
	        	while (true)  { 
	        		
	        		if (loopCount == 0) {
	        		// welcome message to new client
	                out.println("Welcome to ChattyChatChat!");
	        		}
	        		
	                // receive command from client 
	                received = in.readLine(); 
	                  
	                // respond to different chat commands
	                
	                if(received.contains("/quit")) {  
	                	// close socket
	                    out.println("Closing this connection."); 
	                    this.socket.close();
	                    this.isActive = false;
	                    
	                    // remove client object from clientList
	        	        int clientIndex = ChatServer.clientList.
	        	        		indexOf(this);

	        	        if (clientIndex != -1) {
	        	        	ChatServer.clientList.removeElementAt
	        	        	(clientIndex);
	        	        	ChatServer.numberClients--;
	        	        	System.out.println("Client removed from list."); 
	        	        }
	        	        // final message to server
	                    System.out.println("Connection closed. " +
	                    				"Active clients: " + 
	                    				ChatServer.numberClients ); 
	                    break; 
	                } 
	                else if(received.contains("/nick ")) {
	                	setNickName(received);
	                	out.println("nickname changed to: " + nickName);
	                }   
	                else if(received.indexOf("/dm ") != -1) {	              
	                	sendDirectMessage(received);
	                }
	                else { 
	                	// if no command word found, blast msg to other clients
	                	blastToOthers(received);
	                }
	                
	                loopCount++;
	                  
	        	} // END while block
	        	
	        } // END try block
	    	catch ( IOException e ) {
				e.printStackTrace();
	        } // END catch block
	        // close client socket in finally block
	        /*
	        finally {
	        	try {
					this.socket.close(); // close client socket
				} 
	        	catch (IOException e) {
					e.printStackTrace();
				}
	        } // END finally block
	        */
	    } // END overriden run()
	    
	    
	    // setNickName method
	    synchronized void setNickName(String r) {
	    	int startPosition = r.indexOf("/nick ") + "/nick ".length();
	    	int endPosition = r.length();
	    	
	    	// if there is a space before endl, make endPosition that space
	    	if (r.indexOf(" ", startPosition +1 ) != -1 ) {
	    		endPosition = r.indexOf(" ", startPosition +1 );
	    	}
	    	
	    	// extract nickname
	    	String name = r.substring(startPosition, endPosition);
	    	
	    	// set nickname
			this.nickName = name;
			
		} // END setNickName method
	    
	    
	    // sendDirectMessage method
	    synchronized void sendDirectMessage(String r) {
	    	
	    	// extract recipient name
	    	int startPositionName = r.indexOf("/dm ") + "/dm ".length();
	    	int endPositionName = r.indexOf(" ", startPositionName + 1);
	    	String recipient = r.substring(startPositionName, endPositionName);
	    	
	    	// extract message to DM
	    	int startPositionMsg = endPositionName + 1;
	    	int endPositionMsg = r.length();
	    	String msgToDM = r.substring(startPositionMsg, endPositionMsg);
	    	
	    	//boolean recipientFound = false;
	    	
	        // search for recipient in vector storing active clients 
	        for (ClientHandler mc : ChatServer.clientList)  
	        { 
	            // if the recipient is found, write on its output stream 
	            if (mc.nickName.equals(recipient) && mc.isActive == true )  
	            { 
	                mc.out.println(this.nickName + ": " + msgToDM); 
	           
	            } 
	        } // END enhanced for loop through vector of active clients
	    
	    } // END sendDirectMessage method
	    
	
	    // blastToOthers method
	    synchronized void blastToOthers(String r) {
	    	// loop through clients on vector of active clients
	    	for (ClientHandler mc : ChatServer.clientList)  
	        { 
	            // if the client is NOT the sender, write to its output stream
	            if (mc.nickName != this.nickName && mc.isActive == true )  
	            { 
	                mc.out.println(this.nickName + ": " + r); 
	           
	            } 
	        } // END enhanced for loop through vector of active clients
	        
	    	
	    	 //for (int i=0; i<ChatServer.clientList.size(); i++) {
		       
	    } // END blastToOthers method
	    
	
	
} // END class ClientHandler