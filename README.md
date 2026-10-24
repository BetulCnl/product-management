# Product Management (ilk sürüm)

Spring Boot ile yazılmış basit bir ürün yönetimi REST API'si. Daha sonra [mini-eticaret-sitesi](https://github.com/BetulCnl/mini-eticaret-sitesi) projesine dönüşen çalışmanın ilk adımı.

## Neler var

- Kullanıcı kaydı ve girişi, giriş sonrası JWT üretimi
- Ürünler için listeleme, getirme, ekleme, güncelleme, silme
- Bellek içi H2 veritabanı (uygulama kapanınca veriler silinir)

## Bilinen sorunlar

Proje bu haliyle açılmıyor. İki sebebi var:

1. `application.properties` UTF-8 değil, Latin-1 kodlamasıyla kaydedilmiş. Linux'ta Maven derlerken `MalformedInputException` hatası veriyor. Dosyayı UTF-8 olarak yeniden kaydetmek yeterli.
2. `application.properties` Spring Security'nin otomatik yapılandırmasını kapatıyor (`spring.autoconfigure.exclude=...SecurityAutoConfiguration`), ama `SecurityConfig` sınıfı hâlâ `HttpSecurity` bekliyor. Uygulama "required a bean of type HttpSecurity" hatasıyla duruyor. Ya bu `exclude` satırı silinmeli ya da `SecurityConfig` sınıfına `@EnableWebSecurity` eklenmeli.

Bu sorunlar sonraki sürümlerde ([13.08.25windows](https://github.com/BetulCnl/13.08.25windows), [mini-eticaret-sitesi](https://github.com/BetulCnl/mini-eticaret-sitesi)) yok.

## Çalıştırma

Java 21 gerekiyor.

```bash
cd productmanagement
./mvnw spring-boot:run
```

- API: `http://localhost:8080`
- H2 konsolu: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, kullanıcı `sa`, şifre boş)

## Endpoint'ler

| Method | URL | Açıklama |
|---|---|---|
| POST | `/api/auth/register` | kayıt |
| POST | `/api/auth/login` | giriş, JWT döner |
| GET | `/api/products` | tüm ürünler |
| GET | `/api/products/{id}` | tek ürün |
| POST | `/api/products` | ürün ekle |
| PUT | `/api/products/{id}` | ürün güncelle |
| DELETE | `/api/products/{id}` | ürün sil |

## Klasör yapısı

```
productmanagement/src/main/java/com/betul/productmanagement/
  controller/   UserController (auth), ProductController
  service/      UserService, ProductService
  entity/       User, Product
  repository/
  security/     JwtUtil
  DTO/          istek/cevap sınıfları
```
