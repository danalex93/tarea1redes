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
					+"<html><head><title>Home | EmpireNetwork</title><style>div, html, body {margin: 0;padding: 0;box-sizing: border-box;font-family: Helvetica;}body {height: 100%;width: 100%;background-color: rgb(0,38,62);background-size: 100% auto;background-position: center center;background-image: url('http://syanezs.cl/assets/backstarwars.jpg');}.header{font-size: 72px;color: white;padding-top: 50px;width: 100%;text-align: center;}.header img{width: 100px;height: 100px;vertical-align: -25px;}.subheader{width: 100%;text-align: center;color: white;font-size: 23px;text-decoration: italic;}</style></head><body>	<div class = 'header'><img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' />EmpireNetwork<img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /></div><div class = 'subheader'><i>The #1 choice in ISP providers all around the galaxy<br><span style = 'font-size: 0.8em'>...And only </span></i></div></body></html>\n"
				);
			}
			else if (request.get("Request").equals("GET /about HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 200 OK\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html> <head> <title>About | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: black; background-size: 100% auto; background-position: center center; background-image: url('http://www.hdwallpapersinn.com/wp-content/uploads/2014/09/tie-fighter-pic.png'); } .header { font-size: 54px; color: white; padding-top: 50px; width: 100%; text-align: center; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; } </style> </head> <body> <div class = 'header'> <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> EmpireNetwork <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> </div> <div class = 'subheader'> <i> Providing security and peace in over 234,876 systems </i> </div> </body> </html>\n"
				);
			}
			else if (request.get("Request").equals("GET /old_home HTTP/1.1")){
				serverResponse.writeBytes(
					"HTTP/1.1 301 Moved Permanently\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html><head><title>Home | EmpireNetwork</title><style>div, html, body {margin: 0;padding: 0;box-sizing: border-box;font-family: Helvetica;}body {height: 100%;width: 100%;background-color: rgb(0,38,62);background-size: 100% auto;background-position: center center;background-image: url('http://syanezs.cl/assets/backstarwars.jpg');}.header{font-size: 72px;color: white;padding-top: 50px;width: 100%;text-align: center;}.header img{width: 100px;height: 100px;vertical-align: -25px;}.subheader{width: 100%;text-align: center;color: white;font-size: 23px;text-decoration: italic;}</style></head><body>	<div class = 'header'><img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' />EmpireNetwork<img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /></div><div class = 'subheader'><i>The #1 choice in ISP providers all around the galaxy<br><span style = 'font-size: 0.8em'>...And only </span></i></div></body></html>\n"
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
						+"<html> <head> <title>Secret | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: rgb(0,38,62); background-size: 100% auto; background-position: center center; background-image: url('http://a.dilcdn.com/bl/wp-content/uploads/sites/6/2014/07/11-cabbel-lennox.jpg'); } .header { font-size: 46px; color: white; padding-top: 50px; width: 100%; text-align: center; position: absolute; bottom: 95px; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; position: absolute; bottom: 30px; line-height: 30px; }  .subheader a { color: white; } </style> </head> <body> <div class = 'header'> <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> Welcome <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> </div> <div class = 'subheader'> <i> You are an important part of our organization<br> Lord Vader thanks you </i> </div> </body> </html>\n"
					);
				}
				else{
					serverResponse.writeBytes(
						"HTTP/1.1 403 Forbidden\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html> <head> <title>Access Denied | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: black; background-size: 100% auto; background-position: center center; background-image: url('http://p1.pichost.me/i/26/1493274.jpg'); } .header { font-size: 46px; color: white; padding-top: 50px; width: 100%; text-align: center; position: absolute; bottom: 95px; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; position: absolute; bottom: 30px; line-height: 30px; }  .subheader a { color: white; } </style> </head> <body> <div class = 'header'> <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> EmpireNetwork <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> </div> <div class = 'subheader'> <i> Access Forbidden </i> </div> </body> </html>\n"
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
						+"<html> <head> <title>Secret | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: rgb(0,38,62); background-size: 100% auto; background-position: center center; background-image: url('http://a.dilcdn.com/bl/wp-content/uploads/sites/6/2014/07/11-cabbel-lennox.jpg'); } .header { font-size: 46px; color: white; padding-top: 50px; width: 100%; text-align: center; position: absolute; bottom: 95px; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; position: absolute; bottom: 30px; line-height: 30px; }  .subheader a { color: white; } </style> </head> <body> <div class = 'header'> <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> Welcome <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> </div> <div class = 'subheader'> <i> You are an important part of our organization<br> Lord Vader thanks you </i> </div> </body> </html>\n"
					);
				}
				else{
					serverResponse.writeBytes(
						"HTTP/1.1 403 Forbidden\n"
						+"Content-Type: text/html\n"
						+"Connection: close\n"
						+"Server: DeathStar\n"
						+"\n"
						+"<html> <head> <title>Access Denied | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: black; background-size: 100% auto; background-position: center center; background-image: url('http://p1.pichost.me/i/26/1493274.jpg'); } .header { font-size: 46px; color: white; padding-top: 50px; width: 100%; text-align: center; position: absolute; bottom: 95px; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; position: absolute; bottom: 30px; line-height: 30px; }  .subheader a { color: white; } </style> </head> <body> <div class = 'header'> <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> EmpireNetwork <img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' /> </div> <div class = 'subheader'> <i> Access Forbidden </i> </div> </body> </html>\n"
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
					+"<html> <head> <title>LogIn | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: rgb(0,38,62); background-size: auto 100%; background-repeat: no-repeat; background-position: center center; background-image: url('http://bensbargains.net/thecheckout/wp-content/uploads/2013/05/Pro-empire-enlist-today-he-needs-you.jpg'); } .header { font-size: 54px; color: white; padding-top: 50px; width: 100%; text-align: center; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; }  .wrapper{ width: 500px; height: 500px; background-color: rgba(255,255,255,0.8); position: absolute; margin-left: -250px; margin-top: -250px; top: 50%; left: 50%; padding: 50px; text-align: center; }  input { width: 100%; margin: 20px auto; border: 0; height: 50px; box-sizing: border-box; padding-right: 10px; padding-left: 10px; } button img { width: 30px; height: 30px; vertical-align: middle; margin-right: 30px; }  button { background-color: rgb(0,38,62); color: white; height: 50px; border: 0; min-width: 100px; padding-right: 20px; padding-left: 20px; font-size: 22px; margin: 20px auto; }   </style> </head> <body> <div class = 'wrapper'> <h1>LogIn</h1> <form method='post' action='/secret'> <input type = 'text' name = 'user' placeholder = 'Username' /> <input type = 'password' name = 'password' placeholder = 'Password' /> <button type='submit'><img src = 'http://store.shadowvexindustries.com/image/cache/data/Stickers/Galactic_Empire-500x500.png' />Log-In</button> </form> </div> </body> </html>\n"
				);
			}
			else{
				serverResponse.writeBytes(
					"HTTP/1.1 404 Not Found\n"
					+"Content-Type: text/html\n"
					+"Connection: close\n"
					+"Server: DeathStar\n"
					+"\n"
					+"<html> <head> <title>Not Found | EmpireNetwork</title> <style> div, html, body { margin: 0; padding: 0; box-sizing: border-box; font-family: Helvetica; }  body { height: 100%; width: 100%; background-color: black; background-size: 100% auto; background-position: center center; background-image: url('http://i.ytimg.com/vi/g5VR4wdGeRg/maxresdefault.jpg'); } .header { font-size: 46px; color: rgba(0,0,0,.87); padding-top: 50px; width: 100%; text-align: center; position: absolute; bottom: 95px; }  .header img { width: 50px; height: 50px; vertical-align: middle; }  .subheader { width: 100%; text-align: center; color: white; font-size: 23px; text-decoration: italic; position: absolute; bottom: 30px; line-height: 30px; }  .subheader a { color: white; } </style> </head> <body> <div class = 'header'> These are not the droids you are looking for </div> <div class = 'subheader'> oh wait... those <b><i>WERE</i></b> the droids you are looking for!<br> <a href = 'javascript:window.history.back()'>GO BACK NOW!</a> </div> </body> </html>\n"
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