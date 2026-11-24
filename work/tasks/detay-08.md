# Görev: detay-08

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-08.json`
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

HEADLINE: Anthropic, halka arz dosyasında yapay zekanın insanlık için varoluşsal riskler doğurabileceğini belirtti; yatırımcılar halka arz öncesi milyarlarca dolarlık bahsi savundu.

RELATED ITEMS:
[530] 2026-11-23 12:37 | DD News (en) | Anthropic warns AI may pose ‘existential risks to humanity’ in IPO filing
[921] 2026-11-23 01:25 | TechCrunch (en) | How Notable Capital, GIC, and Baillie Gifford bet big on Anthropic before its IPO -- Three significant backers share what gave them conviction to bet billions on Anthropic.

ARTICLE 1 (TechCrunch, en):
When Notable Capital poured roughly 10% of the U.S. portion of its $1 billion-plus fund into Anthropic’s Series E, many limited partners were perplexed by the massive bet. Claude’s maker was still trailing its biggest rival, OpenAI, and DeepSeek had released its R1 model just weeks earlier, threatening to overtake closed-source competitors. Moreover, at $61.5 billion, Anthropic’s valuation was by far the highest that the predominantly early-stage venture firm had invested at in its 25-year history.
But investors at Notable, formerly known as GGV Capital before its 2024 split from its China operations, were confident in Anthropic’s future. A team that included managing partners Hans Tung and Glenn Solomon and principal Dan Cahana crunched the numbers and found that developers were increasingly favoring Claude over ChatGPT, using Claude 70% of the time, a reversal from the previous year.
“We just knew that for AI, the developer is at the center because they’re the early adopters,” Tung, a co-founder of the firm, told TechCrunch.
Having navigated the complexities of U.S.-China relations firsthand, Notable was also convinced that “mission-critical” work would always require U.S.-based, closed-source models, Tung said.
Less than two years later, Notable’s LPs are no longer questioning the outsized investment. On Wednesday, Anthropic raised $81.3 billion in the largest IPO in history, reaching a fully diluted valuation of about $1.5 trillion before its shares began trading the next day, a paper return of roughly 15 times.
Notable is, of course, not the only firm sitting on a massive paper windfall from Anthropic. Even traditional late-stage investors, who rarely see venture-scale multiples, are set for a big win.
Singaporean sovereign wealth fund GIC had been researching Anthropic throughout 2024, but it didn’t make its first investment until after Chris Emanuel, head of the fund’s tech investments, met with Anthropic CFO Krishna Rao at Che Fico, an Italian restaurant in San Francisco, in early 2025. Rao helped pique Emanuel’s interest in the company by showing that model improvements were driving rising enterprise adoption. GIC then made its first investment in Anthropic’s Series F, a round announced in September 2025 at a $183 billion valuation.
“Claude Code was just released then,” Emanuel told TechCrunch. “It was very early, but even then, we could see signs that it was really popular with developers.” GIC’s conviction in the AI lab was so high that the fund later led Anthropic’s Series G alongside Coatue at a $380 billion valuation, and returned as one of the co-leads for the pre-IPO round at $965 billion. Across the three rounds, GIC invested more than $4.5 billion in Anthropic, amassing a stake equal to about 1% of the company after its IPO, according to a person familiar with GIC’s financials.
Lillian Li, who leads U.S. growth investments at Baillie Gifford, began studying Anthropic and speaking with its team nearly two years before first investing in the company’s Series F. Baillie Gifford invested in the Claude maker once it became clear that the company was “executing consistently” on its vision while generating strong revenue and “compelling gross margins,” Li told TechCrunch.
After Baillie Gifford’s initial investment, Anthropic continued to deliver on its promises, expanding into industries such as legal services and life sciences, as well as broader applications like Cowork and cybersecurity, Li said.
“It’s actually quite incredible how consistent they have been with how quickly everything’s been changing,” she added. Baillie Gifford went on to invest a total of $1.5 billion across three funding rounds, a bet that has generated substantial paper gains for the investment manager, as well as for dozens of other backers who invested at what once seemed like astronomical valuations.
