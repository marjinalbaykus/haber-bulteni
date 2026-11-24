# Görev: secim-yerel_llm

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-yerel_llm.json`
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

TOPIC: Yerel LLM (kendi bilgisayarında çalışan modeller)

CRITERIA:
Kullanıcı modelleri kendi bilgisayarında çalıştırıyor: AMD RX 7800 XT, 16 GB VRAM.
İleride ikinci bir kart alıp toplam 32 GB VRAM'e çıkabilir. AMD olduğu için ROCm, Vulkan,
llama.cpp, LM Studio, Ollama desteği önemli.
Önemli: 16-32 GB VRAM'e (4-bit veya benzeri quantization ile) sığan yeni açık ağırlıklı modeller,
kendi boyutunda rekor kıran küçük/orta modeller, büyük laboratuvarların açık model çıkışları
(Qwen, Llama, Mistral, Gemma, DeepSeek, GLM, Phi vb.), AMD GPU'larda performansı ciddi artıran
yazılım güncellemeleri, önemli quantization veya çıkarım tekniği gelişmeleri.
Her haberde modelin boyutunu ve 16 GB / 32 GB VRAM'e sığıp sığmadığını belirt.
Puanlama kuralı: 4-bit ile 32 GB VRAM'e sığmayan modeller normalde en fazla 6 puan alır (bültene girmez).
İstisna: alanı gerçekten sarsan çok büyük olaylar yine de girsin (8 ve üstü). Örneğin açık bir modelin
en iyi kapalı modelleri (GPT, Gemini, Claude) geçmesi, büyük bir laboratuvarın yeni amiral gemisi açık modeli,
ya da yerel LLM dünyasını etkileyecek tarihi bir gelişme. Bu durumda VRAM'e sığmadığını açıkça belirt.
Önemsiz: 32 GB'a sığmayan niş veya alan-özel modeller (tıp, hukuk vb.),
ufak fine-tune'lar, "uncensored" kopyalar, tanıtım amaçlı içerikler.

CANDIDATE ITEMS:
[127] 2026-11-23 21:50 | The Defiant (en) | Cohere Backs Aleph Alpha’s Open-Weight Kolibri Update
[931] 2026-11-23 21:55 | 재경일보 (en) | Thought it was an AI program… malware targeting antivirus software first : 테크/과학 : JKN
[188] 2026-11-23 23:51 | 证券之星 (zh) | 开源晨会
[715] 2026-11-23 23:11 | foreignpolicy.com (en) | Hacker News 298 puan, 234 yorum | Huang's Return Is the Biggest National Security Scandal in U.S. History
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file
[137] 2026-11-23 20:03 | Fello AI (en) | Best Chinese AI Models in 2026: Ranked and Compared
[924] 2026-11-23 19:49 | Hostinger (en) | Ollama CLI tutorial: Learn how to use Ollama in the terminal
[185] 2026-11-23 22:57 | 手机新浪网 (zh) | 智能体时代操作系统夯实根底座
[716] 2026-11-23 22:32 | stalw.art (en) | Hacker News 428 puan, 221 yorum | Stalwart is now a drop-in replacement for Microsoft Exchange
[1082] 2026-11-23 03:55 | Hugging Face (en) | HF trend puanı 122, 134 beğeni | Hugging Face trend modeli: inclusionAI/Ling-3.2-air -- tür: bilinmiyor
[123] 2026-11-23 19:23 | Reuters (en) | Trump saw Nvidia's Huang as protector of open-weight AI models, sources say
[928] 2026-11-23 17:23 | Bioengineer.org (en) | Local-First Open-Source AI Teaching Assistant RUCAI Anchors Classroom Answers to Assigned Materials
[176] 2026-11-23 22:39 | 上观新闻 (zh) | 如何让AI红利为更多人所共享？中国答案：让人工智能成为造福人类的公共产品
[717] 2026-11-23 22:09 | github.com (en) | Hacker News 325 puan, 287 yorum | VP ≠ VNP over Every Field
[1085] 2026-11-21 18:46 | Hugging Face (en) | HF trend puanı 84, 100 beğeni | Hugging Face trend modeli: strata-engine/Inkling-2-Mini-Strata -- tür: image-text-to-text
[132] 2026-11-23 19:15 | VentureBeat (en) | AWS Strands Decider v2 reads context both ways
[933] 2026-11-23 14:45 | ad-hoc-news.de (en) | AMD stock: AMD completed Taalas deal to expand AI inference
[162] 2026-11-23 19:31 | 三易生活 (zh) | 蚂蚁阿福宣布健康服务升级，并开源医疗大模型
[718] 2026-11-23 21:01 | bored.com (en) | Hacker News 227 puan, 50 yorum | Bored.com: Fun, interesting and cool websites to explore when bored
[1087] 2026-11-20 22:34 | Hugging Face (en) | HF trend puanı 69.5, 282 beğeni | Hugging Face trend modeli: OpenUncensored/Inkling-2-Mini-Uncensored -- tür: image-text-to-text
[130] 2026-11-23 16:42 | Startup Fortune (en) | Ant Group's Afu app starts giving away LingSante medical AI models under an MIT license
[932] 2026-11-23 13:59 | Online Tech Tips (en) | How to Run Local LLMs on Windows and Mac With Ollama
[178] 2026-11-23 19:31 | 智源社区 (zh) | 美众议院两委员会发布《Beijing's AI Playbook》：报告称中国开源模型占Hugging Face 年下载量41%，智谱GLM-5.3 据评估可自主构建端到端漏洞利用
[719] 2026-11-23 20:52 | github.com (en) | Hacker News 243 puan, 96 yorum | GPUI is getting its own repository
[1077] 2026-11-20 20:42 | Hugging Face (en) | HF trend puanı 408.09999999999997, 687 beğeni | Hugging Face trend modeli: AtomicChat/Inkling-2-Mini-Uncensored-GGUF -- tür: image-text-to-text
[131] 2026-11-23 15:58 | The Standard (HK) (en) | Trump intervened in Nvidia reinstatement, seeing Huang as key to US open-weight AI, sources say
[923] 2026-11-23 10:32 | shattered.io (en) | What Is GGUF? Quantize a Model in 12 Steps, 70 Min
[201] 2026-11-23 17:25 | yeeyi (zh) | 人大高瓴开源AutoMaster：把智能体变“教研主任”，让大模型教得更好-yeeyi
[720] 2026-11-23 19:17 | arm.jonpalmisc.com (en) | Hacker News 1009 puan, 230 yorum | Per Arm's request, this site will be discontinued on 4 December 2026 :(
[1088] 2026-11-20 13:14 | Hugging Face (en) | HF trend puanı 69, 77 beğeni | Hugging Face trend modeli: MemTensor/MemT-1.0-Pro -- tür: text-generation
[140] 2026-11-23 15:13 | Kingy AI (en) | Baseten Releases bison-1 and groundhog-1, Two Gemma 4 Decision Models
[927] 2026-11-23 09:31 | Medium (en) | Can Your PC Run Local AI? A 5-Minute Check Before You Upgrade | by Himansh | Nov, 2026
[167] 2026-11-23 15:56 | 新浪财经 (zh) | 人大高瓴开源AutoMaster：把智能体变“教研主任”，让大模型教得更好
[721] 2026-11-23 18:48 | github.com (en) | Hacker News 568 puan, 130 yorum | Apple removed my open-source app from EU App Stores after 9 years
[1076] 2026-11-20 13:09 | Hugging Face (en) | HF trend puanı 1021, 1158 beğeni | Hugging Face trend modeli: unsloth/Inkling-2-Mini-GGUF -- tür: image-text-to-text
[139] 2026-11-23 14:28 | Fello AI (en) | GLM 5.4: Z.ai’s Loop Transformer, Access and Real Cost
[929] 2026-11-23 09:10 | Notebookcheck (en) | Copilot+ PCs: Why the NPU is no longer a reason to buy a laptop
[172] 2026-11-23 14:28 | www.itsdw.cn (zh) | 沐曦GPU率先完成中电信星辰大模型Day0适配
[722] 2026-11-23 17:17 | blog.kagi.com (en) | Hacker News 415 puan, 331 yorum | Kagi Mail (public beta)
[1086] 2026-11-19 04:31 | Hugging Face (en) | HF trend puanı 74, 93 beğeni | Hugging Face trend modeli: inclusionAI/LingSante-3.1-air-VL -- tür: image-text-to-text
[136] 2026-11-23 13:18 | ANI News (en) | No government support for running open-weight AI models, says Sarvam's co-founder amid Chinese models row
[164] 2026-11-23 13:17 | 电子工程专辑EE Times China (zh) | 上海团队开源了首个国产记忆模型，554B！这也太狠了
[723] 2026-11-23 17:07 | twitter.com (en) | Hacker News 1613 puan, 903 yorum | "Dario is hyping the dangers so OSS models are illegal and he stays in control"
[1084] 2026-11-19 04:30 | Hugging Face (en) | HF trend puanı 103, 125 beğeni | Hugging Face trend modeli: inclusionAI/LingSante-3.1-flash -- tür: text-generation
[145] 2026-11-23 12:12 | Pasquale Pillitteri (en) | What is a looped transformer and why Inkling 2 Mini uses one
[179] 2026-11-23 12:51 | Sohu (zh) | 微信新开源模型，试图打造面向端侧应用的瑞士军刀
[724] 2026-11-23 16:13 | ladybird.org (en) | Hacker News 1856 puan, 428 yorum | Ladybird is now in alpha
[1083] 2026-11-18 05:31 | Hugging Face (en) | HF trend puanı 112, 348 beğeni | Hugging Face trend modeli: RadixArk/DaisyRose-27B -- tür: image-text-to-text
[146] 2026-11-23 12:06 | Pasquale Pillitteri (en) | Z.ai launches GLM-5.4, its first loop transformer with 750 billion parameters
[159] 2026-11-23 12:19 | 智东西 (zh) | 百灵甩出健康模型全家桶：开源、专业、死磕智效比
[725] 2026-11-23 14:49 | reddit.com (en) | Hacker News 287 puan, 306 yorum | Cyberleek posted the GTA 6 Xbox build executable
[1080] 2026-11-17 10:16 | Hugging Face (en) | HF trend puanı 210.5, 654 beğeni | Hugging Face trend modeli: sensenova/SenseNova-Loop-9B -- tür: image-text-to-text
[144] 2026-11-23 11:15 | explainx.ai (en) | GLM-5.4 Coding Plan: +50% Quota, Vision (Nov 2026)
[168] 2026-11-23 11:40 | www.anfangnews.com (zh) | 全球大模型都出自哪些城市？京杭占三成_人工智能
[726] 2026-11-23 14:01 | nvidianews.nvidia.com (en) | Hacker News 1708 puan, 4 yorum | Jensen Huang Reinstated as Nvidia CEO
[1079] 2026-11-17 03:44 | Hugging Face (en) | HF trend puanı 226, 661 beğeni | Hugging Face trend modeli: tencent/Hy4-flash -- tür: image-text-to-text
[150] 2026-11-23 10:31 | Business Upturn (en) | Yann LeCun challenges Dario Amodei over open-weight AI restrictions
[190] 2026-11-23 11:33 | InfoQ.cn (zh) | 重塑财务职能：引领 AI 时代的转型
[727] 2026-11-23 14:00 | cnbc.com (en) | Hacker News 2518 puan, 3438 yorum | Nvidia Reinstates Jensen Huang as CEO
[1075] 2026-11-17 00:26 | Hugging Face (en) | HF trend puanı 1158.5, 5583 beğeni | Hugging Face trend modeli: thinkingmachines/Inkling-2-Mini -- tür: image-text-to-text
[142] 2026-11-23 10:12 | Analytics India Magazine (en) | Sarvam Defends Hosting Chinese AI Models, Releases Upgraded 105B Model
[195] 2026-11-23 11:30 | www.anfangnews.com (zh) | 端侧AI走进卧室，联发科Genio 420加速智能家居进化_新品
[729] 2026-11-23 12:28 | github.com (en) | Hacker News 231 puan, 121 yorum | Win4wine – Run Windows Shell as the Linux Desktop on Wine
[1081] 2026-11-16 17:21 | Hugging Face (en) | HF trend puanı 191, 575 beğeni | Hugging Face trend modeli: apple/FastFrames-L -- tür: text-to-video
[147] 2026-11-23 10:11 | YourStory.com (en) | Sarvam's new AI model promises better performance at lower cost
[153] 2026-11-23 11:16 | 新浪新闻_手机新浪网 (zh) | 国产大模型引领全球开源生态（大数据观察·智能经济新形态）
[730] 2026-11-23 11:34 | blogs.gentoo.org (en) | Hacker News 243 puan, 150 yorum | The next circle of security hell
[1078] 2026-11-16 06:52 | Hugging Face (en) | HF trend puanı 354.5, 1048 beğeni | Hugging Face trend modeli: harness-hub/Qwen3.8-27B-pi-adapted -- tür: image-text-to-text
[128] 2026-11-23 09:28 | Industrial Cyber (en) | House report flags Chinese AI models embedded in US businesses, cites cybersecurity and national security risks
[163] 2026-11-23 11:10 | www.anfangnews.com (zh) | 智谱GLM-5.4大模型发布：仍是开源最强、比Opus 5.5差距5%内_人工智能
[731] 2026-11-23 11:31 | github.com (en) | Hacker News 308 puan, 172 yorum | Nospeaker ─ Make your MacBook's trackpad talk using its haptic motor
[141] 2026-11-23 09:00 | Superpower Daily (en) | Daily AI Newsletter: Mathematician claims Riemann proof with Claude
[169] 2026-11-23 10:46 | 极客公园 (zh) | 记忆张量与英伟达联合发布，评测最高分的开源记忆原生模型
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[143] 2026-11-23 08:56 | Moneycontrol.com (en) | Yann LeCun criticises Anthropic's Dario Amodei stance on open-weight AI models; says restricting AI...
[197] 2026-11-23 10:20 | www.anfangnews.com (zh) | 天润融通智能门锁案例:ZENAVA优化Agent与人工协同,释放约50%座席产能_企业
[733] 2026-11-23 09:55 | jola.dev (en) | Hacker News 172 puan, 109 yorum | Don't keep your SSH keys on disk
[193] 2026-11-23 10:12 | 36氪 (zh) | 成为 DeepSeek 的股东有什么好处
[734] 2026-11-23 05:00 | micjm.com (en) | Hacker News 263 puan, 340 yorum | Yes, 17M lines of Lean is a lot
[171] 2026-11-23 10:07 | 集微网 (zh) | 燧原科技Day 0适配腾讯混元Hy4与Hy4 flash，为领先开源模型提供全栈推理底座
[735] 2026-11-23 04:31 | facebook.com (en) | Hacker News 218 puan, 80 yorum | DeFlock Amerca Facebook Group Suspended as a Dangerous Organization
[184] 2026-11-23 10:04 | CSDN (zh) | RPA与AI Agent融合：AstronRPA开源企业级自动化平台深度解析
[736] 2026-11-23 03:49 | github.com (en) | Hacker News 609 puan, 245 yorum | XNU Linux: Full OS X GUI Userspace on Linux
[189] 2026-11-23 09:44 | CSDN (zh) | CloddsBot：开源AI交易代理的Node.js工程实践
[737] 2026-11-23 03:34 | docker.com (en) | Hacker News 123 puan, 202 yorum | The New Docker: Built to Run on Trust
[196] 2026-11-23 09:40 | www.anfangnews.com (zh) | 智能门锁主流品牌有哪些：三款高端AI门锁横评拆解_评测
[738] 2026-11-23 01:50 | pengowray.github.io (en) | Hacker News 192 puan, 76 yorum | Treemap of the Riemann Hypothesis Lean Proof
[199] 2026-11-23 09:30 | www.anfangnews.com (zh) | 前10月低空飞行超2585万架次，四川低空经济加快“起飞”
[739] 2026-11-23 01:13 | spelunkr.tv (en) | Hacker News 990 puan, 170 yorum | Free alternative TV network built from the Internet Archive's forgotten videos
[198] 2026-11-23 09:10 | www.anfangnews.com (zh) | 国网永川供电公司：电力巡检下沉合作社 粮食归仓用电全保障
[740] 2026-11-22 23:57 | alvinbunk.wordpress.com (en) | Hacker News 306 puan, 322 yorum | Nespresso's project to reduce grams of coffee per capsule
[200] 2026-11-23 09:06 | 深潮TechFlow (zh) | 不炒 Meme 改做“机器支付宝”？Solana 亲自下场，抢当 AI 智能体的底层银行
[741] 2026-11-22 23:12 | claude.ai (en) | Hacker News 257 puan, 201 yorum | Progress map for the Lean formalization of Alessandro Masullo's manuscript
[170] 2026-11-23 08:50 | 中金在线 (zh) | 【明日主题前瞻】蚂蚁百灵医疗健康大模型家族宣布全面开源_市场测评_市场_中金在线
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
