import java.util.*;
public class MainRiver {
	
	//Attributes
	private String oldDirection, newDirection, oldTurned, newTurned;
	private FeatureEnd mainEnd;
	private ArrayList<River> Rivers;
	private Location neededLocation;
	
	//constructor
	public MainRiver() {
		oldDirection = "D";
		newDirection = "D";
		oldTurned = "D";
		newTurned = "D";
		neededLocation=new Location(5, 6);
		mainEnd=new FeatureEnd();
		Rivers= new ArrayList<>();
	}
	
	//add method
	public void addRiver(River r, GameLog g) {
		if(r.getStartLocation() == null) {
			//This checks to see if the tile  we are adding is the starting river tile and adds it to the Arraylist
			Rivers.add(r);
			mainEnd = r.getEndLocation();//(5, 5)
		}
		else if(r.getEndLocation() == null) {
				//This checks to see if the tile  we are adding is the ending river tile and adds it to the Arraylist
				Rivers.add(r);
				mainEnd = r.getEndLocation();
				oldDirection = newDirection;
				setNeededLocation(mainEnd);
			}
		else if(isValid(r, g) == true) {
			//This checks if the rivers current orientation is valid and if it is the river is added to the Arraylist
			Rivers.add(r);
			System.out.println("\nmainEnd: "+mainEnd+", startLoc: "+r.getStartLocation()+", endLoc: "+r.getEndLocation());
			if(mainEnd.isOppAdj(r.getStartLocation()))
			{
				mainEnd.setFeatureEnd(r.getEndLocation());
				System.out.println("mainEnd.isOppAdj(r.getStartLocation()");
			}
			else if(mainEnd.isOppAdj(r.getEndLocation()))
			{
				mainEnd.setFeatureEnd(r.getStartLocation());
				System.out.println("mainEnd.isOppAdj(r.getEndLocation()");
			}
			System.out.println("\nnew mainEnd: "+mainEnd);
			oldDirection = newDirection;
			oldTurned = newTurned;
			setNeededLocation(mainEnd);
			
			System.out.println("mainEnd is now: "+mainEnd);
			System.out.println("River added");
		}
		else
		{
			System.out.println("River cannot be added");
		}
	}
	
	//Other Methods
	public boolean isValid(River r, GameLog g) {
		if(r.getStartLoc().equals(neededLocation)) {
		if(checkOpp(r) == true) {
			setTurned(r);
			if(makingUTurn(r)) {
				return false;
			}
			//check if rivers end location points to a null tile in grid done in game state
			return true;
			} 
		}else
			g.addMessage("Not valid, not opposite");
			return false;
	}
	
	public boolean makingUTurn(River r)
	{
		if(r.getEndDirection().equals("X")||r.getStartDirection().equals("X"))
			return false;
		String temp=newTurned;
		setTurned(r);
		if(oldTurned.equals("L") && newTurned.equals("L")|| oldTurned.equals("R") && newTurned.equals("R")) {
			newTurned=temp;
			return true;
		}
		else
		{
			newTurned=temp;
			return false;
		}
	}
	
