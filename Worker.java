import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Worker implements Runnable{

	boolean working;
	Socket mTask;

	public Worker(boolean working) {
		super();
		this.working = working;
	}
	
	@Override
	public void run(){
		 try {
			BufferedReader clientRequest = new BufferedReader(new InputStreamReader(mTask.getInputStream()));
			DataOutputStream serverResponse = new DataOutputStream(mTask.getOutputStream());
	         
			System.out.println(clientRequest.readLine());
			
			serverResponse.writeBytes("HTTP/1.1 200 OK");
			serverResponse.writeBytes("<html><h1>Heeeere's Johnny!</h1></html>");
			
			mTask.close();
			mTask = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		working = false;
	}
	
	public void execute(Socket task){
		mTask = task;
		this.run();
	}

}
