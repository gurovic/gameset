public class InvokerPool {
    private int max_invokers;
    private int busy_invokers;
    private List<InvokerRequest> request_queue;
    private void waitForFreeSpace(){};
    private void runInvokers(InvokerRequest request){}
    private void freeInvokerSpace(){}

}
