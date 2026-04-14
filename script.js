// ==========================================
// ADVANCED PORT SCANNER - JAVASCRIPT
// ==========================================

// State management
let scanState = {
    isScanning: false,
    startTime: null,
    elapsedSeconds: 0,
    results: [],
    target: '',
    startPort: 1,
    endPort: 1000,
    totalPorts: 0,
    scannedPorts: 0,
    openPorts: 0
};

let elapsedTimer = null;

// ==========================================
// UTILITY FUNCTIONS
// ==========================================

/**
 * Format time from seconds to mm:ss format
 */
function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

/**
 * Show/Hide sections
 */
function showSection(sectionId) {
    document.getElementById('progressSection').style.display = 'none';
    document.getElementById('resultsSection').style.display = 'none';
    document.getElementById('errorSection').style.display = 'none';
    document.getElementById('configPanel').style.display = 'none';

    if (sectionId) {
        document.getElementById(sectionId).style.display = 'block';
    }
}

/**
 * Reset to initial state
 */
function resetScan() {
    scanState = {
        isScanning: false,
        startTime: null,
        elapsedSeconds: 0,
        results: [],
        target: '',
        startPort: 1,
        endPort: 1000,
        totalPorts: 0,
        scannedPorts: 0,
        openPorts: 0
    };

    if (elapsedTimer) {
        clearInterval(elapsedTimer);
    }

    document.getElementById('configPanel').style.display = 'block';
    document.getElementById('progressSection').style.display = 'none';
    document.getElementById('resultsSection').style.display = 'none';
    document.getElementById('errorSection').style.display = 'none';
}

/**
 * Set preset port ranges
 */
function setPreset(preset) {
    const presets = {
        standard: { start: 1, end: 1000 },
        common: { start: 1, end: 1023 },
        all: { start: 1, end: 65535 }
    };

    if (presets[preset]) {
        document.getElementById('startPort').value = presets[preset].start;
        document.getElementById('endPort').value = presets[preset].end;
    }
}

// ==========================================
// MAIN SCAN FUNCTIONS
// ==========================================

/**
 * Validate input
 */
function validateInput() {
    const target = document.getElementById('target').value.trim();
    const startPort = parseInt(document.getElementById('startPort').value);
    const endPort = parseInt(document.getElementById('endPort').value);
    const timeout = parseInt(document.getElementById('timeout').value);

    if (!target) {
        showError('Please enter a target (IP or domain)');
        return null;
    }

    if (isNaN(startPort) || startPort < 1 || startPort > 65535) {
        showError('Start port must be between 1 and 65535');
        return null;
    }

    if (isNaN(endPort) || endPort < 1 || endPort > 65535) {
        showError('End port must be between 1 and 65535');
        return null;
    }

    if (startPort > endPort) {
        showError('Start port must be less than or equal to end port');
        return null;
    }

    if (isNaN(timeout) || timeout < 50 || timeout > 5000) {
        showError('Timeout must be between 50 and 5000 ms');
        return null;
    }

    return {
        target,
        startPort,
        endPort,
        timeout
    };
}

/**
 * Start scanning
 */
function startScan() {
    const config = validateInput();
    if (!config) return;

    // Initialize scan state
    scanState.target = config.target;
    scanState.startPort = config.startPort;
    scanState.endPort = config.endPort;
    scanState.totalPorts = config.endPort - config.startPort + 1;
    scanState.scannedPorts = 0;
    scanState.openPorts = 0;
    scanState.results = [];
    scanState.isScanning = true;
    scanState.startTime = Date.now();

    // Show progress section
    document.getElementById('configPanel').style.display = 'none';
    document.getElementById('progressSection').style.display = 'block';
    document.getElementById('resultsSection').style.display = 'none';
    document.getElementById('errorSection').style.display = 'none';

    // Start elapsed time counter
    elapsedTimer = setInterval(() => {
        scanState.elapsedSeconds = Math.floor((Date.now() - scanState.startTime) / 1000);
        document.getElementById('elapsedTime').textContent = formatTime(scanState.elapsedSeconds);
    }, 100);

    // Simulate scan (in real app, this would call Java backend via HTTP)
    simulateScan(config);
}

/**
 * Simulate port scanning (for demo purposes)
 */
