package searching.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class LogSearch {

	public static void main(String[] args) {

		String logMsgs[] = { "Starting name=grep_logs job_id=1234",

				"Starting name=app_logs job_id=4321",

				"Ending job_id=1234 time=300",

				"Starting name=server_logs job_id=7890",

				"Ending job_id=4321 time=200",

				"Starting name=grep_logs job_id=1234",

				"Ending job_id=1234 time=100",

				"Ending job_id=7890 time=500" };

		logSearchAlgo(logMsgs, 2);

	}

	static void logSearchAlgo(String logMsgs[], int limit) { // Time Complexity -> O(N+k)

		/*
		 * jobLogMsgOrderedQueue - It is implemented as MaxHeap
		 */
		Map<Integer, List<JobLogMessage>> jobLogMsgMap = new HashMap<>();
		PriorityQueue<JobLogMessage> jobLogMsgOrderedQueue = new PriorityQueue<>();
		String starting = "Starting";
		String ending = "Ending";
		String jobIdPattern = "job_id=";
		String namePattern = "name=";
		String timePattern = "time=";

		for (int i = 0; i < logMsgs.length; i++) { // Time Complexity -> O(N) -- N=Number of log messages

			String logMsg = logMsgs[i];
			int jobIdStartIndex = indexOfByBoyerMooreAlgo(logMsg, jobIdPattern) + jobIdPattern.length();

			/*
			 * if the log message starts with 'Starting', loop-up for jobIdPattern and
			 * namePattern
			 */
			if (logMsg.startsWith(starting)) {
				int nameStartIndex = indexOfByBoyerMooreAlgo(logMsg, namePattern) + namePattern.length();
				/*
				 * StartIndex=Inclusive, EndIndex=Exclusive
				 */
				String name = logMsg.substring(nameStartIndex, jobIdStartIndex - jobIdPattern.length());
				int jobId = Integer.valueOf(logMsg.substring(jobIdStartIndex, logMsg.length()));
				JobLogMessage jobLogMsg = new JobLogMessage();
				jobLogMsg.setJobId(jobId);
				jobLogMsg.setName(name);

				if (jobLogMsgMap.containsKey(jobId)) {
					jobLogMsgMap.get(jobId).add(jobLogMsg);
				} else {
					List<JobLogMessage> jobLogList = new ArrayList<JobLogMessage>();
					jobLogList.add(jobLogMsg);
					jobLogMsgMap.put(jobId, jobLogList);
				}

				/*
				 * if the log message starts with 'Ending', look-up for jobIdPattern and
				 * timePattern
				 */

			} else if (logMsg.startsWith(ending)) {
				int timeStartIndex = indexOfByBoyerMooreAlgo(logMsg, timePattern) + timePattern.length();
				int jobId = Integer
						.valueOf(logMsg.substring(jobIdStartIndex, timeStartIndex - timePattern.length() - 1));
				long time = Long.valueOf(logMsg.substring(timeStartIndex, logMsg.length()));
				/*
				 * The log message pattern for same job_id as 'Starting -> Ending,
				 * Starting->Ending', We can use that pattern to set time fore repetitive job_id
				 * occurrence
				 */
				JobLogMessage jobLogMsg = jobLogMsgMap.get(jobId).get(jobLogMsgMap.get(jobId).size() - 1);
				jobLogMsg.setTime(time);
				/*
				 * Since using MaxHeap and JobLogMessage class implemented Comparable to sort it
				 * in descending order of time, MaxHeap always has JobLogMessage with longest
				 * time at head
				 */
				jobLogMsgOrderedQueue.add(jobLogMsg);
			}

		}

		int polledCount = 0;
		System.out.println("name | time");
		while (polledCount < limit) { // Time Complexity -> O(K) -- K=Number of log messages to display
			JobLogMessage jobLogMsg = jobLogMsgOrderedQueue.poll();
			System.out.println(jobLogMsg.getName() + " " + jobLogMsg.getTime());
			polledCount++;
		}

	}

	/*
	 * This algorithm is more efficient than String.indexOf. It will start to compare
	 * from right side of pattern. If mismatch, it skip by M. M=pattern length
	 * If the pattern length is long, it gets more efficient
	 */
	static int indexOfByBoyerMooreAlgo(String text, String pattern) {

		int index = -1;
		Map<Character, Integer> badMatchSkipTable = preparebadMatchSkipTable(pattern);

		int numOfSkips = 0;
		for (int i = 0; i <= text.length() - pattern.length(); i += numOfSkips) {

			numOfSkips = 0;
			for (int j = pattern.length() - 1; j >= 0; j--) {

				if (pattern.charAt(j) != text.charAt(i + j)) {

					if (badMatchSkipTable.containsKey(text.charAt(i + j))) {
						numOfSkips = badMatchSkipTable.get(text.charAt(i + j));
						break;
					} else {
						numOfSkips = pattern.length();
						break;
					}

				}
			}

			if (numOfSkips == 0)
				return i;
		}

		return index;
	}

	/*
	 * This table will help to store the skip count, if text char is matching with
	 * any of the pattern char
	 */
	static Map<Character, Integer> preparebadMatchSkipTable(String pattern) {
		Map<Character, Integer> badMatchSkipTable = new HashMap<>();
		for (int index = 0; index < pattern.length(); index++) {
			badMatchSkipTable.put(pattern.charAt(index), Math.max(1, pattern.length() - index - 1));
		}
		return badMatchSkipTable;
	}

}
