package pl.edu.agh.eis.datasource;

public enum ProfileType {
	SILENT, VIBRATION, LOUD;
	private static ProfileType[] allValues = values();
	public static ProfileType fromOrdinal(int n) { return allValues[n]; }
}
