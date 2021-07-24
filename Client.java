package configurationHandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {

		String sorter = null, chute = null;
		PrintStream printStream = null;
		Scanner sc1 = null;
		Scanner sc = new Scanner(System.in);

		Socket socket = new Socket("127.0.0.1", 1352);

		String filePath = "C:\\Users\\RUTUJA\\Desktop\\Configuration Files\\ClientConfigurationFile.txt";
		File file = new File(filePath);
		
		//log file
		BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\RUTUJA\\Desktop\\files\\ClientLogFile.txt",true));	
		
		//date time
		SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		
		int min = 100;
		int max = 100000;
		int range = max-min+1;
		int processId=0;
		
		try {
			sc = new Scanner(file);
			sc.useDelimiter("[,\n]");
			while (sc.hasNext()) {
				sorter = sc.next();
				chute = sc.next();
				processId = (int)(Math.random()*range)+min;
				bw.write(dtf.format(date) +"  (ID="+processId+")  Sending Request to server for ("+sorter+"), ("+chute+") \n");
		
				// sending data to server
				printStream = new PrintStream(socket.getOutputStream());
				printStream.println(sorter);
				printStream.println(chute);
				printStream.println(processId);
			
				// accepts response from the server
				sc1 = new Scanner(socket.getInputStream()); 
				String divert = sc1.next();
				String processID = sc1.next();
				System.out.println(divert);
			
				
				if(divert.equals("null")) {
					bw.write(dtf.format(date)+"  (ID="+processID+") Response Received : The combination of <"+sorter+">, <"+chute+"> is Invalid\n");
					//bw.flush();
				}else if(divert!=null){
					bw.write(dtf.format(date)+"  (ID="+processID+") Response Received : The combination of <"+sorter+">, <"+chute+"> is valid on divert "+divert+">\n");

				}
				
			}
		} catch (Exception e) {
			System.out.println("An error ocurred from client");
			e.printStackTrace();
		} finally {
			sc.close();
			sc1.close();
			printStream.close();
			bw.close();

		}

	}

}
