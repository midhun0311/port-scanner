# 🚀 Quick Reference Guide - Advanced Port Scanner

## 📌 Quick Start

### Compile
```bash
javac *.java
```

### Run CLI
```bash
java PortScanner
```

### Run GUI
Open `index.html` in browser

---

## 🎯 Common Commands

### Scan Example 1: Default
```
Target: google.com
Start Port: 1
End Port: 1000
Timeout: 200
```

### Scan Example 2: Specific
```
Target: 192.168.1.1
Start Port: 20
End Port: 25
Timeout: 300
```

### Scan Example 3: Deep
```
Target: localhost
Start Port: 1
End Port: 65535
Timeout: 100
```

---

## 🔧 Configuration Reference

### Thread Pool
```java
// In PortScanner.java, line ~20
private static final int THREAD_POOL_SIZE = 50;

// Recommendations:
// Slow network: 10-25
// Normal network: 50 (default)
// Fast network: 100-200
```

### Timeout Values (milliseconds)
| Value | Speed | Reliability |
|-------|-------|-------------|
| 50ms | Very fast | Low |
| 100ms | Fast | Medium |
| 200ms | Balanced | Good (default) |
| 500ms | Slow | High |
| 1000ms+ | Very slow | Very high |

---

## 📊 Port Categories

### Well-Known (0-1023)
```
21: FTP
22: SSH
23: TELNET
25: SMTP
53: DNS
80: HTTP
110: POP3
143: IMAP
443: HTTPS
445: SMB
```

### Common High Ports (1024-49151)
```
3306: MySQL
3389: RDP
5432: PostgreSQL
5984: CouchDB
6379: Redis
8080: HTTP-ALT
8443: HTTPS-ALT
9200: Elasticsearch
27017: MongoDB
50070: Hadoop
```

---

## 💻 Code Examples

### 1. Basic Scan
```java
PortScanner scanner = new PortScanner("google.com", 1, 1000, 200);
scanner.startScan();
```

### 2. Get Results
```java
List<PortScanner.PortResult> results = scanner.getResults();
for (PortScanner.PortResult result : results) {
    System.out.println("Port " + result.port + ": " + result.service);
}
```

### 3. Service Detection
```java
String service = ServiceDetector.detectService(80);
// Returns: "HTTP"
```

### 4. Banner Grabbing
```java
String banner = BannerGrabber.grabBanner("localhost", 80, 200);
// Returns: "Apache/2.4.6"
```

### 5. Save Results
```java
ResultSaver.saveTXT(results, "google.com");
ResultSaver.saveCSV(results, "google.com");
ResultSaver.saveJSON(results, "google.com");
ResultSaver.saveHTML(results, "google.com");
```

---

## 🎨 GUI Quick Actions

### Preset Buttons
- **Standard (1-1000)**: Default Common ports
- **Common (1-1023)**: Well-known ports only
- **All (1-65535)**: Complete range

### Export Buttons
- **TXT**: Plain text format
- **CSV**: Excel/spreadsheet format
- **JSON**: Structured data format
- **HTML**: Web-viewable report

---

## 📈 Performance Tips

### For Maximum Speed
```
- Threads: 200
- Timeout: 50ms
- Port Range: ≤1000
- Result: 4-5 seconds for 1000 ports
```

### For Reliability
```
- Threads: 50
- Timeout: 500ms
- Port Range: Flexible
- Result: Accurate, comprehensive
```

### For Stealth (IDS Evasion)
```
- Threads: 5-10
- Timeout: 2000ms+
- Port Range: Randomized
- Result: Slow but less detectable
```

---

## 🐛 Troubleshooting

### Problem: Compilation Error
```bash
# Solution: Ensure all files in same directory
ls *.java  # Check files exist
javac *.java
```

### Problem: "Cannot resolve host"
```
# Solution: Check internet connection
# Try: ping google.com
```

### Problem: All ports showing closed
```
# Solution: Increase timeout value
# Try: 500ms instead of 200ms
```

### Problem: Scan too slow
```
# Solution: Increase thread pool
# Edit: THREAD_POOL_SIZE = 100
```

### Problem: Out of memory error
```
# Solution: Increase Java heap
# Use: java -Xmx512m PortScanner
```

---

## 📂 File Descriptions

