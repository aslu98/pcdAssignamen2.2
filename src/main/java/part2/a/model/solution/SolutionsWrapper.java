package part2.a.model.solution;

import part2.a.agency.TrainSolutionsAPIAgent;

import java.util.List;

public class SolutionsWrapper {
    private final List<Solution> solutions;
    private final int num;
    private final TrainSolutionsAPIAgent agent;
    private int readyCount = 0;

    public SolutionsWrapper(List<Solution> solutions, int num, TrainSolutionsAPIAgent agent) {
        this.solutions = solutions;
        this.num = num;
        this.agent = agent;
    }

    @Override
    public String toString() {
        String str = "";
        for (Solution sol: solutions){
            str +=  sol.toString() + "\n";
        }
        return str;
    }

    public void updateReady(){
        readyCount = readyCount + 1;
        if (readyCount == num){
            agent.SolutionsReady();
        }
    }

    public List<Solution> getSolutions() {
        return solutions;
    }
}
