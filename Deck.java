import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.*;

public class Deck {
	ArrayList<Tile> tiles;
	ArrayList<Tile> riverTiles;
	Tile startingRiver, endingRiver, startingTile;
	public Deck(BufferedImage r, BufferedImage e, BufferedImage t)
	{
		tiles=new ArrayList<>();
		riverTiles=new ArrayList<>();
		String[][] pG1= {{"F", "F", "F"}, {"F", "RV", "F"}, {"F", "RV", "F"}};
		String[][] mG1= {{null, null, null}, {null, null, null}, {null, null, null}};
		ArrayList<FeatureEnd> f1=new ArrayList<>();
		f1.add(new FeatureEnd(new Location(5, 5), "UL"));
		f1.add(new FeatureEnd(new Location(5, 5), "LU"));
		f1.add(new FeatureEnd(new Location(5, 5), "LD"));
		f1.add(new FeatureEnd(new Location(5, 5), "DL"));
		f1.add(new FeatureEnd(new Location(5, 5), "DR"));
		f1.add(new FeatureEnd(new Location(5, 5), "RD"));
		f1.add(new FeatureEnd(new Location(5, 5), "RU"));
		f1.add(new FeatureEnd(new Location(5, 5), "UR"));
		
		ArrayList<Field> fields1=new ArrayList<>();
		
		ArrayList<Location> mLocs1=new ArrayList<>();
		mLocs1.add(new Location(0, 0));
		mLocs1.add(new Location(0, 1));
		mLocs1.add(new Location(0, 2));
		mLocs1.add(new Location(1, 0));
		mLocs1.add(new Location(1, 2));
		mLocs1.add(new Location(2, 0));
		mLocs1.add(new Location(2, 2));
		
		fields1.add(new Field(f1, mLocs1, new Location(5, 5), null));
		
		startingRiver=new Tile(r, new River(null, new FeatureEnd(new Location(5, 5), "D")), null, null, fields1, false, new GridLocation(5, 5), pG1, mG1);
		startingRiver.setLocation(new Location(15, 15));
		
		ArrayList<FeatureEnd> f2=new ArrayList<>();
		f2.add(new FeatureEnd(new Location(), "UL"));
		f2.add(new FeatureEnd(new Location(), "LU"));
		f2.add(new FeatureEnd(new Location(), "LD"));
		f2.add(new FeatureEnd(new Location(), "DL"));
		f2.add(new FeatureEnd(new Location(), "DR"));
		f2.add(new FeatureEnd(new Location(), "RD"));
		f2.add(new FeatureEnd(new Location(), "RU"));
		f2.add(new FeatureEnd(new Location(), "UR"));
		
		String[][] pG2= {{"F","RV", "F"}, {"F", "RV", "F"}, {"F", "F", "F"}};
		String[][] mG2= {{"F", null, "F"}, {null, null, null}, {null, null, null}};
		
		ArrayList<Field> fields2=new ArrayList<>();
		
		ArrayList<Location> mLocs2=new ArrayList<>();
		mLocs2.add(new Location(0, 0));
		mLocs2.add(new Location(0, 2));
		
		fields2.add(new Field(f2, mLocs2, new Location(), null));
		
		endingRiver=new Tile(e, new River(new FeatureEnd(new Location(), "U"), new FeatureEnd(new Location(), "X")), null, null, fields2, false, new GridLocation(), pG2, mG2);
		
		//must update isTop and isLeft for fields when rotating
		ArrayList<Road> rd1=new ArrayList<>();
		
		ArrayList<FeatureEnd> f3=new ArrayList<>();
		f3.add(new FeatureEnd(new Location(), "L"));
		f3.add(new FeatureEnd(new Location(), "R"));
		
		ArrayList<Location> mLocs3=new ArrayList<>();
		mLocs3.add(new Location(1, 1));
		
		rd1.add(new Road(false, f3, mLocs3, null));
		
		ArrayList<City> c1=new ArrayList<>();
		
		String[][] pg3= {{"W", "W", "W"}, {"R", "R", "R"}, {"F", "F", "F"}};
		String[][] pg3a= {{"W", "W", "W"}, {"R", "R", "R"}, {"F", "F", "F"}};
		ArrayList<FeatureEnd> f4=new ArrayList<>();
		f4.add(new FeatureEnd(new Location(), "U"));
		
		ArrayList<Location> mLocs4=new ArrayList<>();
		mLocs4.add(new Location(0, 1));
		
		c1.add(new City(pg3a, false, f4, mLocs4, null));
		
		ArrayList<Field> fields3=new ArrayList<>();
		
		ArrayList<FeatureEnd> f5=new ArrayList<>();
		f5.add(new FeatureEnd(new Location(), "LU"));
		f5.add(new FeatureEnd(new Location(), "RU"));
		
		ArrayList<Location> mLocs5=new ArrayList<>();
		mLocs5.add(new Location(0, 0));
		mLocs5.add(new Location(0, 2));
		
		fields3.add(new Field(f5, mLocs5, null, c1));
		
		ArrayList<FeatureEnd> f6=new ArrayList<>();
		f6.add(new FeatureEnd(new Location(), "LD"));
		f6.add(new FeatureEnd(new Location(), "DL"));
		f6.add(new FeatureEnd(new Location(), "DR"));
		f6.add(new FeatureEnd(new Location(), "RD"));
		
		ArrayList<Location> mLocs6=new ArrayList<>();
		mLocs6.add(new Location(2, 1));
		
		fields3.add(new Field(f6, mLocs6, null, null));
		
		String[][] mG3= {{"F", "C", "F"}, {null, "R", null}, {null, "F", null}};
		startingTile=new Tile(t, null, rd1, c1, fields3, false, null, pg3, mG3);
	}
	public void setTiles(ArrayList<Tile> t)
	{
		tiles=t;
	}
	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}
	public void setRiverTiles(ArrayList<Tile> t)
	{
		riverTiles=t;
	}
	public ArrayList<Tile> getRiverTiles()
	{
		return riverTiles;
	}
	public void setStartingRiver(Tile t)
	{
		startingRiver=t;
	}
	public Tile getStartingRiver()
	{
		return startingRiver;
	}
	public void setEndingRiver(Tile t)
	{
		endingRiver=t;
	}
	public Tile getEndingRiver()
	{
		return endingRiver;
	}
	public void setStartingTile(Tile t)
	{
		startingTile=t;
	}
	public Tile getStartingTile()
	{
		return startingTile;
	}
	public void shuffleRivers()
	{
		Collections.shuffle(riverTiles);
	}
	public void shuffleTiles()
	{
		Collections.shuffle(tiles);
	}
}
