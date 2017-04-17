import edu.truman.cs260.guan.toodle.Task;
import edu.truman.cs260.guan.toodle.CompletedTask;
import edu.truman.cs260.guan.toodle.CancelledTask;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * A list of tasks and the interface for adding and changing tasks.
 * @author Orion Guan
 * @version February 27th 2017
 */
public class GuanToodle
{
	//The chars representing priority values
	private static final char PRIORITY_URGENT_CHAR = 'U';
	private static final char PRIORITY_NORMAL_CHAR = 'N';
	private static final char PRIORITY_LOW_CHAR = 'L';
	//The Strings representing statuses
	private static final String STATUS_INCOMPLETE = "      ";
	private static final String STATUS_COMPLETE = "comple";
	private static final String STATUS_CANCELLED = "cancel";
	//The printed headers for tables of task information
	private static final String INCOMPLETE_TASK_INFO_HEADER =
			"ID     Task                 Priority  Order";
	private static final String INCOMPLETE_TASK_INFO_SEPARATOR =
			"------ ----                 --------  -----";
	private static final String ENDED_TASK_INFO_HEADER =
			"ID     Task                 Priority Order Status Date      Reason";
	private static final String ENDED_TASK_INFO_SEPARATOR =
			"------ ----                 -------- ----- ------ --------- ------";
	
	//The sizes of printed outputs for certain parameters
	private static final int DESCRIPTION_CHAR_LIMIT = 20;
	private static final int REASON_CHAR_LIMIT = 20;
	
	//The position in .toString() output at which a field of info ends
	private static final int ID_FIELD_END = 6;
	private static final int PRIORITY_FIELD_END = 36;
	private static final int ORDER_FIELD_END = 42;
	private static final int STATUS_FIELD_END = 49;
	private static final int DATE_FIELD_END = 59;
	private static final int REASON_FIELD_END = 80;
	
	
	//System scanner
	private static final Scanner IN = new Scanner(System.in);
	
	//The location of the task file, the ID number to be assigned to the
	//next created task, and the total tasks currently stored
	private static final String TASK_FILE_LOCATION = "src/resources/task_list.txt";
	private int nextTaskID = 0;
	private int numberOfTasks = 0;
	
	//ArrayLists of Tasks
	ArrayList<Task> taskList = new ArrayList<Task>();
	
	//Highest value for order of any Task in any priority category
	int highestOrder = 0;
	
	public static void main(String[] args)
	{
		GuanToodle toDoList = new GuanToodle(TASK_FILE_LOCATION);
		
		int userInput = 0;
		
		while(userInput != 7)
		{
			System.out.printf("1) Enter new task\n2) List incomplete tasks\n"
					+ "3) List incomplete tasks of a priority level\n4) List all tasks\n"
					+ "5) Mark a task as complete\n6) Mark a task as cancelled\n"
					+ "7) Quit\n");
			System.out.print("Please enter a command (1-7): ");
			userInput = IN.nextInt();
			IN.nextLine();
			
			if (userInput == 1)
			{
				toDoList.addTask();
			}
			else if (userInput == 2)
			{
				System.out.println(INCOMPLETE_TASK_INFO_HEADER);
				System.out.println(INCOMPLETE_TASK_INFO_SEPARATOR);
				toDoList.printIncomplete(PRIORITY_URGENT_CHAR);
				toDoList.printIncomplete(PRIORITY_NORMAL_CHAR);
				toDoList.printIncomplete(PRIORITY_LOW_CHAR);
			}
			else if (userInput == 3)
			{
				System.out.print("Please enter a priority (" +
						PRIORITY_URGENT_CHAR + ", " +
						PRIORITY_NORMAL_CHAR + ", " +
						PRIORITY_LOW_CHAR + "): ");
				char userPriority = IN.nextLine().charAt(0);
				
				System.out.println(INCOMPLETE_TASK_INFO_HEADER);
				System.out.println(INCOMPLETE_TASK_INFO_SEPARATOR);
				toDoList.printIncomplete(userPriority);
			}
			else if (userInput == 4)
			{
				System.out.println(ENDED_TASK_INFO_HEADER);
				System.out.println(ENDED_TASK_INFO_SEPARATOR);
				toDoList.printAll(PRIORITY_URGENT_CHAR);
				toDoList.printAll(PRIORITY_NORMAL_CHAR);
				toDoList.printAll(PRIORITY_LOW_CHAR);
			}
			else if (userInput == 5)
			{
				toDoList.markComplete();
			}
			else if (userInput == 6)
			{
				toDoList.cancel();
			}
			else if (userInput == 7)
			{
				System.out.println("Goodbye");
				toDoList.writeFileData(TASK_FILE_LOCATION);
			}
			else
			{
				System.out.println("Invalid input.");
			}
		}
	}
	
