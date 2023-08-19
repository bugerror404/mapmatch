package map;

import Tool.Tool;
import java.util.List;

public class Edge {

    public class EdgeId{
        public String id;
        public EdgeId(String eid){
            this.id = eid;
        }

        @Override
        public boolean equals(Object edgeId){
            if(this.id.equals(((EdgeId) edgeId).id)) return true;
            return false;
        }

        @Override
        public int hashCode(){
            return this.id.hashCode();
        }

        @Override
        public String toString(){
            return this.id;
        }
    }

    public EdgeId edgeId;	//边id
    public int v; 			//速度
    public double length;	//长度
    public Node nodeStart;  //起点节点
    public Node nodeEnd;    //终点节点
    public List<Node> allNode; //端点及中间点
    public String wayType;
    //public boolean oneWay;


    public Edge() {
    }

    public Edge(String edgeId,Node nodeStart, Node nodeEnd, double length,List<Node> allNode) {
        this.setEdgeId(edgeId);
        this.setStartEnd(nodeStart,nodeEnd);
        this.setAllNode(allNode);
        this.setLength(length);
        //this.setV(wayType);

    }

    private void setLength(double length) {
        this.length = length;
    }

    public EdgeId getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = new EdgeId(edgeId);
    }

    public void setStartEnd(Node start, Node end){
        this.nodeStart = start;
        this.nodeEnd = end;
    }

    public int getV() {
        return v;
    }

    public void setV(String wayType) {
        this.wayType = wayType;
        this.v = Speed.get(wayType);
    }

    public double getLength() {
        return length;
    }

    public void setLength(Node start, Node end) {
        this.length = Tool.getDistance(start.lat, start.lon, end.lat, end.lon);
    }

    public List<Node> getAllNode() {
        return allNode;
    }

    public void setAllNode(List<Node> allNode) {
        this.allNode = allNode;
    }

    public String getWayType() {
        return wayType;
    }

    public void setWayType(String wayType) {
        this.wayType = wayType;
    }

    @Override
    public boolean equals(Object edge){
        if(!(edge instanceof Edge)) return false;
        if(this.edgeId.equals(((Edge) edge).edgeId)) return true;
        return false;
    }

    @Override
    public int hashCode(){
        return this.edgeId.hashCode();
    }

    @Override
    public String toString(){
        return String.valueOf(this.edgeId.id) + "," + String.valueOf(this.nodeStart.nodeId.id) + "," + String.valueOf(this.nodeEnd.nodeId.id) + ","
                 + String.valueOf(this.length) ;
    }

}


