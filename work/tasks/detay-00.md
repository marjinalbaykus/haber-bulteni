# Görev: detay-00

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-00.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You write the detailed Turkish version of ONE news story for a personal bulletin.
You get the one-line headline, the related news items (titles and snippets) and, when available,
extracted article texts that may be in English, Spanish, Chinese or Turkish.

Rules:
- Write natural, fluent Turkish. Translate foreign material.
- Use only facts in the provided material. If the material is thin, write a shorter text.
  Never invent numbers, quotes, dates or names.
- title: Turkish title, at most about 12 words.
- headline: the one-line Turkish bullet (at most about 25 words). Improve it if the articles give better
  facts, otherwise keep it.
- detail: 2-4 short paragraphs separated by a blank line. What happened, key numbers, context, and why
  it matters for this reader (topic criteria given).
- key_points: 2-5 short Turkish bullet points.
- Refer to sources by publisher name if needed, never by bracketed item numbers like [3].

Output JSON: {"title": str, "headline": str, "detail": str, "key_points": [str]}

## GİRDİ

TOPIC: Real Madrid ve Arda Güler

CRITERIA:
Kullanıcı Real Madrid taraftarı ve özellikle Arda Güler'i takip ediyor.
Önemli: Arda Güler'in gol/asist/maç performansı, ilk 11'de oynayıp oynamadığı, sakatlık,
sözleşme veya transfer gelişmeleri, teknik direktörün onunla ilgili net kararları ve açıklamaları.
Real Madrid için: maç sonuçları (özellikle El Clasico, Şampiyonlar Ligi), büyük transferler,
teknik direktör değişikliği, ciddi sakatlıklar, kupa/şampiyonluk gelişmeleri.
Önemsiz: dedikodu niteliğinde "iddia" haberleri, aynı maçın onlarca ön izleme haberi,
tek bir taraftar yorumundan haber çıkarma, Arda Güler'in adı sadece geçtiği alakasız haberler.

HEADLINE: Mourinho, PSV maçı öncesi Arda Güler'i dinlendirme kararı aldığı yönünde haberler çıktı; OkDiario'ya göre Madrid, Vinicius ile başlayan ilk 11'le sahaya çıkıyor.

RELATED ITEMS:
[114] 2026-11-23 13:26 | STAR (tr) | Mourinho'dan Arda Güler kararı! Real Madrid'de tüm hesaplar değişti -- Avrupa futbolunun kulüpler düzeyindeki 1 numaralı organizasyonu olan Şampiyonlar Ligi'nde sezonun 5. haftası yarın başlayacak. Devler Ligi'nde 4'te 4 yapan Real Madrid, PSV'yi konuk edeceği maçta seri ...
[112] 2026-11-23 12:53 | Milliyet (tr) | Real Madrid-PSV Eindhoven Şampiyonlar Ligi maçı bu akşam saat kaçta hangi kanalda? Arda Güler ilk 11'de başlayacak mı? -- Şampiyonlar Ligi 5. hafta maçında milli oyuncumuz Arda Güler'in formasını giydiği Real Madrid sahasında PSV Eindhoven'ı konuk edecek. Geride kalan 4 maçta 4 galibiyet elde eden Eflatun Beyazlılar, PSV ...
[119] 2026-11-23 08:47 | Milli Gazete (tr) | Arda Güler'e kötü haber! Mourinho'dan yeni karar -- LaLiga'da Celta Vigo karşısında aldığı galibiyetle moral bulan Real Madrid'de gözler Şampiyonlar Ligi'ndeki PSV karşılaşmasına çevrildi. Avrupa ...
[116] 2026-11-23 07:36 | Haberler.com on MSN (tr) | Mourinho yine ortalığı karıştırdı! Arda'ya büyük şok -- Real Madrid'de forma giyen milli futbolcu Arda Güler için Şampiyonlar Ligi'ndeki PSV karşılaşması öncesinde dikkat çeken bir gelişme yaşandı. Celta Vigo maçının ardından Arda Güler ve Jude Bellingham' ...
[790] 2026-11-23 18:36 | KARAR (tr) | Arda Güler için sürpriz karar
[801] 2026-11-23 17:38 | kayserihaber.com (tr) | İspanyollar Jose Mourinho’nun Arda Güler kararını konuşuyor! PSV maçı öncesi belli oldu
[90] 2026-11-23 15:16 | Haberler.com (en) | Mourinho announced his decision on Arda Güler
[91] 2026-11-23 12:50 | Haberler.com (en) | Bad news for Arda Güler! Mourinho is doing what he said.
[772] 2026-11-23 15:14 | Haber 7 (tr) | Mourinho maç sonu açıklamıştı! Arda Güler için kararını verdi
[802] 2026-11-23 15:06 | Haberler (tr) | Mourinho, Arda Güler için kararını duyurdu
[803] 2026-11-23 13:39 | SuperHaber (tr) | 5 yıllık sözleşmesi bulunuyordu: Real Madrid'de Mourinho'dan Arda Güler'i kararı
[771] 2026-11-23 13:09 | Ege Saati (tr) | Arda Güler'e kötü haber! Mourinho'dan dinlendirme kararı
[761] 2026-11-23 12:43 | beinsports.com.tr (tr) | Jose Mourinho dan Arda Güler e kötü haber PSV maçında
[774] 2026-11-23 10:17 | Odatv (tr) | Mourinho'dan sürpriz Arda Güler kararı
[775] 2026-11-23 09:35 | Notbir (tr) | Jose Mourinho'dan Arda Güler kararı
[783] 2026-11-23 09:02 | Sabah (tr) | Jose Mourinho’dan Arda Güler kararı! Celta Vigo maçında sinyali vermişti...
[62] 2026-11-23 21:00 | OkDiario (es) | Alineación del Real Madrid contra el PSV: Vinicius y 10 más
[67] 2026-11-23 23:48 | Sporting News (es) | La alineación del Real Madrid: El XI para el partido vs. PSV por la Champions League
[84] 2026-11-23 20:22 | Yahoo Sports (en) | Jose Mourinho talks Guler, Bellingham, Vinicius, Comesana ahead of Real Madrid vs PSV
[93] 2026-11-23 13:28 | 101 Great Goals (en) | Jose Mourinho says Arda Guler and Jude Bellingham are two of the 'good liars' in his Real Madrid squad

