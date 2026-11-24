# Görev: detay-12

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-12.json`
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

HEADLINE: Z.ai, döngüsel 'loop transformer' mimarisine sahip GLM-5.4 modelini tanıttı; şirket modelin 4 kat büyük modeller kadar düşünebildiğini iddia ediyor.

RELATED ITEMS:
[578] 2026-11-23 14:32 | Wccftech (en) | Z.ai Debuts GLM-5.4 With a Novel Looped Transformer Architecture, Skipping AI Benchmarks Entirely
[554] 2026-11-23 13:45 | 东方财富网 (zh) | 智谱发布GLM-5.4旗舰模型
[559] 2026-11-23 11:18 | 东方财富网 (zh) | 智谱正式发布新一代大模型GLM-5.4
[497] 2026-11-23 15:13 | The Decoder (en) | Z.ai follows OpenAI into loop transformer territory with its new 750B flagship model
[498] 2026-11-23 23:06 | Decrypt (en) | China's Z.ai Says Its New AI Model Can Think Like One Four Times Its Size
[163] 2026-11-23 11:10 | www.anfangnews.com (zh) | 智谱GLM-5.4大模型发布：仍是开源最强、比Opus 5.5差距5%内_人工智能
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[556] 2026-11-23 23:17 | Sohu (zh) | 智谱发布新一代大模型
[557] 2026-11-23 15:26 | 证券之星 (zh) | 智谱 重大发布！-证券之星
