package com.changestuffs.shared.constants;

public enum Locales {
	en, es;
	
	public String getPair(){
		return RequestParams.locale.name()+"="+name();
	}
	
	public static Locales getLocale(String name){
		for(Locales locale : Locales.values()){
			if(locale.name().equals(name))
				return locale;
		}
		return en;
	}
	
}
