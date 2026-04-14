# 🔐 Advanced Port Scanner

A professional-grade port scanning tool with multi-threaded support, banner grabbing, service detection, and multiple export formats. Built with Java backend and modern HTML/CSS/JavaScript frontend.

## 📋 Features

### ✅ Basic Features (0–30%)
- **Target Input**: Scan by IP address or domain name
- **Port Scanning**: Customizable port range (1-65535)
- **Open Port Detection**: Identify open ports with visual indicators
- **Scan Completion**: Real-time progress and completion messages

### 🟡 Intermediate Features (30–60%)
- **Service Detection**: Automatically identify services on detected ports
- **Port Range Selection**: Custom start and end ports
- **Scan Timer**: Track elapsed time during scanning
- **Total Open Ports Count**: Summary statistics
- **Host Information**: Display target IP and domain resolution

### 🔴 Advanced Features (60–85%)
- **Multi-Threading**: 50 concurrent threads for 10x faster scanning
- **Save Results to File**: Export to TXT, CSV, JSON, HTML formats
- **Banner Grabbing**: Detect service version and information
- **Timeout Handling**: Configurable TCP timeout (50-5000ms)
- **Scan Specific Ports**: Option to scan individual ports

### 🔥 Professional Features (85–100%)
- **Modern GUI Interface**: Beautiful web-based UI with HTML/CSS/JavaScript
- **Progress Bar**: Visual scanning progress indicator
- **Color Output**: Green for open, red for closed (in CLI)
- **Export Options**: Multiple format support (CSV, TXT, JSON, HTML)
- **OS Detection**: Basic OS fingerprinting capabilities

## 🏗️ Project Structure

```
Java-Port-Scanner/
├── PortScanner.java          # Main scanner (multi-threaded)
├── ServiceDetector.java      # Port-to-service mapping
├── BannerGrabber.java        # Banner and version detection
├── ThreadScanner.java        # Thread worker class
├── ResultSaver.java          # Export functionality
├── index.html                # Web UI
├── styles.css                # UI styling
├── script.js                 # JavaScript interactivity
└── README.md                 # This file
```

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Modern web browser (Chrome, Firefox, Edge, Safari)

### Compilation

```bash
# Compile all Java files
javac *.java
```

### Running

#### CLI Version
```bash
java PortScanner
```

Interactive prompts:
1. Enter target (IP or Domain)
2. Enter starting port (default: 1)
3. Enter ending port (default: 1000)
4. Enter timeout in ms (default: 200)

#### Web GUI Version
Open `index.html` in your web browser for the interactive interface.

## 📖 Usage Examples

### Example 1: Basic Scan
```
Enter target: google.com
Enter starting port: 1
Enter ending port: 1000
Enter timeout in ms: 200

Output:
✅ Port 80 OPEN - Service: HTTP
✅ Port 443 OPEN - Service: HTTPS

Scanning Completed
Total Open Ports: 2
Scan Time: 12.34 seconds
```

### Example 2: Specific Ports
```
Enter target: localhost
Enter starting port: 20
Enter ending port: 25
```

### Example 3: Full System Scan
```
Enter target: 192.168.1.1
Enter starting port: 1
Enter ending port: 65535
Timeout: 100 (faster)

Uses 50 threads for parallel scanning
```

## 🔧 Configuration

### Thread Pool Size
Edit in `PortScanner.java`:
```java
private static final int THREAD_POOL_SIZE = 50; // Increase for faster scanning
```

### Timeout Values (ms)
- **Low (50-100ms)**: Faster, but may miss slow ports
- **Medium (200-500ms)**: Balanced
- **High (1000-5000ms)**: More reliable but slower

## 📊 Common Port Reference

| Port | Service | Purpose |
|------|---------|---------|
| 21   | FTP     | File Transfer |
| 22   | SSH     | Secure Shell |
| 23   | TELNET  | Remote Login |
| 25   | SMTP    | Email Transmission |
| 53   | DNS     | Domain Name System |
| 80   | HTTP    | Web Browsing |
| 110  | POP3    | Email Retrieval |
| 143  | IMAP    | Email Access |
| 443  | HTTPS   | Secure Web |
| 445  | SMB     | Windows Sharing |
| 3306 | MySQL   | Database |
| 3389 | RDP     | Remote Desktop |
| 5432 | PostgreSQL | Database |
| 6379 | Redis   | Cache/Store |
| 8080 | HTTP-ALT | Alternate Web |

## 💾 Export Formats

