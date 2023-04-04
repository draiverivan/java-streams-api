package ua.foxminded.javaspring.javastreamsapi;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartQ1Top15 {

	private static Logger logger = LoggerFactory.getLogger(StartQ1Top15.class.getName());

	public static void main(String[] args) throws IOException {

		Q1Top15 q1Top15 = new Q1Top15();
		List<Racer> racers = q1Top15.parseAbbreviations("src/main/resources/abbreviations.txt");
		racers = q1Top15.parseStart("src/main/resources/start.log", racers);
		racers = q1Top15.parseEnd("src/main/resources/end.log", racers);
		racers = q1Top15.addDurationLap(racers);
		racers = q1Top15.sortByDurationLap(racers);
		racers = q1Top15.formatDurationLap(racers);

		// Get maximum width for each column
		int maxNameWidth = racers.stream().mapToInt(racer -> racer.name.length()).max().orElse(0);
		int maxTeamWidth = racers.stream().mapToInt(racer -> racer.team.length()).max().orElse(0);
		int maxDurationWidth = racers.stream().mapToInt(racer -> racer.durationFormatted.length()).max().orElse(0);

		// Print racers
		for (int i = 0; i < 15; i++) {
			Racer racer = racers.get(i);
			logger.info(String.format(
					"%-2s | %-" + maxNameWidth + "s | %-" + maxTeamWidth + "s | %-" + maxDurationWidth + "s", (i + 1),
					racer.name, racer.team, racer.durationFormatted));
		}

		// Print underline
		StringBuilder underline = new StringBuilder();
		for (int i = 0; i < maxNameWidth + maxTeamWidth + maxDurationWidth + 11; i++) {
			underline.append("-");
		}
		logger.info(underline.toString());

		// Print remaining racers
		for (int i = 15; i < racers.size(); i++) {
			Racer racer = racers.get(i);
			logger.info(String.format(
					"%-2s | %-" + maxNameWidth + "s | %-" + maxTeamWidth + "s | %-" + maxDurationWidth + "s", (i + 1),
					racer.name, racer.team, racer.durationFormatted));
		}
	}
}
