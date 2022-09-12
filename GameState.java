import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import java.util.ListIterator;

public class GameState {
	ArrayList<Player> players;
	Player blue;
	Player green;
	Player yellow;
	Player red;
	Player currentPlayer;
	Grid grid;
	String[][] stringGrid;
	Deck deck;
	Tile currentTile;
	ListIterator<Tile> riverIter;
	ListIterator<Tile> deckIter;
	ListIterator<Player> playerIter;
	GameLog gameLog;
	Boolean endGame, allowClickingGrid, allowClickingMeep, allowPlacing, allowDiscarding, allowClaiming, allowNotClaiming, allowRotating;
	MainRiver mainRiver;
	ArrayList<MainCity> mainCities;
	ArrayList<MainMonastery> mainMonasteries;
	ArrayList<MainRoad> mainRoads;
	ArrayList<MainField> mainFields;
	ArrayList<Location> noRotation, oneRotation, twoRotation, threeRotation;
	BufferedImage startR;
	BufferedImage endR;
	BufferedImage startT=null;
	ArrayList<Tile> riverTiles=new ArrayList<>();
	ArrayList<BufferedImage> tilePics=new ArrayList<>();
	int tileCount=83;
	
	public GameState(Player b, Player g, Player y, Player r)
	{
		players=new ArrayList<>();
		blue=b;
		green=g;
		yellow=y;
		red=r;
		players.add(blue);
		players.add(green);
		players.add(yellow);
		players.add(red);
		//System.out.println("added all players, players size="+players.size());
		currentPlayer=new Player();
		randomOrder();
		playerIter=players.listIterator();
		currentPlayer=playerIter.next();
		grid=new Grid();
		stringGrid=new String[93][93];//inserting startingRiver after all riverTiles created
		gameLog=new GameLog();
		endGame=false;
		allowClickingGrid=true;
		allowClickingMeep=false;
		allowPlacing=false;
		allowDiscarding=true;
		allowClaiming=false;
		allowNotClaiming=false;
		allowRotating=true;
		mainRiver=new MainRiver();
		mainCities=new ArrayList<>();
		mainMonasteries=new ArrayList<>();
		mainRoads=new ArrayList<>();
		mainFields=new ArrayList<>();
		noRotation=new ArrayList<>();
		oneRotation=new ArrayList<>();
		twoRotation=new ArrayList<>();
		threeRotation=new ArrayList<>();
		try {
			startR=ImageIO.read(CarcassonnePanel.class.getResource("/Images/r1.jpg"));
			endR=ImageIO.read(CarcassonnePanel.class.getResource("/Images/r12.jpg"));
			startT=ImageIO.read(CarcassonnePanel.class.getResource("/Images/t1.jpg"));
			deck=new Deck(startR, endR, startT);
			for(int i=2; i<12; i++)
			{
				String temp="/Images/r"+i+".jpg";
				BufferedImage pic=ImageIO.read(CarcassonnePanel.class.getResource(temp));
				Tile rv=new Tile(pic);
				deck.getRiverTiles().add(rv);
			}
			//System.out.println("1. river Tiles size: "+deck.getRiverTiles().size());
			for(int i=2; i<25; i++)
			{
				String temp="/Images/t"+i+".jpg";
				BufferedImage pic=ImageIO.read(CarcassonnePanel.class.getResource(temp));
				tilePics.add(pic);//adding all pictures of tiles to an arraylist except for the first one which is already in deck
			}
		}
		catch (Exception e) {
            System.out.print(e);
            System.out.println(" in gameState");
        }
		
		createTiles();
	}
	public boolean riverPointingNull(Location l, String d, int height, int width)
	{
			//move down 1 level in array
			if(d.equals("D")){l.setX(l.getX()+1);}
			//move up 1 level in array
			if(d.equals("U")){l.setX(l.getX()-1);}
			//move left one level in array
			if(d.equals("L")){l.setY(l.getY()-1);}
			//move right one level in array
			if(d.equals("R")){l.setX(l.getX()+1);}
			if(grid.getAdjTiles(l, height, width) == null) {
				//The location of the tile the river is pointing to is null
				System.out.println("Rivers is pointing to a null tile at location (" + l.getX() + ", " + l.getY() + ")");
				return true;
			}
			//The location of the tile the river is pointing to is not null
			System.out.println("Rivers is pointing to a tile at location (" + l.getX() + ", " + l.getY() + ")");
			return false;
	}
	public boolean checkRiver(River r, int height, int width) {
		if(riverPointingNull(r.getEndLoc(), r.getEndDirection(), height, width) == true) {
			return mainRiver.isValid(r,gameLog);
		}
		return false;
	}
	public ArrayList<Location> findPossibleLocs(Tile t, GameLog g, int height, int width)
	{
		//Tile does not have a proposed location yet
		ArrayList<Location> possibleLocs=new ArrayList<>();
		River tileRiver=t.getRiver();
		Tile[][] gridTiles=grid.getTiles();
		
		//Only one possibleLoc if tile has a river
		if(tileRiver!=null)
		{
			if(tileRiver.getEndDirection().equals("X")||tileRiver.getStartDirection().equals("X"))
			{
				if(mainRiver.checkOpp(tileRiver))
				{
					int mX=mainRiver.getNeededLocation().getX()+width;
					int mY=mainRiver.getNeededLocation().getY()+height;
					if(mX>-1&&mX<31&&mY>-1&&mY<31)
					{
						if(mY-1>-1)
						{
							Tile up=gridTiles[mY-1][mX];
							if(up.getPlaceGrid()[0][0]!=null)
							{
								if(up.getRoadEnds().size()>0)
								{
									for(FeatureEnd r:up.getRoadEnds())
									{
										if(r.getDirection().equals("D"))
											return null;
									}
								}
								if(up.getCityEnds().size()>0)
								{
									for(FeatureEnd r:up.getCityEnds())
									{
										if(r.getDirection().equals("D"))
											return null;
									}
								}
							}
						}
						if(mY+1<31)
						{
							Tile up=gridTiles[mY+1][mX];
							if(up.getPlaceGrid()[0][0]!=null)
							{
								if(up.getRoadEnds().size()>0)
								{
									for(FeatureEnd r:up.getRoadEnds())
									{
										if(r.getDirection().equals("U"))
											return null;
									}
								}
								if(up.getCityEnds().size()>0)
								{
									for(FeatureEnd r:up.getCityEnds())
									{
										if(r.getDirection().equals("U"))
											return null;
									}
								}
							}
						}
						if(mX-1>-1)
						{
							Tile up=gridTiles[mY][mX-1];
							if(up.getPlaceGrid()[0][0]!=null)
							{
								if(up.getRoadEnds().size()>0)
								{
									for(FeatureEnd r:up.getRoadEnds())
									{
										if(r.getDirection().equals("R"))
											return null;
									}
								}
								if(up.getCityEnds().size()>0)
								{
									for(FeatureEnd r:up.getCityEnds())
									{
										if(r.getDirection().equals("R"))
											return null;
									}
								}
							}
						}
						if(mX+1<31)
						{
							Tile up=gridTiles[mY][mX+1];
							if(up.getPlaceGrid()[0][0]!=null)
							{
								if(up.getRoadEnds().size()>0)
								{
									for(FeatureEnd r:up.getRoadEnds())
									{
										if(r.getDirection().equals("L"))
											return null;
									}
								}
								if(up.getCityEnds().size()>0)
								{
									for(FeatureEnd r:up.getCityEnds())
									{
										if(r.getDirection().equals("L"))
											return null;
									}
								}
							}
						}
					}
					System.out.println("mainRiver.getNeededLocation: "+mainRiver.getNeededLocation());
					if(gridTiles[mY][mX].getPlaceGrid()[0][0]==null)
					{
						possibleLocs.add(mainRiver.getNeededLocation());
						return possibleLocs;
					}
					else
						return null;
				}
			}
			//System.out.println("GameState mainRiver.checkOpp: "+mainRiver.checkOpp(tileRiver)+" , !makingUTurn: "+!mainRiver.makingUTurn(tileRiver));
			if(mainRiver.checkOpp(tileRiver) && !mainRiver.makingUTurn(tileRiver))
			{
				FeatureEnd start=t.getRiver().getStartLocation();
				FeatureEnd end=t.getRiver().getEndLocation();
				Boolean isValid=true;
				ArrayList<FeatureEnd> roadEnds=t.getRoadEnds();
				ArrayList<FeatureEnd> cityEnds=t.getCityEnds();
				if(roadEnds.size()>0)
				{
					for(FeatureEnd f:roadEnds)
					{
						if(f.getDirection().equals("U"))
						{
							//just ignore the name upTile, I got lazy
							if((mainRiver.getNeededLocation().getY()-1+height)>-1&&(mainRiver.getNeededLocation().getY()-1+height)<31&&(mainRiver.getNeededLocation().getX()+width)<31&&(mainRiver.getNeededLocation().getX()+width)>-1)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getRoadEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getRoadEnds())
										{
											if(fU.getDirection().equals("D"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
										return null;
								}
								
							}
						}
						if(f.getDirection().equals("D"))
						{
							if((mainRiver.getNeededLocation().getY()+1+height)>-1&&mainRiver.getNeededLocation().getY()+1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getRoadEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getRoadEnds())
										{
											if(fU.getDirection().equals("U"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
						if(f.getDirection().equals("L"))
						{
							if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()-1+width>-1&&mainRiver.getNeededLocation().getX()-1+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()-1+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getRoadEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getRoadEnds())
										{
											if(fU.getDirection().equals("R"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
						if(f.getDirection().equals("R"))
						{
							if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()+1+width>-1&&mainRiver.getNeededLocation().getX()+1+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()+1+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getRoadEnds().size()>0)
									{							
										isValid=false;
										for(FeatureEnd fU:upTile.getRoadEnds())
										{
											if(fU.getDirection().equals("L"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
					}
				}
				if(cityEnds.size()>0)
				{
					for(FeatureEnd f:cityEnds)
					{
						if(f.getDirection().equals("U"))
						{
							if(mainRiver.getNeededLocation().getY()-1+height>-1&&mainRiver.getNeededLocation().getY()-1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getCityEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getCityEnds())
										{
											if(fU.getDirection().equals("D"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
						if(f.getDirection().equals("D"))
						{
							if(mainRiver.getNeededLocation().getY()+1+height>-1&&mainRiver.getNeededLocation().getY()+1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getCityEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getCityEnds())
										{
											if(fU.getDirection().equals("U"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
						if(f.getDirection().equals("L"))
						{
							if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()-1+width>-1&&mainRiver.getNeededLocation().getX()-1+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()-1+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getCityEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getCityEnds())
										{
											if(fU.getDirection().equals("R"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
						if(f.getDirection().equals("R"))
						{
							if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()+1+width>-1&&mainRiver.getNeededLocation().getX()+1+width<31)
							{
								Tile upTile=gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()+1+width];
								if(upTile.getPlaceGrid()[0][0]!=null)
								{
									if(upTile.getCityEnds().size()>0)
									{
										isValid=false;
										for(FeatureEnd fU:upTile.getCityEnds())
										{
											if(fU.getDirection().equals("L"))
												isValid=true;
										}
										if(!isValid)
											return null;
									}
									else
									{
										return null;
									}
								}
							}
						}
					}
				}
				if(end.isOpp(mainRiver.getMainEnd()))
				{
					if(start.getDirection().equals("U"))
					{
						if(mainRiver.getNeededLocation().getY()-1+height>-1&&mainRiver.getNeededLocation().getY()-1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempLeft=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width-1];
								Tile tempRight=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width+1];
								if(tempLeft.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "L")));
											isValid=false;
									}
								}
								if(tempLeft.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "L")));
											isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
					else if(start.getDirection().equals("D"))
					{
						if(mainRiver.getNeededLocation().getY()+1+height>-1&&mainRiver.getNeededLocation().getY()+1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempLeft=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width-1];
								Tile tempRight=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width+1];
								if(tempLeft.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "L")));
											isValid=false;
									}
								}
								if(tempLeft.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "L")));
											isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
					else if(start.getDirection().equals("L"))
					{
						if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()-1+width>-1&&mainRiver.getNeededLocation().getX()-1+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()-1+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempUp=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width-1];
								Tile tempDown=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width-1];
								if(tempUp.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempUp.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "D")));
											isValid=false;
									}
								}
								if(tempDown.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempDown.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "U")));
											isValid=false;
									}
								}
								if(tempUp.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempUp.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "D")));
											isValid=false;
									}
								}
								if(tempDown.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempDown.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "U")));
										isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
					else
					{
						if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()+1+width>-1&&mainRiver.getNeededLocation().getX()+1+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()+1+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempUp=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width+1];
								Tile tempDown=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width+1];
								if(tempUp.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempUp.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "D")));
											isValid=false;
									}
								}
								if(tempDown.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempDown.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "U")));
											isValid=false;
									}
								}
								if(tempUp.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempUp.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "D")));
											isValid=false;
									}
								}
								if(tempDown.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempDown.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "U")));
										isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
				}
				else
				{
					if(end.getDirection().equals("U"))
					{
						if(mainRiver.getNeededLocation().getY()-1+height>-1&&mainRiver.getNeededLocation().getY()-1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempLeft=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width-1];
								Tile tempRight=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width+1];
								if(tempLeft.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "L")));
											isValid=false;
									}
								}
								if(tempLeft.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "L")));
											isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
					else if(end.getDirection().equals("D"))
					{
						if(mainRiver.getNeededLocation().getY()+1+height>-1&&mainRiver.getNeededLocation().getY()+1+height<31&&mainRiver.getNeededLocation().getX()+width>-1&&mainRiver.getNeededLocation().getX()+width<31)
						{
							if(gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width].getPlaceGrid()[0][0]==null)
							{
								Tile tempLeft=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width-1];
								Tile tempRight=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width+1];
								if(tempLeft.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getRoadEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getRoadEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "L")));
											isValid=false;
									}
								}
								if(tempLeft.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempLeft.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "R")));
											isValid=false;
									}
								}
								if(tempRight.getCityEnds().size()>0)
								{
									for(FeatureEnd f:tempRight.getCityEnds())
									{
										if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "L")));
											isValid=false;
									}
								}
								if(isValid)
								{
									possibleLocs.add(mainRiver.getNeededLocation());
									return possibleLocs;
								}
								else
									return null;
							}
							else
							{
								return null;
							}
						}
					}
					else if(end.getDirection().equals("L"))
					{
						if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()-1+width>-1&&mainRiver.getNeededLocation().getX()-1+width<31)
						{
						if(gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()-1+width].getPlaceGrid()[0][0]==null)
						{
							Tile tempUp=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width-1];
							Tile tempDown=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width-1];
							if(tempUp.getRoadEnds().size()>0)
							{
								for(FeatureEnd f:tempUp.getRoadEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "D")));
										isValid=false;
								}
							}
							if(tempDown.getRoadEnds().size()>0)
							{
								for(FeatureEnd f:tempDown.getRoadEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "U")));
										isValid=false;
								}
							}
							if(tempUp.getCityEnds().size()>0)
							{
								for(FeatureEnd f:tempUp.getCityEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()-1), "D")));
										isValid=false;
								}
							}
							if(tempDown.getCityEnds().size()>0)
							{
								for(FeatureEnd f:tempDown.getCityEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()-1, mainRiver.getNeededLocation().getY()+1), "U")));
									isValid=false;
								}
							}
							if(isValid)
							{
								possibleLocs.add(mainRiver.getNeededLocation());
								return possibleLocs;
							}
							else
								return null;
						}
						}
					}	
					else if(end.getDirection().equals("R"))
					{
						if(mainRiver.getNeededLocation().getY()+height>-1&&mainRiver.getNeededLocation().getY()+height<31&&mainRiver.getNeededLocation().getX()+1+width>-1&&mainRiver.getNeededLocation().getX()+1+width<31)
						{
						if(gridTiles[mainRiver.getNeededLocation().getY()+height][mainRiver.getNeededLocation().getX()+1+width].getPlaceGrid()[0][0]==null)
						{
							Tile tempUp=gridTiles[mainRiver.getNeededLocation().getY()-1+height][mainRiver.getNeededLocation().getX()+width+1];
							Tile tempDown=gridTiles[mainRiver.getNeededLocation().getY()+1+height][mainRiver.getNeededLocation().getX()+width+1];
							if(tempUp.getRoadEnds().size()>0)
							{
								for(FeatureEnd f:tempUp.getRoadEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "D")));
										isValid=false;
								}
							}
							if(tempDown.getRoadEnds().size()>0)
							{
								for(FeatureEnd f:tempDown.getRoadEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "U")));
										isValid=false;
								}
							}
							if(tempUp.getCityEnds().size()>0)
							{
								for(FeatureEnd f:tempUp.getCityEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()-1), "D")));
										isValid=false;
								}
							}
							if(tempDown.getCityEnds().size()>0)
							{
								for(FeatureEnd f:tempDown.getCityEnds())
								{
									if(f.equals(new FeatureEnd(new Location(mainRiver.getNeededLocation().getX()+1, mainRiver.getNeededLocation().getY()+1), "U")));
									isValid=false;
								}
							}
							if(isValid)
							{
								possibleLocs.add(mainRiver.getNeededLocation());
								return possibleLocs;
							}
							else
								return null;
						}
						else
						{
							return null;
						}
						}
					}
					int i=mainRiver.getNeededLocation().getX();
					int j=mainRiver.getNeededLocation().getY();
					System.out.println("mainRiver.getNeededLocation: "+mainRiver.getNeededLocation());
					System.out.println("i: "+i+", j: "+j);
					System.out.println("width: "+width+", height:"+height);
					if(gridTiles[j+height][i+width].getPlaceGrid()[0][0]==null)
					{
						System.out.println("Untaken Tile: "+gridTiles[j+height][i+width]);
						possibleLocs.add(mainRiver.getNeededLocation());
						return possibleLocs;
					}
					else
					{
						System.out.println("Taken Tile:"+gridTiles[j+height][i+width]);
						return null;
					}
				}
			}
			else
			{
				//g.addMessage("Cannot place river tile as is");
				//System.out.println("Cannot place river tile as is");
				return null;
			}
		}
		else
		{
			int gX=0;
			int gY=0;
			for(int i=height; i<height+11; i++)
			{
				for(int y=width; y<width+11; y++)
				{
					if(gridTiles[i][y].getPlaceGrid()[0][0]==null)
					{
						//First check if a tile has cities and if this location is next to another tile with oppAdj cities
						//Check if a tile has roads and if this location is next to another tile with OppAdj roads
						//Check if the location is next to any tiles with not OppAdj cities
						//Check if a tile is next to any tiles with not OppAdj roads
						//Lastly, check if the location is next to any tiles at all
						boolean valid=true;
						if (t.getCityEnds().size()!= 0) {
							
							for (int s = 0; s < t.getCityEnds().size(); s++) {
								if(valid)
								{
									if (t.getCityEnds().get(s).getDirection().equals("U")) {
										if(i!=0)
										{
											Tile t2=gridTiles[i-1][y];
											if(t2.getPlaceGrid()[0][0]!=null)
											{
												ArrayList <FeatureEnd> temp = t2.getCityEnds();
												if(temp.size()==0)
												{
													valid=false;
												}
												else
												{
													valid=false;
													for (int d = 0; d < temp.size(); d ++) {
														if (temp.get(d).getDirection().equals("D")){
															valid = true;
														}
													}
												}
											}
										}
									}
									else if (valid&&t.getCityEnds().get(s).getDirection().equals("L")) {
										if(y!=0)
										{
											Tile t2=gridTiles[i][y-1];
											if(t2.getPlaceGrid()[0][0]!=null)
											{
												ArrayList <FeatureEnd> temp = t2.getCityEnds();
												if(temp.size()==0)
												{
													valid=false;
												}
												else
												{
													valid=false;
													for (int d = 0; d < temp.size(); d ++) {
														if (temp.get(d).getDirection().equals("R")){
															valid = true;
														}
													}
												}
											}
										}
									}
									else if (valid&&t.getCityEnds().get(s).getDirection().equals("R")) {
										if(y!=30)
										{
											Tile t2=gridTiles[i][y+1];
											if(t2.getPlaceGrid()[0][0]!=null)
											{
												ArrayList <FeatureEnd> temp = t2.getCityEnds();
												if(temp.size()==0)
												{
													valid=false;
												}
												else
												{
													valid=false;
													for (int d = 0; d < temp.size(); d ++) {
														if (temp.get(d).getDirection().equals("L")){
															valid = true;
														}
													}
												}
											}
										}
									}
									else if (valid&&t.getCityEnds().get(s).getDirection().equals("D")) {
										if(i!=30)
										{
											Tile t2=gridTiles[i+1][y];
											if(t2.getPlaceGrid()[0][0]!=null)
											{
												ArrayList <FeatureEnd> temp = t2.getCityEnds();
												if(temp.size()==0)
												{
													valid=false;
												}
												else
												{
													valid=false;
													for (int d = 0; d < temp.size(); d ++) {
														if (temp.get(d).getDirection().equals("U")){
															valid = true;
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
						if(valid)
						{
							if (t.getRoadEnds().size()!= 0) {
								
								for (int s = 0; s < t.getRoadEnds().size(); s++) {
									if(valid)
									{
										if (t.getRoadEnds().get(s).getDirection().equals("U")) {
											if(i!=0)
											{
												Tile t2=gridTiles[i-1][y];
												if(t2.getPlaceGrid()[0][0]!=null)
												{
													ArrayList <FeatureEnd> temp = t2.getRoadEnds();
													if(temp.size()==0)
													{
														valid=false;
													}
													else
													{
														valid=false;
														for (int d = 0; d < temp.size(); d ++) {
															if (temp.get(d).getDirection().equals("D")){
																valid = true;
															}
														}
													}
												}
											}
										}
										else if (valid&&t.getRoadEnds().get(s).getDirection().equals("L")) {
											if(y!=0)
											{
												Tile t2=gridTiles[i][y-1];
												if(t2.getPlaceGrid()[0][0]!=null)
												{
													ArrayList <FeatureEnd> temp = t2.getRoadEnds();
													if(temp.size()==0)
													{
														valid=false;
													}
													else
													{
														valid=false;
														for (int d = 0; d < temp.size(); d ++) {
															if (temp.get(d).getDirection().equals("R")){
																valid = true;
															}
														}
													}
												}
											}
										}
										else if (valid&&t.getRoadEnds().get(s).getDirection().equals("R")) {
											if(y!=30)
											{
												Tile t2=gridTiles[i][y+1];
												if(t2.getPlaceGrid()[0][0]!=null)
												{
													ArrayList <FeatureEnd> temp = t2.getRoadEnds();
													if(temp.size()==0)
													{
														valid=false;
													}
													else
													{
														valid=false;
														for (int d = 0; d < temp.size(); d ++) {
															if (temp.get(d).getDirection().equals("L")){
																valid = true;
															}
														}
													}
												}
											}
										}
										else if (valid&&t.getRoadEnds().get(s).getDirection().equals("D")) {
											if(i!=30)
											{
												Tile t2=gridTiles[i+1][y];
												if(t2.getPlaceGrid()[0][0]!=null)
												{
													ArrayList <FeatureEnd> temp = t2.getRoadEnds();
													if(temp.size()==0)
													{
														valid=false;
													}
													else
													{
														valid=false;
														for (int d = 0; d < temp.size(); d ++) {
															if (temp.get(d).getDirection().equals("U")){
																valid = true;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
						if(valid)
						{
							ArrayList<FeatureEnd> tCityEnds=t.getCityEnds();
							if(i!=0)
							{
								Tile check=gridTiles[i-1][y];
								if(check.getPlaceGrid()[0][0]!=null&&check.getCities()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getCityEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("D"))
										{
											valid=false;
											for(int w=0; w<tCityEnds.size(); w++)
											{
												if(tCityEnds.get(w).getDirection().equals("U"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&y!=0)
							{
								Tile check=gridTiles[i][y-1];
								if(check.getPlaceGrid()[0][0]!=null&&check.getCities()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getCityEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("R"))
										{
											valid=false;
											for(int w=0; w<tCityEnds.size(); w++)
											{
												if(tCityEnds.get(w).getDirection().equals("L"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&i!=30)
							{
								Tile check=gridTiles[i+1][y];
								if(check.getPlaceGrid()[0][0]!=null&&check.getCities()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getCityEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("U"))
										{
											valid=false;
											for(int w=0; w<tCityEnds.size(); w++)
											{
												if(tCityEnds.get(w).getDirection().equals("D"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&y!=30)
							{
								Tile check=gridTiles[i][y+1];
								if(check.getPlaceGrid()[0][0]!=null&&check.getCities()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getCityEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("L"))
										{
											valid=false;
											for(int w=0; w<tCityEnds.size(); w++)
											{
												if(tCityEnds.get(w).getDirection().equals("R"))
													valid=true;
											}
										}
									}
								}
							}
						}
						
						if(valid)
						{
							ArrayList<FeatureEnd> tRoadEnds=t.getRoadEnds();
							if(i!=0)
							{
								Tile check=gridTiles[i-1][y];
								if(check.getPlaceGrid()[0][0]!=null&&check.getRoads()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getRoadEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("D"))
										{
											valid=false;
											for(int w=0; w<tRoadEnds.size(); w++)
											{
												if(tRoadEnds.get(w).getDirection().equals("U"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&y!=0)
							{
								Tile check=gridTiles[i][y-1];
								if(check.getPlaceGrid()[0][0]!=null&&check.getRoads()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getRoadEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("R"))
										{
											valid=false;
											for(int w=0; w<tRoadEnds.size(); w++)
											{
												if(tRoadEnds.get(w).getDirection().equals("L"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&i!=30)
							{
								Tile check=gridTiles[i+1][y];
								if(check.getPlaceGrid()[0][0]!=null&&check.getRoads()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getRoadEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("U"))
										{
											valid=false;
											for(int w=0; w<tRoadEnds.size(); w++)
											{
												if(tRoadEnds.get(w).getDirection().equals("D"))
													valid=true;
											}
										}
									}
								}
							}
							if(valid&&y!=30)
							{
								Tile check=gridTiles[i][y+1];
								if(check.getPlaceGrid()[0][0]!=null&&check.getRoads()!=null)
								{
									ArrayList<FeatureEnd> ends=check.getRoadEnds();
									for(int q=0; q<ends.size(); q++)
									{
										if(ends.get(q).getDirection().equals("L"))
										{
											valid=false;
											for(int w=0; w<tRoadEnds.size(); w++)
											{
												if(tRoadEnds.get(w).getDirection().equals("R"))
													valid=true;
											}
										}
									}
								}
							}
						}
						
						//for gridTiles[i-1][y], check if i=0
						//for gridTiles[i][y-1], check if y=0, etc
						//for gridTiles[i+1][y], check if i=30, etc for for gridTiles[i][y+1]
						boolean top = true;
						boolean bottom = true;
						boolean left = true;
						boolean right = true;
						if(i!=0){
							if(gridTiles[i-1][y].getPlaceGrid()[0][0]==null)
								left=false;
						}
						if(i != 30) {
							if(gridTiles[i+1][y].getPlaceGrid()[0][0]==null)
								right=false;
							}
						if(y != 0) {
							if(gridTiles[i][y-1].getPlaceGrid()[0][0]==null)
								top=false;
						}
						if(y != 30) {
							if(gridTiles[i][y+1].getPlaceGrid()[0][0]==null)
								bottom=false;
						}
						if((!top||y==0)&&(!bottom||y==30)&&(!left||i==0)&&(!right||i==30)) {
							valid = false;
						}
						if(valid)
							possibleLocs.add(new Location(gY, gX));
					}
					gY++;
				}
				gY=0;
				gX++;
			}
		}
		//cleared rivers, fields, roads, and cities
		return possibleLocs;
	}
	public void addTileToGrid(Tile t, Player p, Location gLoc, int height, int width)
	{
		//Grid, String grid, main everything
		t.setProposedLocation(gLoc);
		t.updateAllLocations(height, width);
		grid.getTiles()[gLoc.getY()+height][gLoc.getX()+width]=t;
		insert2D(stringGrid, t.getPlaceGrid(), (gLoc.getY()+height)*3, (gLoc.getX()+width)*3);
		if(t.getHasMonastery())
		{
			MainMonastery mm=createNewMainMonastery(t);
			Location tGLoc=t.getGridLocation().getGridLoc();
			/*tGLoc.setX(tGLoc.getX()+height);
			tGLoc.setY(tGLoc.getY()+width);*/
			ArrayList<Tile> adjTiles=grid.getAdjTiles(tGLoc, height, width);
			if(adjTiles!=null)
			{
				for(int i=0; i<adjTiles.size(); i++)
				{
					if(adjTiles.get(i).getPlaceGrid()[0][0]!=null)
						mm.add(adjTiles.get(i).getLocation());
				}
			}
			System.out.println("created a new mainMonastery");
			System.out.println("mainMonastery's Locations: ");
			Location[][] mmLocs=mm.getLocations();
			for(int i=0; i<3; i++)
			{
				for(int q=0; q<3; q++)
				{
					if(mmLocs[i][q]!=null)
						System.out.print(mmLocs[i][q]+", ");
					else
						System.out.print("null, ");
				}
				System.out.println();
			}
		}
		for(int i=0; i<mainMonasteries.size(); i++)
		{
			MainMonastery mm=mainMonasteries.get(i);
			Location mmLoc=mm.getTileLoc();
			int minX=mmLoc.getX()-1;
			int minY=mmLoc.getY()-1;
			int maxX=mmLoc.getX()+1;
			int maxY=mmLoc.getY()+1;
			Location tGLoc=t.getLocation();
			if(!mm.contains(tGLoc)&&tGLoc.getX()>=minX && tGLoc.getY()>=minY && tGLoc.getX()<=maxX && tGLoc.getY()<=maxY)
			{
				System.out.println("adding tGLoc: "+tGLoc+" minX: "+minX+", minY: "+minY+", maxX: "+maxX+", maxY: "+maxY);
				mm.add(tGLoc);
				System.out.println("updated mainMonastery's Locations: ");
				Location[][] mmLocs=mm.getLocations();
				for(int z=0; z<3; z++)
				{
					for(int q=0; q<3; q++)
					{
						if(mmLocs[z][q]!=null)
							System.out.print(mmLocs[z][q]+", ");
						else
							System.out.print("null, ");
					}
					System.out.println();
				}
			}
		}
		if(t.getRiver()!=null)
		{
			addToMainRiver(t);
			System.out.println("added to mainRiver");
		}
		if(t.getCities()!=null)
		{
			for(int z=0; z<t.getCities().size(); z++)
			{
				ArrayList<MainCity> adjMainCities=new ArrayList<>();
				City c=t.getCities().get(z);
				for(int q=0; q<c.getFeatureEnds().size(); q++)
				{
					FeatureEnd f=c.getFeatureEnds().get(q);
					for(int i=0; i<mainCities.size(); i++)
					{
						MainCity m=mainCities.get(i);
						for(int y=0; y<m.getFeatureEnds().size(); y++)
						{
							if(m.getFeatureEnds().get(y).isOppAdj(f))
							{
								if(!adjMainCities.contains(m))
									adjMainCities.add(m);
							}
						}
					}
				}
				if(adjMainCities.size()>0)
				{
					if(adjMainCities.size()==1)
					{
						addToMainCity(c, adjMainCities.get(0), t);
						System.out.println("added to a mainCity");
					}
					else
					{
						ArrayList<MainCity> otherAdjMC=new ArrayList<>();
						for(int i=1; i<adjMainCities.size(); i++)
						{
							otherAdjMC.add(adjMainCities.get(i));
						}
						adjMainCities.get(0).combine(c, otherAdjMC, mainFields, adjMainCities.get(0), t);
						System.out.println("combined "+adjMainCities.size()+" mainCities");
						for(int i=0; i<otherAdjMC.size(); i++)
						{
							mainCities.remove(otherAdjMC.get(i));
						}
					}
				}
				else
				{
					createNewMainCity(c, t);
					System.out.println("created a new mainCity");
				}
			}
		}
		if(t.getRoads()!=null)
		{
			for(int z=0; z<t.getRoads().size(); z++)
			{
				ArrayList<MainRoad> adjMainRoads=new ArrayList<>();
				Road r=t.getRoads().get(z);
				for(int q=0; q<r.getFeatureEnds().size(); q++)
				{
					FeatureEnd rE=r.getFeatureEnds().get(q);
					for(int i=0; i<mainRoads.size(); i++)
					{
						MainRoad MR=mainRoads.get(i);
						for(int y=0; y<MR.getFeatureEnds().size(); y++)
						{
							if(MR.getFeatureEnds().get(y).isOppAdj(rE))
							{
								if(!adjMainRoads.contains(MR))
									adjMainRoads.add(MR);
							}
						}
					}
				}
				if(adjMainRoads.size()>0)
				{
					if(adjMainRoads.size()==1)
					{
						addToMainRoad(r, adjMainRoads.get(0), t);
						System.out.println("added to a mainRoad");
					}
					else
					{
						adjMainRoads.get(0).combine(r, adjMainRoads.get(1), t);
						System.out.println("combined two mainRoads");
						mainRoads.remove(adjMainRoads.get(1));
					}
				}
				else
				{
					createNewMainRoad(r, t);
					System.out.println("created a new mainRoad");
				}
			}
		}
		if(t.getFields()!=null)
		{
			for(int z=0; z<t.getFields().size(); z++)
			{
				ArrayList<MainField> adjMainFields=new ArrayList<>();
				Field f=t.getFields().get(z);
				for(int q=0; q<f.getFeatureEnds().size(); q++)
				{
					FeatureEnd fE=f.getFeatureEnds().get(q);
					for(int i=0; i<mainFields.size(); i++)
					{
						MainField MF=mainFields.get(i);
						for(int y=0; y<MF.getFeatureEnds().size(); y++)
						{
							if(MF.getFeatureEnds().get(y).isOppAdjField(fE))
							{
								if(!adjMainFields.contains(MF))
									adjMainFields.add(MF);
							}
						}
					}
				}
				if(adjMainFields.size()>0)
				{
					if(adjMainFields.size()==1)
					{
						addToMainField(f, adjMainFields.get(0), mainCities);
						System.out.println("added to a mainField");
					}
					else
					{
						ArrayList<MainField> otherAdjMF=new ArrayList<>();
						for(int i=1; i<adjMainFields.size(); i++)
						{
							otherAdjMF.add(adjMainFields.get(i));
						}
						MainField first=adjMainFields.get(0);
						first.combine(f, otherAdjMF, mainCities);
						System.out.println("combined "+adjMainFields.size()+" mainFields");
						System.out.println("otherAdjMF.size(): "+otherAdjMF.size());
						for(int i=0; i<otherAdjMF.size(); i++)
						{
							mainFields.remove(otherAdjMF.get(i));
						}
					}
				}
				else
				{
					createNewMainField(f);
					System.out.println("created a new mainField");
				}
			}
		}
		t.setGridLocation(grid.getGridLocs()[gLoc.getX()][gLoc.getY()]);
	}
	public void incrementCurrentTile()
	{
		if(riverIter.hasNext())
			currentTile=riverIter.next();
		else
		{
			if(deckIter.hasNext())
			{
				currentTile=deckIter.next();
				//System.out.println("IncrementedCurrentTile. CurrentTile is currently:\n"+currentTile.toString());
			}
			else
			{
				endGame=true;
				System.out.println("Game Over");
				allowClickingGrid=false;
				allowClickingMeep=false;
				allowPlacing=false;
				allowDiscarding=false;
				allowClaiming=false;
				allowNotClaiming=false;
				allowRotating=false;
			}
		}
	}
	//does not delete completed mainfeature from its arraylist but instead changes a boolean that checks if it has been scored yet
	// this code does not return the meeple to the player after it has been scored
	public void updateScores() {
		for(MainCity c : mainCities){
			// if the main city is complete and the city has a player, check which player has more meeples on the city
			if(c.isComplete() && c.getPlayers().size() > 0 && c.getHasBeenScored() == false) {
				ArrayList<Meeple> meepArr = new ArrayList<Meeple>();
				meepArr = c.getMeeples();
				int redmeeps = 0;
				int bluemeeps = 0;
				int greenmeeps = 0;
				int yellowmeeps = 0;
				for(Meeple m : meepArr) {
					if(m.getOwner().equals("Player Yellow")) {
						yellowmeeps++;
					}else if(m.getOwner().equals("Player Blue")) {
						bluemeeps++;
					}else if(m.getOwner().equals("Player Red")) {
						redmeeps++;
					}else if(m.getOwner().equals("Player Green")) {
						greenmeeps++;
					}else {
						System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
					}
				}
				System.out.println("There are "+meepArr.size()+"meeples total. "+"There are " + redmeeps + " red Meeples, " + bluemeeps + " blue Meeples, " + greenmeeps + " green Meeples," + yellowmeeps + " yellow meeples");
				// sets x to the largest # of meeples owned by 1 person
				int x = redmeeps;
				if(x <= bluemeeps)
					x = bluemeeps;
				if(x <= greenmeeps)
					x = greenmeeps;
				if(x <= yellowmeeps)
					x = yellowmeeps;
				// if players meeps is equal to the largest number of meeps then they get points
				if(redmeeps == x) {
					gameLog.addMessage("Red gets "+c.getScore()+" pts for their city.");
                    red.setScore(red.getScore() + c.getScore());
                    System.out.println("reds score is now " + red.getScore());
				} 
				if(bluemeeps == x) {
					gameLog.addMessage("Blue gets "+c.getScore()+" for thier city.");
                    blue.setScore(blue.getScore() + c.getScore());
                    System.out.println("blues score is now " + blue.getScore());
				}
				if(greenmeeps == x) {
					gameLog.addMessage("Green gets "+c.getScore()+" pts for their city.");
					green.setScore(green.getScore() + c.getScore());
					System.out.println("greens score is now " + green.getScore());
				}
				if(yellowmeeps == x) {
					gameLog.addMessage("Yellow gets "+c.getScore()+" pts for their city.");
					yellow.setScore(yellow.getScore() + c.getScore());
					System.out.println("yellow score is now " + yellow.getScore());
				}
				c.setHasBeenScored(true);
				for(Meeple m: c.getMeeples())
				{
					m.setUsed(false);
				}
				for(Player p: c.getPlayers())
				{
					p.setchoseCity(false);
				}
				c.getMeeples().clear();
				c.getPlayers().clear();
				for(City city: c.getCities())
				{
					for(int i=0; i<grid.getTiles().length; i++)
					{
						for(int y=0; y<grid.getTiles()[i].length; y++)
						{
							Tile t=grid.getTiles()[i][y];
							if(t.getCities()!=null&&t.getCities().contains(city)&&city.getClickedLoc()!=null)
							{
								t.getClaimed()[city.getClickedLoc().getX()][city.getClickedLoc().getY()]=false;
							}
						}
					}
				}
			}
		}
		for(MainRoad r : mainRoads){
			// if the main road is complete and the road has a player, check which player has more meeples on the city
						if(r.isComplete() && r.getPlayers().size() > 0 && r.getHasBeenScored() == false) {
							ArrayList<Meeple> meepArr = new ArrayList<Meeple>();
							meepArr = r.getMeeples();
							int redmeeps = 0;
							int bluemeeps = 0;
							int greenmeeps = 0;
							int yellowmeeps = 0;
							for(Meeple m : meepArr) {
								if(m.getOwner().equals("Player Yellow")) {
									yellowmeeps++;
								}else if(m.getOwner().equals("Player Blue")) {
									bluemeeps++;
								}else if(m.getOwner().equals("Player Red")) {
									redmeeps++;
								}else if(m.getOwner().equals("Player Green")) {
									greenmeeps++;
								}else {
									System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
								}
							}
							System.out.println("There are " + redmeeps + " red Meeples, " + bluemeeps + " blue Meeples, " + greenmeeps + " green Meeples," + yellowmeeps + " yellow meeples");
							// sets x to the largest # of meeples owned by 1 person
							int x = redmeeps;
							if(x <= bluemeeps)
								x = bluemeeps;
							if(x <= greenmeeps)
								x = greenmeeps;
							if(x <= yellowmeeps)
								x = yellowmeeps;
							// if players meeps is equal to the largest number of meeps then they get points 
							if(redmeeps == x) {
								gameLog.addMessage("Red gets "+r.getScore()+" pts for their road.");
								red.setScore(red.getScore() + r.getScore());
								System.out.println("reds score is now " + red.getScore());
							} 
							if(bluemeeps == x) {
								gameLog.addMessage("Blue gets "+r.getScore()+" pts for their road.");
								blue.setScore(blue.getScore() + r.getScore());
								System.out.println("blues score is now " + blue.getScore());
							}
							if(greenmeeps == x) {
								gameLog.addMessage("Green gets "+r.getScore()+" pts for their road.");
								green.setScore(green.getScore() + r.getScore());
								System.out.println("greens score is now " + green.getScore());
							}
							if(yellowmeeps == x) {
								gameLog.addMessage("Yellow gets "+r.getScore()+" pts for their road.");
								yellow.setScore(yellow.getScore() + r.getScore());
								System.out.println("yellow score is now " + yellow.getScore());
							}
							r.setHasBeenScored(true);
							for(Meeple m: r.getMeeples())
							{
								m.setUsed(false);
								System.out.println("Meeple "+m.getOwner()+" got its used set back to false");
							}
							for(Player p: r.getPlayers())
							{
								p.setchoseCity(false);
							}
							r.getMeeples().clear();
							r.getPlayers().clear();
							for(Road road: r.getRoads())
							{
								for(int i=0; i<grid.getTiles().length; i++)
								{
									for(int y=0; y<grid.getTiles()[i].length; y++)
									{
										Tile t=grid.getTiles()[i][y];
										if(t.getRoads()!=null&&t.getRoads().contains(road)&&road.getClickedLoc()!=null)
										{
											t.getClaimed()[road.getClickedLoc().getX()][road.getClickedLoc().getY()]=false;
										}
									}
								}
							}
						}
		}
		for(MainMonastery m : mainMonasteries) {
			m.checkComplete();
			// if is complete is true and the main monastery has a meeple with an owner then claim monastery and award points
			if(m.getisComplete() && m.getOwner() != null && m.getHasBeenScored() == false) {
					if(m.getOwner().equals("Player Yellow")) {
						yellow.setScore(yellow.getScore() + m.scoreMonastery());
						gameLog.addMessage("Yellow gets " + m.scoreMonastery() + " pts for their monastery");
					}else if(m.getOwner().equals("Player Blue")) {
						blue.setScore(blue.getScore() + m.scoreMonastery());
						gameLog.addMessage("Blue gets " + m.scoreMonastery() + " pts for their monastery");
					}else if(m.getOwner().equals("Player Red")) {
						red.setScore(red.getScore() +m.scoreMonastery());
						gameLog.addMessage("Red gets " + m.scoreMonastery() + " pts for their monastery");
					}else if(m.getOwner().equals("Player Green")) {
						green.setScore(green.getScore() + m.scoreMonastery());
						gameLog.addMessage("Green gets " + m.scoreMonastery() + " pts for their monastery");
					}else {
						System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
					}
					m.setHasBeenScored(true);
					m.getMeep().setUsed(false);
					Tile tile=grid.getTiles()[m.getLocations()[1][1].getX()][m.getLocations()[1][1].getY()];
					tile.getClaimed()[1][1]=false;
			}
		}
		if(endGame)
		{			
			for(MainCity c : mainCities){
				// if the main city is complete and the city has a player, check which player has more meeples on the city
				if(c.getPlayers().size() > 0 && c.getHasBeenScored() == false) {
					ArrayList<Meeple> meepArr = new ArrayList<Meeple>();
					meepArr = c.getMeeples();
					int redmeeps = 0;
					int bluemeeps = 0;
					int greenmeeps = 0;
					int yellowmeeps = 0;
					for(Meeple m : meepArr) {
						if(m.getOwner().equals("Player Yellow")) {
							yellowmeeps++;
						}else if(m.getOwner().equals("Player Blue")) {
							bluemeeps++;
						}else if(m.getOwner().equals("Player Red")) {
							redmeeps++;
						}else if(m.getOwner().equals("Player Green")) {
							greenmeeps++;
						}else {
							System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
						}
					}
					System.out.println("There are "+meepArr.size()+"meeples total. "+"There are " + redmeeps + " red Meeples, " + bluemeeps + " blue Meeples, " + greenmeeps + " green Meeples," + yellowmeeps + " yellow meeples");
					// sets x to the largest # of meeples owned by 1 person
					int x = redmeeps;
					if(x <= bluemeeps)
						x = bluemeeps;
					if(x <= greenmeeps)
						x = greenmeeps;
					if(x <= yellowmeeps)
						x = yellowmeeps;
					// if players meeps is equal to the largest number of meeps then they get points
					if(redmeeps == x) {
						gameLog.addMessage("EndGame: Red gets "+c.getScore()+" pts for their city.");
	                    red.setScore(red.getScore() + c.getScore());
	                    System.out.println("reds score is now " + red.getScore());
					} 
					if(bluemeeps == x) {
						gameLog.addMessage("Blue gets "+c.getScore()+" pts for their city.");
	                    blue.setScore(blue.getScore() + c.getScore());
	                    System.out.println("blues score is now " + blue.getScore());
					}
					if(greenmeeps == x) {
						gameLog.addMessage("Green gets "+c.getScore()+" pts for their city.");
						green.setScore(green.getScore() + c.getScore());
						System.out.println("greens score is now " + green.getScore());
					}
					if(yellowmeeps == x) {
						gameLog.addMessage("Yellow gets "+c.getScore()+" pts for their city.");
						yellow.setScore(yellow.getScore() + c.getScore());
						System.out.println("yellow score is now " + yellow.getScore());
					}
					c.setHasBeenScored(true);
					for(Meeple m: c.getMeeples())
					{
						m.setUsed(false);
					}
					for(Player p: c.getPlayers())
					{
						p.setchoseCity(false);
					}
					c.getMeeples().clear();
					c.getPlayers().clear();
				}
			}
			
			for(MainRoad r : mainRoads){
				// if the main road is complete and the road has a player, check which player has more meeples on the city
				if(r.getPlayers().size() > 0 && r.getHasBeenScored() == false) {
					ArrayList<Meeple> meepArr = new ArrayList<Meeple>();
					meepArr = r.getMeeples();
					int redmeeps = 0;
					int bluemeeps = 0;
					int greenmeeps = 0;
					int yellowmeeps = 0;
					for(Meeple m : meepArr) {
						if(m.getOwner().equals("Player Yellow")) {
							yellowmeeps++;
						}else if(m.getOwner().equals("Player Blue")) {
							bluemeeps++;
						}else if(m.getOwner().equals("Player Red")) {
							redmeeps++;
						}else if(m.getOwner().equals("Player Green")) {
							greenmeeps++;
						}else {
							System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
						}
					}
					System.out.println("There are " + redmeeps + " red Meeples, " + bluemeeps + " blue Meeples, " + greenmeeps + " green Meeples," + yellowmeeps + " yellow meeples");
					// sets x to the largest # of meeples owned by 1 person
					int x = redmeeps;
					if(x <= bluemeeps)
						x = bluemeeps;
					if(x <= greenmeeps)
						x = greenmeeps;
					if(x <= yellowmeeps)
						x = yellowmeeps;
					// if players meeps is equal to the largest number of meeps then they get points 
					if(redmeeps == x) {
						gameLog.addMessage("Red gets "+r.getScore()+" pts for their road.");
						red.setScore(red.getScore() + r.getScore());
						System.out.println("reds score is now " + red.getScore());
					} 
					if(bluemeeps == x) {
						gameLog.addMessage("Blue gets "+r.getScore()+" pts for their road.");
						blue.setScore(blue.getScore() + r.getScore());
						System.out.println("blues score is now " + blue.getScore());
					}
					if(greenmeeps == x) {
						gameLog.addMessage("Green gets "+r.getScore()+" pts for their road.");
						green.setScore(green.getScore() + r.getScore());
						System.out.println("greens score is now " + green.getScore());
					}
					if(yellowmeeps == x) {
						gameLog.addMessage("Yellow gets "+r.getScore()+" pts for their road.");
						yellow.setScore(yellow.getScore() + r.getScore());
						System.out.println("yellow score is now " + yellow.getScore());
					}
					r.setHasBeenScored(true);
					for(Meeple m: r.getMeeples())
					{
						m.setUsed(false);
						System.out.println("Meeple "+m.getOwner()+" got its used set back to false");
					}
					for(Player p: r.getPlayers())
					{
						p.setchoseCity(false);
					}
					r.getMeeples().clear();
					r.getPlayers().clear();
				}
			}
			
			for(MainMonastery m : mainMonasteries) {
				// if is complete is true and the main monastery has a meeple with an owner then claim monastery and award points
				if(m.getOwner() != null && m.getHasBeenScored() == false) {
						if(m.getOwner().equals("Player Yellow")) {
							yellow.setScore(yellow.getScore() + m.scoreMonastery());
							gameLog.addMessage("Yellow gets " + m.scoreMonastery() + " pts for their monastery");
						}else if(m.getOwner().equals("Player Blue")) {
							blue.setScore(blue.getScore() + m.scoreMonastery());
							gameLog.addMessage("Blue gets " + m.scoreMonastery() + " pts for their monastery");
						}else if(m.getOwner().equals("Player Red")) {
							red.setScore(red.getScore() +m.scoreMonastery());
							gameLog.addMessage("Red gets " + m.scoreMonastery() + " pts for their monastery");
						}else if(m.getOwner().equals("Player Green")) {
							green.setScore(green.getScore() + m.scoreMonastery());
							gameLog.addMessage("Green gets " + m.scoreMonastery() + " pts for their monastery");
						}else {
							System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
						}
						m.setHasBeenScored(true);
						m.getMeep().setUsed(false);
				}
			}
			
			for(MainField f : mainFields){
				// if the main road is complete and the road has a player, check which player has more meeples on the city
				if(f.getPlayers().size() > 0&&!f.getHasBeenScored()) {
					ArrayList<Meeple> meepArr = new ArrayList<Meeple>();
					meepArr = f.getMeeples();
					int redmeeps = 0;
					int bluemeeps = 0;
					int greenmeeps = 0;
					int yellowmeeps = 0;
					for(Meeple m : meepArr) {
						if(m.getOwner().equals("Player Yellow")) {
							yellowmeeps++;
						}else if(m.getOwner().equals("Player Blue")) {
							bluemeeps++;
						}else if(m.getOwner().equals("Player Red")) {
							redmeeps++;
						}else if(m.getOwner().equals("Player Green")) {
							greenmeeps++;
						}else {
							System.out.println("Meeple owner "+ m.getOwner() + " is not a valid players name");
						}
					}
					System.out.println("There are " + redmeeps + " red Meeples, " + bluemeeps + " blue Meeples, " + greenmeeps + " green Meeples," + yellowmeeps + " yellow meeples");
					// sets x to the largest # of meeples owned by 1 person
					int x = redmeeps;
					if(x <= bluemeeps)
						x = bluemeeps;
					if(x <= greenmeeps)
						x = greenmeeps;
					if(x <= yellowmeeps)
						x = yellowmeeps;
					// if players meeps is equal to the largest number of meeps then they get points 
					if(redmeeps == x) {
						gameLog.addMessage("Red gets "+f.getScore()+" pts for their field.");
						red.setScore(red.getScore() + f.getScore());
						System.out.println("reds score is now " + red.getScore());
					} 
					if(bluemeeps == x) {
						gameLog.addMessage("Blue gets "+f.getScore()+" pts for their field.");
						blue.setScore(blue.getScore() + f.getScore());
						System.out.println("blues score is now " + blue.getScore());
					}
					if(greenmeeps == x) {
						gameLog.addMessage("Green gets "+f.getScore()+" pts for their field.");
						green.setScore(green.getScore() + f.getScore());
						System.out.println("greens score is now " + green.getScore());
					}
					if(yellowmeeps == x) {
						gameLog.addMessage("Yellow gets "+f.getScore()+" pts for their field.");
						yellow.setScore(yellow.getScore() + f.getScore());
						System.out.println("yellow score is now " + yellow.getScore());
					}
					f.setHasBeenScored(true);
				}
			}
		}
	}
	public boolean canDiscard() {
		if ((noRotation == null && oneRotation == null  && twoRotation == null && threeRotation == null)||(noRotation.size()==0&&oneRotation.size()==0&&twoRotation.size()==0&&threeRotation.size()==0)) {
			incrementCurrentTile();
			return true;
			
		}
		return false;
	}
	
	public ArrayList<Meeple> getUnusedMeeple()
	{
		ArrayList<Meeple> m=new ArrayList<>();
		for(int i=7; i>0; i--)
		{
			if(!currentPlayer.getMeepArr().get(i).getUsed())
			{
				m.add(currentPlayer.getMeepArr().get(i));
				break;
			}
		}
		return m;
	}
	
	public MainMonastery createNewMainMonastery(Tile t)
	{
		//tile's location needs to be set already.
		MainMonastery mm=new MainMonastery(t.getLocation());
		mainMonasteries.add(mm);
		return mm;
	}
	public void createNewMainRoad(Road r, Tile t)
	{
		boolean end1=r.getIsEnd();
		ArrayList<Road> roads=new ArrayList<>();
		roads.add(r);
		mainRoads.add(new MainRoad(end1, false, roads, new ArrayList<Player>(), new ArrayList<Meeple>(), r.getFeatureEnds(), t));
		// missing add method
		//r.add(t.getRoad);
		
	}
	public void createNewMainCity(City c, Tile t)
	{
		int i=0;
		if(c.getHasShield()) {
			 i=1;
		}
		MainCity mc=new MainCity(c, i, c.getFeatureEnds(), t);
		mainCities.add(mc);
		
	}
	public void createNewMainField(Field f)
	{
		ArrayList<MainCity> mC=new ArrayList<>();
		ArrayList<Field> fs=new ArrayList<>();
		fs.add(f);
		if(f.getCities()==null)
		{
			MainField mf = new MainField(f.getFeatureEnds(), mC, fs);
			mainFields.add(mf);
		}
		else
		{
			for(int y=0; y<f.getCities().size(); y++)
			{
				for(int i=0; i<mainCities.size(); i++)
				{
					if(mainCities.get(i).getCities().contains(f.getCities().get(y)))
					{
						mC.add(mainCities.get(i));
						break;
					}
				}
			}
			MainField mf = new MainField(f.getFeatureEnds(), mC, fs);
			mainFields.add(mf);
		}
	}
	public void addToMainRiver(Tile t)
	{
		mainRiver.addRiver(t.getRiver(), gameLog);
	}
	public void addToMainMonastery(Tile t, MainMonastery m)
	{
		m.add(t.getGridLocation().getGridLoc());
	}
	public void addToMainRoad(Road r, MainRoad mr, Tile t)
	{
		 mr.add(r, t);
	}
	public void addToMainCity(City c, MainCity mc, Tile t)
	{
		mc.add(c, t);
	}
	public void addToMainField(Field f, MainField mf, ArrayList<MainCity> mC)
	{
		mf.add(f, mC);
	}
	public Road findIfClickedRoad(Tile t, Location clicked)
	{
		if(t.getRoads()!=null)
		{
			ArrayList<Road> tRoads=t.getRoads();
			for(int i=0; i<tRoads.size(); i++)
			{
				ArrayList<Location> rMeeps=tRoads.get(i).getMeepleLocs();
				for(int y=0; y<rMeeps.size(); i++)
				{
					if(rMeeps.get(y).equals(clicked))
						return tRoads.get(i);
				}
			}
		}
		return null;
	}
	public boolean canClaimRoad(Tile t, Player p, Road r, int z, int x)
	{
		//if true, call addPlayer. If false, just return false.
		if(p.canUseMeeple())
		{
			for(MainRoad mr : mainRoads) {
				for(int i = 0; i < mr.getRoads().size(); i++) {
					if(mr.getRoads().get(i) == r) {
						if(!mr.getHasBeenScored())
						{
							if(mr.getPlayers().size() == 0) {
									if(mr.addPlayer(p, gameLog))
									{
										r.setClickedLoc(z, x);
										return true;
									}
									else
										return false;
							}
							else
							{
								gameLog.addMessage(("This road is already claimed"));
								for(int y=0; y<mr.getPlayers().size(); y++)
								{
									System.out.println(mr.getPlayers().get(y));
								}
							}
						}
						else
						{
							gameLog.addMessage(("This road is already scored"));
						}
					}
				}
			}
			return false;
		}
		else
		{
			gameLog.addMessage(p.toString()+" has no Meeples");
			return false;
		}
	}
	public Field findIfClickedField(Tile t, Location clicked)
	{
		if(t.getFields()!=null)
		{
			ArrayList<Field> tFields=t.getFields();
			for(int i=0; i<tFields.size(); i++)
			{
				ArrayList<Location> rMeeps=tFields.get(i).getMeepleLocs();
				for(int y=0; y<rMeeps.size(); i++)
				{
					if(rMeeps.get(y).equals(clicked))
						return tFields.get(i);
				}
			}
		}
		return null;
	}
	public boolean canClaimField(Tile t, Player p, Field f, Location mLoc)
	{
		if(p.canUseMeeple())
		{
			for(MainField mf : mainFields) {
				for(int i = 0; i < mf.getFields().size(); i++) {
					if(mf.getFields().get(i) == f) {
						if(mf.getPlayers().size() == 0) {
								if(mf.addPlayer(p, gameLog))
									return true;
								else
									return false;
							}
							else {
	    						gameLog.addMessage(("This field is already claimed"));
								for(int z=0; z<mf.getPlayers().size(); z++)
								{
									System.out.println(mf.getPlayers().get(z));
								}
								System.out.println("This mainField has featureEnds: ");
								mf.printFeatureEnds();
							}
						}
					}
			}
			return false;
		}
		else
		{
			gameLog.addMessage(p.toString()+" has no Meeples");
			return false;
		}
	}
	//Iterate through Meeple Grid, Go through field's featureEnds, if the field featureEnd=meepleGrid, return true
	public boolean findIfClickedMonastery(Tile t, Location clicked)
	{
		if(t.getHasMonastery())
		{
			if(clicked.equals(new Location(1, 1)))
				return true;
		}
		return false;
	}
	public boolean canClaimMonastery(Tile t, Player p)
	{
		if(p.canUseMeeple())
		{
			for(MainMonastery m : mainMonasteries) {
				if(m.getTileLoc().equals(t.getLocation())) {
					if(!m.getHasBeenScored())
					{
						if(m.getMeep() == null) {
							if(m.addPlayer(p, gameLog))
									return true;
							else
								return false;
						}
						else {
							gameLog.addMessage(("This monastery is already claimed"));
						}
					}
					else
					{
						gameLog.addMessage(("This monastery is already scored"));
					}
				}
			}
			return false;
		}
		else
		{
			gameLog.addMessage(p.toString()+" has no Meeples");
			return false;
		}
	}
	public boolean canClaimCity(Tile t, Player p, City c, Location mLoc, int q, int x)
	{
		//goes through all main cities
		if(p.canUseMeeple())
		{
			for(MainCity mc : mainCities) {
				// goes through each city in main city
				for(int i = 0; i < mc.getCities().size(); i++) {
					// if the city == the passed in city check its players arraylist
					if(mc.getCities().get(i) == c) {
						if(mc.getHasBeenScored())
						{
							gameLog.addMessage(("This city is already scored"));
						}
						else
						{
							// if player arr list is empty add player is called and it can be claimed
							if(mc.getPlayers().size() == 0) {
								if(mc.addPlayer(p, gameLog))
								{
									c.setClickedLoc(q, x);
									return true;
								}
								else
									return false;
							}
							else {
	    						gameLog.addMessage(("This city is already claimed"));
								for(int z=0; z<mc.getPlayers().size(); z++)
								{
									System.out.println(mc.getPlayers().get(z));
								}
							}
						}
					}
				}
			}
			return false;
		}
		else
		{
			gameLog.addMessage(p.toString()+" has no Meeples");
			return false;
		}
	}
	public String[][] insert2D(String[][] main, String[][] a, int x, int y)
	{
		for(int r=x, i=0; r<x+3 && i<3; r++, i++)
		{
			for(int c=y, j=0; c<y+3 && j<3; c++, j++)
			{
				main[r][c]=a[i][j];
			}
		}
		return main;
	}
	public void randomOrder()
	{
		Collections.shuffle(players);
	}
	public void setPlayers(ArrayList<Player> p)
	{
		players=p;
	}
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	public void setCurrentPlayer(Player p)
	{
		currentPlayer=p;
	}
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	public void incrCurrentPlayer()
	{
		if(!playerIter.hasNext())
		{
			playerIter=players.listIterator();
		}
		currentPlayer=playerIter.next();
	}
	public void setGrid(Grid g)
	{
		grid=g;
	}
	public Grid getGrid()
	{
		return grid;
	}
	public void setStringGrid(String[][] s)
	{
		stringGrid=s;
	}
	public String[][] getStringGrid()
	{
		return stringGrid;
	}
	public void setDeck(Deck d)
	{
		deck=d;
	}
	public Deck getDeck()
	{
		return deck;
	}
	public void setCurrentTile(Tile t)
	{
		currentTile=t;
	}
	public Tile getCurrentTile()
	{
		return currentTile;
	}
	public void setGameLog(GameLog g)
	{
		gameLog=g;
	}
	public GameLog getGameLog()
	{
		return gameLog;
	}
	public void setEndGame(boolean b)
	{
		endGame=b;
	}
	public boolean getEndGame()
	{
		return(endGame);
	}
	public boolean getAllowClickingGrid()
	{
		return(allowClickingGrid);
	}
	public void setAllowClickingGrid(boolean b)
	{
		allowClickingGrid=b;
	}
	public boolean getAllowClickingMeep()
	{
		return(allowClickingMeep);
	}
	public void setAllowClickingMeep(boolean b)
	{
		allowClickingMeep=b;
	}
	public void setAllowPlacing(boolean b)
	{
		allowPlacing=b;
	}
	public boolean getAllowPlacing()
	{
		return (allowPlacing);
	}
	public void setAllowDiscarding(boolean b)
	{
		allowDiscarding=b;
	}
	public boolean getAllowDiscarding()
	{
		return allowDiscarding;
	}
	public void setAllowClaiming(boolean b)
	{
		allowClaiming=b;
	}
	public boolean getAllowClaiming()
	{
		return(allowClaiming);
	}
	public void setAllowNotClaiming(boolean b)
	{
		allowNotClaiming=b;
	}
	public boolean getAllowNotClaiming()
	{
		return(allowNotClaiming);
	}
	public void setAllowRotating(boolean b)
	{
		allowRotating=b;
	}
	public boolean getAllowRotating()
	{
		return(allowRotating);
	}
	public void setMainRiver(MainRiver m)
	{
		mainRiver=m;
	}
	public MainRiver getMainRiver()
	{
		return mainRiver;
	}
	public void setMainCities(ArrayList<MainCity> m)
	{
		mainCities=m;
	}
	public ArrayList<MainCity> getMainCities()
	{
		return mainCities;
	}
	public void setMainRoads(ArrayList<MainRoad> m)
	{
		mainRoads=m;
	}
	public ArrayList<MainRoad> getMainRoads()
	{
		return mainRoads;
	}
	public void setMainFields(ArrayList<MainField> m)
	{
		mainFields=m;
	}
	public ArrayList<MainField> getMainFields()
	{
		return mainFields;
	}
	public ArrayList<MainMonastery> getMainMonasteries()
	{
		return mainMonasteries;
	}
	public void setRotations(ArrayList<Location> no,ArrayList<Location> one,ArrayList<Location> two,ArrayList<Location> three)
	{
		noRotation = no;
		oneRotation = one;
		twoRotation = two;
		threeRotation = three;
	}
	public void setNoRotation(ArrayList<Location> no)
	{
		noRotation = no;
	}
	public void setOneRotation(ArrayList<Location> one)
	{
		oneRotation = one;
	}
	public void setTwoRotation(ArrayList<Location> two)
	{
		twoRotation = two;
	}
	public void setThreeRotation(ArrayList<Location> three)
	{
		threeRotation = three;
	}
	public ArrayList<Location> getNoRotations() {
		return noRotation;
	}
	public ArrayList<Location> getOneRotation(){
		return oneRotation;
	}
	public ArrayList<Location> getTwoRotation() {
		return twoRotation;
	}
	public ArrayList<Location> getThreeRotation(){
		return threeRotation;
	}
	
	/*public void resetIters()
	{
		deck.getTiles().remove(0);
		Collections.shuffle(deck.getTiles());
		deck.getTiles().add(0, deck.getStartingTile());
		System.out.println("EndLoc River at 0: "+deck.getRiverTiles().get(0).getRiver().getEndLocation());
		deck.getRiverTiles().remove(0);
		Tile riverEnd=deck.getRiverTiles().remove(10);
		Collections.shuffle(deck.getRiverTiles());
		deck.getRiverTiles().add(0, deck.getStartingRiver());
		deck.getRiverTiles().add(riverEnd);
		riverIter=deck.getRiverTiles().listIterator();
		deckIter=deck.getTiles().listIterator();
		riverIter.next();
		currentTile=riverIter.next();
	}*/
	
	public void createTiles()
	{
		riverTiles=deck.getRiverTiles();
		Tile rv=riverTiles.get(0);//startingRiverTile and endingRiverTile will be added at the end, they already exist
		rv.setRiver(new River(new FeatureEnd(null, "L"), new FeatureEnd(null, "R")));
		ArrayList<Road> roads1=new ArrayList<>();
		ArrayList<FeatureEnd> ends1=new ArrayList<>();
		ends1.add(new FeatureEnd(null, "D"));
		ArrayList<Location> mLocs1=new ArrayList<>();
		mLocs1.add(new Location(1, 1));
		roads1.add(new Road(true, ends1, mLocs1, null));
		rv.setRoads(roads1);
		ArrayList<City> cities1=new ArrayList<>();
		String[][] pG= {{"W", "W", "W"}, {"RV", "R", "RV"}, {"F", "R", "F"}};
		ArrayList<FeatureEnd> ends2=new ArrayList<>();
		ends2.add(new FeatureEnd(null, "U"));
		ArrayList<Location> mLocs2=new ArrayList<>();
		mLocs2.add(new Location(0, 1));
		City c=new City(pG, false, ends2, mLocs2, null);
		cities1.add(c);
		rv.setCities(cities1);
		ArrayList<Field> fields1=new ArrayList<>();
		ArrayList<FeatureEnd> ends3=new ArrayList<>();
		ends3.add(new FeatureEnd(null, "LU"));
		ArrayList<Location> mLocs3=new ArrayList<>();
		mLocs3.add(new Location(0, 0));
		Field f=new Field(ends3, mLocs3, null, cities1);
		fields1.add(f);
		ArrayList<FeatureEnd> ends4=new ArrayList<>();
		ends4.add(new FeatureEnd(null, "RU"));
		ArrayList<Location> mLocs4=new ArrayList<>();
		mLocs4.add(new Location(0, 2));
		f=new Field(ends4, mLocs4, null, cities1);
		fields1.add(f);
		ArrayList<FeatureEnd> ends5=new ArrayList<>();
		ends5.add(new FeatureEnd(null, "LD"));
		ends5.add(new FeatureEnd(null, "DL"));
		ArrayList<Location> mLocs5=new ArrayList<>();
		mLocs5.add(new Location(2, 0));
		f=new Field(ends5, mLocs5, null, null);
		fields1.add(f);
		ArrayList<FeatureEnd> ends6=new ArrayList<>();
		ends6.add(new FeatureEnd(null, "RD"));
		ends6.add(new FeatureEnd(null, "DR"));
		ArrayList<Location> mLocs6=new ArrayList<>();
		mLocs6.add(new Location(2, 2));
		f=new Field(ends6, mLocs6, null, null);
		fields1.add(f);
		rv.setFields(fields1);
		rv.setHasMonastery(false);
		rv.setPlaceGrid(pG);
		String[][] mG= {{"F", "C", "F"}, {null, "R", null}, {"F", null, "F"}};
		rv.setMeepleGrid(mG);
		
		rv=riverTiles.get(1);
		rv.setRiver(new River(new FeatureEnd(null, "L"), new FeatureEnd(null, "R")));
		ArrayList<FeatureEnd> ends7=new ArrayList<>();
		ArrayList<Location> mLocs7=new ArrayList<>();
		ArrayList<City> cities2=new ArrayList<>();
		rv.setRoads(null);
		String[][] pG2= {{"W", "W", "W"}, {"RV", "RV", "RV"}, {"F", "F", "F"}};
		ends7.add(new FeatureEnd(null, "U"));
		mLocs7.add(new Location(0, 1));
		City c2=new City(pG2, false, ends7, mLocs7, null);
		cities2.add(c2);
		String[][] pG3= {{"F", "F", "F"}, {"RV", "RV", "RV"}, {"W", "W", "W"}};
		ArrayList<FeatureEnd> ends8=new ArrayList<>();
		ends8.add(new FeatureEnd(null, "D"));
		ArrayList<Location> mLocs8=new ArrayList<>();
		mLocs8.add(new Location(2, 1));
		City c3=new City(pG3, false, ends8, mLocs8, null);
		ArrayList<City> cities3=new ArrayList<>();//Now we need an arraylist for the other city on the tile seperately
		cities3.add(c3);
		ArrayList<Field> fields2=new ArrayList<>();
		ArrayList<FeatureEnd> ends9=new ArrayList<>();
		ends9.add(new FeatureEnd(null, "LU"));
		ends9.add(new FeatureEnd(null, "RU"));
		ArrayList<Location> mLocs9=new ArrayList<>();
		mLocs9.add(new Location(0, 0));
		mLocs9.add(new Location(0, 2));
		f=new Field(ends9, mLocs9, null, cities2);
		fields2.add(f);
		ArrayList<FeatureEnd> ends10=new ArrayList<>();
		ends10.add(new FeatureEnd(null, "LD"));
		ends10.add(new FeatureEnd(null, "RD"));
		ArrayList<Location> mLocs10=new ArrayList<>();
		mLocs10.add(new Location(2, 0));
		mLocs10.add(new Location(2, 2));
		f=new Field(ends10, mLocs10, null, cities3);
		fields2.add(f);
		rv.setFields(fields2);
		ArrayList<City> cities4=new ArrayList<>();//We need a new arraylist holding both bc the tile needs to hold both
		cities4.add(c2);
		cities4.add(c3);
		rv.setCities(cities4);
		rv.setHasMonastery(false);
		String[][] pG4= {{"W", "W", "W"}, {"RV", "RV", "RV"}, {"W", "W", "W"}};
		rv.setPlaceGrid(pG4);
		String[][] mG2= {{"F", "C", "F"}, {null, null, null}, {"F", "C", "F"}};
		rv.setMeepleGrid(mG2);
		
		rv=riverTiles.get(2);
		rv.setRiver(new River(new FeatureEnd(null, "U"), new FeatureEnd(null, "D")));
		ArrayList<FeatureEnd> ends11=new ArrayList<>();
		rv.setRoads(null);
		rv.setCities(null);
		ends11.add(new FeatureEnd(null, "UL"));
		ends11.add(new FeatureEnd(null, "LU"));
		ends11.add(new FeatureEnd(null, "LD"));
		ends11.add(new FeatureEnd(null, "DL"));
		ArrayList<Location> mLocs11=new ArrayList<>();
		ArrayList<Field> fields3=new ArrayList<>();
		mLocs11.add(new Location(1, 0));
		f=new Field(ends11, mLocs11, null, null);
		fields3.add(f);
		ArrayList<FeatureEnd> ends12=new ArrayList<>();
		ends12.add(new FeatureEnd(null, "UR"));
		ends12.add(new FeatureEnd(null, "RU"));
		ends12.add(new FeatureEnd(null, "RD"));
		ends12.add(new FeatureEnd(null, "DR"));
		ArrayList<Location> mLocs12=new ArrayList<>();
		mLocs12.add(new Location(1, 2));
		f=new Field(ends12, mLocs12, null, null);
		fields3.add(f);
		rv.setFields(fields3);
		rv.setHasMonastery(false);
		String[][] pG5= {{"F", "RV", "F"}, {"F", "RV", "F"}, {"F", "RV", "F"}};
		rv.setPlaceGrid(pG5);
		String[][] mG3= {{null, null, null}, {"F", null, "F"}, {null, null, null}};
		rv.setMeepleGrid(mG3);
		
		rv=riverTiles.get(3);
		rv.setRiver(new River(new FeatureEnd(null, "L"), new FeatureEnd(null, "D")));
		ArrayList<FeatureEnd> ends13=new ArrayList<>();
		ArrayList<City> cities5=new ArrayList<>();
		rv.setRoads(null);
		String[][] pG6= {{"W", "C", "C"}, {"RV", "W", "C"}, {"F", "RV", "W"}};
		ends13.add(new FeatureEnd(null, "U"));
		ends13.add(new FeatureEnd(null, "R"));
		ArrayList<Location> mLocs13=new ArrayList<>();
		mLocs13.add(new Location(0, 2));
		c=new City(pG6, false, ends13, mLocs13, null);
		cities5.add(c);
		rv.setCities(cities5);
		ArrayList<Field> fields4=new ArrayList<>();
		ArrayList<FeatureEnd> ends14=new ArrayList<>();
		ends14.add(new FeatureEnd(null, "LU"));
		ends14.add(new FeatureEnd(null, "DR"));
		ArrayList<Location> mLocs14=new ArrayList<>();
		mLocs14.add(new Location(2, 2));
		f=new Field(ends14, mLocs14, null, cities5);
		fields4.add(f);
		ArrayList<FeatureEnd> ends14b=new ArrayList<>();
		ends14b.add(new FeatureEnd(null, "LD"));
		ends14b.add(new FeatureEnd(null, "DL"));
		ArrayList<Location> mLocs14b=new ArrayList<>();
		mLocs14b.add(new Location(2, 0));
		f=new Field(ends14b, mLocs14b, null, null);
		fields4.add(f);
		rv.setFields(fields4);
		rv.setHasMonastery(false);
		rv.setPlaceGrid(pG6);
		String[][] mG4= {{null, null, "C"}, {null, null, null}, {"F", null, "F"}};
		rv.setMeepleGrid(mG4);
		
		rv=riverTiles.get(4);
		rv.setRiver(new River(new FeatureEnd(null, "U"), new FeatureEnd(null, "D")));
		ArrayList<FeatureEnd> ends15=new ArrayList<>();
		rv.setRoads(null);
		rv.setCities(null);
		ends15.add(new FeatureEnd(null, "UL"));
		ends15.add(new FeatureEnd(null, "LU"));
		ends15.add(new FeatureEnd(null, "LD"));
		ends15.add(new FeatureEnd(null, "DL"));
		ArrayList<Location> mLocs15=new ArrayList<>();
		ArrayList<Field> fields5=new ArrayList<>();
		mLocs15.add(new Location(1, 0));
		f=new Field(ends15, mLocs15, null, null);
		fields5.add(f);
		ArrayList<FeatureEnd> ends16=new ArrayList<>();
		ends16.add(new FeatureEnd(null, "UR"));
		ends16.add(new FeatureEnd(null, "RU"));
		ends16.add(new FeatureEnd(null, "RD"));
		ends16.add(new FeatureEnd(null, "DR"));
		ArrayList<Location> mLocs16=new ArrayList<>();
		mLocs16.add(new Location(1, 2));
		f=new Field(ends16, mLocs16, null, null);
		fields5.add(f);
		rv.setFields(fields5);
		rv.setHasMonastery(false);
		String[][] pG5a= {{"F", "RV", "F"}, {"F", "RV", "F"}, {"F", "RV", "F"}};
		rv.setPlaceGrid(pG5a);
		String[][] mG3a= {{null, null, null}, {"F", null, "F"}, {null, null, null}};
		rv.setMeepleGrid(mG3a);
		
		rv=riverTiles.get(5);
		rv.setRiver(new River(new FeatureEnd(null, "U"), new FeatureEnd(null, "L")));
		ArrayList<FeatureEnd> ends17=new ArrayList<>();
		ArrayList<Location> mLocs17=new ArrayList<>();
		ArrayList<Field> fields6=new ArrayList<>();
		rv.setRoads(null);
		rv.setCities(null);
		ends17.add(new FeatureEnd(null, "UL"));
		ends17.add(new FeatureEnd(null, "LU"));
		mLocs17.add(new Location(0, 0));
		f=new Field(ends17, mLocs17, null, null);
		fields6.add(f);
		ArrayList<FeatureEnd> ends18=new ArrayList<>();
		ArrayList<Location> mLocs18=new ArrayList<>();
		ends18.add(new FeatureEnd(null, "UR"));
		ends18.add(new FeatureEnd(null, "RU"));
		ends18.add(new FeatureEnd(null, "RD"));
		ends18.add(new FeatureEnd(null, "DL"));
		ends18.add(new FeatureEnd(null, "DR"));
		ends18.add(new FeatureEnd(null, "LD"));
		mLocs18.add(new Location(2, 2));
		f=new Field(ends18, mLocs18, null, null);
		fields6.add(f);
		rv.setFields(fields6);
		rv.setHasMonastery(false);
		String[][] pG7= {{"F", "RV", "F"}, {"RV", "RV", "F"}, {"F", "F", "F"}};
		rv.setPlaceGrid(pG7);
		String[][] mG5= {{"F", null, null}, {null, null, null}, {null, null, "F"}};
		rv.setMeepleGrid(mG5);
		
		rv=riverTiles.get(6);
		rv.setRiver(new River(new FeatureEnd(null, "L"), new FeatureEnd(null, "R")));
		ArrayList<FeatureEnd> ends19=new ArrayList<>();
		ArrayList<Location> mLocs19=new ArrayList<>();
		ArrayList<Field> fields7=new ArrayList<>();
		ArrayList<Road> roads3=new ArrayList<>();
		rv.setCities(null);
		ends19.add(new FeatureEnd(null, "D"));
		mLocs19.add(new Location(2, 1));
		roads3.add(new Road(true, ends19, mLocs19, null));
		rv.setRoads(roads3);
		ArrayList<FeatureEnd> ends20=new ArrayList<>();
		ArrayList<Location> mLocs20=new ArrayList<>();
		ends20.add(new FeatureEnd(null, "UL"));
		ends20.add(new FeatureEnd(null, "UR"));
		ends20.add(new FeatureEnd(null, "LU"));
		ends20.add(new FeatureEnd(null, "RU"));
		mLocs20.add(new Location(0, 0));
		mLocs20.add(new Location(0, 2));
		f=new Field(ends20, mLocs20, null, null);
		fields7.add(f);
		ArrayList<FeatureEnd> ends21=new ArrayList<>();
		ArrayList<Location> mLocs21=new ArrayList<>();
		ends21.add(new FeatureEnd(null, "DL"));
		ends21.add(new FeatureEnd(null, "LD"));
		mLocs21.add(new Location(2, 0));
		f=new Field(ends21, mLocs21, null, null);
		fields7.add(f);
		ArrayList<FeatureEnd> ends22=new ArrayList<>();
		ArrayList<Location> mLocs22=new ArrayList<>();
		ends22.add(new FeatureEnd(null, "DR"));
		ends22.add(new FeatureEnd(null, "RD"));
		mLocs22.add(new Location(2, 2));
		f=new Field(ends22, mLocs22, null, null);
		fields7.add(f);
		rv.setFields(fields7);
		rv.setHasMonastery(true);
		String[][] pG8= {{"F", "F", "F"}, {"RV", "M", "RV"}, {"F", "R", "F"}};
		rv.setPlaceGrid(pG8);
		String[][] mG6= {{"F", null, "F"}, {null, "M", null}, {"F", "R", "F"}};
		rv.setMeepleGrid(mG6);
		
		rv=riverTiles.get(7);
		rv.setRiver(new River(new FeatureEnd(null, "D"), new FeatureEnd(null, "R")));
		ArrayList<FeatureEnd> ends23=new ArrayList<>();
		ArrayList<Location> mLocs23=new ArrayList<>();
		ArrayList<Field> fields8=new ArrayList<>();
		ArrayList<Road> roads4=new ArrayList<>();
		ends23.add(new FeatureEnd(null, "L"));
		ends23.add(new FeatureEnd(null, "U"));
		mLocs23.add(new Location(0, 1));
		mLocs23.add(new Location(1, 0));
		roads4.add(new Road(false, ends23, mLocs23, null));
		rv.setRoads(roads4);
		rv.setCities(null);
		ArrayList<FeatureEnd> ends24=new ArrayList<>();
		ArrayList<Location> mLocs24=new ArrayList<>();
		ends24.add(new FeatureEnd(null, "UL"));
		ends24.add(new FeatureEnd(null, "LU"));
		mLocs24.add(new Location(0, 0));
		f=new Field(ends24, mLocs24, null, null);
		fields8.add(f);
		ArrayList<FeatureEnd> ends25=new ArrayList<>();
		ArrayList<Location> mLocs25=new ArrayList<>();
		ends25.add(new FeatureEnd(null, "DL"));
		ends25.add(new FeatureEnd(null, "LD"));
		ends25.add(new FeatureEnd(null, "UR"));
		ends25.add(new FeatureEnd(null, "RU"));
		mLocs25.add(new Location(0, 2));
		f=new Field(ends25, mLocs25, null, null);
		fields8.add(f);
		ArrayList<FeatureEnd> ends26=new ArrayList<>();
		ArrayList<Location> mLocs26=new ArrayList<>();
		ends26.add(new FeatureEnd(null, "DR"));
		ends26.add(new FeatureEnd(null, "RD"));
		mLocs26.add(new Location(2, 2));
		f=new Field(ends26, mLocs26, null, null);
		fields8.add(f);
		rv.setFields(fields8);
		rv.setHasMonastery(false);
		String[][] pG9= {{"F", "R", "F"}, {"R", "RV", "RV"}, {"F", "RV", "F"}};
		rv.setPlaceGrid(pG9);
		String[][] mG7= {{"F", "R", "F"}, {"R", null, null}, {null, null, "F"}};
		rv.setMeepleGrid(mG7);
		
		rv=riverTiles.get(8);
		rv.setRiver(new River(new FeatureEnd(null, "D"), new FeatureEnd(null, "R")));
		ArrayList<FeatureEnd> ends27=new ArrayList<>();
		ArrayList<Location> mLocs27=new ArrayList<>();
		ArrayList<Field> fields9=new ArrayList<>();
		rv.setRoads(null);
		rv.setCities(null);
		ends27.add(new FeatureEnd(null, "UL"));
		ends27.add(new FeatureEnd(null, "UR"));
		ends27.add(new FeatureEnd(null, "LU"));
		ends27.add(new FeatureEnd(null, "LD"));
		ends27.add(new FeatureEnd(null, "DL"));
		ends27.add(new FeatureEnd(null, "RU"));
		mLocs27.add(new Location(0, 0));
		f=new Field(ends27, mLocs27, null, null);
		fields9.add(f);
		ArrayList<FeatureEnd> ends28=new ArrayList<>();
		ArrayList<Location> mLocs28=new ArrayList<>();
		ends28.add(new FeatureEnd(null, "DR"));
		ends28.add(new FeatureEnd(null, "RD"));
		mLocs28.add(new Location(2, 2));
		f=new Field(ends28, mLocs28, null, null);
		fields9.add(f);
		rv.setFields(fields9);
		rv.setHasMonastery(false);
		String[][] pG10= {{"F", "F", "F"}, {"F", "RV", "RV"}, {"F", "RV", "F"}};
		rv.setPlaceGrid(pG10);
		String[][] mG10= {{"F", null, null}, {null, null, null}, {null, null, "F"}};
		rv.setMeepleGrid(mG10);
		
		rv=riverTiles.get(9);
		rv.setRiver(new River(new FeatureEnd(null, "U"), new FeatureEnd(null, "D")));
		ArrayList<FeatureEnd> ends29=new ArrayList<>();
		ArrayList<Location> mLocs29=new ArrayList<>();
		ArrayList<Field> fields10=new ArrayList<>();
		ArrayList<Road> roads5=new ArrayList<>();
		ends29.add(new FeatureEnd(null, "L"));
		ends29.add(new FeatureEnd(null, "R"));
		mLocs29.add(new Location(1, 1));
		roads5.add(new Road(false, ends29, mLocs29, null));
		rv.setRoads(roads5);
		rv.setCities(null);
		ArrayList<FeatureEnd> ends30=new ArrayList<>();
		ArrayList<Location> mLocs30=new ArrayList<>();
		ends30.add(new FeatureEnd(null, "UL"));
		ends30.add(new FeatureEnd(null, "LU"));
		mLocs30.add(new Location(0, 0));
		f=new Field(ends30, mLocs30, null, null);
		fields10.add(f);
		ArrayList<FeatureEnd> ends31=new ArrayList<>();
		ArrayList<Location> mLocs31=new ArrayList<>();
		ends31.add(new FeatureEnd(null, "LD"));
		ends31.add(new FeatureEnd(null, "DL"));
		mLocs31.add(new Location(2, 0));
		f=new Field(ends31, mLocs31, null, null);
		fields10.add(f);
		ArrayList<FeatureEnd> ends32=new ArrayList<>();
		ArrayList<Location> mLocs32=new ArrayList<>();
		ends32.add(new FeatureEnd(null, "DR"));
		ends32.add(new FeatureEnd(null, "RD"));
		mLocs32.add(new Location(2, 2));
		f=new Field(ends32, mLocs32, null, null);
		fields10.add(f);
		ArrayList<FeatureEnd> ends33=new ArrayList<>();
		ArrayList<Location> mLocs33=new ArrayList<>();
		ends33.add(new FeatureEnd(null, "RU"));
		ends33.add(new FeatureEnd(null, "UR"));
		mLocs33.add(new Location(0, 2));
		f=new Field(ends33, mLocs33, null, null);
		fields10.add(f);
		rv.setFields(fields10);
		rv.setHasMonastery(false);
		String[][] pG11= {{"F", "RV", "F"}, {"R", "R", "R"}, {"F", "RV", "F"}};
		rv.setPlaceGrid(pG11);
		String[][] mG11= {{"F", null, "F"}, {null, "R", null}, {"F", null, "F"}};
		rv.setMeepleGrid(mG11);
		deck.shuffleRivers();//once we shuffle, we will add the existing riverTiles.
		deck.getRiverTiles().add(0, deck.getStartingRiver());		
		deck.getRiverTiles().add(deck.getEndingRiver());
		ArrayList<FeatureEnd> startFieldEnd=new ArrayList<>();
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "UL"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "LU"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "LD"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "DL"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "DR"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "RD"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "RU"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "UR"));
		ArrayList<Field> startFields=new ArrayList<>();
		startFields.add(new Field(startFieldEnd, new ArrayList<Location>(), new Location(5, 5), null));
		mainFields.add(new MainField(startFieldEnd, null, startFields));
		
		insert2D(stringGrid, deck.getStartingRiver().getPlaceGrid(), 45, 45);
		mainRiver.addRiver(deck.getStartingRiver().getRiver(), gameLog);
		
		ArrayList<Tile> tiles=deck.getTiles();//similarly, startingTile exists, but isn't added to the beginning till the very end
		Tile temp1=new Tile();
		for(int i=0; i<2; i++)
		{
			ArrayList<FeatureEnd> ends34=new ArrayList<>();
			ArrayList<Location> mLocs34=new ArrayList<>();
			ArrayList<Field> fields11=new ArrayList<>();
			ArrayList<Road> roads6=new ArrayList<>();
			ends34.add(new FeatureEnd(null, "D"));
			mLocs34.add(new Location(2, 1));
			roads6.add(new Road(true, ends34, mLocs34, null));
			ArrayList<FeatureEnd> ends35=new ArrayList<>();
			ArrayList<Location> mLocs35=new ArrayList<>();
			ends35.add(new FeatureEnd(null, "UL"));
			ends35.add(new FeatureEnd(null, "LU"));
			ends35.add(new FeatureEnd(null, "LD"));
			ends35.add(new FeatureEnd(null, "DL"));
			ends35.add(new FeatureEnd(null, "DR"));
			ends35.add(new FeatureEnd(null, "RD"));
			ends35.add(new FeatureEnd(null, "RU"));
			ends35.add(new FeatureEnd(null, "UR"));
			mLocs35.add(new Location(2, 0));
			fields11.add(new Field(ends35, mLocs35, null, null));
			String[][] pG12= {{"F", "F", "F"}, {"F", "M", "F"}, {"F", "R", "F"}};
			String[][] mG12= {{null, null, null}, {null, "M", null}, {"F", "R", null}};
			temp1=new Tile(tilePics.get(0), null, roads6, null, fields11, true, null, pG12, mG12);
			tiles.add(temp1);
		}
		Tile temp2=new Tile();
		for(int i=0; i<4; i++)
		{
			ArrayList<FeatureEnd> ends35=new ArrayList<>();
			ArrayList<Location> mLocs35=new ArrayList<>();
			ArrayList<Field> fields12=new ArrayList<>();
			ends35.add(new FeatureEnd(null, "UL"));
			ends35.add(new FeatureEnd(null, "LU"));
			ends35.add(new FeatureEnd(null, "LD"));
			ends35.add(new FeatureEnd(null, "DL"));
			ends35.add(new FeatureEnd(null, "DR"));
			ends35.add(new FeatureEnd(null, "RD"));
			ends35.add(new FeatureEnd(null, "RU"));
			ends35.add(new FeatureEnd(null, "UR"));
			mLocs35.add(new Location(2, 1));
			fields12.add(new Field(ends35, mLocs35, null, null));
			String[][] pG13= {{"F", "F", "F"}, {"F", "M", "F"}, {"F", "F", "F"}};
			String[][] mG13= {{null, null, null}, {null, "M", null}, {null, "F", null}};
			temp2=new Tile(tilePics.get(1), null, null, null, fields12, true, null, pG13, mG13);
			tiles.add(temp2);
		}
		Tile temp3=new Tile();
		ArrayList<FeatureEnd> ends36=new ArrayList<>();
		ArrayList<Location> mLocs36=new ArrayList<>();
		ArrayList<City> cities7=new ArrayList<>();
		String[][] cG= {{"C", "C", "C"}, {"C", "C", "C"}, {"C", "C", "C"}};
		ends36.add(new FeatureEnd(null, "U"));
		ends36.add(new FeatureEnd(null, "L"));
		ends36.add(new FeatureEnd(null, "R"));
		ends36.add(new FeatureEnd(null, "D"));
		mLocs36.add(new Location(1, 1));
		cities7.add(new City(cG, true, ends36, mLocs36, null));
		String[][] pG13= {{"C", "C", "C"}, {"C", "C", "C"}, {"C", "C", "C"}};
		String[][] mG13= {{null, null, null}, {null, "C", null}, {null, null, null}};
		temp3=new Tile(tilePics.get(2), null, null, cities7, null, false, null, pG13, mG13);
		tiles.add(temp3);
		
		Tile temp4=new Tile();
		for(int i=0; i<3; i++)
		{
			ArrayList<FeatureEnd> ends37=new ArrayList<>();
			ArrayList<Location> mLocs37=new ArrayList<>();
			ArrayList<Field> fields14=new ArrayList<>();
			ArrayList<Road> roads7=new ArrayList<>();
			ArrayList<City> cities8=new ArrayList<>();
			ends37.add(new FeatureEnd(null, "L"));
			ends37.add(new FeatureEnd(null, "R"));
			mLocs37.add(new Location(1, 1));
			roads7.add(new Road(false, ends37, mLocs37, null));
			String[][] pg3= {{"W", "W", "W"}, {"R", "R", "R"}, {"F", "F", "F"}};//taken from deck
			ArrayList<FeatureEnd> ends38=new ArrayList<>();
			ArrayList<Location> mLocs38=new ArrayList<>();
			ends38.add(new FeatureEnd(null, "U"));
			mLocs38.add(new Location(0, 1));
			String[][] pg3a= {{"W", "W", "W"}, {"R", "R", "R"}, {"F", "F", "F"}};
			cities8.add(new City(pg3a, false, ends38, mLocs38, null));
			ArrayList<FeatureEnd> ends39=new ArrayList<>();
			ArrayList<Location> mLocs39=new ArrayList<>();
			ends39.add(new FeatureEnd(null, "LU"));
			ends39.add(new FeatureEnd(null, "RU"));
			mLocs39.add(new Location(0, 0));
			mLocs39.add(new Location(0, 2));
			fields14.add(new Field(ends39, mLocs39, null, cities8));
			ArrayList<FeatureEnd> ends40=new ArrayList<>();
			ArrayList<Location> mLocs40=new ArrayList<>();
			ends40.add(new FeatureEnd(null, "LD"));
			ends40.add(new FeatureEnd(null, "DL"));
			ends40.add(new FeatureEnd(null, "DR"));
			ends40.add(new FeatureEnd(null, "RD"));
			mLocs40.add(new Location(2, 1));
			fields14.add(new Field(ends40, mLocs40, null, null));
			String[][] mg3= {{"F", "C", "F"}, {null, "R", null}, {null, "F", null}};
			temp4=new Tile(startT, null, roads7, cities8, fields14, false, null, pg3, mg3);
			tiles.add(temp4);
		}
		Tile temp5=new Tile();
		for(int i=0; i<5; i++)
		{
			ArrayList<FeatureEnd> ends41=new ArrayList<>();
			ArrayList<Location> mLocs41=new ArrayList<>();
			ArrayList<Field> fields15=new ArrayList<>();
			ArrayList<City> cities9=new ArrayList<>();
			String[][] pG14= {{"W", "W", "W"}, {"F", "F", "F"}, {"F", "F", "F"}};//taken from deck
			ends41.add(new FeatureEnd(null, "U"));
			mLocs41.add(new Location(0, 1));
			String[][] pG14a= {{"W", "W", "W"}, {"F", "F", "F"}, {"F", "F", "F"}};
			cities9.add(new City(pG14a, false, ends41, mLocs41, null));//same ends as roads
			ArrayList<FeatureEnd> ends42=new ArrayList<>();
			ArrayList<Location> mLocs42=new ArrayList<>();
			ends42.add(new FeatureEnd(null, "LU"));
			ends42.add(new FeatureEnd(null, "LD"));
			ends42.add(new FeatureEnd(null, "DL"));
			ends42.add(new FeatureEnd(null, "DR"));
			ends42.add(new FeatureEnd(null, "RD"));
			ends42.add(new FeatureEnd(null, "RL"));
			mLocs42.add(new Location(1, 1));
			fields15.add(new Field(ends42, mLocs42, null, cities9));
			String[][] mG14= {{null, "C", null}, {null, "F", null}, {null, null, null}};
			temp5=new Tile(tilePics.get(3), null, null, cities9, fields15, false, null, pG14, mG14);
			tiles.add(temp5);
		}
		Tile temp6=new Tile();
		for(int i=0;i<2; i++)
		{
			ArrayList<FeatureEnd> ends43=new ArrayList<>();
			ArrayList<Location> mLocs43=new ArrayList<>();
			ArrayList<Field> fields16=new ArrayList<>();
			ArrayList<City> cities10=new ArrayList<>();
			String[][] pG15= {{"W", "W", "W"}, {"C", "C", "C"}, {"W", "W", "W"}};
			String[][] mG15= {{null, "F", null}, {null, "C", null}, {null, "F", null}};
			ends43.add(new FeatureEnd(null, "L"));
			ends43.add(new FeatureEnd(null, "R"));
			mLocs43.add(new Location(1, 1));
			String[][] pG15a= {{"W", "W", "W"}, {"C", "C", "C"}, {"W", "W", "W"}};
			cities10.add(new City(pG15a, true, ends43, mLocs43, null));
			ArrayList<FeatureEnd> ends44=new ArrayList<>();
			ArrayList<Location> mLocs44=new ArrayList<>();
			ends44.add(new FeatureEnd(null, "UL"));
			ends44.add(new FeatureEnd(null, "UR"));
			mLocs44.add(new Location(0, 1));
			fields16.add(new Field(ends44, mLocs44, null, cities10));
			ArrayList<FeatureEnd> ends45=new ArrayList<>();
			ArrayList<Location> mLocs45=new ArrayList<>();
			ends45.add(new FeatureEnd(null, "DL"));
			ends45.add(new FeatureEnd(null, "DR"));
			mLocs45.add(new Location(2, 1));
			fields16.add(new Field(ends45, mLocs45, null, cities10));
			temp6=new Tile(tilePics.get(4), null, null, cities10, fields16, false, null, pG15, mG15);
			tiles.add(temp6);
		}
		Tile temp7=new Tile();
		ArrayList<FeatureEnd> ends46=new ArrayList<>();
		ArrayList<Location> mLocs46=new ArrayList<>();
		ArrayList<Field> fields17=new ArrayList<>();
		ArrayList<City> cities11=new ArrayList<>();
		String[][] pG15a= {{"W", "W", "W"}, {"C", "C", "C"}, {"W", "W", "W"}};
		String[][] mG15a= {{null, "F", null}, {null, "C", null}, {null, "F", null}};
		ends46.add(new FeatureEnd(null, "L"));
		ends46.add(new FeatureEnd(null, "R"));
		mLocs46.add(new Location(1, 1));
		String[][] pG15= {{"W", "W", "W"}, {"C", "C", "C"}, {"W", "W", "W"}};
		cities11.add(new City(pG15, false, ends46, mLocs46, null));
		ArrayList<FeatureEnd> ends47=new ArrayList<>();
		ArrayList<Location> mLocs47=new ArrayList<>();
		ends47.add(new FeatureEnd(null, "UL"));
		ends47.add(new FeatureEnd(null, "UR"));
		mLocs47.add(new Location(0, 1));
		fields17.add(new Field(ends47, mLocs47, null, cities11));
		ArrayList<FeatureEnd> ends48=new ArrayList<>();
		ArrayList<Location> mLocs48=new ArrayList<>();
		ends48.add(new FeatureEnd(null, "DL"));
		ends48.add(new FeatureEnd(null, "DR"));
		mLocs48.add(new Location(2, 1));
		fields17.add(new Field(ends48, mLocs48, null, cities11));
		temp7=new Tile(tilePics.get(5), null, null, cities11, fields17, false, null, pG15a, mG15a);
		tiles.add(temp7);
		
		Tile temp8=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends49=new ArrayList<>();
			ArrayList<Location> mLocs49=new ArrayList<>();
			ArrayList<Field> fields18=new ArrayList<>();
			ArrayList<City> cities12=new ArrayList<>();
			String[][] pG16= {{"W", "F", "W"}, {"W", "F", "W"}, {"W", "F", "W"}};
			String[][] mG16= {{null, null, null}, {"C", "F", "C"}, {null, null, null}};
			ends49.add(new FeatureEnd(null, "L"));
			mLocs49.add(new Location(1, 0));
			String[][] pG16a= {{"W", "F", "F"}, {"W", "F", "F"}, {"W", "F", "F"}};
			cities12.add(new City(pG16a, false, ends49, mLocs49, null));
			ArrayList<FeatureEnd> ends50=new ArrayList<>();
			ArrayList<Location> mLocs50=new ArrayList<>();
			ends50.add(new FeatureEnd(null, "R"));
			mLocs50.add(new Location(1, 2));
			String[][] pG16b= {{"F", "F", "W"}, {"F", "F", "W"}, {"F", "F", "W"}};
			cities12.add(new City(pG16b, false, ends50, mLocs50, null));
			ArrayList<FeatureEnd> ends51=new ArrayList<>();
			ArrayList<Location> mLocs51=new ArrayList<>();
			ends51.add(new FeatureEnd(null, "UL"));
			ends51.add(new FeatureEnd(null, "UR"));
			ends51.add(new FeatureEnd(null, "DL"));
			ends51.add(new FeatureEnd(null, "DR"));
			mLocs51.add(new Location(1, 1));
			fields18.add(new Field(ends51, mLocs51, null, cities12));
			temp8=new Tile(tilePics.get(6), null, null, cities12, fields18, false, null, pG16, mG16);
			tiles.add(temp8);
		}
		Tile temp9=new Tile();
		for(int i=0;i<2; i++)
		{
			ArrayList<FeatureEnd> ends52=new ArrayList<>();
			ArrayList<Location> mLocs52=new ArrayList<>();
			ArrayList<Field> fields19=new ArrayList<>();
			ArrayList<City> cities13=new ArrayList<>();
			String[][] pG17= {{"W", "W", "W"}, {"F", "F", "W"}, {"F", "F", "W"}};
			String[][] mG17= {{null, "C", null}, {null, "F", "C"}, {null, null, null}};
			ends52.add(new FeatureEnd(null, "U"));
			mLocs52.add(new Location(0, 1));
			String[][] pG17a= {{"W", "W", "W"}, {"F", "F", "F"}, {"F", "F", "F"}};
			cities13.add(new City(pG17a, false, ends52, mLocs52, null));
			ArrayList<FeatureEnd> ends53=new ArrayList<>();
			ArrayList<Location> mLocs53=new ArrayList<>();
			ends53.add(new FeatureEnd(null, "R"));
			mLocs53.add(new Location(1, 2));
			String[][] pG16b= {{"F", "F", "W"}, {"F", "F", "W"}, {"F", "F", "W"}};
			cities13.add(new City(pG16b, false, ends53, mLocs53, null));
			ArrayList<FeatureEnd> ends54=new ArrayList<>();
			ArrayList<Location> mLocs54=new ArrayList<>();
			ends54.add(new FeatureEnd(null, "LU"));
			ends54.add(new FeatureEnd(null, "LD"));
			ends54.add(new FeatureEnd(null, "DL"));
			ends54.add(new FeatureEnd(null, "DR"));
			mLocs54.add(new Location(1, 1));
			fields19.add(new Field(ends54, mLocs54, null, cities13));
			temp9=new Tile(tilePics.get(7), null, null, cities13, fields19, false, null, pG17, mG17);
			tiles.add(temp9);
		}
		Tile temp10=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends55=new ArrayList<>();
			ArrayList<Location> mLocs55=new ArrayList<>();
			ArrayList<Road> roads8=new ArrayList<>();
			ArrayList<Field> fields20=new ArrayList<>();
			ArrayList<City> cities14=new ArrayList<>();
			String[][] pG18= {{"W", "W", "W"}, {"F", "F", "R"}, {"F", "R", "F"}};
			String[][] mG18= {{null, "C", null}, {"F", "R", null}, {null, null, "F"}};
			ends55.add(new FeatureEnd(null, "D"));
			ends55.add(new FeatureEnd(null, "R"));
			mLocs55.add(new Location(1, 1));
			roads8.add(new Road(false, ends55, mLocs55, null));
			ArrayList<FeatureEnd> ends56=new ArrayList<>();
			ArrayList<Location> mLocs56=new ArrayList<>();
			ends56.add(new FeatureEnd(null, "U"));
			mLocs56.add(new Location(0, 1));
			String[][] pG18a= {{"W", "W", "W"}, {"F", "F", "R"}, {"F", "R", "F"}};
			cities14.add(new City(pG18a, false, ends56, mLocs56, null));
			ArrayList<FeatureEnd> ends57=new ArrayList<>();
			ArrayList<Location> mLocs57=new ArrayList<>();
			ends57.add(new FeatureEnd(null, "LU"));
			ends57.add(new FeatureEnd(null, "LD"));
			ends57.add(new FeatureEnd(null, "DL"));
			ends57.add(new FeatureEnd(null, "RU"));
			mLocs57.add(new Location(1, 0));
			fields20.add(new Field(ends57, mLocs57, null, cities14));
			ArrayList<FeatureEnd> ends58=new ArrayList<>();
			ArrayList<Location> mLocs58=new ArrayList<>();
			ends58.add(new FeatureEnd(null, "DR"));
			ends58.add(new FeatureEnd(null, "RD"));
			mLocs58.add(new Location(2, 2));
			fields20.add(new Field(ends58, mLocs58, null, null));
			temp10=new Tile(tilePics.get(8), null, roads8, cities14, fields20, false, null, pG18, mG18);
			tiles.add(temp10);
		}
		Tile temp11=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends59=new ArrayList<>();
			ArrayList<Location> mLocs59=new ArrayList<>();
			ArrayList<Road> roads9=new ArrayList<>();
			ArrayList<Field> fields21=new ArrayList<>();
			ArrayList<City> cities15=new ArrayList<>();
			String[][] pG19= {{"W", "W", "W"}, {"R", "F", "F"}, {"F", "R", "F"}};
			String[][] mG19= {{null, "C", null}, {null, "R", "F"}, {"F", null, null}};
			ends59.add(new FeatureEnd(null, "L"));
			ends59.add(new FeatureEnd(null, "D"));
			mLocs59.add(new Location(1, 1));
			roads9.add(new Road(false, ends59, mLocs59, null));
			ArrayList<FeatureEnd> ends60=new ArrayList<>();
			ArrayList<Location> mLocs60=new ArrayList<>();
			ends60.add(new FeatureEnd(null, "U"));
			mLocs60.add(new Location(0, 1));
			String[][] pG19a= {{"W", "W", "W"}, {"R", "F", "F"}, {"F", "R", "F"}};
			cities15.add(new City(pG19a, false, ends60, mLocs60, null));
			ArrayList<FeatureEnd> ends61=new ArrayList<>();
			ArrayList<Location> mLocs61=new ArrayList<>();
			ends61.add(new FeatureEnd(null, "LU"));
			ends61.add(new FeatureEnd(null, "DR"));
			ends61.add(new FeatureEnd(null, "RD"));
			ends61.add(new FeatureEnd(null, "RU"));
			mLocs61.add(new Location(1, 2));
			fields21.add(new Field(ends61, mLocs61, null, cities15));
			ArrayList<FeatureEnd> ends62=new ArrayList<>();
			ArrayList<Location> mLocs62=new ArrayList<>();
			ends62.add(new FeatureEnd(null, "DL"));
			ends62.add(new FeatureEnd(null, "LD"));
			mLocs62.add(new Location(2, 0));
			fields21.add(new Field(ends62, mLocs62, null, null));
			temp11=new Tile(tilePics.get(9), null, roads9, cities15, fields21, false, null, pG19, mG19);
			tiles.add(temp11);
		}
		Tile temp12=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends60=new ArrayList<>();
			ArrayList<Location> mLocs60=new ArrayList<>();
			ArrayList<Road> roads10=new ArrayList<>();
			ArrayList<Field> fields22=new ArrayList<>();
			ArrayList<City> cities16=new ArrayList<>();
			String[][] pG20= {{"W", "W", "W"}, {"R", "V", "R"}, {"F", "R", "F"}};
			String[][] mG20= {{"F", "C", "F"}, {"R", null, "R"}, {"F", "R", "F"}};
			ends60.add(new FeatureEnd(null, "L"));
			mLocs60.add(new Location(1, 0));
			roads10.add(new Road(true, ends60, mLocs60, null));
			ArrayList<FeatureEnd> ends61=new ArrayList<>();
			ArrayList<Location> mLocs61=new ArrayList<>();
			ends61.add(new FeatureEnd(null, "R"));
			mLocs61.add(new Location(1, 2));
			roads10.add(new Road(true, ends61, mLocs61, null));
			ArrayList<FeatureEnd> ends62=new ArrayList<>();
			ArrayList<Location> mLocs62=new ArrayList<>();
			ends62.add(new FeatureEnd(null, "D"));
			mLocs62.add(new Location(2, 1));
			roads10.add(new Road(true, ends62, mLocs62, null));
			ArrayList<FeatureEnd> ends63=new ArrayList<>();
			ArrayList<Location> mLocs63=new ArrayList<>();
			ends63.add(new FeatureEnd(null, "U"));
			mLocs63.add(new Location(0, 1));
			String[][] pG20a= {{"W", "W", "W"}, {"R", "V", "R"}, {"F", "R", "F"}};
			cities16.add(new City(pG20a, false, ends63, mLocs63, null));
			ArrayList<FeatureEnd> ends64=new ArrayList<>();
			ArrayList<Location> mLocs64=new ArrayList<>();
			ends64.add(new FeatureEnd(null, "LU"));
			ends64.add(new FeatureEnd(null, "RU"));
			mLocs64.add(new Location(0, 0));
			mLocs64.add(new Location(0, 2));
			fields22.add(new Field(ends64, mLocs64, null, cities16));
			ArrayList<FeatureEnd> ends65=new ArrayList<>();
			ArrayList<Location> mLocs65=new ArrayList<>();
			ends65.add(new FeatureEnd(null, "DL"));
			ends65.add(new FeatureEnd(null, "LD"));
			mLocs65.add(new Location(2, 0));
			fields22.add(new Field(ends65, mLocs65, null, null));
			ArrayList<FeatureEnd> ends66=new ArrayList<>();
			ArrayList<Location> mLocs66=new ArrayList<>();
			ends66.add(new FeatureEnd(null, "DR"));
			ends66.add(new FeatureEnd(null, "RD"));
			mLocs66.add(new Location(2, 2));
			fields22.add(new Field(ends66, mLocs66, null, null));
			temp12=new Tile(tilePics.get(10), null, roads10, cities16, fields22, false, null, pG20, mG20);
			tiles.add(temp12);
		}
		Tile temp13=new Tile();
		for(int i=0;i<2; i++)
		{
			ArrayList<FeatureEnd> ends67=new ArrayList<>();
			ArrayList<Location> mLocs67=new ArrayList<>();
			ArrayList<Field> fields23=new ArrayList<>();
			ArrayList<City> cities17=new ArrayList<>();
			String[][] pG21= {{"W", "C", "C"}, {"F", "W", "C"}, {"F", "F", "W"}};
			String[][] mG21= {{null, null, "C"}, {null, null, null}, {"F", null, null}};
			ends67.add(new FeatureEnd(null, "U"));
			ends67.add(new FeatureEnd(null, "R"));
			mLocs67.add(new Location(0, 2));
			String[][] pG21a= {{"W", "C", "C"}, {"F", "W", "C"}, {"F", "F", "W"}};
			cities17.add(new City(pG21a, true, ends67, mLocs67, null));
			ArrayList<FeatureEnd> ends68=new ArrayList<>();
			ArrayList<Location> mLocs68=new ArrayList<>();
			ends68.add(new FeatureEnd(null, "LU"));
			ends68.add(new FeatureEnd(null, "LD"));
			ends68.add(new FeatureEnd(null, "DL"));
			ends68.add(new FeatureEnd(null, "DR"));
			mLocs68.add(new Location(2, 0));
			fields23.add(new Field(ends68, mLocs68, null, cities17));
			temp13=new Tile(tilePics.get(11), null, null, cities17, fields23, false, null, pG21, mG21);
			tiles.add(temp13);
		}
		Tile temp14=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends69=new ArrayList<>();
			ArrayList<Location> mLocs69=new ArrayList<>();
			ArrayList<Field> fields24=new ArrayList<>();
			ArrayList<City> cities18=new ArrayList<>();
			String[][] pG21a= {{"W", "C", "C"}, {"F", "W", "C"}, {"F", "F", "W"}};
			String[][] mG21a= {{null, null, "C"}, {null, null, null}, {"F", null, null}};
			ends69.add(new FeatureEnd(null, "U"));
			ends69.add(new FeatureEnd(null, "R"));
			mLocs69.add(new Location(0, 2));
			String[][] pG21= {{"W", "C", "C"}, {"F", "W", "C"}, {"F", "F", "W"}};
			cities18.add(new City(pG21, false, ends69, mLocs69, null));
			ArrayList<FeatureEnd> ends70=new ArrayList<>();
			ArrayList<Location> mLocs70=new ArrayList<>();
			ends70.add(new FeatureEnd(null, "LU"));
			ends70.add(new FeatureEnd(null, "LD"));
			ends70.add(new FeatureEnd(null, "DL"));
			ends70.add(new FeatureEnd(null, "DR"));
			mLocs70.add(new Location(2, 0));
			fields24.add(new Field(ends70, mLocs70, null, cities18));
			temp14=new Tile(tilePics.get(12), null, null, cities18, fields24, false, null, pG21a, mG21a);
			tiles.add(temp14);
		}
		Tile temp15=new Tile();
		for(int i=0;i<2; i++)
		{
			ArrayList<FeatureEnd> ends71=new ArrayList<>();
			ArrayList<Location> mLocs71=new ArrayList<>();
			ArrayList<Road> roads11=new ArrayList<>();
			ArrayList<Field> fields25=new ArrayList<>();
			ArrayList<City> cities19=new ArrayList<>();
			String[][] pG23= {{"C", "C", "W"}, {"C", "W", "R"}, {"W", "R", "F"}};
			String[][] mG23= {{"C", null, null}, {null, null, "R"}, {"F", null, "F"}};
			ends71.add(new FeatureEnd(null, "D"));
			ends71.add(new FeatureEnd(null, "R"));
			mLocs71.add(new Location(1, 2));
			roads11.add(new Road(false, ends71, mLocs71, null));
			ArrayList<FeatureEnd> ends72=new ArrayList<>();
			ArrayList<Location> mLocs72=new ArrayList<>();
			ends72.add(new FeatureEnd(null, "U"));
			ends72.add(new FeatureEnd(null, "L"));
			mLocs72.add(new Location(0, 0));
			String[][] pG23a= {{"C", "C", "W"}, {"C", "W", "R"}, {"W", "R", "F"}};
			cities19.add(new City(pG23a, true, ends72, mLocs72, null));
			ArrayList<FeatureEnd> ends73=new ArrayList<>();
			ArrayList<Location> mLocs73=new ArrayList<>();
			ends73.add(new FeatureEnd(null, "DL"));
			ends73.add(new FeatureEnd(null, "RU"));
			mLocs73.add(new Location(2, 0));
			fields25.add(new Field(ends73, mLocs73, null, cities19));
			ArrayList<FeatureEnd> ends74=new ArrayList<>();
			ArrayList<Location> mLocs74=new ArrayList<>();
			ends74.add(new FeatureEnd(null, "DR"));
			ends74.add(new FeatureEnd(null, "RD"));
			mLocs74.add(new Location(2, 2));
			fields25.add(new Field(ends74, mLocs74, null, null));
			temp15=new Tile(tilePics.get(13), null, roads11, cities19, fields25, false, null, pG23, mG23);
			tiles.add(temp15);
		}
		Tile temp16=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends74=new ArrayList<>();
			ArrayList<Location> mLocs74=new ArrayList<>();
			ArrayList<Road> roads12=new ArrayList<>();
			ArrayList<Field> fields26=new ArrayList<>();
			ArrayList<City> cities20=new ArrayList<>();
			String[][] pG23a= {{"C", "C", "W"}, {"C", "W", "R"}, {"W", "R", "F"}};
			String[][] mG23a= {{"C", null, null}, {null, null, "R"}, {"F", null, "F"}};
			ends74.add(new FeatureEnd(null, "D"));
			ends74.add(new FeatureEnd(null, "R"));
			mLocs74.add(new Location(1, 2));
			roads12.add(new Road(false, ends74, mLocs74, null));
			ArrayList<FeatureEnd> ends75=new ArrayList<>();
			ArrayList<Location> mLocs75=new ArrayList<>();
			ends75.add(new FeatureEnd(null, "U"));
			ends75.add(new FeatureEnd(null, "L"));
			mLocs75.add(new Location(0, 0));
			String[][] pG23= {{"C", "C", "W"}, {"C", "W", "R"}, {"W", "R", "F"}};
			cities20.add(new City(pG23, false, ends75, mLocs75, null));
			ArrayList<FeatureEnd> ends76=new ArrayList<>();
			ArrayList<Location> mLocs76=new ArrayList<>();
			ends76.add(new FeatureEnd(null, "DL"));
			ends76.add(new FeatureEnd(null, "RU"));
			mLocs76.add(new Location(2, 0));
			fields26.add(new Field(ends76, mLocs76, null, cities20));
			ArrayList<FeatureEnd> ends77=new ArrayList<>();
			ArrayList<Location> mLocs77=new ArrayList<>();
			ends77.add(new FeatureEnd(null, "DR"));
			ends77.add(new FeatureEnd(null, "RD"));
			mLocs77.add(new Location(2, 2));
			fields26.add(new Field(ends77, mLocs77, null, null));
			temp16=new Tile(tilePics.get(14), null, roads12, cities20, fields26, false, null, pG23a, mG23a);
			tiles.add(temp16);
		}
		Tile temp17=new Tile();
		ArrayList<FeatureEnd> ends78=new ArrayList<>();
		ArrayList<Location> mLocs78=new ArrayList<>();
		ArrayList<Field> fields27=new ArrayList<>();
		ArrayList<City> cities21=new ArrayList<>();
		String[][] pG24= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "F", "W"}};
		String[][] mG24= {{null, "C", null}, {null, null, null}, {null, "F", null}};
		ends78.add(new FeatureEnd(null, "U"));
		ends78.add(new FeatureEnd(null, "L"));
		ends78.add(new FeatureEnd(null, "R"));
		mLocs78.add(new Location(0, 1));
		String[][] pG24a= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "F", "W"}};
		cities21.add(new City(pG24a, true, ends78, mLocs78, null));
		ArrayList<FeatureEnd> ends79=new ArrayList<>();
		ArrayList<Location> mLocs79=new ArrayList<>();
		ends79.add(new FeatureEnd(null, "DL"));
		ends79.add(new FeatureEnd(null, "DR"));
		mLocs79.add(new Location(2, 1));
		fields27.add(new Field(ends79, mLocs79, null, cities21));
		temp17=new Tile(tilePics.get(15), null, null, cities21, fields27, false, null, pG24, mG24);
		tiles.add(temp17);
		
		Tile temp18=new Tile();
		for(int i=0;i<3; i++)
		{
			ArrayList<FeatureEnd> ends80=new ArrayList<>();
			ArrayList<Location> mLocs80=new ArrayList<>();
			ArrayList<Field> fields28=new ArrayList<>();
			ArrayList<City> cities22=new ArrayList<>();
			String[][] pG24b= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "F", "W"}};
			String[][] mG24a= {{null, "C", null}, {null, null, null}, {null, "F", null}};
			ends80.add(new FeatureEnd(null, "U"));
			ends80.add(new FeatureEnd(null, "L"));
			ends80.add(new FeatureEnd(null, "R"));
			mLocs80.add(new Location(0, 1));
			String[][] pG24c= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "F", "W"}};
			cities22.add(new City(pG24c, false, ends80, mLocs80, null));
			ArrayList<FeatureEnd> ends81=new ArrayList<>();
			ArrayList<Location> mLocs81=new ArrayList<>();
			ends81.add(new FeatureEnd(null, "DL"));
			ends81.add(new FeatureEnd(null, "DR"));
			mLocs81.add(new Location(2, 1));
			fields28.add(new Field(ends81, mLocs81, null, cities22));
			temp18=new Tile(tilePics.get(16), null, null, cities22, fields28, false, null, pG24b, mG24a);
			tiles.add(temp18);
		}
		Tile temp19=new Tile();
		for(int i=0;i<2; i++)
		{
			ArrayList<FeatureEnd> ends82=new ArrayList<>();
			ArrayList<Location> mLocs82=new ArrayList<>();
			ArrayList<Road> roads13=new ArrayList<>();
			ArrayList<Field> fields29=new ArrayList<>();
			ArrayList<City> cities23=new ArrayList<>();
			String[][] pG25= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "R", "W"}};
			String[][] mG25= {{null, "C", null}, {null, null, null}, {"F", "R", "F"}};
			ends82.add(new FeatureEnd(null, "D"));
			mLocs82.add(new Location(2, 1));
			roads13.add(new Road(true, ends82, mLocs82, null));
			ArrayList<FeatureEnd> ends83=new ArrayList<>();
			ArrayList<Location> mLocs83=new ArrayList<>();
			ends83.add(new FeatureEnd(null, "U"));
			ends83.add(new FeatureEnd(null, "L"));
			ends83.add(new FeatureEnd(null, "R"));
			mLocs83.add(new Location(0, 1));
			String[][] pG25a= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "R", "W"}};
			cities23.add(new City(pG25a, true, ends83, mLocs83, null));
			ArrayList<FeatureEnd> ends84=new ArrayList<>();
			ArrayList<Location> mLocs84=new ArrayList<>();
			ends84.add(new FeatureEnd(null, "DL"));
			mLocs84.add(new Location(2, 0));
			fields29.add(new Field(ends84, mLocs84, null, cities23));
			ArrayList<FeatureEnd> ends84b=new ArrayList<>();
			ArrayList<Location> mLocs84b=new ArrayList<>();
			ends84b.add(new FeatureEnd(null, "DR"));
			mLocs84b.add(new Location(2, 2));
			fields29.add(new Field(ends84b, mLocs84b, null, cities23));
			temp19=new Tile(tilePics.get(17), null, roads13, cities23, fields29, false, null, pG25, mG25);
			tiles.add(temp19);
		}
		Tile temp20=new Tile();
		ArrayList<FeatureEnd> ends83=new ArrayList<>();
		ArrayList<Location> mLocs83=new ArrayList<>();
		ArrayList<Road> roads14=new ArrayList<>();
		ArrayList<Field> fields30=new ArrayList<>();
		ArrayList<City> cities24=new ArrayList<>();
		String[][] pG25a= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "R", "W"}};
		String[][] mG25a= {{null, "C", null}, {null, null, null}, {"F", "R", "F"}};
		ends83.add(new FeatureEnd(null, "D"));
		mLocs83.add(new Location(2, 1));
		roads14.add(new Road(true, ends83, mLocs83, null));
		ArrayList<FeatureEnd> ends84=new ArrayList<>();
		ArrayList<Location> mLocs84=new ArrayList<>();
		ends84.add(new FeatureEnd(null, "U"));
		ends84.add(new FeatureEnd(null, "L"));
		ends84.add(new FeatureEnd(null, "R"));
		mLocs84.add(new Location(0, 1));
		String[][] pG25= {{"C", "C", "C"}, {"C", "W", "C"}, {"W", "R", "W"}};
		cities24.add(new City(pG25, false, ends84, mLocs84, null));
		ArrayList<FeatureEnd> ends85=new ArrayList<>();
		ArrayList<Location> mLocs85=new ArrayList<>();
		ends85.add(new FeatureEnd(null, "DL"));
		mLocs85.add(new Location(2, 0));
		fields30.add(new Field(ends85, mLocs85, null, cities24));
		ArrayList<FeatureEnd> ends85b=new ArrayList<>();
		ArrayList<Location> mLocs85b=new ArrayList<>();
		ends85b.add(new FeatureEnd(null, "DR"));
		mLocs85b.add(new Location(2, 2));
		fields30.add(new Field(ends85b, mLocs85b, null, cities24));
		temp20=new Tile(tilePics.get(18), null, roads14, cities24, fields30, false, null, pG25a, mG25a);
		tiles.add(temp20);
		
		Tile temp21=new Tile();
		for(int i=0;i<8; i++)
		{
			ArrayList<FeatureEnd> ends86=new ArrayList<>();
			ArrayList<Location> mLocs86=new ArrayList<>();
			ArrayList<Road> roads15=new ArrayList<>();
			ArrayList<Field> fields31=new ArrayList<>();
			String[][] pG26= {{"F", "R", "F"}, {"F", "R", "F"}, {"F", "R", "F"}};
			String[][] mG26= {{null, null, null}, {"F", "R", "F"}, {null, null, null}};
			ends86.add(new FeatureEnd(null, "U"));
			ends86.add(new FeatureEnd(null, "D"));
			mLocs86.add(new Location(1, 1));
			roads15.add(new Road(false, ends86, mLocs86, null));
			ArrayList<FeatureEnd> ends87=new ArrayList<>();
			ArrayList<Location> mLocs87=new ArrayList<>();
			ends87.add(new FeatureEnd(null, "UL"));
			ends87.add(new FeatureEnd(null, "LU"));
			ends87.add(new FeatureEnd(null, "LD"));
			ends87.add(new FeatureEnd(null, "DL"));
			mLocs87.add(new Location(1, 0));
			fields31.add(new Field(ends87, mLocs87, null, null));
			ArrayList<FeatureEnd> ends88=new ArrayList<>();
			ArrayList<Location> mLocs88=new ArrayList<>();
			ends88.add(new FeatureEnd(null, "DR"));
			ends88.add(new FeatureEnd(null, "RD"));
			ends88.add(new FeatureEnd(null, "RU"));
			ends88.add(new FeatureEnd(null, "UR"));
			mLocs88.add(new Location(1, 2));
			fields31.add(new Field(ends88, mLocs88, null, null));
			temp21=new Tile(tilePics.get(19), null, roads15, null, fields31, false, null, pG26, mG26);
			tiles.add(temp21);
		}
		Tile temp22=new Tile();
		for(int i=0;i<9; i++)
		{
			ArrayList<FeatureEnd> ends87=new ArrayList<>();
			ArrayList<Location> mLocs87=new ArrayList<>();
			ArrayList<Road> roads16=new ArrayList<>();
			ArrayList<Field> fields32=new ArrayList<>();
			String[][] pG27= {{"F", "F", "F"}, {"R", "R", "F"}, {"F", "R", "F"}};
			String[][] mG27= {{null, null, "F"}, {"R", null, null}, {"F", "R", null}};
			ends87.add(new FeatureEnd(null, "L"));
			ends87.add(new FeatureEnd(null, "D"));
			mLocs87.add(new Location(1, 0));
			mLocs87.add(new Location(2, 1));
			roads16.add(new Road(false, ends87, mLocs87, null));
			ArrayList<FeatureEnd> ends88=new ArrayList<>();
			ArrayList<Location> mLocs88=new ArrayList<>();
			ends88.add(new FeatureEnd(null, "UL"));
			ends88.add(new FeatureEnd(null, "LU"));
			ends88.add(new FeatureEnd(null, "DR"));
			ends88.add(new FeatureEnd(null, "RD"));
			ends88.add(new FeatureEnd(null, "RU"));
			ends88.add(new FeatureEnd(null, "UR"));
			mLocs88.add(new Location(0, 2));
			fields32.add(new Field(ends88, mLocs88, null, null));
			ArrayList<FeatureEnd> ends89=new ArrayList<>();
			ArrayList<Location> mLocs89=new ArrayList<>();
			ends89.add(new FeatureEnd(null, "LD"));
			ends89.add(new FeatureEnd(null, "DL"));
			mLocs89.add(new Location(2, 0));
			fields32.add(new Field(ends89, mLocs89, null, null));
			temp22=new Tile(tilePics.get(20), null, roads16, null, fields32, false, null, pG27, mG27);
			tiles.add(temp22);
		}
		Tile temp23=new Tile();
		for(int i=0;i<4; i++)
		{
			ArrayList<FeatureEnd> ends88=new ArrayList<>();
			ArrayList<Location> mLocs88=new ArrayList<>();
			ArrayList<Road> roads17=new ArrayList<>();
			ArrayList<Field> fields33=new ArrayList<>();
			String[][] pG28= {{"F", "F", "F"}, {"R", "V", "R"}, {"F", "R", "F"}};
			String[][] mG28= {{null, "F", null}, {"R", null, "R"}, {"F", "R", "F"}};
			ends88.add(new FeatureEnd(null, "L"));
			mLocs88.add(new Location(1, 0));
			roads17.add(new Road(true, ends88, mLocs88, null));
			ArrayList<FeatureEnd> ends89=new ArrayList<>();
			ArrayList<Location> mLocs89=new ArrayList<>();
			ends89.add(new FeatureEnd(null, "R"));
			mLocs89.add(new Location(1, 2));
			roads17.add(new Road(true, ends89, mLocs89, null));
			ArrayList<FeatureEnd> ends90=new ArrayList<>();
			ArrayList<Location> mLocs90=new ArrayList<>();
			ends90.add(new FeatureEnd(null, "D"));
			mLocs90.add(new Location(2, 1));
			roads17.add(new Road(true, ends90, mLocs90, null));
			ArrayList<FeatureEnd> ends91=new ArrayList<>();
			ArrayList<Location> mLocs91=new ArrayList<>();
			ends91.add(new FeatureEnd(null, "UL"));
			ends91.add(new FeatureEnd(null, "LU"));
			ends91.add(new FeatureEnd(null, "RU"));
			ends91.add(new FeatureEnd(null, "UR"));
			mLocs91.add(new Location(0, 1));
			fields33.add(new Field(ends91, mLocs91, null, null));
			ArrayList<FeatureEnd> ends92=new ArrayList<>();
			ArrayList<Location> mLocs92=new ArrayList<>();
			ends92.add(new FeatureEnd(null, "LD"));
			ends92.add(new FeatureEnd(null, "DL"));
			mLocs92.add(new Location(2, 0));
			fields33.add(new Field(ends92, mLocs92, null, null));
			ArrayList<FeatureEnd> ends93=new ArrayList<>();
			ArrayList<Location> mLocs93=new ArrayList<>();
			ends93.add(new FeatureEnd(null, "RD"));
			ends93.add(new FeatureEnd(null, "DR"));
			mLocs93.add(new Location(2, 2));
			fields33.add(new Field(ends93, mLocs93, null, null));
			temp23=new Tile(tilePics.get(21), null, roads17, null, fields33, false, null, pG28, mG28);
			tiles.add(temp23);
		}
		Tile temp24=new Tile();
		ArrayList<FeatureEnd> ends94=new ArrayList<>();
		ArrayList<Location> mLocs94=new ArrayList<>();
		ArrayList<Road> roads18=new ArrayList<>();
		ArrayList<Field> fields34=new ArrayList<>();
		String[][] pG29= {{"F", "R", "F"}, {"R", "V", "R"}, {"F", "R", "F"}};
		String[][] mG29= {{"F", "R", "F"}, {"R", null, "R"}, {"F", "R", "F"}};
		ends94.add(new FeatureEnd(null, "U"));
		mLocs94.add(new Location(0, 1));
		roads18.add(new Road(true, ends94, mLocs94, null));
		ArrayList<FeatureEnd> ends95=new ArrayList<>();
		ArrayList<Location> mLocs95=new ArrayList<>();
		ends95.add(new FeatureEnd(null, "L"));
		mLocs95.add(new Location(1, 0));
		roads18.add(new Road(true, ends95, mLocs95, null));
		ArrayList<FeatureEnd> ends96=new ArrayList<>();
		ArrayList<Location> mLocs96=new ArrayList<>();
		ends96.add(new FeatureEnd(null, "R"));
		mLocs96.add(new Location(1, 2));
		roads18.add(new Road(true, ends96, mLocs96, null));
		ArrayList<FeatureEnd> ends97=new ArrayList<>();
		ArrayList<Location> mLocs97=new ArrayList<>();
		ends97.add(new FeatureEnd(null, "D"));
		mLocs97.add(new Location(2, 1));
		roads18.add(new Road(true, ends97, mLocs97, null));
		ArrayList<FeatureEnd> ends98=new ArrayList<>();
		ArrayList<Location> mLocs98=new ArrayList<>();
		ends98.add(new FeatureEnd(null, "UL"));
		ends98.add(new FeatureEnd(null, "LU"));
		mLocs98.add(new Location(0, 0));
		fields34.add(new Field(ends98, mLocs98, null, null));
		ArrayList<FeatureEnd> ends99=new ArrayList<>();
		ArrayList<Location> mLocs99=new ArrayList<>();
		ends99.add(new FeatureEnd(null, "UR"));
		ends99.add(new FeatureEnd(null, "RU"));
		mLocs99.add(new Location(0, 2));
		fields34.add(new Field(ends99, mLocs99, null, null));
		ArrayList<FeatureEnd> ends100=new ArrayList<>();
		ArrayList<Location> mLocs100=new ArrayList<>();
		ends100.add(new FeatureEnd(null, "LD"));
		ends100.add(new FeatureEnd(null, "DL"));
		mLocs100.add(new Location(2, 0));
		fields34.add(new Field(ends100, mLocs100, null, null));
		ArrayList<FeatureEnd> ends101=new ArrayList<>();
		ArrayList<Location> mLocs101=new ArrayList<>();
		ends101.add(new FeatureEnd(null, "RD"));
		ends101.add(new FeatureEnd(null, "DR"));
		mLocs101.add(new Location(2, 2));
		fields34.add(new Field(ends101, mLocs101, null, null));
		temp24=new Tile(tilePics.get(22), null, roads18, null, fields34, false, null, pG29, mG29);
		tiles.add(temp24);
		
		Collections.shuffle(tiles);
		tiles.add(0, deck.getStartingTile());
		riverIter=deck.getRiverTiles().listIterator();
		deckIter=deck.getTiles().listIterator();
		riverIter.next();
		currentTile=riverIter.next();
	}
	
	public int getTileCount()
	{
		return tileCount;
	}
	
	public void decTileCount()
	{
		tileCount--;
	}
	
	public void setTileCount(int i)
	{
		tileCount=i;
	}
	
	public void reset()
	{
		for(int i=0; i<players.size(); i++)
		{
			Player p=players.get(i);
			for(int q=0; q<p.getMeepArr().size(); q++)
			{
				p.getMeepArr().get(q).setUsed(false);
			}
			p.setScore(0);
			p.setchoseCity(false);
			p.setchoseField(false);
			p.setchoseMonastery(false);
			p.setchoseRoad(false);
		}
		randomOrder();
		deck=new Deck(startR, endR, startT);
		try {
			startR=ImageIO.read(CarcassonnePanel.class.getResource("/Images/r1.jpg"));
			endR=ImageIO.read(CarcassonnePanel.class.getResource("/Images/r12.jpg"));
			startT=ImageIO.read(CarcassonnePanel.class.getResource("/Images/t1.jpg"));
			deck=new Deck(startR, endR, startT);
			for(int i=2; i<12; i++)
			{
				String temp="/Images/r"+i+".jpg";
				BufferedImage pic=ImageIO.read(CarcassonnePanel.class.getResource(temp));
				Tile rv=new Tile(pic);
				deck.getRiverTiles().add(rv);
			}
			//System.out.println("1. river Tiles size: "+deck.getRiverTiles().size());
			for(int i=2; i<25; i++)
			{
				String temp="/Images/t"+i+".jpg";
				BufferedImage pic=ImageIO.read(CarcassonnePanel.class.getResource(temp));
				tilePics.add(pic);//adding all pictures of tiles to an arraylist except for the first one which is already in deck
			}
		}
		catch (Exception e) {
            System.out.print(e);
            System.out.println(" in gameState");
        }
		createTiles();
		setGrid(new Grid());
		setGameLog(new GameLog());
		endGame=false;
		allowClickingGrid=true;
		allowClickingMeep=false;
		allowPlacing=false;
		allowDiscarding=true;
		allowClaiming=false;
		allowNotClaiming=false;
		allowRotating=true;
		mainRiver=new MainRiver();
		mainRiver.addRiver(deck.getStartingRiver().getRiver(), gameLog);
		System.out.println("StartingRiver is "+deck.getStartingRiver().getRiver().getEndLocation()+" after reset");
		System.out.println("MainEnd of River is now "+mainRiver.getMainEnd()+" after reset");
		mainCities=new ArrayList<>();
		mainMonasteries=new ArrayList<>();
		mainRoads=new ArrayList<>();
		mainFields=new ArrayList<>();

		ArrayList<FeatureEnd> startFieldEnd=new ArrayList<>();
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "UL"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "LU"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "LD"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "DL"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "DR"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "RD"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "RU"));
		startFieldEnd.add(new FeatureEnd(new Location(5, 5), "UR"));
		ArrayList<Field> startFields=new ArrayList<>();
		startFields.add(new Field(startFieldEnd, new ArrayList<Location>(), new Location(5, 5), null));
		mainFields.add(new MainField(startFieldEnd, null, startFields));
		
		noRotation=new ArrayList<>();
		oneRotation=new ArrayList<>();
		twoRotation=new ArrayList<>();
		threeRotation=new ArrayList<>();
		tileCount=83;
	}
	//Get and set methods
}

