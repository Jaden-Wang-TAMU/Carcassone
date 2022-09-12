import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;

public class Grid {
	public GridLocation[][] gridLocs;
	public Tile[][] tiles;
	Rectangle space;
	boolean canPlace, cannotPlace;
	public Grid()
	{
		gridLocs=new GridLocation[31][31];
		for(int i=0; i<31; i++)
		{
			for(int y=0; y<31; y++)
				gridLocs[i][y]=new GridLocation(i, y);
		}
		tiles=new Tile[31][31];
		space=new Rectangle(468, 38, 990, 990);
		canPlace=false;
		cannotPlace=false;
	}
	public boolean contains(Point p)
	{
		return space.contains(p);
	}
	public void addTile(Tile t, Location l)
	{
		int x=l.getX();
		int y=l.getY();
		tiles[x][y]=t;
	}
	public ArrayList<GridLocation> getAdjGridLocs(Location l)
	{
		int x=l.getX();
		int y=l.getY();
		ArrayList<GridLocation> adj=new ArrayList<>();
		if(x!=0&&y!=0)
			adj.add(gridLocs[x-1][y-1]);
		if(x!=0)
			adj.add(gridLocs[x-1][y]);
		if(x!=0&&y!=30)
			adj.add(gridLocs[x-1][y+1]);
		if(y!=0)
			adj.add(gridLocs[x][y-1]);
		if(x!=30)
			adj.add(gridLocs[x+1][y]);
		if(x!=30&&y!=0)
			adj.add(gridLocs[x+1][y-1]);
		if(y!=30)
			adj.add(gridLocs[x][y+1]);
		if(x!=30&&y!=30)
			adj.add(gridLocs[x+1][y+1]);
		for(int i=0; i<adj.size(); i++)
		{
			System.out.println("Adjacent Locs: "+adj.get(i));
		}
		return adj;
	}
	public ArrayList<Tile> getAdjTiles(Location l, int height, int width)
	{
		ArrayList<GridLocation> adjGridLocs=getAdjGridLocs(l);
		ArrayList<Tile> adjTiles=new ArrayList<>();
		System.out.println("getAdjTiles: height: "+height+" width: "+width);
		for(int i=0; i<adjGridLocs.size(); i++)
		{
			GridLocation gridLoc=adjGridLocs.get(i);
			adjTiles.add(tiles[gridLoc.getGridLoc().getY()+height][gridLoc.getGridLoc().getX()+width]);
		}
		if(adjTiles.size()!=0)
			return adjTiles;
		else
			return null;
	}
	public GridLocation[][] getGridLocs()
	{
		return gridLocs;
	}
	public void setGridLocs(GridLocation[][] g)
	{
		gridLocs=g;
	}
	public Tile[][] getTiles()
	{
		return tiles;
	}
	public void setTiles(Tile[][] t)
	{
		tiles=t;
	}
	public Rectangle getSpace()
	{
		return space;
	}
	public void setSpace(Rectangle s)
	{
		space=s;
	}
	public boolean getCanPlace()
	{
		return canPlace;
	}
	public void setCanPlace(boolean c)
	{
		canPlace=c;
	}
	public boolean getCannotPlace()
	{
		return cannotPlace;
	}
	public void getCannotPlace(boolean c)
	{
		cannotPlace=c;
	}
}
