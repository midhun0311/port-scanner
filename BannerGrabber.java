import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class BannerGrabber {

    public static String grabBanner(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

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

    private static String cleanBanner(String banner) {
        return banner.trim()
                .replaceAll("[\\x00-\\x1F]", "")
                .replaceAll("\\s+", " ")
                .substring(0, Math.min(banner.length(), 100));
    }

    public static String sendCommand(String host, int port, String command, int timeout) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(new java.net.InetSocketAddress(host, port), timeout);

            OutputStream out = socket.getOutputStream();
            out.write((command + "\r\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
                if (response.length() > 1000) break; 
            }

            reader.close();
            socket.close();

            return response.toString();

        } catch (Exception e) {
            return null;
        }
    }

    
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
            
        }

        return "Unknown";
    }
}
