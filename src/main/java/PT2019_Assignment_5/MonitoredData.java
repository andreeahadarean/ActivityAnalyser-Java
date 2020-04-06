package PT2019_Assignment_5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonitoredData {

	private String startTime;
	private String endTime;
	private String activity;

	public MonitoredData() {

	}

	public MonitoredData(String startTime, String endTime, String activity) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.activity = activity;
	}

	public String toString() {
		return "Monitored data: " + "start time = " + startTime + ", end time = " + endTime + ", activity = "
				+ activity;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getActivity() {
		return activity;
	}

	public ArrayList<MonitoredData> getMonitoredData() {
		ArrayList<MonitoredData> monitoredData = new ArrayList<MonitoredData>();
		String fileName = "Activities.txt";
		List<String> activityList = new ArrayList<String>();
		try {
			@SuppressWarnings("resource")
			Stream<String> stream = Files.lines(Paths.get(fileName));
			activityList = stream.collect(Collectors.toList());
			for (int i = 0; i < activityList.size(); i++) {
				String data = activityList.get(i);
				String[] aux = data.split("		");
				String removed = aux[2].replaceAll("\t", "");
				MonitoredData m = new MonitoredData(aux[0], aux[1], removed);
				monitoredData.add(m);
			}
		} catch (IOException e) {
			System.out.println("Calea spre fisier nu este corecta!");
		}
		return monitoredData;
	}

	public long countNumberOfDays(ArrayList<MonitoredData> monitoredData) {
		List<String> listOfStartTimes = monitoredData.stream().map(m -> m.getStartTime()).map(s -> s.substring(0, 10))
				.collect(Collectors.toList());
		long numberOfDays = listOfStartTimes.stream().distinct().count();
		return numberOfDays;
	}

	public HashMap<String, Integer> countNumberOfActivities(ArrayList<MonitoredData> monitoredData) {
		HashMap<String, Integer> numberOfActivities = new HashMap<String, Integer>();
		List<String> listOfActivities = monitoredData.stream().map(m -> m.getActivity()).collect(Collectors.toList());
		for (String a : listOfActivities) {
			long count = monitoredData.stream().map(m -> m.getActivity()).filter(activity -> a.equals(activity))
					.count();
			numberOfActivities.put(a, (int) count);
		}
		return numberOfActivities;
	}

	public void countNumberOfActivitiesPerDay(ArrayList<MonitoredData> monitoredData) {
		List<String> listOfStartTimes = monitoredData.stream().map(m -> m.getStartTime()).map(s -> s.substring(0, 10))
				.distinct().collect(Collectors.toList());
		List<String> listOfActivities = monitoredData.stream().map(m -> m.getActivity()).distinct()
				.collect(Collectors.toList());
		for (String day : listOfStartTimes) {
			System.out.println("--- " + "Day " + day + "--- ");
			for (String activity : listOfActivities) {
				long count = monitoredData.stream()
						.filter(n -> n.startTime.contains(day) && n.activity.equals(activity)).count();
				System.out.println(activity + " -> " + count);
			}
		}
	}

	public void computeActivityDuration(ArrayList<MonitoredData> monitoredData) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Long> listOfDurations = monitoredData.stream().map(m -> {
			try {
				return TimeUnit.SECONDS.convert(
						date.parse(m.getEndTime()).getTime() - date.parse(m.getStartTime()).getTime(),
						TimeUnit.MILLISECONDS);
			} catch (ParseException e) {
				System.out.println("Nu s-a reusit parsarea!");
			}
			return null;
		}).collect(Collectors.toList());
		listOfDurations.forEach(d -> System.out.println(d));
	}

	@SuppressWarnings("null")
	public void computeTotalDuration(ArrayList<MonitoredData> monitoredData) {
		List<String> listOfActivities = monitoredData.stream().map(m -> m.getActivity()).distinct()
				.collect(Collectors.toList());
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (String activity : listOfActivities) {
			long totalDuration = 0;
			totalDuration = monitoredData.stream().filter(a -> a.getActivity().equals(activity)).mapToLong(m -> {
				try {
					return TimeUnit.MINUTES.convert(
							date.parse(m.getEndTime()).getTime() - date.parse(m.getStartTime()).getTime(),
							TimeUnit.MILLISECONDS);
				} catch (ParseException e) {
					System.out.println("Nu s-a reusit parsarea!");
				}
				return (Long) null;
			}).sum();
			System.out.println(activity + " -> " + totalDuration + " minutes");
		}
		
	}

	public void filterActivities(ArrayList<MonitoredData> monitoredData) {
		List<String> listOfActivities = monitoredData.stream().map(m -> m.getActivity()).distinct()
				.collect(Collectors.toList());
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<MonitoredData> result = monitoredData.stream().filter(m -> {
			try {
				return 5 > TimeUnit.MINUTES.convert(
						date.parse(m.getEndTime()).getTime() - date.parse(m.getStartTime()).getTime(),
						TimeUnit.MILLISECONDS);
			} catch (ParseException e) {
				System.out.println("Nu s-a reusit parsarea!");
			}
			return false;
		}).collect(Collectors.toList());
		for (String a : listOfActivities) {
			long totalNumber = monitoredData.stream().filter(activity -> activity.getActivity().equals(a)).count();
			long totalNumberAfterFilter = result.stream().filter(activity -> activity.getActivity().equals(a)).count();
			List<String> activities = result.stream()
					.filter(r -> totalNumber != 0 && totalNumberAfterFilter != 0 && r.getActivity().equals(a)
							&& totalNumberAfterFilter >= 0.9 * totalNumber)
					.map(r -> r.getActivity()).distinct().collect(Collectors.toList());
			activities.forEach(n -> System.out.println(n));
		}
	}
}