### CSV Export
```csv
Port,Status,Service,Banner,Scan Date,Target
22,Open,SSH,"OpenSSH 7.4",2024-04-14 10:30,google.com
80,Open,HTTP,"Apache/2.4.6",2024-04-14 10:30,google.com
443,Open,HTTPS,"Apache/2.4.6",2024-04-14 10:30,google.com
```

### JSON Export
```json
{
  "scanInfo": {
    "target": "google.com",
    "scanDate": "2024-04-14",
    "totalOpenPorts": 3
  },
  "results": [
    {
      "port": 22,
      "status": "OPEN",
      "service": "SSH",
      "banner": "OpenSSH 7.4"
    }
  ]
}
```

### HTML Export
Beautiful formatted results with CSS styling, perfect for reports.

## 🎯 Port Categories

- **Well-Known Ports**: 1-1023 (system/reserved)
- **Registered Ports**: 1024-49151 (user/application)
- **Dynamic Ports**: 49152-65535 (ephemeral)

## ⚙️ Advanced Options

### Multi-threaded Scanning
Example: Scanning ports 1-10000 with 50 threads:
- Thread 1: Ports 1-200
- Thread 2: Ports 201-400
- ... (50 total)

Result: ~10x faster than sequential scanning

### Banner Grabbing
```java
String banner = BannerGrabber.grabBanner("localhost", 80, 200);
// Returns: "Apache/2.4.6"
```

### Service Detection
```java
String service = ServiceDetector.detectService(22);
// Returns: "SSH"
```

## 🛡️ Security & Ethics

⚠️ **Important**: 
- Only scan networks you own or have explicit permission to scan
- Unauthorized port scanning is illegal in many jurisdictions
- This tool is for authorized security testing and administrative use only
- Respect privacy and network security policies

## 🐛 Troubleshooting

### Compilation Error: "cannot find symbol"
```bash
# Ensure all .java files are in the same directory
# Compile everything at once:
javac *.java
```

### Slow Scanning
- Reduce timeout value (100ms for fast networks)
- Increase thread pool size (up to 100-200)
- Scan smaller port ranges

### Ports Not Detected
- Increase timeout value
- Check firewall settings
- Verify target is reachable (ping first)

### Connection Refused
- Target may not be online
- Check IP/domain spelling
- Verify network connectivity

## 📈 Performance Tips

1. **For Speed**: Use 100-200 threads, 100ms timeout
2. **For Reliability**: Use 50 threads, 500ms timeout
3. **For Stealth**: Use 10 threads, 1000ms timeout (slower detection)
4. **Target Range**: Smaller ranges scan faster

## 🔍 Service Detection Supported

Currently supports 25+ common services:
- FTP, SSH, TELNET, SMTP, DNS
- HTTP, HTTPS, POP3, IMAP
- SMB, MSSQL, MySQL, PostgreSQL
- Redis, MongoDB, CouchDB
- Elasticsearch, Hadoop, and more

## 🌐 Web Interface Features

- **Real-time Progress**: Live progress bar
- **Responsive Design**: Mobile-friendly interface
- **Quick Presets**: One-click port ranges
- **Multiple Exports**: CSV, JSON, HTML, TXT
- **Statistics**: Time, port count, open ports
- **Service Info**: Built-in port reference

## 📝 Logging

Scan results are automatically saved with timestamps:
```
results_1712000000000.txt
results_1712000000000.csv
results_1712000000000.html
results_1712000000000.json
```

## 🎓 Learning Resources

The code includes examples of:
- Multi-threading (ExecutorService)
- Network programming (Socket)
- File I/O (FileWriter)
- JSON/CSV formatting
- GUI development (HTML/CSS/JS)

## 🤝 Contributing

Feel free to:
- Report bugs
- Suggest features
- Improve code quality
- Add new export formats

## 📄 License

This project is for educational and authorized security testing purposes only.

## 👨‍💻 Author

Advanced Port Scanner | 2024

## 🔗 Related Tools

- **Nmap**: Professional network mapping tool
- **Zenmap**: GUI version of Nmap
- **Masscan**: Very fast port scanner
- **Shodan**: Internet search engine for devices

## ❓ FAQ

**Q: Is this legal to use?**
A: Only on networks you own or have written permission to test.

**Q: How fast is the scanning?**
A: With 50 threads and 200ms timeout, scans ~1000 ports in 4-5 seconds.

**Q: Can it scan multiple targets?**
A: Currently scans one target at a time. Modification needed for batch scanning.

**Q: What's the minimum port?**
A: Port 1. Well-known ports are 1-1023.

**Q: Does it work on Windows/Mac/Linux?**
A: Yes! Java is cross-platform.

---

🔐 **Stay Secure. Scan Responsibly.**
