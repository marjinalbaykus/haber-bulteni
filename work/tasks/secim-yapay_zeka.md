# Görev: secim-yapay_zeka

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-yapay_zeka.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You are the editor of a personal daily news bulletin for a Turkish reader.
You receive candidate news items for ONE topic from roughly the last 30 hours, in several languages
(Turkish, English, Spanish, Chinese). Pick only the genuinely important stories according to the topic
criteria, merge items that cover the same event, and write a one-line Turkish bullet for each.

Rules:
- Use ONLY the given items. Never add facts that are not in the titles or snippets.
- Merge items about the same event into one story and list all of their ids.
- Score importance 1-10 for THIS reader using the criteria: 10 = historic, 8 = big news, 7 = clearly worth
  knowing today, 5 = minor, 3 = noise. Be strict; most items are noise.
- Importance signals: the same event covered by many independent sources, Hacker News points,
  Hugging Face trend scores. A single clickbait or rumor source is weak evidence.
- Skip stories that were already reported on previous days (list given) unless there is a significant
  new development; in that case the headline must state what is new.
- If market data is given and a threshold is exceeded, treat that move as a story candidate even if no
  news item explains it (use ids: []).
- headline: Turkish, one sentence, at most about 25 words, concrete (who did what, numbers if present).
  No clickbait, no emojis.
- Return at most 8 stories, most important first. An empty list is fine and expected on quiet days.

Output JSON: {"stories": [{"ids": [int], "importance": int, "headline": str, "reason": str}]}
"reason" is a short Turkish note on why it matters.

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

