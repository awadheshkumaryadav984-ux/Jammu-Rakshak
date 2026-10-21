# 🛡️ Jammu Rakshak (जम्मू रक्षक)
### Resilience Command Dashboard — Jammu Smart City Hackathon 2026
**Theme 06: Urban Resilience & Disaster Management | Team: ENDLIFERZ**

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new)
[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start)

---

## 🌟 Executive Overview
**Jammu Rakshak** is a unified command dashboard consolidating three defense shields across Jammu City:
1. 🔥 **Fire Shield (Detect)**: Real-time thermal, smoke ppm, and gas telemetry with edge false-alarm filtration and rapid route optimization.
2. 🏛️ **Structure Shield (Monitor)**: Sensor-driven health monitoring of 10 ancient heritage and high-rise structures (tilt angle, crack width, seismic vibration).
3. 💧 **Water Shield (Absorb)**: Continuous monitoring of 8 city-wide drainage channels (>70% flood warning threshold) coupled with **Sponge City** bioswales & aquifer recharge basins reducing waterlogging by 85%.

---

## 🚀 How to Publish to Website (1-Click Hosting)

You can publish this dashboard to a live public website in just a few minutes using any of the following methods:

### Option 1: Vercel (Instant 1-Click)
1. Push this project to your GitHub account (or export ZIP and upload to GitHub).
2. Go to [vercel.com/new](https://vercel.com/new).
3. Import your repository.
4. Click **Deploy**! (The pre-configured `vercel.json` and `index.html` require zero extra configuration).

### Option 2: Netlify (Drag & Drop)
1. Download or export the project ZIP from the AI Studio settings menu.
2. Go to [app.netlify.com/drop](https://app.netlify.com/drop).
3. Drag and drop the folder.
4. Your website will be live instantly with a free `.netlify.app` public domain!

### Option 3: GitHub Pages
1. Push this project to GitHub.
2. Go to your repository's **Settings** → **Pages**.
3. Under **Build and deployment**, select **Source: GitHub Actions**.
4. The workflow at `.github/workflows/deploy.yml` will automatically build and publish your website.

---

## 📱 Progressive Web App (PWA)
This project is configured as a standalone **Progressive Web App**:
- Includes `manifest.json` and `sw.js` (Service Worker).
- Users visiting the website can click **"Install App"** in Chrome, Safari, Edge, or Android to install it directly to their home screen or desktop with offline support.

---

## 🛠️ Key Dashboard Capabilities
- **Tactical Leaflet GIS Map**: Centered on Jammu (`32.7300° N, 74.8600° E`) with real markers for Talab Tillo, Janipur, Roop Nagar, Jewel Chowk, Sanjay Nagar, Golepulli, Old City, and Bahu Fort.
- **Emergency Dispatch Routing**: Plots optimal route from nearest fire brigade unit to incident site with live countdown ETA timer.
- **Sponge City Layer**: Proposed rain gardens and recharge wells with waterlogging comparison (**7.5 hrs → 1.1 hrs, -85%**).
- **Citizen Crack AI Inspection**: Powered by **Gemini 3.5 Flash Vision API** for structural forensic analysis.
- **Multi-Lingual Emergency SMS**: Automated citizen broadcast notifications in **Dogri (डोगरी)**, **Hindi (हिंदी)**, **Urdu (اردو)**, and **English (EN)**.
- **Inter-Agency Dispatch Log**: Verified dispatch coordination with JMC Chief Engineers, Fire Services, and SDMA.
- **SitRep Export**: One-click download of JSON Situational Reports for municipal administration and hackathon judges.

---

## 🏗️ Architecture
```
Sensors (LoRaWAN Mesh) 
   ↓
MQTT Gateway Brokers
   ↓
Spatial Risk Engine
   ↓
AI Inference Engine (Gemini 3.5 Flash)
   ↓
Jammu Rakshak Command Dashboard
   ↓
Multi-Agency Automated Dispatch & Citizen SMS
```

**Developed by Team ENDLIFERZ for Jammu Smart City Hackathon 2026.**