	/**
	 * Creates a new to-do list from file data.
	 * @param taskFileLocation The location of the task file.
	 */
	GuanToodle(String taskFileLocation)
	{
		try (FileReader file = new FileReader(taskFileLocation))
		{
			Scanner fileScanner = new Scanner(file);
			
			if (fileScanner.hasNextLine())
			{
				nextTaskID = fileScanner.nextInt();
				numberOfTasks = fileScanner.nextInt();
				fileScanner.nextLine();
			}
			
			while (fileScanner.hasNextLine())
			{
				String taskInfoLine = fileScanner.nextLine();
				String IDString = taskInfoLine.substring(0, ID_FIELD_END);
				String description = taskInfoLine.substring(ID_FIELD_END + 1, ID_FIELD_END + 1 + DESCRIPTION_CHAR_LIMIT);
				int startIndex = ID_FIELD_END + 1 + DESCRIPTION_CHAR_LIMIT;
				String priorityString = taskInfoLine.substring(startIndex, PRIORITY_FIELD_END);
				String orderString = taskInfoLine.substring(PRIORITY_FIELD_END + 1, ORDER_FIELD_END);
				String status = taskInfoLine.substring(ORDER_FIELD_END + 1, STATUS_FIELD_END);
				
				String IDTrimmed = IDString.trim();
				String orderTrimmed = orderString.trim();
				
				int id = Integer.parseInt(IDTrimmed);
				char priority = priorityString.charAt(priorityString.length() - 1);
				int order = Integer.parseInt(orderTrimmed);
				
				Task task = new Task(id, description, priority, order, status);
				
				if (status.equals(STATUS_COMPLETE))
				{
					String date = taskInfoLine.substring(STATUS_FIELD_END, DATE_FIELD_END);
					task = new CompletedTask(task, date);
				}
				else if (status.equals(STATUS_CANCELLED))
				{
					String reason = taskInfoLine.substring(DATE_FIELD_END + 1, REASON_FIELD_END);
					task = new CancelledTask(task, reason);
				}
				
				taskList.add(task);
			}
			
			fileScanner.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		catch (IOException e)
		{
			System.out.println("IOException occurred");
		}
	}
	
	/**
	 * Prompts the user for information and adds a task to the to-do list.
	 */
	private void addTask()
	{
		System.out.print("Enter task description: ");
		String description = IN.nextLine();
		if (description.length() < DESCRIPTION_CHAR_LIMIT)
		{
			String padding = "";
			for (int i = 0; i < (DESCRIPTION_CHAR_LIMIT - description.length()); i++)
			{
				padding += " ";
			}
			description += padding;
		}
		else
		{
			description = description.substring(0, DESCRIPTION_CHAR_LIMIT);
		}
		
		System.out.print("Enter priority (" + 
				PRIORITY_URGENT_CHAR + " = urgent, " +
				PRIORITY_NORMAL_CHAR + " = normal priority, " +
				PRIORITY_LOW_CHAR + " = low priority): ");
		char priority = IN.nextLine().charAt(0);
		
		System.out.print("Enter task order: ");
		int order = IN.nextInt();
		taskList.add(new Task(nextTaskID, description, priority, order, STATUS_INCOMPLETE));
		
		if (order > highestOrder)
		{
			highestOrder = order;
		}
		
		System.out.printf("-- Task Created: ID is %d --\n", nextTaskID);
		nextTaskID++;
		numberOfTasks++;
	}
	
	/**
	 * Prints information for all tasks of a to-do list that have a specific priority and are incomplete.
	 * @param priority The specified priority of the printed tasks.
	 */
	private void printIncomplete(char priority)
	{	
		ArrayList<Task> incompletePriorityTaskList= new ArrayList<>();
		
		for (Task task : taskList)
		{
			if (task.getPriority() == priority && task.getStatus().equals(STATUS_INCOMPLETE))
			{
				incompletePriorityTaskList.add(task);
			}
		}
		
		printByOrder(incompletePriorityTaskList);
	}
	
	/**
	 * Prints extended information for all tasks, incomplete or not, that have a specific priority.
	 * @param priority The specified priority of the printed tasks.
	 */
	private void printAll(char priority)
	{
		ArrayList<Task> priorityTaskList = new ArrayList<Task>();
		for (Task task : taskList)
		{
			if (task.getPriority() == priority)
			{
				System.out.println(task);
			}
		}
		
		printByOrder(priorityTaskList);
	}
	
	/**
	 * Prints an ArrayList of tasks from those of lowest order to those of highest order.
	 * @param relevantTaskList
	 */
	private void printByOrder(ArrayList<Task> relevantTaskList)
	{		
		for (int i = 0; i <= highestOrder; i++)
		{
			for(Task relevantTask : relevantTaskList)
			{
				if (relevantTask.getOrder() == i)
				{
					System.out.println(relevantTask.toSimpleString());
				}
			}
		}
	}
	
	/**
	 * Prompts a task ID and changes that task from incomplete to complete, marking the completed task with the date this method is called.
	 */
	private void markComplete()
	{
		System.out.print("Enter ID of task to mark as completed: ");
		int completedID = IN.nextInt();
		IN.nextLine();
		String date = getCurrentMMDDYYYY();
		Task taskToBeCompleted = findTaskByID(completedID);
		
		if (taskToBeCompleted == null)
		{
			System.out.println("No task with that ID.");
		}
		else
		{
			CompletedTask newCompletedTask = new CompletedTask(taskToBeCompleted, date);
			taskList.remove(taskToBeCompleted);
			taskList.add(newCompletedTask);
			System.out.printf("-- Task %d marked as completed --\n", newCompletedTask.getIDNumber());
		}
	}
	
	private String getCurrentMMDDYYYY()
	{
		Date currentDate = new Date();
	    String DATE_FORMAT = "MM/dd/yyyy";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    String dateString = sdf.format(currentDate);
	    if (dateString.charAt(0) == '0')
	    {
	    	dateString = " " + dateString.substring(1);
	    }
	    return dateString;
	}
	
	/**
	 * Prompts a task ID and reason, then cancels that task.
	 */
	private void cancel()
	{
		System.out.print("Enter ID of task to cancel: ");
		int cancelID = IN.nextInt();
		IN.nextLine();
		System.out.print("Enter reason for cancellation: ");
		String reason = IN.nextLine();
		if (reason.length() < REASON_CHAR_LIMIT)
		{
			String padding = "";
			for (int i = 0; i < (REASON_CHAR_LIMIT - reason.length()); i++)
			{
				padding += " ";
			}
			reason += padding;
		}
		else
		{
			reason = reason.substring(0, REASON_CHAR_LIMIT);
		}
		Task taskToBeCancelled = findTaskByID(cancelID);
		
		if (taskToBeCancelled == null)
		{
			System.out.println("No task with that ID.");
		}
		else
		{
			CancelledTask newCancelledTask = new CancelledTask(taskToBeCancelled, reason);
			taskList.remove(taskToBeCancelled);
			taskList.add(newCancelledTask);
			System.out.printf("-- Task %d cancelled --\n", newCancelledTask.getIDNumber());
		}
	}
	
	/**
	 * Returns the first task on the list associated with a given ID. Will print a message if there's another task with the same ID.
	 * @param ID
	 * @return
	 */
	private Task findTaskByID(int ID)
	{
		Task correctTask = null;
		for (Task task : taskList)
		{
			if (task.getIDNumber() == ID)
			{
				if (correctTask == null)
				{
					correctTask = task;
				}
				else
				{
					System.out.println("Duplicate ID found!");
				}
			}
		}
		return correctTask;
	}
	
	/**
	 * Writes the to-do list data to the to-do list's file location.
	 */
	private void writeFileData(String taskFileLocation)
	{
		File file = new File(taskFileLocation);
		try (PrintWriter out = new PrintWriter(file))
		{
			out.println(nextTaskID);
			out.println(numberOfTasks);
			
			for (Task task : taskList)
			{
				out.println(task);
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
}
