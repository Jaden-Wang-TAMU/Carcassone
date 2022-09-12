import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import static java.lang.System.*;

public class CarcassonnePanel extends JPanel{
	public static final int RANGE = 100;
    private static Boolean mainMenu = true;
    private static Boolean instructions = false;
    private static Boolean board = false;
    private Rotate r = new Rotate();
    private BufferedImage backdrop;
    private BufferedImage paperBack;
    private BufferedImage paperBackMirrored;
    private BufferedImage forest1;
    private BufferedImage title;
    private BufferedImage blueMeeple;
    private BufferedImage greenMeeple;
    private BufferedImage redMeeple;
    private BufferedImage yellowMeeple;
    private BufferedImage transparentMeeple;
    private BufferedImage scroll, scroll2;
    private BufferedImage retry;
    private Grid gameGrid=new Grid();
    private Tile[][] grid = new Tile[31][31];
    private int minx = 454, maxx = 1454;
    private int miny = 2, maxy = 1002;
    private int count = 11;
    private int gWidth = maxx - minx;
    private int gHeight = maxy - miny;
    private int gap = gWidth%count/2;
    private int xSpace = gWidth/count;
    private int ySpace = gHeight/count;
    private int width = (grid[0].length-count)/2;
    private int height = (grid.length-count)/2;
    private GameState gameState;
    private Deck deck;
    private Player blue;
    private Player green;
    private Player yellow;
    private Player red;
    private BufferedImage tileBack;
    private ArrayList<Integer> scores=new ArrayList<>();
    private boolean startOfTurn=true;
    
