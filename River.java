
public class River {
    //Attributes
    private FeatureEnd startLocation, endLocation;
// when creating a river set the Location of the startingLocation to where the tile is placed in the 30x30 grid and the Location of the endLocation to the location its pointing to in the grid
    public River() {
        startLocation = new FeatureEnd();
        endLocation = new FeatureEnd();
    }
    public River(FeatureEnd s, FeatureEnd e) {
        startLocation = s;
        endLocation = e;

    }
    // for the get and set methods i added some extra methods using the FeatureEnd class so we can directly get or change
//the location and direction of a feature end without separately calling FeatureEnd
//SET METHODS
    public void setStartLocation(FeatureEnd s) {startLocation = s;}
    public void setEndLocation(FeatureEnd e) {endLocation = e;}
    public void setStartDirection(String s) {startLocation.setDirection(s);}
    public void setEndDirection(String e) { endLocation.setDirection(e);}
    public void setStartLocY(int i) {startLocation.setLoc(new Location(startLocation.getLoc().getX(), i));}
    public void setStartLocX(int i) {startLocation.setLoc(new Location(i, startLocation.getLoc().getY()));}
    public int getStartLocY() {return startLocation.getLoc().getY();}
    public int getStartLocX() {return startLocation.getLoc().getX();}
    public void setEndLocY(int i) {endLocation.setLoc(new Location(endLocation.getLoc().getX(), i));}
    public void setEndLocX(int i) {endLocation.setLoc(new Location(i, endLocation.getLoc().getY()));}
    public int getEndLocY() {return endLocation.getLoc().getY();}
    public int getEndLocX() {return endLocation.getLoc().getX();}
    
    public void setStartLoc(Location l)
    {
    	if(startLocation!=null)
    		startLocation.setLoc(l);
    	else
    	{
    		startLocation=new FeatureEnd(l, null);
    	}
    }
    public void setEndLoc(Location l)
    {
    	if(endLocation!=null)
    		endLocation.setLoc(l);
    	else
    	{
    		endLocation=new FeatureEnd(l, null);
    	}
    }
    
    //GET METHODS
    public FeatureEnd getStartLocation() {return startLocation;}
    public FeatureEnd getEndLocation() {return endLocation;}
    public String getStartDirection()
    {
    	if(startLocation!=null)
    		return startLocation.getDirection();
    	return "null";
    }
    public String getEndDirection()
    {
    	if(endLocation!=null)
    		return endLocation.getDirection();
    	else
    		return "null";
    }
    public Location getStartLoc() {return startLocation.getLoc();}
    public Location getEndLoc() {return endLocation.getLoc();}
}
