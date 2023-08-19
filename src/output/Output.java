package output;

import map.Edge;
import map.Map;
import matching.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Output {
	public static void outputEdgesTxt(String outFile, Map map) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		for(Edge edge: map.edgeSet){
			bw.write(edge.toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();	
	}
	
	public static void outputCandidatesTxt(String outFile, CandidatePreparation cp) throws IOException{
		ArrayList<Time> times = cp.getTimes();
		HashMap<Time, ArrayList<Candidate>> candidates = cp.getCandidates();
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		for(int index = 0; index < times.size(); index++){
			Time time = times.get(index);
			if(!candidates.containsKey(time)) continue;
			ArrayList<Candidate> candList = candidates.get(time);
			for(int i = 0; i < candList.size(); i++){
				bw.write(candList.get(i).toString());
				bw.newLine();
			}
		}
		bw.flush();
		bw.close();			
	}
	

	
	public static void outputLine(String sOut, BufferedWriter bw) throws IOException{
		bw.write(sOut);
		bw.newLine();
	}
	
}
