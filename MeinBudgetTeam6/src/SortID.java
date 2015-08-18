import java.util.Comparator;
public class SortID implements Comparator<Posten>{

	  @Override
	  public int compare(Posten p1, Posten p2) {
		  return Long.compare(p1.getID(),p2.getID());
	  }
	
}
