import java.util.ArrayList;
import java.awt.image.*;

public class GameLog {
	private ArrayList<String> messages;
	public GameLog()
	{
		messages=new ArrayList<>();
	}
	public void addRoadMessage(MainRoad r, Player p)
	{
		add(p.getName()+" got "+r.getScore()+" points by completing a road.");
	}
	public void addCityMessage(MainCity c, Player p)
	{
		add(p.getName()+" got "+c.getScore()+" points by completing a city.");
	}
	public void addMonasteryMessage(MainMonastery m, Player p)
	{
		add(p.getName()+" got "+m.scoreMonastery()+" points by having their monastery completed.");
	}
	public void addFieldMessage(MainField f, Player p)
	{
		add(p.getName()+" got "+f.getScore()+" points for their field.");
	}
	public void addInvalidLoc()
	{
		add("That is not a valid location.");
	}
	public void addClaimed()
	{
		add("All features of this tile are claimed.");
	}
	public void addCannotPlace()
	{
		add("You cannot place yet.");
	}
	public void addCannotDiscard()
	{
		add("You cannot discard this tile.");
	}
	public void addCannotNotClaim()
	{
		add("You cannot press N yet because you haven't placed yet.");
	}
	public void addNoMeeples()
	{
		add("You have no meeples left to use.");
	}
	public void addMessage(String s)
	{
		add(s);
	}
	public ArrayList<String> getMessages()
	{
		return messages;
	}
        private void add(String s){
            if(s.length() > 40){
                do{
                    messages.add(s.substring(0, 40));
                    s = s.substring(40);
                }
                while(s.length() > 40);
            }
            if(s.length() > 0){
                messages.add(s);
            }
        }
}
