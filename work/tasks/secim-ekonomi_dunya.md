# Görev: secim-ekonomi_dunya

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-ekonomi_dunya.json`
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

TOPIC: Ekonomi - Dünya

CRITERIA:
Çıta çok yüksek. Sadece küresel piyasaları ciddi etkileyen olaylar girsin.
Önemli: piyasaları sarsan beklenmedik merkez bankası kararları, borsalarda çöküş veya sert yükselişler,
büyük ticaret savaşı/gümrük vergisi hamleleri, büyük bankacılık krizleri, petrol şokları.
Önemsiz: beklentiye uygun Fed kararları ve ardından gelen küçük hareketler
(örneğin "Fed faizi beklendiği gibi değiştirdi, altın yüzde 3 arttı" gibi), analist tahminleri.
Piyasa verisinde eşik aşılmışsa bunu mutlaka değerlendir.

MARKET DATA (thresholds marked):
- Altın (ons, USD): 3615.0 (1 günde %-1.0, 7 günde %-6.5, 30 günde %-9.8)
- Brent petrol: 86.18 (1 günde %-3.6, 7 günde %-7.5, 30 günde %-26.2) [EŞİK AŞILDI]
- S&P 500: 7237.17 (1 günde %+4.7, 7 günde %-2.4, 30 günde %-5.1) [EŞİK AŞILDI]
- Nasdaq: 24089.56 (1 günde %+6.5, 7 günde %-1.7, 30 günde %-7.2) [EŞİK AŞILDI]
- Bitcoin: 60180.82 (1 günde %-1.7, 7 günde %-7.3, 30 günde %-24.8)

CANDIDATE ITEMS:
[1094] 2026-11-23 23:47 | WSMV (en) | Amazon launches Prime Air delivery drones in Mt. Juliet
[1125] 2026-11-23 23:08 | Barron's (en) | HPE Stock Drops After Earnings Beat. Here’s Why.
[1180] 2026-11-23 23:57 | Proactive financial news (en) | Australia faces 50% recession risk as unemployment surges
[1093] 2026-11-23 23:15 | 매일경제 (en) | Unexpected signs of a rebound came from the U.S. stock market while the KOSPI, which had been lookin..
[1112] 2026-11-23 22:25 | Yahoo Finance (en) | Wall Street soars on Huang's return to Nvidia, Treasury yields rise
[1161] 2026-11-23 23:55 | Davis Wright Tremaine (en) | What to Expect in U.S. Tariff Policy Following the 2026 Midterm Elections
[1092] 2026-11-23 21:21 | SchiffGold.com (en) | The Blue Wave Was Economic
[1106] 2026-11-23 22:20 | AP News (en) | US stocks surge and recover half of last week’s plunge after Jensen Huang returns as Nvidia’s CEO
[1171] 2026-11-23 23:36 | Yahoo Finance (en) | Tariffs Strike Again: 343 Companies Report $28.5 Billion in Trump Tariff Refunds. Retail Beats Need a Second Look
[1095] 2026-11-23 21:18 | 24/7 Wall St. (en) | Nvidia Just Had Its Best Day Since 2003 on Jensen Huang's Return. Here's What the Stock Did After Its 5 Biggest Days
[1144] 2026-11-23 22:19 | Yahoo Finance (en) | As Tech Stocks Crash, Is Berkshire Hathaway a Safe-Haven Investment?
[1204] 2026-11-23 23:09 | CBC (en) | 'Tough to hear' some U.S. tariffs will remain in future trade deal: N.S. premier
[1089] 2026-11-23 20:57 | StoneX (en) | Core PCE Inflation vs. Softer Data in the Debate Over Fed Rate Hikes
[1114] 2026-11-23 22:16 | Yahoo Finance (en) | Stock Market Today, Nov. 23: Markets Rebound From Correction on AI Relief Rally
[1163] 2026-11-23 22:51 | Inside Lighting (en) | Tariffs Reshape Sourcing at Canada Light Expo
[1098] 2026-11-23 16:03 | 24/7 Wall St. (en) | Nvidia Is Soaring on Jensen Huang's Return. History Says the Biggest Rallies of the Dot-Com Crash Came on the Way Down, and Cisco Fell 79% After Its 24% Day
[1142] 2026-11-23 22:08 | ICIS (en) | US chems shares mixed amid massive tech-stock rally
[1206] 2026-11-23 22:37 | EnergyNow (en) | COMMENTARY: Here’s How to Attract Investment to Newfoundland Despite Trump’s Tariffs - Fraser Institute
[1099] 2026-11-23 15:31 | TradingView (en) | Fed 'Jedi central banking' won't stop inflation - TS Lombard’s Dario Perkins
[1150] 2026-11-23 22:04 | MarketWatch (en) | Nvidia’s stock is surging again. What to know before chasing the AI rally.
[1164] 2026-11-23 22:10 | Mexico Business News (en) | US Tariffs Affect Costs at 45% of Mexican Companies: HSBC
[1101] 2026-11-23 15:30 | CaptainAltcoin (en) | ChatGPT Predicts Bitcoin and XRP Prices After the December Fed Meeting
[1129] 2026-11-23 22:01 | WSJ (en) | U.S. Stocks Jump as Huang Return Triggers AI Rally
[1167] 2026-11-23 21:47 | Fox Business (en) | Bill Ackman says Federal Reserve is 'about to make another mistake'
[1090] 2026-11-23 13:35 | Forbes (en) | Serious U.S. Treasury Secretary Warning To The Fed Puts Bitcoin And Crypto On The Brink Of A Surprise Price Shock
[1135] 2026-11-23 21:51 | CBC (en) | North American stocks soar, recovering half of last week's plunge after Jensen Huang returns as Nvidia's CEO
[1173] 2026-11-23 21:31 | Harlem World Magazine (en) | In Harlem, The Trade War Reaches The Dance Floor
[1096] 2026-11-23 12:26 | www.tmgm.com (en) | November US S&P Global PMI expected to soften
[1108] 2026-11-23 21:44 | Fortune (en) | 'Doesn't answer the big questions': The stock market is still $5 trillion below its AI-fueled peak despite Monday's rally
[1159] 2026-11-23 21:14 | IPWatchdog (en) | EUIPO Research Paper Shows EU Trademark Filings Cointegrated with GDP Growth, Recession
[1100] 2026-11-23 11:09 | U.Today (en) | Cardano (ADA) Hits Major Death Cross, Will 2026 Year-End Prices Surprise?
[1137] 2026-11-23 21:42 | Bloomberg.com (en) | Wall Street Storms Back as AI Trade Rebounds With Huang’s Return
[1182] 2026-11-23 21:12 | Yahoo Finance (en) | Trinity Industries Sees Railcar Deliveries Rebounding in 2027, but Tariffs Cloud Outlook
[1120] 2026-11-23 21:23 | BBC (en) | Nvidia value climbs by almost $1tn and tech stocks rally after Jensen Huang returns
[1209] 2026-11-23 21:09 | Sault Star (en) | Shoemaker puts tariffs, tourism on agenda with Michigan mayor
[1140] 2026-11-23 21:11 | Bloomberg.com (en) | Stocks Soar as Huang’s Nvidia Return Fuels AI Bets: Markets Wrap
[1177] 2026-11-23 20:30 | The Korea Herald (en) | [Shang-Jin Wei] Export restraints over tariffs
[1124] 2026-11-23 20:51 | MarketWatch (en) | Jensen Huang’s return to Nvidia saved the stock market — for now. Will the rebound last?
[1195] 2026-11-23 20:15 | The Center Square (en) | New York farmers to get $23M tariff ‘relief’ payments
[1151] 2026-11-23 20:38 | Stocks Down Under (en) | Is the US Stock Market Crash Over? Nasdaq Surges 6.6% as Nvidia Sparks Massive Rally
[1172] 2026-11-23 20:05 | Wausau Pilot & Review (en) | Marathon County bucking national trend as existing-home sales hit lowest level since Great Recession
[1122] 2026-11-23 19:54 | MarketWatch (en) | Why Monday’s huge stock-market rally is actually a warning sign for bulls
[1186] 2026-11-23 20:00 | Yahoo (en) | Trump impersonator joins Canadian PM. Here's the latest on the trade war
[1116] 2026-11-23 19:45 | WSJ (en) | Nvidia’s Rally Ripples Across Markets
[1178] 2026-11-23 19:57 | WWNY (en) | North Country farmers getting $2.17M in tariff relief
[1154] 2026-11-23 19:38 | Yahoo Finance (en) | Why Jensen Huang is the 'key man' behind the market rally
[1165] 2026-11-23 19:54 | WBNG (en) | Schumer vows to roll back manufacturing tariffs during visit to Greene business
[1149] 2026-11-23 19:20 | Yahoo Finance (en) | Tech stocks surge as Nvidia rally adds $2 trillion to Nasdaq 100
[1179] 2026-11-23 19:05 | Law360 (en) | EU, Indonesia Finalize Deal To Eliminate Most Tariffs
[1107] 2026-11-23 19:13 | Bloomberg.com (en) | Asian Stocks to Rally as Huang’s Return Revives AI: Markets Wrap
[1169] 2026-11-23 19:00 | The Washington Post (en) | Opinion | The Supreme Court might cross more than Trump at the border
[1146] 2026-11-23 17:50 | Yahoo Finance (en) | Anthropic stock falls as Monday's tech rally takes off
[1200] 2026-11-23 19:00 | Yahoo (en) | Chuck Schumer Blames Trump’s ‘Disastrous War,’ Tariffs and Trade Dispute With Canada for Rising Thanksgiving Grocery Prices: Says American People Sent 'MAGA Majorities In Congress Packing'
[1121] 2026-11-23 17:48 | Reuters (en) | AI rally set to add $2 trillion to Nasdaq-100 as Huang returns to Nvidia helm
[1201] 2026-11-23 18:57 | Niagara Frontier Publications (en) | NYS: 167 NYS farms to benefit from $2,196,286.63 in tariff relief
[1155] 2026-11-23 17:27 | Yahoo Finance (en) | Anthropic stock falls, sitting out Monday's rally
[1160] 2026-11-23 18:48 | Tax Foundation (en) | How Value-Added in Trade Complicates the Tariff Picture
[1148] 2026-11-23 16:53 | CNBC (en) | ‘This establishes a bottom’: Jensen Huang’s return seen as likely to stop market sell-off, for now
[1184] 2026-11-23 18:27 | The Brussels Times (en) | EU-Indonesia landmark deal cuts tariffs on 98% of trade, saving millions
[1133] 2026-11-23 16:00 | The Motley Fool (en) | The Best ETF to Buy if You're Nervous About a Stock Market Crash
[1187] 2026-11-23 18:00 | The Motley Fool (en) | If a Recession Hits in 2027, This Is the Dividend Stock I Want to Own
[1132] 2026-11-23 15:17 | TheBanker.com (en) | How exposed is the US economy to an AI market crash?
[1197] 2026-11-23 17:46 | The National Law Review (en) | The Government Challenges Broad IEEPA Tariff Refunds: What Importers Need to Know
[1131] 2026-11-23 15:00 | Council on Foreign Relations (en) | Boom or Bubble? Understanding AI and the U.S. Economy
[1196] 2026-11-23 17:16 | Steel Market Update (en) | Leibowitz: Who did the tariffs on steel and aluminum help, and who did they hurt?
[1117] 2026-11-23 14:00 | Los Angeles Times (en) | Column: Possible stock market crash means California, Becerra need to wise up
[1188] 2026-11-23 16:31 | Sustainable Views (en) | Regulatory Review: COP31 concludes, SFDR deal and carbon border tariffs
[1136] 2026-11-23 13:07 | The Guardian (en) | Wild stock market swings: will I lose money, and is there anything I can do?
[1170] 2026-11-23 16:25 | Spectrum Noticias (en) | Tariffs and healthcare: El-Sayed outlines priorities ahead of taking office
[1130] 2026-11-23 11:10 | CNBC (en) | Futures rally after Wall Street's nightmare week
[1185] 2026-11-23 16:21 | WIVT - News 34 (en) | New York delivers direct relief to farmers impacted by tariffs
[1128] 2026-11-23 09:23 | Fortune (en) | The stock market faces a worrying scenario: a sell-off with no flight to safety
[1174] 2026-11-23 16:01 | Reuters (en) | India seeks to resolve dispute over Scotch tariffs after UK raises concerns
[1199] 2026-11-23 15:53 | Notes on the Crises (en) | With Tech Stocks (Led by Nvidia) Plunging, is a Recession Around the Corner?
[1166] 2026-11-23 15:01 | Reuters (en) | UK raises concerns with India over high Scotch tariffs, commerce secretary says
[1212] 2026-11-23 14:49 | www.baconsrebellion.com (en) | Trade Wars: Why Freedom Trumps Tariffs
[1191] 2026-11-23 14:20 | Yahoo Finance (en) | If a Recession Hits in 2027, History Says These 4 ETFs Can Weather the Storm
[1205] 2026-11-23 14:15 | Buffalo News (en) | Hochul says farms to reap millions in relief from Trump tariffs to offset losses, costs
[1193] 2026-11-23 13:30 | electrive.com (en) | EU and Indonesia agree free trade deal to cut tariffs on cars
[1176] 2026-11-23 13:00 | Yahoo! Finance Canada (en) | Trump hit US$20B of Canadian goods with a 96-year-old Smoot-Hawley law that has no expiry date. Can Ottawa undo it?
[1192] 2026-11-23 12:48 | PV Tech (en) | First Solar CEO backs bipartisan support for Trump’s Section 232 tariffs
[1202] 2026-11-23 12:00 | MedWatch (en) | Lundbeck CEO rules out US production plans depite new tariffs
[1194] 2026-11-23 10:57 | MarketWatch (en) | The U.S. may enter a recession if the Fed raises rates, hedge-fund manager Bill Ackman says