ARTICLE 1 (STAR, tr):
Avrupa futbolunun kulüpler düzeyindeki 1 numaralý organizasyonu olan Þampiyonlar Ligi'nde sezonun 5. haftasý yarýn baþlayacak. Devler Ligi'nde 4'te 4 yapan Real Madrid, PSV'yi konuk edeceði maçta serisini sürdürmenin peþinde.
MOURINHO NE DEMÝÞTÝ?
Real Madrid'de hafta sonu oynanan Celta Vigo maçýnda milli futbolcu Arda Güler maça ilk 11'de baþlamýþ ve 90 dakika sahada kalmýþtý. Mücadelenin ardýndan Real Madrid Teknik Direktörü Jose Mourinho, Arda Güler ve Jude Bellingham'ýn büyük bir çaba sarf ettiðini belirtip, "Ýkinci golden sonra, takým dengesizleþti. Arda ve Jude orta sahada fiziksel olarak tükendi ama iþleri nasýl yoluna koyacaðýmýzý biliyorduk." demiþti.
PSV MAÇI ÝÇÝN KARAR
Þampiyonlar Ligi'ne yarýn Santiago Bernabeu'da PSV'yi aðýrlayacak Real Madrid'de bu karþýlaþma için Arda Güler kararý çýktý. Ýspanyol basýnýnda yer alan haberlere göre Jose Mourinho'nun Celta Vigo maçýnýn ardýndan fiziksel olarak yorulduklarýný vurguladýðý Arda Güler ve Jude Bellingham'ý PSV karþýsýnda dinlendirmesi bekleniyor. Milli yýldýz, Þampiyonlar Ligi'ndeki son AEK maçýnda da dinlendirilmiþ ve Yunanistan'a giden kafilede yer almamýþtý.
ARDA GÜLER'ÝN BU SEZONKÝ PERFORMANSI
Real Madrid formasýyla bu sezon Arda Güler iki Þampiyonlar Ligi olmak üzere 15 maçta süre aldý. Milli futbolcu bu karþýlaþmalarda 4 gol ve 7 asist kaydetti. Arda Güler, Þampiyonlar Ligi'ndeki ilk maçta kart cezasý nedeniyle forma giyememiþ, son oynanan AEK maçýnda ise kadroya dahil edilmemiþti.

