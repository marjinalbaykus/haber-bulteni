# Görev: secim-ekonomi_tr

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-ekonomi_tr.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You are the editor of a personal daily news bulletin for a Turkish reader.
You receive candidate news items for ONE topic from roughly the last 30 hours, in several languages
(Turkish, English, Spanish, Chinese). Pick only the genuinely important stories according to the topic
criteria, merge items that cover the same event, and write a one-line Turkish bullet for each.

Rules:
- Use ONLY the given items. Never add facts that are not in the titles or snippets.
- Merge items about the same event into one story and list all of their ids.
- Score importance 1-10 for THIS reader using the criteria: 10 = historic, 8 = big news, 7 = clearly worth
  knowing today, 5 = minor, 3 = noise. Be strict; most items are noise.
- Importance signals: the same event covered by many independent sources, Hacker News points,
  Hugging Face trend scores. A single clickbait or rumor source is weak evidence.
- Skip stories that were already reported on previous days (list given) unless there is a significant
  new development; in that case the headline must state what is new.
- If market data is given and a threshold is exceeded, treat that move as a story candidate even if no
  news item explains it (use ids: []).
- headline: Turkish, one sentence, at most about 25 words, concrete (who did what, numbers if present).
  No clickbait, no emojis.
- Return at most 8 stories, most important first. An empty list is fine and expected on quiet days.

Output JSON: {"stories": [{"ids": [int], "importance": int, "headline": str, "reason": str}]}
"reason" is a short Turkish note on why it matters.

## GİRDİ

TOPIC: Ekonomi - Türkiye

CRITERIA:
Çıta çok yüksek. Sadece hayatı veya piyasaları ciddi etkileyen olaylar girsin.
Önemli: TCMB'nin beklenmedik veya büyük faiz kararları, kurda sert hareketler, enflasyonda büyük sürpriz,
büyük vergi/düzenleme değişiklikleri, kredi notu değişiklikleri, ekonomiyi sarsan siyasi gelişmeler.
Önemsiz: beklentiye uygun olağan kararlar, küçük yüzdelik kur/altın/borsa hareketleri,
yorumcu tahminleri, sıradan şirket haberleri.
Piyasa verisinde eşik aşılmışsa bunu mutlaka değerlendir.

MARKET DATA (thresholds marked):
- Gram altın (TL): 5883.51 (1 günde %-0.9, 7 günde %-6.1, 30 günde %-8.2)
- Dolar/TL: 50.62 (1 günde %+0.1, 7 günde %+0.4, 30 günde %+1.8)
- Euro/TL: 55.32 (1 günde %-0.4, 7 günde %-0.3, 30 günde %-0.9)
- BIST 100: 11073.13 (1 günde %-0.3, 7 günde %-5.0, 30 günde %-0.4)

