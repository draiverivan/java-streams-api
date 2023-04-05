package ua.foxminded.javaspring.javastreamsapi;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartQ1Top15 {

	private static Logger logger = LoggerFactory.getLogger(StartQ1Top15.class.getName());
	static List<Racer> racers;
	private static final int TOP15RACERS = 15;

	private static void printRacer(Racer racer, int maxNameWidth, int maxTeamWidth, int maxDurationWidth,
			int position) {
		logger.info(
				String.format("%-2s | %-" + maxNameWidth + "s | %-" + maxTeamWidth + "s | %-" + maxDurationWidth + "s",
						position, racer.getName(), racer.getTeam(), racer.getDurationFormatted()));
	}

	public static void main(String[] args) {

		HandleDataQ1Formula1 handleDataQ1Formula1 = new HandleDataQ1Formula1();
		try {
			racers = handleDataQ1Formula1.parseAbbreviations("src/main/resources/abbreviations.txt");
			handleDataQ1Formula1.parseStart("src/main/resources/start.log", racers);
			handleDataQ1Formula1.parseEnd("src/main/resources/end.log", racers);
		} catch (IOException e) {
			logger.error("An error occurred while parsing: {}", e.getStackTrace());
		}

		handleDataQ1Formula1.addDurationLap(racers);
		racers = handleDataQ1Formula1.sortByDurationLap(racers);
		handleDataQ1Formula1.formatDurationLap(racers);

		// Get maximum width for each column
		int maxNameWidth = racers.stream().mapToInt(racer -> racer.getName().length()).max().orElse(0);
		int maxTeamWidth = racers.stream().mapToInt(racer -> racer.getTeam().length()).max().orElse(0);
		int maxDurationWidth = racers.stream().mapToInt(racer -> racer.getDurationFormatted().length()).max().orElse(0);

		// Print Top15 racers
		for (int i = 0; i < TOP15RACERS; i++) {
			Racer racer = racers.get(i);
			printRacer(racer, maxNameWidth, maxTeamWidth, maxDurationWidth, (i + 1));
		}

		// Print underline
		StringBuilder underline = new StringBuilder();
		for (int i = 0; i < maxNameWidth + maxTeamWidth + maxDurationWidth + 11; i++) {
			underline.append("-");
		}
		logger.info(underline.toString());

		// Print remaining racers
		for (int i = TOP15RACERS; i < racers.size(); i++) {
			Racer racer = racers.get(i);
			printRacer(racer, maxNameWidth, maxTeamWidth, maxDurationWidth, (i + 1));
		}
	}
}
