package configurationHandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Server {

	public String validate(String sorter, String chute, ArrayList<SorterDetail> fileData) {

		for (int i = 0; i < fileData.size(); i++) {
			if (sorter.equals(fileData.get(i).getSorter()) && chute.equals(fileData.get(i).getChute())) {
				return fileData.get(i).getDivert();
			}
		}
		return null;
	}

	public void readServerConfigurationFile(String file, ArrayList<SorterDetail> fileData) {
		Scanner sc;
		String s = null, c = null, d = null, f = null;
		try {

			sc = new Scanner(new File(file));
			sc.useDelimiter("[,\n]");
			while (sc.hasNext()) {
				s = sc.next();
				c = sc.next();
				d = sc.next();
				f = sc.next();
				fileData.add(new SorterDetail(s, c, d, f));
			}
		} catch (Exception e) {
			System.out.println("An error ocurred");
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner sc;
		Server serverObj = new Server();
		ArrayList<SorterDetail> fileData = new ArrayList<SorterDetail>();
		ServerSocket server = new ServerSocket(1352);
		Socket socket = server.accept();
		
		SorterDetail sorterDetail;
		String file = "C:\\Users\\RUTUJA\\Desktop\\Configuration Files\\ServerConfigurationFile.txt";

		// reading server configurtaion file
		serverObj.readServerConfigurationFile(file, fileData);

		// Handling clients request
		sc = new Scanner(socket.getInputStream());// accepts the data from client
		
		BufferedWriter bwt = new BufferedWriter(new FileWriter("C:\\Users\\RUTUJA\\Desktop\\files\\ServerLogFile.txt",true));	

		while (true) {
			String sorter;
			String chute;
			String processId;
			String message;

			while (sc.hasNext()) {
				sorter = sc.next();
				chute = sc.next();
				processId=sc.next();
				
				SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date date = new Date();
				
				System.out.println(dtf.format(date)+"  ("+processId+") Processing request for <"+sorter+">, <"+chute+">");
				bwt.write(dtf.format(date)+"  (ID="+processId+") Request received for <"+sorter+">, <"+chute+">\n");
				bwt.write(dtf.format(date)+"  (ID="+processId+") Processing request for <"+sorter+">, <"+chute+">\n");
				
				String divert = serverObj.validate(sorter, chute, fileData);
				
				if (divert==null) {
					message = "Invalid Request";
					bwt.write(dtf.format(date)+"  (ID="+processId+") Sending Response : The combination of <"+sorter+">, <"+chute+"> is Invalid\n");
					//bwt.flush();
				} else {
					message = "Chute " + chute + " is valid on sorter " + sorter + ". Mapped divert is " + divert;
					bwt.write(dtf.format(date)+"  (ID="+processId+") Sending Response : The combination of <"+sorter+">, <"+chute+"> is valid on divert <"+divert+">\n");
				
				}
				PrintStream p = new PrintStream(socket.getOutputStream());
				p.println(divert);
				p.println(processId);
				
			}
			bwt.close();
		}

	}

}
