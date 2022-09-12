import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Testing {
	public static void main(String args[])
	{
		System.out.println((true || (false && (false && true) && false) || (false || (false && true)) ) && false);
		System.out.println(true || false && false);
		System.out.println(true || (false && false));
		System.out.println((true || false) && false);
		Scanner scan=new Scanner(System.in);
		String[] lol = {"Me", "Myself", "And I"};
		System.out.println(lol);
		 for(int i=0; i<10; i++)
		 {
		      Random r = new Random();
		      int x=r.nextInt(26);
		      char c = (char)(x + 'a');
		      System.out.println(x+" "+c);
		 }
		 char a=(char)(1+'a');
		 System.out.println("\n"+a+"\ndone\n");
		 ArrayList<Character> words=new ArrayList<>();
		 for(int i=0; i<5; i++)
		 {
			 for(int y=0; y<6; y++)
			 {
				 Random r = new Random();
			     char c = (char)(r.nextInt(26) + 'a');
			     words.add(c);
			 }
			 String word="";
			 for(char c:words)
			 {
				 word+=c;
			 }
			 System.out.println(word);
			 words.clear();
		 }
		 ArrayList<Integer> nums=new ArrayList<>(Arrays.asList(1, 2, 3));
		 ArrayList<ArrayList<Integer>> graph=new ArrayList<>();
		 graph.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
		 graph.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
		 System.out.println(graph.get(1).get(1));
		 
	}
}
