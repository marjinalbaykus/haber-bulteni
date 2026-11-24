# Görev: detay-13

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-13.json`
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

HEADLINE: Google Gemini 4 Neon ve OpenAI GPT-6.2 Astra-Sol modelleri, daha büyük kurumsal iş yükleri ve verimlilik hedefiyle duyuruldu.

RELATED ITEMS:
[500] 2026-11-23 13:15 | TechTarget (en) | Model Release: Gemini 4 Neon, GPT-6.2-Astra-Sol target business productivity
[544] 2026-11-23 22:02 | SoaplandTV (en) | Google's Gemini 4 Neon Takes on Bigger AI Jobs but Is It Actually Cheaper?

ARTICLE 1 (TechTarget, en):
Your briefing from Esther Shittu on the AI systems shaping the week ahead.
The AI model race is shifting from models that can perform individual tasks, such as coding, to models that power everyday work across the enterprise.
“There's a lot of competition right now for the enterprise UX itself,” said Bradley Shimmin, an analyst at Futurum Group.
That competition is evident in the types of models top generative AI vendors released last week, which typically focus not only on performance in coding and reasoning but also on professional work and on helping enterprise users be more effective in everyday tasks.
Google's new workhorse
Google introduced Gemini 4 Neon on Nov. 18, rolling it out across Google AI Studio, Gemini for business, Google Antigravity and the Gemini app. Google said the new model can consolidate across applications the work enterprises do in spreadsheets, PDFs, charts, audio and video and is built for long-running agent workflows.
This is the sixth model Google has introduced since September as it tries to keep up with rivals, chiefly OpenAI and Anthropic, developer of Claude.
"Google has been catching up quite significantly lately, and that's probably because … they have a broader ecosystem," said Lian Jye Su, an analyst at Omdia, a division of Informa TechTarget. "They have much better infrastructure that they can train their AI on."
With Neon, Google is going after the everyday work enterprises do and is striving to make it the default model for complex, high-volume agents, said Sid Nag, founder of Tekonyx.
"Google is betting that [enterprise] teams will pay for frontier model-level reasoning when it helps them finish more work in a predictable time and budget," Nag said. "Neon could really become Google's workforce for production agents."
OpenAI’s offerings
The ChatGPT maker also took a step toward greater productivity with the launch of GPT-6.2 Astra and GPT-6.2 Sol, also on Nov. 18.
GPT-6.2 Astra is the vendor’s frontier model among the latest releases, intended for the hardest projects, and GPT-6.2 Sol is for everyday work. The vendor said that the models deliver better results on demanding tasks with fewer tokens. GPT-6.2 Sol is more efficient than its predecessor, GPT-6.1 Sol, according to OpenAI. The model powers multistep agentic workflows and writes clearly and naturally. Meanwhile, OpenAI said GPT-6.2 Astra creates high-quality slides, spreadsheets and documents for professionals in fields including law, sales, finance and accounting.
"They're really trying to create that think-fast, think-slow parity among their models," Shimmin said. "Astra is the ‘think-slow, do deep work model’. Sol is the model doing daily workflows in a more performant and more cost-effective manner."
However, the models are still just sharpened versions and not full upgrades, Shimmin added.
Qwen4-Max: enterprise productivity in China
On the international front, on Nov. 17 Alibaba unveiled Qwen4-Max, a foundation model that, while strong in tasks such as coding and agentic reasoning, is also well-suited to professional work and creative production, including 3D modeling, game development, and agentic video creation. Alibaba called the model an always-on AI colleague that works, creates and explores alongside people.
The new model exemplifies how the agentic AI work market is exploding in China, said Lian Jye Su, an analyst at Omdia, a division of Informa TechTarget.
“It's a very big market in China because people don't have access to ChatGPT, people don't have access to Claude, to Gemini," Su said.
The target
The focus on everyday tasks reveals a "propensity for the model builders to build an agentic layer" on top of models that influence the future work surface, Nag said, one that can be generic or that enterprises can tailor for the enterprise worker.
"That is where all of these things are headed to; essentially, how do you make that work environment productive and build a work surface for the enterprise worker," he said.
Esther Shittu is a TechTarget reporter and podcast host and producer covering AI systems.
