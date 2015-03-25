import java.io.*;
import java.net.*;

class Server
{
   public static void main(String argv[]) throws Exception
   {
	   ServerSocket server_socket = new ServerSocket(8080);
	   Pool pool = new Pool(5);

	   while(true)
	   {
			Socket socket = server_socket.accept();
			if (socket != null){
				System.out.println("New connection!");
				pool.newConnection(socket);
			}
	   }
   }
}