CANDIDATE ITEMS:
[1030] 2026-11-23 21:57 | Vietnam.vn (tr) | Aralık 2026'daki faiz artışı enflasyonu dizginlemek için yeterli olmayabilir.
[1009] 2026-11-23 13:32 | Foreks.com (tr) | UBS: Altında 3.500 dolar desteği kritik, Fed baskısı 2027'nin ilk çeyreğine uzanabilir
[1333] 2026-11-23 16:23 | Independent Türkçe (tr) | Cumhurbaşkanı Erdoğan’dan ekonomi mesajı: 2027 bütçesi halkın ihtiyaçlarına göre hazırlandı
[1058] 2026-11-23 19:42 | Haberet (tr) | TCMB Başkanı Karahan'dan kritik mesaj: Gerektiğinde faiz ve likidite araçları devreye alınacak!
[1010] 2026-11-23 12:11 | NTV Haber (tr) | FED faiz kararı ne zaman açıklanacak? Ekonomistlerin faiz beklentisi ne yönde?
[1065] 2026-11-23 19:32 | Yeni Alanya (tr) | Kasım 2026 enflasyon rakamları ne zaman açıklanacak?
[1011] 2026-11-23 10:45 | Paratic Haber (tr) | Altın, borsa ve mevduat için tahminler ne? Güsar açıkladı
[1036] 2026-11-23 19:13 | Ege Alternatif (tr) | Hanehalkının enflasyon beklentisi yüzde 43,24'e indi
[1070] 2026-11-23 19:03 | Manisa Kulis Haber (tr) | Zam bekleyen milyonları ilgilendiriyor! Merkez Bankasından enflasyon açıklaması
[1033] 2026-11-23 18:50 | CNN Türk (tr) | Kasım ayı enflasyon beklentisi ne? TÜİK Kasım ayı enflasyon rakamları ne zaman, saat kaçta açıklanacak?
[1019] 2026-11-23 17:56 | Yeni Şafak (tr) | Merkez Bankası faiz toplantısı ne zaman? Faiz kararı saat kaçta açıklanacak? 2026 Kasım PPK toplantı takvimi
[1023] 2026-11-23 17:52 | Mağusa Haber Ajansı (tr) | MERKEZ BANKASI BAŞKANI KARAHAN’DAN ENFLASYON VE FAİZ MESAJI: GEREKİRSE YENİ ADIMLAR ATILACAK!
[1053] 2026-11-23 17:37 | 12punto (tr) | Merkez Bankası Başkanı Karahan’dan zam bekleyenleri ilgilendiren enflasyon mesajı
[1055] 2026-11-23 17:30 | Nefes Gazetesi (tr) | Zam bekleyen emekli ve çalışanı ilgilendiren açıklama. Merkez Bankası Başkanı'ndan enflasyon sözleri
[1042] 2026-11-23 16:42 | Bold Medya (tr) | Karahan’dan yeni sıkılaşma sinyali: Kemer sıkmaya ek tedbirler gündemde
[1041] 2026-11-23 16:01 | Ege Alternatif (tr) | ABD büyüme ve PCE verileri 25 Kasım'da açıklanacak
[1016] 2026-11-23 15:35 | Bigpara (tr) | TCMB FAİZ KARARI TARİHİ 2026 ARALIK (PPK TOPLANTISI) | Merkez Bankası faiz kararı ne zaman açıklanacak, hangi gün? Merkez Bankası faiz beklentisi nedir? Gözler Aralık ayı toplantısında!
[1045] 2026-11-23 14:58 | siyasalbirikim.com.tr (tr) | TCMB Kasım 2026 Enflasyon Beklentilerini ve Finansal Güven Endeksini Açıkladı
[1040] 2026-11-23 14:45 | Milliyet (tr) | Kasım ayı enflasyonu beklentisi Merkez Bankası anketi! 2026 Kasım enflasyon oranı ne zaman, saat kaçta açıklanacak?
[1039] 2026-11-23 14:27 | Üreten Ankara (tr) | Merkez Bankası Piyasa Katılımcıları Anketi belli oldu - GÜNCEL - Ankara'nın Ekonomi Sitesi
[1029] 2026-11-23 13:49 | İstanbul Ticaret Gazetesi (tr) | VADELİ MEVDUAT FAİZ ORANLARI KASIM 2026: 2 milyon TL'ye 58 bin lirayı aşan kazanç! En yüksek faiz hangi bankada?
[1052] 2026-11-23 13:39 | AKŞAM (tr) | Merkez Bankası faiz kararı ne zaman, saat kaçta açıklanacak? 2026 TCMB toplantı takvimi!
[1059] 2026-11-23 13:34 | 24 Saat Gazetesi Ankara (tr) | Vadeli mevduat faiz oranları 2026: 100 bin TL'nin 32 günlük getirisi ne kadar? Kasım 2026 banka banka faiz oranları
[1068] 2026-11-23 13:33 | Kocaeli TV (tr) | 2026 Kasım enflasyon beklentisi kaç oldu? Merkez Bankası yeni rakamları açıkladı
[1051] 2026-11-23 12:25 | Van Postası Gazetesi (tr) | İş Dünyasından Asgari Ücret İçin İki Aşamalı Artış Önerisi
[1062] 2026-11-23 12:00 | Yozgat Hakimiyet (tr) | Merkez Bankası Başkanı Karahan’dan Faiz ve Enflasyon Mesajı! TL İçin Yeni Adımlar Gündemde
[1037] 2026-11-23 11:55 | CNBC-e (tr) | TCMB rezervlerinde hızlı düşüş bekleniyor: 2023'ten sonra ilk kez olacak
[1034] 2026-11-23 11:52 | Haberton (tr) | TCMB açıkladı! Vatandaşın enflasyon beklentisi yüzde 43,24'e geriledi: Piyasanın beklentisi yükseldi
[1031] 2026-11-23 11:27 | Vietnam.vn (tr) | ABD Merkez Bankası'nın (Fed) faiz oranlarını 50 baz puan artırma olasılığının ortadan kalkmasıyla birlikte ABD doları zayıflamaya devam etti.
[1046] 2026-11-23 11:25 | https://www.ajansturkhaber.com/ (tr) | Merkez Bankası Kasım Ayı Verilerini Paylaştı Enflasyon Beklentileri Değişkenlik Gösterdi
[1057] 2026-11-23 11:10 | kayserihaber.com (tr) | Merkez Bankası Başkanı Karahan'dan faiz açıklaması: Gerektiğinde yeni adımlar atılabilir
[1024] 2026-11-23 11:07 | İstanbul Ticaret Gazetesi (tr) | TCMB Başkanı Karahan'dan faiz ve stopaj açıklaması: Gerekirse adımlar gelebilir
[1035] 2026-11-23 10:59 | CNBC-e (tr) | Yatırımcıların gözü bu tarihte: Fed'in faiz rotası değişebilir
[1056] 2026-11-23 10:59 | Yeni Ankara (tr) | Türk lirası için yeni adımlar gelebilir: Karahan seçenekleri açıkladı
[1073] 2026-11-23 10:58 | Yeni Mesaj (tr) | Merkez Bankası Başkanı Karahan'dan para politikası ve enflasyona dair önemli mesajlar verdi. Karahan, "Türk lirasına olan talebin desteklenmesinin gerekmesi durumunda, başta temel aracımız olan politika faizi olmak üzere makroihtiyati ve likidite...
[1021] 2026-11-23 10:52 | CNN Türk (tr) | Merkez Bankası Başkanı Karahan'dan enflasyon açıklaması - Son Dakika Ekonomi Haberleri | Video
[1067] 2026-11-23 10:47 | Elips Haber (tr) | Merkez Bankası Başkanı Karahan'dan enflasyon ve TL mesajı: İlave adımlar gündemde
[1026] 2026-11-23 10:39 | Anadolu Ajansı (tr) | TCMB kasım ayı "Sektörel Enflasyon Beklentileri"ni ve "Hanehalkı Beklenti Anketi"ni yayımladı
[1047] 2026-11-23 10:37 | Forbes Türkiye (tr) | TCMB Başkanı Fatih Karahan'dan enflasyon ve para politikası mesajları
[1074] 2026-11-23 10:37 | https://www.babaocagi.com/ (tr) | TCMB Başkanı Karahan’dan para politikası ve enflasyon mesajları
[1069] 2026-11-23 10:33 | 12punto (tr) | Başkan Fatih Karahan açıkladı: Merkez Bankası'ndan TL'yi destekleyecek yeni adımlar
[1044] 2026-11-23 10:31 | Yeni Şafak (tr) | Karahan'dan rezerv yönetimi ve enflasyon açıklaması | Türkiye Ekonomisi Haberleri
[1066] 2026-11-23 10:30 | Haber 7 (tr) | Merkez Bankası’ndan enflasyon mesajı! Ana eğilim yüzde 24 seviyesine geriledi
[1072] 2026-11-23 10:30 | Türkgün (tr) | Merkez Bankası Başkanı Karahan'dan TL için yeni hamle sinyali: Faiz ve stopaj dahil tüm araçlar masada
[1049] 2026-11-23 10:28 | Bölge Gündemi (tr) | Merkez Bankası anketi: Hanehalkının yatırım tercihi altın oldu
[1032] 2026-11-23 10:27 | T24 (tr) | Merkez Bankası Başkanı Fatih Karahan'dan Türk Lirası mesajı: Faiz ve stopaj dahil yeni adımlar gündeme gelebilir
[1048] 2026-11-23 10:22 | Finans Hattı (tr) | TCMB Başkanı Karahan’dan Kritik Mesaj: Yeni Faiz ve Stopaj Sinyali
[1071] 2026-11-23 10:19 | Gerçek Gündem (tr) | Merkez Bankası Başkanı Karahan'dan TL'yi Desteklemek İçin Yeni Adım Sinyali: Faiz ve Stopaj Gündemde
[1017] 2026-11-23 10:17 | Anadolu Ajansı (tr) | Merkez Bankası Başkanı Karahan'dan para politikası ve enflasyon mesajları
[1061] 2026-11-23 10:14 | Borsatek (tr) | Merkez Bankası Başkanı Fatih Karahan’dan faiz ve stopaj açıklaması
[1060] 2026-11-23 10:08 | Patronlar Dünyası (tr) | Merkez Bankası Başkanı Fatih Karahan'dan para politikası ve enflasyon mesajları
[1063] 2026-11-23 10:02 | İnternet Haber (tr) | Merkez Bankası Başkanı Fatih Karahan'dan kritik enflasyon mesajı
[1050] 2026-11-23 09:52 | Ekovitrin Haber (tr) | Hanehalkının enflasyon beklentisi yüzde 43,24’e geriledi
[1038] 2026-11-23 08:52 | Cumhuriyet (tr) | TCMB anketinde dikkat çeken sonuç: Yurttaşın enflasyon beklentisi düştü, dolar tahmini yükseldi
[1043] 2026-11-23 08:46 | Haberton (tr) | Dolar 58 TL mi olacak? Merkez Bankası’nın son anketinde kur beklentisi değişti!
