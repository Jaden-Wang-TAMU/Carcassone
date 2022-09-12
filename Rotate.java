import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;

public class Rotate {
    public void rotate(Tile t, int a)
    {
    	t.setFront(rotate(t.getFront(), a));
    	t.setPlaceGrid(rotate(t.getPlaceGrid(), a));
    	t.setMeepleGrid(rotate(t.getMeepleGrid(), a));
    	//rotateMeepleLocations(t, a);
    	if(t.getRiver()!=null)
    		rotateRiver(t.getRiver(), a);
    	t.incNumRotation();
    	if(t.getCities()!=null)
    	{	
	    	for(int i=0; i<t.getCities().size(); i++)
	    	{
	    		rotate(t.getCities().get(i).getGrid(), a);
	    	}
	    	for(int i=0; i<t.getCities().size(); i++)
	    	{
	    		rotateFeature(t.getCities().get(i).getFeatureEnds(), a);
	    	}
	    	t.setCityEnds();
    	}
    	if(t.getRoads()!=null)
    	{
	    	for(int i=0; i<t.getRoads().size(); i++)
	    	{
	    		rotateFeature(t.getRoads().get(i).getFeatureEnds(), a);
	    	}
	    	t.setRoadEnds();
    	}
    	if(t.getFields()!=null)
    	{
	    	for(int i=0; i<t.getFields().size(); i++)
	    	{
	    		rotateFeatureField(t.getFields().get(i).getFeatureEnds(), a);
	    	}
	    	t.setFieldEnds();
    	}
    	rotateMeepleLocations(t, a);
    }
    public void rotateMeepleLocations(Tile t, int a)
    {
    	for(int i = 0; i < a/90; i++){
            rotate90MeepleLocations(t);
        }
    }
    //roadMeepleLocs, cityMeepleLocs, fieldMeepleLocs
    public void rotate90MeepleLocations(Tile t)
    {
    	ArrayList<City> cities = new ArrayList<City>();
    	ArrayList<Road> roads = new ArrayList<Road>();
    	ArrayList<Field> fields = new ArrayList<Field>();
    	if(t.getRoads() != null) {
    		roads = t.getRoads();
    		for(Road r : roads) {
    			ArrayList<Location> l = new ArrayList<>(r.getMeepleLocs());
    			ArrayList<Location> updatedLocs = new ArrayList<>();
    			for(Location loc : l) {
    				//swaps x and y
    				int x = loc.getX();
    				int y = loc.getY();
    				loc.setLoc(y, x);
    				// now we swap the top locations with the bottom locations the y location is already in the correct spot so we just need to change the x
    				// rotates counterclockwise
    				if(loc.getX() == 0) {
    					loc.setX(2);
    				}else if(loc.getX() == 2) {
    					loc.setX(0);
    				}
    				/*moves swaps the y location to perform 90 rotation clockwise
    				if(loc.getX() == 0 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 0 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 1 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 1 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 2 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 2 && loc.getY() == 2) {
    					loc.setY(0);
    				}*/
    				updatedLocs.add(loc);
    			}
    			r.setMeepleLocs(updatedLocs);
    		}
    		}
    	if(t.getCities() != null) {
        	cities = t.getCities();
    		for(City c : cities) {
    			ArrayList<Location> l = new ArrayList<>(c.getMeepleLocs());
    			ArrayList<Location> updatedLocs = new ArrayList<>();
    			for(Location loc : l) {
    				//swaps x and y
    				int x = loc.getX();
    				int y = loc.getY();
    				loc.setLoc(y, x);
    				// now we swap the top locations with the bottom locations the y location is already in the correct spot so we just need to change the x
    				// rotates counterclockwise
    				if(loc.getX() == 0) {
    					loc.setX(2);
    				}else if(loc.getX() == 2) {
    					loc.setX(0);
    				}
    				
    				/*moves swaps the y location to perform 90 rotation clockwise
    				if(loc.getX() == 0 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 0 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 1 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 1 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 2 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 2 && loc.getY() == 2) {
    					loc.setY(0);
    				}*/
    				updatedLocs.add(loc);
    			}
    			c.setMeepleLocs(updatedLocs);
    		}
        	}
    	if(t.getFields() != null) {
    		fields = t.getFields();
    		for(Field f : fields) {
    			ArrayList<Location> l = new ArrayList<>(f.getMeepleLocs());
    			ArrayList<Location> updatedLocs = new ArrayList<>();
    			for(Location loc : l) {
    				//swaps x and y
    				int x = loc.getX();
    				int y = loc.getY();
    				loc.setLoc(y, x);
    				// now we swap the top locations with the bottom locations the y location is already in the correct spot so we just need to change the x
    				// rotates counterclockwise
    				if(loc.getX() == 0) {
    					loc.setX(2);
    				}else if(loc.getX() == 2) {
    					loc.setX(0);
    				}
    				/*moves swaps the y location to perform 90 rotation clockwise
    				if(loc.getX() == 0 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 0 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 1 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 1 && loc.getY() == 2) {
    					loc.setY(0);
    				}else if(loc.getX() == 2 && loc.getY() == 0) {
    					loc.setY(2);
    				}else if(loc.getX() == 2 && loc.getY() == 2) {
    					loc.setY(0);
    				}*/
    				updatedLocs.add(loc);
    			}
    			f.setMeepleLocs(updatedLocs);
    		}
        	}
    }
    
