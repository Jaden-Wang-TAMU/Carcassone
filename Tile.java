/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adamc
 */
import java.awt.image.*;
import java.awt.*;
import java.util.*;

public class Tile {
    private BufferedImage front, meepleColor;
    private River river;
    private Location loc = new Location(), proposedLoc = new Location(), topLeft = new Location();
    private GridLocation gridLoc;
    private ArrayList<Road> roads;
    private ArrayList<City> cities;
    private ArrayList<Field> fields;
    private boolean hasMonastery;
    private int numRotation = 0;
    private String[][] placeGrid, meepleGrid;
    private ArrayList<FeatureEnd> roadEnds;
    private ArrayList<FeatureEnd> cityEnds;
    private ArrayList<FeatureEnd> fieldEnds;
    private Rectangle meepRect;
    private Rectangle[][] meepRectGrid;
    private Boolean[][] claimed = new Boolean[3][3];
    private Boolean turned;
    
    public Tile(){
        front = null;
        meepleColor = null;
        river = new River();
        gridLoc = new GridLocation();
        roads = new ArrayList<>();
        cities = new ArrayList<>();
        hasMonastery = false;
        placeGrid = new String[3][3];
        meepleGrid = new String[3][3];
        roadEnds=new ArrayList<>();
        cityEnds=new ArrayList<>();
        fieldEnds=new ArrayList<>();
        meepRect=new Rectangle(149, 741, 157, 157);
        meepRectGrid=new Rectangle[3][3];
        for(int i=0; i<3; i++)
        {
        	for(int y=0; y<3; y++)
        	{
        		meepRectGrid[i][y]=new Rectangle(159+i*52, 741+y*52, 52, 52);
        	}
        }
        for(int r = 0; r < claimed.length; r++){
            for(int c = 0; c < claimed[r].length; c++){
                claimed[r][c] = false;
            }
        }
        turned=false;
    }
    
    public Tile(BufferedImage f){
        front = f;
        meepleColor = null;
        river = new River();
        gridLoc = new GridLocation();
        roads = new ArrayList<>();
        cities = new ArrayList<>();
        hasMonastery = false;
        placeGrid = new String[3][3];
        meepleGrid = new String[3][3];
        roadEnds=new ArrayList<>();
        cityEnds=new ArrayList<>();
        fieldEnds=new ArrayList<>();
        meepRect=new Rectangle(149, 749, 157, 157);
        meepRectGrid=new Rectangle[3][3];
        for(int i=0; i<3; i++)
        {
        	for(int y=0; y<3; y++)
        	{
        		meepRectGrid[i][y]=new Rectangle(159+i*52, 741+y*52, 52, 52);
        	}
        }
        for(int r = 0; r < claimed.length; r++){
            for(int c = 0; c < claimed[r].length; c++){
                claimed[r][c] = false;
            }
        }
        turned=false;
    }
    public Tile(BufferedImage f, River rv, ArrayList<Road> r, ArrayList<City> c, ArrayList<Field> fi, Boolean m, GridLocation gr, String[][] pg, String[][] mg){
        front = f;
        river = rv;
        gridLoc = gr;
        roads = r;
        cities = c;
        fields = fi;
        placeGrid = pg;
        meepleGrid = mg;
        hasMonastery = m;
        roadEnds=new ArrayList<>();
        cityEnds=new ArrayList<>();
        fieldEnds=new ArrayList<>();
        setRoadEnds();
        setCityEnds();
        setFieldEnds();
        meepRect=new Rectangle(159, 741, 157, 157);
        meepRectGrid=new Rectangle[3][3];
        for(int i=0; i<3; i++)
        {
        	for(int y=0; y<3; y++)
        	{
        		meepRectGrid[i][y]=new Rectangle(159+i*52, 741+y*52, 52, 52);
        	}
        }
        for(int x = 0; x < claimed.length; x++){
            for(int y = 0; y < claimed[x].length; y++){
                claimed[x][y] = false;
            }
        }
        turned=false;
    }

    public void setRiver(River r){ river = r;}
    public River getRiver(){ return river; }

    public void setLocation(Location l, Location pl, Location tl) {
        loc = l;
        proposedLoc = pl;
        topLeft = tl;
    }
    
