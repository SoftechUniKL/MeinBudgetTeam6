import java.util.Comparator;
public class SortDate implements Comparator<Posten>{

	  @Override
	  public int compare(Posten p1, Posten p2) {
		  return p1.getDatum().compareTo(p2.getDatum());
	  }
	
}
