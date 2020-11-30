package br.cin.ufpe.pcd.exercicio6;

import java.io.IOException;

import br.cin.ufpe.pcd.exercicio6.Client.cMain;
import br.cin.ufpe.pcd.exercicio6.Naming.NamingService;
import br.cin.ufpe.pcd.exercicio6.Server.sMain;
import br.cin.ufpe.pcd.util.ChartSeries;
import br.cin.ufpe.pcd.util.Exporter;

public class Main {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		new Thread(new Runnable() {
	           public void run() {
	       		NamingService.main(null);           
	    }
		}).start();
		
		Thread.sleep(100);
		
		new Thread(new Runnable() {
	           public void run() {
	       		sMain.main(null);  
	    }
		}).start();

		Thread.sleep(100);
	
		cMain cMain = new cMain();
		Thread cMainThread = new Thread(cMain);
		cMainThread.start();
		
		cMainThread.join();
		
		ChartSeries avgSeries = new ChartSeries("OUR RPC", cMain.getNumClientArray(), cMain.getAvgTimeArray());
		ChartSeries stdSeries = new ChartSeries("OUR RPC", cMain.getNumClientArray(), cMain.getStdDeviationArray());
		
		Exporter.exportJson(avgSeries, "rpc_avg.json");
		Exporter.exportJson(stdSeries, "rpc_std.json");
	}
}