function simulateScan(config) {
    // Common open ports for simulation
    const commonOpenPorts = [
        { port: 22, service: 'SSH', banner: 'OpenSSH 7.4' },
        { port: 80, service: 'HTTP', banner: 'Apache/2.4.6' },
        { port: 443, service: 'HTTPS', banner: 'Apache/2.4.6' },
        { port: 3306, service: 'MySQL', banner: 'MySQL 5.7.26' },
        { port: 5432, service: 'PostgreSQL', banner: 'PostgreSQL 11' },
        { port: 8080, service: 'HTTP-ALT', banner: 'Tomcat 7.0' }
    ];

    const portsToCheck = commonOpenPorts.filter(p => 
        p.port >= config.startPort && p.port <= config.endPort
    );

    let scanned = 0;

    // Simulate scanning all ports
    const scanInterval = setInterval(() => {
        scanned++;

        // Randomly decide if port should be open
        if (scanned % Math.floor(Math.random() * 50 + 30) === 0 && portsToCheck.length > 0) {
            const randomPort = portsToCheck[Math.floor(Math.random() * portsToCheck.length)];
            addResultToUI(randomPort);
        }

        scanState.scannedPorts = Math.min(scanned, scanState.totalPorts);
        updateProgress();

        if (scanned >= scanState.totalPorts) {
            clearInterval(scanInterval);
            completeScan();
        }
    }, 50); // Adjust speed based on port range
}

/**
 * Add result to UI
 */
function addResultToUI(result) {
    // Check if already added
    if (scanState.results.some(r => r.port === result.port)) {
        return;
    }

    scanState.results.push(result);
    scanState.openPorts++;
    document.getElementById('openPorts').textContent = scanState.openPorts;
}

/**
 * Update progress bar and stats
 */
function updateProgress() {
    const percentage = Math.floor((scanState.scannedPorts / scanState.totalPorts) * 100);
    document.getElementById('progressFill').style.width = percentage + '%';
    document.getElementById('progressText').textContent = percentage + '%';
    document.getElementById('portScanned').textContent = scanState.scannedPorts + ' / ' + scanState.totalPorts;
}

/**
 * Complete scan and show results
 */