| File | Purpose | Size |
|------|---------|------|
| PortScanner.java | Main scanner logic | ~2 KB |
| ServiceDetector.java | Port-to-service mapping | ~1 KB |
| BannerGrabber.java | Banner & version info | ~1.5 KB |
| ThreadScanner.java | Thread worker | ~0.5 KB |
| ResultSaver.java | Export functionality | ~2 KB |
| index.html | Web UI | ~5 KB |
| styles.css | UI styling | ~8 KB |
| script.js | UI logic | ~7 KB |
| README.md | Documentation | ~10 KB |
| FEATURES.md | Feature list | ~12 KB |

---

## 🔐 Security Checklist

- ✅ Only scan owned networks
- ✅ Have authorization
- ✅ Document all scans
- ✅ Follow responsible disclosure
- ✅ Respect legal boundaries
- ✅ Use for authorized testing only

---

## 🎯 Feature Matrix

```
Basic Features
├─ Target Input ✅
├─ Port Scanning ✅
├─ Open Port Detection ✅
└─ Completion Message ✅

Intermediate Features
├─ Service Detection ✅
├─ Port Range Selection ✅
├─ Scan Timer ✅
├─ Port Count ✅
└─ Host Information ✅

Advanced Features
├─ Multi-Threading ✅
├─ Save to File ✅
├─ Banner Grabbing ✅
├─ Timeout Handling ✅
└─ Specific Port Scan ✅

Professional Features
├─ GUI Interface ✅
├─ Progress Bar ✅
├─ Color Output ✅
├─ Export CSV ✅
├─ Export JSON ✅
├─ Export HTML ✅
└─ OS Detection ✅
```

---

## 📞 Common Issues & Solutions

### Issue 1: "Address already in use"
```
Cause: Scanner already running
Solution: Wait or use different timeout
```

### Issue 2: False positives (closed ports showing open)
```
Cause: Firewall blocking
Solution: Increase timeout, check firewall rules
```

### Issue 3: "Connection reset by peer"
```
Cause: Network issue
Solution: Check network, retry scan
```

### Issue 4: Browser won't display HTML
```
Cause: File not loaded properly
Solution: Use "Open with" → Browser, or HTTP server
```

### Issue 5: Results not saving
```
Cause: No write permission
Solution: Check file permissions, use different directory
```

---

## 🚀 Performance Comparison

### Single-threaded (old way)
```
1000 ports × 200ms timeout = 200+ seconds
```

### Multi-threaded (our way)
```
50 threads → 200s ÷ 50 = 4 seconds
100 threads → 200s ÷ 100 = 2 seconds
Improvement: 50-100x faster!
```

---

## 💡 Pro Tips

1. **Start with small range**: Test with 100 ports first
2. **Adjust timeout**: Start with 200ms, increase if needed
3. **Check services**: Use service map to identify purpose
4. **Review banners**: Detect vulnerable versions
5. **Export results**: Keep records for compliance
6. **Use presets**: Quick start with common ranges
7. **Monitor progress**: Watch real-time progress bar
8. **Batch operations**: Save multiple scans for reports

---

## 🔗 Quick Links

| Resource | Location |
|----------|----------|
| Main Scanner | PortScanner.java |
| GUI Interface | index.html |
| Documentation | README.md |
| Features List | FEATURES.md |
| Service Map | ServiceDetector.java |
| Export Options | ResultSaver.java |

---

## 📊 Export Format Comparison

| Format | Best For | Pros | Cons |
|--------|----------|------|------|
| TXT | Viewing | Human-readable | Not parseable |
| CSV | Excel/Database | Structured, universal | Limited formatting |
| JSON | APIs/Web | Nested data, flexible | Verbose |
| HTML | Reports/Sharing | Beautiful, printable | Large file size |

---

## ⚡ Quick Performance Checklist

- [ ] Set appropriate thread count
- [ ] Adjust timeout for network speed
- [ ] Limit port range if possible
- [ ] Close other applications
- [ ] Check network connectivity
- [ ] Monitor system resources
- [ ] Save results before closing

---

**Version**: 2.0 | **Updated**: April 2024 | **Status**: Production Ready

---

## 🎓 Learning Points

This project teaches:
- **Networking**: Socket programming
- **Threading**: Concurrent execution
- **File I/O**: Reading/writing data
- **Data Structures**: Collections, Lists, Maps
- **GUI**: HTML/CSS/JavaScript
- **Software Design**: Architecture, patterns
- **Security**: Testing, scanning concepts

---

Happy Scanning! 🔐
