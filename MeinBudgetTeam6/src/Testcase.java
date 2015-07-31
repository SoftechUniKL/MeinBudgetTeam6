import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Testcase {

	BudgetPlanModel bpm = new BudgetPlanModel();
	
	@Test
	public void t_isDate() {
		assertFalse(bpm.isDate("test"));
		assertFalse(bpm.isDate("30.02.2015"));
		assertFalse(bpm.isDate("30.30.2015"));
		assertFalse(bpm.isDate("-30.02.2015"));
		
	}
	@Test 
	public void t_save(){
		int gs = bpm.gesamt.size();
		
		try{
			bpm.save("10.10.2010"," Testcase", "2000", "-",0,1);
		}
		catch (Exception e){
		}
		
		bpm.refresh();

		assertEquals(gs+1, bpm.gesamt.size());
		
		bpm.remove(gs);
		
		assertEquals(gs, bpm.gesamt.size());
	}
	
	@Test
	public void t_kontostand(){
		double m = bpm.getKontostandM();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format_pattern = new SimpleDateFormat("dd.MM.yyyy");
		String formatiert = format_pattern.format(cal.getTime());
		
		//Einzahlungen
		bpm.save(formatiert," Testcase", "2000", "-",0,1);
		bpm.refresh();
		assertEquals(m+2000, bpm.getKontostandM(), 0.0001);
		bpm.remove(bpm.gesamt.size()-1);
		bpm.refresh();
		assertEquals(m, bpm.getKontostandM(), 0.0001);
		
		//Auszahlungen
		bpm.save(formatiert," Testcase", "2000", "-",0,2);
		bpm.refresh();
		assertEquals(m-2000, bpm.getKontostandM(), 0.0001);
		bpm.remove(bpm.gesamt.size()-1);
		
	}
}
