# Görev: detay-11

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-11.json`
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

HEADLINE: OpenAI, gelişmiş yapay zeka sistemleri için rakiplerin ortak sınırlar üzerinde anlaşmasını ve ölçülebilir bir Perplexity skoru kullanılmasını önerdi.

RELATED ITEMS:
[499] 2026-11-23 21:56 | PYMNTS.com (en) | OpenAI Pitches Perplexity Score to Set AI Safety Limits
[506] 2026-11-23 18:42 | The Decoder (en) | OpenAI warns that upcoming AI systems could outpace safety checks and urges rivals to agree on shared limits
[540] 2026-11-23 18:00 | De Último Minuto (en) | OpenAI scientist calls for shared limits on AI development amid growing safety concerns
[507] 2026-11-23 14:35 | The Mac Observer (en) | OpenAI Chief Scientist Wants New Limits on AI Development
[518] 2026-11-23 13:26 | Quartz (en) | OpenAI's chief scientist called for industrywide limits on AI development
[526] 2026-11-23 08:54 | ForkLog (en) | OpenAI chief scientist proposes measurable limits on advanced AI development
[505] 2026-11-23 12:20 | Mezha (en) | OpenAI Proposes Limiting AI Development Until Companies Learn to Control It
[529] 2026-11-23 10:09 | Dawan Africa (en) | OpenAI Urges Common Limits on AI Development
[543] 2026-11-23 10:06 | وكالة سوا الإخبارية (en) | OpenAI Chief Scientist Reveals Model Progress Metrics, Urges Shared Safety Thresholds
[539] 2026-11-23 09:53 | Gadgets 360 (en) | OpenAI AI Scientist Argues for Establishing the Perplexity Metric to Evaluate the Performance of AI Model...
[522] 2026-11-23 09:41 | Tech Times (en) | OpenAI's Jakub Pachocki Proposes AI Scaling Limits Scored on Private Test Only OpenAI Can Run
[549] 2026-11-23 12:26 | Techgenyz (en) | OpenAI Proposes AI Safety Limits: Can Perplexity Work?

ARTICLE 1 (PYMNTS.com, en):
Artificial intelligence developers often learn what a new model can do only after they have built it. OpenAI Chief Scientist Jakub Pachocki wants the industry to watch one metric that he says tracks how intelligent a model is, and to agree on limits tied to that metric until safety tools catch up.
	
The metric is perplexity (not the AI search company of the same name), which measures how well a model predicts the next token, or piece of text, on average. A lower score means better predictions.
A large language model learns by reading huge amounts of text, split into tokens that are words or pieces of words, and guessing which token comes next. Perplexity measures how good those guesses are on text the model was not trained on.
In a Sunday (Nov. 22) blog post titled “Perplexity and Intelligence,” Pachocki proposed that AI developers use perplexity to compare progress across companies and to set shared safety standards.
The post also calls for common limits that developers commit not to cross until researchers can better understand and monitor AI behavior, PYMNTS reported. OpenAI CEO Sam Altman wrote on X that the post is “the most important thing OpenAI has published in a while.”
OpenAI’s Perplexity Score Fell as Its Models Gained Skills
GPT-3.5, which powered the original ChatGPT, had a log-perplexity score of about 0.99. GPT-4 reached about 0.91, alongside improvements in reasoning capabilities. More recent models have brought that measurement below 0.8, coinciding with advances in mathematics and cybersecurity.
Pachocki wrote that a model at about 0.76 produced “groundbreaking results” on open math problems. OpenAI has posed such problems to its models as a test for more than a year, he wrote.
On Sept. 8, OpenAI said an unreleased model solved the Navier-Stokes problem on fluid flow, one of seven Millennium Prize Problems. The Clay Mathematics Institute chose the seven in 2000 to record some of the hardest problems of the time and put a $1 million prize on each. Only one, the Poincaré conjecture, had been solved before. About 10,000 AI agents worked for 88 hours on this one, Quanta Magazine reported.
Another of the seven may also have fallen to AI. Anthropic researcher Levent Alpöge claimed Friday (Nov. 20) that he used Claude to prove the Riemann hypothesis, SiliconANGLE reported. It is the oldest of the seven problems, formulated in 1859 to explain how prime numbers are spread along the number line, the Clay Mathematics Institute said. Anthropic had not published an official announcement as of Monday (Nov. 23).
Pachocki expects “further transformative capabilities” as scores approach the 0.68 to 0.7 range, although the specific capabilities that might emerge remain unknown.
OpenAI, Google DeepMind and Microsoft Point to the Score’s Limits
OpenAI’s perplexity figures come from its internal code dataset, and developers use different data and methods, so scores are hard to compare across companies, Pachocki wrote. Techniques that let models spend more time reasoning can improve performance without lowering the score, he added.
Outside researchers have pointed to limits beyond the ones Pachocki named. Google DeepMind researchers argued in a January paper, “Perplexity Cannot Always Tell Right From Wrong,” that a low perplexity score does not prove a model is right. A model can give a wrong answer with full confidence and still score well, they wrote. The better score does not always belong to the more accurate model.
Scores from different labs also cannot be compared directly, because each lab splits text into tokens its own way and perplexity is counted per token. Microsoft AI CEO Mustafa Suleyman backed OpenAI’s effort on X but also wrote that comparing labs may require a different unit because “different labs use different tokenizers.”
Why OpenAI Wants Limits Before 2027
AI could contribute to breakthroughs in the algorithms used to train new models as early as 2027, Pachocki wrote. Such advances could outpace the methods researchers use to monitor models.
OpenAI has stopped once already. The company suspended training on its Astra model after determining Aug. 7 that the model may have crossed the “Critical” cybersecurity threshold in its Preparedness Framework, PYMNTS reported in August.
Pachocki didn’t specify what the first limit should be or when it might take effect.
OpenAI isn’t the only lab asking for brakes. In his September essay “We Must Pace the Frontier,” Anthropic CEO Dario Amodei called for independent evaluators inside AI companies, coordination among companies in democratic countries and an eventual international agreement that includes China, PYMNTS reported.
Perplexity-based development limits remain a proposal. Pachocki’s post names no other developer that has agreed to one.
For all PYMNTS AI coverage, subscribe to the daily AI Newsletter.
