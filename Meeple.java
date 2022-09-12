import java.awt.image.BufferedImage;

public class Meeple {
//Attributes
	private BufferedImage pic;
	private int length, width;
	private Boolean Used;
	private String owner;
		public Meeple( String o, BufferedImage p, int l, int w) {
			pic = p;
			owner = o;
			Used = false;
			 length = l;
			 width = w;
		}
		//SET METHODS
		public void setImage(BufferedImage p) {pic = p;}
		public void setLength(int l) {length = l;}
		public void setWidth(int w) {width = w;}
		public void setUsed(Boolean b) {Used = b;}
		public void setOwner(String o) {owner = o;}
		//GET METHODS
		public BufferedImage getPic() {return pic;}
		public int getLength() {return length;}
		public int getWidth() {return width;}
		public Boolean getUsed() {return Used;}
		public String getOwner() {return owner;}
}
