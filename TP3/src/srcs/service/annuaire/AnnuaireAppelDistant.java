package srcs.service.annuaire;

import java.util.HashMap;
import java.util.Map;

import srcs.service.AppelDistant;
import srcs.service.EtatGlobal;

@EtatGlobal
public class AnnuaireAppelDistant implements AppelDistant, Annuaire{

private Map<String, String> annuaires = new HashMap<String, String>();
	
	public String lookup(String name) {
		if(!annuaires.containsKey(name)) {
			return "";
		}
		return annuaires.get(name);
	}
	
	public void bind(String name, String value) {
		annuaires.put(name, value);
	}
	
	public void unbind(String name) {
		annuaires.remove(name);
	}
}
