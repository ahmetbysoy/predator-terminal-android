# 🚀 Predator Terminal Android - Kurulum Rehberi

## Proje Oluşturuldu!

Native Kotlin ile yazılmış Binance kripto para terminal uygulaması hazır.

## 📁 Proje Yapısı

```
predator-terminal-android/
├── .github/workflows/build.yml    # GitHub Actions CI/CD
├── app/
│   ├── build.gradle.kts           # App dependencies
│   ├── proguard-rules.pro         # ProGuard rules
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/baysoy/predatorterminal/
│           ├── MainActivity.kt
│           ├── data/
│           │   ├── api/           # Binance REST API
│           │   ├── model/         # Data models
│           │   └── websocket/     # WebSocket client
│           ├── domain/
│           │   ├── engine/        # Signal engine & Depth manager
│           │   └── repository/    # Data repository
│           └── ui/
│               ├── chart/         # Chart screen & ViewModel
│               ├── navigation/    # Navigation graph
│               ├── settings/      # Settings screen
│               ├── signal/        # Signal screen
│               ├── theme/         # App theme
│               └── watchlist/     # Watchlist screen
├── build.gradle.kts               # Root build file
├── settings.gradle.kts
├── gradle/wrapper/
├── gradlew & gradlew.bat
├── .gitignore
├── LICENSE
├── README.md
└── CONTRIBUTING.md
```

## 🛠️ GitHub'a Yükleme Adımları

### 1. GitHub'da Repository Oluştur
- GitHub.com'a git
- "New repository" tıkla
- Repository adı: `baysoy`
- Public/Private seç
- "Create repository" tıkla

### 2. Local Git Başlat
```bash
cd /home/user/predator-terminal-android
git init
git add .
git commit -m "Initial commit: Predator Terminal Android"
```

### 3. GitHub'a Push Et
```bash
git remote add origin https://github.com/YOUR_USERNAME/baysoy.git
git branch -M main
git push -u origin main
```

## ⚙️ GitHub Actions Workflow

Workflow otomatik olarak:
1. **Her push'ta** build çalışır
2. **Debug APK** oluşturulur
3. **Release APK** oluşturulur
4. **Artifact** olarak yüklenir
5. **Main branch'e** push'ta release oluşturulur

## 📱 APK İndirme

1. GitHub repo → "Actions" tab'ı
2. Son workflow run'ı seç
3. "Artifacts" section'dan APK indir

## 🎨 Özellikler

- ✅ Jetpack Compose UI
- ✅ TradingView Lightweight Charts
- ✅ Binance WebSocket (Kline, Trade, Depth)
- ✅ Sinyal Motoru (RSI, MACD, Likidite)
- ✅ Derinlik Haritası
- ✅ Takip Listesi
- ✅ Ses & Haptic Feedback
- ✅ Modern Material3 Design

## 🔧 Geliştirme

Android Studio'da aç:
1. File → Open → `predator-terminal-android` klasörü
2. Gradle Sync
3. Run (Emulator veya cihaz)

## 📝 Notlar

- Min SDK: API 28 (Android 9.0)
- Target SDK: API 34
- Kotlin: 1.9.20
- Compose BOM: 2024.01.00

## 🐛 Sorun mu var?

GitHub Issues'a yaz!

---

**Hazırlayan**: Baysoy  
**Lisans**: MIT