    public void setClaimedToTrue(int x, int y)
    {
    	claimed[x][y]=true;
    }
    
    public Location getLocation(){ return loc; }
    public Location getProposedLocation(){ return proposedLoc; }
    public Location getTopLeftLocation(){ return topLeft; }
    
    public void setLocation(Location l){ loc=l; }
    public void setProposedLocation(Location l){ proposedLoc=l; }
    public void setTopLeftLocation(Location l){ topLeft=l; }

    public void setFront(BufferedImage br){ front = br;}
    public BufferedImage getFront(){ return front; }

    public void setGridLocation(GridLocation gr){ gridLoc = gr; }
    public GridLocation getGridLocation(){ return gridLoc; }
    public void setGridLocationX(int x) {gridLoc.setGridLoc(new Location(x, gridLoc.getGridLoc().getY()));}
    public void setGridLocationY(int y) {gridLoc.setGridLoc(new Location(gridLoc.getGridLoc().getX(), y));}
    
    public void setRoads(ArrayList<Road> r){ roads = r;}
    public ArrayList<Road> getRoads(){ return roads; }

    public void setCities(ArrayList<City> c){ cities = c; }
    public ArrayList<City> getCities(){ return cities; }

    public void setFields(ArrayList<Field> fi){ fields = fi; }
    public ArrayList<Field> getFields(){ return fields; }

    public void setHasMonastery(Boolean m){ hasMonastery = m; }
    public Boolean getHasMonastery() { return hasMonastery; }

    public void setPlaceGrid(String[][] list){ placeGrid = list; }
    public String[][] getPlaceGrid(){ return placeGrid; }

    public void setMeepleGrid(String[][] list){ meepleGrid = list; }
    public String[][] getMeepleGrid(){ return meepleGrid; }

    public void setTurned(boolean b){ turned=b;}
    public boolean getTurned(){ return turned; }
    
    public int getNumRotation(){ return numRotation; }
    public void incNumRotation(){ 
        numRotation++;
        if(numRotation == 4)
            numRotation = 0;
    }
    
    public void setRoadEnds()
    {
    	if(roads!=null)
    	{
	    	ArrayList<FeatureEnd> ends=new ArrayList<>();
	    	for(int i=0; i<roads.size(); i++)
	    	{
	    		ends.addAll(roads.get(i).getFeatureEnds());
	    	}
	    	roadEnds=ends;
    	}
    }
    public ArrayList<FeatureEnd> getRoadEnds(){ return roadEnds; }
    
    public void setCityEnds()
    {
    	if(cities!=null)
    	{
	    	ArrayList<FeatureEnd> ends=new ArrayList<>();
	    	for(int i=0; i<cities.size(); i++)
	    	{
	    		ends.addAll(cities.get(i).getFeatureEnds());
	    	}
	    	cityEnds=ends;
    	}
    }
    public ArrayList<FeatureEnd> getCityEnds(){ return cityEnds; }
    
    public void setFieldEnds()
    {
    	if(fields!=null)
    	{
	    	ArrayList<FeatureEnd> ends=new ArrayList<>();
	    	for(int i=0; i<fields.size(); i++)
	    	{
	    		ends.addAll(fields.get(i).getFeatureEnds());
	    	}
	    	fieldEnds=ends;
    	}
    }
    public ArrayList<FeatureEnd> getFieldsEnds(){ return fieldEnds; }
    