ARTICLE 2 (Milliyet, tr):
24.11.2026 - 12:31 | Son Güncellenme:
HABER MERKEZİ
Real Madrid-PSV Eindhoven Şampiyonlar Ligi maçına yönelik araştırmalar hız kazandı. Şampiyonlar Ligi'nde 5. hafta maçında İspanyol temsilcisi Real Madrid, Santigo Bernabeu'da Hollanda temsilcisi PSV Eindhoven karşısında sahaya çıkacak. Mücadeleyi Norveçli hakem Espen Eskas yönetecek. Maçın canlı yayın kanalı ve saatine ilişkin bilgiler futbolseverlerin yakın takibinde yer almaya devam ediyor. Peki Real Madrid-PSV Eindhoven Şampiyonlar Ligi maçı ne zaman saat kaçta hangi kanalda?
REAL MADRİD-PSV EİNDHOVEN ŞAMPİYONLAR LİGİ MAÇI NE ZAMAN SAAT KAÇTA HANGİ KANALDA?
Real Madrid-PSV Eindhoven Şampiyonlar Ligi maçı 24 Kasım Salı TSİ ile 23:00'te başlayacak. Santiago Bernabeu'da oynanacak olan mücadele tabi spor ekranlarından yayınlanacak. Real Madrid-PSV Eindhoven maçını Norveçli hakem Espen Eskas yönetecek.
ARDA GÜLER YEDEK BAŞLAYABİLİR!
Son olarak La Liga'da oynanan Celta Vigo maçına ilk 11'de başlayan milli oyuncumuz Arda Güler ve Jude Bellingham hakkında açıklamalarda bulunan teknik direktör Jose Mourinho iki ismin yorulduğuna dair açıklamalarda bulunmuştu. PSV maçında Bellingham ve Arda Güler'in dinlendirilmesi bekleniyor.
MUHTEMEL 11'LER
Real Madrid: Courtois; Valverde, Militao, Rudiger, Cucurella; Tchouameni, Camavinga; Diomande, Diaz, Vinicius; Mbappe
PSV: Kovar; Dest, Geertruida, Obispo, Kostic; Sano, Fernandez; Perisic, Mijnans, Van Bommel; Til

ARTICLE 3 (Milli Gazete, tr):
Celta Vigo mücadelesinde 90 dakika sahada kalan Arda Güler'in PSV karşısında yedek kulübesinde oturabileceği öne sürüldü. İspanyol basınında yer alan haberlere göre Mourinho, yoğun maç temposu nedeniyle milli futbolcuyu dinlendirmeyi planlıyor.
Mourinho'dan Arda Güler ve Bellingham açıklaması
Real Madrid'in Celta Vigo ile oynadığı karşılaşmada Arda Güler ilk 11'de görev aldı. Mücadelenin başından sonuna kadar sahada kalan milli futbolcu, orta sahada Jude Bellingham ile birlikte takımın hücum organizasyonlarında sorumluluk üstlendi.
Karşılaşmanın ardından açıklamalarda bulunan Jose Mourinho, iki oyuncunun da fiziksel açıdan yoğun efor sarf ettiğini söyledi. Takımın ikinci golün ardından oyun kontrolünü kaybettiğini belirten Portekizli teknik adam, Arda Güler ve Bellingham'ın orta sahada fiziksel olarak yorulduğunu ifade etti. Mourinho, açıklamasında, "Arda ve Jude orta sahada fiziksel olarak tükendi ama işleri nasıl yoluna koyacağımızı biliyorduk." sözlerini kullandı.
Arda Güler PSV karşısında yedek mi kalacak?
İspanyol basınında çıkan haberlere göre Mourinho, Celta Vigo karşılaşmasının ardından fiziksel olarak yıpranan Arda Güler ve Jude Bellingham'ı PSV maçında dinlendirmeyi düşünüyor. İki futbolcunun da karşılaşmaya ilk 11'de başlamaması ve mücadeleye yedek kulübesinde başlaması bekleniyor.
Arda Güler açısından Şampiyonlar Ligi'nde üst üste yaşanan gelişmeler de dikkat çekiyor. Milli futbolcu, Real Madrid'in Avrupa arenasındaki son AEK karşılaşmasında kadroda yer almamıştı. Yunanistan deplasmanına giden kafileye dahil edilmeyen Arda, PSV mücadelesinde de ilk 11 dışında kalma ihtimaliyle gündeme geldi.
Arda Güler'in bu sezonki istatistikleri
Real Madrid formasıyla başarılı bir sezon geçiren Arda Güler, şu ana kadar toplam 15 karşılaşmada görev yaptı. Milli futbolcu, bu maçlarda 4 gol ve 7 asist üreterek takımının skor katkısında önemli rol oynadı.
Şampiyonlar Ligi'nde ise iki karşılaşmada forma giyen Arda Güler, Avrupa'daki ilk maçını kart cezası nedeniyle kaçırmıştı. Son AEK mücadelesinde de kadroda bulunmayan genç oyuncunun PSV karşılaşmasındaki durumu merak konusu oldu.
Mourinho'nun yoğun fikstür nedeniyle Arda Güler ve Bellingham'ı dinlendirme ihtimali, Real Madrid'in PSV karşısındaki muhtemel kadro tercihlerini gündeme taşıdı. Milli futbolcunun mücadelede süre alıp almayacağı ise teknik heyetin son kararına bağlı olacak.
