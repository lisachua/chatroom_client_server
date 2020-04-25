## Multi-client Chatroom

### Overview:
This repositary contains a java implementation of a functional multi-client chatroom.

* <b>ChatClient.java</b> runs the server, which listens for clients and starts a new thread when a new client accepts. The server keeps track of the number of active clients. It prints a welcome message to new clients and responds to different chat commands such as requests to set a nickname, direct message, and quit the chatroom.

* <b>ChatClient.java</b> implements the client-side, which establishes a connection to the host and port number and creates PrintWriter and BufferedReader objects. It initializes the ReceiveHandler and SendHandler threads.

* <b>SendHandler.java</b> implements the SendHandler class, which reads in client input from the console and writes client input out to the socket.

* <b>ReceiveHandler.java</b> runs the ReceiveHandler class, which reads in client responses and writes to consolve on the server side. 


### Features:
* ' /nick x ' allows clients to set their nickname as x.
* '/dm x' allows clients to send a private message to x.
* '/quit' allows clients to quit the chatroom.

