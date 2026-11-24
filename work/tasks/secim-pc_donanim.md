# Görev: secim-pc_donanim

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-pc_donanim.json`
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

TOPIC: PC Donanım, GPU ve Çipler

CRITERIA:
Kullanıcının bilgisayarı: AMD Ryzen 7 9800X3D işlemci ve AMD Radeon RX 7800 XT (16 GB) ekran kartı.
Önemli: yeni GPU/CPU duyuruları ve çıkışları (AMD, NVIDIA, Intel), büyük fiyat değişimleri,
RAM/VRAM fiyatlarında sert hareketler, AMD sürücülerinde önemli güncellemeler veya sorunlar
(özellikle RDNA3 / RX 7000 serisini etkileyen), FSR gibi teknolojilerde büyük yenilikler,
TSMC, ihracat kısıtları, çip sektöründe büyük olaylar, Çinli GPU/çip üreticilerinde büyük adımlar.
Önemsiz: indirim/fırsat haberleri, sıradan aksesuar incelemeleri, küçük söylentiler,
tek bir kasa/klavye/fare ürünü tanıtımı.

CANDIDATE ITEMS:
[594] 2026-11-23 22:00 | Tom's Hardware (en) | Grab a TP-Link tri-band Wi-Fi 7 router for less than $90 -- TP-Link is aiming for the value crown with a $90 tri-band Wi-Fi 7 router.
[695] 2026-11-23 21:08 | Wccftech (en) | Samsung’s Galaxy Tab S11 Is Now $300 Off For Every Storage Tier; Get A Dimensity 9400+, 12GB RAM & More, From Just $700 -- Apple might have the lion’s share of the tablet market, but that doesn’t mean Samsung isn’t putting up a good fight in churning out solid iPad Pro alternatives for the masses. Its Galaxy Tab S11 is a solid option, and thanks to Amazon’s Black Friday 2026 sale, you’ll consider the 11-inch slate....
[1406] 2026-11-23 19:41 | cnBeta (zh) | AI服务器价格争议升级 戴尔、慧与、联想及超微被指在签约后大幅加价 -- 据英国《金融时报》报道，戴尔、慧与、联想和超微等主要服务器厂商，被指在与客户就AI服务器采购价格达成初步协议后，仍在数月内大幅上调最终售价。部分订单的加价幅度据称可达25%、30%，甚至45%，而客户往往只能接受，因为数据中心建设对算力硬件的需求极为迫切。 阅读全文
[385] 2026-11-23 23:17 | TweakTown (en) | 'RTX Lens' is an open-source 12V-2x6 power monitoring tool developed by an RTX 5090 owner to stop more GPU deaths
[345] 2026-11-23 23:58 | Politico (en) | Congress is going to war with Nvidia
[286] 2026-11-23 23:53 | 中国基金报 (zh) | 黄仁勋复职消息扭转市场情绪，美股三大股指高开高走，“科技七巨头”齐涨，芯片、光通信板块集体飙升
[715] 2026-11-23 23:11 | foreignpolicy.com (en) | Hacker News 298 puan, 234 yorum | Huang's Return Is the Biggest National Security Scandal in U.S. History
[595] 2026-11-23 21:09 | Tom's Hardware (en) | MacBook Neo falls to all-time low $599 in Amazon Black Friday sale — 256GB Citrus model price matches its original launch (Update: now $649) -- Apple's MacBook Neo in citrus is down to $599, its original launch price from earlier this year, at Amazon.
[696] 2026-11-23 19:52 | Wccftech (en) | Looking For A Fast, Lag-Free Wireless Gaming Mouse From Razer For Under $50? We Have Three Options For You, Starting From $28 Only -- Getting a high-performance, low-latency wireless gaming setup has never been more affordable, and Amazon’s Black Friday sale makes Razer’s popular wireless lineup an easy recommendation for casual players and competitive esports enthusiasts alike. Right now, the ultra-lightweight Orochi V2 is...
[1407] 2026-11-23 19:40 | cnBeta (zh) | 幼年黑猩猩或许会像人类一样形成“口音” -- 一项新研究发现，幼年黑猩猩的叫声可能会随着成长而逐渐呈现出不同群体的特征，类似人类在成长过程中形成的地区口音。这一发现表明，黑猩猩的发声并非完全由基因决定，社会互动和所处环境也可能在其沟通方式的塑造中发挥重要作用。 阅读全文
[426] 2026-11-23 22:41 | Investing.com (en) | XMax Inc. enters corporate guarantee agreement related to GPU services
[374] 2026-11-23 23:41 | Dallas News (en) | CEO's return to Nvidia caps comeback bid with White House help
[273] 2026-11-23 23:29 | 东方财富网 (zh) | AI芯片供应链收紧 国产超节点迎发展机遇窗口
[716] 2026-11-23 22:32 | stalw.art (en) | Hacker News 428 puan, 221 yorum | Stalwart is now a drop-in replacement for Microsoft Exchange
[596] 2026-11-23 21:00 | Tom's Hardware (en) | Jensen Huang returns to applause from employees at Nvidia HQ -- Jensen Huang returned to the office after being reinstated to CEO to cheers, according to videos spread on social media.
[697] 2026-11-23 19:45 | Wccftech (en) | NVIDIA Employees Give Reinstated CEO, Jensen, A Hero’s Reception At Endeavor HQ, Hopes To Get An RTX Spark To Work On As His New PC -- NVIDIA's CEO, Jensen Huang, is back in action and has visited the Santa Clara HQ as he returns to lead the company into a new beginning. Emotional Scenes Witnessed at NVIDIA's Santa Clara HQ As Employees Hug CEO Jensen On His Return Two weeks ago, NVIDIA announced that its CEO and Founder, Jensen...
[1408] 2026-11-23 19:40 | cnBeta (zh) | 苹果将重新推出(PRODUCT)RED产品 HomePod mini 2和AirPods Max 2可能性较大 -- 苹果或将重新推出红色特别版产品，据彭博社报道，苹果计划为HomePod mini 2和AirPods Max 2推出(PRODUCT)RED版本。这两款新品预计将成为苹果支持全球基金抗击艾滋病、结核病和疟疾年度筹款活动的一部分。 阅读全文
[412] 2026-11-23 21:33 | TradingView (en) | XMax Guarantees Up to $117 Million in GPU Cloud Service Obligations to KT
[341] 2026-11-23 23:10 | CBS News (en) | Huang's return as Nvidia CEO raises questions about its board, experts said
[308] 2026-11-23 23:27 | 端傳媒Initium Media (zh) | 【华尔街日报】与黄乐有关联、被控欺诈：英伟达芯片输华网络的幕后操盘者浮出水面
[717] 2026-11-23 22:09 | github.com (en) | Hacker News 325 puan, 287 yorum | VP ≠ VNP over Every Field
[597] 2026-11-23 19:58 | Tom's Hardware (en) | Grab this $1199 Asus Prime RTX 5070 Ti for a 1440p or 4K gaming upgrade -- RTX 5070 Ti graphics cards have been selling for close to twice their MSRP of late, but you can this Asus card for $1199 at Best Buy right now — the cheapest we've seen so far in the Black Friday frenzy.
[698] 2026-11-23 19:00 | Wccftech (en) | Massive Entertainment Chief Christoph Hartmann Wants the Ubisoft Studio to Become the A24 of Video Games, Winning Oscars and Still Selling -- In late January, Christoph Hartmann left Amazon Game Studios as the company significantly reduced its gaming investments (closing the MMORPG New World and ditching other projects, like Maverick's open world racing game). The former Amazon Game Studios Chief landed another job quickly enough. He...
[1409] 2026-11-23 19:38 | cnBeta (zh) | 英伟达董事会恢复黄仁勋职务据悉未事先征询公司高级管理层意见 -- 据知情人士透露，英伟达董事会出人意料地将黄仁勋复职的举动并未事先征询公司高级管理层的意见，后者直到最后一刻才获知该消息。知情人士表示，在美国总统特朗普向董事会成员施加政治压力后，相关决定于周日下午作出。由于讨论属于非公开信息，知情人士要求匿名。他们还表示，一些原本被考虑接替黄仁勋的人选也退出了遴选程序。 阅读全文
[411] 2026-11-23 21:17 | IGN Southeast Asia (en) | The $749 ABS Cyclone RTX 5060 Gaming PC Is More Powerful AND Less Expensive Than the Steam Machine
[352] 2026-11-23 23:08 | Al Jazeera (en) | What Nvidia’s Jensen Huang’s return means for Washington and Wall Street
[271] 2026-11-23 22:49 | 财联社 (zh) | 高盛上调2027年AI项目融资预期逾八成至5450亿美元 并提示融资成本走高
[718] 2026-11-23 21:01 | bored.com (en) | Hacker News 227 puan, 50 yorum | Bored.com: Fun, interesting and cool websites to explore when bored
[598] 2026-11-23 19:24 | Tom's Hardware (en) | Electric Intel Core Ultra 7 270K Plus deal knocks it down to just $232 -- The Intel Core Ultra 7 270K Plus is back in stock at Amazon for an all-time low price of just $232, over $80 off its average selling price.
[699] 2026-11-23 18:49 | Wccftech (en) | Jensen Huang Was Reinstated As NVIDIA CEO In A Sunday Board Decision That Kept Interim CEO Collette Kress Out Of The Loop, Says Bloomberg -- NVIDIA's decision to reinstate CEO Jensen Huang was made on Sunday afternoon and kept several key leaders out of the loop, says a fresh report from Bloomberg. The process kept former interim CEO Collette Kress out of the loop, and it involved pressure from President Trump. Trump admitted to having...
[1410] 2026-11-23 19:38 | cnBeta (zh) | 黄仁勋重返英伟达总部 -- 援引其获得的一段来自员工的视频报道称，黄仁勋已返回英伟达总部，员工欢呼迎接。黄仁勋对员工表示，第二次担任CEO，他会做得更好。 阅读全文
[415] 2026-11-23 20:22 | Gizmodo (en) | Tech Bros Celebrate Jensen Huang's Return to Nvidia
[362] 2026-11-23 22:55 | BigGo Finance (en) | Jensen Huang's Return Doesn't Stop California AG's Probe Into NVIDIA Chip Flows, Three Key Questions in Focus
[303] 2026-11-23 20:46 | 同花顺 (zh) | 高盛重新定价存储芯片2028：供需缺口收窄与价格下行压力
[719] 2026-11-23 20:52 | github.com (en) | Hacker News 243 puan, 96 yorum | GPUI is getting its own repository
[599] 2026-11-23 18:11 | Tom's Hardware (en) | Stock up on storage with 4TB T-Force G50 SSD at just $0.10 per GB -- Team Group has an excellent deal on its 4TB T-Force G50 Evo, knocking the price down to just $0.10 per GB ahead on Black Friday.
[700] 2026-11-23 17:08 | Wccftech (en) | RayNeo’s Air 4 Pro and GT Bring A 231-Inch Virtual Theater Screen In Your Pocket Without Breaking The Bank; Up To 24% Off On Amazon, Starting From $233 -- Wearable display glasses are quickly becoming the ultimate travel and gaming accessory, and Amazon’s Black Friday 2026 sale discounts two of RayNeo’s standout models for anyone looking to carry a massive virtual cinema screen everywhere and pay under $250 for both of them. Right now, the RayNeo Air...
[1411] 2026-11-23 18:27 | cnBeta (zh) | 黄仁勋复任英伟达CEO后将出席美国参议院听证会 12月3日接受质询 -- 英伟达首席执行官黄仁勋将在12月3日出席美国参议院银行委员会举行的公开听证会。根据该委员会官方网站公布的通知，这场听证会将围绕美国出口管制政策及相关国家安全问题展开。值得注意的是，听证会安排公布之际，黄仁勋刚刚在英伟达董事会决定下重新出任公司首席执行官。 阅读全文
[396] 2026-11-23 20:09 | GameGPU (en) | Sapphire Radeon RX 9070 XT for $979 suddenly broke down after a year and a half of use
[337] 2026-11-23 22:51 | kcentv.com (en) | TSMC files application for proposed $22.6 billion chip plant in Brazos County
[298] 2026-11-23 20:01 | 财联社 (zh) | 【早报】深夜巨震，美股芯片股剧烈分化；美联储纪要放鹰，几乎所有官员预计还需加息；字节、微软据悉洽谈扩容云合同
[720] 2026-11-23 19:17 | arm.jonpalmisc.com (en) | Hacker News 1009 puan, 230 yorum | Per Arm's request, this site will be discontinued on 4 December 2026 :(
[600] 2026-11-23 18:07 | Tom's Hardware (en) | Crucial’s 1TB P310 SSD plummets to £122 at Amazon for Black Friday -- Grab 1TB of premium SSD storage for your PC or PS5 in this Amazon Black Friday deal.
[701] 2026-11-23 15:50 | Wccftech (en) | Grand Theft Auto VI Leaker CYBERLEEK Posts the Xbox Build’s Executable, Calling It a Fight Against Subscriptions and $20 “Ultimate Editions” -- CYBERLEEK, the anonymous hacker/leaker (or group) responsible for the major pre-release Grand Theft Auto VI footage leaks, has now delivered on their final promise: posting the Xbox build's executable after already sharing the rest of the files, as an "early Christmas gift". The document published...
[1412] 2026-11-23 17:12 | cnBeta (zh) | 英国湖区救援人员警告：越来越多年轻人因依赖Google地图在山地迷路 -- 英国湖区（Lake District）山地救援人员表示，越来越多年轻人因在山区徒步时依赖手机上的Google地图导航而迷路或陷入险境。瓦兹代尔山地救援队（Wasdale Mountain Rescue Team）在周日完成一次全员出动救援后发出警告，称遇险者中年轻人比例明显上升，其中大多数年龄在18岁至24岁之间。 阅读全文
[380] 2026-11-23 20:00 | XDA (en) | I let my Nvidia RTX GPU upscale my old DVD rips in VLC, and they hold up on a 4K TV
[321] 2026-11-23 22:43 | The Washington Post (en) | Jensen Huang went to war with Washington — and won a path to China
[311] 2026-11-23 17:29 | 财联社 (zh) | 亚马逊据悉斥资约75亿美元收购Decart技术与团队 加码Trainium芯片挑战英伟达
[721] 2026-11-23 18:48 | github.com (en) | Hacker News 568 puan, 130 yorum | Apple removed my open-source app from EU App Stores after 9 years
[601] 2026-11-23 18:00 | Tom's Hardware (en) | Penguin shows off photonic memory box at SC26 that can expand AI accelerator capacity over optical links -- Penguin Solutions demonstrates external memory box with up to 576 GB of HBM3E and 16 TB of DDR5 inside that connects to hosts using optical links. There is a major caveat with this one as it does not seem to support existing servers and accelerators.
[702] 2026-11-23 15:48 | Wccftech (en) | Elon Musk Takes Credit For NVIDIA’s Reinstatement Of Jensen Huang: “It Is Done” -- In what is nothing short of a remarkable turnaround, Jensen Huang is now all set to return to NVIDIA as its CEO. And in what appears to be an even bigger shocker, Elon Musk has just taken credit for Huang's surprise reinstatement. NVIDIA's board has just unanimously approved Jensen Huang's...
[1413] 2026-11-23 17:12 | cnBeta (zh) | 荷兰隐私监管机构呼吁消费者不要购买“偷拍眼镜” -- 荷兰数据保护局近日发出建议，呼吁消费者不要购买具备人工智能功能的摄像眼镜。该机构警告称，这类可联网、可长时间开启的设备不仅会侵犯周围路人的隐私，还可能给佩戴者自身带来安全风险。 阅读全文
[377] 2026-11-23 19:43 | Latham & Watkins LLP (en) | Latham & Watkins Advises on Apollo and Goldman Sachs’ Landmark US$44 Billion SpaceX GPU Financing
[330] 2026-11-23 22:40 | KBTX News 3 (en) | World’s leading semiconductor manufacturer eyes Brazos County for 3,200+ acre facility
[291] 2026-11-23 17:03 | 东方财富网 (zh) | 特朗普称只想向中国出售相对落后的芯片 外交部回应
[722] 2026-11-23 17:17 | blog.kagi.com (en) | Hacker News 415 puan, 331 yorum | Kagi Mail (public beta)
[602] 2026-11-23 17:00 | Tom's Hardware (en) | AMD's top-performing Ryzen 9850X3D is now the same price as the 9800X3D, an all-time low -- AMD's king of gaming CPUs, the Ryzen 7 9850X3D, is down to its lowest price ever at $399 ahead of Black Friday.
[703] 2026-11-23 15:31 | Wccftech (en) | Grand Theft Auto VI 01.002.002 Bug Fix Update Has Started Rolling Out, But Some Users Will Reportedly Have to Wait 6 Hours To Get It -- [UPDATE - November 23, 2026 - 10:29 AM EST] Rockstar Games released the full update notes for the Grand Theft Auto VI 1.002.002 update. As expected, the update only addresses a few specific issues: Original story follows. [Original Story] Grand Theft Auto VI has only just been released on...
[1414] 2026-11-23 16:35 | cnBeta (zh) | 罢免AI巨头CEO不容易：黄仁勋、奥特曼不到两周都回归了 -- 英伟达周一宣布，黄仁勋(Jensen Huang)回归公司担任CEO。《华尔街日报》对此表示，这件事再次证明，想罢免一位AI巨头的CEO并不是一件容易事。在这轮AI热潮中，已有两位最受瞩目的业界CEO被各自的董事会解雇。但是，这两次决定都没能撑过两周。 阅读全文
[381] 2026-11-23 19:43 | HPCwire (en) | AWS Lambda Adds Support for GPU Instances on Lambda Managed Instances
[334] 2026-11-23 22:30 | Reuters (en) | US lawmakers criticize Nvidia decision to reinstate Huang as CEO
[300] 2026-11-23 13:45 | 中国科技网 (zh) | 中原工学院：以“硬核”育人锻造芯片产业生力军
[723] 2026-11-23 17:07 | twitter.com (en) | Hacker News 1613 puan, 903 yorum | "Dario is hyping the dangers so OSS models are illegal and he stays in control"
[603] 2026-11-23 16:30 | Tom's Hardware (en) | 3D printer that promises simultaneous printing from all four heads gets $500,000 in crowdfunding -- The $899 Co Print Quadro IQEX 3D printer raised over $500,000 on Kickstarter, exceeding its $50,000 goal.
[578] 2026-11-23 14:32 | Wccftech (en) | Z.ai Debuts GLM-5.4 With a Novel Looped Transformer Architecture, Skipping AI Benchmarks Entirely
[1415] 2026-11-23 16:27 | cnBeta (zh) | 特朗普：祝贺黄仁勋恢复职位 英伟达董事会决定正确且恰当 -- 11月23日，美国总统特朗普发文：英伟达董事会作出了让黄仁勋重回首席执行官一职的正确、恰当的决定。国家需要黄仁勋掌舵英伟达，以保障这家公司取得成功、蓬勃发展，同时助力美国经济走向繁荣。美国政府没有理由认为黄仁勋回归英伟达会带来任何国家安全风险。 阅读全文
[432] 2026-11-23 19:21 | IGN Southeast Asia (en) | The Best Black Friday Prebuilt Gaming PC Deals Are Already Going Live Now
[348] 2026-11-23 22:23 | NPR (en) | Ex-White House AI advisor weighs in on national security as Huang is back as Nvidia CEO
[295] 2026-11-23 13:40 | 美国之音 (zh) | 贝森特：政府仍在等待英伟达就其芯片流入中国问题作出解释
[724] 2026-11-23 16:13 | ladybird.org (en) | Hacker News 1856 puan, 428 yorum | Ladybird is now in alpha
[604] 2026-11-23 16:23 | Tom's Hardware (en) | Save $2,000 on this sick Corsair RTX 5090 prebuilt gaming PC this Black Friday week -- Most RTX 5090 prebuilts now run over five figures, but you can get this Corsair Vengeance system with 64GB of RAM and a 4TB SSD for $8999.99, or $2,000 off, this Black Friday deals week.
[704] 2026-11-23 14:09 | Wccftech (en) | NVIDIA Says Jensen Huang Isn’t A Security Risk As It Reinstalls Him As CEO After Consulting Senior US Officials -- In a surprising move, after he resigned from NVIDIA earlier this month, NVIDIA CEO Jensen Huang has returned to the firm. NVIDIA announced Huang's return in a press release shared moments back as it outlined that it had decided to reinstate Huang unanimously after holding discussions with him and...
[1416] 2026-11-23 16:22 | cnBeta (zh) | 黄仁勋时隔12天重回英伟达CEO宝座 苹果乔布斯等了12年 -- 英伟达董事会只用了12天，就做出撤销罢免黄仁勋的决定。三年前的同一周，OpenAI撤换山姆・奥特曼，完成同样的复位只用了不到五天。几乎没人预料到黄仁勋复职的消息。周一早晨，就在英伟达发布官方复职公告的几分钟之前，预测市场Polymarket上，押注黄仁勋本月复职的概率仅约3%。公告发布五分钟后，该概率飙升至99%。 阅读全文
[387] 2026-11-23 19:11 | Phoronix (en) | Mesa 27.0 RadeonSI Gallium3D Disables Radeon Kernel Driver Support By Default
[328] 2026-11-23 22:06 | PBS (en) | Huang's reinstatement at Nvidia sparks fury in Congress as lawmakers move forward with hearings
[725] 2026-11-23 14:49 | reddit.com (en) | Hacker News 287 puan, 306 yorum | Cyberleek posted the GTA 6 Xbox build executable
[605] 2026-11-23 15:30 | Tom's Hardware (en) | Samsung Odyssey G8 G80SH 32-inch 4K OLED gaming monitor review: Color, performance and style -- Samsung’s Odyssey G8 G80SH is a 32-inch Quantum Dot OLED with 4K resolution and 240 Hz. It delivers a huge color gamut along with smooth and responsive gaming and it supports HDR10+ with dynamic tone mapping.
[705] 2026-11-23 14:00 | Wccftech (en) | AMD Ryzen 7 5800X3D Bundle With B550 Motherboard, 32 GB DDR4 RAM, And 1 TB SSD Drops To $451 As Soon As You Hit Newegg’s “Build With It” Button -- This is probably the greatest AM4 deal you will find today, offering four components for nearly half the price. Newegg Slashes AM4 Bundle With Ryzen 5800X3D, 32 GB Memory, 1 TB SSD, and AM4 Motherboard to Just $451, Saving you Nearly $400 A four-item combo has been listed by Newegg for a...
[1417] 2026-11-23 16:22 | cnBeta (zh) | 特朗普对黄仁勋重返英伟达表示祝贺 -- 英伟达周一宣布恢复黄仁勋首席执行官职务。过去数日，美国政府公开表态持续力挺这位 CEO，特朗普对此表示祝贺。英伟达官宣消息发布数分钟后，特朗普在真实社交平台发帖称：“英伟达董事会做出了正确且恰当的决定。美国政府没有理由认为黄仁勋重返英伟达会带来任何国家安全风险。” 阅读全文
[397] 2026-11-23 18:45 | Decrypt (en) | GPU Jesus Has Risen: Jensen Huang’s Nvidia Comeback Gets a Meme Parade
[369] 2026-11-23 21:21 | WSVN (en) | Nvidia leads a surge on Wall Street as the S&P 500 leaps 4.7% following Jensen Huang’s return as the chip company’s CEO
[726] 2026-11-23 14:01 | nvidianews.nvidia.com (en) | Hacker News 1708 puan, 4 yorum | Jensen Huang Reinstated as Nvidia CEO
[606] 2026-11-23 14:14 | Tom's Hardware (en) | Nvidia reinstates Jensen Huang as CEO following unanimous vote by company board -- Nvidia has reinstated Jensen Huang as CEO, president, and a board member.
[706] 2026-11-23 13:30 | Wccftech (en) | Activision AI NPC Patent Could Teach Game Characters To Trash-Talk Using Players’ Voice Chats -- Activision is exploring an interesting, if potentially controversial, application of artificial intelligence that could make video game NPCs sound considerably more human. Rather than relying exclusively on prerecorded dialogue, the publisher has outlined a system that learns how actual players...
[1418] 2026-11-23 16:21 | cnBeta (zh) | 黄仁勋复职带动AI相关股票普涨 英伟达市值数小时增逾1万亿美元 -- 英伟达恢复黄仁勋总裁兼首席执行官职务、并立即生效后，人工智能相关公司的股价大涨。英伟达股价一度上涨35%，创2000年以来最大盘中涨幅，短短数小时内市值增加逾1万亿美元，并带动标普500指数和纳斯达克100指数走高。涨势还蔓延至芯片股，费城证券交易所半导体指数飙升13%；其他与人工智能基础设施交易相关的股票也上涨，包括能源供应商和工业企业。 阅读全文
[427] 2026-11-23 18:41 | Traders Union (en) | Nvidia communications strategy faces pressure after graphics card launch backlash
[332] 2026-11-23 21:14 | Yahoo Finance (en) | Nvidia soars 31% as board reinstates Jensen Huang as CEO, chip stocks jump
[727] 2026-11-23 14:00 | cnbc.com (en) | Hacker News 2518 puan, 3438 yorum | Nvidia Reinstates Jensen Huang as CEO
[607] 2026-11-23 14:00 | Tom's Hardware (en) | Russia readying nuclear-armed satellite that could destroy every low-Earth-orbit satellite, report claims -- Intelligence agencies believe that Russia has spent the last several years readying Sputnik-S, a satellite armed with a nuclear weapon that could be used to destroy rival satellites in Earth's orbit — and it's expected to launch before the end of 2026.
[707] 2026-11-23 13:30 | Wccftech (en) | Grand Theft Auto VI Sales Have Reached 45 Million Units at the End of the Launch Weekend, Claims Alinea, with 40-Plus Million Users Active on Sunday -- Last Friday, Take-Two Interactive and Rockstar Games announced the staggering initial Grand Theft Auto VI sales figures: 32.5 million units of pre-orders and $3.79 billion in revenue generated through November 19 (the launch day). However, those numbers certainly did not stop there. The fresh...
[1419] 2026-11-23 16:19 | cnBeta (zh) | 英伟达恢复黄仁勋CEO背后：特朗普强势施压相助 -- 英伟达周一宣布，董事会一致投票决定，立即恢复黄仁勋的总裁、首席执行官及董事职务，距他 11 月 12 日辞职仅 11 天。公司称，经过与黄仁勋的一系列讨论和会面，董事会对他的坦诚和领导能力感到满意。董事长哈维·琼斯表示，董事会认为黄仁勋是领导英伟达的合适人选，复职符合股东、员工、合作伙伴和客户的最佳利益。临时 CEO 科莱特·克雷斯留任首席财务官，琼斯继续担任董事长。 阅读全文
[413] 2026-11-23 18:05 | BigGo Finance (en) | Walmart Cuts ABS Cyclone RTX 5060 Gaming PC to $749, a $650 Drop From List Price
[339] 2026-11-23 21:10 | VOA - Voice of America English News (en) | Nvidia reinstates Jensen Huang as CEO as US probes alleged chip exports to China
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file
[608] 2026-11-23 13:30 | Tom's Hardware (en) | The RTX 5090’s 16-pin connector just got a free pin-by-pin watchdog -- RTX Lens, a free open-source Windows app, reads per-pin current from Asus ROG Astral GPUs, WireView Pro II and MSI PSUs.
[708] 2026-11-23 13:25 | Wccftech (en) | ACEMAGIC’s Retro X5 Mini Gaming PC With 10-Core Zen 5 CPU, 32GB Memory, And 1 TB SSD Drops to $899 – 24GB Mini PCs Now Under $500! -- Not one or two, but three ACEMAGIC mini PCs have gotten deep discounts, giving you a big opportunity to upgrade to a powerful configuration for your setup. ACEMAGIC Slashes $300 Off Retro X5 Mini Gaming PC, Bringing 32 GB + 1 TB Configuration to Just $899, While its Budget Intel and AMD Mini PCs...
[1420] 2026-11-23 16:18 | cnBeta (zh) | 英伟达官宣黄仁勋回归 马斯克发文“It is done”庆祝 -- 今日晚间，英伟达官方宣布即日起恢复黄仁勋公司总裁、首席执行官及董事会成员职务。董事会表示，经多轮沟通，认可其坦诚态度与领导能力，并在听取美国政府高层意见后，确信其复职不存在国家安全风险。 阅读全文
[409] 2026-11-23 17:49 | StorageReview.com (en) | HighPoint PAE Bundles Move 600W RTX PRO Blackwell GPUs to an External 1300W Enclosure Over PCIe Gen5 x16
[331] 2026-11-23 20:51 | Reuters (en) | US blacklists Alibaba subsidiary and Megaspeed linked to Nvidia scandal
[729] 2026-11-23 12:28 | github.com (en) | Hacker News 231 puan, 121 yorum | Win4wine – Run Windows Shell as the Linux Desktop on Wine
[609] 2026-11-23 13:00 | Tom's Hardware (en) | MacBook modder turns Apple’s Force Touch trackpad into a speaker using haptic engine -- The MacBook's Force Touch trackpad with its haptic engine can be used to make it into a speaker using some new 'No Speaker' software.
[709] 2026-11-23 13:10 | Wccftech (en) | Intel’s 12 And 10 P-Core Bartlett Lake CPUs Now Reportedly Run Out Of The Box On MSI Z690 And Z790 Boards With Power Limits Unlocked -- Thanks to the latest BIOS updates, Intel's "Embedded" P-Core-only Bartlett Lake CPUs can now work on several MSI motherboards out of the box. Latest MSI BIOS Now Supports Intel 12P and 10P Bartlett Lake CPUs on Z690 and Z790 Motherboards Without Modifying BIOS Running Intel's Bartlett Lake CPUs on...
[1421] 2026-11-23 15:04 | cnBeta (zh) | 三星将继续独家向苹果供货Phone Duo折叠屏 -- 苹果或将在至少未来五年内继续依赖三星显示作为iPhone Duo折叠屏的独家供应商，这一局面可能重演苹果在2017年推出首款OLED iPhone时所面临的供应链依赖问题。 阅读全文
[421] 2026-11-23 17:20 | Fierce Network (en) | SpaceX bags $44B in financing for Nvidia GPUs
[372] 2026-11-23 20:48 | CNBC (en) | Jensen Huang cheered by Nvidia employees after return, pledges to be a better CEO
[730] 2026-11-23 11:34 | blogs.gentoo.org (en) | Hacker News 243 puan, 150 yorum | The next circle of security hell
[610] 2026-11-23 12:30 | Tom's Hardware (en) | AI server makers hike prices up to 45% months after contracts are signed as RAM shortage triples memory-heavy CPU node costs -- AI server OEMs are hiking prices post-contract up to 45% — financial firms building datacenters still pay up as "the compute is valuable"
[710] 2026-11-23 12:42 | Wccftech (en) | New Bazuka Android Malware Fakes Your Home Screen To Hide Banking Data Theft, Uses AI To Identify Victims -- Cybersecurity researchers have uncovered a sophisticated new Android malware framework called Bazuka, which can steal banking credentials, intercept two-factor authentication codes, and remotely control infected smartphones. More alarmingly, the malware can disguise its activities by displaying a...
[1422] 2026-11-23 14:28 | cnBeta (zh) | 特朗普：祝贺黄仁勋恢复职位 -- 美国总统特朗普在社交媒体发文称，英伟达董事会作出了让黄仁勋重回首席执行官一职的正确、恰当的决定。国家需要黄仁勋掌舵英伟达，以保障这家公司取得成功、蓬勃发展，同时助力美国经济走向繁荣。 阅读全文
[388] 2026-11-23 17:17 | Data Center Dynamics (en) | Apollo and Goldman Sachs lead $44bn SpaceX GPU financing deal
[340] 2026-11-23 18:31 | Investor's Business Daily (en) | Amazon Makes AI Chip Move; Stock Climbs Alongside Nvidia Relief Rally
[731] 2026-11-23 11:31 | github.com (en) | Hacker News 308 puan, 172 yorum | Nospeaker ─ Make your MacBook's trackpad talk using its haptic motor
[611] 2026-11-23 12:15 | Tom's Hardware (en) | Walmart has slashed $1,090 off this RTX 5070 gaming laptop, nearly half price -- Save nearly 50% on this HyperX gaming laptop with RTX 5070.
[711] 2026-11-23 12:30 | Wccftech (en) | Colorful Goes Compact With iGame B850I Mini W OC Motherboard, Delivering 12 Power Phases, X3D AI Turbo Mode, And Dual PCIe 5.0 Storage Connectivity -- Colorful's new AM5 motherboard brings the latest connectivity, powerful VRM, and proprietary X3D AI Turbo Mode for best possible Ryzen X3D performance. Colorful Launches iGame B850I Mini W OC Motherboard at $239 for Enthusiasts Looking for a Compact yet Powerful AM5 Solution for Their Gaming Builds...
[1423] 2026-11-23 14:12 | cnBeta (zh) | 黄仁勋恢复CEO职务 英伟达美股盘前涨超25% -- 11月23日，英伟达官网公告称，公司董事会已一致投票决定，即日起恢复黄仁勋的总裁、首席执行官及董事职务。英伟达表示，董事会经过与黄仁勋多轮讨论和会议，对其坦诚程度及领导公司能力表示认可，并认为恢复其职务符合股东、员工、合作伙伴及客户的最佳利益。 阅读全文
[375] 2026-11-23 17:15 | NVIDIA Developer (en) | Get Started with GPU-Accelerated DuckDB Analytics Using the Sirius Core Extension
[367] 2026-11-23 17:08 | Cheddar (en) | White House Says No More Nvidia Chip Restrictions Needed
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[612] 2026-11-23 11:59 | Tom's Hardware (en) | President Trump says Nvidia board rehiring Jensen Huang would be best for the company and US -- After saying that Nvidia's new CEO should come "outside the company," U.S. Treasury Secretary Scott Bessent now argues that former chief Jensen Huang "is on the outside," paving the way for a return following President Trump's declaration of his "hope" that Huang will be back.
[712] 2026-11-23 12:30 | Wccftech (en) | GameStop CEO Ryan Cohen Criticizes GTA 6 Skipping Physical Discs: ‘When Buying a Game, Players Should Be Able to Sell It, Trade It, or Lend It’ -- GameStop CEO Ryan Cohen is unhappy that games like Grand Theft Auto VI are skipping physical discs in favor of digital downloads. In an interview with Bloomberg, he stated: When you buy a game, you should be able to sell it, trade it, or lend it to a friend. Unquestionably, the decision to move to...
[1424] 2026-11-23 14:11 | cnBeta (zh) | 黄仁勋回应回复职务：已经迫不及待重返工作岗位 -- 英伟达今日宣布，即刻生效，黄仁勋恢复担任公司总裁、首席执行官及董事会成员。 董事会经与黄仁勋多轮沟通会晤后，一致投票同意恢复其职务；董事会认可黄仁勋态度坦诚，具备领导英伟达的能力。在作出该决定的过程中，董事会听取了美国政府高层的意见。基于相关意见，董事会确信，黄仁勋复职不会带来国家安全风险。 阅读全文
[410] 2026-11-23 17:00 | XDA (en) | A used RTX 3060 with 12GB is still the card I'd put in a home server instead of a newer 8GB GPU
[373] 2026-11-23 17:01 | Yahoo Finance (en) | Applied Materials Jumps 13% as Chip Rally Follows Nvidia's CEO News on Nov. 23
[733] 2026-11-23 09:55 | jola.dev (en) | Hacker News 172 puan, 109 yorum | Don't keep your SSH keys on disk
[613] 2026-11-23 11:39 | Tom's Hardware (en) | Save $730 on this RTX 5070 Ti gaming PC from Cyberpower -- An impressive gaming PC prebuilt with an RTX 5070 TI 16GB powering the graphics. All for just $2.479 in Walmart's Black Friday deal.
[1425] 2026-11-23 14:02 | cnBeta (zh) | NVIDIA董事会宣布黄仁勋即刻恢复总裁、首席执行官及董事职务 -- 英伟达（NVIDIA）今日宣布，黄仁勋（Jensen Huang）自即日起恢复担任公司总裁兼首席执行官，并重新出任董事会成员。董事会一致投票通过这一决定，认为黄仁勋具备坦诚沟通和领导英伟达的能力，其复职符合公司股东、员工、合作伙伴及客户的最佳利益。 阅读全文
[431] 2026-11-23 16:43 | VideoCardz.com (en) | GTA 5 runs at 15 FPS on 19-year-old ATI GPU with 64MB VRAM, but good luck stealing invisible cars
[338] 2026-11-23 16:56 | Reuters (en) | Rene Haas to remain CEO of chipmaker Arm through 2033 amid Nvidia top job chatter
[734] 2026-11-23 05:00 | micjm.com (en) | Hacker News 263 puan, 340 yorum | Yes, 17M lines of Lean is a lot
[614] 2026-11-23 11:30 | Tom's Hardware (en) | Weird finger-wearable gaming mouse raises $43,000 on Kickstarter, a staggering 8x its crowdfunding goal -- The Borslon finger-worn gaming mouse is the first of its kind, reaching $43,000 in its Kickstarter campaign and shattering its $5,000 goal. However, it may have some major design flaws.
[1426] 2026-11-23 13:59 | cnBeta (zh) | 古尔曼：苹果Siri裁员不影响AI项目 智能体要等到2027年底 -- 知名科技记者马克·古尔曼表示，苹果近期针对Siri的裁员，主要裁撤负责语音助手旧底层架构的员工，新一代AI系统研发团队并未受到波及。古尔曼表示，尽管苹果力求加快开发节奏、把重大更新分散到全年推送，但员工尚未感受到公司运转模式有明显变化。他还提到，苹果正面临激烈竞争：其他AI厂商迭代模型与服务的频率远高于苹果一年一度的操作系统更新周期。 阅读全文
[404] 2026-11-23 16:28 | GameGPU (en) | Perekhod s Radeon RX 9060 XT + Radeon RX 9070 for 1440p will cost the player €265
[317] 2026-11-23 14:35 | The New York Times (en) | U.S. Tracked Alice Huang’s Contacts With China Spy Agency During Nvidia Affair
[735] 2026-11-23 04:31 | facebook.com (en) | Hacker News 218 puan, 80 yorum | DeFlock Amerca Facebook Group Suspended as a Dangerous Organization
[615] 2026-11-23 11:00 | Tom's Hardware (en) | Homelabber's $3,500 build straps four 32GB Nvidia V100s to a Lenovo mini PC for 128GB of VRAM -- A homelab builder runs four used Nvidia Tesla V100 32GB GPUs off a Lenovo ThinkCentre M720q Tiny over OCuLink for 128GB of VRAM.
[1427] 2026-11-23 13:36 | cnBeta (zh) | 美国两州起诉SpaceX旗下Grok：指其未防滥用生成儿童性虐待材料 -- 据路透社报道，美国弗吉尼亚州和俄勒冈州已对埃隆·马斯克旗下SpaceX Super Intelligence提起诉讼，指控该公司从Grok图像生成功能中获利，却未设置充分的安全防护，导致其可能被用于制作儿童性虐待材料及其他未经当事人同意的性化图像。 阅读全文
[400] 2026-11-23 16:06 | Yahoo Finance (en) | Bad News for CoreWeave and IREN: GPU Lenders Now Want “50 to 60 Percent” Loan-to-Value, Down From 80%
[357] 2026-11-23 14:13 | Yahoo Finance (en) | NVIDIA Jumps 28% as Jensen Huang Reportedly Reinstated as CEO; Intel Rallies 13%, AMD Gains 7%
[736] 2026-11-23 03:49 | github.com (en) | Hacker News 609 puan, 245 yorum | XNU Linux: Full OS X GUI Userspace on Linux
[616] 2026-11-23 10:30 | Tom's Hardware (en) | Canadian scalper places 40+ Nvidia RTX 5090s on Facebook Marketplace for over $6,600 apiece -- Canadian scalper places 40+ Nvidia RTX 5090s on Facebook Marketplace for over 6,600 USD a piece — purportedly sold every unit quite quickly
[1428] 2026-11-23 13:36 | cnBeta (zh) | Telegram创始人嘲讽《GTA6》 主角犯罪连个群都没有 -- Telegram创始人兼CEO帕维尔·杜罗夫近日在社交媒体上发推，吐槽了《GTA6》。杜罗夫认为，《GTA6》中最不真实的地方在于，游戏主角能够经营犯罪活动，却连一个Telegram群都没有。 阅读全文
[403] 2026-11-23 15:59 | GameGPU (en) | CyberPowerPC with RTX 5060 graphics card is down to £749,99 ahead of Black Friday
[376] 2026-11-23 14:11 | Yahoo Finance (en) | Nvidia soars 25% as board reinstates Jensen Huang as CEO
[737] 2026-11-23 03:34 | docker.com (en) | Hacker News 123 puan, 202 yorum | The New Docker: Built to Run on Trust
[617] 2026-11-23 10:00 | Tom's Hardware (en) | Anthropic researcher tunes chess software to turn a 200 MHz Pentium Pro into a behemoth that would handily defeat Kasparov and Carlsen -- Anthropic researcher tunes chess software to turn a lone Pentium Pro into a behemoth that would handily defeat Kasparov and Carlsen — 1995 challenge would have a different outcome with today's software
[1429] 2026-11-23 13:36 | cnBeta (zh) | 荷兰地方法院裁定比亚迪Ti7在欧盟禁售禁产 -- 11月23日，针对比亚迪Ti7车型被禁止在欧盟销售，比亚迪方面回应称，对该判决持有强烈异议，并将依法提起上诉。Ti7车型系比亚迪自主独立设计成果，未侵犯捷豹路虎的任何权利。比亚迪将通过相关法律途径，坚决维护自身合法权益。 阅读全文
[402] 2026-11-23 15:57 | Technology Org (en) | Maker Builds a 20-Cent USB GPU Around a Single RISC-V Microcontroller
[360] 2026-11-23 14:10 | PBS (en) | Nvidia board unanimously reinstates Jensen Huang as CEO and chairman
[738] 2026-11-23 01:50 | pengowray.github.io (en) | Hacker News 192 puan, 76 yorum | Treemap of the Riemann Hypothesis Lean Proof
[618] 2026-11-22 21:32 | Tom's Hardware (en) | Corsair's tiny Xeneon Edge PC touchscreen discounted by $50 in all five colors -- Corsair has all five colors of its 14.5-inch Xeneon Edge touchscreen on sale for Black Friday for $50 off.
[1430] 2026-11-23 13:33 | cnBeta (zh) | 基于原生WinUI 3 的全新Windows 11搜索界面已向普通用户推送 -- 微软在11月的可选更新KB5132057中，逐步向普通Windows 11电脑推出了原生WinUI 3搜索体验。新搜索采用更紧凑的界面设计，摆脱了此前基于WebView2的旧版搜索，在测试中仅占用约26.3MB内存，整体体验明显更轻量、更现代化。 阅读全文
[379] 2026-11-23 15:53 | MIXED News (en) | ASRock’s $979.99 Radeon RX 9070 XT comes with a free 360mm CPU cooler
[739] 2026-11-23 01:13 | spelunkr.tv (en) | Hacker News 990 puan, 170 yorum | Free alternative TV network built from the Internet Archive's forgotten videos
[619] 2026-11-22 20:03 | Tom's Hardware (en) | 17-year-old Windows 7 gets unofficial DirectX 12 and Vulkan 1.4 support -- A community-developed project brings DirectX 12 and Vulkan 1.4 support to Microsoft's retired operating system, allowing modern Nvidia, AMD, and Intel graphics cards to work with Windows 7.
[1431] 2026-11-23 13:27 | cnBeta (zh) | 苹果或将调整新品发布节奏 2027年年初集中推出平价产品 -- 苹果或将调整新品发布节奏，2027年年初集中推出iPhone 18e、新款入门iPad和MacBook Neo等平价产品。据彭博社记者马克·古尔曼的消息，苹果计划在明年2月至3月间展开一轮以中低端设备为重点的发布潮。 阅读全文
[395] 2026-11-23 15:49 | GameGPU (en) | ASRock owner Radeon RX 9070 XT talked about buying a video card for $579,99
[740] 2026-11-22 23:57 | alvinbunk.wordpress.com (en) | Hacker News 306 puan, 322 yorum | Nespresso's project to reduce grams of coffee per capsule
[620] 2026-11-22 19:34 | Tom's Hardware (en) | An under-desk headphone hanger with three charging ports (up to 20W) for less than $10 -- With two hanging hooks, notches for six cables and 20W of charging, Cozoo's quirky combo device has been a useful addition to my standing desk since 2023.
[1432] 2026-11-23 13:25 | cnBeta (zh) | 新增超广角镜头带来空间瓶颈 iPhone Air 2或不会采用更小的灵动岛 -- 根据近期曝光的机模照片及业内分析，下一代iPhone Air 2可能仍将沿用初代机型的灵动岛设计，而不会像iPhone 18 Pro那样采用尺寸更小的灵动岛。尽管该机型预计将新增一颗后置超广角镜头，但机身内部空间可能不足以支持更小的前置摄像头开孔布局。 阅读全文
[378] 2026-11-23 15:30 | GameGPU (en) | A gamer bought a GeForce RTX 5070 OC for $950 instead Radeon RX 9070 XT for $ 1050
[741] 2026-11-22 23:12 | claude.ai (en) | Hacker News 257 puan, 201 yorum | Progress map for the Lean formalization of Alessandro Masullo's manuscript
[621] 2026-11-22 19:00 | Tom's Hardware (en) | Nintendo Switch 2 bundles drop to pre-hike pricing in Black Friday run-up sale -- The Nintendo Switch 2 is down to its pre-hike pricing in a Black Friday run-up sale, knocking a rare $50 off a bundle that includes the console and Mario Kart World.
[1433] 2026-11-23 13:24 | cnBeta (zh) | 伯克希尔·哈撒韦停止减持苹果股票 占其投资组合的四分之一 -- 伯克希尔·哈撒韦自2026年初新任首席执行官格雷格·阿贝尔上任以来，已停止出售苹果股票。目前，苹果在该公司股票投资组合中所占比例达到25%，成为其最重要的持仓之一。 阅读全文
[392] 2026-11-23 15:18 | GameGPU (en) | Gamers criticize a pre-built PC with a GeForce RTX 5060 Ti graphics card for $1900.
[742] 2026-11-22 23:06 | gl-inet.com (en) | Hacker News 269 puan, 125 yorum | GL.iNet Security Notice 2026-11-20
[622] 2026-11-22 18:17 | Tom's Hardware (en) | Give your gaming setup a serious upgrade with the Razer Basilisk V3 for half price -- Whether you play competitive shooters, racing games, or RPGs, the Basilisk V3 offers customizable controls and an adjustable scroll wheel to suit different gaming preferences.
[1434] 2026-11-23 13:22 | cnBeta (zh) | 汽车智能化程度不断提高也让维修变得更贵、更慢 -- 汽车智能化程度不断提高，正在让维修变得更贵、更慢，并间接推高保险费用。一项新调查显示，美国独立维修店因获取和使用汽车制造商的维修软件而损失的时间，每年价值约120亿美元。 阅读全文
[398] 2026-11-23 15:10 | Jon Peddie (en) | PC GPU shipments declined -3.6% quarter to quarter and -5.0% year to year
[743] 2026-11-22 22:10 | ias.edu (en) | Hacker News 179 puan, 42 yorum | The Usefulness of Useless Knowledge [pdf] (1939)
[623] 2026-11-22 18:15 | Tom's Hardware (en) | Grab Keychron's full-sized low-profile K5 V2 wireless mechanical keyboard for its lowest-ever price of just $56 -- If you're after a full-sized mechanical keyboard on a budget, this is one of the best deals around during early Black Friday. It has a white backlight and supports either Bluteooth or wired connectivity.
[1435] 2026-11-23 13:21 | cnBeta (zh) | 摩托罗拉新款防丢标签Moto Tag 3已提前在亚马逊上架并接受预订 -- 摩托罗拉尚未正式发布的新款防丢器 Moto Tag 3，近日已在亚马逊英国站上架并接受预订。该页面自周五起便已上线，显示产品采用“Pantone Lucent White”配色，售价为24.96英镑，约合33美元，预计发货时间为1至4个月。 阅读全文
[399] 2026-11-23 14:55 | VideoCardz.com (en) | ASUS launches Onimusha-themed Radeon RX 9070 XT for €1,399 in Europe
[744] 2026-11-22 21:53 | pequalnp.com (en) | Hacker News 194 puan, 170 yorum | Agent swarm trying to solve P vs. NP
[1436] 2026-11-23 12:49 | cnBeta (zh) | 拒绝英伟达CEO职位邀请？Arm首席执行官将合同延长至2033年 -- 芯片设计公司Arm Holdings首席执行官雷内·哈斯（Rene Haas）此前曾被接洽，作为英伟达首席执行官黄仁勋的潜在继任者。如今，哈斯已延长合同，将继续领导Arm直至2033年。 阅读全文
[425] 2026-11-23 14:54 | Electronic Engineering Journal (en) | DDC-I and PACE Deliver Certifiable DO-178C DAL A Software Rendering for Avionics Displays
[745] 2026-11-22 21:33 | fosscomics.com (en) | Hacker News 169 puan, 76 yorum | The struggle for freedom: How BSD escaped AT&T licensing
[1437] 2026-11-23 12:12 | cnBeta (zh) | 国产“筷子夹火箭”迎重大突破：宇石空间自研百吨级捕获臂完成验证 -- 11月23日，宇石空间宣布，其自主研制的百吨级全尺寸“筷子”捕获臂已完成联合地面验证试验。据介绍，从试验平台搭建到试验结束历时100余天，团队围绕火箭高可靠回收开展全天候苛刻工况考核，全面覆盖火箭回收任务剖面，验证了捕获臂全天候、全工况、满负载的作业能力，以及动载工况下的结构稳定性。 阅读全文
[419] 2026-11-23 14:37 | citybiz (en) | KIDZ AI Establishes Cortex Infra to Launch GPU Asset-Backed Lending Business
[746] 2026-11-22 21:01 | gpupartner.com (en) | Hacker News 755 puan, 306 yorum | A Beginner's Guide to Local AI with Inkling, Qwen, and Unsloth
[1438] 2026-11-23 12:11 | cnBeta (zh) | 植入了Neuralink脑机的患者仅凭意念畅玩《GTA VI》 线上演示如何操控游戏 -- Neuralink再次展示其脑机接口技术的应用潜力：一名参与临床试验的患者仅凭大脑意念，成功操控Rockstar Games的新作《侠盗猎车手VI》（GTA VI）。该公司于11月20日在社交平台X发布了一段约两分钟的游戏视频，此时距离《GTA VI》登陆主机仅过去一天。 阅读全文
[429] 2026-11-23 14:33 | MIXED News (en) | GTA 6’s unofficial PC port has an 8GB GPU claim, but the game still won’t boot
[747] 2026-11-22 20:33 | smallsf.com (en) | Hacker News 373 puan, 114 yorum | A miniature San Francisco with live buses, planes and boats
[1439] 2026-11-23 12:10 | cnBeta (zh) | 研究发现新一代AI模型更爱“爆粗口” -- 据人工智能分析公司Vals发布的报告，2026年7月之后训练的大型语言模型出现粗俗语言的频率明显高于早期模型；这些脏话几乎都出现在模型的“思维链”中，也就是模型在给出最终答案前写下的逐步推理内容，通常不会直接展示给用户。 阅读全文
[390] 2026-11-23 14:11 | eTeknix (en) | RAM & GPU Prices: Why They Keep Rising and When It Might Ease
[748] 2026-11-22 20:15 | nyx.net (en) | Hacker News 160 puan, 39 yorum | Nyx, the Oldest Internet Service Provider
[1440] 2026-11-23 12:08 | cnBeta (zh) | 英特尔Bartlett Lake-S处理器据称已可在MSI Z690和Z790主板原生启动 -- 英特尔Bartlett Lake-S处理器据称可在MSI Z690和Z790主板原生启动。此前测试者需要修改BIOS才能让这类纯性能核处理器运行，但最新报告显示，只需更新MSI最新BIOS，部分主板即可直接支持该系列处理器。 阅读全文
[414] 2026-11-23 14:04 | Cybernews (en) | Nvidia RTX 5090 GPU owner suspects theft after sending card for warranty repair
[749] 2026-11-22 19:51 | nytimes.com (en) | Hacker News 124 puan, 54 yorum | All Delta Flights Through Atlanta Delayed as Automated System Falters
[1441] 2026-11-23 12:08 | cnBeta (zh) | 全球土壤价值约325万亿美元 却仍长期缺乏专门国际条约保护 -- 科学家警告称，覆盖地球表面的薄薄一层土壤，支撑着人类绝大多数食物生产，同时过滤水源、储存大量碳并维系无数生命。然而，尽管土壤对粮食安全、气候稳定和生态系统至关重要，目前仍没有一项专门、具有约束力的国际条约来保护它。 阅读全文
[423] 2026-11-23 14:00 | GameGPU (en) | Where Winds Meet: GeForce RTX 50 Series GPU Benchmarks with DLSS 5
[750] 2026-11-22 19:41 | danielsada.tech (en) | Hacker News 314 puan, 162 yorum | Assume People Are Not Interested in Your Blog Post
[1442] 2026-11-23 12:07 | cnBeta (zh) | 恒星“吃掉”同类过程首次曝光，为理解紧密恒星系统形成提供重要依据 -- 据《自然》杂志19日发表的一项最新研究，天文学家利用詹姆斯·韦布空间望远镜，首次直接追踪到恒星“吃掉”同类过程：两颗恒星旋近进入共有包层阶段，随后剧烈爆发。这一名为AT 2025abao的事件，为理解紧密恒星系统的形成与其他能量剧烈的天文事件提供了重要依据。 阅读全文
[417] 2026-11-23 13:55 | Life Insurance International (en) | Arbol launches parametric protection against faster GPU depreciation
[751] 2026-11-22 19:20 | oreilly.com (en) | Hacker News 807 puan, 450 yorum | What Happened to Silicon Valley?
[1443] 2026-11-23 12:06 | cnBeta (zh) | BepiColombo探测器即将抵达水星 ESA与JAXA将于11月27日举行媒体简报会 -- 欧洲空间局（ESA）与日本宇宙航空研究开发机构（JAXA）宣布，将于11月27日在伦敦科学博物馆及线上同步举行混合媒体简报会，介绍贝皮科伦坡（BepiColombo）水星探测任务的最新进展及后续计划。该简报会定于格林尼治时间上午9时30分至11时举行，即欧洲中部时间上午10时30分至12时。 阅读全文
[430] 2026-11-23 13:28 | OC3D (en) | It didn’t work – Nvidia’s SSD-writing Hotfix needs another Hotfix
[752] 2026-11-22 19:10 | niemanlab.org (en) | Hacker News 126 puan, 130 yorum | Canada recommits to federal funding for news
[1444] 2026-11-23 12:05 | cnBeta (zh) | 微软计划从2027年开始将Wi-Fi驱动移出Windows内核 -- 微软目前正在计划从 2027 年开始将 WiFi 驱动程序移出 Windows 11 内核，即改成用户模式运行。微软的目的是减少 WiFi 驱动程序引起的黑屏死机问题，这项改进预计会在 2027 年发布的 Windows 11 中推出。 阅读全文
[424] 2026-11-23 12:59 | Rock Paper Shotgun (en) | Nvidia release hotfix for latest graphics card driver battering SSDs with excessive writes, but it looks like reverting may still be the better option
[753] 2026-11-22 19:10 | audubon.org (en) | Hacker News 631 puan, 367 yorum | The Birds of GTA 6: A Field Guide to Vice City and Leonida's Wildlife
[1445] 2026-11-23 11:58 | cnBeta (zh) | RTX6090新消息：最快明年底登场 比5090更贵 -- 近日据Moore's Law Is Dead透露， NVIDIA下一代旗舰游戏显卡RTX 6090可能在2027年底至2028年初推出，预计售价2500至3000美元(快科技注：约20033元人民币)，现款旗舰RTX 5090据称计划在2027年结束生产。 阅读全文
[406] 2026-11-23 12:30 | Yahoo Finance (en) | KIDZ AI Commences GPU Asset-Backed Lending Business to Finance AI Infrastructure
[1446] 2026-11-23 11:58 | cnBeta (zh) | 极速冲击532km/h 科尼塞克要从比亚迪手里夺回世界第一快 -- 本月初，仰望U9 Xtreme在阿布扎比跑出503.17km/h，拿下量产车极速世界纪录！ 近日，老牌超跑厂商科尼塞克宣布，旗下Jesko Absolut仍将发起极速挑战，目标冲击330英里/小时（约532km/h），意图重新夺回全球量产车极速王冠。 阅读全文
[422] 2026-11-23 12:24 | TechPowerUp (en) | Put Your GPU to Work: Offline AI Video Enhancement with Aiarty (Black Friday Deal)
[1447] 2026-11-23 11:57 | cnBeta (zh) | 《GTA6》偷“特斯拉”被自动驾驶送进警局 给马斯克看笑了 -- 据报道，《GTA6》上市后，玩家发现游戏内一款名为Coil Raiden的电动车具备特殊防盗机制。该车外形与早期Model S颇为相似，被玩家称为游戏里的“特斯拉”。 阅读全文
[433] 2026-11-23 11:10 | Wccftech (en) | Scalper Lists Over A Dozen RTX 5090 Models For Sale, Including MSI Lightning Z For $6978 As Soon As Market Witnesses Price Decline
[1448] 2026-11-23 11:50 | cnBeta (zh) | 日本汽车照明设备厂商希望到2030财年将车灯制造成本降低约30% -- 日本汽车照明设备厂商小糸制作所正加快调整产品设计与生产方式，希望到2030财年将车灯制造成本降低约30%，以应对来自中国供应商的竞争。 让这家日本老牌车灯企业感到压力的，是中国同行的价格。目前部分中国厂商的车灯产品比小糸便宜约三成，小糸副社长内山正巳坦言，中国的价格已经成为行业对标标准。 阅读全文
[405] 2026-11-23 10:17 | BGR (en) | Before You Sell Your Old Nvidia GPU, Do This
[1449] 2026-11-23 11:49 | cnBeta (zh) | 智谱发布GLM-5.4：用Loop Transformer循环28层 主攻泛Coding -- 11月23日，智谱正式发布GLM-5.4，这是其首代采用Loop Transformer架构的旗舰模型。官方称，该模型在参数量不变的情况下，通过让一组共享权重的层循环执行，获得更深的推理能力，智能水平达到2-3T级模型效果。 阅读全文
[393] 2026-11-23 09:58 | TweakTown (en) | Moore Threads' Lushan GPU could pack 64GB of VRAM, the highest capacity ever on a consumer graphics card
[1450] 2026-11-23 11:48 | cnBeta (zh) | CrowdStrike将以440亿美元收购云安全公司Zscaler -- 美国科技集团CrowdStrike已同意以约440亿美元的价格收购云安全公司Zscaler。这笔交易预计将缔造一家网络安全巨头。此次全股票收购正值全球企业和政府面临新一轮技术系统故障之际。近几周，包括微软和美国联邦航空管理局（FAA）在内的多个机构的在线系统因技术故障而瘫痪。 阅读全文
[420] 2026-11-23 08:52 | shattered.io (en) | AMD vs Nvidia: 7 RTX Tiers, 1 Confirmed Radeon Price
[1451] 2026-11-23 11:44 | cnBeta (zh) | 测试内容大调整？懂车帝：信息多处失实，部分内容为AI撰写 -- 新浪科技讯 11月23日晚间消息，近日有博主爆料称，懂车帝内部对公开测试发布了四项要求，包括不再开放对比类内容、想要发布公开测试类内容的账号必须有资质等。懂车帝方面则表示，网传“内容大调整”“行业受影响”等信息多处失实，部分内容为AI撰写，未跟平台核实求证。 阅读全文
[1452] 2026-11-23 09:53 | cnBeta (zh) | 媒体：黄仁勋夫妇已积累巨额财富 -- 据报道，黄仁勋与妻子洛丽（Lori）在英伟达股价大幅上涨前，就已将英伟达股票捐赠至不可撤销信托。创始人常会采用该方式，把股票后续增值部分剥离出自身遗产，以此规避赠与税与遗产税。纽约遗产规划律师乔纳森·布拉特马赫表示：“这是最常见的遗产规划策略。在资产价值较低时完成赠与，期待其后续大幅升值。这样一来，增值收益就不会被计入你的遗产范围。” 阅读全文
[1453] 2026-11-23 09:08 | cnBeta (zh) | 黑莓推出Windows安全浏览器 让企业可在员工个人电脑上安全办公 -- 黑莓公司近日面向Windows平台推出全新企业级浏览器BlackBerry Access Secure Browser，旨在让员工能够在个人电脑上安全处理工作事务，同时避免企业完全接管整台设备。该产品基于Chromium开发，将黑莓的数据保护能力引入浏览器层面，并加入了此前已登陆iOS和Android平台的BlackBerry Access产品线，同时取代旧版BlackBerry Access桌面客户端。 阅读全文
[1454] 2026-11-23 09:04 | cnBeta (zh) | 传懂车帝开始大调整：不能做对比测试、没资质也不行等 -- 今天有行业博主发帖爆料，汽车内容平台懂车帝接下来可能要针对内容做大调整。按照博主晒出的最新内部通知要求，平台接下来将全面收紧内容发布的权限门槛，此前可以随意做的多车横向对比测试，后续直接被划入了受限范畴，不再支持无资质的个人或者机构发布这类内容。 阅读全文
[1455] 2026-11-23 09:03 | cnBeta (zh) | 黄牛本想靠iPhone Duo大赚一笔 结果血亏20万 -- 微博话题“iPhone Duo破发”引发网友热议。 有数码城老板向记者透露，今年不少囤货的黄牛损失惨重，部分人亏损超过20万元。由于市场行情持续下跌，不少黄牛陷入“一边收货、一边跌价”的困境。总而言之，首批高价囤货的商家亏损严重。 阅读全文
[1456] 2026-11-23 09:02 | cnBeta (zh) | 达美航空周日全美超1600架次延误 重量平衡系统出现故障 -- 达美航空称，周日全美范围内超过1600架次达美航班出现延误，故障原因为一套飞机重量平衡系统中断。本次设备故障恰逢感恩节出行高峰，数百万民众计划本周出行；就在几天前，美国联邦航空管理局（FAA）因设备安装问题引发通信故障，发布了“9・11”事件以来第二次全国范围地面停飞指令。 阅读全文
[1457] 2026-11-23 08:58 | cnBeta (zh) | 比特币连跌七周 ETF资金加速撤离令市场雪上加霜 -- 投资者正加速从美国上市的比特币ETF撤资，使持续承压的比特币失去一大关键支撑。数据显示，上周12只ETF资金净流出约18亿美元，创6月以来最大单周流出规模，并连续第四周遭遇资金净流出。 阅读全文
[1458] 2026-11-23 08:57 | cnBeta (zh) | 特朗普出席迷因币持有者晚宴 币价已较峰值暴跌98% -- Morten Christensen第一次通过交易获得参加美国总统唐纳德·特朗普晚宴的资格，花了大约1200美元。第二次花了约500美元。而这一次，他说自己最终反倒赚了6美元。为了获得入场资格，Christensen买入特朗普迷因币，同时另行押注币价下跌，以便从跌势中获利。前两次活动，他采用的也是这套策略。 阅读全文
