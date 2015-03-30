import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;


public class Worker implements Runnable{
	static boolean logged = false;
	static Semaphore writeToFile = new Semaphore(1), changeLogStatus = new Semaphore(1);
	
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
	         
			String ipAddress = mTask.getRemoteSocketAddress().toString();
			
			/* Get Stream Data */
			String inputLine;
			ArrayList<String> requestString = new ArrayList<String>();
			
			while ((inputLine = clientRequest.readLine()).length() != 0){
				if (inputLine.isEmpty())
					break;
			    requestString.add(inputLine.replace("\n", ""));
			}
			/* Convert to Hash */
			Hashtable<String,String> request = new Hashtable<String,String>();
			
			for (int i=0; i<requestString.size(); i++){
				if (i == 0){
					request.put("Request", requestString.get(i));
				}
				else{
					String key = requestString.get(i).split(":")[0];
					String val = requestString.get(i).split(":")[1];
					request.put(key,val);
				}
			}
			
			if (request.get("Request").equals("GET / HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 200 OK\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><h1>Heeeere's Johnny!</h1></html>\n"
				);
			}
			else if (request.get("Request").equals("GET /about HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 200 OK\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><h1>About the Death Star</h1></html>\n"
				);
			}
			else if (request.get("Request").equals("GET /old_home HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 301 Moved Permanently\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><h1>Heeeere's Johnny!</h1></html>\n"
				);
			}
			else if (request.get("Request").equals("GET /secret HTTP/1.1")){
				if (logged){
					serverResponse.writeBytes(
						"HTTP/1.1 403 Forbidden\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html><h1>May the force be with you</h1></html>\n"
					);
				}
				else{
					serverResponse.writeBytes(
						"HTTP/1.1 403 Forbidden\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html><h1>Pass you shall not!</h1></html>\n"
					);
				}
			}
			else if (request.get("Request").equals("POST /secret HTTP/1.1")){
		        String params = "";
		        while (clientRequest.ready()){
		        	params += (char) clientRequest.read();
		        }
				String[] var = params.replace("\n", "").split("&");
				String user = var[0].split("=")[1];
				String password = var[1].split("=")[1];

				if (user.equals("root") && password.equals("laboratorio1")){
					changeLogStatus.acquire();
					logged = true;
					changeLogStatus.release();
					serverResponse.writeBytes(
						"HTTP/1.1 200 OK\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html><h1>Force with you is</h1></html>\n"
					);
				}
				else{
					serverResponse.writeBytes(
						"HTTP/1.1 403 Forbidden\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html><h1>Pass you shall not!</h1></html>\n"
					);
				}
			}
			else if (request.get("Request").equals("GET /login HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 200 Forbidden\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><h1>Login you Must!</h1>"
					+ "<form action='/secret' method='post'>"
					+ "<div>"
					+ "<label for='user'>User</label>"
					+ "<input type='text' name='user' id='user' required='required'>"
					+ "</div>"
					+ "<div>"
					+ "<label for='password'>Pass</label>"
					+ "<input type='password' name='pass' id='pass' required='required'>"
					+ "</div>"
					+ "<input type='submit' value='Login'>"
					+ "</form>"
					+ "</html>\n"
				);
			}
			else{
				serverResponse.writeBytes(
					"HTTP/1.1 404 Not Found\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><h1>These are not the droids you're looking for!</h1></html>\n"
				);
			}
			
			mTask.close();
			mTask = null;
			
			/* Write to log when connection success*/
			writeToFile.acquire();
			
			FileWriter writer = new FileWriter("log.txt", true);
			BufferedWriter out = new BufferedWriter(writer);
			out.write("\nIP: "+ipAddress+", Request: "+request.get("Request"));
			out.close();
			writer.close();
			
			writeToFile.release();
		} catch (Exception e) {
		}
		working = false;
	}
	
	public void execute(Socket task){
		mTask = task;
		this.run();
	}

}
