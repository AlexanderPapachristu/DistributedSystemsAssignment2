import java.io.*;
import java.net.*;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

class Server {
	public static void main(String[]args)
	{
		ServerSocket server = null;
		
		try{
			//server listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			
			//run infinite loop for getting client request
			while(true){
				
				//socket to accept incoming client requests
				Socket client = server.accept();
				
				//Display a new client is connected
				System.out.println("New Client Connected" + client.getInetAddress().getHostAddress());
				
				//create new thread object
				ClientHandler clientSocket = new ClientHandler(client);
				
				//This thread will handle the client seperately
				new Thread(clientSocket).start();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally {
			if(server != null){
				try{
					server.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}

class ClientHandler implements Runnable {
	private final Socket clientSocket;
	
	//Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
	}
	
	public void run()
	{
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			//get the outstream of the client
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			//get input stream from the client
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String line;
			while ((line = in.readLine()) != null) {

				//System.out.printf("Sent from the client: %s\n", line); //writing the recieved message from client
				
				if(line.contains("+"))
					out.println(math(line));
				else if(line.contains("-"))
					out.println(math(line));
				else if(line.contains("*"))
					out.println(math(line));
				else if(line.contains("/"))
					out.println(math(line));
				else if(line.equalsIgnoreCase("Time"))
					out.println(getDateTime());
				else if(line.contains("Palindrome") || line.contains("palindrome")){
					String pal = line.replaceAll("(?i)palindrome", "");
					out.println(checkPalindrome(pal));
				}
				else if(line.contains("Binary") || line.contains("binary")){
					String decimal = line.replaceAll("(?i)binary", "");
					out.println(decToBin(decimal));
				}
				else if(line.contains("Hex") || line.contains("hex")){
					String decimal = line.replaceAll("(?i)hex", "");
					out.println(decToHex(decimal));
				}
					
				
				
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
					clientSocket.close();
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String math(String message){
		String sign = "";
		
		if(message.contains("+"))
			sign = "+";
		else if(message.contains("-"))
			sign = "-";
		else if(message.contains("*"))
			sign = "*";
		else if(message.contains("/"))
			sign = "/";
		else
			return ("Input is not a basic math operation");
		
		String [] msgParts = message.split("[+*/]|(?<=\\s)-"); //break the string into each part seperated by each operation
		
		double num1 = Double.parseDouble(msgParts[0]);
		double num2 = Double.parseDouble(msgParts[1]);
		
		double ans = 0;
		switch(sign){ //determine the sign from the user input and compute the answer
			case "+":
				ans = num1 + num2;
				break;
			case "-":
				ans = num1 - num2;
				break;
			case "*":
				ans = num1 * num2;
				break;
			case "/":
				ans = num1 / num2;
				break;
			default:
				System.out.println("Input is not readable");
		}
		String answer = Double.toString(ans); //convert answer to string to send the full answer back to the client
		return answer;
	}
	
	public static String getDateTime(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"); //format the date and time
		LocalDateTime now = LocalDateTime.now(); //get the current date and time 
		String answer = dtf.format(now); //save value to string
		return(answer);  
	}
	
	public static String checkPalindrome(String pal){
		String revString = "";
		for(int i = (pal.length() - 1); i >= 0; i--){
			revString += pal.charAt(i);
		}
		if(revString.equalsIgnoreCase(pal))
			return "The inputted text is a pelindrome";
		else
			return "The inputted string is not a palindrome";
	}
	
	public static String decToBin(String decimal){
		int dec = Integer.parseInt(decimal);
		String bin = Integer.toBinaryString(dec);
		return bin;
	}
	public static String decToHex(String decimal){
		int dec = Integer.parseInt(decimal);
		String hex = Integer.toHexString(dec);
		return hex;
	}
}