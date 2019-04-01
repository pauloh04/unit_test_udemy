package br.com.paulo.matchers;

import java.util.Calendar;

public class MatcherProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY); 
	}
	
	public static DiaSemanaMatcher ehHojeComDiferencaDias(Integer dias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return new DiaSemanaMatcher(cal.get(Calendar.DAY_OF_WEEK)); 
	}
	
	public static DiaSemanaMatcher ehHoje() {
		return ehHojeComDiferencaDias(0); 
	}
}
