public class MainMonastery {
	//Attributes
	private Boolean hasBeenScored = false;
	private Boolean isComplete;
	private Location meepleLoc = new Location();
	private Location[][] Locations = new Location[3][3];
	private Meeple meep;
	private Location tileLoc;
	
	public MainMonastery(Location l) {
		isComplete = false;
		Locations [1][1] = l;
		tileLoc = l;
		meepleLoc.setX(1);
		meepleLoc.setY(1);
		meep=null;
		// use grid to find the adj grid locations and use add method to put them in monastery *not done
	}
	public MainMonastery(Location l, Meeple m) {
		isComplete = false;
		Locations [1][1] = l;
		tileLoc = l;
		meepleLoc.setX(1);
		meepleLoc.setY(1);
		meep = m;
		// use grid to find the adj grid locations and use add method to put them in monastery *not done
	}
	
	//Sets is complete to true if locations array is full
	public void checkComplete() {
		int x = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(Locations[i][j] != null) {
					x++;
				}
			}
		}
		//System.out.println("There are " + x + " tiles in Monastery");//used for debugging
		if( x == 9) {
			isComplete = true;
		} else {
			isComplete = false;
		}
	}
	// adds the Location to the proper spot in monastery's array... also checks for complete after adding so we don't have to call the check every time we add a tile
	public void add(Location l) {
		// grid will always pass in valid location so no need to check if it is out of bounds
		int i = 0;
		int j = 0;
		if(l.getX() == Locations[1][1].getX()) {
			i = 1;
		}else if(l.getX() > Locations[1][1].getX()) {
			i = 2;
		}
		if(l.getY() == Locations[1][1].getY()) {
			j = 1;
		}else if(l.getY() > Locations[1][1].getY()) {
			j = 2;
		}
		if(Locations[i][j]==null)
		{
			Locations[i][j]  = l;
			System.out.println("Added in "+l+" to MainMonastery at "+i+", "+j);
			checkComplete();
		}//also used for debugging
	}
	
	// Awards a point for every tile that is not null in monasteries array and returns the total
	public int scoreMonastery() {
		int score = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(Locations[i][j] != null) {
					score++;
					}
				}
			}
		return score;
		}
	
	// Prints the monastery's Location array in a 2d format with a dash which symbolizes a spot that is currently holding a location 
	public void printMonastery() {
		String str = "";
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(Locations[i][j] != null) {
					str = str + "[-]";
					}else{
						str = str + "[]";
					}
				}
			System.out.println(str);
			System.out.println();
			str = "";
			}
		
	}
	
	public boolean addPlayer(Player p, GameLog g) {
		if(!p.canUseMeeple())
		{
			g.addNoMeeples();
			System.out.println(p.getName()+"has no Meeples");
			return false;
		}
		else
		{
			Meeple m=p.getUsableMeeple();
			meep = m;
			System.out.println("MainMonastery added Player "+p.getName());
			return true;
		}
	}
	
	//SET METHODS
	public void setisComplete(Boolean b) { isComplete = b;}
	public void setmeepleLoc(Location l) {meepleLoc = l;}
	public void setMeep(Meeple m) {meep = m;}
	public void setTileLoc(Location l) {tileLoc = l;}
	public void setHasBeenScored(Boolean b) {
		hasBeenScored = b;
		}
	//GET METHODS
	public Boolean getisComplete() {return isComplete;}
	public Location getmeepleLoc() {return meepleLoc;}
	public String getOwner()
	{
		if(meep!=null)
			return meep.getOwner();
		else
			return null;
	}
	public Location getTileLoc() {return tileLoc;}
	public Meeple getMeep() {return meep;}
	public Location[][] getLocations(){return Locations;}
	public Boolean getHasBeenScored() {
		return hasBeenScored;
	}
	public boolean contains(Location l)
	{
		for(int i=0; i<3; i++)
		{
			for(int y=0; y<3; y++)
			{
				Location loc=Locations[i][y];
				if(loc!=null&&loc.equals(l))
					return true;
			}
		}
		return false;
	}
}
