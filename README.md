# Haber Bülteni

Her akşam 21:00'de ilgi alanlarındaki önemli gelişmeleri tarar, Türkçe kısa maddeler halinde hazırlar.
Bülten GitHub Pages'te yayınlanır ve Android uygulamasında (`android/`) okunur.
Maddeye dokununca detaylı Türkçe özet ve orijinal kaynak linkleri açılır.
Uygulamadaki "Ara" ekranından bir konu araştırılıp rapor hazırlanabilir.

## Nasıl çalışıyor

1. **Toplama (yapay zeka yok):** RSS akışları, Google News (Türkçe, İngilizce, İspanyolca, Çince), Bing News,
   Hacker News, Hugging Face trendleri ve piyasa verileri taranır.
2. **Seçme:** Her başlık için adaylar `config.yaml` içindeki kriterlere göre 1-10 arası puanlanır.
   Aynı olayı anlatan haberler birleştirilir, önceki günlerde verilenler tekrar edilmez.
   Eşiği geçen yoksa "Kayda değer bir gelişme yok" yazılır.
3. **Detay:** Seçilen her haberin makale metni okunur ve Türkçe detay yazılır.
4. **Yayın:** Detay sayfaları ve uygulamanın okuduğu JSON GitHub Pages'te yayınlanır.

Her şey GitHub Actions üzerinde çalışır. Bilgisayarın açık olması gerekmez.
Ayrıntılı notlar için `DEVAM.md`.

## Kurulum (bir kerelik)

1. GitHub'da **Public** yeni bir depo oluştur: adı `haber-bulteni`, hiçbir dosya ekleme.
   (Public olması gerekiyor: Actions dakikaları ve Pages bu şekilde ücretsiz ve sınırsız.
   Depoda sadece haber özetleri ve ilgi alanları görünür; anahtarlar gizli kalır.)
2. **Settings > Secrets and variables > Actions > New repository secret** ile gizli değer ekle:
   - `DEEPSEEK_API_KEY`
3. Kod depoya gönderildikten sonra: **Settings > Pages > Build and deployment**:
   Source = *Deploy from a branch*, Branch = `main`, klasör = `/docs`, Save.
4. **Actions** sekmesinde iş akışlarını etkinleştir, **Günlük bülten > Run workflow** ile ilk denemeyi yap.

## İlgi alanlarını değiştirmek

Uygulamadaki **Konular** sekmesinden (seviye, ad, grup, yeni konu), ya da `config.yaml` dosyasını GitHub'da düzenleyerek:
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
