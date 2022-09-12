import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainRoad {
	private Boolean hasBeenScored = false;
    private Boolean ended1, ended2;
    private ArrayList<Road> roads;
    private ArrayList<Player> players;
    private ArrayList<Meeple> meeples;
    private ArrayList<FeatureEnd> featureEnds;
    private ArrayList<Tile> tiles=new ArrayList<>();

    public MainRoad(){
        ended1 = ended2 = false;
        roads = new ArrayList<>();
        players = new ArrayList<>();
        meeples = new ArrayList<>();
        featureEnds = new ArrayList<>();
    }

    public MainRoad(Boolean e1, Boolean e2, ArrayList<Road> r, ArrayList<Player> p, ArrayList<Meeple> m, ArrayList<FeatureEnd> f, Tile t){
        ended1 = e1;
        ended2 = e2;
        roads = r;
        players = p;
        meeples = m;
        featureEnds = f;
        tiles.add(t);
    }



    public void setEnded1(Boolean e){ ended1 = e; }
    public Boolean getEnded1(){ return ended1; }

    public void setEnded2(Boolean e){ ended2 = e; }
    public Boolean getEnded2(){ return ended2; }

    public void setRoads(ArrayList<Road> r){ roads = r; }
    public ArrayList<Road> getRoads(){ return roads; }

    public void setPlayers(ArrayList<Player> p){ players = p; }
    public ArrayList<Player> getPlayers(){ return players; }
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
				System.out.println("MainRoad addPlayer: added "+p.getName());
				return true;
			}
		}
		return false;
	}

    public void setMeeples(ArrayList<Meeple> m){ meeples = m; }
    public ArrayList<Meeple> getMeeples(){ return meeples; }

    public void setFeatureEnds(ArrayList<FeatureEnd> f){ featureEnds = f; }
    public ArrayList<FeatureEnd> getFeatureEnds(){ return featureEnds; }

    public Boolean isComplete()
    {
    	return (ended1 && ended2)||(featureEnds.size()==0);
    }

    public void add(Road newRoad, Tile t){
        if(!roads.contains(newRoad))
	    {
	        updateFeatureEnds(newRoad.getFeatureEnds());
	    	roads.add(newRoad);
	    	if(!tiles.contains(t))
	    		tiles.add(t);
	        if(newRoad.getIsEnd())
	        {
	        	if(!ended1)
	        		ended1=true;
	        	else
	        		ended2=true;
	        }
	        	
	        System.out.println("added a road");
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

    public void combine(Road newRoad, MainRoad main, Tile t){
        add(newRoad, t);
        for(int i=0; i<main.getRoads().size(); i++)
		{
        	if(!roads.contains(main.getRoads().get(i)))
        		roads.add(main.getRoads().get(i));
		}
        for(int i=0; i<main.getTiles().size(); i++)
		{
        	if(!tiles.contains(main.getTiles().get(i)))
        		tiles.add(main.getTiles().get(i));
		}
        updateFeatureEnds(main.getFeatureEnds());

        ArrayList<Player> p = main.getPlayers();
        ArrayList<Meeple> m = main.getMeeples();
        for(Player player: p){
            if(!players.contains(player)){
            	players.add(player);
            }
        }
        int count=0;
        for(int i = m.size() - 1; i >= 0; i--){
            meeples.add(m.get(i));
            m.remove(m.get(i));
            count++;
        }
        System.out.println("added"+count+"meeples of player");
    }
    public int getScore() {
        return tiles.size();
    }
	public void setHasBeenScored(Boolean b) {
		hasBeenScored = b;
		}
	public Boolean getHasBeenScored() {
		return hasBeenScored;
	}
	
	public void printFeatureEnds()
	{
		System.out.println("It has "+roads.size()+" roads.");
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
