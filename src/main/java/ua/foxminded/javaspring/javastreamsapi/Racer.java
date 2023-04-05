package ua.foxminded.javaspring.javastreamsapi;

public class Racer {

	private String abbreviation;
	private String name;
	private String team;
	private String startTime;
	private String endTime;
	private long duration;
	private String durationFormatted;

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

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getDurationFormatted() {
		return durationFormatted;
	}

	public void setDurationFormatted(String durationFormatted) {
		this.durationFormatted = durationFormatted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
