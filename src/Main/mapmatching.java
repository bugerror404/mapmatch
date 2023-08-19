package Main;

import Tool.Tool;
import map.Edge;
import map.Map;
import matching.*;
import output.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class mapmatching {

    public static void main(String[] args) throws IOException {
        Tool.log("Start...");
        long time1 = System.currentTimeMillis();
        //**************** Generate Map *****************************
        Map.setList("D:\\1Amystudy\\data\\lw.txt");
        Map map = new Map();
        String edgesOut = "D:\\1Amystudy\\data\\edges.txt";
        Output.outputEdgesTxt(edgesOut, map);
        Tool.log("Done: map generation.\n---------------------------------");
        CandidatePreparation cand = new CandidatePreparation("D:\\1Amystudy\\data\\t1.txt", map, 200, 5);
        HashMap<Time, ArrayList<Candidate>> candidates = cand.getCandidates();
//        for (Time time : candidates.keySet()) {
//            ArrayList<Candidate> list = candidates.get(time);
//            System.out.print(time+" : ");
//            for (Candidate candidate : list) {
//                System.out.print(candidate.candidateId +"; ");
//            }
//            System.out.println();
//        }
        Tool.log("Done: candidation preparation.");
        PFAnalysis sta = new PFAnalysis(map, cand, 0, 20, true, true, true);

        Tool.log("Done: Candidate Path analysis.");

        ResultMatching rm = new ResultMatching(sta, cand);
        Set<PFAnalysis.CandidatePair> pairSet = sta.getShortestPath().keySet();
        HashMap<Time, Candidate> matched = rm.getSequenceMatched();
        HashMap<String, String> parents = rm.getParents();
        ArrayList<Time> gpslist = cand.getTimes();
        ArrayList<Edge> allpath = new ArrayList<>();
        for (int i = 1; i < gpslist.size() - 1; i++) {
            Time t0 = gpslist.get(i-1);
            Time t1 = gpslist.get(i);
            String c0 = matched.get(t0).candidateId;
            String c1 = matched.get(t1).candidateId;
            PFAnalysis.CandidatePair pair = sta.new CandidatePair(c0,c1);
            ArrayList<Edge> spath = sta.getShortestPath().get(pair);
            if ((spath != null)&&(spath.size() != 0)) {
                allpath.addAll(spath);
            }
        }

        int plength = 0;

        for (Edge edge : allpath) {
            plength += edge.length;
            System.out.print(edge.edgeId + " , ");
        }
        System.out.println();
        System.out.println("path length:" + plength);
        long time2 = System.currentTimeMillis();
        Tool.log("Done: result matching. " + "run time：" + (time2 - time1) + "ms");




    }
}