    public void rotateRiver(River r, int a)
    {
    	for(int i = 0; i < a/90; i++){
            rotate90River(r);
        }
    }
    
    public void rotate90River(River r)
    {
    	FeatureEnd s=r.getStartLocation();
    	FeatureEnd e=r.getEndLocation();
    	if(s!=null)
    	{
		if(s.getDirection().equals("U"))
			s.setDirection("L");
		else if(s.getDirection().equals("L"))
			s.setDirection("D");
		else if(s.getDirection().equals("D"))
			s.setDirection("R");
		else if(s.getDirection().equals("R"))
			s.setDirection("U");
    	}
		if(e!=null)
		{
			if(e.getDirection().equals("U"))
				e.setDirection("L");
			else if(e.getDirection().equals("L"))
				e.setDirection("D");
			else if(e.getDirection().equals("D"))
				e.setDirection("R");
			else if(e.getDirection().equals("R"))
				e.setDirection("U");
		}
    }
    
    public void rotateFeature(ArrayList<FeatureEnd> f, int a)
    {
    	for(int i = 0; i < a/90; i++){
            rotate90(f);
        }
    }
    
    public void rotate90(ArrayList<FeatureEnd> f)
    {
    	for(int i=0; i<f.size(); i++)
    	{
    		FeatureEnd fe=f.get(i);
    		if(fe.getDirection().equals("U"))
    			fe.setDirection("L");
    		else if(fe.getDirection().equals("L"))
    			fe.setDirection("D");
    		else if(fe.getDirection().equals("D"))
    			fe.setDirection("R");
    		else
    			fe.setDirection("U");
    	}
    }
    
    public void rotateFeatureField(ArrayList<FeatureEnd> f, int a)
    {
    	for(int i = 0; i < a/90; i++){
            rotate90Field(f);
        }
    }
    
    public void rotate90Field(ArrayList<FeatureEnd> f)
    {
    	for(int i=0; i<f.size(); i++)
    	{
    		FeatureEnd fe=f.get(i);
    		if(fe.getDirection().equals("UR"))
    			fe.setDirection("LU");
    		else if(fe.getDirection().equals("UL"))
    			fe.setDirection("LD");
    		else if(fe.getDirection().equals("LU"))
    			fe.setDirection("DL");
    		else if(fe.getDirection().equals("LD"))
    			fe.setDirection("DR");
    		else if(fe.getDirection().equals("DL"))
    			fe.setDirection("RD");
    		else if(fe.getDirection().equals("DR"))
    			fe.setDirection("RU");
    		else if(fe.getDirection().equals("RD"))
    			fe.setDirection("UR");
    		else
    			fe.setDirection("UL");
    	}
    }
    
    public static BufferedImage rotate(BufferedImage image, int a) {
        double angle = 0.0;
        if(a == 0 || a == 360)
            return image;
        if(a == 90)
            angle = 67.544;
        if(a == 180)
            angle = 135.0885;
        if(a == 270)
            angle = 202.6325;
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }
    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
    public String[][] rotate(String[][] list, int a){
        for(int i = 0; i < a/90; i++){
            rotate90(list);
        }
        return list;
    }
    private String[][] rotate90(String[][] list){
        for(int r = 0; r < 3/2; r++){
            for(int c = r; c < 2 - r; c++){
                String temp = list[r][c];
                list[r][c] = list[c][2 - r];
                list[c][3 - (r + 1)] = list[2 - r][2 - c];
                list[2 - r][2 - c] = list[2 - c][r];
                list[2 - c][r] = temp;
            }
        }
        return list;
    }
    
}
