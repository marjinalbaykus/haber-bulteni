# Devam notları (son güncelleme: 25 Kasım 2026, Android arayüzü bitti)

Yeni sohbette Claude'a şunu yaz:
"haber-bulteni projesine devam ediyoruz, DEVAM.md dosyasını oku. Sıradaki işlerden başla."

Kullanıcı tercihleri: her şey Türkçe yazılsın (wrap_up kutusu dahil); emoji ve uzun tire yok; DeepSeek'e ek para vermek
istemiyor (günlük bülten Claude Pro aboneliğiyle); yerel testleri ve ücretli API çalıştırmalarını kullanıcı demeden başlatma.
Büyük bir karar gerektiren işte önce sor ("önce karar verelim sonra yap dicem" dediği oldu).

## Genel yapı
- Depo (public): https://github.com/marjinalbaykus/haber-bulteni , yerel: `C:\Users\gokbe\Documents\compare\haber-bulteni`
- Site: https://marjinalbaykus.github.io/haber-bulteni/ (GitHub Pages, `/docs`). Uygulama için JSON:
  `/api/index.json` (tüm bültenler ve araştırmalar), `/api/b/<tarih>.json`, `/api/r/<id>.json`, `/api/topics.json`.
- Python paketi `bulten/`: `daily.py` (DeepSeek ile tek seferde bülten), `staged.py` (Claude rutini için adımlı, API'siz),
  `sources.py` (Google News, Bing, RSS, Hacker News, Hugging Face), `topics.py` (uygulamadan konu değişikliği),
  `research.py` (araştırma), `render.py` (site + api), `markets.py`, `telegram.py`. Yerelde çalıştırmak için `.venv\Scripts\python.exe`.

## Bülten nasıl hazırlanıyor
- **Her akşam 21:00 TR:** Claude bulut rutini "Günlük bülten (Sonnet)" (`trig_01F4ckY2JzmUQUwEMrvdLyV4`, cron `0 18 * * *`),
  https://claude.ai/code/routines/trig_01F4ckY2JzmUQUwEMrvdLyV4 . Akış: `python3 -m bulten staged prep --variant sonnet`
  -> paket başına bir Sonnet alt ajan -> `staged next` -> `data/bulletins/<tarih>.json` + `data/history.json` push.
  25 Kasım'da 08:30'dan 21:00'e alındı: bülten o günün gündemi olsun diye. İlk akşam çalışması 25 Kasım ~21:05.
- **publish.yml:** `data/bulletins/**` main'e gelince siteyi üretir (`scripts/commit_push.sh`) ve Telegram'a gönderir.
- **daily.yml (DeepSeek, elle, ücretli ~0,08 dolar):** uygulamada Ayarlar > "Bülteni şimdi hazırla" ile başlar.
  `date` girdisi: verilirse SADECE o günün haberleri (TR 00:00-24:00), bülten o tarihi alır; boşsa dün ve bugün birlikte,
  bülten bugünün tarihini alır. Geçmiş gün için Google News `after:/before:` tarih aralığıyla aranır (test edildi:
  24 Kasım için 2.252 haber o günün içinden geldi), Hacker News de aralıkla sınırlanır. RSS ve Bing sadece son
  haberleri verdiği için çok eski günlerde haber sayısı azalır.
- **Saklama:** config.yaml `retention_days: 730`. render, daha eski bültenleri ve karşılığı kalmayan sayfaları siler.
- **Tekrar önleme:** `data/history.json` son 7 günün başlık ve kaynaklarını tutar.
- Aynı olay birden fazla başlığa girerse tekilleştirme birini tutar, habere `also: [başlık id]` ekler.
- Telegram komut işi (poll.yml) KAPALI (`gh workflow enable poll.yml` ile açılır): GitHub zamanlayıcısı 5 dakikalık
  çalıştırmaları atlıyordu, cevaplar saatler sonra geliyordu. Araştırma artık uygulamadan.

## Konular (config.yaml + data/user_topics.json)
- Başlıklar: Arda Güler (Çok), Real Madrid (Orta), Şampiyonlar Ligi, Premier Lig, Donanım ve Çipler (Çok), Local LLM,
  AI Gündemi, AI Kodlama Araçları, Teknoloji Gündemi (Çok), Akıllı Telefonlar, Soulslike ve Aksiyon RPG, Oyun Dünyası,
  CS2 (Az), Türkiye Ekonomisi, Dünya Ekonomisi; kullanıcının eklediği: Türkiye Siyaseti, Barcelona, Türkiye Milli Futbol Takımı.
  Juventus kullanıcı tarafından kaldırıldı.
- Gruplar (sadece uygulamadaki görünüm): Futbol, Yapay Zeka, Teknoloji, Oyun, Ekonomi, Gündem.
- Seviyeler: Az (>=8 puan, en fazla 3), Orta (>=7, 5), Çok (>=6, 8).
- Uygulamadaki değişiklikler `data/user_topics.json`'a yazılır (levels, titles, removed, added, groups); `load_config()`
  bunu config.yaml'ın üstüne uygular. topics.yml `action=save`, `text=JSON` (`topics.save_all`). Yeni konu eklenirken
  sorgu ve kriter DeepSeek ile üretilir; kısa ad yazılırsa (5 kelimeye kadar) başlık aynen o olur.
- KURAL: iş akışı (.yml) dosyası değişince push'tan önce `python -c "import yaml;yaml.safe_load(open(F))"` ile kontrol et.
  İki kez tırnaksız ": " yüzünden iş akışı bozuldu (publish.yml ve topics.yml).

## Android uygulaması (`android/`)
- Kotlin + Jetpack Compose, paket `com.marjinalbaykus.bulten`, minSdk 26, AGP 9.5.2, ek kütüphane yok.
  Kod: `MainActivity.kt` (ekran yığını, alt çubuk, sağ üst sıralama/yenile, klavye payı), `data/Model.kt`, `data/Repo.kt`,
  `ui/Screens.kt` (Akış, haber kartı, grup sıralama, detay, geçmiş + tarih seçici, ayarlar), `ui/Research.kt` (Ara),
  `ui/Topics.kt` (Konular), `ui/Keyboard.kt`, `ui/Icons.kt`, `ui/Format.kt`, `notify/NewsJob.kt`.
- Alt çubuk: Akış, Ara, Konular (etiket simgesi), Ayarlar. Üç çizgili menü yok. Geçmiş bültenler Ara sayfasından açılır.
  Sekmeye ikinci basış (Akış) başa kaydırır. Üst çubukta (sadece Akış) küçük Sırala ve Yenile simgeleri; aşağı çekip yenileme de var.
- Akış: tarih/gün/hazırlanma saati, sağda küçük piyasa tablosu, "N haber tarandı, M haber seçildi". Haberler baloncuklu kart:
  "Önem X/10" rozeti (>=8 vurgulu), gruplu/puan modunda başlık etiketi, sadece haber başlığı. Okunan haber soluk
  (okundu bilgisi telefonda, "tarih/sid" anahtarıyla, 90 gün). Sırala menüsü: Gruplara göre (varsayılan, gruplar Konular'daki
  sırayla, sonra grupsuz başlıklar) / Puana göre (tek liste, puan azalan), "Sadece okunmamışlar", "Grupları sırala".
  Grup başlığına 1 sn basılı tutunca da sıralama modu açılır (titreşim): sadece grup adları, tutup sürükle, Vazgeç/Bitti.
  Sıra telefonda saklanır ve sunucuya (topics.yml) da kaydedilir; Konular ile aynıdır. En altta: hazırlayan model,
  maliyet (Claude rutininde "abonelik içinde"), süre ve token.
- Ara: yazdıkça telefondaki bültenlerde arar; solda "Araştır" (research.yml, ort. 3-4 dk, ~0,005 dolar), sağda küçük "!"
  (açıklama), altında tam genişlik "Geçmiş bültenler" butonu. Geçmiş sayfasında tarihe göre liste ve "Tarih seç"
  (sadece bülteni olan günler seçilebilir, güne dokununca o bülten Akış'ta açılır). Araştırma detayının altında model, token, maliyet.
- Konular: değişiklikler taslak (TopicsDraft, sekme değişince kaybolmaz), alttaki sabit çubukta Kaydet. "Yeni konu ekle"
  kartının altında küçük "+ Yeni grup" (yeni grup en üste eklenir). Grup başlığında "⋮" menü (uzun basınca da): Adını
  değiştir, Yukarı/Aşağı taşı, Sil. Konu "⋮" menüsü: Adını değiştir, Yukarı/Aşağı taşı (gruptaysa), "Başka gruba taşı" (alt
  menü: gruplar + Gruptan çıkar), Kaldır (onay sorar).
  Hızlı kaydetme: Kaydet'e basınca değişiklik ekranda hemen uygulanır; arka planda iş akışı durumu (GitHub runs API,
  3 sn) ve site (her istekte taze adres, 5 sn) beklenir. Alt çubuk "Kaydediliyor..." sonra "Kaydedildi". İş akışı
  başarısız olursa değişiklik geri alınır ve hata yazılır.
- Ayarlar (sekme): tema, bildirim, GitHub anahtarı (kullanıcı girdi, çalışıyor), saklama süresi (telefonda, gün; boş = sınırsız),
  "Bülteni şimdi hazırla" (tarih seçici: bir gün seç veya "Bugün"; iki onay; DeepSeek, ücretli).
- Bildirim: JobScheduler, yaklaşık 30 dakikada bir. 20:45 öncesi dünkü, sonrası bugünkü bülteni bekler; o bülten
  görüldü/bildirildiyse internete çıkmaz; 01:00-07:00 arası bakmaz. Firebase ertelendi (kullanıcı "lazım olursa").
- Simge: gazete okuyan baykuş (gece mavisi zemin).
- Derleme: android klasöründe `$env:JAVA_HOME="$env:ProgramFiles\Android\Android Studio\jbr"; .\gradlew.bat assembleRelease`
  (release, R8 ile optimize, debug anahtarıyla imzalı; debug sürüm Compose'da takılıyordu. `packageScope` ayarı çöktürdü, kaldırıldı).
- Telefon: OnePlus 12R (adb seri c34465ef). Kullanıcı kabloyu takınca:
  `adb -s c34465ef install -r app/build/outputs/apk/release/app-release.apk`. Kullanıcı Play Protect'te "her zaman gönder"
  seçti, artık sormuyor; yine de yükledikten sonra `dumpsys window | grep mFocusedApp` ile PlayProtectDialogsActivity kontrol et.
- Emülatör: `Medium_Phone_API_37.0` (İngilizce). İki cihaz bağlıyken `-s` ver.

## İş listesi (25 Kasım istekleri) - yaptıkça [x] işaretle
Kararlar: alt çubuk = Akış, Ara, Konular, Ayarlar. Geçmiş bültenler Ara sayfasında: "Araştır" kutunun altında solda,
"!" daha küçük ve sağda; altında tam genişlik "Geçmiş bültenler" butonu -> Geçmiş sayfası (tarihe göre sıralı liste +
tarih seçici; tarihe dokununca o günün bülteni açılır). Kullanıcı cevaplamadı, varsayılan alındı: sayfa adı "Akış",
uzun basma 1 sn + titreşim (kullanıcı ilk mesajda "mesela 3 sn" dedi), grup sırası tek (ana sayfa = Konular, sunucuda).

Sunucu tarafı (Python):
- [x] 1. Tekrar önleme sıkılaştırılacak: son 5 günün TÜM başlıkları (sadece aynı konu değil, ilgili konular da;
      örn. Arda Güler ve Real Madrid aynı olayı verebiliyor) seçim adımına verilsin. Seçimden sonra ayrı bir kontrol
      adımı: yeni haber önceki 5 günden biriyle aynı olaysa ve gerçekten yeni gelişme yoksa çıkarılsın. Şu an:
      history.json 7 gün tutuyor ama modele konu başına son 25 başlık gidiyor ve karar tamamen seçim modelinde.
- [x] 11. Seviye sınırı esnesin: Az/Orta/Çok en fazla 3/5/8 haber ama 9-10 puanlı haberler sınıra takılmasın
      (5 tane 9 puan varsa 5'i de gelsin). daily.py ve staged.py ikisinde de.

Uygulama (Android):
- [x] 2. Okunan haberler soluk görünsün (okundu bilgisi telefonda, haber kimliğiyle).
- [x] 3. Ana sayfa yenilensin: "önem 9/10" rozeti ve baloncuklu kart görünümü (Tüm haberler'deki gibi), sadece başlık,
      içerikten 1-2 cümle yok. Sayfanın adı "Ana sayfa" değil: "Akış" / "Özet" / "Haberler" (karar: ...).
- [x] 4. Tüm haberler sekmesi silinecek, yerine Ayarlar. Geçmiş bültenler Ara sayfasından (yukarıdaki kararlar).
- [x] 5. Konular kaydetme yavaşlığı: (ölçüldü) iş akışı ~20 sn, GitHub Pages yayını ~20-25 sn, uygulama 20 sn'de bir
      bakıyor ve adres dakikalık önbellek anahtarı kullanıyor (aynı dakika içinde eski veri). Çözüm: kaydedince
      değişiklik ekranda hemen uygulanmış görünsün (iyimser güncelleme), arka planda sunucu onayı 5 sn aralıkla ve
      her istekte yeni önbellek anahtarıyla beklensin.
- [x] 6. Konular: grup ve konular üç noktadan yukarı/aşağı taşınabilsin (sıralama). "Yeni grup" en üste,
      "Yeni konu ekle"nin hemen altına, daha küçük.
- [x] 7. Konu menüsü: her grup için ayrı "şu gruba taşı" satırı yerine tek "Başka gruba taşı" (ok simgeli),
      tıklayınca grup listesi açılsın. Gruba uzun basınca taşıma / yer değiştirme.
- [x] 8. Ana sayfada gruba uzun basınca sıralama modu: sadece grup adları görünür, sürükleyip sıralanır.
      Sıra telefonda saklanır (ve/veya Konular'daki grup sırasıyla aynı olur, karar: ...).
- [x] 9. Ana sayfanın en altında: hangi modelle hazırlandı, maliyet (stats.model, stats.cost_usd; Claude rutininde
      maliyet yerine "abonelik" yazılır).
- [x] 10. Hamburger menü (üç çizgi) her sayfadan kalkacak. Ana sayfada sağ üstte küçük sıralama/filtre simgesi
      (varsayılan: grup başlıkları altında; "puana göre": en yüksek puan en üstte) ve yanında yenile simgesi.
- [x] 12. Bu liste.

### Durum (25 Kasım)
Hepsi kodlandı, `assembleRelease` başarılı, Python içe aktarma (`bulten.daily`, `bulten.staged`) tamam.
Commit/push YOK, telefona yüklenmedi, cihazda denenmedi (kullanıcı onayı bekleniyor).
- Python: `daily.py` DEDUPE_SYSTEM "repeats" de döndürür; `recent_history`, `previous_headlines`, `needs_dedupe`,
  `dedupe_listing`, `apply_dedupe`; `keep_stories`: puan >= 9 ise sınır max_stories + 5; `update_history` aynı günü
  yeniden yazınca eskisini siler. `staged.py` aynı fonksiyonları kullanır (state'e "prev"). config.yaml `repeat_days: 5`.
  Push öncesi: `.venv\Scripts\python.exe -c "import bulten.daily, bulten.staged"`.
- Android: yukarıdaki "Android uygulaması" bölümü yeni yapıyı anlatır. Cihazda bakılacaklar: grup sürükleyerek sıralama
  hissi, 1 sn uzun basma, tarih seçici, hızlı kaydetme süreleri (Kaydediliyor -> Kaydedildi), okundu soluklaşma.
- Sıradaki: kullanıcıdan onay alınca commit + push, telefon takılınca adb ile yükleme.
- Kullanıcı 9-10 puan kuralını onayladı: sınır + en fazla 5 ek (mevcut `HIGH_EXTRA = 5`), değişiklik gerekmez.

### F. Aynı günü yeniden çalıştırınca BİRLEŞTİR (yeni istek, 25 Kasım)
Amaç: bir günün bülteni varken o gün tekrar çalıştırılırsa (elle DeepSeek veya rutin) bülten değiştirilmez,
sadece eksik/yeni haberler eklenir. Örn. 21:00 rutini + 23:00'te elle çalıştırma: 23:00 olayı da görünür. Günde tek bülten.
Hem `daily.run_daily` hem `staged` (prep + son aşama) aynı ortak fonksiyonları kullanmalı. `--compare` modunda birleştirme YOK.
1. Hazırlık: `existing = read_json(DATA/"bulletins"/f"{date}.json")` (yoksa None).
   - existing'in tüm kaynak adresleri (`S.norm_url`) `cited` kümesine eklenir (aynı haberler aday olmasın).
   - existing'in başlıkları "zaten bugünkü bültende" olarak tekrar kontrolüne girer: `previous_headlines` listesinin
     başına `[date, başlık adı, headline]` olarak eklenir; seçim isteminde konu başına `prev_headlines`'a da eklenir.
     DEDUPE_SYSTEM'e bir cümle: "Headlines dated today are already in today's bulletin; the same repeat rule applies."
2. Sınır: `keep_stories(topics, sel, existing_counts)`: konu sınırı = sınır - mevcut haber sayısı (9-10 için +5 kuralı
   mevcut sayı dahil toplamda geçerli: toplam en fazla max_stories + 5).
3. Birleştirme: yeni ortak fonksiyon `merge_bulletin(old: dict, new: dict) -> dict` (daily.py):
   - Konu başına: eski haberler AYNEN kalır (sid değişmez: uygulamadaki okundu bilgisi "tarih/sid" ile tutuluyor);
     yeni haberlere sid = `{konu}-{eski en büyük numara + 1}`, `+2`, ... verilir ve `"added_at": new["generated_at"]` eklenir.
     Liste önem puanına göre sıralanır. Yeni konu (eskide yoksa) olduğu gibi eklenir. Konu notu: haber varsa "".
   - `generated_at` eskisi kalır, `updated_at` = yeni çalıştırmanın zamanı. `markets`/`market_alarms` yeniden alınır.
   - `stats`: `runs` listesi tutulur (her çalıştırma: at, model, cost_usd varsa, items); üst düzey `cost_usd` toplam,
     `model` son çalıştırmanınki, `items` toplam, token toplam.
   - Sonra `update_history(merged["topics"], date)` (zaten o günü silip yeniden yazıyor, doğru).
4. Telegram (`telegram.py bulletin_messages`, publish.yml): bültende `updated_at` varsa ve `added_at == updated_at`
   olan haber varsa sadece onları "Güncelleme (HH:MM): N yeni haber" başlığıyla gönder; yoksa hiçbir şey gönderme.
   Kodu okuyup publish.yml'in nasıl çağırdığını anla; .yml değişirse yaml.safe_load kontrolü (KURAL yukarıda).
5. Uygulama: `Model.kt` Bulletin'e `updatedAt`, Story'ye `addedAt`. Akış başlığında "Hazırlandı ..." altına
   "Güncellendi HH:MM" (varsa). `addedAt` dolu ve okunmamış haberde kartta küçük `Tag("Yeni", highlight = true)`.
   StatsFooter: birden çok çalıştırma varsa "2 çalıştırma, toplam maliyet ..." ve modeller. Ayarlar > Bülteni şimdi
   hazırla onay metni: "Bu günün bülteni zaten var; sadece yeni haberler eklenir."
6. Test (ücretli çalıştırma YOK): `merge_bulletin` ve `keep_stories(existing_counts)` için sahte verilerle çevrimdışı
   Python denemesi (sid'ler korunuyor mu, added_at, toplam maliyet, sınır). `assembleRelease`. Sonra commit; push
   için kullanıcıya sor.

### Öneriler (kullanıcıya soruldu, cevap bekliyor)
- Tekrar yerine "Gelişme" etiketi: önceki günün devamı olan haber gizlenmek yerine "dünkü haberin devamı" rozetiyle.
- Haberde "ilgimi çekmedi" geri bildirimi: konu kriterine eklenir, seçim zamanla öğrenir.
- Akış'ta "Hepsini okundu say", yazı boyutu, ana ekran widget'ı.

## Sıradaki işler (eski)
1. Kullanıcı Salı (24 Kasım) bültenini Ayarlar > Bülteni şimdi hazırla ile elle çalıştıracak (yeni sürüm telefonda).
   Sonucu kontrol et: tarih 2026-11-24, haberler o günden mi, yeni başlıklar (Barcelona, Milli Takım, Türkiye Siyaseti,
   ayrı futbol başlıkları) ve `also` etiketi düzgün mü.
2. 25 Kasım akşamı 21:00: ilk akşam rutin bülteni, publish.yml ve telefona bildirim geldi mi kontrol et
   (`RemoteTrigger list_runs`, `gh run list`).
3. Öneriler (kullanıcı henüz istemedi): okundu işareti, kaydetme (şimdilik istemiyor), yazı boyutu, widget.
4. Uzun vade: Play Store, çok kullanıcı: hazır konular merkezde "Çok" seviyesinde üretilir, uygulama seviyeye göre
   süzer; kişiye özel konular ücretli abonelik olabilir (sonra konuşulacak).

## Geçmiş özet (ayrıntı git geçmişinde)
- 24 Kasım 02:58'de hazırlanan bülten aslında pazartesinin haberleriydi, 23 Kasım olarak yeniden adlandırıldı.
- Haiku/Sonnet/DeepSeek karşılaştırması: puanlar DeepSeek 7,5, Sonnet 7, Haiku 5,5. Model denemeleri sitede.
- DeepSeek bülten maliyeti ~0,08 dolar, araştırma ~0,005 dolar. Pro kotası: bir Sonnet bülteni ~1 puan haftalık.
