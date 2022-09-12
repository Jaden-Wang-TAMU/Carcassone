import java.awt.*;

public class Location
{
    Point p;

    public Location() { p = new Point(0,0); }
    public Location(int x, int y) { p= new Point(x,y); }

    public void setLoc(int x, int y){ p.setLocation(x,y); }
    
    public void setX(int x){ p.setLocation(x, p.getY()); }
    public void setY(int y){ p.setLocation(p.getX(), y); }

    public int getX(){ return (int)p.getX(); }
    public int getY(){ return (int)p.getY(); }

    public String toString() {return "("+p.getX()+", "+p.getY()+")";};
    
    public boolean equals(Location l)
    {
    	if(l.getX()==p.getX()&&l.getY()==p.getY())
    		return true;
    	else
    		return false;
    }
}
