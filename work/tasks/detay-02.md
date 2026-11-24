# Görev: detay-02

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-02.json`
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

HEADLINE: Endrick yeniden sakatlandı ve yaklaşık üç hafta yok; PSV maçında forma giyemeyecek.

RELATED ITEMS:
[64] 2026-11-23 19:49 | Diario de México (es) | Endrick será baja en el Real Madrid para el partido de Champions League
[86] 2026-11-23 15:05 | Mundo Deportivo (en) | Endrick ruled out for three weeks
[244] 2026-11-23 13:44 | Yahoo Sports (en) | Real Madrid blow as Endrick suffers fresh injury setback ahead of Champions League clash
[229] 2026-11-23 11:30 | Real Madrid CF | Web Oficial (en) | Medical Report: Endrick
[207] 2026-11-23 20:30 | Sports Illustrated (en) | Real Madrid Rocked by Double Injury Blow Ahead of Crucial Champions League Push
[491] 2026-11-23 19:36 | DAZN (es) | Fabrizio Romano detalla en DAZN el futuro de Endrick en el Real Madrid: "Mourinho no quiere cambiar ni un jugador..."

ARTICLE 1 (Sportstar, en):
Endrick Felipe, the Real Madrid forward, has been sidelined by an injury to his right thigh, the club confirmed in a statement on Monday.
According to reports, the Brazilian is expected to be out for between two and three weeks.
The absence comes just ahead of Real Madrid’s Champions League game against PSV Eindhoven on Tuesday.
ALSO READ: Barcelona’s Lamine Yamal to miss Champions League match against Sabah
Endrick is also in danger of missing the matches against Alaves, Real Sociedad, Athletic Bilbao, Arsenal and Osasuna.
He had been a substitute in each of Real Madrid’s last four games, and this season he has registered one goal and one assist from eight appearances.
The club is also without Ferland Mendy, Raul Asencio and Rodrygo Goes, all of whom are injured.
(With inputs from REUTERS)
Published on Nov 23, 2026