	public Boolean checkOpp(River r) {
	FeatureEnd temp;
	// checks if the rivers end location connects with main ends river
	if(r.getEndLocation()!=null)
	{
		if(r.getEndDirection().equals("X"))
		{
			if(r.getStartLocation()!=null)
			{
				if(r.getStartDirection().equals("U")&& mainEnd.getDirection().equals("D") || r.getStartDirection().equals("D")&& mainEnd.getDirection().equals("U") || r.getStartDirection().equals("L")&& mainEnd.getDirection().equals("R") || r.getStartDirection().equals("R")&& mainEnd.getDirection().equals("L")) {
					// set temp to start location
					temp = r.getStartLocation();
					// startLocation becomes endLocation
					r.setStartLocation(r.getEndLocation());
					//endLocation becomes startLocation
					r.setEndLocation(temp);
					return true;
				}
			}
			return false;
		}
		if(r.getStartDirection().equals("X"))
		{
			if(r.getEndLocation()!=null)
			{
				if(r.getEndDirection().equals("U")&& mainEnd.getDirection().equals("D") || r.getEndDirection().equals("D")&& mainEnd.getDirection().equals("U") || r.getEndDirection().equals("L")&& mainEnd.getDirection().equals("R") || r.getEndDirection().equals("R")&& mainEnd.getDirection().equals("L")) {
					// set temp to start location
					temp = r.getStartLocation();
					// startLocation becomes endLocation
					r.setStartLocation(r.getEndLocation());
					//endLocation becomes startLocation
					r.setEndLocation(temp);
					return true;
				}
			}
			return false;
		}
		if(r.getEndDirection().equals("U")&& mainEnd.getDirection().equals("D") || r.getEndDirection().equals("D")&& mainEnd.getDirection().equals("U") || r.getEndDirection().equals("L")&& mainEnd.getDirection().equals("R") || r.getEndDirection().equals("R")&& mainEnd.getDirection().equals("L")) 
				return true;
	}
	// checks if the startLocation connects with main ends river and swaps the start and end locations
	if(r.getStartLocation()!=null)
	{
		if(r.getStartDirection().equals("U")&& mainEnd.getDirection().equals("D") || r.getStartDirection().equals("D")&& mainEnd.getDirection().equals("U") || r.getStartDirection().equals("L")&& mainEnd.getDirection().equals("R") || r.getStartDirection().equals("R")&& mainEnd.getDirection().equals("L")) {
				// set temp to start location
				temp = r.getStartLocation();
				// startLocation becomes endLocation
				r.setStartLocation(r.getEndLocation());
				//endLocation becomes startLocation
				r.setEndLocation(temp);
				return true;
			}
			return false;
	}
	return false;
	}
	//Set Methods
	public void setOldDirection(String d) {
		oldDirection = d;
	}
	public void setNewDirection(String d) {
		newDirection = d;
	}
	public void setOldTurned(String d) {
		oldTurned = d;
	}
	public void setNewTurned(String d) {
		newTurned = d;
	}
	public void setTurned(River r) {
		//sets the new turned based on new rivers start and end directions
		if(r.getEndDirection().equals("X")||r.getStartDirection().equals("U") && r.getEndDirection().equals("D") || r.getStartDirection().equals("D") && r.getEndDirection().equals("U") || r.getStartDirection().equals("L") && r.getEndDirection().equals("R")|| r.getStartDirection().equals("R") && r.getEndDirection().equals("L"))
			newTurned = "D";
		else if(r.getStartDirection().equals("U") && r.getEndDirection().equals("L") || r.getStartDirection().equals("R") && r.getEndDirection().equals("U") || r.getStartDirection().equals("D") && r.getEndDirection().equals("R")|| r.getStartDirection().equals("L") && r.getEndDirection().equals("D"))
			newTurned = "R";
		else if(r.getStartDirection().equals("U") && r.getEndDirection().equals("R") || r.getStartDirection().equals("R") && r.getEndDirection().equals("D") || r.getStartDirection().equals("D") && r.getEndDirection().equals("L")|| r.getStartDirection().equals("L") && r.getEndDirection().equals("U"))
			newTurned = "L";
	}
	public void setMainEnd(FeatureEnd e) {
		mainEnd = e;
	}
	public void setRiver(int i, River r) {
		Rivers.set(i, r);
	}
	public void setRivers(ArrayList<River> r) {
		Rivers = r;
	}
	public void setNeededLocation(Location l) {
		neededLocation = l;
	}
	public void setNeededLocation(FeatureEnd f) {
		//Updates needed location based on mainEnds direction
		neededLocation.setLoc(f.getLoc().getX(), f.getLoc().getY());
		if(f.getDirection().equals("D")) {
			neededLocation.setY(neededLocation.getY() + 1);
		}
		if(f.getDirection().equals("U")) {
			neededLocation.setY(neededLocation.getY() - 1);
		}
		if(f.getDirection().equals("L")) {
			neededLocation.setX(neededLocation.getX() - 1);
		}
		if(f.getDirection().equals("R")) {
			neededLocation.setX(neededLocation.getX() + 1);
		}
	}
	
	//GetMethods
	public String getOldDirection() {
		return oldDirection;
	}
	public String getNewDirection() {
		return newDirection;
	}
	public String getOldTurned() {
		return oldTurned;
	}
	public String getNewTurned() {
		return oldDirection;
	}
	public FeatureEnd getMainEnd() {
		return mainEnd;
	}
	public River getRiver(int i){
		return Rivers.get(i);
	}
	public ArrayList<River> getRivers(){
		return Rivers;
	}
	public Location getNeededLocation() {
		return neededLocation;
	}
}
