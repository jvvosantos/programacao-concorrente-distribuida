package br.cin.ufpe.pcd.exercicio7;

import java.io.IOException;

import br.cin.ufpe.pcd.exercicio7.Client.cMain;
import br.cin.ufpe.pcd.exercicio7.Naming.NamingService;
import br.cin.ufpe.pcd.exercicio7.Server.sMain;
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

		ChartSeries avgSeries = new ChartSeries("OUR POOLING RPC", cMain.getNumClientArray(), cMain.getAvgTimeArray());
		ChartSeries stdSeries = new ChartSeries("OUR POOLING RPC", cMain.getNumClientArray(), cMain.getStdDeviationArray());

		Exporter.exportJson(avgSeries, "prpc_avg.json");
		Exporter.exportJson(stdSeries, "prpc_std.json");
	}


}
