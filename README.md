<div align="center">

# 🎵 Spotify API Integration Project

**Spotify Web API** ile sanatçı, albüm ve parça bilgilerini sorgulayan Spring Boot backend uygulaması.

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Spotify](https://img.shields.io/badge/Spotify_API-1DB954?style=for-the-badge&logo=spotify&logoColor=white)

</div>

---

## 📖 Hakkında

Bu proje, **Spotify Web API**'sini kullanarak sanatçı, albüm ve parça (track) bilgilerini sorgulamak ve yönetmek amacıyla **Spring Boot** framework'ü ile geliştirilmiş bir backend uygulamasıdır.

---

## 🚀 Teknolojiler

| Teknoloji | Açıklama |
|-----------|----------|
| **Java 17+** | Ana programlama dili |
| **Spring Boot** | Web ve konfigürasyon altyapısı |
| **Maven** | Bağımlılık yönetimi |
| **Spotify Web API** | Müzik verisi kaynağı |

---

## 📂 Proje Yapısı

```text
spotify-project/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/handedereli/spotify_project/
│   │   │   ├── config/          # Spotify API istemci ve güvenlik yapılandırmaları
│   │   │   ├── controller/      # API Endpoint'lerinin yönetildiği katman
│   │   │   ├── dto/             # Veri transfer nesneleri (Album, Artist, Track vb.)
│   │   │   ├── service/         # İş mantığı ve Spotify Client/Token yönetim katmanı
│   │   │   └── SpotifyProjectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── mvnw / mvnw.cmd
└── pom.xml
```

---

## 📡 API Endpoints

Uygulama `http://localhost:8080` üzerinde çalışır.

### 🎤 Sanatçı (Artist)

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/artist/{id}` | ID'ye göre sanatçı bilgilerini getirir |
| `GET` | `/api/artist/search?name={name}` | İsme göre sanatçı arar |
| `GET` | `/api/artist/bundle/{id}` | Sanatçı + albüm + parça bilgilerini toplu getirir |

### 💿 Albüm (Album)

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/album/{id}` | ID'ye göre albüm detaylarını getirir |
| `GET` | `/api/album/artist/{artistId}` | Sanatçının tüm albümlerini listeler |

### 🎵 Parça (Track)

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/track/{id}` | ID'ye göre parça detaylarını getirir |
| `GET` | `/api/track/artist/{artistId}/top` | Sanatçının en popüler parçalarını getirir |

> **Not:** Gerçek endpoint path'leri proje koduna göre farklılık gösterebilir.

---

## 🛠️ Kurulum ve Çalıştırma

### 1. Gereksinimler

Projeyi yerelde çalıştırmadan önce aşağıdakilerin kurulu olduğundan emin olun:
- Java JDK 17+
- Maven 3.x

### 2. Spotify Geliştirici Hesabı Ayarları

Spotify API'sine erişebilmek için **Client ID** ve **Client Secret** gereklidir:

1. [Spotify Developer Dashboard](https://developer.spotify.com/dashboard) adresine gidin ve giriş yapın.
2. **Create an App** seçeneği ile yeni bir uygulama oluşturun.
3. Oluşturduğunuz uygulamanın detaylarından **Client ID** ve **Client Secret** değerlerini kopyalayın.

### 3. Konfigürasyon

`src/main/resources/application.properties` dosyasına Spotify bilgilerinizi ekleyin:

```properties
# Sunucu Portu
server.port=8080

# Spotify API Kimlik Bilgileri
spotify.client.id=YOUR_SPOTIFY_CLIENT_ID
spotify.client.secret=YOUR_SPOTIFY_CLIENT_SECRET
```

### 4. Çalıştırma

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Uygulama `http://localhost:8080` adresinde çalışmaya başlayacaktır.

---

## 🧬 Mimari ve Katmanlar

```
İstek → Controller → Service → SpotifyClient → Spotify Web API
                                     ↑
                               TokenService
                          (OAuth 2.0 Token Yönetimi)
```

| Katman | Açıklama |
|--------|----------|
| **TokenService** | OAuth 2.0 erişim token'larını otomatik alır ve yönetir |
| **SpotifyClient** | Spotify API uç noktalarına HTTP istekleri atar |
| **ArtistService** | Sanatçı bilgilerini tekil veya toplu (bundle) getirir |
| **AlbumService** | Albüm detaylarına erişim sağlar |
| **TrackService** | Şarkı/parça detaylarını sorgular |

---

<div align="center">

Geliştirici: **[@hanndes](https://github.com/hanndes)**

</div>
