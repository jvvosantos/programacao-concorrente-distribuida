package br.cin.ufpe.pcd.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Exporter {
	
	private static final String PATH = "/home/joao/Documents/ufpe/concorrente/programacao-concorrente-distribuida/data/";
	
	public static void exportJson(ChartSeries chartSeries, String fileName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(PATH+fileName), chartSeries);
	}
	
	public static ChartSeries readJson(String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new File(PATH+fileName), ChartSeries.class);
		
	}

}
