package ua.foxminded.javaspring.javastreamsapi;

/*This is Java Formula 1 application, the first stage of the qualification. 
It prints report that shows the top 15 racers, which go to the Q2 stage and the rest ones after the underline.*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Q1Top15 {

	String dataFormat = "yyyy-MM-dd_HH:mm:ss.SSS";

	// method for parse file 'abbreviations'
	public List<Racer> parseAbbreviations(String abbreviationsFile) {

		List<Racer> racers = new ArrayList<>();

		try (BufferedReader abbreviationsReader = new BufferedReader(new FileReader(abbreviationsFile))) {
			abbreviationsReader.lines().forEach(line -> {
				String[] parts = line.split("_");
				String abbreviation = parts[0];
				String name = parts[1];
				String team = parts[2];
				Racer racer = new Racer(abbreviation, name, team);
				racers.add(racer);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return racers;
	}

	// method for parse file 'start'
	public List<Racer> parseStart(String startTimeFile, List<Racer> racers) throws IOException {

		try (BufferedReader startReader = new BufferedReader(new FileReader(startTimeFile))) {
			startReader.lines().forEach(line -> {
				String abbreviation = line.substring(0, 3);
				String startTime = line.substring(3);
				racers.stream().filter(racer -> racer.abbreviation.equals(abbreviation)).findFirst()
						.ifPresent(racer -> racer.startTime = startTime);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return racers;
	}

	// method for parse file 'start'
	public List<Racer> parseEnd(String endTimeFile, List<Racer> racers) {

		try (BufferedReader endReader = new BufferedReader(new FileReader(endTimeFile))) {
			endReader.lines().forEach(line -> {
				String abbreviation = line.substring(0, 3);
				String endTime = line.substring(3);
				racers.stream().filter(racer -> racer.abbreviation.equals(abbreviation)).findFirst()
						.ifPresent(racer -> racer.endTime = endTime);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return racers;
	}

	// method for add duration lap
	public List<Racer> addDurationLap(List<Racer> racers) {

		racers.stream().forEach(racer -> {
			long duration = Duration
					.between(LocalDateTime.parse(racer.startTime, DateTimeFormatter.ofPattern(dataFormat)),
							LocalDateTime.parse(racer.endTime, DateTimeFormatter.ofPattern(dataFormat)))
					.toMillis();
			racer.duration = duration;
		});
		return racers;

	}

	// method for sort 'Racer' List by duration of lap from less to greater
	public List<Racer> sortByDurationLap(List<Racer> racers) {
		Comparator<Racer> byDuration = Comparator.comparingLong(racer -> racer.duration);
		return racers.stream().sorted(byDuration).collect(Collectors.toList());
	}

	// method for format duration lap in string
	public List<Racer> formatDurationLap(List<Racer> racers) {
		return racers.stream().map(racer -> {
			String durationFormatted = String.format("%02d:%06.3f", TimeUnit.MILLISECONDS.toMinutes(racer.duration),
					TimeUnit.MILLISECONDS.toSeconds(racer.duration) % 60
							+ TimeUnit.MILLISECONDS.toMillis(racer.duration) % 1000 / 1000.0);
			racer.durationFormatted = durationFormatted;
			return racer;
		}).collect(Collectors.toList());
	}

}