CANDIDATE ITEMS:
[935] 2026-11-23 23:25 | The Verge (en) | The hacker behind early GTA VI leaks has been sentenced — and released -- Arion Kurtaj, who pleaded guilty last week to hacking Grand Theft Auto VI developer Rockstar Games and other companies, including Uber and Revolut, was sentenced to seven years in prison but released on license, the BBC reports. After hacking Rockstar, Kurtaj leaked 90 videos of GTA VI in 2022,...
[904] 2026-11-23 23:07 | TechCrunch (en) | Figma employee sues, alleging company forced her to confront harassers -- Figma and its CEO are being sued by a current employee for alleged sexual harassment and discrimination.
[498] 2026-11-23 23:06 | Decrypt (en) | China's Z.ai Says Its New AI Model Can Think Like One Four Times Its Size
[574] 2026-11-23 22:16 | Bioengineer.org (en) | Patient-Facing AI Models Diverge in Accuracy and Safety for Anal Fissure Inquiries
[556] 2026-11-23 23:17 | Sohu (zh) | 智谱发布新一代大模型
[646] 2026-11-23 22:47 | Foreks.com (tr) | Anthropic'ten yazılımcılara yeni yapay zeka aracı: Claude Ship kodları otomatik onaylayabilecek
[715] 2026-11-23 23:11 | foreignpolicy.com (en) | Hacker News 298 puan, 234 yorum | Huang's Return Is the Biggest National Security Scandal in U.S. History
[936] 2026-11-23 23:14 | The Verge (en) | The Verge’s 2026 holiday gift guide -- Gift-giving isn't just about grabbing something solid under a determined spending limit. It's an expression of love, a way to show someone how well you really know them. That doesn't mean you have to do it all on your own, though. Sometimes you just need a push in the right direction to get things...
[863] 2026-11-23 22:05 | TechCrunch (en) | Anthropic launches Claude Ship, letting AI agents review, fix, and approve code changes
[544] 2026-11-23 22:02 | SoaplandTV (en) | Google's Gemini 4 Neon Takes on Bigger AI Jobs but Is It Actually Cheaper?
[510] 2026-11-23 20:52 | Georgia Institute of Technology (en) | New Benchmark Could Help AI Keep Up with Current Events
[162] 2026-11-23 19:31 | 三易生活 (zh) | 蚂蚁阿福宣布健康服务升级，并开源医疗大模型
[634] 2026-11-23 22:26 | Ekonomim (tr) | Türkiye’nin yapay zekâ fırsatı fabrikaların içinde
[716] 2026-11-23 22:32 | stalw.art (en) | Hacker News 428 puan, 221 yorum | Stalwart is now a drop-in replacement for Microsoft Exchange
[937] 2026-11-23 22:38 | The Verge (en) | Ladybird, the first new browser engine in years, is now available to test -- Ladybird, an independent web browser with a built-from-scratch engine, is finally available for testing. On Monday, Ladybird developer Andreas Kling announced that the browser is launching in alpha on macOS and Linux following more than four years of development. The release makes Ladybird the...
[905] 2026-11-23 22:02 | TechCrunch (en) | Supertake raises $20M to let AI agents turn your market predictions into stock portfolios -- Think you know where the market is headed? A startup called Supertake lets you put money behind your ideas without having to pick the stocks yourself.
[499] 2026-11-23 21:56 | PYMNTS.com (en) | OpenAI Pitches Perplexity Score to Set AI Safety Limits
[581] 2026-11-23 20:03 | Sarvam AI (en) | Sarvam Dictate: fast, accurate dictation
[557] 2026-11-23 15:26 | 证券之星 (zh) | 智谱 重大发布！-证券之星
[665] 2026-11-23 22:22 | Sözcü Gazetesi (tr) | New York borsasında yapay zeka rallisi: Nasdaq yüzde 6,51 yükseldi
[717] 2026-11-23 22:09 | github.com (en) | Hacker News 325 puan, 287 yorum | VP ≠ VNP over Every Field
[938] 2026-11-23 22:25 | The Verge (en) | Trump engineered a risky comeback for Jensen Huang -- After Nvidia's board accepted Jensen Huang's resignation as CEO, following reports that Huang had an affair with an alleged Chinese spy, President Donald Trump, too, appeared to submit to the new reality. Appearing on Stephen A. Smith's radio show shortly after the news came out, Trump said that...
[906] 2026-11-23 21:47 | TechCrunch (en) | Elon Musk and others rejoice as Jensen Huang is reinstated at Nvidia amid scandal -- "The mistake has been corrected," Elon Musk chimed in.
[137] 2026-11-23 20:03 | Fello AI (en) | Best Chinese AI Models in 2026: Ranked and Compared
[577] 2026-11-23 15:07 | CASRAI (en) | AI Singapore and Votee AI Sign MoU for Southeast Asian Language Model Research
[563] 2026-11-23 14:23 | 上观新闻 (zh) | 同济发布数字文化产业研究成果：京粤沪浙位列国内文化AI创新第一梯队
[683] 2026-11-23 21:10 | DonanımHaber (tr) | Seedream 5.5 ile yapay zeka görsellerinde yeni dönem başlıyor
[718] 2026-11-23 21:01 | bored.com (en) | Hacker News 227 puan, 50 yorum | Bored.com: Fun, interesting and cool websites to explore when bored
[939] 2026-11-23 22:10 | The Verge (en) | Nvidia CEO Jensen Huang is back. What happens next? -- After the leather-jacketed CEO, president, and co-founder of Nvidia, Jensen Huang, acknowledged an affair with Megaspeed exec Alice Huang, his company initially announced an investigation. However, after The New York Times reported there was evidence of another affair, this time with an Nvidia...
[907] 2026-11-23 21:25 | TechCrunch (en) | Megaspeed, the company at the center of the Jensen Huang scandal, appears headed for a US blacklist -- Megaspeed, which was previously led by Alice Huang, would be barred from doing business with U.S. companies unless those companies attained a special license from the federal government.
[512] 2026-11-23 20:00 | Search Engine Journal (en) | I Blind-Tested 3 Gemini Models On Real SEO Questions – Neon Won 5 Of 6
[584] 2026-11-23 14:35 | BigGo Finance (en) | Evercore Says AI-Era Selloff Echoes 1987 Crash, Calls It a Buying Window
[554] 2026-11-23 13:45 | 东方财富网 (zh) | 智谱发布GLM-5.4旗舰模型
[653] 2026-11-23 17:50 | Anadolu Ajansı (tr) | Amazon, Decart'ın yapay zeka optimizasyon teknolojisini satın alıyor
[719] 2026-11-23 20:52 | github.com (en) | Hacker News 243 puan, 96 yorum | GPUI is getting its own repository
[940] 2026-11-23 22:09 | The Verge (en) | Welcome to elite impunity, Jensen Huang -- I suppose I must issue a hearty congratulations to Jensen Huang. Huang is CEO of Nvidia again, to the surprise of basically everyone except President Donald Trump and Vice President JD Vance, who made sure it happened. Now, those of us who had some kind of basic education on what a "free market" is...
[908] 2026-11-23 20:38 | TechCrunch (en) | Nvidia employees greet Jensen Huang like a hero as he returns to company’s HQ -- "I'm going to be a better CEO the second time around," Huang told employees.
[517] 2026-11-23 19:27 | Quartz (en) | Anthropic is giving enterprise customers control of their own AI data
[578] 2026-11-23 14:32 | Wccftech (en) | Z.ai Debuts GLM-5.4 With a Novel Looped Transformer Architecture, Skipping AI Benchmarks Entirely
[553] 2026-11-23 12:26 | 中国教育新闻网 (zh) | 厦门理工学院联合多家单位发布新一代“文鳐”海洋原生多模态大模型
[681] 2026-11-23 12:45 | Anadolu Ajansı (tr) | Yapay zeka endişeleri küresel piyasalardaki korkuyu canlı tutuyor
[720] 2026-11-23 19:17 | arm.jonpalmisc.com (en) | Hacker News 1009 puan, 230 yorum | Per Arm's request, this site will be discontinued on 4 December 2026 :(
[941] 2026-11-23 21:36 | The Verge (en) | The Apple Watch Series 12 is cheaper than ever ahead of Black Friday -- If you’re still sporting an Apple Watch that launched five or so years ago, now is a good time to upgrade. Apple’s latest smartwatch, the 42mm Apple Watch Series 12 with Wi-Fi and GPS connectivity, has dropped to a new all-time low of $299 ($100 off) at Amazon and Walmart, beating its most sale...
[909] 2026-11-23 19:31 | TechCrunch (en) | ‘Outrageous,’ ‘reckless,’ ‘wow’: Here’s what politicians are saying about Jensen Huang’s return to Nvidia -- Politicians on both sides of the aisle seem angry, agitated, and nonplussed by the return of Jensen Huang to Nvidia leadership less than two weeks after his ousting.
[506] 2026-11-23 18:42 | The Decoder (en) | OpenAI warns that upcoming AI systems could outpace safety checks and urges rivals to agree on shared limits
[593] 2026-11-23 13:50 | iHarare News (en) | BIT Capital Expands Asia-Pacific Activities with Focus on Hong Kong and Singapore, Bringing Its Human × AI Approach to Asian Investors
[565] 2026-11-23 12:26 | 36Kr (zh) | 国产大模型的黄仁勋变量
[675] 2026-11-23 12:39 | MEB (tr) | "OKULDAN ÖĞRENDİM YAPAY ZEKÂ TURNUVASI" BAŞLIYOR
[721] 2026-11-23 18:48 | github.com (en) | Hacker News 568 puan, 130 yorum | Apple removed my open-source app from EU App Stores after 9 years
[942] 2026-11-23 21:09 | The Verge (en) | AI data centers throw another wrench into chaotic UN climate talks -- The clean energy revolution hinges on electrifying the entire global economy just as power-hungry data centers are surging. It's a challenge delegates from all over the world had to grapple with during the United Nations climate conference, called COP31, that came to a chaotic and unsatisfying...
[910] 2026-11-23 19:14 | TechCrunch (en) | Google Search’s AI Mode can now tap into Drive and Wallet for more personalized responses -- By bringing Personal Intelligence in AI Mode to Drive, Wallet, and the Gemini app, Google is leveraging the wealth of user data that's already within its ecosystem
[501] 2026-11-23 18:05 | TradingView (en) | Anthropic Is Releasing New Safety System That Lets Business Customers Keep Control Of Their Data When Using Its AI Models - Bloomberg News
[582] 2026-11-23 13:15 | 巴士的報 (en) | VENETA WorldModel Ranks 1st in Two Categories on NIKA AI Network Benchmark
[568] 2026-11-23 11:55 | 36Kr (zh) | ​豆包工作下调多款模型消耗倍率，超八成日常任务免费用
[722] 2026-11-23 17:17 | blog.kagi.com (en) | Hacker News 415 puan, 331 yorum | Kagi Mail (public beta)
[943] 2026-11-23 20:56 | The Verge (en) | Apple renews Ted Lasso for season 5 -- One of Apple's biggest shows is sticking around for at least one more season. The streamer announced today that Ted Lasso has been renewed for a fifth season on Apple TV, after what the company calls a "record-breaking season four debut." There are currently no details about the new season nor when...
[911] 2026-11-23 18:12 | TechCrunch (en) | Jensen Huang scheduled to testify next week in Senate following reinstatement as Nvidia CEO -- The public hearing follows bipartisan calls for Huang to testify about the affair with the suspected Chinese spy.
[538] 2026-11-23 18:02 | آي-فون إسلام (en) | Google Launches Gemini 3.7 Flash: Super-Fast Coding and Half the Price
[587] 2026-11-23 13:00 | Business Wire (en) | Hammerspace Establishes New World Performance Record, Topping Results from All Leading Commercial Parallel File Systems
[168] 2026-11-23 11:40 | www.anfangnews.com (zh) | 全球大模型都出自哪些城市？京杭占三成_人工智能
[723] 2026-11-23 17:07 | twitter.com (en) | Hacker News 1613 puan, 903 yorum | "Dario is hyping the dangers so OSS models are illegal and he stays in control"
[912] 2026-11-23 17:14 | TechCrunch (en) | Amazon snaps up Decart’s AI optimization tech for $7.5B as it prepares to sell Trainium chips -- Amazon is acquiring Decart’s AI optimization technology and research team in a reported $7.5 billion deal.
[540] 2026-11-23 18:00 | De Último Minuto (en) | OpenAI scientist calls for shared limits on AI development amid growing safety concerns
[586] 2026-11-23 11:48 | Bioengineer.org (en) | Multimodal AI Fuses Chest X-Rays and Health Records to Improve Diagnostic Accuracy
[558] 2026-11-23 11:34 | 星岛环球网 (zh) | AI视频电话实现“望闻问切”，蚂蚁阿福发布医疗大模型
[724] 2026-11-23 16:13 | ladybird.org (en) | Hacker News 1856 puan, 428 yorum | Ladybird is now in alpha
[913] 2026-11-23 17:13 | TechCrunch (en) | Meta throws out its net-zero goal — its competitors should be furious -- Meta walked back its 2030 net-zero goal in its 2026 sustainability report, a decision that could send ripples throughout the tech industry.
[547] 2026-11-23 15:49 | theopenpress.in (en) | Yann LeCun vs Dario Amodei: AI Godfather Slams Anthropic CEO
[592] 2026-11-23 11:44 | openPR.com (en) | SEO agent benchmark: paid data often costs more than the model
[552] 2026-11-23 11:19 | fjrb.fjdaily.com (zh) | 新一代“文鳐”海洋原生多模态大模型发布
[725] 2026-11-23 14:49 | reddit.com (en) | Hacker News 287 puan, 306 yorum | Cyberleek posted the GTA 6 Xbox build executable
[914] 2026-11-23 16:57 | TechCrunch (en) | Jensen Huang isn’t out of the woods yet -- Tech leaders should be clear-eyed about all the problems likely to come with Huang's reinstatement.
[497] 2026-11-23 15:13 | The Decoder (en) | Z.ai follows OpenAI into loop transformer territory with its new 750B flagship model
[503] 2026-11-23 11:32 | shattered.io (en) | GPT-6.2 Astra vs Fable 5.2: 62 vs 65, $4 vs $26.54
[559] 2026-11-23 11:18 | 东方财富网 (zh) | 智谱正式发布新一代大模型GLM-5.4
[726] 2026-11-23 14:01 | nvidianews.nvidia.com (en) | Hacker News 1708 puan, 4 yorum | Jensen Huang Reinstated as Nvidia CEO
[915] 2026-11-23 16:39 | TechCrunch (en) | Trump reportedly called into Nvidia board meeting to advocate for Huang’s return -- "I spoke to them, let me put it that way … And I convinced them," the president told CNBC.
[507] 2026-11-23 14:35 | The Mac Observer (en) | OpenAI Chief Scientist Wants New Limits on AI Development
[590] 2026-11-23 11:09 | FourWeekMBA (en) | Semtech Reports Today With a Guide 19.9% Above Its Record
[153] 2026-11-23 11:16 | 新浪新闻_手机新浪网 (zh) | 国产大模型引领全球开源生态（大数据观察·智能经济新形态）
[727] 2026-11-23 14:00 | cnbc.com (en) | Hacker News 2518 puan, 3438 yorum | Nvidia Reinstates Jensen Huang as CEO
[916] 2026-11-23 16:20 | TechCrunch (en) | CrowdStrike to acquire Zscaler for $44 billion -- The cybersecurity giant said that the deal to buy Zscaler, which provides zero-trust products to large enterprises, will better equip their customers to use AI agents.
[504] 2026-11-23 14:31 | OrcaRouter (en) | Gemini 4 Release Date: Helium Reported Next, No Date Announced
[580] 2026-11-23 11:05 | The Hacker News (en) | AI Coding Agents Are Getting Malicious Results From Web Search Indexes, WebSec Benchmark Finds
[163] 2026-11-23 11:10 | www.anfangnews.com (zh) | 智谱GLM-5.4大模型发布：仍是开源最强、比Opus 5.5差距5%内_人工智能
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file
[917] 2026-11-23 15:29 | TechCrunch (en) | Dutch privacy authority urges consumers not to buy ‘pervert glasses’ -- The Netherlands' data protection agency said smart wearables violate people's privacy, get information wrong, and can result in unsafe situations.
[495] 2026-11-23 13:45 | Android Police (en) | I still can't use Google's best Gemini model on my $20 AI Pro plan
[591] 2026-11-23 10:37 | FourWeekMBA (en) | HPE Reports Today With a Guide 17.5% Above a Record Quarter
[567] 2026-11-23 10:12 | 中国基金报 (zh) | 港股探底反弹，大模型双雄双双收涨
[729] 2026-11-23 12:28 | github.com (en) | Hacker News 231 puan, 121 yorum | Win4wine – Run Windows Shell as the Linux Desktop on Wine
[918] 2026-11-23 15:04 | TechCrunch (en) | Trump endorses Jensen Huang’s return to Nvidia, says US has no evidence of wrongdoing -- The president cited the U.S. attorney general in defending Huang's return, while urging Nvidia to continue its export-control investigation.
[521] 2026-11-23 13:45 | Crypto Briefing (en) | Chris Van Hollen backs bipartisan Senate AI committee and independent model testing
[570] 2026-11-23 09:58 | Sohu (zh) | 蚂蚁阿福App新版发布！联动真人医生把关，AI也能实现“望闻问切”了
[730] 2026-11-23 11:34 | blogs.gentoo.org (en) | Hacker News 243 puan, 150 yorum | The next circle of security hell
[893] 2026-11-23 14:43 | TechCrunch (en) | Sierra doubles valuation to $30B within six months as demand for AI in CX takes off
[525] 2026-11-23 13:45 | dev.ua (en) | OpenAI has released a guide to prompting and configuring the API for GPT-6.2 Astra and Sol models. What's changed for developers
[555] 2026-11-23 09:38 | 紫牛新闻 (zh) | “大模型经过中医类别医师资格考试试题测试”不实（2026·11·23）
[731] 2026-11-23 11:31 | github.com (en) | Hacker News 308 puan, 172 yorum | Nospeaker ─ Make your MacBook's trackpad talk using its haptic motor
[919] 2026-11-23 14:21 | TechCrunch (en) | Nvidia reverses course and brings Jensen Huang back as CEO -- The stunning reversal was supported by senior officials in the U.S. government.
[509] 2026-11-23 13:28 | Digg (en) | Users report 'Helium' label in Gemini on the web, rumored as next Gemini 4 variant
[560] 2026-11-23 09:34 | www.itsdw.cn (zh) | 摩尔线程发布KUAE训练套件新版 支撑万卡万亿模型训练
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[920] 2026-11-23 14:00 | TechCrunch (en) | Uber now lets you book rides that record the drive by default -- The option, which uses the front camera of the Uber driver's phone to record the entire drive, will show up as "Recorded Rides" in the app.
[518] 2026-11-23 13:26 | Quartz (en) | OpenAI's chief scientist called for industrywide limits on AI development
[561] 2026-11-23 09:31 | 星岛环球网 (zh) | 蚂蚁健康发布四大AI医疗健康模型，阿福推“视频看病”及国际版APP
[733] 2026-11-23 09:55 | jola.dev (en) | Hacker News 172 puan, 109 yorum | Don't keep your SSH keys on disk
[921] 2026-11-23 01:25 | TechCrunch (en) | How Notable Capital, GIC, and Baillie Gifford bet big on Anthropic before its IPO -- Three significant backers share what gave them conviction to bet billions on Anthropic.
[500] 2026-11-23 13:15 | TechTarget (en) | Model Release: Gemini 4 Neon, GPT-6.2-Astra-Sol target business productivity
[734] 2026-11-23 05:00 | micjm.com (en) | Hacker News 263 puan, 340 yorum | Yes, 17M lines of Lean is a lot
[530] 2026-11-23 12:37 | DD News (en) | Anthropic warns AI may pose ‘existential risks to humanity’ in IPO filing
[735] 2026-11-23 04:31 | facebook.com (en) | Hacker News 218 puan, 80 yorum | DeFlock Amerca Facebook Group Suspended as a Dangerous Organization
[527] 2026-11-23 12:30 | MakeUseOf (en) | Gemini can replace the touchscreen menus in your car, and you may already own one
[736] 2026-11-23 03:49 | github.com (en) | Hacker News 609 puan, 245 yorum | XNU Linux: Full OS X GUI Userspace on Linux
[549] 2026-11-23 12:26 | Techgenyz (en) | OpenAI Proposes AI Safety Limits: Can Perplexity Work?
[737] 2026-11-23 03:34 | docker.com (en) | Hacker News 123 puan, 202 yorum | The New Docker: Built to Run on Trust
[505] 2026-11-23 12:20 | Mezha (en) | OpenAI Proposes Limiting AI Development Until Companies Learn to Control It
[738] 2026-11-23 01:50 | pengowray.github.io (en) | Hacker News 192 puan, 76 yorum | Treemap of the Riemann Hypothesis Lean Proof
[531] 2026-11-23 12:06 | Pasquale Pillitteri (en) | Nadella confirms the IDE faded in six months after Claude Code
[739] 2026-11-23 01:13 | spelunkr.tv (en) | Hacker News 990 puan, 170 yorum | Free alternative TV network built from the Internet Archive's forgotten videos
[541] 2026-11-23 11:58 | BizPac Review (en) | Here’s why some say OpenAI’s latest model is ‘unusable,’ ‘uneven’ and ‘insufferable to work with’
[740] 2026-11-22 23:57 | alvinbunk.wordpress.com (en) | Hacker News 306 puan, 322 yorum | Nespresso's project to reduce grams of coffee per capsule
[542] 2026-11-23 10:56 | Windows Report (en) | Google Gemini Can Now Make GIFs, So You Can Finally Stop Searching for Them
[741] 2026-11-22 23:12 | claude.ai (en) | Hacker News 257 puan, 201 yorum | Progress map for the Lean formalization of Alessandro Masullo's manuscript
[508] 2026-11-23 10:49 | www.journaldunet.com (en) | OpenAI Engineers Share Top 4 Tips for Getting the Most From Code Agents
[742] 2026-11-22 23:06 | gl-inet.com (en) | Hacker News 269 puan, 125 yorum | GL.iNet Security Notice 2026-11-20
[529] 2026-11-23 10:09 | Dawan Africa (en) | OpenAI Urges Common Limits on AI Development
[743] 2026-11-22 22:10 | ias.edu (en) | Hacker News 179 puan, 42 yorum | The Usefulness of Useless Knowledge [pdf] (1939)
[543] 2026-11-23 10:06 | وكالة سوا الإخبارية (en) | OpenAI Chief Scientist Reveals Model Progress Metrics, Urges Shared Safety Thresholds
[744] 2026-11-22 21:53 | pequalnp.com (en) | Hacker News 194 puan, 170 yorum | Agent swarm trying to solve P vs. NP
[539] 2026-11-23 09:53 | Gadgets 360 (en) | OpenAI AI Scientist Argues for Establishing the Perplexity Metric to Evaluate the Performance of AI Model...
[745] 2026-11-22 21:33 | fosscomics.com (en) | Hacker News 169 puan, 76 yorum | The struggle for freedom: How BSD escaped AT&T licensing
[522] 2026-11-23 09:41 | Tech Times (en) | OpenAI's Jakub Pachocki Proposes AI Scaling Limits Scored on Private Test Only OpenAI Can Run
[746] 2026-11-22 21:01 | gpupartner.com (en) | Hacker News 755 puan, 306 yorum | A Beginner's Guide to Local AI with Inkling, Qwen, and Unsloth
[520] 2026-11-23 09:33 | LBB - Little Black Book (en) | Sora 2 Shutdown: What It Means for AI-Powered Creative When the Model Goes Dark — LBB
[747] 2026-11-22 20:33 | smallsf.com (en) | Hacker News 373 puan, 114 yorum | A miniature San Francisco with live buses, planes and boats
[548] 2026-11-23 09:12 | The Hans India (en) | Yann LeCun Calls Dario Amodei's AI Approach 'Medieval', Questions OpenAI Exit
[748] 2026-11-22 20:15 | nyx.net (en) | Hacker News 160 puan, 39 yorum | Nyx, the Oldest Internet Service Provider
[533] 2026-11-23 08:58 | DD News (en) | FM Sitharaman warns of ‘unprecedented’ AI risks, urges banks to bolster cyber defences
[749] 2026-11-22 19:51 | nytimes.com (en) | Hacker News 124 puan, 54 yorum | All Delta Flights Through Atlanta Delayed as Automated System Falters
[526] 2026-11-23 08:54 | ForkLog (en) | OpenAI chief scientist proposes measurable limits on advanced AI development
[750] 2026-11-22 19:41 | danielsada.tech (en) | Hacker News 314 puan, 162 yorum | Assume People Are Not Interested in Your Blog Post
[751] 2026-11-22 19:20 | oreilly.com (en) | Hacker News 807 puan, 450 yorum | What Happened to Silicon Valley?
[752] 2026-11-22 19:10 | niemanlab.org (en) | Hacker News 126 puan, 130 yorum | Canada recommits to federal funding for news
[753] 2026-11-22 19:10 | audubon.org (en) | Hacker News 631 puan, 367 yorum | The Birds of GTA 6: A Field Guide to Vice City and Leonida's Wildlife
