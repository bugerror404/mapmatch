package matching;

import map.Edge;

public class Point {

    public Time pointId;
    public double lon;
    public double lat;
    public Double[] location;
    public Edge edge;
    public double direction;
    public double v;

    public Point(long epoch, double lon, double lat,double direction,double v){
        this.setPointId(epoch);
        this.setLocation(lon, lat);
        this.setDirection(direction);
        this.setV(v);
    }

    public Point(long epoch, double lon, double lat, Edge edge, double direction,double v){
        this.setPointId(epoch);
        this.setLocation(lon, lat);
        this.setEdge(edge);
        this.setDirection(direction);
        this.setV(v);
    }

    private void setPointId(long epoch){
        this.pointId = new Time(epoch);
    }

    private void setLocation(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
        Double[] loc = {lon, lat};
        this.location = loc;
    }

    private void setEdge(Edge edge){
        this.edge = edge;
    }

    private void setDirection(double direction){
        this.direction = direction;
    }

    private void setV(double v){
        this.v = v;
    }

    @Override
    public boolean equals(Object point){
        if(!(point instanceof Point)) return false;
        if(this.pointId.equals(((Point) point).pointId) &&
                this.lon == ((Point) point).lon && this.lat == ((Point) point).lat) return true;
        return false;
    }

    @Override
    public int hashCode(){
        return this.pointId.hashCode() + ((Double) this.lon).hashCode() + ((Double) this.lat).hashCode();
    }

    @Override
    public String toString(){
        return this.pointId + "," + this.lon + "," + this.lat;
    }
}
