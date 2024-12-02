package srcs.service.annuaire;

public interface Annuaire {
	public abstract String lookup(String name);
	public abstract void bind(String name, String value);
	public abstract void unbind(String name);
}
