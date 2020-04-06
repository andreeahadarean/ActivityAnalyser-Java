package PT2019_Assignment_5;

import java.text.ParseException;
import java.util.*;

public class Main {

	public static void main(String[] args) throws ParseException {
		MonitoredData monitoredData = new MonitoredData();
		System.out.println("--------------------------------- " + "1. List of monitored data"
				+ " ---------------------------------");
		ArrayList<MonitoredData> listOfMonitoredData = monitoredData.getMonitoredData();
		listOfMonitoredData.forEach(data -> System.out.println(data));
		System.out.println("--------------------------------- " + "2. Number of days in the log"
				+ " ---------------------------------");
		long numberOfDays = monitoredData.countNumberOfDays(listOfMonitoredData);
		System.out.println(numberOfDays);
		System.out.println("--------------------------------- " + "3. Number of activities in the log"
				+ " ---------------------------------");
		HashMap<String, Integer> numberOfActivities = monitoredData.countNumberOfActivities(listOfMonitoredData);
		numberOfActivities.forEach((key, value) -> System.out.println(key + " -> " + value));
		System.out.println("--------------------------------- " + "4. Number of activities per day"
				+ " ---------------------------------");
		monitoredData.countNumberOfActivitiesPerDay(listOfMonitoredData);
		System.out.println("--------------------------------- " + "5. Duration of each activity"
				+ " ---------------------------------");
		monitoredData.computeActivityDuration(listOfMonitoredData);
		System.out.println("--------------------------------- " + "6. Total duration of each activity"
				+ " ---------------------------------");
		monitoredData.computeTotalDuration(listOfMonitoredData);
		System.out.println(
				"--------------------------------- " + "7. Filtered activities" + " ---------------------------------");
		monitoredData.filterActivities(listOfMonitoredData);

	}

}
