package main;

import exception.CommandFoundButInvalidException;
import exception.CommandNotFoundException;
import exception.InvalidSyntaxException;
import task.Deadlines;
import task.Events;
import task.Task;
import task.ToDos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private String filePath;
    private File file;

    /**
     * Constructs a {@code Storage} instance with the given filePath
     *
     * @param filePath the path to the file where data will be store. A new
     *                 file is created if the file does not exist
     */
    public Storage(String filePath) {
        // create the file if it does not exist
        filePath = filePath;
        file = new File(filePath);
        file.getParentFile().mkdirs();

        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }
    /**
     * Loads tasks from a file and return a list of tasks
     *
     * @return a {@code List} of {@code Task} objects read from the file. If the
     *         file is empty, an empty {@code List} is returned
     * @throws CommandFoundButInvalidException if the file content is corrupted and not
     *         in the required format
     */
    public List<Task> load() throws CommandFoundButInvalidException {
        List<Task> allTasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                return allTasks;
            }
            // iterate through the buffered reader
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\| ");
                Task currTask;
                switch (parts[0].trim()) {
                    case "T":
                        currTask = new ToDos(parts[2]);
                        break;
                    case "D":
                        currTask = new Deadlines(parts[2]);
                        break;
                    case "E":
                        currTask = new Events(parts[2]);
                        break;
                    default:
                        throw new InvalidSyntaxException("File is corrupted");
                }
                if (Integer.parseInt(parts[1].trim()) == 1) {
                    currTask.markAsDone();
                }
                allTasks.add(currTask);
            }

        } catch (IOException e) {
            System.out.println("Error when reading file. " + e.getMessage());
        }
        return allTasks;
    }
    /**
     * Saves tasks from the input list of tasks into a file
     *
     * @param taskList a {@code TaskList} instance
     */
    public void put(TaskList taskList) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
            bw.append(taskList.toMemoryString());
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred when saving");
        }
    }
}
