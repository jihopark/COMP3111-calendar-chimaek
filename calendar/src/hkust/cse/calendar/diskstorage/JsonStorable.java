package hkust.cse.calendar.diskstorage;

public interface JsonStorable {
	
	public String getFileName();
	
	public void loadFromJson();
	
	public void saveToJson();
}
