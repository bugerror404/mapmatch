package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map {

    public HashMap<Node.NodeId, Node> nodes;
    public HashSet<Edge> edgeSet;
    public HashMap<Node, ArrayList<Edge>> outEdges;
    public HashMap<Node, ArrayList<Edge>> inEdges;
    public static List<String[]> list;


    public Map(){
        //this.setList(mapFile);
        this.setNodes(list);
        this.setEdges(list);
        this.setOutAndInEdges();
    }

    public static void setList(String mapFile) throws IOException {
        list = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(mapFile));
        String sBuf = null;
        while((sBuf = br.readLine()) != null){
            String[] regex = sBuf.split(",");
            list.add(regex);
        }
        br.close();

    }

    private void setNodes(List<String[]> list){
        this.nodes = new HashMap<Node.NodeId, Node>();
        for (int i = 0; i < list.size(); i++) {
            String nodeid1 = list.get(i)[1];
            String nodeid2 = list.get(i)[2];
            Node.NodeId no1 = new Node().new NodeId(nodeid1);
            Node.NodeId no2 = new Node().new NodeId(nodeid2);
            String[] nodelist = list.get(i)[5].split(";");
            if (!nodes.containsKey(no1)){
                String[] firstnode = nodelist[0].split(":");
                double lon_f = Double.parseDouble(firstnode[0]);
                double lat_f = Double.parseDouble(firstnode[1]);
                Node node_f = new Node(nodeid1,lon_f,lat_f);
                this.nodes.put(node_f.nodeId,node_f);
            }
            if (!nodes.containsKey(no2)) {
                String[] lastnode = nodelist[nodelist.length-1].split(":");
                double lon_l = Double.parseDouble(lastnode[0]);
                double lat_l = Double.parseDouble(lastnode[1]);
                Node node_l = new Node(nodeid2,lon_l,lat_l);
                this.nodes.put(node_l.nodeId,node_l);
            }
        }

    }

    private void setEdges(List<String[]> list) {
        this.edgeSet = new HashSet<Edge>();
        for (int i = 0; i < list.size(); i++) {
            String linkid = list.get(i)[0];
            String fid = list.get(i)[1];
            String eid = list.get(i)[2];
            double length = Double.parseDouble(list.get(i)[3]);
            Node.NodeId nowId_f = new Node().new NodeId(fid);
            Node.NodeId nowId_e = new Node().new NodeId(eid);
            Node nodeStart = this.nodes.get(nowId_f);
            Node nodeEnd = this.nodes.get(nowId_e);

            List<Node> allNode = new ArrayList<>();
            String[] nodelist = list.get(i)[5].split(";");
            for (int j = 0; j < nodelist.length; j++) {
                String[] tmp = nodelist[j].split(":");
                double lon = Double.parseDouble(tmp[0]);
                double lat = Double.parseDouble(tmp[1]);
                Node node = new Node(lon,lat);
                allNode.add(node);
            }
            Edge edge = new Edge(linkid,nodeStart,nodeEnd,length,allNode);
            this.edgeSet.add(edge);
        }

    }

    private void setOutAndInEdges() {
        this.outEdges = new HashMap<Node, ArrayList<Edge>>();
        this.inEdges = new HashMap<Node, ArrayList<Edge>>();
        for(Edge edge: this.edgeSet){
            Node nodeStart = edge.nodeStart;
            Node nodeEnd = edge.nodeEnd;
            if(this.outEdges.containsKey(nodeStart))
                this.outEdges.get(nodeStart).add(edge);
            else{
                ArrayList<Edge> edgeList = new ArrayList<Edge>();
                edgeList.add(edge);
                this.outEdges.put(nodeStart, edgeList);
            }
            if(this.inEdges.containsKey(nodeEnd))
                this.inEdges.get(nodeEnd).add(edge);
            else{
                ArrayList<Edge> edgeList = new ArrayList<Edge>();
                edgeList.add(edge);
                this.inEdges.put(nodeEnd, edgeList);
            }
        }


    }
}
