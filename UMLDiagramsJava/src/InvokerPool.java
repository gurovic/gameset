public class InvokerPool {
    private int max_invokers;
    private int busy_invokers;
    private List<InvokerRequest> request_queue;
    private void wait_for_free_invokers(){};
    private void runInvokers(InvokerRequest request){}

}
