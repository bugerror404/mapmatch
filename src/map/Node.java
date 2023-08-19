package map;

import java.util.Objects;

public class Node {

    //ID
    public class NodeId{
        public String id;
        public NodeId(String nid){
            this.id = nid;
        }

        @Override
        public boolean equals(Object nodeId){
            if(!(nodeId instanceof NodeId)) return false;
            if(this.id.equals(((NodeId) nodeId).id)) return true;
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
    //点对
    public class NodePair{
        public NodeId nodeIdStart;
        public NodeId nodeIdEnd;

        public NodePair(NodeId startId, NodeId endId){
            this.nodeIdStart = startId;
            this.nodeIdEnd = endId;
        }

        public NodePair(String startId, String endId){
            this.nodeIdStart = new NodeId(startId);
            this.nodeIdEnd = new NodeId(endId);
        }

        @Override
        public boolean equals(Object nodePair){
            if(!(nodePair instanceof NodePair)) return false;
            if(this.nodeIdStart.equals(((NodePair) nodePair).nodeIdStart)
                    && this.nodeIdEnd.equals(((NodePair) nodePair).nodeIdEnd)) return true;
            return false;
        }

        @Override
        public int hashCode(){
            return this.nodeIdStart.hashCode() + this.nodeIdEnd.hashCode();
        }

        @Override
        public String toString(){
            return this.nodeIdStart + "," + this.nodeIdEnd;
        }
    }

    public NodeId nodeId;
    public double lon;
    public double lat;
    public Double[] location;
    public double direction;
    public String time;

    public Node(String nid, double dlon, double dlat, double direction){
        this.setNodeId(nid);
        this.setLocation(dlon, dlat);
        this.setDirection(direction);
    }

    public Node(String nodeId, double lon, double lat) {
        this.setLocation(lon,lat);
        this.setNodeId(nodeId);
    }

    public Node(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Node() {

    }

    private void setNodeId(String nid){
        this.nodeId = new NodeId(nid);
    }

    private void setLocation(double dlon, double dlat){
        Double[] loc = {dlon, dlat};
        this.lon = dlon;
        this.lat = dlat;
        this.location = loc;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }


    @Override
    public boolean equals(Object node){
        if(!(node instanceof Node)) return false;
        if(this.nodeId.equals(((Node) node).nodeId)) return true;
        return false;
    }

    @Override
    public int hashCode(){
        return this.nodeId.hashCode();
    }

    @Override
    public String toString(){
        return this.nodeId.id + "," + this.lon + "," + this.lat;
    }
}