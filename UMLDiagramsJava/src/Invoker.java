import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class Pair<A, B> {
    public A fst;
    public B snd;
    public Pair(A a, B b) {
        fst = a;
        snd = b;
    }
}

class InvokerState { }

interface InvokerObserver {
    void receive(InvokerState invokerState);
}

public class Invoker
{
    public String state = "InvokerCreated";
    public List<Pair<String, String>> rwMount;
    public List<Pair<String, String>> roMount;

    public long wallTimeLimitMs = 2000;
    public long cpuTimeLimitMs = 1000;
    public long memoryLimitMb = 256;
    public String stdin = null;
    public String stderr = null;
    public String stdout = null;

    public String path;
    public List<String> argv;

    public Invoker(String path, List<String> argv) {
        this.path = path;
        this.argv = argv;
    }

    public void run(InvokerObserver observer, String requestId) {

    }
}