import java.util.ArrayList;

public class City {
	String[][] grid;
	Boolean hasShield;
	ArrayList<FeatureEnd> featureEnds;
	ArrayList<Location> meepleLocs;
	Location gridLoc;
	Location clickedLoc;
	
	public City(String[][] g, Boolean h, ArrayList<FeatureEnd> f, ArrayList<Location> m, Location gL)
	{
		grid=g;
		hasShield=h;
		featureEnds=f;
		meepleLocs=m;
		gridLoc=gL;
	}
	public void setGrid(String[][] g)
	{
		grid=g;
	}
	public String[][] getGrid()
	{
		return grid;
	}
	public void setHasShield(Boolean b)
	{
		hasShield=b;
	}
	public Boolean getHasShield()
	{
		return hasShield;
	}
	public void setFeatureEnds(ArrayList<FeatureEnd> f)
	{
		featureEnds=f;
	}
	public ArrayList<FeatureEnd> getFeatureEnds()
	{
		return featureEnds;
	}
	public void setMeepleLocs(ArrayList<Location> m)
	{
		meepleLocs=m;
	}
	public ArrayList<Location> getMeepleLocs()
	{
		return meepleLocs;
	}
	public void setGridLoc(Location g)
	{
		gridLoc=g;
	}
	public Location getGridLoc()
	{
		return gridLoc;
	}
	public void setCity(City c)
	{
		grid=c.getGrid();
		hasShield=c.getHasShield();
		featureEnds=c.getFeatureEnds();
		meepleLocs=c.getMeepleLocs();
	}
	public boolean isValid(City c)
	{
		int cX=c.getGridLoc().getX();
		int cY=c.getGridLoc().getY();
		int x=gridLoc.getX();
		int y=gridLoc.getY();
		ArrayList<FeatureEnd> cE=c.getFeatureEnds();
		if(cX-x==1)//passed in is below
		{
			boolean found1=false;
			for(int i=0; i<featureEnds.size(); i++)
			{
				if(featureEnds.get(i).getDirection().equals("D"))
				{
					found1=true;
					break;
				}
			}
			boolean found2=false;
			for(int i=0; i<cE.size(); i++)
			{
				if(cE.get(i).getDirection().equals("U"))
				{
					found2=true;
					break;
				}
			}
			if(found1&&found2)
				return true;
			else
				return false;
		}
		else if(x-cX==1)//passed in is above
		{
			boolean found1=false;
			for(int i=0; i<featureEnds.size(); i++)
			{
				if(featureEnds.get(i).getDirection().equals("U"))
				{
					found1=true;
					break;
				}
			}
			boolean found2=false;
			for(int i=0; i<cE.size(); i++)
			{
				if(cE.get(i).getDirection().equals("D"))
				{
					found2=true;
					break;
				}
			}
			if(found1&&found2)
				return true;
			else
				return false;
		}
		else if(cY-y==1)//passed in is to the right
		{
			boolean found1=false;
			for(int i=0; i<featureEnds.size(); i++)
			{
				if(featureEnds.get(i).getDirection().equals("R"))
				{
					found1=true;
					break;
				}
			}
			boolean found2=false;
			for(int i=0; i<cE.size(); i++)
			{
				if(cE.get(i).getDirection().equals("L"))
				{
					found2=true;
					break;
				}
			}
			if(found1&&found2)
				return true;
			else
				return false;
		}
		else if(y-cY==1)//passed in is to the left
		{
			boolean found1=false;
			for(int i=0; i<featureEnds.size(); i++)
			{
				if(featureEnds.get(i).getDirection().equals("L"))
				{
					found1=true;
					break;
				}
			}
			boolean found2=false;
			for(int i=0; i<cE.size(); i++)
			{
				if(cE.get(i).getDirection().equals("R"))
				{
					found2=true;
					break;
				}
			}
			if(found1&&found2)
				return true;
			else
				return false;
		}
		else
			return false;//passed in is not adjacent
	}
	public void setClickedLoc(int x, int y)
    {
    	clickedLoc=new Location(x, y);
    }
    public Location getClickedLoc()
    {
    	return clickedLoc;
    }
}
