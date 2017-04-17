package edu.truman.cs260.guan.toodle;

/**
 * A completed task with the date it was marked complete.
 * @author Orion Guan
 * @version February 27, 2017
 */
public class CompletedTask extends Task
{
	private static final String STATUS_COMPLETE = "comple";
	private String date;
	
	/**
	 * Makes a new CompletedTask object.
	 * @param task The task object to be marked complete.
	 * @param String The date at which this task is marked complete.
	 */
	public CompletedTask(Task task, String date)
	{
		super(task.ID, task.description, task.priority, task.order, STATUS_COMPLETE);
		this.date = date;
	}
	
	/**
	 * Returns the date of completion.
	 * @return The date of completion.
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Returns a String representation of all the information of the completed task.
	 * @return A String representation of a completed task.
	 */
	public String toString()
	{
		return String.format("%6d " + super.getDescription() + "        " +
				super.getPriority() + " " + "%5d " + super.getStatus() + 
				date, super.getIDNumber(), super.getOrder()); //make the Strings both a set length somehow
	}
}
