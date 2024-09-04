package main;

import exception.CommandFoundButInvalidException;
import exception.EmptyDescriptionException;
import exception.InvalidSyntaxException;
import task.Deadlines;
import task.Events;
import task.Task;
import task.ToDos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Manages a list of tasks, providing methods to add, mark, unmark, delete, find, and list tasks.
 * Also handles storing tasks in a format suitable for persistence
 */
public class TaskList {
    private List<Task> allTasks;
    private Stack<Task> deletedTasks, addedTasks, markedTasks, unmarkedTasks;

    /**
     * Constructs a {@code TaskList} instance with an initial list of tasks.
     *
     * @param allTasks the initial list of tasks
     */
    public TaskList(List<Task> allTasks) {
        this.allTasks = allTasks;
        this.deletedTasks = new Stack<>();
        this.addedTasks = new Stack<>();
        this.markedTasks = new Stack<>();
        this.unmarkedTasks = new Stack<>();
    }

    /**
     * Deletes a task from the list based on the specified index.
     *
     * @param description the index of the task to be deleted, as a {@code String}
     * @throws CommandFoundButInvalidException if the command is invalid or the task index is out of bounce
     */
    public void delete(String description) throws CommandFoundButInvalidException {
        try {
            int num = Integer.parseInt(description);
            int index = num - 1;
            if (index >= 0 && index < allTasks.size()) {
                Task removedTask = allTasks.remove(index);
                this.deletedTasks.push(removedTask);
            } else {
                throw new InvalidSyntaxException("delete");
            }
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("delete");
        }
    }

    /**
     * Adds a new {@code ToDos} task with the given description to the list
     *
     * @param description the description of the task to be added
     * @throws CommandFoundButInvalidException if the command is invalid
     */
    public void addTodo(String description) throws CommandFoundButInvalidException {
        Task current = new ToDos(description);
        this.allTasks.add(current);
        this.addedTasks.push(current);
    }

    /**
     * Adds a new {@code Deadlines} task with the given description to the list
     *
     * @param description the description of the task to be added
     * @throws CommandFoundButInvalidException if the command is invalid
     */
    public void addDeadline(String description) throws CommandFoundButInvalidException {
        Task current = new Deadlines(description);
        this.allTasks.add(current);
        this.addedTasks.push(current);
    }

    /**
     * Adds a new {@code Events} task with the given description to the list.
     *
     * @param description the description of the task to be added
     * @throws CommandFoundButInvalidException if the command is invalid
     */
    public void addEvent(String description) throws CommandFoundButInvalidException {
        Task current = new Events(description);
        this.allTasks.add(current);
        this.addedTasks.push(current);
    }

    /**
     * Marks a task as done based on the specified index.
     *
     * @param description the index of the task to be marked as done, as a {@code String}
     * @throws CommandFoundButInvalidException if the command is invalid or the task index is out of bounce
     */
    public void mark(String description) throws CommandFoundButInvalidException {
        try {
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("mark");
            }
            int index = Integer.parseInt(description) - 1;
            if (index < 0 || index >= this.allTasks.size()) {
                throw new InvalidSyntaxException("mark");
            }
            Task targetTask = this.allTasks.get(index);
            targetTask.markAsDone();
            this.markedTasks.push(targetTask);
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("mark");
        }
    }

    /**
     * Unmarks a task as not done based on the specified index.
     *
     * @param description the index of the task to be unmarked, with type {@code String}
     * @throws CommandFoundButInvalidException if the command is invalid or the task index is out of bounce
     */
    public void unmark(String description) throws CommandFoundButInvalidException {
        try {
            if (description.isEmpty()) {
                throw new EmptyDescriptionException("unmark");
            }
            int index = Integer.parseInt(description) - 1;
            if (index < 0 || index >= this.allTasks.size()) {
                throw new InvalidSyntaxException("unmark");
            }
            Task targetTask = this.allTasks.get(index);
            targetTask.markAsNotDone();
            this.unmarkedTasks.push(targetTask);
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("unmark");
        }
    }

    /**
     * Finds tasks that contain the specified substring in their description
     *
     * @param str the substring to search for
     * @return a {@code List} of {@code Task} objects that contain the substring
     */
    public List<Task> find(String str) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t : this.allTasks) {
            if (t.toString().contains(str)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Returns the string representation when users enter the list command
     *
     * @return the string representation of the {@code Task} in the {@code ArrayList}
     */
    public String list() {
        String s1 = "Here are the tasks in your list:";
        for (int i = 0; i < allTasks.size(); i++) {
            String index = String.format("%d", i + 1);
            String curr = "\n" + index + ". " + allTasks.get(i).toString();
            s1 = s1.concat(curr);
        }
        return s1;
    }

    /**
     * Returns a string that follows the format of the storage
     *
     * @return the string representation of the {@code Tasks} in the {@code ArrayList} for
     *         storage
     */
    public String toMemoryString() {
        StringBuilder result = new StringBuilder();
        for (Task t : allTasks) {
            result.append(t.getInitDesc());
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Retrieves the most recently marked task.
     *
     * @return the last marked {@code Task}
     */
    public Task getLastMarked() {
        return this.markedTasks.pop();
    }

    /**
     * Retrieves the most recently unmarked task.
     *
     * @return the last unmarked {@code Task}
     */
    public Task getLastUnmarked() {
        return this.unmarkedTasks.pop();
    }

    /**
     * Retrieves the most recently added task.
     *
     * @return the last added {@code Task}
     */
    public Task getLastAdded() {
        return addedTasks.pop();
    }

    /**
     * Returns the number of tasks in the list
     *
     * @return the size of the task list
     */
    public int getSize() {
        return this.allTasks.size();
    }

    /**
     * Retrieves the most recently deleted task.
     *
     * @return the last deleted {@code Task}
     */
    public Task getLastDeleted() {
        return deletedTasks.pop();
    }

}
