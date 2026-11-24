# Görev: detay-09

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-09.json`
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

HEADLINE: Amazon, Trainium çiplerini satışa hazırlarken Decart'ın yapay zeka optimizasyon teknolojisini ve araştırma ekibini yaklaşık 7,5 milyar dolara satın alıyor.

RELATED ITEMS:
[912] 2026-11-23 17:14 | TechCrunch (en) | Amazon snaps up Decart’s AI optimization tech for $7.5B as it prepares to sell Trainium chips -- Amazon is acquiring Decart’s AI optimization technology and research team in a reported $7.5 billion deal.
[653] 2026-11-23 17:50 | Anadolu Ajansı (tr) | Amazon, Decart'ın yapay zeka optimizasyon teknolojisini satın alıyor

ARTICLE 1 (TechCrunch, en):
Amazon has agreed to acquire a good chunk of Israeli startup Decart.
The e-commerce and cloud giant is paying $7.5 billion for the startup, according to Bloomberg.
Decart has made a name for itself building interactive real-time world models — most recently Oasis 3 — and AI video-generation tools. But under the hood, the company stands out for its Decart Optimization Stack (DOS), software that helps AI models run efficiently on Nvidia, Amazon, and Google’s hardware.
As part of the deal, Amazon will acquire the technology behind DOS, as well as Decart’s founding team and team of AI researchers and engineers, Amazon said in a blog post.
“Every leap forward in AI starts with getting more out of every chip,” said Peter DeSantis, SVP of foundational AI models, custom silicon, quantum computing, at Amazon. “More efficient compute means better training, better reinforcement learning, faster inference, and better models for customers at a lower cost.”
Amazon, which has so far been using its Trainium chips at its own data centers, is preparing to start selling them to customers as a competitor to Nvidia. Decart’s technology could make it easier for customers to run their workloads on Amazon chips, rather than Nvidia’s, which has been the gold standard so far.
Decart and Amazon did not immediately return requests for comment.
