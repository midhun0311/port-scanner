import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * Banner Grabber Class
 * Retrieves service banners to detect service version
 */
public class BannerGrabber {

    /**
     * Grab banner from a specific port
     * @param host Target host/IP
     * @param port Target port
     * @param timeout Connection timeout in milliseconds
     * @return Service banner or null if unable to grab
     */
    public static String grabBanner(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);

            // Get input stream
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

            // Read banner (first line)
            String banner = reader.readLine();

            reader.close();
            socket.close();

            if (banner != null && !banner.trim().isEmpty()) {
                return cleanBanner(banner);
            }

            return null;

        } catch (SocketTimeoutException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Clean banner by removing special characters and extra spaces
     */
    private static String cleanBanner(String banner) {
        return banner.trim()
                .replaceAll("[\\x00-\\x1F]", "")
                .replaceAll("\\s+", " ")
                .substring(0, Math.min(banner.length(), 100));
    }

    /**
     * Send custom command to port and get response
     */
    public static String sendCommand(String host, int port, String command, int timeout) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);

            // Send command
            OutputStream out = socket.getOutputStream();
            out.write((command + "\r\n").getBytes(StandardCharsets.UTF_8));
            out.flush();

            // Read response
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
                if (response.length() > 1000) break; // Limit response size
            }

            reader.close();
            socket.close();

            return response.toString();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Detect OS based on TTL or other fingerprinting
     */
    public static String detectOS(String host) {
        try {
            ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", host);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ttl=")) {
                    int ttl = Integer.parseInt(line.split("ttl=")[1].split("[^0-9]")[0]);
                    if (ttl <= 64) {
                        return "Linux/Unix";
                    } else if (ttl <= 128) {
                        return "Windows";
                    }
                }
            }

            reader.close();
            process.waitFor();

        } catch (Exception e) {
            // OS detection failed
        }

        return "Unknown";
    }
}
