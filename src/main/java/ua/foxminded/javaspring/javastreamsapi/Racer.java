package ua.foxminded.javaspring.javastreamsapi;

public class Racer {

	String abbreviation;
	String name;
	String team;
	String startTime;
	String endTime;
	long duration;
	String durationFormatted;

	public Racer(String abbreviation, String name, String team, String startTime, String endTime, long duration) {

		this.abbreviation = abbreviation;
		this.name = name;
		this.team = team;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
	}

	public Racer(String abbreviation, String name, String team) {

		this.abbreviation = abbreviation;
		this.name = name;
		this.team = team;
	}

}
