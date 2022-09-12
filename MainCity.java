import java.util.ArrayList;

public class MainCity {
	private Boolean hasBeenScored = false;
	ArrayList<City> cities;
	int numShields;
	Boolean isComplete;
	ArrayList<Player> players;
	ArrayList<Meeple> meeples;
	ArrayList<FeatureEnd> featureEnds;
	Location topLeftCorner;
	ArrayList<Tile> tiles=new ArrayList<>();
	public MainCity(City c, int n, ArrayList<FeatureEnd> f, Tile t)
	{
		cities=new ArrayList<>();
		cities.add(c);
		numShields=n;
		isComplete=false;
		players=new ArrayList<>();
		meeples=new ArrayList<>();
		featureEnds=f;
		Location loc=c.getFeatureEnds().get(0).getLoc();
		topLeftCorner=new Location(loc.getX()*3, loc.getY()*3);
		//System.out.println("inserted first city at "+loc.getX()*3+", "+loc.getY()*3+" as its topLeftCorner");
		tiles.add(t);
	}
	public void add(City addingCity, Tile t)
	{
		if(!cities.contains(addingCity))
		{
			updateFeatureEnds(addingCity.getFeatureEnds());
			cities.add(addingCity);
			if(!tiles.contains(t))
			{
				tiles.add(t);
			}
			if(addingCity.getHasShield())
			{
				numShields++;
				System.out.println("numShields increased");
			}
		}
	}
	public void combine(City addingCity, ArrayList<MainCity> mainCities, ArrayList<MainField> mainFields, MainCity myself, Tile t)
	{//Update, do not add individual featureEnds
		add(addingCity, t);
		for(int i=0; i<mainCities.size(); i++)
		{
			MainCity mC=mainCities.get(i);
			ArrayList<City> c=mainCities.get(i).getCities();
			for(int y=0; y<c.size(); y++)
			{
				if(!cities.contains(c.get(y)))
					cities.add(c.get(y));
			}
			ArrayList<Tile> addingTiles=mainCities.get(i).getTiles();
			for(int y=0; y<addingTiles.size(); y++)
			{
				if(!tiles.contains(addingTiles.get(y)))
					tiles.add(addingTiles.get(y));
			}
			for(int y=0; y<c.size(); y++)
			{
				cities.add(c.get(y));
			}
			updateFeatureEnds(mainCities.get(i).getFeatureEnds());
			System.out.println("Combining: added all of a mainCity's city");
			
			ArrayList<Player> p=mC.getPlayers();
			ArrayList<Meeple> m=mC.getMeeples();
			for(int y=0; y<p.size(); y++)
			{
				if(players.contains(p.get(y)))
				{
					Boolean didNotFindMeep=true;
					for(int z=0; z<m.size(); z++)
					{
						if(m.get(z).getOwner().equals(p.get(y).getName()))
						{
							meeples.add(m.get(z));
							didNotFindMeep=false;
							System.out.println("Combining: added a "+m.get(z).getOwner()+" meeple without adding that player");
						}
					}
					if(didNotFindMeep)
					{
						System.out.println("Could not find a meeple belonging to player "+p.get(y).getName()+" in this MainCity without adding player");
						return;
					}
					else
						System.out.println("added all meeples of player "+p.get(y).getName()+" without adding that player");
				}
				else
				{
					players.add(p.get(y));
					Boolean didNotFindMeep=true;
					for(int z=0; z<m.size(); z++)
					{
						if(m.get(z).getOwner().equals(p.get(i).getName()))
						{
							meeples.add(m.get(z));
							didNotFindMeep=false;
							System.out.println("Combining: added a "+m.get(z).getOwner()+" meeple and the player"+p.get(y).getName());
						}
					}
					if(didNotFindMeep)
					{
						System.out.println("Could not find a meeple matching player "+p.get(y).getName()+" in this MainCity when adding that player");
						return;
					}
					else
						System.out.println("added all meeples of player "+p.get(y).getName()+" with adding them");
				}
			}
			for(int y=0; y<mainFields.size(); y++)
			{
				MainField f=mainFields.get(y);
				if(f.getTouchingCities()!=null)
				{
					if(f.getTouchingCities().contains(mC))
					{
						f.getTouchingCities().remove(mC);
						System.out.println("deleted old mainCity");
					}
				}
			}
		}
	}
	public void updateFeatureEnds(ArrayList<FeatureEnd> addEnds)
	{
		for(int i=0; i<addEnds.size(); i++)
		{
			FeatureEnd addEnd=addEnds.get(i);
			Boolean notAdj=true;
			if(!featureEnds.contains(addEnd))
			{
				for(int y=0; y<featureEnds.size(); y++)
				{
					if(notAdj)//only one mainEnd will be opposite and adjacent to featureEnd
					{
						FeatureEnd mainEnd=featureEnds.get(y);
						if(addEnd.isOppAdj(mainEnd))
						{
							notAdj=false;
							featureEnds.remove(mainEnd);
							y--;
							System.out.println("deleted featureEnd: "+mainEnd.getDirection()+" "+mainEnd.getLoc().getX()+", "+mainEnd.getLoc().getY()+" for a mainCity");
						}
					}
				}
				if(notAdj)
				{
					featureEnds.add(addEnd);
					System.out.println("added featureEnd "+addEnd.getDirection()+" "+addEnd.getLoc().getX()+", "+addEnd.getLoc().getY()+" for a mainCity");
				}
			}
		}
	}
	public void setFeatureEnds(ArrayList<FeatureEnd> f)
	{
		featureEnds=f;
	}
	public ArrayList<FeatureEnd> getFeatureEnds(){
		return featureEnds;
	}
	
	public boolean isComplete()//alternate isComplete method
	{
		if(featureEnds.size()==0)
		{
			isComplete=true;
			return true;
		}
		else
			return false;
	}

	public int getScore()
	{
		if(isComplete)
		{
			return(2*tiles.size()+2*numShields);
		}
		else
		{
			return(tiles.size()+numShields);
		}
	}
	public boolean addPlayer(Player p, GameLog g)
	//also updates meeples
	//will not work if player has no meeples
	//only used for claiming unclaimed MainCities
	{
		if(players.isEmpty())
		{
			if(p.canUseMeeple())
			{
				Meeple m=p.getUsableMeeple();
				players.add(p);
				meeples.add(m);
				System.out.println("added Player "+p.getName());
				System.out.println("MainCity addPlayer: added "+p.getName());
				return true;
			}
			System.out.println("No Meeples");
		}
		System.out.println("This MainCity is already claimed (addPlayer)");
		return false;
	}
	public String[][] insert2D(String[][] main, String[][] a, int x, int y)
	{
		for(int r=x, i=0; r<x+3 && i<3; r++, i++)
		{
			for(int c=y, j=0; c<y+3 && j<3; c++, j++)
				main[r][c]=a[i][j];
		}
		return main;
	}
	public ArrayList<City> getCities()
	{
		return cities;
	}
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	public ArrayList<Meeple> getMeeples()
	{
		return meeples;
	}
	
	public boolean isValid(City c)
	{
		for(int i=0; i<cities.size(); i++)
		{
			if(cities.get(i).isValid(c))
				return true;
		}
		return false;
	}
	public void setHasBeenScored(Boolean b) {
		hasBeenScored = b;
		}
	public Boolean getHasBeenScored() {
		return hasBeenScored;
	}
	public void printFeatureEnds()
	{
		System.out.println("It has "+cities.size()+" cities and "+numShields+" shields.");
		for(int i=0; i<featureEnds.size(); i++)
		{
			System.out.println(featureEnds.get(i));
		}
	}
	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}
}
