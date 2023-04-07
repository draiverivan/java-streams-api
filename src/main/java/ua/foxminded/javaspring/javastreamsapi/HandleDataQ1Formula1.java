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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleDataQ1Formula1 {

	private static final Logger logger = LoggerFactory.getLogger(HandleDataQ1Formula1.class.getName());
	private static final String DATAFORMAT = "yyyy-MM-dd_HH:mm:ss.SSS";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATAFORMAT);

	// method for parse file 'abbreviations'
	public List<Racer> parseAbbreviations(String abbreviationsFile) throws IOException {

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
			logger.error("An error occurred while parsing the abbreviations file: {}", e.getMessage(), e);
			throw new DataParsingException(e.getMessage(), e);
		}
		return racers;
	}

	// method for parse files
	private List<Racer> parseFile(String file, List<Racer> racers, String setTime) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			reader.lines().forEach(line -> {
				String abbreviation = line.substring(0, 3);
				String time = line.substring(3);
				racers.stream().filter(racer -> racer.getAbbreviation().equals(abbreviation)).findFirst()
						.ifPresent(racer -> {
							if ("setStartTime".equals(setTime)) {
								racer.setStartTime(time);
							} else if ("setEndTime".equals(setTime)) {
								racer.setEndTime(time);
							}
						});
			});
		} catch (IOException e) {
			logger.error("An error occurred while parsing the file: {}", e.getMessage(), e);
			throw new DataParsingException(e.getMessage(), e);
		}
		return racers;
	}

	// method for parse file 'start'
	public List<Racer> parseStart(String startTimeFile, List<Racer> racers) throws IOException {
		String setStartTime = "setStartTime";
		return parseFile(startTimeFile, racers, setStartTime);
	}

	// method for parse file 'end'
	public List<Racer> parseEnd(String endTimeFile, List<Racer> racers) throws IOException {
		String setEndTime = "setEndTime";
		return parseFile(endTimeFile, racers, setEndTime);
	}

	// method for add duration lap
	public List<Racer> addDurationLap(List<Racer> racers) {

		return racers.stream().map(racer -> {
			LocalDateTime start = LocalDateTime.parse(racer.getStartTime(), FORMATTER);
			LocalDateTime end = LocalDateTime.parse(racer.getEndTime(), FORMATTER);
			long duration = Duration.between(start, end).toMillis();
			racer.setDuration(duration);
			return racer;
		}).collect(Collectors.toList());
	}

	// method for sort 'Racer' List by duration of lap from less to greater
	public List<Racer> sortByDurationLap(List<Racer> racers) {
		Comparator<Racer> byDuration = Comparator.comparingLong(Racer::getDuration);
		return racers.stream().sorted(byDuration).collect(Collectors.toList());
	}

	// method for format duration lap in string
	public List<Racer> formatDurationLap(List<Racer> racers) {
		return racers.stream().map(racer -> {
			String durationFormatted = String.format("%02d:%06.3f",
					TimeUnit.MILLISECONDS.toMinutes(racer.getDuration()),
					TimeUnit.MILLISECONDS.toSeconds(racer.getDuration()) % 60
							+ TimeUnit.MILLISECONDS.toMillis(racer.getDuration()) % 1000 / 1000.0);
			racer.setDurationFormatted(durationFormatted);
			return racer;
		}).collect(Collectors.toList());
	}

}
