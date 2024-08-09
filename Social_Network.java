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
    private Map<String, List<String>> graph;

    public Social_Network() {
        graph = new HashMap<>();
    }

    public void addFriendship(String person1, String person2) {
        graph.computeIfAbsent(person1, k -> new ArrayList<>()).add(person2);
        graph.computeIfAbsent(person2, k -> new ArrayList<>()).add(person1);
    }

    public List<String> findFriends(String person) {
        return graph.getOrDefault(person, Collections.emptyList());
    }

    public Object findCommonFriends(String person1, String person2) {
        Set<String> friends1 = new HashSet<>(findFriends(person1));
        Set<String> friends2 = new HashSet<>(findFriends(person2));
        friends1.retainAll(friends2);

        if (friends1.isEmpty()) {
            return -1;
        } else {
            return new ArrayList<>(friends1);
        }
    }

    public int findNthConnection(String person1, String person2) {
        if (person1.equals(person2)) return 0;

        Set<String> visited = new HashSet<>();
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(person1, 0));

        while (!queue.isEmpty()) {
            Pair<String, Integer> current = queue.poll();
            String currentPerson = current.getKey();
            int distance = current.getValue();

            visited.add(currentPerson);

            for (String friend : graph.getOrDefault(currentPerson, Collections.emptyList())) {
                if (friend.equals(person2)) {
                    return distance + 1;
                }
                if (!visited.contains(friend)) {
                    queue.add(new Pair<>(friend, distance + 1));
                }
            }
        }
        return -1;
    }

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Social_Network network = new Social_Network();

        System.out.println("Enter the number of friendships:");
        int numFriendships = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numFriendships; i++) {
            System.out.println("Enter friendship " + (i + 1) + " (format: person1 person2):");
            String[] friends = scanner.nextLine().split(" ");
            if (friends.length == 2) {
                network.addFriendship(friends[0], friends[1]);
            } else {
                System.out.println("Invalid input. Please enter two names separated by a space.");
                i--;
            }
        }

        System.out.println("Enter a person's name to find their friends:");
        String person = scanner.nextLine();
        System.out.println("Friends of " + person + ": " + network.findFriends(person));

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

        scanner.close();
    }
}
