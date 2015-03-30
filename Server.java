import java.io.*;
import java.net.*;

class Server
{

	public static void main(String argv[]) throws Exception
	{
		@SuppressWarnings("resource")
		ServerSocket server_socket = new ServerSocket(8080);
		PrintWriter writer = new PrintWriter("log.txt");
		writer.close();
		Pool pool = new Pool(5);

		while(true)
		{
			Socket socket = server_socket.accept();
			if (socket != null){
				pool.newConnection(socket);
			}
	   }
   }
}