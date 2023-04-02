package ua.foxminded.javaspring.javastreamsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Q1Top15Test {

	private Q1Top15 q1Top15;

	@BeforeEach
	void setUp() throws IOException {
		q1Top15 = new Q1Top15("src/main/resources/abbreviations.txt", "src/main/resources/start.log",
				"src/main/resources/end.log");
	}

	@Test
	void testParseLogs_checksIfCorrectlyParsesTheInputFiles() throws IOException {

		Map<String, Map<String, Object>> racerDataMap = q1Top15.parseLogs("src/main/resources/abbreviations.txt",
				"src/main/resources/start.log", "src/main/resources/end.log");

		Assertions.assertEquals(19, racerDataMap.size());

		Map<String, Object> racer1Data = racerDataMap.get("Lewis Hamilton");
		Assertions.assertNotNull(racer1Data);
		Assertions.assertEquals("MERCEDES", racer1Data.get("team"));
		Assertions.assertEquals(72460L, racer1Data.get("duration"));

		Map<String, Object> racer2Data = racerDataMap.get("Sebastian Vettel");
		Assertions.assertNotNull(racer2Data);
		Assertions.assertEquals("FERRARI", racer2Data.get("team"));
		Assertions.assertEquals(64415L, racer2Data.get("duration"));

		Map<String, Object> racer3Data = racerDataMap.get("Valtteri Bottas");
		Assertions.assertNotNull(racer3Data);
		Assertions.assertEquals("MERCEDES", racer3Data.get("team"));
		Assertions.assertEquals(72434L, racer3Data.get("duration"));
	}

	@Test
	void testSortByDurationLap_checksIfCorrectlySortsByDuration() throws IOException {

		Map<String, Map<String, Object>> racerDataMap = q1Top15.parseLogs("src/main/resources/abbreviations.txt",
				"src/main/resources/start.log", "src/main/resources/end.log");

		Map<String, Map<String, Object>> sortedRacerDataMap = q1Top15.sortByDurationLap();

		Assertions.assertEquals(sortedRacerDataMap.size(), racerDataMap.size());

		Map.Entry<String, Map<String, Object>> previousEntry = null;
		for (Map.Entry<String, Map<String, Object>> entry : sortedRacerDataMap.entrySet()) {
			if (previousEntry != null) {
				long previousDuration = (long) previousEntry.getValue().get("duration");
				long currentDuration = (long) entry.getValue().get("duration");
				Assertions.assertTrue(previousDuration <= currentDuration);
			}
			previousEntry = entry;
		}
	}

	@Test
	void testParseLogs_shouldThrowsIOException() {
		String invalidFilePath = "invalid/file/path";
		assertThrows(IOException.class, () -> new Q1Top15("abbreviations.txt", "start.log", invalidFilePath));
	}

	@Test
	void testParseLogs_shouldReturnMapWith19Entries() throws IOException {
		Map<String, Map<String, Object>> result = q1Top15.parseLogs("src/main/resources/abbreviations.txt",
				"src/main/resources/start.log", "src/main/resources/end.log");

		assertEquals(19, result.size());
	}

	@Test
	void testParseLogs_shouldVerifyingOutputMapIsNotNullNotEmptyAndContainsTheExpectedNumberOfEntries()
			throws IOException {
		Map<String, Map<String, Object>> racerDataMap = null;

		racerDataMap = q1Top15.parseLogs("src/main/resources/abbreviations.txt", "src/main/resources/start.log",
				"src/main/resources/end.log");

		assertNotNull(racerDataMap);
		assertFalse(racerDataMap.isEmpty());
		assertEquals(19, racerDataMap.size());
	}

	@Test
	void testSortByDurationLap_shouldVerifyingOutputMapIsNotNullNotEmptyAndContainsTheExpectedNumberOfEntriesAndSortsByDurationCorrectly() {
		Map<String, Map<String, Object>> sortedRacerDataMap = q1Top15.sortByDurationLap();
		assertNotNull(sortedRacerDataMap);
		assertFalse(sortedRacerDataMap.isEmpty());
		assertEquals(19, sortedRacerDataMap.size());

		Map.Entry<String, Map<String, Object>> previousEntry = null;
		for (Map.Entry<String, Map<String, Object>> entry : sortedRacerDataMap.entrySet()) {
			if (previousEntry != null) {
				long previousDuration = (long) previousEntry.getValue().get("duration");
				long currentDuration = (long) entry.getValue().get("duration");
				Assertions.assertTrue(previousDuration <= currentDuration);
			}
			previousEntry = entry;
		}
	}

}
