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
