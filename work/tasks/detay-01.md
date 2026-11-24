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

HEADLINE: Real Madrid, La Liga'da Celta Vigo'yu 2-1 yenerek üst üste altıncı galibiyetini aldı.

RELATED ITEMS:
[70] 2026-11-23 18:38 | Caracol Radio (es) | Real Madrid 2-1 Celta: resultado, resumen y goles
[786] 2026-11-23 10:05 | BirGün Gazetesi (tr) | La Liga’da haftanın görünümü: Barcelona geri döndü, Real Madrid’den üst üste altıncı galibiyet
