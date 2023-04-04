package ua.foxminded.javaspring.javastreamsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class HandleDataQ1Formula1Test {

	private HandleDataQ1Formula1 handleDataQ1Formula1 = new HandleDataQ1Formula1();
	List<Racer> racers = new ArrayList<>();

	@Test
	void parseAbbreviations() {
		String abbreviationsFile = "src/main/resources/abbreviations.txt";
		racers = handleDataQ1Formula1.parseAbbreviations(abbreviationsFile);
		assertEquals(19, racers.size());
		assertEquals("DRR", racers.get(0).getAbbreviation());
		assertEquals("Daniel Ricciardo", racers.get(0).getName());
		assertEquals("RED BULL RACING TAG HEUER", racers.get(0).getTeam());
		assertEquals("EOF", racers.get(5).getAbbreviation());
		assertEquals("Kimi Raikkonen", racers.get(3).getName());
		assertEquals("HAAS FERRARI", racers.get(18).getTeam());
	}

	@Test
	void parseStartReturnsExpectedListWhenGivenValidInput() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		expected.get(0).setStartTime("2018-05-24_12:02:58.917");
		expected.get(1).setStartTime("2018-05-24_12:04:03.332");
		List<Racer> actual = handleDataQ1Formula1.parseStart("src/main/resources/start.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void parseStartReturnsInputListUnchangedWhenGivenEmptyFile() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		List<Racer> actual = handleDataQ1Formula1.parseStart("empty.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void parseStartReturnsInputListUnchangedWhenGivenFileWithUnknownAbbreviation() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		List<Racer> actual = handleDataQ1Formula1.parseStart("unknown.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void parseStartReturnsInputListUnchangedWhenGivenFileWithInvalidFormat() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		List<Racer> actual = handleDataQ1Formula1.parseStart("invalid.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void parseEndReturnsExpectedListWhenGivenValidInput() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		expected.get(0).setStartTime("2018-05-24_12:02:58.917");
		expected.get(1).setStartTime("2018-05-24_12:04:03.332");
		List<Racer> actual = handleDataQ1Formula1.parseEnd("src/main/resources/end.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void parseEndReturnsInputListUnchangedWhenGivenFileWithInvalidFormat() throws IOException {
		racers.add(new Racer("AAA", "John Doe", "Team A"));
		racers.add(new Racer("BBB", "Jane Doe", "Team B"));
		List<Racer> expected = new ArrayList<>(racers);
		List<Racer> actual = handleDataQ1Formula1.parseEnd("invalid.log", racers);
		assertEquals(expected, actual);
	}

	@Test
	void addDurationLapWithValidTimeFormat() {
		// Arrange
		Racer racer1 = new Racer("RAC", "Racer 1", "Team 1");
		racer1.setStartTime("2023-04-04_10:00:00.000");
		racer1.setEndTime("2023-04-04_10:01:00.000");
		Racer racer2 = new Racer("DRV", "Driver 2", "Team 2");
		racer2.setStartTime("2023-04-04_10:01:00.000");
		racer2.setEndTime("2023-04-04_10:02:00.000");
		racers.add(racer1);
		racers.add(racer2);

		// Act
		List<Racer> racersWithDuration = handleDataQ1Formula1.addDurationLap(racers);

		// Assert
		assertEquals(2, racersWithDuration.size());
		assertEquals(60_000, racersWithDuration.get(0).getDuration());
		assertEquals(60_000, racersWithDuration.get(1).getDuration());
	}

	@Test
	void addDurationLapWithInvalidTimeFormat() {
		// Arrange
		Racer racer1 = new Racer("RAC", "Racer 1", "Team 1");
		racer1.setStartTime("2023-04-04_10:00:00.000");
		racer1.setEndTime("2023-04-04_10:01:00.000");
		Racer racer2 = new Racer("DRV", "Driver 2", "Team 2");
		racer2.setStartTime("invalid_start_time");
		racer2.setEndTime("invalid_end_time");
		racers.add(racer1);
		racers.add(racer2);

		// Act and Assert
		assertThrows(DateTimeParseException.class, () -> {
			handleDataQ1Formula1.addDurationLap(racers);
		});
	}

	@Test
	void sortByDurationLap() throws IOException {
		racers = handleDataQ1Formula1.parseAbbreviations("src/main/resources/abbreviations.txt");
		racers = handleDataQ1Formula1.parseStart("src/main/resources/start.log", racers);
		racers = handleDataQ1Formula1.parseEnd("src/main/resources/end.log", racers);
		racers = handleDataQ1Formula1.addDurationLap(racers);
		List<Racer> sortedRacers = handleDataQ1Formula1.sortByDurationLap(racers);
		assertEquals("SVF", sortedRacers.get(0).getAbbreviation());
		assertEquals("DRR", sortedRacers.get(1).getAbbreviation());
		assertEquals("VBM", sortedRacers.get(2).getAbbreviation());
	}

	@Test
	void sortByDurationLapWithEmptyList() {
		List<Racer> emptyList = new ArrayList<>();
		List<Racer> sortedRacers = handleDataQ1Formula1.sortByDurationLap(emptyList);
		assertTrue(sortedRacers.isEmpty());
	}

	@Test
	void sortByDurationLapWithNullList() {
		List<Racer> nullList = null;
		assertThrows(NullPointerException.class, () -> handleDataQ1Formula1.sortByDurationLap(nullList));
	}

	@Test
	void formatDurationLap_shouldFormatDurationCorrectly() {
		// Arrange
		racers.add(new Racer("VET", "Sebastian Vettel", "FERRARI", "2018-05-24_12:02:58.917", "2018-05-24_12:04:03.332",
				95415));
		racers.add(new Racer("RAI", "Kimi Raikkonen", "FERRARI", "2018-05-24_12:13:04.512", "2018-05-24_12:14:17.169",
				72757));

		// Act
		List<Racer> racersWithDurationFormatted = handleDataQ1Formula1.formatDurationLap(racers);

		// Assert
		assertEquals("01:35.415", racersWithDurationFormatted.get(0).getDurationFormatted());
		assertEquals("01:12.757", racersWithDurationFormatted.get(1).getDurationFormatted());
	}

	@Test
	void formatDurationLap_shouldReturnEmptyListWhenGivenEmptyList() {
		// Act
		List<Racer> racersWithDurationFormatted = handleDataQ1Formula1.formatDurationLap(racers);

		// Assert
		assertEquals(0, racersWithDurationFormatted.size());
	}

}
