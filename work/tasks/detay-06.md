# Görev: detay-06

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-06.json`
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

TOPIC: Yapay Zeka

CRITERIA:
Önemli: büyük yapay zeka modellerinin çıkışı (OpenAI, Google Gemini, Anthropic Claude, xAI, Meta,
DeepSeek, Qwen ve diğer Çinli laboratuvarlar), benchmark rekorları ve somut puanlar,
sektörü etkileyen büyük şirket hamleleri ve satın almalar, önemli düzenlemeler ve yasaklar,
çığır açan araştırmalar.
Haberde model puan aldıysa puanı ve hangi testte aldığını yaz.
Ayrıca sektörde neler olduğunu ve somut ilerlemeleri göster: yeni yetenekler (akıl yürütme, ajanlar,
görüntü/video/ses üretimi), model maliyetlerinde ve verimlilikte büyük düşüşler, büyük yatırımlar ve
veri merkezi hamleleri, laboratuvarlar arası rekabetteki önemli değişimler.
Önemsiz: sıradan ürün özellik güncellemeleri, köşe yazıları, "yapay zeka işimizi alacak mı" yorumları,
küçük girişim yatırımları.
Bu başlığa girmesin (ayrı başlıkları var): yerel/açık ağırlıklı modellerin bilgisayarda çalıştırılması
("Yerel LLM"); kodlama araçları, abonelik planları, kota ve API fiyat değişiklikleri
("Yapay Zeka Kodlama Araçları ve Fiyatlar"). Bunlara burada ancak sektörü sarsacak kadar büyükse yer ver.

HEADLINE: Google Gemini 4 Neon ve OpenAI GPT-6.2 Astra/Sol modelleri iş verimliliğine odaklanarak çıktı; Google ayrıca yarı fiyatlı Gemini 3.7 Flash'ı duyurdu.

RELATED ITEMS:
[544] 2026-11-23 22:02 | SoaplandTV (en) | Google's Gemini 4 Neon Takes on Bigger AI Jobs but Is It Actually Cheaper?
[500] 2026-11-23 13:15 | TechTarget (en) | Model Release: Gemini 4 Neon, GPT-6.2-Astra-Sol target business productivity
[512] 2026-11-23 20:00 | Search Engine Journal (en) | I Blind-Tested 3 Gemini Models On Real SEO Questions – Neon Won 5 Of 6
[538] 2026-11-23 18:02 | آي-فون إسلام (en) | Google Launches Gemini 3.7 Flash: Super-Fast Coding and Half the Price
[525] 2026-11-23 13:45 | dev.ua (en) | OpenAI has released a guide to prompting and configuring the API for GPT-6.2 Astra and Sol models. What's changed for developers
[503] 2026-11-23 11:32 | shattered.io (en) | GPT-6.2 Astra vs Fable 5.2: 62 vs 65, $4 vs $26.54
