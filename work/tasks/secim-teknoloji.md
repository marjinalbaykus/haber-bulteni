# Görev: secim-teknoloji

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-teknoloji.json`
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

TOPIC: Genel Teknoloji

CRITERIA:
Bu geniş bir başlık; sadece gerçekten büyük olaylar girsin.
Önemli: dev şirketlerin büyük hamleleri, büyük satın almalar, önemli ürün çıkışları (telefon, araç, platform),
elektrikli araç devlerinin (BYD, Tesla vb.) çığır açan adımları, büyük güvenlik açıkları veya sızıntılar,
büyük düzenlemeler, uzay alanında önemli olaylar.
Önemsiz: sıradan ürün incelemeleri, indirimler, küçük uygulama güncellemeleri, sıradan araba haberleri.
Yapay zeka ve PC donanımı ayrı başlıklarda; burada onlara ancak çok büyükse yer ver.

CANDIDATE ITEMS:
[935] 2026-11-23 23:25 | The Verge (en) | The hacker behind early GTA VI leaks has been sentenced — and released -- Arion Kurtaj, who pleaded guilty last week to hacking Grand Theft Auto VI developer Rockstar Games and other companies, including Uber and Revolut, was sentenced to seven years in prison but released on license, the BBC reports. After hacking Rockstar, Kurtaj leaked 90 videos of GTA VI in 2022,...
[1213] 2026-11-23 21:58 | Ars Technica (en) | Trump halts Iowa detonation, says new governor to blame if it blows accidentally -- The Army said on Sunday that it would carry out the detonation on Wednesday.
[904] 2026-11-23 23:07 | TechCrunch (en) | Figma employee sues, alleging company forced her to confront harassers -- Figma and its CEO are being sued by a current employee for alleged sexual harassment and discrimination.
[1406] 2026-11-23 19:41 | cnBeta (zh) | AI服务器价格争议升级 戴尔、慧与、联想及超微被指在签约后大幅加价 -- 据英国《金融时报》报道，戴尔、慧与、联想和超微等主要服务器厂商，被指在与客户就AI服务器采购价格达成初步协议后，仍在数月内大幅上调最终售价。部分订单的加价幅度据称可达25%、30%，甚至45%，而客户往往只能接受，因为数据中心建设对算力硬件的需求极为迫切。 阅读全文
[824] 2026-11-23 22:19 | 阿波罗新闻网 (zh) | 中共科技部前重大专项司女高官刘敏落马
[715] 2026-11-23 23:11 | foreignpolicy.com (en) | Hacker News 298 puan, 234 yorum | Huang's Return Is the Biggest National Security Scandal in U.S. History
[936] 2026-11-23 23:14 | The Verge (en) | The Verge’s 2026 holiday gift guide -- Gift-giving isn't just about grabbing something solid under a determined spending limit. It's an expression of love, a way to show someone how well you really know them. That doesn't mean you have to do it all on your own, though. Sometimes you just need a push in the right direction to get things...
[1214] 2026-11-23 21:30 | Ars Technica (en) | States sue SpaceX to stop Grok from generating child sex images, revenge porn -- X says every Grok output is screened, but state AGs say Grok is still making CSAM.
[863] 2026-11-23 22:05 | TechCrunch (en) | Anthropic launches Claude Ship, letting AI agents review, fix, and approve code changes
[1407] 2026-11-23 19:40 | cnBeta (zh) | 幼年黑猩猩或许会像人类一样形成“口音” -- 一项新研究发现，幼年黑猩猩的叫声可能会随着成长而逐渐呈现出不同群体的特征，类似人类在成长过程中形成的地区口音。这一发现表明，黑猩猩的发声并非完全由基因决定，社会互动和所处环境也可能在其沟通方式的塑造中发挥重要作用。 阅读全文
[819] 2026-11-23 21:46 | 大紀元 (zh) | 李燕銘：科技部女高官劉敏被查 內幕解析
[716] 2026-11-23 22:32 | stalw.art (en) | Hacker News 428 puan, 221 yorum | Stalwart is now a drop-in replacement for Microsoft Exchange
[937] 2026-11-23 22:38 | The Verge (en) | Ladybird, the first new browser engine in years, is now available to test -- Ladybird, an independent web browser with a built-from-scratch engine, is finally available for testing. On Monday, Ladybird developer Andreas Kling announced that the browser is launching in alpha on macOS and Linux following more than four years of development. The release makes Ladybird the...
[1215] 2026-11-23 20:34 | Ars Technica (en) | Russia will launch a routine ISS supply mission tomorrow—apparently, it's a big secret -- "I would ask you to please reach out to Roscosmos directly."
[905] 2026-11-23 22:02 | TechCrunch (en) | Supertake raises $20M to let AI agents turn your market predictions into stock portfolios -- Think you know where the market is headed? A startup called Supertake lets you put money behind your ideas without having to pick the stocks yourself.
[1408] 2026-11-23 19:40 | cnBeta (zh) | 苹果将重新推出(PRODUCT)RED产品 HomePod mini 2和AirPods Max 2可能性较大 -- 苹果或将重新推出红色特别版产品，据彭博社报道，苹果计划为HomePod mini 2和AirPods Max 2推出(PRODUCT)RED版本。这两款新品预计将成为苹果支持全球基金抗击艾滋病、结核病和疟疾年度筹款活动的一部分。 阅读全文
[821] 2026-11-23 16:36 | FX168财经 (zh) | 特朗普突然宣布重大禁令！谷歌、微软等科技巨头恐受冲击，连白宫幕僚都不知情
[717] 2026-11-23 22:09 | github.com (en) | Hacker News 325 puan, 287 yorum | VP ≠ VNP over Every Field
[938] 2026-11-23 22:25 | The Verge (en) | Trump engineered a risky comeback for Jensen Huang -- After Nvidia's board accepted Jensen Huang's resignation as CEO, following reports that Huang had an affair with an alleged Chinese spy, President Donald Trump, too, appeared to submit to the new reality. Appearing on Stephen A. Smith's radio show shortly after the news came out, Trump said that...
[1216] 2026-11-23 20:11 | Ars Technica (en) | Surveillance pricing uses our personal data to make us pay more -- Personalized pricing can mean unfair, and even biased, prices.
[906] 2026-11-23 21:47 | TechCrunch (en) | Elon Musk and others rejoice as Jensen Huang is reinstated at Nvidia amid scandal -- "The mistake has been corrected," Elon Musk chimed in.
[1409] 2026-11-23 19:38 | cnBeta (zh) | 英伟达董事会恢复黄仁勋职务据悉未事先征询公司高级管理层意见 -- 据知情人士透露，英伟达董事会出人意料地将黄仁勋复职的举动并未事先征询公司高级管理层的意见，后者直到最后一刻才获知该消息。知情人士表示，在美国总统特朗普向董事会成员施加政治压力后，相关决定于周日下午作出。由于讨论属于非公开信息，知情人士要求匿名。他们还表示，一些原本被考虑接替黄仁勋的人选也退出了遴选程序。 阅读全文
[822] 2026-11-23 14:49 | 金融界 (zh) | 燧原科技：若经营不受不可抗力重大影响，预计2027年合并报表盈利，第三季度营收同比增395.34%
[718] 2026-11-23 21:01 | bored.com (en) | Hacker News 227 puan, 50 yorum | Bored.com: Fun, interesting and cool websites to explore when bored
[939] 2026-11-23 22:10 | The Verge (en) | Nvidia CEO Jensen Huang is back. What happens next? -- After the leather-jacketed CEO, president, and co-founder of Nvidia, Jensen Huang, acknowledged an affair with Megaspeed exec Alice Huang, his company initially announced an investigation. However, after The New York Times reported there was evidence of another affair, this time with an Nvidia...
[1217] 2026-11-23 19:38 | Ars Technica (en) | Amazon fights Delivery Protection Act by putting stickers on its packages -- Amazon now uses its packages for political purposes.
[907] 2026-11-23 21:25 | TechCrunch (en) | Megaspeed, the company at the center of the Jensen Huang scandal, appears headed for a US blacklist -- Megaspeed, which was previously led by Alice Huang, would be barred from doing business with U.S. companies unless those companies attained a special license from the federal government.
[1410] 2026-11-23 19:38 | cnBeta (zh) | 黄仁勋重返英伟达总部 -- 援引其获得的一段来自员工的视频报道称，黄仁勋已返回英伟达总部，员工欢呼迎接。黄仁勋对员工表示，第二次担任CEO，他会做得更好。 阅读全文
[818] 2026-11-23 12:13 | 科学网—新闻 (zh) | 科技部原重大专项司司长刘敏被查
[719] 2026-11-23 20:52 | github.com (en) | Hacker News 243 puan, 96 yorum | GPUI is getting its own repository
[940] 2026-11-23 22:09 | The Verge (en) | Welcome to elite impunity, Jensen Huang -- I suppose I must issue a hearty congratulations to Jensen Huang. Huang is CEO of Nvidia again, to the surprise of basically everyone except President Donald Trump and Vice President JD Vance, who made sure it happened. Now, those of us who had some kind of basic education on what a "free market" is...
[1218] 2026-11-23 19:28 | Ars Technica (en) | Reports: Russia may launch nuclear weapon into orbit as soon as next month -- A nuclear detonation in space "could cause rapid and sudden loss or degradation of thousands of satellites."
[908] 2026-11-23 20:38 | TechCrunch (en) | Nvidia employees greet Jensen Huang like a hero as he returns to company’s HQ -- "I'm going to be a better CEO the second time around," Huang told employees.
[1411] 2026-11-23 18:27 | cnBeta (zh) | 黄仁勋复任英伟达CEO后将出席美国参议院听证会 12月3日接受质询 -- 英伟达首席执行官黄仁勋将在12月3日出席美国参议院银行委员会举行的公开听证会。根据该委员会官方网站公布的通知，这场听证会将围绕美国出口管制政策及相关国家安全问题展开。值得注意的是，听证会安排公布之际，黄仁勋刚刚在英伟达董事会决定下重新出任公司首席执行官。 阅读全文
[823] 2026-11-23 12:05 | 同花顺 (zh) | 源杰科技签下重大订单，光芯片的验证信号别读成放量
[720] 2026-11-23 19:17 | arm.jonpalmisc.com (en) | Hacker News 1009 puan, 230 yorum | Per Arm's request, this site will be discontinued on 4 December 2026 :(
[941] 2026-11-23 21:36 | The Verge (en) | The Apple Watch Series 12 is cheaper than ever ahead of Black Friday -- If you’re still sporting an Apple Watch that launched five or so years ago, now is a good time to upgrade. Apple’s latest smartwatch, the 42mm Apple Watch Series 12 with Wi-Fi and GPS connectivity, has dropped to a new all-time low of $299 ($100 off) at Amazon and Walmart, beating its most sale...
[1219] 2026-11-23 15:55 | Ars Technica (en) | Marvel crams every superhero ever into Avengers: Doomsday final trailer -- "All of you have lived stolen lives. But now you must give them back."
[909] 2026-11-23 19:31 | TechCrunch (en) | ‘Outrageous,’ ‘reckless,’ ‘wow’: Here’s what politicians are saying about Jensen Huang’s return to Nvidia -- Politicians on both sides of the aisle seem angry, agitated, and nonplussed by the return of Jensen Huang to Nvidia leadership less than two weeks after his ousting.
[1412] 2026-11-23 17:12 | cnBeta (zh) | 英国湖区救援人员警告：越来越多年轻人因依赖Google地图在山地迷路 -- 英国湖区（Lake District）山地救援人员表示，越来越多年轻人因在山区徒步时依赖手机上的Google地图导航而迷路或陷入险境。瓦兹代尔山地救援队（Wasdale Mountain Rescue Team）在周日完成一次全员出动救援后发出警告，称遇险者中年轻人比例明显上升，其中大多数年龄在18岁至24岁之间。 阅读全文
[820] 2026-11-23 09:37 | 同花顺 (zh) | 恒生科技指数，重大调整！或引发被动基金约12%持仓变动
[721] 2026-11-23 18:48 | github.com (en) | Hacker News 568 puan, 130 yorum | Apple removed my open-source app from EU App Stores after 9 years
[942] 2026-11-23 21:09 | The Verge (en) | AI data centers throw another wrench into chaotic UN climate talks -- The clean energy revolution hinges on electrifying the entire global economy just as power-hungry data centers are surging. It's a challenge delegates from all over the world had to grapple with during the United Nations climate conference, called COP31, that came to a chaotic and unsatisfying...
[1220] 2026-11-23 15:46 | Ars Technica (en) | Jensen Huang reinstated as Nvidia CEO, board chair -- Trump says he spoke to the board and that Huang "did nothing wrong."
[910] 2026-11-23 19:14 | TechCrunch (en) | Google Search’s AI Mode can now tap into Drive and Wallet for more personalized responses -- By bringing Personal Intelligence in AI Mode to Drive, Wallet, and the Gemini app, Google is leveraging the wealth of user data that's already within its ecosystem
[1413] 2026-11-23 17:12 | cnBeta (zh) | 荷兰隐私监管机构呼吁消费者不要购买“偷拍眼镜” -- 荷兰数据保护局近日发出建议，呼吁消费者不要购买具备人工智能功能的摄像眼镜。该机构警告称，这类可联网、可长时间开启的设备不仅会侵犯周围路人的隐私，还可能给佩戴者自身带来安全风险。 阅读全文
[722] 2026-11-23 17:17 | blog.kagi.com (en) | Hacker News 415 puan, 331 yorum | Kagi Mail (public beta)
[943] 2026-11-23 20:56 | The Verge (en) | Apple renews Ted Lasso for season 5 -- One of Apple's biggest shows is sticking around for at least one more season. The streamer announced today that Ted Lasso has been renewed for a fifth season on Apple TV, after what the company calls a "record-breaking season four debut." There are currently no details about the new season nor when...
[1221] 2026-11-23 15:23 | Ars Technica (en) | COP31 ends in failure as countries split over fossil fuels, climate finance -- COP31 ended without a consensus deal for the first time since 2009.
[911] 2026-11-23 18:12 | TechCrunch (en) | Jensen Huang scheduled to testify next week in Senate following reinstatement as Nvidia CEO -- The public hearing follows bipartisan calls for Huang to testify about the affair with the suspected Chinese spy.
[1414] 2026-11-23 16:35 | cnBeta (zh) | 罢免AI巨头CEO不容易：黄仁勋、奥特曼不到两周都回归了 -- 英伟达周一宣布，黄仁勋(Jensen Huang)回归公司担任CEO。《华尔街日报》对此表示，这件事再次证明，想罢免一位AI巨头的CEO并不是一件容易事。在这轮AI热潮中，已有两位最受瞩目的业界CEO被各自的董事会解雇。但是，这两次决定都没能撑过两周。 阅读全文
[723] 2026-11-23 17:07 | twitter.com (en) | Hacker News 1613 puan, 903 yorum | "Dario is hyping the dangers so OSS models are illegal and he stays in control"
[1222] 2026-11-23 13:54 | Ars Technica (en) | F1 in Las Vegas: You've never seen an F1 race like this before -- There were 352 position changes, including 35 for the lead.
[912] 2026-11-23 17:14 | TechCrunch (en) | Amazon snaps up Decart’s AI optimization tech for $7.5B as it prepares to sell Trainium chips -- Amazon is acquiring Decart’s AI optimization technology and research team in a reported $7.5 billion deal.
[1415] 2026-11-23 16:27 | cnBeta (zh) | 特朗普：祝贺黄仁勋恢复职位 英伟达董事会决定正确且恰当 -- 11月23日，美国总统特朗普发文：英伟达董事会作出了让黄仁勋重回首席执行官一职的正确、恰当的决定。国家需要黄仁勋掌舵英伟达，以保障这家公司取得成功、蓬勃发展，同时助力美国经济走向繁荣。美国政府没有理由认为黄仁勋回归英伟达会带来任何国家安全风险。 阅读全文
[724] 2026-11-23 16:13 | ladybird.org (en) | Hacker News 1856 puan, 428 yorum | Ladybird is now in alpha
[1223] 2026-11-23 12:15 | Ars Technica (en) | Tri-screen portable monitors aren’t for the faint of heart -- Three extra screens boost multitasking but can distract and overwhelm.
[913] 2026-11-23 17:13 | TechCrunch (en) | Meta throws out its net-zero goal — its competitors should be furious -- Meta walked back its 2030 net-zero goal in its 2026 sustainability report, a decision that could send ripples throughout the tech industry.
[1416] 2026-11-23 16:22 | cnBeta (zh) | 黄仁勋时隔12天重回英伟达CEO宝座 苹果乔布斯等了12年 -- 英伟达董事会只用了12天，就做出撤销罢免黄仁勋的决定。三年前的同一周，OpenAI撤换山姆・奥特曼，完成同样的复位只用了不到五天。几乎没人预料到黄仁勋复职的消息。周一早晨，就在英伟达发布官方复职公告的几分钟之前，预测市场Polymarket上，押注黄仁勋本月复职的概率仅约3%。公告发布五分钟后，该概率飙升至99%。 阅读全文
[725] 2026-11-23 14:49 | reddit.com (en) | Hacker News 287 puan, 306 yorum | Cyberleek posted the GTA 6 Xbox build executable
[1224] 2026-11-23 12:00 | Ars Technica (en) | Review: The Asus ProArt P16 offers a glimpse of a future powered by local AI -- The new Asus ProArt laptop costs up to $7,000, but it can run a gaggle of local AI agents.
[914] 2026-11-23 16:57 | TechCrunch (en) | Jensen Huang isn’t out of the woods yet -- Tech leaders should be clear-eyed about all the problems likely to come with Huang's reinstatement.
[1417] 2026-11-23 16:22 | cnBeta (zh) | 特朗普对黄仁勋重返英伟达表示祝贺 -- 英伟达周一宣布恢复黄仁勋首席执行官职务。过去数日，美国政府公开表态持续力挺这位 CEO，特朗普对此表示祝贺。英伟达官宣消息发布数分钟后，特朗普在真实社交平台发帖称：“英伟达董事会做出了正确且恰当的决定。美国政府没有理由认为黄仁勋重返英伟达会带来任何国家安全风险。” 阅读全文
[726] 2026-11-23 14:01 | nvidianews.nvidia.com (en) | Hacker News 1708 puan, 4 yorum | Jensen Huang Reinstated as Nvidia CEO
[915] 2026-11-23 16:39 | TechCrunch (en) | Trump reportedly called into Nvidia board meeting to advocate for Huang’s return -- "I spoke to them, let me put it that way … And I convinced them," the president told CNBC.
[1418] 2026-11-23 16:21 | cnBeta (zh) | 黄仁勋复职带动AI相关股票普涨 英伟达市值数小时增逾1万亿美元 -- 英伟达恢复黄仁勋总裁兼首席执行官职务、并立即生效后，人工智能相关公司的股价大涨。英伟达股价一度上涨35%，创2000年以来最大盘中涨幅，短短数小时内市值增加逾1万亿美元，并带动标普500指数和纳斯达克100指数走高。涨势还蔓延至芯片股，费城证券交易所半导体指数飙升13%；其他与人工智能基础设施交易相关的股票也上涨，包括能源供应商和工业企业。 阅读全文
[727] 2026-11-23 14:00 | cnbc.com (en) | Hacker News 2518 puan, 3438 yorum | Nvidia Reinstates Jensen Huang as CEO
[916] 2026-11-23 16:20 | TechCrunch (en) | CrowdStrike to acquire Zscaler for $44 billion -- The cybersecurity giant said that the deal to buy Zscaler, which provides zero-trust products to large enterprises, will better equip their customers to use AI agents.
[1419] 2026-11-23 16:19 | cnBeta (zh) | 英伟达恢复黄仁勋CEO背后：特朗普强势施压相助 -- 英伟达周一宣布，董事会一致投票决定，立即恢复黄仁勋的总裁、首席执行官及董事职务，距他 11 月 12 日辞职仅 11 天。公司称，经过与黄仁勋的一系列讨论和会面，董事会对他的坦诚和领导能力感到满意。董事长哈维·琼斯表示，董事会认为黄仁勋是领导英伟达的合适人选，复职符合股东、员工、合作伙伴和客户的最佳利益。临时 CEO 科莱特·克雷斯留任首席财务官，琼斯继续担任董事长。 阅读全文
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file
[917] 2026-11-23 15:29 | TechCrunch (en) | Dutch privacy authority urges consumers not to buy ‘pervert glasses’ -- The Netherlands' data protection agency said smart wearables violate people's privacy, get information wrong, and can result in unsafe situations.
[1420] 2026-11-23 16:18 | cnBeta (zh) | 英伟达官宣黄仁勋回归 马斯克发文“It is done”庆祝 -- 今日晚间，英伟达官方宣布即日起恢复黄仁勋公司总裁、首席执行官及董事会成员职务。董事会表示，经多轮沟通，认可其坦诚态度与领导能力，并在听取美国政府高层意见后，确信其复职不存在国家安全风险。 阅读全文
[729] 2026-11-23 12:28 | github.com (en) | Hacker News 231 puan, 121 yorum | Win4wine – Run Windows Shell as the Linux Desktop on Wine
[918] 2026-11-23 15:04 | TechCrunch (en) | Trump endorses Jensen Huang’s return to Nvidia, says US has no evidence of wrongdoing -- The president cited the U.S. attorney general in defending Huang's return, while urging Nvidia to continue its export-control investigation.
[1421] 2026-11-23 15:04 | cnBeta (zh) | 三星将继续独家向苹果供货Phone Duo折叠屏 -- 苹果或将在至少未来五年内继续依赖三星显示作为iPhone Duo折叠屏的独家供应商，这一局面可能重演苹果在2017年推出首款OLED iPhone时所面临的供应链依赖问题。 阅读全文
[730] 2026-11-23 11:34 | blogs.gentoo.org (en) | Hacker News 243 puan, 150 yorum | The next circle of security hell
[893] 2026-11-23 14:43 | TechCrunch (en) | Sierra doubles valuation to $30B within six months as demand for AI in CX takes off
[1422] 2026-11-23 14:28 | cnBeta (zh) | 特朗普：祝贺黄仁勋恢复职位 -- 美国总统特朗普在社交媒体发文称，英伟达董事会作出了让黄仁勋重回首席执行官一职的正确、恰当的决定。国家需要黄仁勋掌舵英伟达，以保障这家公司取得成功、蓬勃发展，同时助力美国经济走向繁荣。 阅读全文
[731] 2026-11-23 11:31 | github.com (en) | Hacker News 308 puan, 172 yorum | Nospeaker ─ Make your MacBook's trackpad talk using its haptic motor
[919] 2026-11-23 14:21 | TechCrunch (en) | Nvidia reverses course and brings Jensen Huang back as CEO -- The stunning reversal was supported by senior officials in the U.S. government.
[1423] 2026-11-23 14:12 | cnBeta (zh) | 黄仁勋恢复CEO职务 英伟达美股盘前涨超25% -- 11月23日，英伟达官网公告称，公司董事会已一致投票决定，即日起恢复黄仁勋的总裁、首席执行官及董事职务。英伟达表示，董事会经过与黄仁勋多轮讨论和会议，对其坦诚程度及领导公司能力表示认可，并认为恢复其职务符合股东、员工、合作伙伴及客户的最佳利益。 阅读全文
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[920] 2026-11-23 14:00 | TechCrunch (en) | Uber now lets you book rides that record the drive by default -- The option, which uses the front camera of the Uber driver's phone to record the entire drive, will show up as "Recorded Rides" in the app.
[1424] 2026-11-23 14:11 | cnBeta (zh) | 黄仁勋回应回复职务：已经迫不及待重返工作岗位 -- 英伟达今日宣布，即刻生效，黄仁勋恢复担任公司总裁、首席执行官及董事会成员。 董事会经与黄仁勋多轮沟通会晤后，一致投票同意恢复其职务；董事会认可黄仁勋态度坦诚，具备领导英伟达的能力。在作出该决定的过程中，董事会听取了美国政府高层的意见。基于相关意见，董事会确信，黄仁勋复职不会带来国家安全风险。 阅读全文
[733] 2026-11-23 09:55 | jola.dev (en) | Hacker News 172 puan, 109 yorum | Don't keep your SSH keys on disk
[921] 2026-11-23 01:25 | TechCrunch (en) | How Notable Capital, GIC, and Baillie Gifford bet big on Anthropic before its IPO -- Three significant backers share what gave them conviction to bet billions on Anthropic.
[1425] 2026-11-23 14:02 | cnBeta (zh) | NVIDIA董事会宣布黄仁勋即刻恢复总裁、首席执行官及董事职务 -- 英伟达（NVIDIA）今日宣布，黄仁勋（Jensen Huang）自即日起恢复担任公司总裁兼首席执行官，并重新出任董事会成员。董事会一致投票通过这一决定，认为黄仁勋具备坦诚沟通和领导英伟达的能力，其复职符合公司股东、员工、合作伙伴及客户的最佳利益。 阅读全文
[734] 2026-11-23 05:00 | micjm.com (en) | Hacker News 263 puan, 340 yorum | Yes, 17M lines of Lean is a lot
[1426] 2026-11-23 13:59 | cnBeta (zh) | 古尔曼：苹果Siri裁员不影响AI项目 智能体要等到2027年底 -- 知名科技记者马克·古尔曼表示，苹果近期针对Siri的裁员，主要裁撤负责语音助手旧底层架构的员工，新一代AI系统研发团队并未受到波及。古尔曼表示，尽管苹果力求加快开发节奏、把重大更新分散到全年推送，但员工尚未感受到公司运转模式有明显变化。他还提到，苹果正面临激烈竞争：其他AI厂商迭代模型与服务的频率远高于苹果一年一度的操作系统更新周期。 阅读全文
[735] 2026-11-23 04:31 | facebook.com (en) | Hacker News 218 puan, 80 yorum | DeFlock Amerca Facebook Group Suspended as a Dangerous Organization
[1427] 2026-11-23 13:36 | cnBeta (zh) | 美国两州起诉SpaceX旗下Grok：指其未防滥用生成儿童性虐待材料 -- 据路透社报道，美国弗吉尼亚州和俄勒冈州已对埃隆·马斯克旗下SpaceX Super Intelligence提起诉讼，指控该公司从Grok图像生成功能中获利，却未设置充分的安全防护，导致其可能被用于制作儿童性虐待材料及其他未经当事人同意的性化图像。 阅读全文
[736] 2026-11-23 03:49 | github.com (en) | Hacker News 609 puan, 245 yorum | XNU Linux: Full OS X GUI Userspace on Linux
[1428] 2026-11-23 13:36 | cnBeta (zh) | Telegram创始人嘲讽《GTA6》 主角犯罪连个群都没有 -- Telegram创始人兼CEO帕维尔·杜罗夫近日在社交媒体上发推，吐槽了《GTA6》。杜罗夫认为，《GTA6》中最不真实的地方在于，游戏主角能够经营犯罪活动，却连一个Telegram群都没有。 阅读全文
[737] 2026-11-23 03:34 | docker.com (en) | Hacker News 123 puan, 202 yorum | The New Docker: Built to Run on Trust
[1429] 2026-11-23 13:36 | cnBeta (zh) | 荷兰地方法院裁定比亚迪Ti7在欧盟禁售禁产 -- 11月23日，针对比亚迪Ti7车型被禁止在欧盟销售，比亚迪方面回应称，对该判决持有强烈异议，并将依法提起上诉。Ti7车型系比亚迪自主独立设计成果，未侵犯捷豹路虎的任何权利。比亚迪将通过相关法律途径，坚决维护自身合法权益。 阅读全文
[738] 2026-11-23 01:50 | pengowray.github.io (en) | Hacker News 192 puan, 76 yorum | Treemap of the Riemann Hypothesis Lean Proof
[1430] 2026-11-23 13:33 | cnBeta (zh) | 基于原生WinUI 3 的全新Windows 11搜索界面已向普通用户推送 -- 微软在11月的可选更新KB5132057中，逐步向普通Windows 11电脑推出了原生WinUI 3搜索体验。新搜索采用更紧凑的界面设计，摆脱了此前基于WebView2的旧版搜索，在测试中仅占用约26.3MB内存，整体体验明显更轻量、更现代化。 阅读全文
[739] 2026-11-23 01:13 | spelunkr.tv (en) | Hacker News 990 puan, 170 yorum | Free alternative TV network built from the Internet Archive's forgotten videos
[1431] 2026-11-23 13:27 | cnBeta (zh) | 苹果或将调整新品发布节奏 2027年年初集中推出平价产品 -- 苹果或将调整新品发布节奏，2027年年初集中推出iPhone 18e、新款入门iPad和MacBook Neo等平价产品。据彭博社记者马克·古尔曼的消息，苹果计划在明年2月至3月间展开一轮以中低端设备为重点的发布潮。 阅读全文
[740] 2026-11-22 23:57 | alvinbunk.wordpress.com (en) | Hacker News 306 puan, 322 yorum | Nespresso's project to reduce grams of coffee per capsule
[1432] 2026-11-23 13:25 | cnBeta (zh) | 新增超广角镜头带来空间瓶颈 iPhone Air 2或不会采用更小的灵动岛 -- 根据近期曝光的机模照片及业内分析，下一代iPhone Air 2可能仍将沿用初代机型的灵动岛设计，而不会像iPhone 18 Pro那样采用尺寸更小的灵动岛。尽管该机型预计将新增一颗后置超广角镜头，但机身内部空间可能不足以支持更小的前置摄像头开孔布局。 阅读全文
[741] 2026-11-22 23:12 | claude.ai (en) | Hacker News 257 puan, 201 yorum | Progress map for the Lean formalization of Alessandro Masullo's manuscript
[1433] 2026-11-23 13:24 | cnBeta (zh) | 伯克希尔·哈撒韦停止减持苹果股票 占其投资组合的四分之一 -- 伯克希尔·哈撒韦自2026年初新任首席执行官格雷格·阿贝尔上任以来，已停止出售苹果股票。目前，苹果在该公司股票投资组合中所占比例达到25%，成为其最重要的持仓之一。 阅读全文
[742] 2026-11-22 23:06 | gl-inet.com (en) | Hacker News 269 puan, 125 yorum | GL.iNet Security Notice 2026-11-20
[1434] 2026-11-23 13:22 | cnBeta (zh) | 汽车智能化程度不断提高也让维修变得更贵、更慢 -- 汽车智能化程度不断提高，正在让维修变得更贵、更慢，并间接推高保险费用。一项新调查显示，美国独立维修店因获取和使用汽车制造商的维修软件而损失的时间，每年价值约120亿美元。 阅读全文
[743] 2026-11-22 22:10 | ias.edu (en) | Hacker News 179 puan, 42 yorum | The Usefulness of Useless Knowledge [pdf] (1939)
[1435] 2026-11-23 13:21 | cnBeta (zh) | 摩托罗拉新款防丢标签Moto Tag 3已提前在亚马逊上架并接受预订 -- 摩托罗拉尚未正式发布的新款防丢器 Moto Tag 3，近日已在亚马逊英国站上架并接受预订。该页面自周五起便已上线，显示产品采用“Pantone Lucent White”配色，售价为24.96英镑，约合33美元，预计发货时间为1至4个月。 阅读全文
[744] 2026-11-22 21:53 | pequalnp.com (en) | Hacker News 194 puan, 170 yorum | Agent swarm trying to solve P vs. NP
[1436] 2026-11-23 12:49 | cnBeta (zh) | 拒绝英伟达CEO职位邀请？Arm首席执行官将合同延长至2033年 -- 芯片设计公司Arm Holdings首席执行官雷内·哈斯（Rene Haas）此前曾被接洽，作为英伟达首席执行官黄仁勋的潜在继任者。如今，哈斯已延长合同，将继续领导Arm直至2033年。 阅读全文
[745] 2026-11-22 21:33 | fosscomics.com (en) | Hacker News 169 puan, 76 yorum | The struggle for freedom: How BSD escaped AT&T licensing
[1437] 2026-11-23 12:12 | cnBeta (zh) | 国产“筷子夹火箭”迎重大突破：宇石空间自研百吨级捕获臂完成验证 -- 11月23日，宇石空间宣布，其自主研制的百吨级全尺寸“筷子”捕获臂已完成联合地面验证试验。据介绍，从试验平台搭建到试验结束历时100余天，团队围绕火箭高可靠回收开展全天候苛刻工况考核，全面覆盖火箭回收任务剖面，验证了捕获臂全天候、全工况、满负载的作业能力，以及动载工况下的结构稳定性。 阅读全文
[746] 2026-11-22 21:01 | gpupartner.com (en) | Hacker News 755 puan, 306 yorum | A Beginner's Guide to Local AI with Inkling, Qwen, and Unsloth
[1438] 2026-11-23 12:11 | cnBeta (zh) | 植入了Neuralink脑机的患者仅凭意念畅玩《GTA VI》 线上演示如何操控游戏 -- Neuralink再次展示其脑机接口技术的应用潜力：一名参与临床试验的患者仅凭大脑意念，成功操控Rockstar Games的新作《侠盗猎车手VI》（GTA VI）。该公司于11月20日在社交平台X发布了一段约两分钟的游戏视频，此时距离《GTA VI》登陆主机仅过去一天。 阅读全文
[747] 2026-11-22 20:33 | smallsf.com (en) | Hacker News 373 puan, 114 yorum | A miniature San Francisco with live buses, planes and boats
[1439] 2026-11-23 12:10 | cnBeta (zh) | 研究发现新一代AI模型更爱“爆粗口” -- 据人工智能分析公司Vals发布的报告，2026年7月之后训练的大型语言模型出现粗俗语言的频率明显高于早期模型；这些脏话几乎都出现在模型的“思维链”中，也就是模型在给出最终答案前写下的逐步推理内容，通常不会直接展示给用户。 阅读全文
[748] 2026-11-22 20:15 | nyx.net (en) | Hacker News 160 puan, 39 yorum | Nyx, the Oldest Internet Service Provider
[1440] 2026-11-23 12:08 | cnBeta (zh) | 英特尔Bartlett Lake-S处理器据称已可在MSI Z690和Z790主板原生启动 -- 英特尔Bartlett Lake-S处理器据称可在MSI Z690和Z790主板原生启动。此前测试者需要修改BIOS才能让这类纯性能核处理器运行，但最新报告显示，只需更新MSI最新BIOS，部分主板即可直接支持该系列处理器。 阅读全文
[749] 2026-11-22 19:51 | nytimes.com (en) | Hacker News 124 puan, 54 yorum | All Delta Flights Through Atlanta Delayed as Automated System Falters
[1441] 2026-11-23 12:08 | cnBeta (zh) | 全球土壤价值约325万亿美元 却仍长期缺乏专门国际条约保护 -- 科学家警告称，覆盖地球表面的薄薄一层土壤，支撑着人类绝大多数食物生产，同时过滤水源、储存大量碳并维系无数生命。然而，尽管土壤对粮食安全、气候稳定和生态系统至关重要，目前仍没有一项专门、具有约束力的国际条约来保护它。 阅读全文
[750] 2026-11-22 19:41 | danielsada.tech (en) | Hacker News 314 puan, 162 yorum | Assume People Are Not Interested in Your Blog Post
[1442] 2026-11-23 12:07 | cnBeta (zh) | 恒星“吃掉”同类过程首次曝光，为理解紧密恒星系统形成提供重要依据 -- 据《自然》杂志19日发表的一项最新研究，天文学家利用詹姆斯·韦布空间望远镜，首次直接追踪到恒星“吃掉”同类过程：两颗恒星旋近进入共有包层阶段，随后剧烈爆发。这一名为AT 2025abao的事件，为理解紧密恒星系统的形成与其他能量剧烈的天文事件提供了重要依据。 阅读全文
[751] 2026-11-22 19:20 | oreilly.com (en) | Hacker News 807 puan, 450 yorum | What Happened to Silicon Valley?
[1443] 2026-11-23 12:06 | cnBeta (zh) | BepiColombo探测器即将抵达水星 ESA与JAXA将于11月27日举行媒体简报会 -- 欧洲空间局（ESA）与日本宇宙航空研究开发机构（JAXA）宣布，将于11月27日在伦敦科学博物馆及线上同步举行混合媒体简报会，介绍贝皮科伦坡（BepiColombo）水星探测任务的最新进展及后续计划。该简报会定于格林尼治时间上午9时30分至11时举行，即欧洲中部时间上午10时30分至12时。 阅读全文
[752] 2026-11-22 19:10 | niemanlab.org (en) | Hacker News 126 puan, 130 yorum | Canada recommits to federal funding for news
[1444] 2026-11-23 12:05 | cnBeta (zh) | 微软计划从2027年开始将Wi-Fi驱动移出Windows内核 -- 微软目前正在计划从 2027 年开始将 WiFi 驱动程序移出 Windows 11 内核，即改成用户模式运行。微软的目的是减少 WiFi 驱动程序引起的黑屏死机问题，这项改进预计会在 2027 年发布的 Windows 11 中推出。 阅读全文
[753] 2026-11-22 19:10 | audubon.org (en) | Hacker News 631 puan, 367 yorum | The Birds of GTA 6: A Field Guide to Vice City and Leonida's Wildlife
[1445] 2026-11-23 11:58 | cnBeta (zh) | RTX6090新消息：最快明年底登场 比5090更贵 -- 近日据Moore's Law Is Dead透露， NVIDIA下一代旗舰游戏显卡RTX 6090可能在2027年底至2028年初推出，预计售价2500至3000美元(快科技注：约20033元人民币)，现款旗舰RTX 5090据称计划在2027年结束生产。 阅读全文
[1446] 2026-11-23 11:58 | cnBeta (zh) | 极速冲击532km/h 科尼塞克要从比亚迪手里夺回世界第一快 -- 本月初，仰望U9 Xtreme在阿布扎比跑出503.17km/h，拿下量产车极速世界纪录！ 近日，老牌超跑厂商科尼塞克宣布，旗下Jesko Absolut仍将发起极速挑战，目标冲击330英里/小时（约532km/h），意图重新夺回全球量产车极速王冠。 阅读全文
[1447] 2026-11-23 11:57 | cnBeta (zh) | 《GTA6》偷“特斯拉”被自动驾驶送进警局 给马斯克看笑了 -- 据报道，《GTA6》上市后，玩家发现游戏内一款名为Coil Raiden的电动车具备特殊防盗机制。该车外形与早期Model S颇为相似，被玩家称为游戏里的“特斯拉”。 阅读全文
[1448] 2026-11-23 11:50 | cnBeta (zh) | 日本汽车照明设备厂商希望到2030财年将车灯制造成本降低约30% -- 日本汽车照明设备厂商小糸制作所正加快调整产品设计与生产方式，希望到2030财年将车灯制造成本降低约30%，以应对来自中国供应商的竞争。 让这家日本老牌车灯企业感到压力的，是中国同行的价格。目前部分中国厂商的车灯产品比小糸便宜约三成，小糸副社长内山正巳坦言，中国的价格已经成为行业对标标准。 阅读全文
[1449] 2026-11-23 11:49 | cnBeta (zh) | 智谱发布GLM-5.4：用Loop Transformer循环28层 主攻泛Coding -- 11月23日，智谱正式发布GLM-5.4，这是其首代采用Loop Transformer架构的旗舰模型。官方称，该模型在参数量不变的情况下，通过让一组共享权重的层循环执行，获得更深的推理能力，智能水平达到2-3T级模型效果。 阅读全文
[1450] 2026-11-23 11:48 | cnBeta (zh) | CrowdStrike将以440亿美元收购云安全公司Zscaler -- 美国科技集团CrowdStrike已同意以约440亿美元的价格收购云安全公司Zscaler。这笔交易预计将缔造一家网络安全巨头。此次全股票收购正值全球企业和政府面临新一轮技术系统故障之际。近几周，包括微软和美国联邦航空管理局（FAA）在内的多个机构的在线系统因技术故障而瘫痪。 阅读全文
[1451] 2026-11-23 11:44 | cnBeta (zh) | 测试内容大调整？懂车帝：信息多处失实，部分内容为AI撰写 -- 新浪科技讯 11月23日晚间消息，近日有博主爆料称，懂车帝内部对公开测试发布了四项要求，包括不再开放对比类内容、想要发布公开测试类内容的账号必须有资质等。懂车帝方面则表示，网传“内容大调整”“行业受影响”等信息多处失实，部分内容为AI撰写，未跟平台核实求证。 阅读全文
[1452] 2026-11-23 09:53 | cnBeta (zh) | 媒体：黄仁勋夫妇已积累巨额财富 -- 据报道，黄仁勋与妻子洛丽（Lori）在英伟达股价大幅上涨前，就已将英伟达股票捐赠至不可撤销信托。创始人常会采用该方式，把股票后续增值部分剥离出自身遗产，以此规避赠与税与遗产税。纽约遗产规划律师乔纳森·布拉特马赫表示：“这是最常见的遗产规划策略。在资产价值较低时完成赠与，期待其后续大幅升值。这样一来，增值收益就不会被计入你的遗产范围。” 阅读全文
[1453] 2026-11-23 09:08 | cnBeta (zh) | 黑莓推出Windows安全浏览器 让企业可在员工个人电脑上安全办公 -- 黑莓公司近日面向Windows平台推出全新企业级浏览器BlackBerry Access Secure Browser，旨在让员工能够在个人电脑上安全处理工作事务，同时避免企业完全接管整台设备。该产品基于Chromium开发，将黑莓的数据保护能力引入浏览器层面，并加入了此前已登陆iOS和Android平台的BlackBerry Access产品线，同时取代旧版BlackBerry Access桌面客户端。 阅读全文
[1454] 2026-11-23 09:04 | cnBeta (zh) | 传懂车帝开始大调整：不能做对比测试、没资质也不行等 -- 今天有行业博主发帖爆料，汽车内容平台懂车帝接下来可能要针对内容做大调整。按照博主晒出的最新内部通知要求，平台接下来将全面收紧内容发布的权限门槛，此前可以随意做的多车横向对比测试，后续直接被划入了受限范畴，不再支持无资质的个人或者机构发布这类内容。 阅读全文
[1455] 2026-11-23 09:03 | cnBeta (zh) | 黄牛本想靠iPhone Duo大赚一笔 结果血亏20万 -- 微博话题“iPhone Duo破发”引发网友热议。 有数码城老板向记者透露，今年不少囤货的黄牛损失惨重，部分人亏损超过20万元。由于市场行情持续下跌，不少黄牛陷入“一边收货、一边跌价”的困境。总而言之，首批高价囤货的商家亏损严重。 阅读全文
[1456] 2026-11-23 09:02 | cnBeta (zh) | 达美航空周日全美超1600架次延误 重量平衡系统出现故障 -- 达美航空称，周日全美范围内超过1600架次达美航班出现延误，故障原因为一套飞机重量平衡系统中断。本次设备故障恰逢感恩节出行高峰，数百万民众计划本周出行；就在几天前，美国联邦航空管理局（FAA）因设备安装问题引发通信故障，发布了“9・11”事件以来第二次全国范围地面停飞指令。 阅读全文
[1457] 2026-11-23 08:58 | cnBeta (zh) | 比特币连跌七周 ETF资金加速撤离令市场雪上加霜 -- 投资者正加速从美国上市的比特币ETF撤资，使持续承压的比特币失去一大关键支撑。数据显示，上周12只ETF资金净流出约18亿美元，创6月以来最大单周流出规模，并连续第四周遭遇资金净流出。 阅读全文
[1458] 2026-11-23 08:57 | cnBeta (zh) | 特朗普出席迷因币持有者晚宴 币价已较峰值暴跌98% -- Morten Christensen第一次通过交易获得参加美国总统唐纳德·特朗普晚宴的资格，花了大约1200美元。第二次花了约500美元。而这一次，他说自己最终反倒赚了6美元。为了获得入场资格，Christensen买入特朗普迷因币，同时另行押注币价下跌，以便从跌势中获利。前两次活动，他采用的也是这套策略。 阅读全文
[1459] 2026-11-23 08:36 | cnBeta (zh) | 36.7万参数模型装进一张1.44MB软盘 开发者让1990年代老电脑也能跑AI -- 面向追求响应速度、低延迟与更强隐私控制用户的 Windows 11 修改版操作系统 AtlasOS，在时隔一年后推出全新版本 Atlas 0.6。此次更新引入了全新的 Atlas Manager 应用，使用户无需重新安装 Windows，即可从旧版本升级到最新系统，同时大幅简化了安装介质制作流程。 阅读全文
[1460] 2026-11-23 08:23 | cnBeta (zh) | 主打高性能与低延迟的Windows 11替代方案AtlasOS发布重大更新 -- 面向追求响应速度、低延迟与更强隐私控制用户的 Windows 11 修改版操作系统 AtlasOS，在时隔一年后推出全新版本 Atlas 0.6。此次更新引入了全新的 Atlas Manager 应用，使用户无需重新安装 Windows，即可从旧版本升级到最新系统，同时大幅简化了安装介质制作流程。 阅读全文
[1461] 2026-11-23 08:19 | cnBeta (zh) | Docker更换新的品牌形象 由可爱的彩色鲸鱼变成黑色抽象鲸鱼 -- 知名容器平台 Docker 近期发布新的品牌形象并重新设计鲸鱼吉祥物 Moby，该公司称新的品牌形象主要面向 AI 智能体时代，重点转向可信软件供应链、智能体运行隔离和企业治理方案，现有容器产品将继续发展。 阅读全文
[1462] 2026-11-23 08:17 | cnBeta (zh) | 发售没几天，GTA6就在谣言中成了刀工龟男模拟器 -- 说实话，现在已经是一个后《GTA6》时代了。作为一个发售日当天MTC开分99分，目前已官宣达到三千万销量的现象级大作，这两天说从没刷到《GTA6》的相关消息，我都有点不信。而《GTA6》能够在玩家圈子里引起怎样程度的狂欢和热度，自然不用我多说——小到日版游戏里男主杰森洗澡还要穿条内裤的截图，“大”对剧情里突然出现的比基尼辣妹曼蒂，每条话题单拿出来都至少是几百万浏览量的大热点。 阅读全文
[1463] 2026-11-23 08:16 | cnBeta (zh) | Grok Bot正在接管SpaceX财务 5个bot替代十余个岗位 -- SpaceX旗下AI部门SpaceXSI已将五个Grok Bot部署至财务团队担任全职岗位，完成原本需要10至15名新员工才能覆盖的工作量。马斯克于11月17日在X平台公开为Grok Bot站台，称其“可以为企业节省数百万美元开支”，并表示该Bot将研究每一笔支出、评估其必要性，并通过互联网或直接致电供应商寻找更优报价。同日，Grok Bot官方账号发文确认，五个Bot已在SpaceX财务团队中担任全职角色。 阅读全文
[1464] 2026-11-23 08:13 | cnBeta (zh) | AI行业被美人计盯上 黄仁勋不会是最后一个 -- 黄仁勋辞职风波持续发酵的同时，AI行业还在有更多人正面临风险。随着AI芯片、模型和算力基础设施的商业价值持续攀升，安全专家警告， AI企业正面临越来越复杂的社交渗透风险，攻击者不一定需要破解公司网络，也可能通过结识高管和员工获取内部信息。 阅读全文
[1465] 2026-11-23 08:12 | cnBeta (zh) | 西班牙一特斯拉超充站充电线全被剪走 窃贼留下一个充电头挑衅 -- 近日， 西班牙阿尔瓦塞特一座尚未投入运营的特斯拉超级充电站遭遇盗窃，站点内所有充电桩的充电线缆被人剪断偷走，整座充电站直接瘫痪，原定开业计划被迫延后 。超充电缆含大量高纯铜，单根线缆价值不菲，对于窃贼吸引力很强，他们偷盗铜芯后，往往当作废金属变卖获利。 阅读全文
[1466] 2026-11-23 08:12 | cnBeta (zh) | 加密货币窃贼利用网络广告攻击iPhone 用户点击一次即可感染恶意软件 -- 网络安全公司Confiant的最新研究显示，一场大规模恶意广告（Malvertising）活动正利用网络广告入侵iPhone，窃取加密货币钱包助记词。与通常需要用户下载文件或在点击链接后按照进一步指示操作的传统攻击手法相比，这种方式更为先进。 阅读全文
[1467] 2026-11-23 08:10 | cnBeta (zh) | 韩国教育部长辞职 称自己年事已高难以应对AI考试作弊问题 -- 韩国教育部长崔教振（Choi Kyo-jin）辞职。此前，韩国政府在采取措施打击该国竞争激烈的大学入学考试中利用人工智能（AI）作弊的行为时陷入混乱。 阅读全文
[1468] 2026-11-23 08:10 | cnBeta (zh) | 伯恩斯坦：SpaceX领先竞争对手约十年 星舰首次成功回收上面级成为关键突破 -- 11月23日，伯恩斯坦在最新报告中指出，SpaceX在11月14日举行的星舰第15次试飞中，首次成功利用发射塔机械臂回收火箭上面级，标志着其完全可重复使用运载系统取得关键技术突破。该机构认为，SpaceX在火箭回收及重复使用技术方面已领先其他竞争对手约十年，并重申对其股票的“跑赢大盘”评级。 阅读全文
[1469] 2026-11-23 08:08 | cnBeta (zh) | AI五至十年内攻克癌症？诺华首席执行官：不太可能 -- 诺华（Novartis）首席执行官、Anthropic董事会成员瓦斯·纳拉辛汉（Vas Narasimhan）驳斥了人工智能将在未来五至十年内攻克癌症的观点。他认为，即使经过十年发展，人工智能也只能将药物研发周期缩短四年。 阅读全文
[1470] 2026-11-23 08:07 | cnBeta (zh) | Shopify CEO：黄仁勋离职事件系“精心策划” 英伟达董事会是“一群蠢货” -- Shopify首席执行官托比·吕特克（Tobi Lütke）就英伟达董事会处理黄仁勋辞职事件的方式发表了尖锐批评。在周日发布的一期大卫·森拉（David Senra）播客节目中，吕特克质疑黄仁勋离职前一系列指控出现的先后顺序，认为这些事件的安排过于巧合，难以用偶然来解释。 阅读全文
[1471] 2026-11-23 07:05 | cnBeta (zh) | 英伟达股价三周暴跌42% 游戏玩家正密切关注 -- 自10月29日《纽约时报》报道英伟达CEO黄仁勋可能成为情报机构的目标以来，英伟达股价已下跌近42%。11月20日（周五），英伟达股价收于125.53美元，低于10月28日报道发布前的215.38美元。短短三周多时间，股价就蒸发了近90美元。 阅读全文
[1472] 2026-11-23 07:01 | cnBeta (zh) | 《拓麻歌子》与《数码宝贝》推出30周年互换养成玩具 -- 《拓麻歌子》与《数码宝贝》围绕30周年纪念推出互换养成对象的玩具，目前已经开放预订。两款产品将各自体系中的养成角色进行交换。 阅读全文
[1473] 2026-11-23 06:57 | cnBeta (zh) | 苹果一大波低价新品即将来袭 就在明年2、3月 -- 彭博社知名苹果记者马克·古尔曼(Mark Gurman)周日透露，苹果公司计划于明年2月至3月发布 iPhone 18e、iPhone 18、新款入门级iPad，以及新款MacBook Neo和MacBook Air。 古尔曼称，苹果正为明年初的iOS 27.4发布周期准备一系列新产品。就新品发布而言，这可能是苹果最繁忙的秋季之一。iPhone Duo 在今年10月底发售，触屏版MacBook Pro、HomeView和新版Apple TV均在11月推出。 阅读全文
[1474] 2026-11-23 06:57 | cnBeta (zh) | AI时代比拼“周更” 苹果12个月模式恐输掉智能体之战 -- 11月23日，据彭博社报道，AI智能体能够代表用户执行任务，正逐渐成为科技行业争夺的下一个重要生态系统战场。然而，尽管苹果拥有先天优势，但由于产品开发方式过时，该公司已面临输掉这场战役的严重风险。 阅读全文
[1475] 2026-11-23 06:56 | cnBeta (zh) | 算力疯狂扩产埋隐患？马斯克警告：明年AI行业或现严重“资金荒” -- SpaceX首席执行官兼特斯拉首席执行官、全球首富马斯克最新表示，随着科技公司竞相建设数据中心并扩大计算能力，人工智能（AI）行业可能在2027年面临严重的资金短缺。马斯克是在回应一篇帖子时作出了上述警告。他还表示，挑战不仅在于购买昂贵的芯片和建设设施，更在于如何尽快让这些设施投入运营并开始产生收入。 阅读全文
[1476] 2026-11-23 06:56 | cnBeta (zh) | 马斯克把GTA 6搬上特斯拉 FSD端到端实时生世界 一脚油开进3A游戏 -- 马斯克把“现实版GTA”搬上车了！这个周末，特斯拉正式推送新一代FSD v15，端到端驾驶神经网络迎来全新升级。FSD v15最出圈的一个功能：Reality View，直接让全网车主和AI圈集体沸腾了。中控屏上，周围的高楼、路边的树、过马路的行人、迎面的车，光影自然得像一款3A游戏过场画面。 阅读全文
[1477] 2026-11-23 06:55 | cnBeta (zh) | 微软准备放假 Windows 11下月暂停更新 -- 微软已经确认，Windows 11将在2026年12月暂停发布每月下旬的可选非安全预览更新，正常更新节奏将在2027年1月恢复。 微软在Windows消息中心解释，临近圣诞节和新年假期，相关团队将减少日常维护工作，因此12月下半月不再安排非安全预览更新。 阅读全文
[1478] 2026-11-23 06:55 | cnBeta (zh) | 杨奇谈《GTA 6》：用“工业化”形容是一种贬损 更像昂贵且低效的艺术品 -- 11月23日，游戏科学联合创始人、《黑神话：悟空》美术总监杨奇近日在微博回复网友提问，分享了自己游玩《GTA 6》后的直观感受。 在他看来，用“工业化”来形容《GTA 6》其实是一种贬损，这款游戏本质上是无数日夜与灵感的整合，更像是一件昂贵且低效的艺术品。 阅读全文
[1479] 2026-11-23 06:54 | cnBeta (zh) | 中国大模型调用量连续30周第一 DeepSeek霸榜五周 -- 11月23日，大模型API聚合平台OpenRouter公布了最新一周的模型调用数据，在11月16日至22日这一周，全球大模型总调用量为210万亿Token，相比上周的194万亿增长8.25%。其中，中国大模型周Token处理量已经连续第30周超过美国，居全球首位，继续领跑这一平台的模型调用榜单。但相比上周，中国模型的调用量有所下滑。 阅读全文
[1480] 2026-11-23 06:54 | cnBeta (zh) | 中国车企在39个国家新车份额超过日本车企 -- 日经中文网报道，美国调查公司Mobility Global的数据显示，全球已有39个国家，中国汽车制造商的新车销售份额超过日本汽车制造商。该统计包含150个国家和地区，汇总的是1至9月的销量（不含部分大型车辆），并对其中的93个可确认各厂商销量的国家进行了份额比较。中国汽车在挪威、瑞典、新加坡等电动化转型较快的国家，以及伊朗、俄罗斯等市场占据优势。 阅读全文
[1481] 2026-11-23 06:54 | cnBeta (zh) | 泰国网民对比全球空调产量 中国绕图一圈半 -- 11月12日，一名泰国网民发布了一个关于全球空调产量对比的帖子。帖子中是各国空调产品对比，第一名毫不意外地是中国，第二名是让人有点意外的泰国。 阅读全文
[1482] 2026-11-23 06:53 | cnBeta (zh) | 韩国高考出了一道中国历史题引热议：考生基本没人会 包含AI -- 今年韩国高考世界历史科目里的一道题目引发了全国性的巨大争议，这道题的考生作答错误率超过90%，几乎把绝大多数考生都考倒了。不少考完的学生集体质疑题目设置本身存在误导性偏差，甚至不少人上传了测试结果，说市面上主流的人工智能大模型给出的答案，都和韩国教育课程评价院事后公布的官方标准答案不一样，普遍认为这道题的题干引导本身就存在问题。 阅读全文
[1483] 2026-11-23 06:52 | cnBeta (zh) | M9车主开辅助驾驶熟睡引热议 账号已全网搜不到 -- 近日一段网络流传的行车记录仪视频引发全网哗然，一名疑似问界M9的车主在高速路段开启辅助驾驶功能之后，直接躺在主驾位置上完全熟睡，车辆仍保持上百公里的时速在公路上自主高速行驶，整个过程车主完全没有接管操作。这段危险操作的视频被发上网之后瞬间引发大量网友热议，相关话题问界M9车主开辅助驾驶熟睡直接冲上了微博热搜榜前列。 阅读全文
[1484] 2026-11-23 06:52 | cnBeta (zh) | AMD数据中心业务2027年冲80%营收占比 -- AMD近日对外公布长期业务转型规划，计划到2027年让数据中心业务占总营收达到80%。这意味着企业资源会大规模倾斜AI算力业务，消费端硬件的资源分配或将受到挤压。 阅读全文
[1485] 2026-11-23 06:52 | cnBeta (zh) | 特朗普再喊话：希望英伟达董事会重新聘用黄仁勋 -- 11月23日，美国总统特朗普当地时间11月22日在社交平台Truth Social上表示，希望英伟达董事会重新聘用黄仁勋，这对英伟达、对美国而言都将是最优选择。 阅读全文
[1486] 2026-11-23 06:49 | cnBeta (zh) | 黄仁勋夫妇财富版图曝光：通过信托控制8.7亿股英伟达股票 妻子洛丽为核心人物 -- 11月23日，《华尔街日报》发文，拆解了黄仁勋(Jensen Huang)及其妻子洛丽(Lori)的财富版图。文章称，黄仁勋夫妇通过信托及其他实体控制着8.7亿股英伟达股票，价值超过1090亿美元。 阅读全文
[1487] 2026-11-23 06:44 | cnBeta (zh) | 微软总裁警告：未来数月或将出现更多技术宕机风险 -- 据美国《新闻周刊》、哥伦比亚广播公司（CBS）等多家美媒报道，美国微软公司总裁布拉德·史密斯当地时间22日警告称，随着各机构加紧修补软件漏洞、升级老旧基础设施，未来数月出现大规模技术宕机风险或将持续处于高位。 阅读全文
[1488] 2026-11-23 06:43 | cnBeta (zh) | 贝森特“改口”：黄仁勋可能“不知情” -- 美国财政部长贝森特暗示未排除黄仁勋回归英伟达的可能性。这一措辞较其此前呼吁英伟达“大换血”、从外部寻找领导人的强硬立场明显缓和，为这位创始人的回归留出了想象空间。 阅读全文
[1489] 2026-11-23 06:21 | cnBeta (zh) | Anthropic承诺投入9000亿美元 华尔街预期其债务将为垃圾级 -- Anthropic的上市之路颠簸。而它在债券市场的前景，疑问更大。这家备受关注的AI企业上周募资超1000亿美元，估值约1.18万亿美元，高于沃尔玛和摩根大通。但它已经承诺，未来数年投入约9000亿美元，为其旗舰AI模型采购海量算力。 阅读全文
[1490] 2026-11-23 06:12 | cnBeta (zh) | 研究员向AI服务发送近200万条诱饵提示词后发现泄露情况 -- 网络安全公司 DRAGOS 的研究团队日前发布报告透露 AI 服务商的数据泄露问题，该团队在近 200 万条 AI 请求中植入可以追踪的诱饵信息，测试目的是观察这些提示词经过模型路由、转售服务提供商和托管服务提供商后最终会流向哪里。 阅读全文
[1491] 2026-11-23 06:11 | cnBeta (zh) | GrapheneOS指控Google向Android OEM厂商施压 可能阻止用户解锁BL -- 注重隐私和安全的Android衍生系统 GrapheneOS 日前发文指控Google向Android OEM 施压，试图阻止这些Android设备支持安装 GrapheneOS 系统，Google甚至还试图阻止Android OEM 允许用户解锁引导程序。该系统开发团队担心未来Google会锁定 Pixel 手机的引导程序，让用户无法安装替代的Android系统。 阅读全文
[1492] 2026-11-23 06:10 | cnBeta (zh) | 微软正在开发名为扩展显示的新功能 Android设备当Windows 11副屏使用 -- 微软可能正在开发名为扩展显示的新功能，该功能基于连接至电脑 (Link to Windows) 应用，根据相关文字、图标和代码描述，这个功能应该会支持将Android设备当成 Windows 11 副屏。 阅读全文
[1493] 2026-11-23 06:08 | cnBeta (zh) | Google搭建镜像互联网用来测试Chrome AI功能 防止被恶意网页提示词注入攻击 -- 据Google发布的博客，Chrome 安全团队搭建名为“镜像维度”的专用隔离测试环境，该测试环境中包含数以千计的模拟网站，用来测试 AI 智能体浏览网页、完成任务和抵御恶意指令的能力。 阅读全文
[1494] 2026-11-23 06:07 | cnBeta (zh) | 法国AI公司Mistral在新加坡设立亚太总部 面向政府和企业开发主权AI模型 -- 法国 AI 公司 Mistral 于 2026 年 11 月 20 日在新加坡正式启用其亚太地区总部，新办公室同时设立 AI 研究中心，计划面向公共服务、高科技制造和机器人等领域开发定制 AI 模型。Mistral 还计划将亚太地区的团队从目前约 100 人扩大到 200 人，大部分新增员工都将位于新加坡办公室。 阅读全文
[1495] 2026-11-23 06:06 | cnBeta (zh) | MBK Partners拟以逾1万亿韩元收购日本Wacom 三星电子将参与交易并维持合作 -- 韩国私募股权公司MBK Partners已同意收购日本数字笔技术企业Wacom，交易规模超过1万亿韩元。据《日本经济新闻》等媒体报道，MBK Partners已与Wacom签署要约收购协议，计划通过管理层收购方式完成这笔交易。 阅读全文
[1496] 2026-11-23 06:04 | cnBeta (zh) | 美国执法部门追踪英伟达案中黄乐与中国情报机构接触情况 -- 据《纽约时报》报道，美国执法部门在调查英伟达相关案件期间，追踪到黄乐（Alice Huang）与中国情报机构之间的联系。相关记录显示，她曾与据信隶属中国情报体系的人员保持接触，引发外界对其在案件中所扮演角色的关注。 阅读全文
[1497] 2026-11-23 06:02 | cnBeta (zh) | 微软计划从Windows Server 2028开始不再默认内置32位应用兼容层 -- 微软计划从Windows Server 2028开始，默认不再内置32位应用兼容层，标志着这家公司正进一步清理Windows中的老旧技术。不过，Windows 11目前仍会继续保留32位应用支持，普通用户暂时无需担心老程序无法运行。 阅读全文
[1498] 2026-11-23 06:00 | cnBeta (zh) | 柯达推出SlideTag迷你电子相框 兼具防丢追踪功能 -- 柯达品牌授权方Reto推出了一款名为SlideTag的迷你电子相框，将复古胶片幻灯片的造型与现代数码功能相结合。这款产品不仅可展示照片、文字和二维码，还能作为蓝牙防丢追踪器使用，兼容苹果的“查找”网络或Google的Find Hub网络。 阅读全文
[1499] 2026-11-23 05:55 | cnBeta (zh) | 动视专利申请拟让游戏NPC通过玩家语音聊天学习“拟人化”反应 -- 动视（Activision）近日公开的一项专利申请显示，该公司正在研究一种利用玩家语音聊天训练游戏内非玩家角色（NPC）的技术。该专利名为“应用机器学习生成游戏玩法音频的方法和系统”，于11月12日公开，最早由Patentlyze发现。其核心设想是，让NPC在队友或敌人身份下，对击杀、阵亡等游戏事件作出更接近真实玩家的反应。 阅读全文
[1500] 2026-11-23 05:47 | cnBeta (zh) | 引力被当做“不可屏蔽”的通信媒介 澳大利亚研究人员首次成功用移动重物传递信息 -- 澳大利亚昆士兰大学的物理学家首次利用引力场的变化，将一段信息编码并通过隔墙传输，成功拼出英文单词“Gravity”（引力）。这项实验证明，移动质量所产生的微小引力变化，可以像二进制信号一样承载信息，为一种理论上无法被普通材料屏蔽的通信方式提供了实证基础。 阅读全文
[1501] 2026-11-23 05:46 | cnBeta (zh) | AI渗透测试工具遭滥用：黑客利用Cairn自主发现零日漏洞并入侵服务器 -- 据网络安全研究人员披露，网络犯罪分子近期滥用一款名为Cairn的自主人工智能平台，自行发现并利用一款医学影像应用中的零日漏洞，进而控制了目标服务器。这一事件再次表明，原本用于安全测试的AI工具正被攻击者改造成实施网络攻击的手段。 阅读全文
[1502] 2026-11-23 05:44 | cnBeta (zh) | 三星或调整Galaxy Watch发布节奏 明年可能不推新系列 -- 三星电子可能不会在明年推出新一代Galaxy Watch系列，而是将智能手表产品线改为两年一次的更新周期。若这一计划落实，将是三星自2018年推出首款Galaxy Watch以来首次打破每年更新一代的传统节奏。 阅读全文
