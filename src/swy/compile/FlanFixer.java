package swy.compile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import drafterdat.settings.SettingsFolder;
import swy.yeet.RunYasova;

public class FlanFixer {
	public static void main(String[] args) throws InterruptedException {
		RunYasova.main(args);
		/*try {
			System.out.println(prefixFolder() + "Flan-TA-Leaderboards.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + "2020-11-12-TA-Leaderboards.txt"));
			BufferedReader flan = new BufferedReader(new FileReader(prefixFolder() + "Flan-TA-Leaderboards.txt"));
			BufferedReader noflan = new BufferedReader(new FileReader(prefixFolder() + "NoFlan-TA-Leaderboards.txt"));
			Queue<String> flanList = new LinkedBlockingQueue<String>();
			Queue<String> noflanList = new LinkedBlockingQueue<String>();
			String nextLine = flan.readLine();
			while ((nextLine) != null) {
				flanList.add(nextLine);
				nextLine = flan.readLine();
			}
			nextLine = noflan.readLine();
			while ((nextLine) != null) {
				noflanList.add(nextLine);
				nextLine = noflan.readLine();
			}
			flan.close();
			noflan.close();
			
			for (int i = 0; i < 18; i++) {
				for (int c1 = 0; c1 < 21; c1++) {
					for (int c2 =c1; c2< 21; c2++) {
						Queue<String> q;
						if (c2 == 20) {
							System.out.print(" Flan: ");
							q =flanList;
						}
						else {
							System.out.print("NoFlan: ");
							q = noflanList;
						}
						System.out.println(q.peek());
						bw.write(q.poll());
						bw.newLine();
						try {
						while (q.peek().substring(0,1).equals(" ")) {
							bw.write(q.poll());
							bw.newLine();
						}
						}
						catch (NullPointerException e) {
							
						}
						bw.flush();
					}
				}
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + "2020-11-12\\";
		SettingsFolder.prepFolder(output);
		return output;
	}*/
	}
}
