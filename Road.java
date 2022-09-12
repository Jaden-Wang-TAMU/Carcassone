import java.util.ArrayList;

public class Road {
    private Boolean isEnd;
    private ArrayList<FeatureEnd> featureEnds;
    private ArrayList<Location> meepleLocs;
    private Location gridLoc;
    private Location clickedLoc;
    public Road(){
        isEnd = false;
        featureEnds = new ArrayList<>();
        meepleLocs = new ArrayList<>();
        gridLoc=null;
    }

    public Road(Boolean i, ArrayList<FeatureEnd> f, ArrayList<Location> l, Location gL){
        isEnd = i;
        featureEnds = f;
        meepleLocs = l;
        gridLoc=gL;
    }

    public Boolean getIsEnd(){ return isEnd; }
    public void setIsEnd(Boolean i){ isEnd = i; }

    public ArrayList<FeatureEnd> getFeatureEnds(){ return featureEnds; }
    public void setFeatureEnds(ArrayList<FeatureEnd> f){ featureEnds = f; }

    public ArrayList<Location> getMeepleLocs(){ return meepleLocs; }
    public void setMeepleLocs(ArrayList<Location> l){ meepleLocs = l; }

    public Location getGridLoc(){ return gridLoc; }
    public void setGridLoc(Location gL){ gridLoc=gL; }

    public boolean isValid(Road r){
        int newX = r.getGridLoc().getX();
        int newY = r.getGridLoc().getY();
        int x = gridLoc.getX();
        int y = gridLoc.getY();
        boolean found1 = false, found2 = false;
        ArrayList<FeatureEnd> temp = r.getFeatureEnds();

        if(Math.abs(newX - x) > 1 && Math.abs(newY - y) > 1)
            return false;

        //left
        if(y - newY == 1){
            found1 = setFound(featureEnds, "L");
            found2 = setFound(temp, "R");
        }
        //right
        else if(newY - y == 1){
            found1 = setFound(featureEnds, "R");
            found2 = setFound(temp, "L");
        }
        //above
        else if(x - newX == 1){
            found1 = setFound(featureEnds, "U");
            found2 = setFound(temp, "D");
        }
        //below
        else if(newX - x == 1){
            found1 = setFound(featureEnds, "D");
            found2 = setFound(temp, "U");
        }
        return found1 && found2;
    }
    private boolean setFound(ArrayList<FeatureEnd> featureEnds, String str){
        for(FeatureEnd f: featureEnds){
            if(f.getDirection().equals(str))
                return true;
        }
        return false;
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
