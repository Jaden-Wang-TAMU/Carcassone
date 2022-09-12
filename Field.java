import java.util.ArrayList;
public class Field {
	ArrayList<FeatureEnd> featureEnds;
	ArrayList<Location> meepleLocs;
	Location gridLoc;
	ArrayList <City> Cities;	
	public Field(ArrayList<FeatureEnd> f, ArrayList<Location> m, Location gL, ArrayList<City> c)
	{
		featureEnds=f;
		meepleLocs=m;
		gridLoc=gL;
		Cities=c;
	}
	
	public void setFeatureEnds(ArrayList<FeatureEnd> f)
	{
		featureEnds = f;
	}
	public ArrayList<FeatureEnd> getFeatureEnds()
	{
		return featureEnds;
	}
	
	public void setMeepleLocs(ArrayList<Location> m)
	{
		meepleLocs = m;
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
	public void setCities(ArrayList<City> c)
	{
		Cities=c;
	}
	public ArrayList<City> getCities()
	{
		return Cities;
	}
	public void makeField(Field F)
	{
		featureEnds=F.getFeatureEnds();
		meepleLocs=F.getMeepleLocs();
		gridLoc=F.getGridLoc();
		Cities=F.getCities();
	}
	
	public boolean isValid(Field f)
    {
        Location l=f.getGridLoc();
        int fX=l.getX();
        int fY=l.getY();
        int x=gridLoc.getX();
        int y=gridLoc.getY();
        ArrayList<FeatureEnd> fD=f.getFeatureEnds();
        if(fX-x==1)//passed in is below
        {
        	//current Field needs feautreEnds starting with D
        	ArrayList<FeatureEnd> downEnds=new ArrayList<>();
        	for(int i=0; i<featureEnds.size(); i++)
        	{
        		if(featureEnds.get(i).getDirection().charAt(0)=='D')
        		{
        			downEnds.add(featureEnds.get(i));
        		}
        	}
        	//passed in field needs featureEnds starting with U
        	ArrayList<FeatureEnd> fUpEnds=new ArrayList<>();
        	for(int i=0; i<fD.size(); i++)
        	{
        		if(fD.get(i).getDirection().charAt(0)=='U')
        		{
        			fUpEnds.add(fD.get(i));
        		}
        	}
        	//verification, check all of downEnds
        	Boolean verify=false;
        	for(int i=0; i<downEnds.size(); i++)
        	{
        		FeatureEnd temp=downEnds.get(i);
        		for(int z=0; z<fUpEnds.size(); z++)
        		{
        			FeatureEnd fTemp=fUpEnds.get(z);
        			if(temp.getDirection().charAt(1)==fTemp.getDirection().charAt(1))
        			{
        				verify=true;
        			}
        		}
        		if(!verify)//found no correct passed in featureEnd
        		{
        			return false;
        		}
        		else//found a correct passed in featureEnd, restart conditions to line 85, does not matter at the end
        		{
        			verify=false;
        		}
        	}
            return true;
        }
        else if(x-fX==1)//passed in is above
        	
        {
        	ArrayList<FeatureEnd> UpEnds = new ArrayList<>();
        	for(int i=0; i<featureEnds.size(); i++)
        	{
        		if(featureEnds.get(i).getDirection().charAt(0)=='U')
        		{
        			UpEnds.add(featureEnds.get(i));
        		}
        	}
        	//passed in field needs featureEnds starting with U
        	ArrayList<FeatureEnd> fUEnds=new ArrayList<>();
        	for(int i=0; i<fD.size(); i++)
        	{
        		if(fD.get(i).getDirection().charAt(0)=='D')
        		{
        			UpEnds.add(fD.get(i));
        		}
        	}
        	//verification, check all of downEnds
        	Boolean verify=false;
        	for(int i=0; i<UpEnds.size(); i++)
        	{
        		FeatureEnd temp  = UpEnds.get(i);
        		for(int z=0; z<fUEnds.size(); z++)
        		{
        			FeatureEnd fTemp=fUEnds.get(z);
        			if(temp.getDirection().charAt(1)==fTemp.getDirection().charAt(1))
        			{
        				verify=true;
        			}
        		}
        		if(!verify)//found no correct passed in featureEnd
        		{
        			return false;
        		}
        		else//found a correct passed in featureEnd, restart conditions to line 85, does not matter at the end
        		{
        			verify=false;
        		}
        	}
            return true;
        	
        	
        	
        	
        	
        	
        	
        	
        }
           
        else if(fY-y==1)//passed in is to the right
        {
        	//current Field needs feautreEnds starting with D
        	ArrayList<FeatureEnd> rightEnds=new ArrayList<>();
        	for(int i=0; i<featureEnds.size(); i++)
        	{
        		if(featureEnds.get(i).getDirection().charAt(0)=='R')
        		{
        			rightEnds.add(featureEnds.get(i));
        		}
        	}
        	//passed in field needs featureEnds starting with U
        	ArrayList<FeatureEnd> fLeftEnds=new ArrayList<>();
        	for(int i=0; i<fD.size(); i++)
        	{
        		if(fD.get(i).getDirection().charAt(0)=='U')
        		{
        			fLeftEnds.add(fD.get(i));
        		}
        	}
        	//verification, check all of downEnds
        	Boolean verify=false;
        	for(int i=0; i<rightEnds.size(); i++)
        	{
        		FeatureEnd temp=rightEnds.get(i);
        		for(int z=0; z<fLeftEnds.size(); z++)
        		{
        			FeatureEnd fTemp=fLeftEnds.get(z);
        			if(temp.getDirection().charAt(1)==fTemp.getDirection().charAt(1))
        			{
        				verify=true;
        			}
        		}
        		if(!verify)//found no correct passed in featureEnd
        		{
        			return false;
        		}
        		else//found a correct passed in featureEnd, restart conditions to line 85, does not matter at the end
        		{
        			verify=false;
        		}
        	}
            return true;
        }
        else if(y-fY==1)//passed in is to the left
        {
        	//current Field needs feautreEnds starting with D
        	ArrayList<FeatureEnd> leftEnds=new ArrayList<>();
        	for(int i=0; i<featureEnds.size(); i++)
        	{
        		if(featureEnds.get(i).getDirection().charAt(0)=='D')
        		{
        			leftEnds.add(featureEnds.get(i));
        		}
        	}
        	//passed in field needs featureEnds starting with U
        	ArrayList<FeatureEnd> fRightEnds=new ArrayList<>();
        	for(int i=0; i<fD.size(); i++)
        	{
        		if(fD.get(i).getDirection().charAt(0)=='U')
        		{
        			fRightEnds.add(fD.get(i));
        		}
        	}
        	//verification, check all of downEnds
        	Boolean verify=false;
        	for(int i=0; i<leftEnds.size(); i++)
        	{
        		FeatureEnd temp=leftEnds.get(i);
        		for(int z=0; z<fRightEnds.size(); z++)
        		{
        			FeatureEnd fTemp=fRightEnds.get(z);
        			if(temp.getDirection().charAt(1)==fTemp.getDirection().charAt(1))
        			{
        				verify=true;
        			}
        		}
        		if(!verify)//found no correct passed in featureEnd
        		{
        			return false;
        		}
        		else//found a correct passed in featureEnd, restart conditions to line 85, does not matter at the end
        		{
        			verify=false;
        		}
        	}
            return true;
        }
        else
            return false;
    }
}
