package matching;

import Tool.Tool;
import map.Edge;
import map.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CandidatePreparation {
	private int searchRadius = 100;
	private int maxCandidateNo = 5;
	private ArrayList<Time> times;
	private HashMap<Time, ArrayList<Candidate>> candidates;
	private HashMap<Time, Point> points;
	private double MINLON;
	private double MAXLON;
	private double MINLAT;
	private double MAXLAT;

	private HashMap<Time, Integer> nRoadSegmentS;
	private HashMap<Time, Integer> nRoadTypeS;

	public CandidatePreparation(String gpsFile, Map map) throws IOException{
		this.setPoints(gpsFile);
		this.setCandidates(map);
	}

	public CandidatePreparation(String gpsFile, Map map, int sRadius, int mCanNo) throws IOException{
		this.setSearchRadius(sRadius);
		this.setMaxCandidateNo(mCanNo);
		this.setPoints(gpsFile);
		this.setCandidates(map);
		this.setGpsRange();
	}

	private void setSearchRadius(int sRadius){
		this.searchRadius = sRadius;
	}

	private void setMaxCandidateNo(int mCanNo){
		this.maxCandidateNo = mCanNo;
	}

	private void setCandidates(Map map) {
		this.candidates = new HashMap<Time, ArrayList<Candidate>>();
		this.nRoadSegmentS = new HashMap<Time, Integer>();
		this.nRoadTypeS = new HashMap<Time, Integer>();
		setGpsRange();

		for(int index = 0; index < this.times.size(); index++){
			Time pid = this.times.get(index);
			Point point = this.points.get(pid);
			HashMap<String, Candidate> xydCand = new HashMap<String, Candidate>();
			HashSet<Edge> edgeInCircle = new HashSet<Edge>();
			HashSet<String> edgeTypeInCircle = new HashSet<String>();

			for(Edge edge: map.edgeSet){
				if(!inRange(edge, point)) continue;
				Candidate candidate = computeCandidate(point, edge);
				if(candidate.distance > this.searchRadius) continue;
				String xyd = candidate.lon + "," + candidate.lat + "," + candidate.distance + "," + candidate.dirsub;
				xydCand = renewXydCand(xydCand, xyd, candidate, edge);
				edgeInCircle.add(edge);
				edgeTypeInCircle.add(edge.wayType);
			}
			ArrayList<Candidate> topMaxNoCand = getTopMaxNoCandidates(xydCand);
			addCandidates(topMaxNoCand, pid);
			this.nRoadSegmentS.put(pid, edgeInCircle.size());
			this.nRoadTypeS.put(pid, edgeTypeInCircle.size());
		}
	}

	public static Candidate computeCandidate(Point point, Edge edge){

		double x1_1 = edge.nodeStart.lon;
		double y1_1 = edge.nodeStart.lat;
		double x1_2 = edge.nodeEnd.lon;
		double y1_2 = edge.nodeEnd.lat;
		double x2_1 = point.lon;
		double y2_1 = point.lat;

		double k1 = 0;
		double b1 = 0;
		double k2 = 0;
		double b2 = 0;
		double x2_2 = 0;
		double y2_2 = 0;

		if((x1_2 - x1_1) == 0){
			x2_2 = x1_1;
			y2_2 = y2_1;
		}else if((y1_2 - y1_1) == 0){
			x2_2 = x2_1;
			y2_2 = y1_1;
		}else{
			k1 = (y1_2 - y1_1)/(x1_2 - x1_1);
			b1 = y1_1 - k1*x1_1;
			k2 = -1/k1;
			b2 = y2_1 - k2*x2_1;

			x2_2 = -(b2 - b1)/(k2 - k1);
			y2_2 = k2*x2_2 + b2;
		}

		double lon = 0;
		double lat = 0;
		double alpha = -1;
		Double[] maxlon_lat = {0.0, 0.0};
		Double[] minlon_lat = {0.0, 0.0};
		Double[] maxlat_lon = {0.0, 0.0};
		Double[] minlat_lon = {0.0, 0.0};
		if(x1_1 > x1_2){
			maxlon_lat[0] = x1_1;
			maxlon_lat[1] = y1_1;
		}else{
			maxlon_lat[0] = x1_2;
			maxlon_lat[1] = y1_2;
		}
		if(x1_1 < x1_2){
			minlon_lat[0] = x1_1;
			minlon_lat[1] = y1_1;
		}else{
			minlon_lat[0] = x1_2;
			minlon_lat[1] = y1_2;
		}
		if(y1_1 > y1_2){
			maxlat_lon[0] = y1_1;
			maxlat_lon[1] = x1_1;
		}else{
			maxlat_lon[0] = y1_2;
			maxlat_lon[1] = x1_2;
		}
		if(y1_1 < y1_2){
			minlat_lon[0] = y1_1;
			minlat_lon[1] = x1_1;
		}else{
			minlat_lon[0] = y1_2;
			minlat_lon[1] = x1_2;
		}
		if(x1_2 - x1_1 == 0){
			lon = x1_2;
			if(y2_2 >= maxlat_lon[0]){

				lat = maxlat_lon[0];
				alpha = (y1_1 == maxlat_lon[0])? 1:0;
			}else if(y2_2 <= minlat_lon[0]){

				lat = minlat_lon[0];
				alpha = (y1_1 == minlat_lon[0])? 1:0;
			}else{

				lat = y2_2;
				if(y1_2 - y1_1 != 0) alpha = (y1_2 - lat)/(y1_2 - y1_1);
				else return new Candidate(0l, 0.0, 0.0, Double.MAX_VALUE,0, null);
			}
		}else if(x2_2 > maxlon_lat[0]){

			lon = maxlon_lat[0];
			lat = maxlon_lat[1];
			alpha = (x1_1 == maxlon_lat[0])? 1:0;
		}else if(x2_2 < minlon_lat[0]){

			lon = minlon_lat[0];
			lat = minlon_lat[1];
			alpha = (x1_1 == minlon_lat[0])? 1:0;
		}
		else{
			lon = x2_2;
			lat = y2_2;
			alpha = (x1_2 - lon)/(x1_2 - x1_1);
		}
		double distance = Tool.getDistance(point.lat, point.lon, lat, lon);
		double d = Tool.getDirection(edge.nodeStart.location,edge.nodeEnd.location);
		double dirsub = Math.abs(d - point.direction);
		HashMap<Edge, Double> edgeAlpha = new HashMap<Edge, Double>();
		edgeAlpha.put(edge, alpha);
		return new Candidate(point.pointId.epoch, lon, lat, distance, dirsub, edgeAlpha);
	}

	private boolean inRange(Edge edge, Point point){
		if(edge.nodeStart.lon < (point.lon - 0.005) && edge.nodeEnd.lon < (point.lon - 0.005)) return false;
		if(edge.nodeStart.lon > (point.lon + 0.005) && edge.nodeEnd.lon > (point.lon + 0.005)) return false;
		if(edge.nodeStart.lat < (point.lat - 0.005) && edge.nodeEnd.lat < (point.lat - 0.005)) return false;
		if(edge.nodeStart.lat > (point.lat + 0.005) && edge.nodeEnd.lat > (point.lat + 0.005)) return false;
		return true;
	}

	private HashMap<String, Candidate> renewXydCand(HashMap<String, Candidate> xydCand, String xyd, Candidate newCand, Edge edge){
		if(!xydCand.containsKey(xyd)) xydCand.put(xyd, newCand);
		else{
			Candidate oldCand = xydCand.get(xyd);
			oldCand.edgeAlpha.put(edge, newCand.edgeAlpha.get(edge));
		}
		return xydCand;
	}

	private ArrayList<Candidate> getTopMaxNoCandidates(HashMap<String, Candidate> xydCand){
		ArrayList<Candidate> topMaxNoCand = new ArrayList<Candidate>();
		ArrayList<Double> dList = new ArrayList<Double>();
		HashSet<Double> dSet = new HashSet<Double>();
		for(String xyd: xydCand.keySet())
			dSet.add(Double.parseDouble(xyd.split(",")[2]));
		for(Iterator<Double> it = dSet.iterator(); it.hasNext();)
			dList.add(it.next());
		Collections.sort(dList);
		int indexCand = 0;
		for(int index = 0; index < dList.size(); index++){
			String distance = Double.toString(dList.get(index));
			for(String xyd: xydCand.keySet()){
				if((xyd.split(",")[2]).equals(distance)){
					indexCand++;
					Candidate candidate = xydCand.get(xyd);
					candidate.setIndex(indexCand);
					topMaxNoCand.add(candidate);
				}
				if(topMaxNoCand.size() >= this.maxCandidateNo) break;
			}
			if(topMaxNoCand.size() >= this.maxCandidateNo) break;
		}
		return topMaxNoCand;
	}

	private void addCandidates(ArrayList<Candidate> candList, Time time){
		this.candidates.put(time, candList);
	}
	private void setPoints(String gpsFile) throws IOException{
		this.points = new HashMap<Time, Point>();
		this.times = new ArrayList<Time>();
		BufferedReader br = new BufferedReader(new FileReader(gpsFile));
		String sBuf = null;
		while((sBuf = br.readLine()) != null){
			int carno = Integer.parseInt(sBuf.split(",")[0]);
			long epoch = Long.parseLong(sBuf.split(",")[1]);
			double lon = Double.parseDouble(sBuf.split(",")[2]);
			double lat = Double.parseDouble(sBuf.split(",")[3]);
			double v = Double.parseDouble(sBuf.split(",")[4]);
			double d = Double.parseDouble(sBuf.split(",")[5]);
			this.addTimeStamp(epoch);//所有GPS点ID存储
			Point point = new Point(epoch, lon, lat, null, d,v);
			points.put(point.pointId, point);
		}
		br.close();
	}

	private void setGpsRange(){
		this.MINLON = Double.MAX_VALUE;
		this.MINLAT = Double.MAX_VALUE;
		this.MAXLON = Double.MIN_VALUE;
		this.MAXLAT = Double.MIN_VALUE;
		for(Time pid: this.points.keySet()){
			double lon = this.points.get(pid).lon;
			double lat = this.points.get(pid).lat;
			this.MINLON = (this.MINLON <= lon)? this.MINLON:lon;
			this.MINLAT = (this.MINLAT <= lat)? this.MINLAT:lat;
			this.MAXLON = (this.MAXLON >= lon)? this.MAXLON:lon;
			this.MAXLAT = (this.MAXLAT >= lat)? this.MAXLAT:lat;
		}
		this.MINLON -= 0.005;
		this.MINLAT -= 0.005;
		this.MAXLON += 0.005;
		this.MAXLAT += 0.005;	//500m
	}

	private void addTimeStamp(long epoch){
		if(this.times.size() > 0 && epoch <= this.times.get(this.times.size() - 1).epoch)
			return;
		Time time = new Time(epoch);
		this.times.add(time);
	}

	public HashMap<Time, ArrayList<Candidate>> getCandidates(){
		return this.candidates;
	}

	public HashMap<Time, Point> getPoints(){
		return this.points;
	}

	public ArrayList<Time> getTimes(){
		return this.times;
	}

	public double getMAXLON(){
		return this.MAXLON;
	}

	public double getMINLON(){
		return this.MINLON;
	}

	public double getMAXLAT(){
		return this.MAXLAT;
	}

	public double getMINLAT(){
		return this.MINLAT;
	}

	public HashMap<Time, Integer> getNRoadSegmentS(){
		return this.nRoadSegmentS;
	}

	public HashMap<Time, Integer> getNRoadTypeS(){
		return this.nRoadTypeS;
	}

}
