import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Result Saver Class
 * Saves scan results to various file formats (TXT, CSV, JSON)
 */
public class ResultSaver {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Save results to text file
     */
    public static void saveTXT(List<PortScanner.PortResult> results, String target) {
        String filename = "results_" + System.currentTimeMillis() + ".txt";
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("═".repeat(70) + "\n");
            writer.write("PORT SCANNER RESULTS\n");
            writer.write("═".repeat(70) + "\n");
            writer.write("Scan Date: " + LocalDateTime.now().format(formatter) + "\n");
            writer.write("Target: " + target + "\n");
            writer.write("Total Open Ports: " + results.size() + "\n");
            writer.write("═".repeat(70) + "\n\n");

            if (results.isEmpty()) {
                writer.write("No open ports found.\n");
            } else {
                writer.write(String.format("%-10s %-15s %-30s %-100s\n", "Port", "Status", "Service", "Banner"));
                writer.write("-".repeat(70) + "\n");
                
                for (PortScanner.PortResult result : results) {
                    writer.write(String.format("%-10d %-15s %-30s %-100s\n", 
                        result.port, result.status, result.service, result.banner));
                }
            }

            writer.write("\n" + "═".repeat(70) + "\n");
            System.out.println("✅ Results saved to: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error saving TXT file: " + e.getMessage());
        }
    }

    /**
     * Save results to CSV file
     */
    public static void saveCSV(List<PortScanner.PortResult> results, String target) {
        String filename = "results_" + System.currentTimeMillis() + ".csv";
        
        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            writer.write("Port,Status,Service,Banner,Scan Date,Target\n");

            // Write data
            String scanDate = LocalDateTime.now().format(formatter);
            for (PortScanner.PortResult result : results) {
                writer.write(String.format("%d,%s,%s,\"%s\",%s,%s\n",
                    result.port,
                    result.status,
                    result.service,
                    escapeCSV(result.banner),
                    scanDate,
                    target));
            }

            System.out.println("✅ Results saved to: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error saving CSV file: " + e.getMessage());
        }
    }

    /**
     * Save results to JSON file
     */
    public static void saveJSON(List<PortScanner.PortResult> results, String target) {
        String filename = "results_" + System.currentTimeMillis() + ".json";
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("{\n");
            writer.write("  \"scanInfo\": {\n");
            writer.write("    \"target\": \"" + target + "\",\n");
            writer.write("    \"scanDate\": \"" + LocalDateTime.now().format(formatter) + "\",\n");
            writer.write("    \"totalOpenPorts\": " + results.size() + "\n");
            writer.write("  },\n");
            writer.write("  \"results\": [\n");

            for (int i = 0; i < results.size(); i++) {
                PortScanner.PortResult result = results.get(i);
                writer.write("    {\n");
                writer.write("      \"port\": " + result.port + ",\n");
                writer.write("      \"status\": \"" + result.status + "\",\n");
                writer.write("      \"service\": \"" + result.service + "\",\n");
                writer.write("      \"banner\": \"" + escapeJSON(result.banner) + "\"\n");
                writer.write("    }");
                
                if (i < results.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");

            System.out.println("✅ Results saved to: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error saving JSON file: " + e.getMessage());
        }
    }

    /**
     * Save results to HTML file
     */
    public static void saveHTML(List<PortScanner.PortResult> results, String target) {
        String filename = "results_" + System.currentTimeMillis() + ".html";
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("  <title>Port Scan Results</title>\n");
            writer.write("  <style>\n");
            writer.write("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("    .header { background-color: #4CAF50; color: white; padding: 10px; }\n");
            writer.write("    table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
            writer.write("    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("    th { background-color: #4CAF50; color: white; }\n");
            writer.write("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write("  </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("  <div class='header'>\n");
            writer.write("    <h1>Port Scanner Results</h1>\n");
            writer.write("    <p>Target: " + target + "</p>\n");
            writer.write("    <p>Scan Date: " + LocalDateTime.now().format(formatter) + "</p>\n");
            writer.write("    <p>Total Open Ports: " + results.size() + "</p>\n");
            writer.write("  </div>\n");

            writer.write("  <table>\n");
            writer.write("    <thead>\n");
            writer.write("      <tr>\n");
            writer.write("        <th>Port</th>\n");
            writer.write("        <th>Status</th>\n");
            writer.write("        <th>Service</th>\n");
            writer.write("        <th>Banner</th>\n");
            writer.write("      </tr>\n");
            writer.write("    </thead>\n");
            writer.write("    <tbody>\n");

            for (PortScanner.PortResult result : results) {
                writer.write("      <tr>\n");
                writer.write("        <td>" + result.port + "</td>\n");
                writer.write("        <td>" + result.status + "</td>\n");
                writer.write("        <td>" + result.service + "</td>\n");
                writer.write("        <td>" + result.banner + "</td>\n");
                writer.write("      </tr>\n");
            }

            writer.write("    </tbody>\n");
            writer.write("  </table>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");

            System.out.println("✅ Results saved to: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error saving HTML file: " + e.getMessage());
        }
    }

    /**
     * Escape CSV values
     */
    private static String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    /**
     * Escape JSON values
     */
    private static String escapeJSON(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }
}
