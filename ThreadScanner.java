import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Thread Scanner Class
 * Represents a worker thread that scans a range of ports
 */
public class ThreadScanner implements Callable<List<PortScanner.PortResult>> {

    private String host;
    private int startPort;
    private int endPort;
    private int timeout;

    public ThreadScanner(String host, int startPort, int endPort, int timeout) {
        this.host = host;
        this.startPort = startPort;
        this.endPort = endPort;
        this.timeout = timeout;
    }

    /**
     * Scan a range of ports
     */
    @Override
    public List<PortScanner.PortResult> call() throws Exception {
        List<PortScanner.PortResult> results = new ArrayList<>();

        for (int port = startPort; port <= endPort; port++) {
            if (scanPort(port)) {
                String service = ServiceDetector.detectService(port);
                String banner = BannerGrabber.grabBanner(host, port, timeout);
                results.add(new PortScanner.PortResult(port, "OPEN", service, banner));
            }
        }

        return results;
    }

    /**
     * Scan individual port
     */
    private boolean scanPort(int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
