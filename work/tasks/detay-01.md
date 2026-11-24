# Görev: detay-01

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-01.json`
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

HEADLINE: Bellingham'ın Real Madrid ile uzun vadeli sözleşme yenileme görüşmeleri ileri aşamada; NYT haberi duyurunun yakında gelebileceğini bildirdi.

RELATED ITEMS:
[95] 2026-11-23 23:22 | roundtable.io (en) | Real Madrid Star Jude Bellingham Set to Commit Future to the Club
[239] 2026-11-23 18:59 | Yahoo Sports (en) | Real Madrid in advanced talks with key superstar over renewal; announcement to follow soon
[220] 2026-11-23 18:45 | The New York Times (en) | Jude Bellingham in advanced talks over Real Madrid long-term contract renewal
[94] 2026-11-23 20:17 | Get Spanish Football News (en) | Real Madrid star will earn ‘significantly’ more than €15m-per-year as part of contract renewal
