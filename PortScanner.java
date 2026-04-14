import java.net.Socket;
import java.util.Scanner;

public class PortScanner {
    private String target;
    private int startPort;
    private int endPort;
    private int timeout;
    private List<PortResult> results;
    private ExecutorService executorService;
    private volatile int scannedPorts = 0;
    private long startTime;
    private static final int THREAD_POOL_SIZE = 50;

    public PortScanner(String target, int startPort, int endPort, int timeout) {
        this.target = target;
        this.startPort = startPort;
        this.endPort = endPort;
        this.timeout = timeout;
        this.results = Collections.synchronizedList(new ArrayList<>());
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * Main scanning method using multi-threading
     */
    public void startScan() {
        startTime = System.currentTimeMillis();
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔍 PORT SCANNER - INITIALIZED");
        System.out.println("=".repeat(60));
        System.out.println("Target: " + getResolvedIP());
        System.out.println("Scanning Ports: " + startPort + " - " + endPort);
        System.out.println("Timeout: " + timeout + "ms");
        System.out.println("Threads: " + THREAD_POOL_SIZE);
        System.out.println("=".repeat(60) + "\n");

        System.out.println("⏳ Scanning in progress...\n");

        for (int port = startPort; port <= endPort; port++) {
            final int currentPort = port;
            executorService.submit(() -> scanPort(currentPort));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("❌ Scanning interrupted: " + e.getMessage());
        }

        displayResults();
    }

    /**
     * Scan individual port
     */
    private void scanPort(int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(getResolvedIP(), port), timeout);
            
            String service = ServiceDetector.detectService(port);
            String banner = BannerGrabber.grabBanner(getResolvedIP(), port, timeout);
            
            PortResult result = new PortResult(port, "OPEN", service, banner);
            results.add(result);
            
            System.out.println("✅ Port " + port + " OPEN - Service: " + service);
            
            socket.close();
        } catch (SocketTimeoutException e) {
            // Port is closed or filtered
        } catch (ConnectException e) {
            // Port is closed
        } catch (Exception e) {
            // Other exceptions
        }
        
        scannedPorts++;
        printProgress();
    }

    /**
     * Print scanning progress
     */
    private void printProgress() {
        int totalPorts = endPort - startPort + 1;
        int percentage = (scannedPorts * 100) / totalPorts;
        int filledBars = percentage / 5;
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 20; i++) {
            bar.append(i < filledBars ? "█" : "░");
        }
        bar.append("] ").append(percentage).append("%");
        
        System.out.print("\r" + bar.toString());
    }

    /**
     * Display final results
     */
    private void displayResults() {
        long endTime = System.currentTimeMillis();
        double scanTime = (endTime - startTime) / 1000.0;

        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("✅ SCANNING COMPLETED");
        System.out.println("=".repeat(60));
        System.out.println("Scan Time: " + String.format("%.2f", scanTime) + " seconds");
        System.out.println("Total Open Ports: " + results.size());
        System.out.println("=".repeat(60) + "\n");

        if (results.isEmpty()) {
            System.out.println("❌ No open ports found\n");
        } else {
            System.out.println("📊 RESULTS:");
            System.out.println("-".repeat(60));
            System.out.printf("%-8s %-12s %-20s\n", "Port", "Status", "Service");
            System.out.println("-".repeat(60));
            
            for (PortResult result : results) {
                System.out.printf("%-8d %-12s %-20s\n", result.port, result.status, result.service);
            }
            System.out.println("-".repeat(60) + "\n");
        }
    }

    /**
     * Resolve domain to IP address
     */
    public String getResolvedIP() {
        try {
            return InetAddress.getByName(target).getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("❌ Unable to resolve: " + target);
            return target;
        }
    }

    /**
     * Get scan results
     */
    public List<PortResult> getResults() {
        return results;
    }

    /**
     * Port Result Class
     */
    public static class PortResult {
        public int port;
        public String status;
        public String service;
        public String banner;

        public PortResult(int port, String status, String service, String banner) {
            this.port = port;
            this.status = status;
            this.service = service;
            this.banner = banner != null ? banner : "N/A";
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("==== Java Port Scanner ====");
        System.out.print("Enter Target (IP or Website): ");

        String target = scanner.nextLine();

        System.out.println("\nScanning " + target + "...\n");

        for (int port = 1; port <= 100; port++) {
            try {
                Socket socket = new Socket(target, port);
                System.out.println("Port " + port + " is OPEN");
                socket.close();
            } catch (Exception e) {
                // Port closed - do nothing
            }
        }

        System.out.println("\nScanning Completed.");
    }
}