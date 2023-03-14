class MatchManager {
    private Manager manager;
    private MatchQueue matchQueue;
    private MatchRunner[] matchRunners;

    public MatchRunnersManager(MatchQueue matchQueue, int numMatchRunners) {
        this.matchQueue = matchQueue;
        this.matchRunners = new MatchRunner[numMatchRunners];
    }

    // get invoker number from manager
    public int getInvokerNumber() {
        return manager.getInvokerNumber();
    }

    public void run() {
        for (int i = 0; i < matchRunners.length; i++) {
            matchRunners[i] = new MatchRunner(matchQueue);
            matchRunners[i].start();
        }
    }

    //if invoker number is more than 0 then run match
    public void runMatch() {
        if (getInvokerNumber() > 0) {
            run();
        }
    }
}
