# Görev: secim-ai_kodlama

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-ai_kodlama.json`
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

TOPIC: Yapay Zeka Kodlama Araçları ve Fiyatlar

CRITERIA:
Kullanıcı Claude Code (Claude aboneliği ile) ve DeepSeek API'sini (OpenCode üzerinden ve bu bültenin
kendisinde deepseek-flash olarak) kullanıyor. İleride Claude'u mimar, DeepSeek'i işçi model olarak
kullanıp uygulama/oyun yazmayı planlıyor. Fiyat/performans en önemli ölçüt.
Önemli: Claude Code, OpenAI Codex, Cursor, OpenCode, Gemini CLI gibi kodlama araçlarında büyük
yenilikler; Claude abonelik planı, kullanım kotası veya fiyat değişiklikleri; DeepSeek'in yeni modelleri,
API fiyat değişiklikleri ve indirimli saat düzenlemeleri; kodlama testlerinde (SWE-bench vb.) büyük
sıçramalar ve fiyat/performans dengesini değiştiren yeni modeller; ucuz ama güçlü API seçenekleri.
Fiyat değişikliği varsa eski ve yeni fiyatı (1M token başına) yaz. Puan varsa testi ve puanı yaz.
Önemsiz: küçük sürüm notları, eklenti tanıtımları, "en iyi 10 yapay zeka kodlama aracı" listeleri,
kişisel deneyim yazıları, küçük girişimlerin araç duyuruları.

CANDIDATE ITEMS:
[935] 2026-11-23 23:25 | The Verge (en) | The hacker behind early GTA VI leaks has been sentenced — and released -- Arion Kurtaj, who pleaded guilty last week to hacking Grand Theft Auto VI developer Rockstar Games and other companies, including Uber and Revolut, was sentenced to seven years in prison but released on license, the BBC reports. After hacking Rockstar, Kurtaj leaked 90 videos of GTA VI in 2022,...
[904] 2026-11-23 23:07 | TechCrunch (en) | Figma employee sues, alleging company forced her to confront harassers -- Figma and its CEO are being sued by a current employee for alleged sexual harassment and discrimination.
[825] 2026-11-23 23:43 | Business Insider (en) | Saudi engineer used OpenAI’s vibe-coding app to more efficiently bug its office, feds say
[881] 2026-11-23 23:29 | TestingCatalog AI News (en) | Anthropic launches Claude Ship to review and approve PRs
[498] 2026-11-23 23:06 | Decrypt (en) | China's Z.ai Says Its New AI Model Can Think Like One Four Times Its Size
[688] 2026-11-23 21:31 | Crypto Briefing (en) | Anthropic launches Claude Ship to automate code reviews and approvals
[808] 2026-11-23 21:28 | 财联社 (zh) | V4全系列模型上线后，DeepSeek Harness开发者预览版开放测试
[715] 2026-11-23 23:11 | foreignpolicy.com (en) | Hacker News 298 puan, 234 yorum | Huang's Return Is the Biggest National Security Scandal in U.S. History
[936] 2026-11-23 23:14 | The Verge (en) | The Verge’s 2026 holiday gift guide -- Gift-giving isn't just about grabbing something solid under a determined spending limit. It's an expression of love, a way to show someone how well you really know them. That doesn't mean you have to do it all on your own, though. Sometimes you just need a push in the right direction to get things...
[863] 2026-11-23 22:05 | TechCrunch (en) | Anthropic launches Claude Ship, letting AI agents review, fix, and approve code changes
[854] 2026-11-23 22:05 | Blockchain News (en) | Enterprise Guide to Securing AI Coding Agents with Claude Code
[868] 2026-11-23 23:24 | NewsCord (en) | Anthropic Launches Claude Ship to Automate Code Reviews, Fixes, and PR Approvals: 11 outlets compared
[137] 2026-11-23 20:03 | Fello AI (en) | Best Chinese AI Models in 2026: Ranked and Compared
[686] 2026-11-23 21:27 | Crypto Briefing (en) | VentureBeat bug-fix test finds Anthropic’s top Claude model costs triple for similar results
[810] 2026-11-23 18:17 | 证券之星 (zh) | 前端白菜价后端开盲盒 Token中转站生意需引入“公平秤”-证券之星
[716] 2026-11-23 22:32 | stalw.art (en) | Hacker News 428 puan, 221 yorum | Stalwart is now a drop-in replacement for Microsoft Exchange
[937] 2026-11-23 22:38 | The Verge (en) | Ladybird, the first new browser engine in years, is now available to test -- Ladybird, an independent web browser with a built-from-scratch engine, is finally available for testing. On Monday, Ladybird developer Andreas Kling announced that the browser is launching in alpha on macOS and Linux following more than four years of development. The release makes Ladybird the...
[905] 2026-11-23 22:02 | TechCrunch (en) | Supertake raises $20M to let AI agents turn your market predictions into stock portfolios -- Think you know where the market is headed? A startup called Supertake lets you put money behind your ideas without having to pick the stocks yourself.
[828] 2026-11-23 20:51 | Digital Trends (en) | Anthropic expands Claude Code’s review tools with automated fixes and approvals
[865] 2026-11-23 22:13 | www.trendingtopics.eu (en) | Claude Ship: Anthropic Now Lets Claude Approve Code, Too
[685] 2026-11-23 11:45 | Coin Gabbar (en) | Pearl Research OpenRouter Twist: Is DeepSeek Traffic Quietly Shifting?
[689] 2026-11-23 15:53 | The Innermost Loop (en) | Welcome to November 23, 2026
[812] 2026-11-23 18:15 | CSDN (zh) | DeepSeek API省钱攻略：这样调用，账单直接打骨折
[717] 2026-11-23 22:09 | github.com (en) | Hacker News 325 puan, 287 yorum | VP ≠ VNP over Every Field
[938] 2026-11-23 22:25 | The Verge (en) | Trump engineered a risky comeback for Jensen Huang -- After Nvidia's board accepted Jensen Huang's resignation as CEO, following reports that Huang had an affair with an alleged Chinese spy, President Donald Trump, too, appeared to submit to the new reality. Appearing on Stephen A. Smith's radio show shortly after the news came out, Trump said that...
[906] 2026-11-23 21:47 | TechCrunch (en) | Elon Musk and others rejoice as Jensen Huang is reinstated at Nvidia amid scandal -- "The mistake has been corrected," Elon Musk chimed in.
[833] 2026-11-23 18:22 | Hackread (en) | Malicious Google Ads Target Mac Users with Fake Codex and Claude Code Downloads
[864] 2026-11-23 21:21 | TradingKey (en) | Anthropic Shares Drop Over 4% After Jensen Huang Returns to Nvidia: Open-Weight Models May Weaken Claude's Pricing Power
[144] 2026-11-23 11:15 | explainx.ai (en) | GLM-5.4 Coding Plan: +50% Quota, Vision (Nov 2026)
[691] 2026-11-23 10:28 | OrcaRouter (en) | WeMM-Ecom-35B-A3B vs Qwen3.8 27B: Nul gedeelde rijen
[811] 2026-11-23 14:48 | 虎嗅网 (zh) | DeepSeek完成千亿级融资 梁文锋保留战略主导权与研发路线选择权
[718] 2026-11-23 21:01 | bored.com (en) | Hacker News 227 puan, 50 yorum | Bored.com: Fun, interesting and cool websites to explore when bored
[939] 2026-11-23 22:10 | The Verge (en) | Nvidia CEO Jensen Huang is back. What happens next? -- After the leather-jacketed CEO, president, and co-founder of Nvidia, Jensen Huang, acknowledged an affair with Megaspeed exec Alice Huang, his company initially announced an investigation. However, after The New York Times reported there was evidence of another affair, this time with an Nvidia...
[907] 2026-11-23 21:25 | TechCrunch (en) | Megaspeed, the company at the center of the Jensen Huang scandal, appears headed for a US blacklist -- Megaspeed, which was previously led by Alice Huang, would be barred from doing business with U.S. companies unless those companies attained a special license from the federal government.
[836] 2026-11-23 18:02 | The AI Economy | Ken Yeung (en) | AWS's Strands Decider v2 Gets Smarter at Every Size and More Honest About Its Doubts
[883] 2026-11-23 21:09 | The Next Web (en) | Anthropic can now watch for Claude misuse without keeping firms’ data
[687] 2026-11-23 10:02 | OrcaRouter (en) | MiniCPM5-15B-A2.5B vs MiniCPM5-2B: Six Times the Weights, Same Compute
[813] 2026-11-23 11:50 | 21财经 (zh) | 免费AI的好日子，到头了？
[719] 2026-11-23 20:52 | github.com (en) | Hacker News 243 puan, 96 yorum | GPUI is getting its own repository
[940] 2026-11-23 22:09 | The Verge (en) | Welcome to elite impunity, Jensen Huang -- I suppose I must issue a hearty congratulations to Jensen Huang. Huang is CEO of Nvidia again, to the surprise of basically everyone except President Donald Trump and Vice President JD Vance, who made sure it happened. Now, those of us who had some kind of basic education on what a "free market" is...
[908] 2026-11-23 20:38 | TechCrunch (en) | Nvidia employees greet Jensen Huang like a hero as he returns to company’s HQ -- "I'm going to be a better CEO the second time around," Huang told employees.
[834] 2026-11-23 17:30 | XDA (en) | I stopped trusting Claude's memory and setup my own memory system
[870] 2026-11-23 21:02 | citybiz (en) | Anthropic Launches Claude Ship to Automate Code Reviews, Fix Pull Requests and Accelerate Software Development
[692] 2026-11-23 09:13 | NewzTodays (en) | Inkling 2 Mini: Advanced AI Model for Laptops Without Internet
[193] 2026-11-23 10:12 | 36氪 (zh) | 成为 DeepSeek 的股东有什么好处
[720] 2026-11-23 19:17 | arm.jonpalmisc.com (en) | Hacker News 1009 puan, 230 yorum | Per Arm's request, this site will be discontinued on 4 December 2026 :(
[941] 2026-11-23 21:36 | The Verge (en) | The Apple Watch Series 12 is cheaper than ever ahead of Black Friday -- If you’re still sporting an Apple Watch that launched five or so years ago, now is a good time to upgrade. Apple’s latest smartwatch, the 42mm Apple Watch Series 12 with Wi-Fi and GPS connectivity, has dropped to a new all-time low of $299 ($100 off) at Amazon and Walmart, beating its most sale...
[909] 2026-11-23 19:31 | TechCrunch (en) | ‘Outrageous,’ ‘reckless,’ ‘wow’: Here’s what politicians are saying about Jensen Huang’s return to Nvidia -- Politicians on both sides of the aisle seem angry, agitated, and nonplussed by the return of Jensen Huang to Nvidia leadership less than two weeks after his ousting.
[831] 2026-11-23 16:25 | CRN (en) | OpenAI Channel Chief: ‘Huge’ Codex, Dots And AI Security Opportunities For Partners Today
[897] 2026-11-23 21:00 | FourWeekMBA (en) | Anthropic’s Claude Ship Puts 3 Agents on Pull Requests
[815] 2026-11-23 09:57 | DoNews (zh) | 从英伟达换帅到DeepSeek限额：AI行业不再“拼卡”、开始“算账”
[721] 2026-11-23 18:48 | github.com (en) | Hacker News 568 puan, 130 yorum | Apple removed my open-source app from EU App Stores after 9 years
[942] 2026-11-23 21:09 | The Verge (en) | AI data centers throw another wrench into chaotic UN climate talks -- The clean energy revolution hinges on electrifying the entire global economy just as power-hungry data centers are surging. It's a challenge delegates from all over the world had to grapple with during the United Nations climate conference, called COP31, that came to a chaotic and unsatisfying...
[910] 2026-11-23 19:14 | TechCrunch (en) | Google Search’s AI Mode can now tap into Drive and Wallet for more personalized responses -- By bringing Personal Intelligence in AI Mode to Drive, Wallet, and the Gemini app, Google is leveraging the wealth of user data that's already within its ecosystem
[849] 2026-11-23 16:18 | TIKR.com (en) | Musk Says SpaceX’s Cursor Isn’t “Serving Open Source Models” as Trump Weighs Outing Chinese AI Users
[871] 2026-11-23 20:55 | BigGo Finance (en) | Anthropic Debuts Claude Ship to Automate Pull Request Reviews and Merges
[817] 2026-11-23 09:15 | 虎嗅网 (zh) | DeepSeek免费额度收紧，大厂围绕AI智能体与Token消耗争夺“第一”
[722] 2026-11-23 17:17 | blog.kagi.com (en) | Hacker News 415 puan, 331 yorum | Kagi Mail (public beta)
[943] 2026-11-23 20:56 | The Verge (en) | Apple renews Ted Lasso for season 5 -- One of Apple's biggest shows is sticking around for at least one more season. The streamer announced today that Ted Lasso has been renewed for a fifth season on Apple TV, after what the company calls a "record-breaking season four debut." There are currently no details about the new season nor when...
[911] 2026-11-23 18:12 | TechCrunch (en) | Jensen Huang scheduled to testify next week in Senate following reinstatement as Nvidia CEO -- The public hearing follows bipartisan calls for Huang to testify about the affair with the suspected Chinese spy.
[844] 2026-11-23 15:52 | NeoTeo (en) | Uber Claude Code cost controls: settings and results
[885] 2026-11-23 20:55 | Meteora Web (en) | Claude Ship, Anthropic Automates Pull Requests
[723] 2026-11-23 17:07 | twitter.com (en) | Hacker News 1613 puan, 903 yorum | "Dario is hyping the dangers so OSS models are illegal and he stays in control"
[912] 2026-11-23 17:14 | TechCrunch (en) | Amazon snaps up Decart’s AI optimization tech for $7.5B as it prepares to sell Trainium chips -- Amazon is acquiring Decart’s AI optimization technology and research team in a reported $7.5 billion deal.
[829] 2026-11-23 15:38 | Food Safety Magazine (en) | Codex Committee on Food Hygiene Green-Lights New Work on Infant Formula Standards at 56th Session
[878] 2026-11-23 20:47 | MLQ.ai (en) | Anthropic launches Claude Ship to review, fix and approve pull requests under team-set rules
[724] 2026-11-23 16:13 | ladybird.org (en) | Hacker News 1856 puan, 428 yorum | Ladybird is now in alpha
[913] 2026-11-23 17:13 | TechCrunch (en) | Meta throws out its net-zero goal — its competitors should be furious -- Meta walked back its 2030 net-zero goal in its 2026 sustainability report, a decision that could send ripples throughout the tech industry.
[832] 2026-11-23 15:21 | Finextra Research (en) | Stablecoin FX platform Codex raises $30m
[886] 2026-11-23 20:31 | Bradenton Herald (en) | Anthropic launches Claude Ship to help developers ship code faster
[725] 2026-11-23 14:49 | reddit.com (en) | Hacker News 287 puan, 306 yorum | Cyberleek posted the GTA 6 Xbox build executable
[914] 2026-11-23 16:57 | TechCrunch (en) | Jensen Huang isn’t out of the woods yet -- Tech leaders should be clear-eyed about all the problems likely to come with Huang's reinstatement.
[846] 2026-11-23 14:39 | Yucatán Magazine (en) | Maní Opens Museum to Yucatán’s Most Divisive Friar
[867] 2026-11-23 20:24 | The New Stack (en) | Anthropic’s Claude Ship lets Claude approve routine pull requests
[726] 2026-11-23 14:01 | nvidianews.nvidia.com (en) | Hacker News 1708 puan, 4 yorum | Jensen Huang Reinstated as Nvidia CEO
[915] 2026-11-23 16:39 | TechCrunch (en) | Trump reportedly called into Nvidia board meeting to advocate for Huang’s return -- "I spoke to them, let me put it that way … And I convinced them," the president told CNBC.
[842] 2026-11-23 14:30 | AIM Media House (en) | An Engineer Used Claude Code to Catch What CAM Software Missed
[891] 2026-11-23 19:36 | MLQ.ai (en) | Anthropic starts rolling out Enterprise Frontier Safeguards, letting customers keep Claude logs in their own cloud
[727] 2026-11-23 14:00 | cnbc.com (en) | Hacker News 2518 puan, 3438 yorum | Nvidia Reinstates Jensen Huang as CEO
[916] 2026-11-23 16:20 | TechCrunch (en) | CrowdStrike to acquire Zscaler for $44 billion -- The cybersecurity giant said that the deal to buy Zscaler, which provides zero-trust products to large enterprises, will better equip their customers to use AI agents.
[847] 2026-11-23 13:13 | Digg (en) | Cursor reportedly eyes the exit for Chinese open-weight AI models
[875] 2026-11-23 18:51 | Superpower Daily (en) | Anthropic opens beta for Claude safety monitoring with customer-held data
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file
[917] 2026-11-23 15:29 | TechCrunch (en) | Dutch privacy authority urges consumers not to buy ‘pervert glasses’ -- The Netherlands' data protection agency said smart wearables violate people's privacy, get information wrong, and can result in unsafe situations.
[860] 2026-11-23 13:02 | Geeky Gadgets (en) | Anthropic Engineer Explains How to Verify Claude Code Builds
[901] 2026-11-23 18:43 | FourWeekMBA (en) | Anthropic Opens 6 Top Claude Models to Zero-Retention Firms
[729] 2026-11-23 12:28 | github.com (en) | Hacker News 231 puan, 121 yorum | Win4wine – Run Windows Shell as the Linux Desktop on Wine
[918] 2026-11-23 15:04 | TechCrunch (en) | Trump endorses Jensen Huang’s return to Nvidia, says US has no evidence of wrongdoing -- The president cited the U.S. attorney general in defending Huang's return, while urging Nvidia to continue its export-control investigation.
[826] 2026-11-23 12:20 | infoq.com (en) | Claude Code Adds Experimental Code Mode to Orchestrate Tool Calls with JavaScript
[888] 2026-11-23 18:20 | Unite.AI (en) | Anthropic Launches Zero-Retention Enterprise Frontier Safeguards Beta
[730] 2026-11-23 11:34 | blogs.gentoo.org (en) | Hacker News 243 puan, 150 yorum | The next circle of security hell
[893] 2026-11-23 14:43 | TechCrunch (en) | Sierra doubles valuation to $30B within six months as demand for AI in CX takes off
[840] 2026-11-23 12:20 | HackerNoon (en) | 48 Agent Skills Build MCP Servers, Claude Code Plugins and Even More Agent Skills
[879] 2026-11-23 16:26 | www.equiti.com (en) | Is Anthropic profitable? revenue, losses and what the S-1 shows
[731] 2026-11-23 11:31 | github.com (en) | Hacker News 308 puan, 172 yorum | Nospeaker ─ Make your MacBook's trackpad talk using its haptic motor
[919] 2026-11-23 14:21 | TechCrunch (en) | Nvidia reverses course and brings Jensen Huang back as CEO -- The stunning reversal was supported by senior officials in the U.S. government.
[841] 2026-11-23 12:08 | Fello AI (en) | Claude Code Effort Levels: What They Do, Which to Use
[880] 2026-11-23 16:05 | Nikkei Asia (en) | Anthropic Claude use by Japanese companies up 10-fold in 1 year
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[920] 2026-11-23 14:00 | TechCrunch (en) | Uber now lets you book rides that record the drive by default -- The option, which uses the front camera of the Uber driver's phone to record the entire drive, will show up as "Recorded Rides" in the app.
[531] 2026-11-23 12:06 | Pasquale Pillitteri (en) | Nadella confirms the IDE faded in six months after Claude Code
[882] 2026-11-23 13:45 | CBS News (en) | Anthropic's IPO set for blastoff Thursday in key test for AI boom
[733] 2026-11-23 09:55 | jola.dev (en) | Hacker News 172 puan, 109 yorum | Don't keep your SSH keys on disk
[921] 2026-11-23 01:25 | TechCrunch (en) | How Notable Capital, GIC, and Baillie Gifford bet big on Anthropic before its IPO -- Three significant backers share what gave them conviction to bet billions on Anthropic.
[851] 2026-11-23 11:14 | cwibc.co.id (en) | Nadella Says Claude Code Reshaped Software Development
[900] 2026-11-23 12:18 | Business Today (en) | Anthropic's Claude AI takes on 167-year-old maths mystery, two groups verify landmark proof
[734] 2026-11-23 05:00 | micjm.com (en) | Hacker News 263 puan, 340 yorum | Yes, 17M lines of Lean is a lot
[850] 2026-11-23 10:43 | Digg (en) | Cursor weighs dropping Chinese open-weight AI models. A House report says U.S. open-weight options fall short on price and performance
[902] 2026-11-23 12:15 | FourWeekMBA (en) | Gemini Neon Tops Coding Agent Index at 44% of Claude’s Cost
[735] 2026-11-23 04:31 | facebook.com (en) | Hacker News 218 puan, 80 yorum | DeFlock Amerca Facebook Group Suspended as a Dangerous Organization
[848] 2026-11-23 10:33 | TradingView (en) | Satya Nadella Says Claude Code Could Reshape IDE Use Within Six Months
[503] 2026-11-23 11:32 | shattered.io (en) | GPT-6.2 Astra vs Fable 5.2: 62 vs 65, $4 vs $26.54
[736] 2026-11-23 03:49 | github.com (en) | Hacker News 609 puan, 245 yorum | XNU Linux: Full OS X GUI Userspace on Linux
[838] 2026-11-23 10:32 | Mobile World Live (en) | Cursor mulls exit from Chinese AI models over geopolitical risks
[890] 2026-11-23 09:33 | TradingView (en) | Anthropic Claude AI Predicts the Top 3 Bitcoin Mining Stocks for 2027
[737] 2026-11-23 03:34 | docker.com (en) | Hacker News 123 puan, 202 yorum | The New Docker: Built to Run on Trust
[858] 2026-11-23 09:41 | Windows Report (en) | Satya Nadella Admits Claude Code Is Changing the Future of VS Code as Developers Move Away From IDEs
[892] 2026-11-23 08:57 | CIO Africa (en) | Anthropic Raises $81.3 Billion In Record IPO
[738] 2026-11-23 01:50 | pengowray.github.io (en) | Hacker News 192 puan, 76 yorum | Treemap of the Riemann Hypothesis Lean Proof
[843] 2026-11-23 09:23 | Techzine Global (en) | Developers get real-time AI coding cost controls inside Claude Code
[896] 2026-11-23 08:51 | The Business Times (en) | China’s next AI deals clouded by sell-off after Anthropic IPO
[739] 2026-11-23 01:13 | spelunkr.tv (en) | Hacker News 990 puan, 170 yorum | Free alternative TV network built from the Internet Archive's forgotten videos
[740] 2026-11-22 23:57 | alvinbunk.wordpress.com (en) | Hacker News 306 puan, 322 yorum | Nespresso's project to reduce grams of coffee per capsule
[741] 2026-11-22 23:12 | claude.ai (en) | Hacker News 257 puan, 201 yorum | Progress map for the Lean formalization of Alessandro Masullo's manuscript
[742] 2026-11-22 23:06 | gl-inet.com (en) | Hacker News 269 puan, 125 yorum | GL.iNet Security Notice 2026-11-20
[743] 2026-11-22 22:10 | ias.edu (en) | Hacker News 179 puan, 42 yorum | The Usefulness of Useless Knowledge [pdf] (1939)
[744] 2026-11-22 21:53 | pequalnp.com (en) | Hacker News 194 puan, 170 yorum | Agent swarm trying to solve P vs. NP
[745] 2026-11-22 21:33 | fosscomics.com (en) | Hacker News 169 puan, 76 yorum | The struggle for freedom: How BSD escaped AT&T licensing
[746] 2026-11-22 21:01 | gpupartner.com (en) | Hacker News 755 puan, 306 yorum | A Beginner's Guide to Local AI with Inkling, Qwen, and Unsloth
[747] 2026-11-22 20:33 | smallsf.com (en) | Hacker News 373 puan, 114 yorum | A miniature San Francisco with live buses, planes and boats
[748] 2026-11-22 20:15 | nyx.net (en) | Hacker News 160 puan, 39 yorum | Nyx, the Oldest Internet Service Provider
[749] 2026-11-22 19:51 | nytimes.com (en) | Hacker News 124 puan, 54 yorum | All Delta Flights Through Atlanta Delayed as Automated System Falters
[750] 2026-11-22 19:41 | danielsada.tech (en) | Hacker News 314 puan, 162 yorum | Assume People Are Not Interested in Your Blog Post
[751] 2026-11-22 19:20 | oreilly.com (en) | Hacker News 807 puan, 450 yorum | What Happened to Silicon Valley?
[752] 2026-11-22 19:10 | niemanlab.org (en) | Hacker News 126 puan, 130 yorum | Canada recommits to federal funding for news
[753] 2026-11-22 19:10 | audubon.org (en) | Hacker News 631 puan, 367 yorum | The Birds of GTA 6: A Field Guide to Vice City and Leonida's Wildlife
