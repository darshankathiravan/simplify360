//Question 2:

// Both Alice & Bob have friends. Create a Java/Python/JS/Typescript console application to find all the friends of Alice and all the friends of Bob & common friends of Alice and Bob.
// Your algorithm should be able to do the following:
// Take any 2 friends and find the common friends between the 2 friends
// Take any 2 friends find the nth connection - for example: connection(Alice, Janice) => 2
// Alice has friend Bob and Bob has friend Janice, if the input given is Alice and Janice the output should be 2, meaning 2nd connection, that means Janice is the second connection of Alice and Bob being the 1st connection of Alice.
// Likewise if input given is Alice and Bob, the output should be 1, for 1st connection
// If there is no connection at all, it should return -1
// Add the console applications(programs) to your Github account and share the Github links for evaluation. Make sure your code is executable and free of syntax errors. Also please explain the Time and Space complexity of your respective programs as code comments
import java.util.*;

public class Social_Network {
    // Map to store the social network graph
    private Map<String, List<String>> graph;

    // Constructor to initialize the graph
    public Social_Network() {
        graph = new HashMap<>();
    }

    // Method to add a friendship between two people
    public void addFriendship(String person1, String person2) {
        // Add person2 to the list of friends of person1
        graph.computeIfAbsent(person1, k -> new ArrayList<>()).add(person2);
        // Add person1 to the list of friends of person2
        graph.computeIfAbsent(person2, k -> new ArrayList<>()).add(person1);
    }

    // Method to find all friends of a person
    public List<String> findFriends(String person) {
        // Return the list of friends or an empty list if the person is not in the graph
        return graph.getOrDefault(person, Collections.emptyList());
    }

    // Method to find common friends between two people
    public Object findCommonFriends(String person1, String person2) {
        // Create sets of friends for both persons
        Set<String> friends1 = new HashSet<>(findFriends(person1));
        Set<String> friends2 = new HashSet<>(findFriends(person2));
        // Retain only the common friends
        friends1.retainAll(friends2);

        // If there are no common friends, return -1
        if (friends1.isEmpty()) {
            return -1;
        } else {
            // Otherwise, return the list of common friends
            return new ArrayList<>(friends1);
        }
    }

    // Method to find the nth connection (shortest path) between two people
    public int findNthConnection(String person1, String person2) {
        // If both persons are the same, return 0 as the distance
        if (person1.equals(person2)) return 0;

        // Set to keep track of visited nodes
        Set<String> visited = new HashSet<>();
        // Queue to perform BFS, storing pairs of person and distance
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(person1, 0)); // Start BFS from person1

        // BFS loop
        while (!queue.isEmpty()) {
            Pair<String, Integer> current = queue.poll();
            String currentPerson = current.getKey();
            int distance = current.getValue();

            // Mark the current person as visited
            visited.add(currentPerson);

            // Traverse all friends of the current person
            for (String friend : graph.getOrDefault(currentPerson, Collections.emptyList())) {
                // If we find person2, return the distance + 1
                if (friend.equals(person2)) {
                    return distance + 1;
                }
                // If the friend has not been visited, add them to the queue
                if (!visited.contains(friend)) {
                    queue.add(new Pair<>(friend, distance + 1));
                }
            }
        }
        // If no connection is found, return -1
        return -1;
    }

    // Inner static class to represent a pair of values
    static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    // Main method to run the program and interact with the user
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Social_Network network = new Social_Network();

        // Input number of friendships
        System.out.println("Enter the number of friendships:");
        int numFriendships = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Input friendship pairs
        for (int i = 0; i < numFriendships; i++) {
            System.out.println("Enter friendship " + (i + 1) + " (format: person1 person2):");
            String[] friends = scanner.nextLine().split(" ");
            if (friends.length == 2) {
                network.addFriendship(friends[0], friends[1]);
            } else {
                System.out.println("Invalid input. Please enter two names separated by a space.");
                i--; // Repeat this iteration
            }
        }

        // Input a person's name to find their friends
        System.out.println("Enter a person's name to find their friends:");
        String person = scanner.nextLine();
        System.out.println("Friends of " + person + ": " + network.findFriends(person));

        // Input two names to find their common friends
        System.out.println("Enter two names to find their common friends (format: person1 person2):");
        String[] commonFriendsInput = scanner.nextLine().split(" ");
        if (commonFriendsInput.length == 2) {
            Object commonFriends = network.findCommonFriends(commonFriendsInput[0], commonFriendsInput[1]);
            if (commonFriends instanceof Integer && (int) commonFriends == -1) {
                System.out.println(" -1 " + commonFriendsInput[0] + " and " + commonFriendsInput[1]);
            } else {
                System.out.println("Common friends of " + commonFriendsInput[0] + " and " + commonFriendsInput[1] + ": " + commonFriends);
            }
        } else {
            System.out.println("Invalid input. Please enter two names separated by a space.");
        }

        // Input two names to find the nth connection
        System.out.println("Enter two names to find the nth connection (format: person1 person2):");
        String[] nthConnectionInput = scanner.nextLine().split(" ");
        if (nthConnectionInput.length == 2) {
            int distance = network.findNthConnection(nthConnectionInput[0], nthConnectionInput[1]);
            if (distance == -1) {
                System.out.println("No connection between " + nthConnectionInput[0] + " and " + nthConnectionInput[1]);
            } else {
                System.out.println("Nth connection between " + nthConnectionInput[0] + " and " + nthConnectionInput[1] + ": " + distance);
            }
        } else {
            System.out.println("Invalid input. Please enter two names separated by a space.");
        }

        // Close the scanner
        scanner.close();
    }
}
