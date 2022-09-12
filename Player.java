import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player {
//Attributes
	private ArrayList<Meeple> MeepArr = new ArrayList<Meeple>();
	 private BufferedImage pic;
	 private String name;
	 private int score;
	 private Boolean choseCity, choseRoad, choseMonastery, choseField;
	 
	 public Player()
	 {
		name="";
		score=0;
		choseCity = false;
		choseRoad = false;
		choseMonastery = false;
		choseField = false;
		MeepArr=new ArrayList<>();
		pic=null;
	 }
	 
	public Player(String x, BufferedImage p) {
		name = x;
		score = 0;
		choseCity = false;
		choseRoad = false;
		choseMonastery = false;
		choseField = false;
		//adds 7 meeples to each player array with the players name and BufferedImage 
		for(int i = 0; i < 7; i++) {
			Meeple m = new Meeple(x,p,10, 10);
			MeepArr.add(m);
		}
		pic=p;
		
	}
	
	//EDITING MEEPARR
	public void removeMeep(int i) {MeepArr.remove(i);}
	public void addMeep(Meeple m) {MeepArr.add(m);}
	public Meeple getMeep(int i) {return MeepArr.get(i);}
	public int getMeepLength() {return MeepArr.size();}
    public ArrayList<Meeple> getMeepArr(){return MeepArr;} 
	public boolean canUseMeeple()//this will tell whether or not a player can use a meeple, and if they can, one will be used
	{
		ArrayList<Meeple> meeps=getMeepArr();
		for(int i=6; i>-1; i--)
		{
			Meeple m=meeps.get(i);
			if(!m.getUsed())
			{
				return true;
			}
		}
		return false;
		
	}
    public Meeple getUsableMeeple()
    {
    	ArrayList<Meeple> meeps=getMeepArr();
		for(int i=6; i>-1; i--)
		{
			Meeple m=meeps.get(i);
			if(!m.getUsed())
			{
				m.setUsed(true);
				return m;
			}
		}
		return null;
    }
	// SET METHODS
	public void setScore(int i) {score = i;}
	public void setName(String n) {name = n;}
	public void setchoseCity(Boolean b) {choseCity = b;}
	public void setchoseRoad(Boolean b) {choseRoad = b;}
	public void setchoseMonastery(Boolean b) {choseMonastery = b;}
	public void setchoseField(Boolean b) { choseField = b;}

	// GET METHODS
	public BufferedImage getPic() {return pic;}
	public int getScore() {return score;}
	public String getName() {return name;}
	public Boolean getchoseCity() {return choseCity;}
	public Boolean getchoseRoad() {return choseRoad;}
	public Boolean getchoseMonastery() { return choseMonastery;}
	public Boolean getchoseField() {return choseField;}
	public String toString(){ return name.substring(7); }
}
