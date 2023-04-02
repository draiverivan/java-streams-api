package ua.foxminded.javaspring.javastreamsapi;

/*This is Java Formula 1 application, the first stage of the qualification. 
It prints report that shows the top 15 racers, which go to the Q2 stage and the rest ones after the underline.*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Q1Top15 {

	static Logger logger = LoggerFactory.getLogger(Q1Top15.class);
	private Map<String, Map<String, Object>> racerDataMap = new HashMap<>();

	// constructor
	public Q1Top15(String abbreviationsFile, String startTimeFile, String endTimeFile) throws IOException {
		parseLogs(abbreviationsFile, startTimeFile, endTimeFile);
	}

	// method for parse files, and store data 'fullName', 'team' and
	// 'durationInMillis' in 'racerDataMap'
	public Map<String, Map<String, Object>> parseLogs(String abbreviationsFile, String startTimeFile,
			String endTimeFile) throws IOException {

		Map<String, String[]> abbreviations = new HashMap<>();
		BufferedReader abbreviationsReader = new BufferedReader(new FileReader(abbreviationsFile));
		String line;
		while ((line = abbreviationsReader.readLine()) != null) {
			String[] parts = line.split("_");
			String abbreviation = parts[0];
			String[] racer = new String[] { parts[1], parts[2] };
			abbreviations.put(abbreviation, racer);
		}
		abbreviationsReader.close();

		Map<String, String> startTimes = new HashMap<>();
		BufferedReader startReader = new BufferedReader(new FileReader(startTimeFile));
		while ((line = startReader.readLine()) != null) {
			String abbreviation = line.substring(0, 3);
			String startTime = line.substring(3);
			startTimes.put(abbreviation, startTime);
		}
		startReader.close();

		Map<String, String> endTimes = new HashMap<>();
		BufferedReader endReader = new BufferedReader(new FileReader(endTimeFile));
		while ((line = endReader.readLine()) != null) {
			String abbreviation = line.substring(0, 3);
			String endTime = line.substring(3);
			endTimes.put(abbreviation, endTime);
		}
		endReader.close();

		abbreviations.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
			String abbreviation = entry.getKey();
			String[] racer = entry.getValue();
			String fullName = racer[0];
			String team = racer[1];
			String startTime = startTimes.getOrDefault(abbreviation, "");
			String endTime = endTimes.getOrDefault(abbreviation, "");
			if (!startTime.isEmpty() && !endTime.isEmpty()) {
				long durationInMillis = Duration
						.between(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS")),
								LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS")))
						.toMillis();

				Map<String, Object> racerData = new HashMap<>();
				racerData.put("team", team);
				racerData.put("duration", durationInMillis);
				racerDataMap.put(fullName, racerData);
			}
		});
		return racerDataMap;
	}

	// method for sort 'racerDataMap' by duration of lap from less to greater
	public Map<String, Map<String, Object>> sortByDurationLap() {
		racerDataMap = racerDataMap.entrySet().stream()
				.sorted(Comparator.comparingLong(entry -> (Long) entry.getValue().get("duration")))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));
		return racerDataMap;
	}

	public static void main(String[] args) throws IOException {

		Q1Top15 racerData = new Q1Top15("src/main/resources/abbreviations.txt", "src/main/resources/start.log",
				"src/main/resources/end.log");
		racerData.sortByDurationLap();

		// Find the widest values for each column
		int maxFullNameWidth = racerData.racerDataMap.keySet().stream().mapToInt(String::length).max().orElse(0);
		int maxTeamWidth = racerData.racerDataMap.values().stream()
				.mapToInt(racerData1 -> ((String) racerData1.get("team")).length()).max().orElse(0);
		int maxDurationWidth = racerData.racerDataMap.values().stream().mapToInt(racerData1 -> {
			Long duration = (Long) racerData1.get("duration");
			return String.format("%02d:%06.3f", TimeUnit.MILLISECONDS.toMinutes(duration),
					TimeUnit.MILLISECONDS.toSeconds(duration) % 60
							+ TimeUnit.MILLISECONDS.toMillis(duration) % 1000 / 1000.0)
					.length();
		}).max().orElse(0);

		// Print the separator after the 15th line
		int lineCount = 1;
		for (Map.Entry<String, Map<String, Object>> entry : racerData.racerDataMap.entrySet()) {
			if (lineCount == 16) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < maxFullNameWidth + maxTeamWidth + maxDurationWidth + 12; i++) {
					sb.append("-");
				}
				logger.info(sb.toString());
			}

			String fullName = entry.getKey();
			Map<String, Object> racerData1 = entry.getValue();
			String team = (String) racerData1.get("team");
			Long duration = (Long) racerData1.get("duration");
			String formattedDuration = String.format("%02d:%06.3f", TimeUnit.MILLISECONDS.toMinutes(duration),
					TimeUnit.MILLISECONDS.toSeconds(duration) % 60
							+ TimeUnit.MILLISECONDS.toMillis(duration) % 1000 / 1000.0);

			logger.info(String.format(
					"%3d | %-" + maxFullNameWidth + "s | %-" + maxTeamWidth + "s | %-" + maxDurationWidth + "s",
					lineCount, fullName, team, formattedDuration));
			lineCount++;
		}
	}

}
