
// Question 1:

// We have a large and complex workflow, made of tasks. And
// have to decide who does what, when, so it all gets done on time.
// All tasks have dependency on other tasks to complete
// Each task(t) has a
// D[t] = duration of task
// EFT[t] = the earliest finish time for a task
// LFT[t] = the latest finish time for a task
// EST[t] = the earliest start time for a task
// LST[t] = the last start time for a task
// Assume
// that “clock” starts at 0 (project starting time).
// We are given the starting task T_START where the EST[t] = 0 and LST[t] = 0
// You have to write an Java/Python/JS/Typescript console application to answer the following questions
// Earliest time all the tasks will be completed?
// Latest time all tasks will be completed?

import java.util.*;

class Task {
    String name;
    int duration;
    List<String> dependencies;
    int est; // Earliest Start Time
    int eft; // Earliest Finish Time
    int lft; // Latest Finish Time
    int lst; // Latest Start Time

    // Constructor to initialize a task with its name, duration, and dependencies
    public Task(String name, int duration, List<String> dependencies) {
        this.name = name;
        this.duration = duration;
        this.dependencies = dependencies;
        this.est = 0;
        this.eft = 0;
        this.lft = Integer.MAX_VALUE; // Initialize with a very large value
        this.lst = Integer.MAX_VALUE; // Initialize with a very large value
    }
}

public class Time_line {
    public static void main(String[] args) {
        // Initialize tasks with their durations and dependencies
        Map<String, Task> tasks = new HashMap<>();
        tasks.put("A", new Task("A", 3, Collections.emptyList())); // Task A has no dependencies
        tasks.put("B", new Task("B", 2, Arrays.asList("A"))); // Task B depends on A
        tasks.put("C", new Task("C", 4, Arrays.asList("A"))); // Task C depends on A
        tasks.put("D", new Task("D", 2, Arrays.asList("B", "C"))); // Task D depends on B and C

        // Calculate the times for each task
        calculateTimes(tasks);

        // Determine the earliest and latest completion times for the entire workflow
        int earliestCompletion = tasks.values().stream().mapToInt(t -> t.eft).max().orElse(0);
        int latestCompletion = tasks.values().stream().mapToInt(t -> t.lft).max().orElse(0);

        // Output the results
        System.out.println("Earliest Completion Time: " + earliestCompletion);
        System.out.println("Latest Completion Time: " + latestCompletion);
    }

    private static void calculateTimes(Map<String, Task> tasks) {
        // Forward pass to calculate EST and EFT for each task
        for (Task task : tasks.values()) {
            if (task.dependencies.isEmpty()) {
                // If the task has no dependencies, start it at time 0
                task.eft = task.est + task.duration;
            } else {
                // If the task has dependencies, set EST to the max EFT of its dependencies
                task.est = task.dependencies.stream()
                                            .map(dep -> tasks.get(dep).eft)
                                            .max(Integer::compare)
                                            .orElse(0);
                task.eft = task.est + task.duration;
            }
        }

        // Find the task with the maximum EFT (i.e., the end of the project)
        Task endTask = tasks.values().stream().max(Comparator.comparingInt(t -> t.eft)).orElse(null);
        if (endTask != null) {
            endTask.lft = endTask.eft; // Set LFT of the last task to its EFT
            endTask.lst = endTask.lft - endTask.duration; // Calculate LST
        }

        // Backward pass to calculate LFT and LST for each task
        List<Task> sortedTasks = new ArrayList<>(tasks.values());
        sortedTasks.sort(Comparator.comparingInt(t -> -t.eft)); // Sort tasks in descending order of EFT
        
        for (Task task : sortedTasks) {
            for (String dep : task.dependencies) {
                // For each dependency, update LFT and LST
                Task dependencyTask = tasks.get(dep);
                dependencyTask.lft = Math.min(dependencyTask.lft, task.lst); // Set LFT to the minimum LST of the successor tasks
                dependencyTask.lst = dependencyTask.lft - dependencyTask.duration; // Calculate LST
            }
        }
    }
}