function completeScan() {
    scanState.isScanning = false;

    if (elapsedTimer) {
        clearInterval(elapsedTimer);
    }

    // Hide progress, show results
    document.getElementById('progressSection').style.display = 'none';
    document.getElementById('resultsSection').style.display = 'block';

    // Populate results
    document.getElementById('resultTarget').textContent = scanState.target;
    document.getElementById('totalOpenPorts').textContent = scanState.openPorts;
    document.getElementById('scanTime').textContent = formatTime(scanState.elapsedSeconds);

    // Populate table
    const tbody = document.getElementById('resultsTableBody');
    tbody.innerHTML = '';

    if (scanState.results.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center; padding: 20px;">❌ No open ports found</td></tr>';
    } else {
        // Sort results by port
        scanState.results.sort((a, b) => a.port - b.port);

        scanState.results.forEach(result => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td class="col-port"><strong>${result.port}</strong></td>
                <td class="col-status"><span style="color: #4CAF50; font-weight: bold;">✅ OPEN</span></td>
                <td class="col-service">${result.service}</td>
                <td class="col-banner">${result.banner}</td>
            `;
            tbody.appendChild(row);
        });
    }
}

/**
 * Cancel scan
 */
function cancelScan() {
    scanState.isScanning = false;

    if (elapsedTimer) {
        clearInterval(elapsedTimer);
    }

    resetScan();
}

// ==========================================
// ERROR HANDLING
// ==========================================

/**
 * Show error message
 */
function showError(message) {
    if (scanState.isScanning) {
        cancelScan();
    }

    document.getElementById('configPanel').style.display = 'none';
    document.getElementById('progressSection').style.display = 'none';
    document.getElementById('resultsSection').style.display = 'none';
    document.getElementById('errorSection').style.display = 'block';
    document.getElementById('errorMessage').textContent = '⚠️ ' + message;
}

// ==========================================
// EXPORT FUNCTIONS
// ==========================================

/**
 * Export results in different formats
 */
function exportResults(format) {
    if (scanState.results.length === 0) {
        alert('No results to export');
        return;
    }

    let content = '';
    let filename = `port-scan-${Date.now()}`;
    let mimeType = 'text/plain';

    switch (format) {
        case 'txt':
            content = exportTXT();
            filename += '.txt';
            break;

        case 'csv':
            content = exportCSV();
            filename += '.csv';
            mimeType = 'text/csv';
            break;

        case 'json':
            content = exportJSON();
            filename += '.json';
            mimeType = 'application/json';
            break;

        case 'html':
            content = exportHTML();
            filename += '.html';
            mimeType = 'text/html';
            break;
    }

    // Download file
    const blob = new Blob([content], { type: mimeType });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
}

/**
 * Export as TXT
 */
function exportTXT() {
    let txt = '═'.repeat(70) + '\n';
    txt += 'PORT SCANNER RESULTS\n';
    txt += '═'.repeat(70) + '\n';
    txt += `Scan Date: ${new Date().toLocaleString()}\n`;
    txt += `Target: ${scanState.target}\n`;
    txt += `Total Open Ports: ${scanState.openPorts}\n`;
    txt += `Scan Time: ${formatTime(scanState.elapsedSeconds)}\n`;
    txt += '═'.repeat(70) + '\n\n';

    if (scanState.results.length === 0) {
        txt += 'No open ports found.\n';
    } else {
        txt += String.format('%-10s %-15s %-30s %-50s\n', 'Port', 'Status', 'Service', 'Banner');
        txt += '-'.repeat(70) + '\n';

        scanState.results.forEach(result => {
            txt += `${result.port.toString().padEnd(10)}${'OPEN'.padEnd(15)}${result.service.padEnd(30)}${result.banner}\n`;
        });
    }

    return txt;
}

/**
 * Export as CSV
 */
function exportCSV() {
    let csv = 'Port,Status,Service,Banner,Scan Date,Target\n';

    const scanDate = new Date().toLocaleString();
    scanState.results.forEach(result => {
        csv += `${result.port},OPEN,${result.service},"${result.banner}",${scanDate},${scanState.target}\n`;
    });

    return csv;
}

/**
 * Export as JSON
 */
function exportJSON() {
    const data = {
        scanInfo: {
            target: scanState.target,
            scanDate: new Date().toLocaleString(),
            totalOpenPorts: scanState.openPorts,
            scanTime: formatTime(scanState.elapsedSeconds),
            elapsedSeconds: scanState.elapsedSeconds
        },
        results: scanState.results
    };

    return JSON.stringify(data, null, 2);
}

/**
 * Export as HTML
 */
function exportHTML() {
    let html = '<!DOCTYPE html>\n<html>\n<head>\n';
    html += '<title>Port Scan Results</title>\n';
    html += '<style>\n';
    html += 'body { font-family: Arial, sans-serif; margin: 20px; }\n';
    html += '.header { background-color: #4CAF50; color: white; padding: 20px; border-radius: 5px; }\n';
    html += 'table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n';
    html += 'th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n';
    html += 'th { background-color: #4CAF50; color: white; }\n';
    html += 'tr:nth-child(even) { background-color: #f2f2f2; }\n';
    html += 'tr:hover { background-color: #f0f8ff; }\n';
    html += '</style>\n</head>\n<body>\n';
    html += '<div class="header">\n';
    html += '<h1>Port Scanner Results</h1>\n';
    html += `<p>Target: ${scanState.target}</p>\n`;
    html += `<p>Scan Date: ${new Date().toLocaleString()}</p>\n`;
    html += `<p>Total Open Ports: ${scanState.openPorts}</p>\n`;
    html += '</div>\n';

    html += '<table>\n<thead>\n<tr>\n';
    html += '<th>Port</th>\n<th>Status</th>\n<th>Service</th>\n<th>Banner</th>\n';
    html += '</tr>\n</thead>\n<tbody>\n';

    scanState.results.forEach(result => {
        html += '<tr>\n';
        html += `<td>${result.port}</td>\n`;
        html += '<td>✅ OPEN</td>\n';
        html += `<td>${result.service}</td>\n`;
        html += `<td>${result.banner}</td>\n`;
        html += '</tr>\n';
    });

    html += '</tbody>\n</table>\n';
    html += '</body>\n</html>';

    return html;
}

// ==========================================
// INITIALIZATION
// ==========================================

/**
 * Fix for String.format (utility function)
 */
if (!String.format) {
    String.format = function(str) {
        let args = Array.prototype.slice.call(arguments, 1);
        return str.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] !== 'undefined' ? args[number] : match;
        });
    };
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    // Set initial config panel display
    const configPanel = document.querySelector('.config-panel');
    if (configPanel) {
        configPanel.id = 'configPanel';
    }

    // Add keyboard support
    document.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !scanState.isScanning) {
            const target = document.getElementById('target');
            if (document.activeElement !== target) {
                startScan();
            }
        }
    });

    console.log('✅ Port Scanner initialized successfully');
});

// ==========================================
// HELPER FUNCTIONS
// ==========================================

/**
 * Simulate network delay
 */
function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Check if target is valid IP or domain
 */
function isValidTarget(target) {
    const ipRegex = /^(\d{1,3}\.){3}\d{1,3}$/;
    const domainRegex = /^([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,}$/;

    return ipRegex.test(target) || domainRegex.test(target);
}

// ==========================================
// EXPORT FOR TESTING
// ==========================================

if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        startScan,
        cancelScan,
        resetScan,
        setPreset,
        exportResults
    };
}
