<p>This project is a Java-based socket programming application designed for managing students and courses in a client-server architecture. The server runs on a single machine (PC1) and allows multiple clients (PC2, etc.) to connect simultaneously over a network.</p>
Technologies Used <br>
Programming Language: Java <br>
IDE: Eclipse <br>
Network Protocol: TCP (Transmission Control Protocol) <br>
Libraries: java.net, java.io <br>

### Setup and Configuration
![Enable ICMPv4 Example](https://github.com/0mehedihasan/Local-Student-Management-System/blob/main/icmpv4.png)
#### Server (PC1)
1. Open the project in Eclipse IDE on PC1.
2. Navigate to the `SERVER` package and locate the `Server.java` file.
3. Run the `Server.java` file:
   * Right-click on `Server.java` > Run As > Java Application.
4. The server will start listening on port 5000.

#### Client (PC2 or More)
1. Open the project in Eclipse IDE on PC2 (or any client machine).
2. Navigate to the `CLIENT` package and locate the `Client.java` file.
3. Update the server IP address in the `Client.java` file to match the IP of PC1 (replace with actual server IP):

   ```java
   Socket socket = new Socket("192.168.0.100", 3334); // Replace with actual server IP
   ```
4. Run the `Client.java` file:
   * Right-click on `Client.java` > Run As > Java Application.
