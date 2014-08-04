import java.util.HashMap;

public class BaseballElimination {
    private class Team {
        public int id;
        public String name;
        public int wins;
        public int losses;
        public int remainingGames;
        public int[] remainingGamesAgainst;

        public Team(int _id, String _name, int _wins, int _losses, int _remaining, String... _remainingAgainst) {
            id = _id;
            name = _name;
            wins = _wins;
            losses = _losses;
            remainingGames = _remaining;
            remainingGamesAgainst = new int[numberOfTeams()]; //you can include yourself since the remaining-games includes you-vs-you with a zero
            for (int i = 0; i < _remainingAgainst.length; i++) { remainingGamesAgainst[i] = Integer.parseInt(_remainingAgainst[i]); }
            if ((leader == -1) || (wins > teams[leader].wins)) { leader = _id; }
        }
    }

    private Team[] teams;
    private HashMap<String, SET<String>> eliminators;
    //private int leaderWins = 0;
    //private String leaderName;
    private int leader = -1;

    public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
        In in = new In(filename);
        int teamCount = in.readInt();
        teams = new Team[teamCount];
        eliminators = new HashMap<String, SET<String>>();
        for (int i = 0; i < teamCount; i++) {
            teams[i] = new Team(i, in.readString(), in.readInt(), in.readInt(), in.readInt(), in.readLine().trim().split("\\s+"));
        }
    }

    public int numberOfTeams() {                        // number of teams
        return teams.length;
    }

    public Iterable<String> teams() {                                // all teams
        Stack<String> retVal = new Stack<String>();
        for (Team t : teams) { retVal.push(t.name); }
        return retVal;
    }

    public int wins(String team) {                      // number of wins for given team
        return getTeamFromName(team).wins;
    }

    public int losses(String team) {                    // number of losses for given team
        return getTeamFromName(team).losses;
    }

    public int remaining(String team) {                 // number of remaining games for given team
        return getTeamFromName(team).remainingGames;
    }

    public int against(String team1, String team2) {    // number of remaining games between team1 and team2
        return teams[getTeamNumberFromName(team1)].remainingGamesAgainst[getTeamNumberFromName(team2)];
    }

    public boolean isEliminated(String team) {              // is given team eliminated?
        solve(getTeamFromName(team));
        return (eliminators.containsKey(team));
    }

    public Iterable<String> certificateOfElimination(String team) {  // subset R of teams that eliminates given team; null if not eliminated
        solve(getTeamFromName(team));
        return eliminators.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) { StdOut.print(t + " "); }
                StdOut.println("}");
            }
            else { StdOut.println(team + " is not eliminated"); }
        }
    }

    private Team getTeamFromName(String team) {
        for (Team t : teams) { if (t.name.equals(team)) { return t; } }
        throw new java.lang.IllegalArgumentException(String.format("No team named '%s' exists in this division!", team));
    }


    private int getTeamNumberFromName(String team) {
        for (int i = 0; i < teams.length; i++) { if (teams[i].name.equals(team)) { return i; } }
        throw new java.lang.IllegalArgumentException(String.format("No team named '%s' exists in this division!", team));
    }

    private void solve(Team t) {
        if (eliminators.containsKey(t.name)) { return; } //result is cached, so don't recalculate it

        // Trivial elimination
        if (!t.equals(teams[leader]) && isTriviallyEliminated(t)) {
            eliminators.put(t.name, new SET<String>() {{ add(teams[leader].name); }}); //I bet checkstyle HATES this. //TODO: I bet this is a bug where sliminators are overwritten
            return;
        }

        // Maxflowy realzo elimination
        FordFulkerson ffFlow = graphTheTeam(t);
        if (isNumericallyEliminated(t, ffFlow.value())) {
            for (int i = 0; i < numberOfTeams(); i++) {
                if (ffFlow.inCut(i)) {
                    SET<String> winners = new SET<String>();
                    if (eliminators.containsKey(t.name)) {
                        winners = eliminators.get(t.name);
                    }
                    winners.add(teams[i].name);
                    eliminators.put(t.name, winners);
                }
            }
        }
    }

    private boolean isNumericallyEliminated(Team t, double maxFlow) {
        int sum = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == t.id) { continue; }
            for (int j = 0; j < i; j++) {
                if (j == t.id) { continue; }
                sum += teams[i].remainingGamesAgainst[j];
            }
        }
        return sum > maxFlow;
    }

    private boolean isTriviallyEliminated(Team t) {
        return (t.wins + t.remainingGames < teams[leader].wins);
    }

    private FordFulkerson graphTheTeam(Team t) {
        int start = numberOfTeams();
        int end = start + 1;
        int gameNodeCounter = start + 2; //first number afer the [0 ... numberOfTeams - 1] representing teams, then start, then end
        Bag<FlowEdge> edges = new Bag<FlowEdge>();

        for (int i = 0; i < numberOfTeams(); i++) {
            //don't include the team in question or any team we already know to be (TRIVIALLY! THAT'S IMPORTANT!) eliminated
            if (i == t.id /*|| isTriviallyEliminated(teams[i])*/) { continue; }
            for (int j = 0; j < i; j++) {
                //don't include the team in question, any eliminated team, OR teams that won't play each other again
                if (j == t.id /*|| isTriviallyEliminated(teams[j])*/ || teams[i].remainingGamesAgainst[j] == 0) { continue; }
                edges.add(new FlowEdge(start, gameNodeCounter, teams[i].remainingGamesAgainst[j]));
                edges.add(new FlowEdge(gameNodeCounter, i, Double.POSITIVE_INFINITY));
                edges.add(new FlowEdge(gameNodeCounter, j, Double.POSITIVE_INFINITY));
                gameNodeCounter++;
            }
            // including an edge from team vertex i to the sink vertex with capacity w[x] + r[x] - w[i].
            edges.add(new FlowEdge(i, end, t.wins + t.remainingGames - teams[i].wins));
        }

        FlowNetwork net = new FlowNetwork(gameNodeCounter + 2); //those two are start and end
        for (FlowEdge edge : edges) { net.addEdge(edge); }
        return new FordFulkerson(net, start, end);
    }
}