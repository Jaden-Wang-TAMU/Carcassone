
public class FeatureEnd {
    Location loc;
    String direction;
    public FeatureEnd()
    {
        loc = null;
        direction = null;
    }
    public FeatureEnd(Location l, String d)
    {
        loc=l;
        direction=d;
    }
    public void setLoc(Location l)
    {
        loc=l;
    }
    public Location getLoc()
    {
        return loc;
    }
    public void setDirection(String d)
    {
        direction=d;
    }
    public String getDirection()
    {
        return direction;
    }
    public void setFeatureEnd(FeatureEnd f)
    {
        loc=f.getLoc();
        direction=f.getDirection();
    }
    public boolean isOppAdj(FeatureEnd f)
    {
    	Location l=f.getLoc();
        int fX=l.getX();
        int fY=l.getY();
        int x=loc.getX();
        int y=loc.getY();
        String fD=f.getDirection();
        if(fX-x==1&&y==fY&&direction.equals("R")&&fD.equals("L"))
            return true;
        else if(x-fX==1&&y==fY&&direction.equals("L")&&fD.equals("R"))
            return true;
        else if(fY-y==1&&x==fX&&direction.equals("D")&&fD.equals("U"))
            return true;
        else if(y-fY==1&&x==fX&&direction.equals("U")&&fD.equals("D"))
            return true;
        else
            return false;
    }
    
    public boolean isOpp(FeatureEnd f)
    {
        String fD=f.getDirection();
        if(direction.equals("R")&&fD.equals("L"))
            return true;
        else if(direction.equals("L")&&fD.equals("R"))
            return true;
        else if(direction.equals("D")&&fD.equals("U"))
            return true;
        else if(direction.equals("U")&&fD.equals("D"))
            return true;
        else
            return false;
    }
    
    public boolean isOppAdjField(FeatureEnd f)
    {
		char first=direction.charAt(0);
		char sec=direction.charAt(1);
		char fFirst=f.getDirection().charAt(0);
		char fSec=f.getDirection().charAt(1);
		Location l=f.getLoc();
		int fX=l.getX();
        int fY=l.getY();
        int x=loc.getX();
        int y=loc.getY();
		if(y-fY==1&&x==fX&&first=='U')
		{
			if(fFirst=='D'&&(sec==fSec))
				return true;
			else
				return false;
		}
		else if(fY-y==1&&x==fX&&first=='D')
		{
			if(fFirst=='U'&&(sec==fSec))
				return true;
			else
				return false;
		}
		else if(x-fX==1&&y==fY&&first=='L')
		{
			if(fFirst=='R'&&(sec==fSec))
				return true;
			else
				return false;
		}
		else if(fX-x==1&&y==fY&&first=='R')
		{
			if(fFirst=='L'&&(sec==fSec))
				return true;
			else
				return false;
		}
		else
			return false;
    }
    public String toString()
    {
    	String s="Dir: ";
    	if(direction!=null)
    	{
    		s+=direction+", Loc:";
    	}
    	else
    	{
    		s+="null, Loc:";
    	}
    	if(loc!=null)
    	{
    		s+=loc.toString();
    	}
    	else
    	{
    		s+="(null, null)";
    	}
    	return s;
    }
}
