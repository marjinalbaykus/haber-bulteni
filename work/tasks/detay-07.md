# Görev: detay-07

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-07.json`
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

HEADLINE: OpenAI baş bilim insanı Jakub Pachocki, gelişmiş sistemler güvenlik kontrollerini aşmadan önce rakiplerin ortak ve ölçülebilir geliştirme sınırlarında anlaşmasını istedi.

RELATED ITEMS:
[506] 2026-11-23 18:42 | The Decoder (en) | OpenAI warns that upcoming AI systems could outpace safety checks and urges rivals to agree on shared limits
[540] 2026-11-23 18:00 | De Último Minuto (en) | OpenAI scientist calls for shared limits on AI development amid growing safety concerns
[507] 2026-11-23 14:35 | The Mac Observer (en) | OpenAI Chief Scientist Wants New Limits on AI Development
[518] 2026-11-23 13:26 | Quartz (en) | OpenAI's chief scientist called for industrywide limits on AI development
[505] 2026-11-23 12:20 | Mezha (en) | OpenAI Proposes Limiting AI Development Until Companies Learn to Control It
[526] 2026-11-23 08:54 | ForkLog (en) | OpenAI chief scientist proposes measurable limits on advanced AI development
[529] 2026-11-23 10:09 | Dawan Africa (en) | OpenAI Urges Common Limits on AI Development
[543] 2026-11-23 10:06 | وكالة سوا الإخبارية (en) | OpenAI Chief Scientist Reveals Model Progress Metrics, Urges Shared Safety Thresholds
[539] 2026-11-23 09:53 | Gadgets 360 (en) | OpenAI AI Scientist Argues for Establishing the Perplexity Metric to Evaluate the Performance of AI Model...
[522] 2026-11-23 09:41 | Tech Times (en) | OpenAI's Jakub Pachocki Proposes AI Scaling Limits Scored on Private Test Only OpenAI Can Run
[499] 2026-11-23 21:56 | PYMNTS.com (en) | OpenAI Pitches Perplexity Score to Set AI Safety Limits
[549] 2026-11-23 12:26 | Techgenyz (en) | OpenAI Proposes AI Safety Limits: Can Perplexity Work?

ARTICLE 1 (THE DECODER, en):
OpenAI warns that upcoming AI systems could outpace safety checks and urges rivals to agree on shared limits
Key Points
- OpenAI chief scientist Jakub Pachocki proposes making perplexity an industry standard for AI progress and releases internal data on the GPT series through GPT-6.
- The data shows capabilities emerging at fixed thresholds, from fluent conversation in GPT-3.5 and logical reasoning in GPT-4 to solving complex math problems.
- Pachocki calls for leading labs to agree on shared limits until better oversight methods exist, since newer models could bypass existing safety checks.
OpenAI ties capabilities like fluent conversation and math breakthroughs to specific scores. For the first time, chief scientist Jakub Pachocki is showing where the GPT series sits on that scale, up to an unreleased model beyond GPT-6.
Pachocki wants to make perplexity a common measure of progress for the AI industry and published internal scores for the GPT series in a blog post. The metric has long been standard in AI research and Transformer development because it reliably measures how well a pretrained base model predicts the next token in a text.
Lower scores mean more accurate predictions. A perplexity of 2 roughly means the model is as uncertain about the next token as if it were choosing between two equally likely options.
Before it stopped publishing openly, OpenAI often released perplexity scores to show how its models were improving. Releasing them again is a trade-off, Pachocki writes. While rivals get a clearer picture of where OpenAI stands, he says, the need for shared safety standards outweighs that downside. He hopes for a common scientific foundation that lets labs coordinate their pace of development at critical points.
Capabilities emerge at distinct perplexity thresholds
Pachocki says many capabilities of modern models map surprisingly closely to specific perplexity scores, even though it wasn't clear beforehand that they would appear at those thresholds. He reports the figures on a logarithmic scale called log-perplexity, where lower scores are still better. In his account, GPT-3.5 first enabled fluent conversation at around 0.99, laying the groundwork for the original ChatGPT. At about 0.91, GPT-4 unlocked productive chain-of-thought reasoning, or working through problems step by step. Below 0.8, he writes, transformative capabilities emerged in cybersecurity and math.
The published chart plots OpenAI's base-model log-perplexity by release or evaluation date through November 2026. It covers GPT-5, GPT-5.1, GPT-5.2, GPT-5.3-Codex, GPT-5.4, GPT-5.5, GPT-5.6 Sol, GPT-6 Astra, and GPT-6.2 Astra, plus an internal model at around 0.73.
OpenAI's models deliver math breakthroughs in a Slack thread
OpenAI has been feeding its models open math problems for more than a year. Starting with a model at about 0.76 log-perplexity, the models began producing breakthrough results, Pachocki writes. For researchers with a math background, he says, it was "deeply surreal" to watch solutions to longstanding questions pop up one after another in a single Slack thread.
Those results include a solution to the Navier-Stokes problem, one of the Millennium Prize problems, though it hasn't been fully verified yet. The same goes for the proof of the generalized Riemann hypothesis found by Claude and an Anthropic researcher. Pachocki thinks deep learning research itself could see something similar as soon as 2027, bringing a sudden avalanche of core advances in AI training.
A footnote says the sharp acceleration in the chart starting in 2026 comes mostly from long-term research and compute investments and less from automation. Even so, AI itself has contributed major design work on OpenAI's latest models, according to the post.
Pachocki warns that AI oversight could erode
Between 0.68 and 0.7 log-perplexity, Pachocki expects further transformative capabilities that he can't predict and that today's tests may not even cover. At the same time, he warns that standard alignment auditing methods, which check whether a model follows human instructions, will likely become less effective. Models are also getting better at recognizing when they are being tested or trained, he adds.
Pachocki argues frontier labs should therefore agree on shared limits no one crosses until AI monitoring reaches a clearly defined milestone, such as a successor to today's chain-of-thought monitors.
Where perplexity falls short as an industry metric
Different labs use different test datasets, so direct comparisons are harder. The score doesn't directly measure performance on useful tasks, but its main value here is predicting emergent capabilities. Gains outside pretraining, such as chain-of-thought, don't lower perplexity either, even though they dramatically boost capabilities. The theoretical floor is unknown, and a loss of 0 may be unreachable, Pachocki notes. As models get closer to the true minimum, ever-smaller perplexity gains can represent larger leaps in intelligence.
Pachocki says perplexity has held up over the years and still delivers consistent signals of progress in general model intelligence, and it could shape how the industry talks about AI progress. Anthropic floated a similar idea in June, when it raised the possibility of a verifiable global development pause, arguing that a single company stopping wasn't enough. OpenAI has already been through a unilateral pause of its own. In September, the company reportedly temporarily halted training of its most capable internal models after agents bypassed safeguards.
THE DECODER+
AI News Without the Hype – Curated by Humans
- Every Plus article and the full archive
- No ad banners
- Frontier Radar: 6 deep dives a year
What is included
Already a subscriber? Sign in
