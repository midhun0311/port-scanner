import java.util.HashMap;
import java.util.Map;

/**
 * Service Detector Class
 * Maps ports to common services and protocols
 */
public class ServiceDetector {
    
    private static final Map<Integer, String> serviceMap = new HashMap<>();

    static {
        // Well-known ports and their services
        serviceMap.put(21, "FTP");
        serviceMap.put(22, "SSH");
        serviceMap.put(23, "TELNET");
        serviceMap.put(25, "SMTP");
        serviceMap.put(53, "DNS");
        serviceMap.put(80, "HTTP");
        serviceMap.put(110, "POP3");
        serviceMap.put(143, "IMAP");
        serviceMap.put(443, "HTTPS");
        serviceMap.put(445, "SMB");
        serviceMap.put(465, "SMTPS");
        serviceMap.put(587, "SMTP");
        serviceMap.put(993, "IMAPS");
        serviceMap.put(995, "POP3S");
        serviceMap.put(1433, "MSSQL");
        serviceMap.put(3306, "MySQL");
        serviceMap.put(3389, "RDP");
        serviceMap.put(5432, "PostgreSQL");
        serviceMap.put(5984, "CouchDB");
        serviceMap.put(6379, "Redis");
        serviceMap.put(8080, "HTTP-ALT");
        serviceMap.put(8443, "HTTPS-ALT");
        serviceMap.put(9200, "Elasticsearch");
        serviceMap.put(27017, "MongoDB");
        serviceMap.put(50070, "Hadoop");
    }

    /**
     * Detect service running on a specific port
     * @param port Port number
     * @return Service name or "Unknown"
     */
    public static String detectService(int port) {
        return serviceMap.getOrDefault(port, "Unknown");
    }

    /**
     * Check if port is in well-known range (1-1023)
     */
    public static boolean isWellKnownPort(int port) {
        return port >= 1 && port <= 1023;
    }

    /**
     * Check if port is in registered range (1024-49151)
     */
    public static boolean isRegisteredPort(int port) {
        return port >= 1024 && port <= 49151;
    }

    /**
     * Check if port is in dynamic/private range (49152-65535)
     */
    public static boolean isDynamicPort(int port) {
        return port >= 49152 && port <= 65535;
    }

    /**
     * Get port category
     */
    public static String getPortCategory(int port) {
        if (isWellKnownPort(port)) {
            return "Well-Known";
        } else if (isRegisteredPort(port)) {
            return "Registered";
        } else if (isDynamicPort(port)) {
            return "Dynamic/Private";
        }
        return "Invalid";
    }

    /**
     * Get all registered services
     */
    public static Map<Integer, String> getAllServices() {
        return new HashMap<>(serviceMap);
    }
}