    public CarcassonnePanel(){

        try
        {
            backdrop = ImageIO.read(CarcassonnePanel.class.getResource("/Images/tabletop.jpg"));
            paperBack = ImageIO.read(CarcassonnePanel.class.getResource("/Images/paper.jpg"));
            paperBackMirrored = ImageIO.read(CarcassonnePanel.class.getResource("/Images/paperMirrored.jpg"));
            forest1 = ImageIO.read(CarcassonnePanel.class.getResource("/Images/forestBack11.png"));
            title = ImageIO.read(CarcassonnePanel.class.getResource("/Images/CarcassonneTitle.png"));
            greenMeeple = ImageIO.read(CarcassonnePanel.class.getResource("/Images/GreenMeeple.png"));
            blueMeeple = ImageIO.read(CarcassonnePanel.class.getResource("/Images/BlueMeeple.png"));
            redMeeple = ImageIO.read(CarcassonnePanel.class.getResource("/Images/RedMeeple.png"));
            yellowMeeple = ImageIO.read(CarcassonnePanel.class.getResource("/Images/YellowMeeple.png"));
    	    transparentMeeple = ImageIO.read(CarcassonnePanel.class.getResource("/Images/BlackMeeple.png"));
            scroll = ImageIO.read(CarcassonnePanel.class.getResource("/Images/scroll.png"));
            scroll2 = ImageIO.read(CarcassonnePanel.class.getResource("/Images/scroll01.png"));
            tileBack=ImageIO.read(CarcassonnePanel.class.getResource("/Images/TileBack.jpg"));
            retry=ImageIO.read(CarcassonnePanel.class.getResource("/Images/Retry.jpg"));
        }
        catch(Exception e)
        {
            out.println(e);
            out.println("in CarcassonnePanel");
            return;
        }
        blue = new Player("Player Blue", blueMeeple);
        green = new Player("Player Green", greenMeeple);
        yellow = new Player("Player Yellow", yellowMeeple);
        red = new Player("Player Red", redMeeple);
        gameState = new GameState(blue, green, yellow, red);
        gameGrid=gameState.getGrid();
        grid=gameGrid.getTiles();
        deck = gameState.getDeck();
        for(int i=0; i<gameState.getPlayers().size(); i++)
        {
        	scores.add(gameState.getPlayers().get(i).getScore());
        	//System.out.println("scores added "+gameState.getPlayers().get(i).getName());
        }
        //Player order: blue, green, yellow, red
        for(int i = 0; i < 31; i++){
            for(int j = 0; j < 31; j++){
                grid[i][j] = new Tile();
                //out.println((i == j) + " i = " + i + " j = " + j);
                //(i == 15 && j == 15)
                    //grid[i][j].setFront(tempRiver);
                //else
                    //grid[i][j].setFront(backdrop);
            }
        }
        grid[15][15] = deck.getStartingRiver();
        minx += gap;
        miny += gap;

        out.println("minX: "+minx + " minY: " + miny);

        maxx -= gap;
        maxy -= gap;
        out.println("maxX: "+maxx + " maxY: " + maxy);
        //out.println(grid[15][15].getFront() == null);
    }
    public boolean getStartOfTurn() {
		return startOfTurn;
	}
    public void setStartOfTurn(boolean st) {
    	startOfTurn = st;
    }
    public static void customSort(int[] arr)
    {
        // create a new array to store counts of elements in the input array
        int[] freq = new int[RANGE];
 
        // using the value of elements in the input array as an index,
        // update their frequencies in the new array
        for (int i: arr) {
            freq[i]++;
        }
 
        // overwrite the input array with sorted order
        int k = 0;
        for (int i = 0; i < RANGE; i++)
        {
            while (freq[i]-- > 0) {
                arr[k++] = i;
            }
        }
    }
    @Override
    
  
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(mainMenu){
            g.drawImage(forest1, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(title, 608,110,639,180,null);


            g.drawImage(scroll, 650, 350, 563, 90, null);//New Game
            g.drawImage(scroll2, 645, 500, 563, 90, null);//Load Game
            g.drawImage(scroll, 650, 650, 563, 90, null);//Instructions
            //g.drawRect(625,120,613,90);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Lindsay Becker", 1, 48));

            g.drawString("New Game", 820,410);
            g.drawString("Load Game", 810,560);
            g.drawString("Instructions", 805,710); //-300

        }
        if(instructions){
            g.drawImage(forest1, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(r.rotate(scroll, 270), getWidth()/3, getHeight()/8, getWidth()/3, 3 * getHeight()/4, null);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Lindsay Becker", 1, 32));
            g.drawString("Instructions", getWidth()/3 + 250,getHeight()/8 + 60);

            g.drawString("Controls", getWidth()/3 + 265,getHeight()/2 + 80+50);
            g.drawString("P: Place", getWidth()/3 + 120,getHeight()/2 +110+50);
            g.drawString("C: Center the Board", getWidth()/3 + 120,getHeight()/2 + 140+50);
            g.drawString("N: Not Claim", getWidth()/3 + 120,getHeight()/2 + 170+50);
            g.drawString("D: Discard", getWidth()/3 + 120,getHeight()/2 + 200+50);
            g.drawString("Esc: Main Menu", getWidth()/3 + 120,getHeight()/2 + 230+50);
            g.drawString("Arrow Keys: Move Board View", getWidth()/3 + 120,getHeight()/2 + 260+50);

            g.setFont(new Font("Lindsay Becker", 0, 24));
            g.drawString("Click on the space in the grid that you want to place on.", getWidth()/3+80, getHeight()/8 + 100);
            g.drawString("Press P to confirm the clicked location.", getWidth()/3+80, getHeight()/8 + 140);
            g.drawString("Press D to discard an unplayable tile.", getWidth()/3+80, getHeight()/8 + 180);
            g.drawString("The Tile in Play box will show where to claim features.", getWidth()/3+80, getHeight()/8 + 220);
            g.drawString("Click on a letter to claim the feature. This will end the turn.", getWidth()/3+80, getHeight()/8 + 260);
            g.drawString("If you don't want to claim, press N. This will end the turn.", getWidth()/3+80, getHeight()/8 + 300);
            g.drawString("Press the arrow keys to move the board view.", getWidth()/3+80, getHeight()/8 + 340);
            g.drawString("In this way, you can see the whole board.", getWidth()/3+80, getHeight()/8 + 380);
            g.drawString("Press C to recenter the board view.", getWidth()/3+80, getHeight()/8 + 420);
        }
        if(board){
            gameState.updateScores();
            g.drawImage(backdrop, 452, 0, 1004, getHeight(), null);
            g.drawImage(paperBack, 0, 0, 454, getHeight(), null);
            g.drawImage(paperBackMirrored,getWidth() - 450, 0, 454, getHeight(), null);

            g.setColor(Color.WHITE);
            drawGrid(g);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Lindsay Becker", 0, 32));

            g.drawRect(4,595, 445, 405);
            g.drawRect(5,596, 443, 403);

            g.drawString("GameLog: ", getWidth() - 280, 40);

    		int [] sort = new int [4];	  
    		int sc1 = blue.getScore();
		    int sc2 = green.getScore();
		    int sc3 = yellow.getScore();
		    int sc4 = red.getScore();
		    int p1score = sc1;
			int p2score = sc2;
			int p3score = sc3;
			int p4score = sc4;
		    int pos1y = 100;  //dif for carcassone
		    int xLoc = 60; // dif for carcassone
		    int pos2y = 210;
		    int pos3y = 320;
		    int pos4y = 430;
		    sort [0] = p1score;//last place
		    sort [1] = p2score;//3rd place
		    sort [2] = p3score;//2nd place
		    sort [3] = p4score;//1st place
    		customSort(sort);
	        System.out.println(Arrays.toString(sort));
	        if (p1score == sort[3] && p1score != -1) {
	    		
			    g.drawString("1st Player Blue: "+p1score, xLoc, pos1y);
			    drawAvailableMeeples(g, blue.getPic(), blue.getMeepArr().size(), 60, 121, blue);
			
			    sort[3] = -1;
			    p1score = -1;
			    }

			    if (p2score == sort[3] && p2score != -1) {
			
			    g.drawString("1st Player Green: "+p2score, xLoc, pos1y);
			
			    drawAvailableMeeples(g, green.getPic(), green.getMeepArr().size(), 60, 121, green);
			    sort[3] = -1;
			    p2score = -1;
			    }

			    if (p3score == sort[3] && p3score != -1) {
			
			    g.drawString("1st Player Yellow: "+p3score, xLoc, pos1y);
			    drawAvailableMeeples(g, yellow.getPic(), yellow.getMeepArr().size(), 60, 121, yellow);
			
			    sort[3] = -1;
			    p3score = -1;
			    }

			    if (p4score == sort[3] && p4score != -1) {
			
			    g.drawString("1st Player Red: "+p4score, xLoc, pos1y);
			    drawAvailableMeeples(g, red.getPic(), red.getMeepArr().size(), 60, 121, red);
			
			    sort[3] = -1;
			    p4score = -1;
			}
	        
		    if (p1score == sort[2] && p1score != -1) {
		    	
		    	g.drawString("2nd Player Blue: "+p1score, xLoc, pos2y);
		    	drawAvailableMeeples(g, blue.getPic(), blue.getMeepArr().size(), 60, 231, blue);
		    	
		    	sort[2] = -1;
		    	p1score = -1;
		    }
		    if (p2score == sort[2] && p2score != -1) {
		
		    g.drawString("2nd Player Green: "+p2score, xLoc, pos2y);
		    drawAvailableMeeples(g, green.getPic(), green.getMeepArr().size(), 60, 231, green);
		
		    sort[2] = -1;
		    p2score = -1;
		    }
		
		    if (p3score == sort[2] && p3score != -1) {
		
		    g.drawString("2nd Player Yellow: "+p3score, xLoc, pos2y);
		    drawAvailableMeeples(g, yellow.getPic(), yellow.getMeepArr().size(), 60, 231, yellow);
		
		    sort[2] = -1;
		    p3score = -1;
		    }
		    if (p4score == sort[2] && p4score != -1) {
		
		    g.drawString("2nd Player Red: "+p4score, xLoc, pos2y);
		    drawAvailableMeeples(g, red.getPic(), red.getMeepArr().size(), 60, 231, red);
		
		    sort[2] = -1;
		    p4score = -1;
		    }
			
		    if (p1score == sort[1] && p1score != -1) {
	    		
	    		g.drawString("3rd Player Blue: "+p1score, xLoc, pos3y);
	    		drawAvailableMeeples(g, blue.getPic(), blue.getMeepArr().size(), 60, 341, blue);
	    		
	    		sort[1] = -1;
	    		p1score = -1;
		    }
		    if (p2score == sort[1] && p2score != -1) {
		    	
		    	g.drawString("3rd Player Green: "+p2score, xLoc, pos3y);
		    	drawAvailableMeeples(g, green.getPic(), green.getMeepArr().size(), 60, 341, green);
		    	
		    	sort[1] = -1;
		    	p2score = -1;
		    }
		    if (p3score == sort[1] && p3score != -1) {
		
		    g.drawString("3rd Player Yellow: "+p3score, xLoc, pos3y);
			drawAvailableMeeples(g, yellow.getPic(), yellow.getMeepArr().size(), 60, 341, yellow);
		
		    sort[1] = -1;
		    p3score = -1;
		    }
		    if (p4score == sort[1] && p4score != -1) {
		
		    g.drawString("3rd Player Red: "+p4score, xLoc, pos3y);
			drawAvailableMeeples(g, red.getPic(), red.getMeepArr().size(), 60, 341, red);
		
		    sort[1] = -1;
		    p4score = -1;
		    }
		    
	    	if (p1score == sort[0] && p1score != -1) {
	    		
	    		g.drawString("4th Player Blue: "+p1score, xLoc, pos4y);
	    		drawAvailableMeeples(g, blue.getPic(), blue.getMeepArr().size(), 60, 451, blue);
	    		
	    		sort[0] = -1;
	    		p1score = -1;
	    	}
	    	if (p2score == sort[0] && p2score != -1) {
	    		
	    		g.drawString("4th Player Green: "+p2score, xLoc, pos4y);
	    		drawAvailableMeeples(g, green.getPic(), green.getMeepArr().size(), 60, 451, green);
	    		
	    		sort[0] = -1;
	    		p2score = -1;
	    	}
	    	if (p3score == sort[0] && p3score != -1) {
	
	    		g.drawString("4th Player Yellow: "+p3score, xLoc, pos4y);
	    		drawAvailableMeeples(g, yellow.getPic(), yellow.getMeepArr().size(), 60, 451, yellow);
	
	    		sort[0] = -1;
	    		p3score = -1;
	    	}
	    	if (p4score == sort[0] && p4score != -1) {
	
	    		g.drawString("4th Player Red: "+p4score, xLoc, pos4y);
	    		drawAvailableMeeples(g, red.getPic(), red.getMeepArr().size(), 60, 451, red);
	
	    		sort[0] = -1;
	    	}
    		p1score = sc1;
    		p2score = sc2;
    		p3score = sc3;
    		p4score = sc4;
    		
    		
    		sort [0] = p1score;
    		sort [1] = p2score;
    		sort [2] = p3score;
    		sort [3] = p4score;
    		
    		
    		
    		customSort(sort);
    		
    		System.out.println(Arrays.toString(sort));
    		g.drawString("Current Player: " + gameState.getCurrentPlayer(), 54, 50);
            g.drawString("Tile in Play:", 10, 584);
            g.drawString(gameState.getTileCount()+" Tiles left.", 155, 624);
            g.drawImage(gameState.getCurrentTile().getFront(), 145, 705, 165, 165, null);
            //System.out.println("P1. CurrentTile is currently:\n"+gameState.getCurrentTile().toString());
            if(gameState.getCurrentTile().getGridLocation() != null){
                g.setColor(Color.WHITE);
                drawGrid(g, 149, 709, 157, 3, gameState.getCurrentTile().getMeepleGrid());
                g.setColor(Color.BLACK);
            }
            
            ArrayList<Location> noRot=gameState.getNoRotations();
            ArrayList<Location> oneRot=gameState.getOneRotation();
            ArrayList<Location> twoRot=gameState.getTwoRotation();
            ArrayList<Location> threeRot=gameState.getThreeRotation();
            if(startOfTurn)
            {
	            //System.out.println("Pre Rotation: "+gameState.getCurrentTile().toString());
	            noRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	            
	            r.rotate(gameState.getCurrentTile(), 270);
	            //System.out.println("One Rotation: "+gameState.getCurrentTile().toString());
	            //g.drawImage(gameState.getCurrentTile().getFront(), 110, 50, 50, 50, null);
	            oneRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	            
	            r.rotate(gameState.getCurrentTile(), 270);
	            //System.out.println("Two Rotation: "+gameState.getCurrentTile().toString());
	            //g.drawImage(gameState.getCurrentTile().getFront(), 170, 50, 50, 50, null);
	            twoRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	            
	            r.rotate(gameState.getCurrentTile(), 270);
	            //System.out.println("Three Rotation: "+gameState.getCurrentTile().toString());
	            //g.drawImage(gameState.getCurrentTile().getFront(), 230, 50, 50, 50, null);
	            threeRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	            
	            r.rotate(gameState.getCurrentTile(), 270);
	            //System.out.println("Back to original: "+gameState.getCurrentTile().toString());
	            startOfTurn=false;
            }
            if(!(noRot==null&&oneRot==null&&twoRot==null&&threeRot==null)&&!gameState.getEndGame())
            {
                if(!gameState.getAllowClickingMeep()){
                    if(gameState.getCurrentTile().getNumRotation()==0){
                        if(noRot!=null){
                        	System.out.println("There are");
                            for(int i=0; i<11; i++){
                                for(int y=0; y<11; y++){
                                    for(Location l: noRot){
                                    	if(l!=null)
                                    	{
	                                        if(i==l.getY()&&y==l.getX())
	                                        {
	                                            g.drawImage(tileBack, minx + l.getX() * xSpace + 1, miny + l.getY() * ySpace + 1, xSpace - 1, ySpace - 1, null);
	                                        }
                                    	}
                                    }
                                }
                            }
                        }
                    }
                    if(gameState.getCurrentTile().getNumRotation()==1){
                        if(oneRot!=null){
                            for(int i=0; i<11; i++){
                                for(int y=0; y<11; y++){
                                    for(Location l: oneRot){
                                        if(i==l.getY()&&y==l.getX())
                                            g.drawImage(tileBack, minx + l.getX() * xSpace + 1, miny + l.getY() * ySpace + 1, xSpace - 1, ySpace - 1, null);
                                    }
                                }
                            }
                        }
                    }       
                    if(gameState.getCurrentTile().getNumRotation()==2){
                        if(twoRot!=null){
                            for(int i=0; i<11; i++){
                                for(int y=0; y<11; y++){
                                    for(Location l: twoRot){
                                        if(i==l.getY()&&y==l.getX())
                                            g.drawImage(tileBack, minx + l.getX() * xSpace + 1, miny + l.getY() * ySpace + 1, xSpace - 1, ySpace - 1, null);
                                    }
                                }
                            }
                        }
                    }
                    if(gameState.getCurrentTile().getNumRotation()==3){
                        if(threeRot!=null){
                            for(int i=0; i<11; i++){
                                for(int y=0; y<11; y++){
                                    for(Location l: threeRot){
                                        if(i==l.getY()&&y==l.getX())
                                            g.drawImage(tileBack, minx + l.getX() * xSpace + 1, miny + l.getY() * ySpace + 1, xSpace - 1, ySpace - 1, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            gameState.setNoRotation(noRot);
            gameState.setOneRotation(oneRot);
            gameState.setTwoRotation(twoRot);
            gameState.setThreeRotation(threeRot);
	    
            
            g.setFont(new Font("Lindsay Becker", 0, 24));
            g.setColor(Color.BLACK);
            System.out.println("Possible Locs for no Rotation:");
            g.setFont(new Font("Lindsay Becker", 0, 30));
            g.drawString("Possible actions:",8, 908);
            g.setFont(new Font("Lindsay Becker", 0, 24));
            if(!gameState.getEndGame())
            {
	            if(noRot!=null && noRot.size()!=0){
	                out.println(noRot);
	                g.drawString("Can be placed as is", 8, 933);
	            }
	            System.out.println("Possible Locs for one Rotation:");
	            if(oneRot!=null && oneRot.size()!=0){
	                out.println(oneRot);
	                g.drawString("Rotate once to place", 8, 953);
	            }
	            System.out.println("Possible Locs for two Rotation:");
	            if(twoRot!=null&& twoRot.size()!=0){
	                out.println(twoRot);
	                g.drawString("Rotate twice to place", 8, 973);
	            }
	            System.out.println("Possible Locs for three Rotation:");
	            if(threeRot!=null&& threeRot.size()!=0){
	                out.println(threeRot);
	                g.drawString("Rotate thrice to place", 8, 993);
	            }
	            else{
	            	System.out.println("none");
	            }
            }
            /*if(canPlace){
                g.setColor(new Color(59, 127, 26, 255));
                g.drawString("Can Place", 254, 584);
            }else{
                g.setColor(new Color(170, 40, 38, 255));
                g.drawString("Cannot Place", 254, 584);
            }*/
            //g.drawImage(gameState.getCurrentTile().getFront(), 50, 50, 50, 50, null);
            
            out.println("Current Location: " + (width - 10) + " " + (height - 10));
            printBooleans();

            for(int i=0; i<gameState.getMainRoads().size(); i++)
            {
            	String owner="";
            	if(gameState.getMainRoads().get(i).getPlayers().size()>0)
            	{
            		for(int q=0; q<gameState.getMainRoads().get(i).getPlayers().size(); q++)
            		{
            			owner+=""+gameState.getMainRoads().get(i).getPlayers().get(q).getName();
            		}
            	}
            	else
            	{
            		owner+="nobody";
            	}
            	System.out.println("MainRoad"+(i+1)+":"+" owned by "+owner);
            	gameState.getMainRoads().get(i).printFeatureEnds();
            }
            for(int i=0; i<gameState.getMainCities().size(); i++)
            {
            	String owner="";
            	if(gameState.getMainCities().get(i).getPlayers().size()>0)
            	{
            		for(int q=0; q<gameState.getMainCities().get(i).getPlayers().size(); q++)
            		{
            			owner+=""+gameState.getMainCities().get(i).getPlayers().get(q).getName();
            		}
            	}
            	else
            	{
            		owner+="nobody";
            	}
            	System.out.println("MainCity"+(i+1)+":"+owner);
            	gameState.getMainCities().get(i).printFeatureEnds();
            }
            for(int i=0; i<gameState.getMainMonasteries().size(); i++)
            {
            	String owner="";
            	if(gameState.getMainMonasteries().get(i).getMeep()!=null)
            	{
            			owner+=""+gameState.getMainMonasteries().get(i).getOwner();
            	}
            	else
            	{
            		owner+="nobody";
            	}
            	System.out.println("MainMonastery"+(i+1)+":"+owner);
            	MainMonastery mm=gameState.getMainMonasteries().get(i);
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
            System.out.println("There are currently:\n"+gameState.getMainMonasteries().size()+" MainMonasteries\n"+gameState.getMainRoads().size()+" MainRoads\n"+gameState.getMainCities().size()+" MainCities\n"+gameState.getMainFields().size()+" MainFields");
            for(int i=0; i<gameState.getMainFields().size(); i++)
            {
            	String owner="";
            	if(gameState.getMainFields().get(i).getPlayers().size()>0)
            	{
            		for(int q=0; q<gameState.getMainFields().get(i).getPlayers().size(); q++)
            		{
            			owner+=""+gameState.getMainFields().get(i).getPlayers().get(q).getName();
            		}
            	}
            	else
            	{
            		owner+="nobody";
            	}
            	System.out.println("MainField"+(i+1)+":"+" owned by "+owner);
            	gameState.getMainFields().get(i).printFeatureEnds();
            }
            
            
            for(int r = 0; r < 11; r++){
                for(int c = 0; c < 11; c++){
                    if(grid[r+height][c+width].getFront() != null)
                    {
                    	Tile t=grid[r+height][c+width];
                        g.drawImage(grid[r+height][c+width].getFront(), minx+1 + c * xSpace, miny+1 + r * ySpace, xSpace-1, ySpace-1, null);
                        Boolean[][] list = t.getClaimed();
                        for(int i = 0; i < list.length; i++){
                            for(int j = 0; j < list[i].length; j++){
                                Boolean claimed = list[i][j];
                                if(claimed)
                                {
                                	System.out.println("t isTurned: "+t.getTurned());
                                	if(t.getTurned())
                                	{
                                		Rotate rot=new Rotate();
                                		g.drawImage(rot.rotate(t.getMeepleColor(), 90), c * xSpace + minx + 2+j*xSpace/3, r * ySpace + miny + 2+i*ySpace/3, xSpace/3-2, ySpace/3-2, null);
                                	}
                                	else
                                	{
                                    g.drawImage(t.getMeepleColor(), c * xSpace + minx + 2+j*xSpace/3, r * ySpace + miny + 2+i*ySpace/3, xSpace/3-2, ySpace/3-2, null);
                                	}
                                }
                            }
                        }
                    }
                }
           }
        //drawMeeple(g);
        g.setColor(Color.BLACK);
        drawGameLog(g);
        g.setFont(new Font("Lindsay Becker", 1, 30));
        g.drawString("Reset", 355, 890);
        if(gameState.getEndGame())
        {
        	int max=gameState.getPlayers().get(0).getScore();
        	for(int i=1; i<gameState.getPlayers().size(); i++)
        	{
        		if(gameState.getPlayers().get(i).getScore()>max)
        			max=gameState.getPlayers().get(i).getScore();
        	}
        	g.setColor(Color.GREEN);
        	g.drawString("WINNER", xLoc+280, pos1y);
        	if(sort[1]==sort[0])
        		g.drawString("WINNER", xLoc+280, pos2y);
        	if(sort[2]==sort[0])
        		g.drawString("WINNER", xLoc+280, pos3y);
        	if(sort[3]==sort[0])
        		g.drawString("WINNER", xLoc+280, pos4y);
            g.setFont(new Font("Lindsay Becker", 1, 80));
            g.setColor(Color.RED);
        	g.drawString("GAME OVER", 12, 679);
        }
    	g.drawImage(retry, 350, 900, 75, 75, null);
    	System.out.println("currentTile: "+gameState.getCurrentTile());
        }
    }
    public void printBooleans()
    {
    	System.out.println("allowClaim: "+gameState.getAllowClaiming());
    	System.out.println("allowClickGrid: "+gameState.getAllowClickingGrid());
    	System.out.println("allowClickMeep: "+gameState.getAllowClickingMeep());
    	System.out.println("allowDiscard: "+gameState.getAllowDiscarding());
    	System.out.println("allowNotClaim: "+gameState.getAllowNotClaiming());
    	System.out.println("allowPlace: "+gameState.getAllowPlacing());
    	System.out.println("allowRotate:"+gameState.getAllowRotating());
    }
    private void drawGrid(Graphics g){

        //int minx = this.minx + gap;
        //int miny = this.miny + gap;

        //int maxx = this.maxx - gap;
        //int maxy = this.maxy - gap;

        int i = 0; int x = minx; int y = miny;
        while(i < count && x < maxx && y < maxy)
        {
            g.drawLine(x, miny, x, maxy);
            g.drawLine(minx, y, maxx, y);
            x += xSpace; y += ySpace; i++;
        }

        g.drawRect(minx,miny,maxx-minx, maxy-miny);
    }
    private void drawGrid(Graphics g, int x, int y, int length, int count, String[][] grid){
        g.setFont(new Font("Courier New", 1, 32));
        
        int minx = x;
        int miny = y;
        
        int maxx = x + length;
        int maxy = y + length;
        
        int xSpace = length/3;
        int ySpace = length/3;
        
        int i = 0;
        while(i < count && x < maxx && y < maxy)
        {
            g.drawLine(x, miny, x, maxy);
            g.drawLine(minx, y, maxx, y);
            x += xSpace; y += ySpace; i++;
        }
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                out.print(grid[r][c] + " ");
                if(grid[c][r] != null){
                    g.drawString(grid[c][r], minx + r * xSpace + 17, miny + c * ySpace + 35);
                }
            }
            out.println();
        }

        g.drawRect(minx,miny,maxx-minx, maxy-miny);
        
    }
    private void drawAvailableMeeples(Graphics g, BufferedImage bi, int count, int x, int y, Player p){
    	 int cnt = 0;
         for(Meeple m: p.getMeepArr()){
             if(!m.getUsed()){
                 g.drawImage(m.getPic(), x, y, 40, 40, null);
                 cnt++;
                 x+=41;
             }
         }
         for(int i = 0; i < 7 - cnt; i++){
             g.drawImage(transparentMeeple, x, y, 40, 40, null);
             x+=41;
         }
     }
    public boolean moveGrid(String direction){
    	Boolean allowed=false;
    	if(direction.equals("U") && height + 11 < grid.length){
            height = height + 1;
            for(int r = 0; r < 11; r++) {
            	for(int c = 0; c < 11; c++) {
            		GridLocation g = grid[r+height][c+width].getGridLocation();
            		g.setGridLoc(new Location(g.getGridLoc().getX(), g.getGridLoc().getY() - 1));
            		grid[r+height][c+width].setGridLocation(g);
            	}
            }
            //updateRotations(0, -1);
            allowed=true;
        }
        else if(direction.equals("D") && height > 0){
            height = height - 1;
            for(int r = 0; r < 11; r++) {
            	for(int c = 0; c < 11; c++) {
            		GridLocation g = grid[r+height][c+width].getGridLocation();
            		g.setGridLoc(new Location(g.getGridLoc().getX(), g.getGridLoc().getY() + 1));
            		grid[r+height][c+width].setGridLocation(g);
            	}
            }
            //updateRotations(0, 1);
            allowed=true;
        }
        else if(direction.equals("L") && width + 11 < grid.length){
            width = width + 1;
            for(int r = 0; r < 11; r++) {
            	for(int c = 0; c < 11; c++) {
            		GridLocation g = grid[r+height][c+width].getGridLocation();
            		g.setGridLoc(new Location(g.getGridLoc().getX()-1, g.getGridLoc().getY()));
            		grid[r+height][c+width].setGridLocation(g);
            	}
            }
            //updateRotations(-1, 0);
            allowed=true;
        }
        else if(direction.equals("R") && width > 0){
        	width = width - 1;
          
        	for(int r = 0; r < 11; r++) {
            	for(int c = 0; c < 11; c++) {
            		GridLocation g = grid[r+height][c+width].getGridLocation();
            		g.setGridLoc(new Location(g.getGridLoc().getX()+1, g.getGridLoc().getY()));
            		grid[r+height][c+width].setGridLocation(g);
            	}
            }
            //updateRotations(1, 0);
        	allowed=true;
        }
    	System.out.println("moving Grid allowed: "+allowed);
    	if(allowed)
    	{
	    	updateAllLocations(direction);
	        ArrayList<Location> noRot=gameState.getNoRotations();
	        ArrayList<Location> oneRot=gameState.getOneRotation();
	        ArrayList<Location> twoRot=gameState.getTwoRotation();
	        ArrayList<Location> threeRot=gameState.getThreeRotation();
	        final int initialRot=gameState.getCurrentTile().getNumRotation();
	        for(int i=0; i<4-initialRot; i++)
	        {
	        	r.rotate(gameState.getCurrentTile(), 270);
	        }
	    	noRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	
	        r.rotate(gameState.getCurrentTile(), 270);
	        oneRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	        
	        r.rotate(gameState.getCurrentTile(), 270);
	        twoRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	        
	        r.rotate(gameState.getCurrentTile(), 270);
	        threeRot=gameState.findPossibleLocs(gameState.getCurrentTile(), gameState.getGameLog(), height, width);
	        
	        r.rotate(gameState.getCurrentTile(), 270);
	        for(int i=0; i<initialRot; i++)
	        {
	        	r.rotate(gameState.getCurrentTile(), 270);
	        }
	    	gameState.setNoRotation(noRot);
	        gameState.setOneRotation(oneRot);
	        gameState.setTwoRotation(twoRot);
	        gameState.setThreeRotation(threeRot);
    	}
    	return allowed;
    }

    public void setMainMenu(Boolean m){ mainMenu = m; repaint();}
    public Boolean getMainMenu(){ return mainMenu; }

    public void setInstructions(Boolean i){ instructions = i; repaint();}
    public Boolean getInstructions(){ return instructions; }

    public void setBoard(Boolean b){ board = b; repaint();}
    public Boolean getBoard(){ return board; }

    public GameState getGameState()
    {
    	return gameState;
    }
    
    public Tile[][] getGrid()
    {
    	return grid;
    }
    
    public void rotateTile(){ 
        r.rotate(gameState.getCurrentTile(), 270);
        repaint();
    }
    /*public void drawMeeple(Graphics g)
    {
    	for(int r = 0; r < grid.length; r++){
            for(Tile t: grid[r]){
                if(t.getPlaceGrid()[0][0]!=null){
                    int x = t.getGridLocation().getGridLoc().getX();
                    int y = t.getGridLocation().getGridLoc().getY();
                    Boolean[][] list = t.getClaimed();
                    for(int i = 0; i < list.length; i++){
                        for(int j = 0; j < list[i].length; j++){
                            Boolean claimed = list[i][j];
                            if(claimed)
                            {
                                g.drawImage(t.getMeepleColor(), x * xSpace + minx + 2+j*xSpace/3, y * ySpace + miny + 2+i*ySpace/3, xSpace/3-2, ySpace/3-2, null);
                                System.out.println("drawing a meeple at "+x+", "+y+" and at "+i+", "+j);
                            }
                        }
                    }
                }
            }
        }
    }*/
    public void updateAllLocations(String s)
    {
    	//All the things that must have their locations updated
    	ArrayList<MainCity> mainCities=gameState.getMainCities();
    	ArrayList<MainRoad> mainRoads=gameState.getMainRoads();
    	ArrayList<MainField> mainFields=gameState.getMainFields();
    	if(s=="U")
    	{
    		MainRiver mR=gameState.getMainRiver();
    		FeatureEnd mREnd=mR.getMainEnd();
    		mREnd.setLoc(new Location(mREnd.getLoc().getX(), mREnd.getLoc().getY()-1));
    		ArrayList<River> mRRivers=mR.getRivers();
    		for(int i=0; i<mRRivers.size(); i++)
    		{
    			if(mRRivers.get(i).getEndLocation()!=null&&!mRRivers.get(i).getEndLocation().equals(mR.getMainEnd())||mRRivers.get(i).getStartLocation()!=null&&!mRRivers.get(i).getStartLocation().equals(mR.getMainEnd()))
    			{
	    			if(mRRivers.get(i).getStartLocation()!=null)
	    				mRRivers.get(i).setStartLocY(mRRivers.get(i).getStartLocY()-1);
	    			if(mRRivers.get(i).getEndLocation()!=null)
	    				mRRivers.get(i).setEndLocY(mRRivers.get(i).getEndLocY()-1);
    			}
    		}
    		mR.setNeededLocation(mR.getMainEnd());
    		gameState.getCurrentTile().getProposedLocation().setY(gameState.getCurrentTile().getProposedLocation().getY()-1);
    		//deck.getStartingRiver().getGridLocation().getGridLoc().setY(deck.getStartingRiver().getGridLocation().getGridLoc().getY()-1);
    		for(MainRoad main: mainRoads){
    			Road firstRoad=main.getRoads().get(0);
                for(FeatureEnd f:firstRoad.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()-1));
        		}
                for(Road r: main.getRoads()){
                    r.setGridLoc(new Location(r.getGridLoc().getX(), r.getGridLoc().getY()-1));
                }
            }
    		
            for(MainField main: mainFields){
                Field firstField=main.getFields().get(0);
                for(FeatureEnd f:firstField.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()-1));
        		}
                for(Field f: main.getFields()){
                    f.setGridLoc(new Location(f.getGridLoc().getX(), f.getGridLoc().getY()-1));
                }
            }
            
            for(MainCity main: mainCities){
            	City firstCity=main.getCities().get(0);
                for(FeatureEnd f:firstCity.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()-1));
        		}
                for(City c: main.getCities()){
                    c.setGridLoc(new Location(c.getGridLoc().getX(), c.getGridLoc().getY()-1));
                }
            }
    	}
    	else if(s=="D")
    	{
    		MainRiver mR=gameState.getMainRiver();
    		FeatureEnd mREnd=mR.getMainEnd();
    		mREnd.setLoc(new Location(mREnd.getLoc().getX(), mREnd.getLoc().getY()+1));
    		ArrayList<River> mRRivers=mR.getRivers();
    		for(int i=0; i<mRRivers.size(); i++)
    		{
    			if(mRRivers.get(i).getEndLocation()!=null&&!mRRivers.get(i).getEndLocation().equals(mR.getMainEnd())||mRRivers.get(i).getStartLocation()!=null&&!mRRivers.get(i).getStartLocation().equals(mR.getMainEnd()))
    			{
	    			if(mRRivers.get(i).getStartLocation()!=null)
	    				mRRivers.get(i).setStartLocY(mRRivers.get(i).getStartLocY()+1);
	    			if(mRRivers.get(i).getEndLocation()!=null)
	    				mRRivers.get(i).setEndLocY(mRRivers.get(i).getEndLocY()+1);
    			}
    		}
    		mR.setNeededLocation(mR.getMainEnd());
    		//deck.getStartingRiver().getGridLocation().getGridLoc().setY(deck.getStartingRiver().getGridLocation().getGridLoc().getY()+1);
    		
    		gameState.getCurrentTile().getProposedLocation().setY(gameState.getCurrentTile().getProposedLocation().getY()+1);
    		
    		for(MainRoad main: mainRoads){

    			Road firstRoad=main.getRoads().get(0);
                for(FeatureEnd f:firstRoad.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()+1));
        		}
                for(Road r: main.getRoads()){
                    r.setGridLoc(new Location(r.getGridLoc().getX(), r.getGridLoc().getY()+1));
                }
            }
    		
            for(MainField main: mainFields){
            	Field firstField=main.getFields().get(0);
                for(FeatureEnd f:firstField.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()+1));
        		}
                for(Field f: main.getFields()){
                    f.setGridLoc(new Location(f.getGridLoc().getX(), f.getGridLoc().getY()+1));
                }
            }
            
