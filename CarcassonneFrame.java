import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.System.*;
import java.util.ArrayList;

public class CarcassonneFrame extends JFrame implements MouseListener, KeyListener{
    public static int WIDTH = 1920;
    public static int HEIGHT = 1044;
    public CarcassonnePanel p = new CarcassonnePanel();
    int xLoc=-1;
    int yLoc=-1;
    public CarcassonneFrame(String name){
        super(name);
        //JScrollPane scroll = new JScrollPane(p);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        //add(scroll);
        add(p);
        setVisible(true);
        addMouseListener(this);
        addKeyListener(this);
    }
    public void mousePressed(MouseEvent e){

    }
    public void mouseReleased(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        if(p.getMainMenu()) {
            if (x > 650 && x < 1213) {
                if (y > 350 && y < 440) { //New Game
                    p.setBoard(true);
                    p.setInstructions(false);
                    p.setMainMenu(false);
                    p.reset();
            		p.repaint();
                } else if (y > 650 && y < 740) { //Instructions
                    p.setBoard(false);
                    p.setInstructions(true);
                    p.setMainMenu(false);
                }

            }
            if (x > 645 && x < 1208) {
                if (y > 500 && y < 590) { //Load Game
                    p.setBoard(true);
                    p.setInstructions(false);
                    p.setMainMenu(false);
                } else if (y > 800 && y < 890) { //Instructions
                    p.setBoard(false);
                    p.setInstructions(true);
                    p.setMainMenu(false);
                }
            }
        }
        if(p.getBoard()){
            GameState gameState=p.getGameState();
            Point point=new Point(x, y);
            Grid g=gameState.getGrid();
        	if(x>=350&&x<=425&&y>=900&&y<=975)
        	{
        		p.reset();
        		p.repaint();
        	}
            if(gameState.getAllowClickingGrid())
            {
                System.out.println("clicked at point "+x+", "+y);
	            if(g.contains(point))
	            {
		            GridLocation[][] gL=g.getGridLocs();
		            GridLocation found=null;
		            boolean wasFound=false;
		            for(int i=0; i<31; i++)
		            {
		                for(int q=0; q<31; q++)
		                {
		                    if(gL[i][q].contains(point))
		                    {
		                    found=gL[i][q];
		                    wasFound=true;
		                    xLoc=i;
		                    yLoc=q;
		                    System.out.println("clicked on "+i+", "+q);
		                    break;
		                    }
		                }
		                if(wasFound)
		                    break;
		            }
		            if(found!=null)
		            {
		                    Location clickedLoc=new Location(xLoc, yLoc);
		                    Tile current=gameState.getCurrentTile();
		                    current.setProposedLocation(clickedLoc);
		                    System.out.println("set ProposedLocation to "+current.getProposedLocation());
		                    //System.out.println("F1. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
		                    gameState.setAllowPlacing(true);
		                    p.repaint();
		            }
	            }
            }
            else
            {
            	System.out.println("You cannot click on grid yet");
            }
            if(gameState.getAllowClickingMeep())
            {
            	if(gameState.getCurrentTile().getMeepRect().contains(point))
	            {
	            	for(int i=0; i<3; i++)
	            	{
	            		for(int z=0; z<3; z++)
	            		{
	            			if(gameState.getCurrentTile().getMeepRectGrid()[i][z].contains(point))
	            			{
	            				System.out.println("meepRectGrid: "+z+", "+i);
	            				//Go to currentTile's meepleGrid[z][i] and check which string it is. r, c, m, or f
	            				//Depending on the string, call gameState's corresponding canClaimFeatureMethod passing in the correct parameters
	            				//call incrCurrentPlayer
	            				//call incrCurrentTile
	            				//draw the meeple at the right location on the placed tile
	            				Tile t = gameState.getCurrentTile();
	            				String str = t.getMeepleGrid()[z][i];
	            				Tile cT=gameState.getCurrentTile();
	            				Player cP=gameState.getCurrentPlayer();
	            				System.out.println("meepRecGrid contains point, str: "+str);
	            				Location neededLoc=new Location(z, i);
	            				if(str!=null)
	            				{	
		            				if(str.equals("R"))
		            				{
		            					Road neededRoad=null;
		            					System.out.println("Current tile has "+cT.getRoads().size()+" roads.");
		            					for(int q=0; q<cT.getRoads().size(); q++)
		            					{
		            						for(int w=0; w<cT.getRoads().get(q).getMeepleLocs().size(); w++)
		            						{
		            							System.out.println("road's meepLoc: "+cT.getRoads().get(q).getMeepleLocs().get(w)+", neededLoc:"+neededLoc);
		            							if(cT.getRoads().get(q).getMeepleLocs().get(w).equals(neededLoc))
		            							{
		            								neededRoad=cT.getRoads().get(q);
		            							}
		            						}
		            					}
		            					if(neededRoad!=null)
		            					{
			            					boolean b=gameState.canClaimRoad(cT, cP, neededRoad, z, i);
			            					System.out.println("tried to claim a road with meepleLoc "+z+", "+i);
			            					if(b)
			            					{
			            						if(!gameState.getEndGame())
			            						{
					            					cT.setClaimedToTrue(z, i);
					            					ArrayList<Meeple> meeps=cP.getMeepArr();
					            					Meeple m=meeps.get(0);
					            					cT.setMeepleColor(m.getPic());
						                            gameState.incrCurrentPlayer();
	
						            				//System.out.println("F2a Claimed Road. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
						                            gameState.incrementCurrentTile();
	
						            				//System.out.println("F2b Claimed Road. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
						                            gameState.setAllowClaiming(false);
						                            gameState.setAllowClickingGrid(true);
						                            gameState.setAllowClickingMeep(false);
						                            gameState.setAllowDiscarding(true);
						                            gameState.setAllowNotClaiming(false);
						                            gameState.setAllowPlacing(false);
						                            gameState.setAllowRotating(true);
						                            p.setStartOfTurn(true);
						                            gameState.decTileCount();
			            						}
					                            else
					                			{
					            	                gameState.setAllowClaiming(false);
					            	                gameState.setAllowClickingGrid(false);
					            	                gameState.setAllowClickingMeep(false);
					            	                gameState.setAllowDiscarding(false);
					            	                gameState.setAllowNotClaiming(false);
					            	                gameState.setAllowPlacing(false);
					            	                gameState.setAllowRotating(false);
					            	                gameState.setTileCount(0);
					            	                p.setStartOfTurn(true);
					                			}
                                                System.out.println("You had clicked on the grid at "+xLoc+", "+yLoc);
                                                p.repaint();
			            					}
		            					}
		            					else
		            					{
		            						System.out.println("could not find a road at "+z+", "+i+" in meepleGrid");
		            					}
		            				}
		            				if(str.equals("C"))
		            				{
		            					City neededCity=null;
		            					System.out.println("Current tile has "+cT.getCities().size()+" cities.");
		            					for(int q=0; q<cT.getCities().size(); q++)
		            					{
		            						for(int w=0; w<cT.getCities().get(q).getMeepleLocs().size(); w++)
		            						{
		            							if(cT.getCities().get(q).getMeepleLocs().get(w).equals(new Location(z, i)))
		            							{
		            								neededCity=cT.getCities().get(q);
		            							}
		            						}
		            					}
		            					if(neededCity!=null)
		            					{
			            					boolean b=gameState.canClaimCity(cT, cP, neededCity, new Location(z, i), z, i);
			            					System.out.println("tried to claim a city "+z+", "+i);
			            					if(b)
			            					{
			            						if(!gameState.getEndGame())
			            						{
					            					cT.setClaimedToTrue(z, i);
					            					ArrayList<Meeple> meeps=cP.getMeepArr();
					            					Meeple m=meeps.get(0);
					            					cT.setMeepleColor(m.getPic());
						                            gameState.incrCurrentPlayer();
						                            gameState.incrementCurrentTile();
						                            gameState.setAllowClaiming(false);
						                            gameState.setAllowClickingGrid(true);
						                            gameState.setAllowClickingMeep(false);
						                            gameState.setAllowDiscarding(true);
						                            gameState.setAllowNotClaiming(false);
						                            gameState.setAllowPlacing(false);
						                            gameState.setAllowRotating(true);
						                            p.setStartOfTurn(true);
						                            gameState.decTileCount();
			            						}
			            						else
			            		    			{
			            			                gameState.setAllowClaiming(false);
			            			                gameState.setAllowClickingGrid(false);
			            			                gameState.setAllowClickingMeep(false);
			            			                gameState.setAllowDiscarding(false);
			            			                gameState.setAllowNotClaiming(false);
			            			                gameState.setAllowPlacing(false);
			            			                gameState.setAllowRotating(false);
			            			                gameState.setTileCount(0);
			            			                p.setStartOfTurn(true);
			            		    			}
                                                System.out.println("You had clicked on the grid at "+xLoc+", "+yLoc);
					            				p.repaint();
					            				//System.out.println("F2 Claimed City. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
			            					}
		            					}
		            					else
		            					{
		            						System.out.println("could not find a city at "+z+", "+i+" in meepleGrid");
		            					}
		            				}
		            				if(str.equals("F"))
		            				{
		            					Field neededField=null;
		            					System.out.println("Current tile has "+cT.getFields().size()+" fields.");
		            					for(int q=0; q<cT.getFields().size(); q++)
		            					{
		            						for(int w=0; w<cT.getFields().get(q).getMeepleLocs().size(); w++)
		            						{
		            							if(cT.getFields().get(q).getMeepleLocs().get(w).equals(new Location(z, i)))
		            							{
		            								neededField=cT.getFields().get(q);
		            							}
		            						}
		            					}
		            					if(neededField!=null)
		            					{
			            					boolean b=gameState.canClaimField(cT, cP, neededField, new Location(z, i));
			            					System.out.println("tried to claim a field "+z+", "+i);
			            					if(b)
			            					{
			            						if(!gameState.getEndGame())
			            						{
					            					cT.setClaimedToTrue(z, i);
					            					ArrayList<Meeple> meeps=cP.getMeepArr();
					            					Meeple m=meeps.get(0);
					            					cT.setMeepleColor(m.getPic());
					            					cT.setTurned(true);
					            					System.out.println("set Turned to true");
						                            gameState.incrCurrentPlayer();
						                            gameState.incrementCurrentTile();
						                            gameState.setAllowClaiming(false);
						                            gameState.setAllowClickingGrid(true);
						                            gameState.setAllowClickingMeep(false);
						                            gameState.setAllowDiscarding(true);
						                            gameState.setAllowNotClaiming(false);
						                            gameState.setAllowPlacing(false);
						                            gameState.setAllowRotating(true);
						                            p.setStartOfTurn(true);
						                            gameState.decTileCount();
			            						}
					                            else
					                			{
					            	                gameState.setAllowClaiming(false);
					            	                gameState.setAllowClickingGrid(false);
					            	                gameState.setAllowClickingMeep(false);
					            	                gameState.setAllowDiscarding(false);
					            	                gameState.setAllowNotClaiming(false);
					            	                gameState.setAllowPlacing(false);
					            	                gameState.setAllowRotating(false);
					            	                gameState.setTileCount(0);
					            	                p.setStartOfTurn(true);
					                			}
                                                System.out.println("You had clicked on the grid at "+xLoc+", "+yLoc);

					            				p.repaint();
					            				//System.out.println("F2 Claimed Field. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
			            					}
		            					}
		            					else
		            					{
		            						System.out.println("could not find a field at "+z+", "+i+" in meepleGrid");
		            						for(int q=0; q<cT.getFields().size(); q++)
		            						{
			            						System.out.println("Field"+(q+1)+" MeepleLocs: ");
		            							for(int w=0; w<cT.getFields().get(q).getMeepleLocs().size(); w++)
		            							{
			            							System.out.println(cT.getFields().get(q).getMeepleLocs().get(w));
		            							}
		            						}
		            					}
		            				}
		            				if(str.equals("M"))
	            					{
		            					boolean b=gameState.canClaimMonastery(cT, cP);
		            					System.out.println("tried to claim a monastery "+z+", "+i);
		            					if(b)
		            					{
		            						if(!gameState.getEndGame())
		            						{
				            					cT.setClaimedToTrue(z, i);
				            					ArrayList<Meeple> meeps=cP.getMeepArr();
				            					Meeple m=meeps.get(0);
				            					cT.setMeepleColor(m.getPic());
					                            gameState.incrCurrentPlayer();
					                            gameState.incrementCurrentTile();
					                            gameState.setAllowClaiming(false);
					                            gameState.setAllowClickingGrid(true);
					                            gameState.setAllowClickingMeep(false);
					                            gameState.setAllowDiscarding(true);
					                            gameState.setAllowNotClaiming(false);
					                            gameState.setAllowPlacing(false);
					                            gameState.setAllowRotating(true);
					                            p.setStartOfTurn(true);
					                            gameState.decTileCount();
		            						}
				                            else
				                			{
				            	                gameState.setAllowClaiming(false);
				            	                gameState.setAllowClickingGrid(false);
				            	                gameState.setAllowClickingMeep(false);
				            	                gameState.setAllowDiscarding(false);
				            	                gameState.setAllowNotClaiming(false);
				            	                gameState.setAllowPlacing(false);
				            	                gameState.setAllowRotating(false);
				            	                gameState.setTileCount(0);
				            	                p.setStartOfTurn(true);
				                			}
                                            System.out.println("You had clicked on the grid at "+xLoc+", "+yLoc);

				            				p.repaint();
				            				//System.out.println("F2 Claimed Monastery. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
		            					}
	            					}
		            				
	            				}
	            				else
	            				{
	            					System.out.println("You did not click on a feature. ");
	            				}
	            			}
	            		}
	            	}
	            }
            	System.out.println("You clicked on "+point.getX()+", "+point.getY());
            }
            else
            {
                //gameState.getGameLog().addMessage("You cannot click on meeple grid yet");
            	System.out.println("You cannot click on meeple grid yet");
            }
        }
        if(p.getInstructions()){

        }
        p.repaint();


    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
    	GameState g=p.getGameState();
    	Boolean b=true;
        if(e.getKeyCode() == KeyEvent.VK_UP){
            b=p.moveGrid("U");
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            b=p.moveGrid("D");
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            b=p.moveGrid("L");
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            b=p.moveGrid("R");
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            p.setBoard(false);
            p.setInstructions(false);
            p.setMainMenu(true);
        }
        else if(e.getKeyChar() == 'r' && p.getBoard() && g.getAllowRotating()){
            p.rotateTile();
        }
        if(e.getKeyChar() == 'c'){
    	    p.centerBoard();
            }
        if(b)
        	p.repaint();
    }
    public void keyReleased(KeyEvent e){
    	GameState gameState=p.getGameState();
    	if(e.getKeyChar() == 'p')
    	{
    		System.out.println("Pressed p");
	    	if(gameState.getAllowPlacing())
	    	{
		    	Tile current=gameState.getCurrentTile();
		    	if(current.getNumRotation()==0)
				{
		    		if(gameState.getNoRotations()!=null)
		    		{
			    		for(int i=0; i<gameState.getNoRotations().size(); i++)
			    		{
			    			if(gameState.getNoRotations().get(i).equals(current.getProposedLocation()))
			    			{
			    				gameState.addTileToGrid(current, gameState.getCurrentPlayer(), current.getProposedLocation(), p.getHeightMoveGrid(), p.getWidthMoveGrid());
								System.out.println("added Tile because proposedLoc was in noRotations");
								gameState.setAllowPlacing(false);
								gameState.setAllowDiscarding(false);
								gameState.setAllowClickingGrid(false);
								gameState.setAllowClaiming(true);
								gameState.setAllowNotClaiming(true);
								gameState.setAllowClickingMeep(true);
								gameState.setAllowRotating(false);
			    			}
			    		}
		    		}
		    		else
		    		{
		    			System.out.println("noRotations is null");
		    		}
				}
		    	else if(current.getNumRotation()==1)
		    	{
		    		if(gameState.getOneRotation()!=null)
		    		{
			    		for(int i=0; i<gameState.getOneRotation().size(); i++)
			    		{
			    			if(gameState.getOneRotation().get(i).equals(current.getProposedLocation()))
			    			{
			    				gameState.addTileToGrid(current, gameState.getCurrentPlayer(), current.getProposedLocation(), p.getHeightMoveGrid(), p.getWidthMoveGrid());
								System.out.println("added Tile because proposedLoc was in oneRotation");
								gameState.setAllowPlacing(false);
								gameState.setAllowDiscarding(false);
								gameState.setAllowClickingGrid(false);
								gameState.setAllowClaiming(true);
								gameState.setAllowNotClaiming(true);
								gameState.setAllowClickingMeep(true);
								gameState.setAllowRotating(false);
			    			}
			    		}
		    		}
		    		else
		    		{
		    			System.out.println("oneRotation is null");
		    		}
		    	}
		    	else if(current.getNumRotation()==2)
		    	{
		    		if(gameState.getTwoRotation()!=null)
		    		{
			    		for(int i=0; i<gameState.getTwoRotation().size(); i++)
			    		{
			    			if(gameState.getTwoRotation().get(i).equals(current.getProposedLocation()))
			    			{
			    				gameState.addTileToGrid(current, gameState.getCurrentPlayer(), current.getProposedLocation(), p.getHeightMoveGrid(), p.getWidthMoveGrid());
								System.out.println("added Tile because proposedLoc was in twoRotations");
								gameState.setAllowPlacing(false);
								gameState.setAllowDiscarding(false);
								gameState.setAllowClickingGrid(false);
								gameState.setAllowClaiming(true);
								gameState.setAllowNotClaiming(true);
								gameState.setAllowClickingMeep(true);
								gameState.setAllowRotating(false);
			    			}
			    		}
		    		}
		    		else
		    		{
		    			System.out.println("twoRotations is null");
		    		}
		    	}
		    	else
		    	{
		    		if(gameState.getThreeRotation()!=null)
		    		{
			    		for(int i=0; i<gameState.getThreeRotation().size(); i++)
			    		{
			    			if(gameState.getThreeRotation().get(i).equals(current.getProposedLocation()))
			    			{
			    				gameState.addTileToGrid(current, gameState.getCurrentPlayer(), current.getProposedLocation(), p.getHeightMoveGrid(), p.getWidthMoveGrid());
								System.out.println("added Tile because proposedLoc was in threeRotations");
								gameState.setAllowPlacing(false);
								gameState.setAllowDiscarding(false);
								gameState.setAllowClickingGrid(false);
								gameState.setAllowClaiming(true);
								gameState.setAllowNotClaiming(true);
								gameState.setAllowClickingMeep(true);
								gameState.setAllowRotating(false);
			    			}
			    		}
		    		}
		    		else
		    		{
		    			System.out.println("threeRotations is null");
		    		}
		    	}
		    	p.repaint();
	    	}
	    	else
	    	{
	    		System.out.println("You cannot place yet");
	    	}
        //Place (P)
        //Discard (D)
        //Claim (C)
        //Not Claim (N)
        //Return to Main Menu (Esc)
        }
    	if (e.getKeyChar()== 'd') {
    		if (gameState.allowDiscarding == true) {
    			boolean b=gameState.canDiscard();
    			System.out.println("Tile is discardable: "+b);
    			if(b)
            		gameState.decTileCount();
    		}
    		else
    		{
                    gameState.getGameLog().addMessage("You cannot discard yet");
    			//System.out.println("You cannot discard yet");
    		}
    		p.setStartOfTurn(true);
    		p.repaint();
    	}
    	if (e.getKeyChar()== 'n') {
    		if (gameState.allowNotClaiming == true) {
    			if(!gameState.getEndGame())
    			{
	    			gameState.incrCurrentPlayer();
	                gameState.incrementCurrentTile();
	                gameState.setAllowClaiming(false);
	                gameState.setAllowClickingGrid(true);
	                gameState.setAllowClickingMeep(false);
	                gameState.setAllowDiscarding(true);
	                gameState.setAllowNotClaiming(false);
	                gameState.setAllowPlacing(false);
	                gameState.setAllowRotating(true);
	                gameState.decTileCount();
	                p.setStartOfTurn(true);
    			}
    			else
    			{
	                gameState.setAllowClaiming(false);
	                gameState.setAllowClickingGrid(false);
	                gameState.setAllowClickingMeep(false);
	                gameState.setAllowDiscarding(false);
	                gameState.setAllowNotClaiming(false);
	                gameState.setAllowPlacing(false);
	                gameState.setAllowRotating(false);
	                gameState.setTileCount(0);
	                p.setStartOfTurn(true);
    			}
    		}
    		else
    		{
                    gameState.getGameLog().addMessage("You cannot end your turn yet");
    			//System.out.println("You cannot end your turn yet");
    		}
    		p.repaint();
    	}
    }
}
