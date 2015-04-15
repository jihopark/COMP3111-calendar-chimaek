package hkust.cse.calendar.diskstorage;

public interface JsonStorable {
	
	public String getFileName();
	
	public Object loadFromJson();
	
	public void saveToJson();
}
