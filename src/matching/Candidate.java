package matching;

import map.Edge;

import java.util.HashMap;

public class Candidate {

	private int index = 1;
	public Time time; //候选点所属gps id
	public String candidateId;	//候选点id=gps点id+index序号
	public double lon;
	public double lat;
	public Double[] location;
	public double distance;//距离
	public double dirsub; //方向
	public HashMap<Edge, Double> edgeAlpha;	//候选点属于的边，该点到边end的距离
	//distance(candidate,edge.end) = alpha * edge.length

	public Candidate(){

	}

	public Candidate(long epoch, double lon, double lat, double distance, double dirsub, HashMap<Edge, Double> edgeAlpha){
		this.setTime(epoch);
		this.setCandidateId();
		this.setLocation(lon, lat);
		this.setDistance(distance);
		this.setDirsub(dirsub);
		this.setEdgeAlpha(edgeAlpha);
	}

	public void setIndex(int index){
		this.index = index;
		this.candidateId = this.time.epoch + "" + this.index;
	}

	private void setTime(long epoch){
		this.time = new Time(epoch);
	}

	private void setCandidateId(){
		this.candidateId = this.time.epoch + "" + this.index;
	}

	private void setLocation(double lon, double lat){
		this.lon = lon;
		this.lat = lat;
		Double[] loc = {lon, lat};
		this.location = loc;
	}

	private void setDistance(double distance){
		this.distance = distance;
	}
	private void setDirsub(double dirsub){
		this.dirsub = dirsub;
	}
	private void setEdgeAlpha(HashMap<Edge, Double> edgeAlpha){
		this.edgeAlpha = edgeAlpha;
	}

	@Override
	public boolean equals(Object candidate){
		if(!(candidate instanceof Candidate)) return false;
		if(this.candidateId.equals(((Candidate) candidate).candidateId)
				&& this.lon == ((Candidate) candidate).lon
				&& this.lat == ((Candidate) candidate).lat
				&& this.distance == ((Candidate) candidate).distance) return true;
		return false;
	}

	@Override
	public int hashCode(){
		return this.candidateId.hashCode() + ((Double) this.lon).hashCode()
				+ ((Double) this.lat).hashCode() + ((Double) this.distance).hashCode();
	}

	@Override
	public String toString(){
		String ea = "";
		for(Edge edge: edgeAlpha.keySet()){
			ea += "," + edge.edgeId + "," + edgeAlpha.get(edge);
		}
		return this.candidateId + "," + this.lon + "," + this.lat + "," + this.distance + ea;
	}
}