            for(MainCity main: mainCities){
            	City firstCity=main.getCities().get(0);
                for(FeatureEnd f:firstCity.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX(), f.getLoc().getY()+1));
        		}
                for(City c: main.getCities()){
                    c.setGridLoc(new Location(c.getGridLoc().getX(), c.getGridLoc().getY()+1));
                }
            }
    	}
    	else if(s=="L")
    	{
    		MainRiver mR=gameState.getMainRiver();
    		FeatureEnd mREnd=mR.getMainEnd();
    		mREnd.setLoc(new Location(mREnd.getLoc().getX()-1, mREnd.getLoc().getY()));
    		ArrayList<River> mRRivers=mR.getRivers();
    		for(int i=0; i<mRRivers.size(); i++)
    		{
    			if(mRRivers.get(i).getEndLocation()!=null&&!mRRivers.get(i).getEndLocation().equals(mR.getMainEnd())||mRRivers.get(i).getStartLocation()!=null&&!mRRivers.get(i).getStartLocation().equals(mR.getMainEnd()))
    			{
	    			if(mRRivers.get(i).getStartLocation()!=null)
	    				mRRivers.get(i).setStartLocX(mRRivers.get(i).getStartLocX()-1);
	    			if(mRRivers.get(i).getEndLocation()!=null)
	    				mRRivers.get(i).setEndLocX(mRRivers.get(i).getEndLocX()-1);
    			}
    		}
    		mR.setNeededLocation(mR.getMainEnd());
    		//deck.getStartingRiver().getGridLocation().getGridLoc().setX(deck.getStartingRiver().getGridLocation().getGridLoc().getX()-1);
    		
    		gameState.getCurrentTile().getProposedLocation().setX(gameState.getCurrentTile().getProposedLocation().getX()-1);
    		
    		for(MainRoad main: mainRoads){
    			Road firstRoad=main.getRoads().get(0);
                for(FeatureEnd f:firstRoad.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX()-1, f.getLoc().getY()));
        		}
                for(Road r: main.getRoads()){
                    r.setGridLoc(new Location(r.getGridLoc().getX()-1, r.getGridLoc().getY()));
                }
            }
    		
            for(MainField main: mainFields){
            	Field firstField=main.getFields().get(0);
                for(FeatureEnd f:firstField.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX()-1, f.getLoc().getY()));
        		}
                for(Field f: main.getFields()){
                    f.setGridLoc(new Location(f.getGridLoc().getX()-1, f.getGridLoc().getY()));
                }
            }
            
            for(MainCity main: mainCities){
            	City firstCity=main.getCities().get(0);
                for(FeatureEnd f:firstCity.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX()-1, f.getLoc().getY()));
        		}
                for(City c: main.getCities()){
                    c.setGridLoc(new Location(c.getGridLoc().getX()-1, c.getGridLoc().getY()));
                }
            }
    	}
    	else if(s=="R")
       	{
       		MainRiver mR=gameState.getMainRiver();
       		FeatureEnd mREnd=mR.getMainEnd();
       		mREnd.setLoc(new Location(mREnd.getLoc().getX()+1, mREnd.getLoc().getY()));
       		ArrayList<River> mRRivers=mR.getRivers();
       		for(int i=0; i<mRRivers.size(); i++)
       		{
       			if(mRRivers.get(i).getEndLocation()!=null&&!mRRivers.get(i).getEndLocation().equals(mR.getMainEnd())||mRRivers.get(i).getStartLocation()!=null&&!mRRivers.get(i).getStartLocation().equals(mR.getMainEnd()))
       			{
   	    			if(mRRivers.get(i).getStartLocation()!=null)
   	    				mRRivers.get(i).setStartLocX(mRRivers.get(i).getStartLocX()+1);
   	    			if(mRRivers.get(i).getEndLocation()!=null)
   	    				mRRivers.get(i).setEndLocX(mRRivers.get(i).getEndLocX()+1);
       			}
       		}
       		mR.setNeededLocation(mR.getMainEnd());
       		//deck.getStartingRiver().getGridLocation().getGridLoc().setX(deck.getStartingRiver().getGridLocation().getGridLoc().getX()+1);
       		gameState.getCurrentTile().getProposedLocation().setX(gameState.getCurrentTile().getProposedLocation().getX()+1);
       		
       		for(MainRoad main: mainRoads){
       			Road firstRoad=main.getRoads().get(0);
                for(FeatureEnd f:firstRoad.getFeatureEnds())
        		{
        			f.setLoc(new Location(f.getLoc().getX()+1, f.getLoc().getY()));
        		}
                for(Road r: main.getRoads()){
                    r.setGridLoc(new Location(r.getGridLoc().getX()+1, r.getGridLoc().getY()));
                }
               }
       		
               for(MainField main: mainFields){
            	   Field firstField=main.getFields().get(0);
                   for(FeatureEnd f:firstField.getFeatureEnds())
	           		{
	           			f.setLoc(new Location(f.getLoc().getX()+1, f.getLoc().getY()));
	           		}
                   for(Field f: main.getFields()){
                       f.setGridLoc(new Location(f.getGridLoc().getX()+1, f.getGridLoc().getY()));
                   }
               }
               
               for(MainCity main: mainCities){
            	   City firstCity=main.getCities().get(0);
                   for(FeatureEnd f:firstCity.getFeatureEnds())
		       		{
		       			f.setLoc(new Location(f.getLoc().getX()+1, f.getLoc().getY()));
		       		}
                   for(City c: main.getCities()){
                       c.setGridLoc(new Location(c.getGridLoc().getX()+1, c.getGridLoc().getY()));
                   }
               }
       	}
        System.out.println("CurrentTile's new Loc: "+gameState.getCurrentTile().getProposedLocation());
        System.out.println("Height is now: "+height+" width is now: "+width);
        System.out.println("CurrentTile's new Loc should be "+(gameState.getCurrentTile().getProposedLocation().getY()+height)+", "+(gameState.getCurrentTile().getProposedLocation().getX()+width));
    }
    public int getHeightMoveGrid()
    {
    	return height;
    }
    public int getWidthMoveGrid()
    {
    	return width;
    }
    public void updateRotations(int x, int y){
        ArrayList<Location> noRot = gameState.getNoRotations();
        ArrayList<Location> oneRot = gameState.getOneRotation();
        ArrayList<Location> twoRot = gameState.getTwoRotation();
        ArrayList<Location> threeRot = gameState.getThreeRotation();
        if(noRot != null){
            for(Location l: noRot){
                int newX = l.getX() + x;
                int newY = l.getY() + y;
                if(newX >= 0 && newX <= 30 && newY >= 0 && newY <= 30)
                    l.setLoc(newX, newY);
            }
        }
        if(oneRot != null){
            for(Location l: oneRot){
                int newX = l.getX() + x;
                int newY = l.getY() + y;
                if(newX >= 0 && newX <= 30 && newY >= 0 && newY <= 30)
                    l.setLoc(newX, newY);
            }
        }
        if(twoRot != null){
            for(Location l: twoRot){
                int newX = l.getX() + x;
                int newY = l.getY() + y;
                if(newX >= 0 && newX <= 30 && newY >= 0 && newY <= 30)
                    l.setLoc(newX, newY);
            }
        }
        if(threeRot != null){
            for(Location l: threeRot){
                int newX = l.getX() + x;
                int newY = l.getY() + y;
                if(newX >= 0 && newX <= 30 && newY >= 0 && newY <= 30)
                    l.setLoc(newX, newY);
            }
        }
        gameState.setNoRotation(noRot);
        gameState.setOneRotation(oneRot);
        gameState.setTwoRotation(twoRot);
        gameState.setThreeRotation(threeRot);
        repaint();
    }
	public void drawGameLog(Graphics g){
        GameLog gameLog = gameState.getGameLog();
        ArrayList<String> log = gameLog.getMessages();
        int i = 0, j = 0;
        if(log.size() > 32){
            j = log.size() - 32;
        }
        while(j < log.size()){
            g.drawString(log.get(j), getWidth() - 450, 70 + i*30);
            i++; j++;
        }
    }
	public void reset()
	{
		gameState.reset();
		gameGrid=gameState.getGrid();
        grid=gameGrid.getTiles();
        deck = gameState.getDeck();
        minx = 454;
        maxx = 1454;
        miny = 2;
        maxy = 1002;
        count = 11;
        gWidth = maxx - minx;
        gHeight = maxy - miny;
        gap = gWidth%count/2;
        xSpace = gWidth/count;
        ySpace = gHeight/count;
        width = (grid[0].length-count)/2;
        height = (grid.length-count)/2;
        scores=new ArrayList<>();
        startOfTurn=true;
        for(int i=0; i<gameState.getPlayers().size(); i++)
        {
        	scores.add(gameState.getPlayers().get(i).getScore());
        }
        for(int i = 0; i < 31; i++){
            for(int j = 0; j < 31; j++){
                grid[i][j] = new Tile();
            }
        }
        grid[15][15] = deck.getStartingRiver();
        minx += gap;
        miny += gap;

        maxx -= gap;
        maxy -= gap;
	}
	public void centerBoard(){
        int hChange = height - 10;
        int wChange = width - 10;
        if(hChange != 0){
            if(hChange > 0){
                for(int i = 0; i < hChange; i++){
                    moveGrid("D");
                }
            }
            else{
                for(int i = hChange; i < 0; i++){
                    moveGrid("U");
                }
            }
        }
        if(wChange != 0){
            if(wChange > 0){
                for(int i = 0; i < wChange; i++){
                    moveGrid("R");
                }
            }
            else{
                for(int i = wChange; i < 0; i++){
                    moveGrid("L");
                }
            }
        }
	}
}
