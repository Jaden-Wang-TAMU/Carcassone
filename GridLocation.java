import java.awt.*;

public class GridLocation {
	Location loc;
	Location gridLoc;
	Rectangle space;
	public GridLocation()
	{
		loc=new Location();
		gridLoc=new Location();
		space=new Rectangle(-1, -1, 90, 90);//For a 31 by 31 grid.
	}
	public GridLocation(int x, int y)
	{
		loc=new Location(468+x*90, 38+y*90);
		gridLoc=new Location(x, y);
		space=new Rectangle(468+x*90, 38+y*90, 90, 90);//For a 11 by 11 grid.
	}
	public Location getLoc()
	{
		return loc;
	}
	public Location getGridLoc()
	{
		return gridLoc;
	}
	public void setLoc(Location l)
	{
		loc=l;
	}
	public void setGridLoc(Location l)
	{
		gridLoc=l;
	}
	public boolean contains(Point p)
    {
        return space.contains(p);
    }
	public String toString()
	{
		String s="Loc: ";
		if(loc!=null)
		{
			s+=loc.toString()+", gridLoc: ";
		}
		else
		{
			s+="null, gridLoc: ";
		}
		if(gridLoc!=null)
		{
			s+=gridLoc.toString();
		}
		else
		{
			s+="null";
		}
		return s;
	}
}
