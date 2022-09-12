import java.util.ArrayList;

public class MainField {
private Boolean hasBeenScored = false;
	ArrayList<Player> players;
	ArrayList<Meeple> meeples;
	
	int score=0;
	ArrayList <MainCity> touchingCities;
	ArrayList<FeatureEnd> featureEnds;
	
	ArrayList <Field> Fields;
	
	public MainField(ArrayList<FeatureEnd> f, ArrayList <MainCity> tC, ArrayList<Field> fi) {
		
	featureEnds = f;
	touchingCities = tC;
	players=new ArrayList<>();
	meeples=new ArrayList<>();
	Fields = fi;
		//Constructor
	}
	public ArrayList<FeatureEnd> getFeatureEnds(){
	return featureEnds;
	}
	public ArrayList<Field> getFields() {
		return Fields;
	}
	
	public ArrayList<MainCity> getTouchingCities() {
		return touchingCities;
	}
	
	public void setFields (ArrayList <Field> f) {
		Fields = f;
	}
	public void setTouchingCities(ArrayList<MainCity> gtc) {
		touchingCities=gtc;
	}
	
	public  ArrayList<Meeple> getMeeples()
	{
		return meeples;
	}
	
	public ArrayList<Player>  getPlayers () {
		return players;
	}
	
	public  void setMeeples(ArrayList<Meeple> m)
	{
		meeples=m;
	}
	
	public void setPlayers (ArrayList<Player> p) {
		players=p;
	}
	//get and set methods
	public void add(Field addingField, ArrayList<MainCity> mC)
	{
		if(!Fields.contains(addingField))
		{
			updateFeatureEnds(addingField.getFeatureEnds());
			Fields.add(addingField);
			if(addingField.getCities()!=null)
			{
				for(int i=0; i<addingField.getCities().size(); i++)
				{
					City c=addingField.getCities().get(i);
					boolean containedCity=false;
					if(touchingCities!=null)
					{
						for(int y=0; y<touchingCities.size(); y++)
						{
							if(touchingCities.get(y).getCities().contains(c))
							{
								containedCity=true;
							}
						}
						
						if(!containedCity)
						{
							for(int q=0; q<mC.size(); q++)
							{
								if(mC.get(q).getCities().contains(c))
								{
									touchingCities.add(mC.get(q));
									break;
								}
							}
						}
					}
					else
					{
						touchingCities=new ArrayList<MainCity>();
						for(int q=0; q<mC.size(); q++)
						{
							if(mC.get(q).getCities().contains(c))
							{
								touchingCities.add(mC.get(q));
								break;
							}
						}
					}
				}
			}
		}
	}
	public void combine(Field addingField, ArrayList<MainField> mainFields, ArrayList<MainCity> mC)
	{
		add(addingField, mC);
		//update players and meeples if needed
		//delete the mainFields you combine into this one, they no longer exist. They will have no fields anyway.
		for (int x = 0; x < mainFields.size(); x ++) {
			MainField mf = mainFields.get(x);
			for(int y=0; y<mf.getFields().size(); y++)
			{
					add(mf.getFields().get(y), mC);
			}
			updateFeatureEnds(mf.getFeatureEnds());
			ArrayList<Player> pl=mf.getPlayers();
			ArrayList<Meeple> ml=mf.getMeeples();
			for(int z=0; z<ml.size(); z++)
			{
					meeples.add(ml.get(z));
			}
			for(int z=0; z<pl.size(); z++)
			{
				if(!players.contains(pl.get(z)))
					players.add(pl.get(z));
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
						if(addEnd.isOppAdjField(mainEnd))
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
	
	
	public boolean addPlayer(Player p, GameLog g)
	
	{
		if(players.isEmpty())
		{
			if(!p.canUseMeeple())
			{
				g.addNoMeeples();
				System.out.println(p.getName()+"has no Meeples");
				return false;
			}
			else
			{
				Meeple m=p.getUsableMeeple();
				players.add(p);
				meeples.add(m);
				System.out.println("MainField addPlayer: added "+p.getName());
				return true;
			}
		}
		System.out.println("This MainField is already claimed (addPlayer)");
		return false;
	}
	
	public void printFeatureEnds()
	{
		//if(Fields!=null&&touchingCities!=null)
		int tc=0;
		if(touchingCities!=null)
			tc=touchingCities.size();
		System.out.println("It has "+Fields.size()+" fields and is touching "+tc+" cities");
		for(int i=0; i<featureEnds.size(); i++)
		{
			System.out.println(featureEnds.get(i));
		}
	}
	
	public int getScore() {
		int completeCities=0;
		for(int i=0; i<touchingCities.size(); i++)
		{
			if(touchingCities.get(i).isComplete)
				completeCities++;
		}
		return completeCities*3;
		
	}
	public void setHasBeenScored(Boolean b) {
		hasBeenScored = b;
		}
	public Boolean getHasBeenScored() {
		return hasBeenScored;
	}
}
	
	
