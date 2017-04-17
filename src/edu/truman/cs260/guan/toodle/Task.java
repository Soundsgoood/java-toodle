package edu.truman.cs260.guan.toodle;

/**
 * A task on a to-do list
 * @author Orion Guan
 * @version February 27, 2017
 */
public class Task
{
	protected int ID;
	protected String description;
	protected char priority;
	protected int order;
	protected String status;
	
	/**
	 * Creates a new task.
	 * @param ID The unique ID number of the task in the to-do list.
	 * @param description A short description of the task.
	 * @param priority Whether the task has urgent, normal, or low priority.
	 * @param order A number assigned to the task indicating its place in a queue.
	 * @param status Whether the task is incomplete, complete, or cancelled.
	 */
	public Task(int ID, String description, char priority, int order, String status)
	{
		this.ID = ID;
		this.description = description;
		this.priority = priority;
		this.order = order;
		this.status = status;
	}
	
	/**
	 * Returns the ID number of the task.
	 * @return The ID number.
	 */
	public int getIDNumber()
	{
		return ID;
	}
	
	/**
	 * Returns the description of the task.
	 * @return The description.
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the priority of the task.
	 * @return The priority.
	 */
	public char getPriority()
	{
		return priority;
	}
	
	/**
	 * Returns the order of the task.
	 * @return The order.
	 */
	public int getOrder()
	{
		return order;
	}
	
	/**
	 * Returns the status of the task.
	 * @return The status.
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * Returns a String representation of the task that includes parameters that all tasks have.
	 * @return A String representation of an incomplete task.
	 */
	public String toSimpleString()
	{
		return String.format("%6d " + description + "        " + priority + "  " + "%5d", ID, order); //make the Strings both a set length somehow
	}
	
	/**
	 * Returns a String representation of the task that includes all possible information for completed or cancelled tasks.
	 * @return A String representation of a task in general.
	 */
	public String toString()
	{
		return String.format("%6d " + description + "        " + priority + " " + "%5d" + " " + status, ID, order); //make the Strings both a set length somehow
	}
}
