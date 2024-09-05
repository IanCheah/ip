package main;

import exception.CommandFoundButInvalidException;
import exception.CommandNotFoundException;
import exception.EmptyStringException;

/**
 * Parses user input commands and performs corresponding actions on tasks
 * Handles command recognition, validation, and execution, interacting with
 * {@code TaskList}, {@code Storage}, and {@code Ui} components
 */
public class Parser {
    private String command;
    private String remainder;
    private TaskList allTasks;
    private String description;
    private Storage storage;
    private boolean isOver = false;
    private Ui ui;
    /**
     * Constructs a {@code Parser} instance which processes a given input String and performs
     * an action depending on the first word (command)
     *
     * @param input the user input string containing the command and remaining arguments
     * @param allTasks the {@code TaskList} instance where Tasks are stored and managed
     * @param s the {@code Storage} instance used for saving that state of tasks
     * @param ui the {@code Ui} instance for user interface
     * @throws EmptyStringException if the input String is empty
     * @throws CommandNotFoundException if the command is not recognized
     * @throws CommandFoundButInvalidException if the command is recognized but the remaining string
     *         are of invalid syntax
     */
    public Parser(String input, TaskList allTasks, Storage s, Ui ui) throws
            EmptyStringException, CommandNotFoundException, CommandFoundButInvalidException {
        this.allTasks = allTasks;
        this.ui = ui;
        if (input.isEmpty()) {
            throw new EmptyStringException();
        }
        if (input.split(" ", 2).length == 1) {
            this.command = input.split(" ", 2)[0];
            this.remainder = "";
        }
        if (input.split(" ", 2).length == 2) {
            this.command = input.split(" ", 2)[0];
            this.remainder = input.split(" ", 2)[1];
        }
        Commands cmd = Commands.fromString(command);
        switch(cmd) {
        case TODO:
            allTasks.addTodo(remainder);
            s.put(allTasks);
            ui.display(ui.addedMessage(allTasks.getLastAdded(), allTasks.getSize()));
            break;
        case DEADLINE:
            allTasks.addDeadline(remainder);
            s.put(allTasks);
            System.out.println(ui.addedMessage(allTasks.getLastAdded(), allTasks.getSize()));
            break;
        case EVENT:
            allTasks.addEvent(remainder);
            s.put(allTasks);
            System.out.println(ui.addedMessage(allTasks.getLastAdded(), allTasks.getSize()));
            break;
        case DELETE:
            allTasks.delete(remainder);
            s.put(allTasks);
            System.out.println(ui.deleteMessage(this.allTasks.getLastDeleted(), this.allTasks.getSize()));
            break;
        case LIST:
            System.out.println(allTasks.list());
            break;
        case MARK:
            allTasks.mark(remainder);
            s.put(allTasks);
            System.out.println(ui.markedMessage(allTasks.getLastMarked()));
            break;
        case UNMARK:
            allTasks.unmark(remainder);
            s.put(allTasks);
            ui.display(ui.unmarkedMessage(allTasks.getLastUnmarked()));
            break;
        case FIND:
            ui.display(ui.findMessage());
            ui.display(new TaskList(allTasks.find(remainder)).list());
            break;
        case BYE:
            System.out.println(ui.bye());
            this.isOver = true;
            break;
        default:
            throw new CommandNotFoundException(command);
        }
    }

    /**
     * Returns a boolean to determine if the program should stop running
     *
     * @return a boolean value depending on the command input of user
     */
    public boolean isOver() {
        return this.isOver;
    }
}
