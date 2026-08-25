# Predator Terminal Android

Native Kotlin ile yazılmış Binance kripto para terminal uygulaması.

## Özellikler

- 📈 Gerçek zamanlı mum grafikleri (TradingView Lightweight Charts)
- 📊 Derinlik haritası (Orderbook overlay)
- ⚡ Sinyal motoru (RSI, MACD, Likidite analizi)
- 🔔 Ses ve haptic geri bildirim
- ⭐ Takip listesi
- 🎨 Modern Jetpack Compose UI

## Teknolojiler

- **Dil**: Kotlin
- **UI**: Jetpack Compose
- **Grafik**: TradingView Lightweight Charts Android
- **WebSocket**: OkHttp
- **API**: Retrofit + Moshi
- **Build**: Gradle (Kotlin DSL)
- **CI/CD**: GitHub Actions

## Kurulum

1. Repository'yi klonlayın
2. Android Studio'da açın
3. Sync & Run

## GitHub Actions Workflow

Her push'ta otomatik APK build edilir:

- `debug-apk` branch: Debug APK
- `release-apk` branch: Release APK (imzalı)

## Mimari

```
app/
├── data/
│   ├── api/          # Binance REST API
│   ├── websocket/    # WebSocket bağlantıları
│   └── model/        # Veri modelleri
├── domain/
│   ├── engine/       # Sinyal motoru
│   └── repository/   # Veri repository'leri
├── ui/
│   ├── chart/        # Grafik ekranı
│   ├── signal/       # Sinyal ekranı
│   ├── watchlist/    # Takip listesi
│   └── settings/     # Ayarlar
└── util/             # Yardımcı sınıflar
```

## License

MIT License