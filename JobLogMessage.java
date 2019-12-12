package searching.algo;

public class JobLogMessage implements Comparable<JobLogMessage> {

	private int jobId;
	private String name;
	private long time;


	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + jobId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobLogMessage other = (JobLogMessage) obj;
		if (jobId != other.jobId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (time != other.time)
			return false;
		return true;
	}

	@Override
	public int compareTo(JobLogMessage other) {

		int i = 0;
		if (this.time > other.time) {
			i = -1;
		} else if (this.time < other.time) {
			i = 1;
		}
		return i;
	}

	@Override
	public String toString() {
		return "JobLog [jobId=" + jobId + ", name=" + name + ", time=" + time + "]";
	}
	

}