    public Boolean contains(String feature){
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                if(placeGrid[r][c].equals(feature))
                    return true;
            }
        }
        return false;
    }
    public Boolean contains(String feature, int x, int y){
        if(placeGrid[x][y].equals(feature))
            return true;
        return false;
    }
    public String toString()
    {
    	String s="River: ";
    	if(river==null)
    	{
    		s+="null\n";
    	}
    	else
    	{
            if(river.getStartDirection()!=null)
    		s+=" start Direction: "+river.getStartDirection();
            else
    		s+="start Direction: null";
            if(river.getEndDirection()!=null)
    		s+=", end Direction: "+river.getEndDirection()+"\n";
            else
    		s+=", end Direction: null\n";
        }
    	if(loc==null)
    	{
    		s+="Loc: null\n";
    	}
    	else
    	{
    		s+="Loc: "+loc.toString()+"\n";
    	}
        if(gridLoc==null)
        {
        	s+="GridLoc: null\n";
        }
        else
        {
        	s+="GridLoc: "+gridLoc.toString()+"\n";
        }
        if(roads==null)
        {
        	s+="Roads: null";
        }
        else
        {
        	s+="Roads: ";
        	for(int i=0; i<roads.size(); i++)
        	{
        		s+=roads.size()+":";
        		for(int y=0; y<roads.get(i).getFeatureEnds().size(); y++)
        		{
        			s+=roads.get(i).getFeatureEnds().get(y)+", ";
        		}
        	}
        }
        s+="\n";
        if(cities==null)
        {
        	s+="Cities: null";
        }
        else
        {
        	s+="Cities ";
        	for(int i=0; i<cities.size(); i++)
        	{
        		s+=cities.size()+":";
        		for(int y=0; y<cities.get(i).getFeatureEnds().size(); y++)
        		{
        			s+=cities.get(i).getFeatureEnds().get(y)+", ";
        		}
        	}
        }
        s+="\n";
        if(fields==null)
        {
        	s+="Fields: null";
        }
        else
        {
        	s+="Fields:\n";
        	for(int i=0; i<fields.size(); i++)
        	{
        		s+=fields.size()+":";
        		for(int y=0; y<fields.get(i).getFeatureEnds().size(); y++)
        		{
        			s+=fields.get(i).getFeatureEnds().get(y)+", ";
        		}

            	s+="\n";
        	}
        }
        s+="hasMonastery: "+hasMonastery+"\n";
        s+="numRotations: "+numRotation+"\n";
        s+="placeGrid:\n";
        for(int i=0; i<placeGrid.length; i++)
        {
        	for(int y=0; y<placeGrid[i].length; y++)
        	{
        		s+=placeGrid[i][y]+", ";
        	}
        	s+="\n";
        }
        s+="meepleGrid:\n";
        for(int i=0; i<meepleGrid.length; i++)
        {
        	for(int y=0; y<meepleGrid[i].length; y++)
        	{
        		s+=meepleGrid[i][y]+", ";
        	}
        	s+="\n";
        }
        return s;
    }
    public void updateAllLocations(int height, int width)
    {
        loc = new Location(height+proposedLoc.getY(), width+proposedLoc.getX());
        topLeft = proposedLoc;
        gridLoc=new GridLocation(proposedLoc.getX(), proposedLoc.getY());
        if(river!=null)
        {
        	river.setStartLoc(proposedLoc);
        	river.setEndLoc(proposedLoc);
        }
        if(roads!=null)
        {
        	for(int i=0; i<roads.size(); i++)
        	{
        		Road r=roads.get(i);
        		for(int y=0; y<r.getFeatureEnds().size(); y++)
        		{
        			r.getFeatureEnds().get(y).setLoc(proposedLoc);
        		}
        		r.setGridLoc(proposedLoc);
        	}
        }
        if(cities!=null)
        {
        	for(int i=0; i<cities.size(); i++)
        	{
        		City c=cities.get(i);
        		for(int y=0; y<c.getFeatureEnds().size(); y++)
        		{
        			c.getFeatureEnds().get(y).setLoc(proposedLoc);
        		}
        		c.setGridLoc(proposedLoc);
        	}
        }
        if(fields!=null)
        {
        	for(int i=0; i<fields.size(); i++)
        	{
        		Field f=fields.get(i);
        		for(int y=0; y<f.getFeatureEnds().size(); y++)
        		{
        			f.getFeatureEnds().get(y).setLoc(proposedLoc);
        		}
        		f.setGridLoc(proposedLoc);
        	}
        }
        setRoadEnds();
        setCityEnds();
        setFieldEnds();
    }
    public Rectangle getMeepRect()
    {
    	return meepRect;
    }
    public Rectangle[][] getMeepRectGrid()
    {
    	return meepRectGrid;
    }
    public Boolean[][] getClaimed(){ return claimed; }
    public void setClaimed(Boolean[][] b){ claimed = b;}
    
    public void setMeepleColor(BufferedImage c){ meepleColor = c; }
    public BufferedImage getMeepleColor(){ return meepleColor; }
}
