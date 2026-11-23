# Haber Bülteni

Her sabah 08:30'da ilgi alanlarındaki önemli gelişmeleri tarar, Türkçe kısa maddeler halinde Telegram'a gönderir.
Maddeye dokununca detaylı Türkçe özet ve orijinal kaynak linkleri açılır.
Telegram'dan `araştır konu` yazınca o konuyu araştırıp rapor hazırlar.

## Nasıl çalışıyor

1. **Toplama (yapay zeka yok):** RSS akışları, Google News (Türkçe, İngilizce, İspanyolca, Çince), Bing News,
   Hacker News, Hugging Face trendleri ve piyasa verileri taranır.
2. **Seçme (DeepSeek):** Her başlık için adaylar `config.yaml` içindeki kriterlere göre 1-10 arası puanlanır.
   Aynı olayı anlatan haberler birleştirilir, önceki günlerde verilenler tekrar edilmez.
   Eşiği geçen yoksa "Kayda değer bir gelişme yok" yazılır.
3. **Detay (DeepSeek):** Seçilen her haberin makale metni okunur ve Türkçe detay yazılır.
4. **Yayın:** Detay sayfaları GitHub Pages'te yayınlanır, özet Telegram'a gider.

Her şey GitHub Actions üzerinde çalışır. Bilgisayarın açık olması gerekmez.

## Kurulum (bir kerelik)

### 1. Telegram botu
1. Telegram'da **@BotFather**'a yaz, `/newbot` gönder, bir isim ve kullanıcı adı ver.
2. Verdiği **token**'ı bir yere not et (kimseyle paylaşma).
3. Yeni botunu aç ve ona `merhaba` yaz.
4. Tarayıcıda şu adresi aç (TOKEN yerine kendi token'ını yaz; başındaki `bot` kelimesi silinmemeli):
   `https://api.telegram.org/botTOKEN/getUpdates`
   Çıkan metinde `"chat":{"id":123456789` kısmındaki sayı senin **chat id**'n.

### 2. GitHub deposu
1. GitHub'da **Public** yeni bir depo oluştur: adı `haber-bulteni`, hiçbir dosya ekleme.
   (Public olması gerekiyor: Actions dakikaları ve Pages bu şekilde ücretsiz ve sınırsız.
   Depoda sadece haber özetleri ve ilgi alanları görünür; anahtarlar gizli kalır.)
2. **Settings > Secrets and variables > Actions > New repository secret** ile üç gizli değer ekle:
   - `DEEPSEEK_API_KEY`
   - `TELEGRAM_BOT_TOKEN`
   - `TELEGRAM_CHAT_ID`
3. Kod depoya gönderildikten sonra: **Settings > Pages > Build and deployment**:
   Source = *Deploy from a branch*, Branch = `main`, klasör = `/docs`, Save.
4. **Actions** sekmesinde iş akışlarını etkinleştir, **Günlük bülten > Run workflow** ile ilk denemeyi yap.

## Kullanım

| Telegram'a yaz | Ne olur |
|---|---|
| `tara` | Günlük bülteni hemen hazırlar |
| `araştır RX 7800 XT için en iyi yerel model` | Konuyu araştırıp rapor gönderir |
| `tara arda güler sakatlık` | `araştır` ile aynı |
| `yardım` | Komut listesi |

Bot mesajları 5 dakikada bir kontrol eder; cevap genelde 5-15 dakika içinde gelir.

## İlgi alanlarını değiştirmek

`config.yaml` dosyasını GitHub'da (telefondan da olur) düzenle:
- `criteria`: modele neyin önemli olduğunu anlatan metin. En etkili ayar budur.
- `min_importance`: yükseltirsen daha az ve daha önemli haber gelir.
- `sources`: aranacak kelimeler ve kaynaklar.
- `markets`: piyasa alarm eşikleri (yüzde).

## Yerelde çalıştırma

```powershell
python -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r requirements.txt
$env:DEEPSEEK_API_KEY = "..."
.\.venv\Scripts\python.exe -X utf8 -m bulten daily
.\.venv\Scripts\python.exe -X utf8 -m bulten render
```
