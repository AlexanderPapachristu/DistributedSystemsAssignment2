import java.io.*;
import java.net.*;
import java.util.*;


class Client {
	
	//driver code
	public static void main(String[]args)
	{
		try (Socket socket = new Socket("localhost", 1234))
		{
			//writing to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			
			//reading from server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Scanner sc = new Scanner(System.in);
			String line = "";
			String option = "";
			while(!option.equalsIgnoreCase("exit")){
			System.out.println("Please enter an option: Math, Time, Palindrome, Binary, Hex\nOr enter exit to exit the code.");
			option = sc.nextLine();
			
			//****function to hold all the work to solve math problems*********
			if(option.equalsIgnoreCase("math")){
				System.out.println("please input the name of the file containing basic math operations");
				String fileName = sc.nextLine();
				File myFile = new File (fileName);
				long startTime = System.currentTimeMillis();
				if(myFile.exists() && !myFile.isDirectory()){
					//reading from file
					Scanner fl = new Scanner(myFile);
					
					//while (!"exit".equalsIgnoreCase(line)){
					while(fl.hasNextLine()){
						//read line from user
						
						line = fl.nextLine();
						
						// sending the user input to server
						out.println(line);
						out.flush();
						
						//displaying server reply
						System.out.println("User Input: " + line);
						System.out.println("Answer: " + in.readLine());
					}
				}
				else{
					System.err.println("Error: File not found");
				}
			long endTime = System.currentTimeMillis();
			//close scanner object
			sc.close();
			System.out.println("Code executed in: " + (endTime-startTime) + " ms");
			}
			
			//********Function to deal with returning a time*************
			if(option.equalsIgnoreCase("Time")){
				out.println("Time"); //send to server to activate function
				out.flush();
				System.out.println("Current Date and Time: " + in.readLine()); //return the date and time
			}
			
			//*****Check Palindrome**********
			if(option.equalsIgnoreCase("Palindrome")){
				System.out.println("What would you like to check for a palendrome?");
				String pal = sc.nextLine();
				option += pal;
				out.println(option); //send to server to activate function
				out.flush();
				System.out.println(in.readLine()); //return if the text is a palindrome
			}
			
			//********Convert decimal to binary
			if(option.equalsIgnoreCase("Binary")){
				System.out.println("What number would you like to convert to binary?");
				String num = sc.nextLine();
				option += num;
				out.println(option);
				out.flush();
				System.out.println(in.readLine());
			}
			
			//********Convert decimal to Hex
			if(option.equalsIgnoreCase("Hex")){
				System.out.println("What number would you like to convert to Hexidecimal?");
				String num = sc.nextLine();
				option += num;
				out.println(option);
				out.flush();
				System.out.println(in.readLine());
			}
			}
		}
		catch (IOException e ){
			e.printStackTrace();
		}
	}
}