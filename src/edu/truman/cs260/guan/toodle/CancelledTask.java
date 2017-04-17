package edu.truman.cs260.guan.toodle;

/**
 * A cancelled task with the reason it was cancelled.
 * @author Orion Guan
 * @version February 27, 2017
 */
public class CancelledTask extends Task
{
	private static final String STATUS_CANCELLED = "cancel";
	private static final String BLANK_DATE_SLOT = "          ";
	private String reason;
	
	/**
	 * Makes a new CancelledTask object.
	 * @param task The task to be cancelled.
	 * @param reason The reason the task is being cancelled.
	 */
	public CancelledTask(Task task, String reason)
	{
		super(task.ID, task.description, task.priority, task.order, STATUS_CANCELLED);
		this.reason = reason;
	}
	
	/**
	 * Returns the reason for cancellation.
	 * @return The reason for cancellation.
	 */
	public String getReason()
	{
		return reason;
	}
	
	/**
	 * Returns a String representation of all the information of the cancelled task.
	 * @return A String representation of a cancelled task.
	 */
	public String toString()
	{
		return String.format("%6d " + super.getDescription() + "        " +
				super.getPriority() + " " + "%5d " + super.getStatus() +
				BLANK_DATE_SLOT + " " + reason, super.getIDNumber(), super.getOrder()); //make the Strings both a set length somehow
	}
}
