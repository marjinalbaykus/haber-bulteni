# Görev: secim-oyunlar

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-oyunlar.json`
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

TOPIC: Oyunlar (Soulslike ve Aksiyon-RPG)

CRITERIA:
Kullanıcı özellikle soulslike ve aksiyon-RPG oyunlarını seviyor.
Önemli: bu türlerde yeni oyun duyuruları, çıkış tarihleri, büyük fragmanlar, inceleme puanları
(Metacritic/OpenCritic), büyük DLC'ler, FromSoftware ve benzeri stüdyolardan haberler,
oyun sektöründe büyük olaylar (The Game Awards kazananları, büyük stüdyo kapanışları/satın almaları).
Önemsiz: oyun rehberleri, "en iyi 10 oyun" listeleri, indirimler, küçük yamalar,
kullanıcının türüyle ilgisiz küçük oyunlar.

CANDIDATE ITEMS:
[1251] 2026-11-23 23:45 | Gematsu (en) | Grand Theft Auto VI for PS5 tops 39 million players -- More than 39 million unique users have played Grand Theft Auto VI on PlayStation 5 …
[1259] 2026-11-23 22:47 | PC Gamer (en) | GTA 6 players found a '60 fps glitch', but it’s too unstable to stop me from wishing it was on PC already -- Spoiler: It doesn't actually run at 60 fps.
[1243] 2026-11-23 13:04 | Epic Games (en) | How Mad Mimic squeezed pirate soulslike Mark of the Deep onto a phone
[1225] 2026-11-23 22:39 | Rogueliker (en) | Games like Hades 2: thrilling action-RPG roguelikes and isometric dungeon crawlers
[945] 2026-11-23 08:53 | 17173 (zh) | WeGame游戏之夜猛料！DNF三款新作集中放出重磅消息
[1252] 2026-11-23 15:00 | Gematsu (en) | INDIE Live Expo: December 1, 2026 to feature more than 100 games -- INDIE Live Expo: December 1, 2026, the next edition of the biannual independent games-focused broadcast, …
[1260] 2026-11-23 22:20 | PC Gamer (en) | God of War: Laufey star Deborah Ann Woll will reprise the role in Amazon's God of War show -- Woll will portray Faye, the "mysterious" wife of Kratos and mother of Atreus.
[1234] 2026-11-23 22:11 | IGN (en) | Vox Seas Codes (November 2026)
[1253] 2026-11-23 14:00 | Gematsu (en) | The Regreening launches December 7 -- Cozy restoration adventure game The Regreening will launch for PC via Steam on December 7, publisher …
[1261] 2026-11-23 21:33 | PC Gamer (en) | Every GTA 6 ending and how to get them -- All the endings (and a couple variations) for GTA 6 we've seen so far.
[1229] 2026-11-23 21:24 | Notebookcheck (en) | AetherCycle is on the way to early access
[1254] 2026-11-23 10:30 | Gematsu (en) | Magical Girl Taboo Village and The Streamer’s Alt Account Labyrinth delayed -- Acacia has delayed (2) visual novel Magical Girl Taboo Village to 2027, and visual novel …
[1262] 2026-11-23 21:29 | PC Gamer (en) | 2026 Steam Awards pit the GOTY winners of the past 10 years against each other in an all-out battle for supremacy -- It's the 10th anniversary of the Steam Awards and that means it's time for something new.
[1227] 2026-11-23 21:15 | IMDb (en) | George R.R. Martin’s Game of Thrones-Inspired Manga Officially Confirms Final Volume in 2027
[1255] 2026-11-23 09:00 | Gematsu (en) | Acts of Blood for PC delayed to spring 2027 -- Publisher Big Fan Games and developer Eksil Team have delayed beat ’em up action game …
[1263] 2026-11-23 21:16 | PC Gamer (en) | The Grand Theft Auto 6 hacker who was hospitalized in 2023 has been released on parole with a warning not to do it again -- Following his guilty plea, Arion Kurtaj was given a seven-year sentence but released "on license" with a warning and restrictions to his online activity..
[1233] 2026-11-23 19:51 | VGChartz (en) | Queen's Domain Sales Top 300,000 Units
[1256] 2026-11-23 02:36 | Gematsu (en) | This Week’s Japanese Game Releases: Azur Promilia, Expelled from Paradise: The Stellar Angel, more -- Azur Promilia for PC, iOS, and Android, and Expelled from Paradise: The Stellar Angel for …
[1264] 2026-11-23 20:55 | PC Gamer (en) | Cyberpunk 2 creative director says he's suffering from 'GTA 6-induced-open-world-game-dev-depression,' jokes that 'Cyberpunk 2 is cancelled' -- Tough act to follow.
[1235] 2026-11-23 19:32 | IGN India (en) | Farever - Official The Obral Ascent Release Date Trailer
[1265] 2026-11-23 19:54 | PC Gamer (en) | GTA 6 players swore the weather system was broken and the latest patch has confirmed those suspicions -- Rockstar brought weather back to Leonida.
[1226] 2026-11-23 17:50 | iXBT.games (en) | Cyberpunk action RPG became more technologically advanced — update released for The Ascent
[1266] 2026-11-23 18:01 | PC Gamer (en) | Former Dragon Age boss questions whether BioWare should have become 'a one project studio' dropping either Mass Effect or Dragon Age to survive EA -- "There are lots of studios within EA that did that and it didn't work—they still got murdered in their sleep."
[1237] 2026-11-23 17:29 | lemonwire.com (en) | Assassin’s Creed Valhalla: Experience the Viking Age on Xbox
[1267] 2026-11-23 17:54 | PC Gamer (en) | Cyberleek releases executable for GTA 6 Xbox build leaked last week: 'The torch passes to the open source recomp and emulation projects' -- The notorious GTA 6 leaker called it "an early Christmas gift."
[1231] 2026-11-23 16:50 | RPGamer (en) | Verho: Curse of Faces DLC Set for Spring
[1268] 2026-11-23 16:11 | PC Gamer (en) | This fan-made map shows why Queen's Domain is the true GOTY, and you can even upload your save to it to track the things you have left to find -- Cartographic miracles.
[1236] 2026-11-23 15:28 | IGN India (en) | Arknights: Endfield - Official Kay Operator Combat Demo Trailer
[1269] 2026-11-23 16:02 | PC Gamer (en) | 60 hours in GTA 6 has me yearning for an online Leonida -- Leonida deserves to be explored with friends.
[1238] 2026-11-23 15:10 | OtakuKart (en) | Phantom Blade Zero’s Denuvo Protection Cracked by Voices38 Despite Ongoing Lawsuit
[1270] 2026-11-23 15:36 | PC Gamer (en) | World of Warcraft: Forever review: The first proper MMO adventure I've been on in years -- A second wind for old-school MMOs that has me hooked.
[1232] 2026-11-23 12:36 | 80 Level (en) | P-Studio Claims The Next Title After Persona 6 Will Be Released Faster
[1271] 2026-11-23 15:09 | PC Gamer (en) | Trump praises Jensen Huang's reinstatement as Nvidia CEO, calls him a 'great patriot and national treasure,' while Musk says 'the mistake has been corrected' -- "The Nvidia Board of Directors has made the correct and proper decision," says the US president.
[1230] 2026-11-23 11:55 | ScreenRant (en) | Elden Ring: Rot & Sorcery Officially Confirmed For 2027
[1272] 2026-11-23 14:56 | PC Gamer (en) | Should you choose the cut or pure drugs in GTA 6? -- Sell the baby powder-spliced product or real deal in the Dealer's Choice mission.
[1239] 2026-11-23 11:31 | Niche Gamer (en) | Frame of Suffering brings Diablo-style loot and mech combat in late 2027
[1273] 2026-11-23 14:30 | PC Gamer (en) | Jensen Huang has been reinstated as Nvidia CEO: 'I take this responsibility with the greatest humility and renewed commitment' -- That was quick.
[1241] 2026-11-23 10:30 | Destructoid (en) | Clover RPG codes (November 2026)
[1274] 2026-11-23 14:30 | PC Gamer (en) | Dragon Age's former boss says that poor management from EA led to BioWare shutting down because 'the options on the table were all terrible' -- "Did they make us make Dragon Age 2? No. But they made us do something."
[1228] 2026-11-23 10:04 | Eurogamer.net (en) | Queen's Domain review - a stunning action-RPG of genuine wonder
[1275] 2026-11-23 13:08 | PC Gamer (en) | How to steal the Schafter V12 in GTA 6 -- Prove yourself to Raul by yoinking this car.
[1240] 2026-11-23 09:09 | GLITCHED (en) | Nioh 3 Complete Edition Has Been Rated in Korea
[1276] 2026-11-23 12:15 | PC Gamer (en) | GTA's treatment of women 'has not been good', says Rockstar vet delighted by 'long overdue' female protagonist in GTA 6 -- Lucia has stans everywhere.
[1242] 2026-11-23 09:08 | Destructoid (en) | Beyond Nen codes (November 2026)
[1277] 2026-11-23 12:10 | PC Gamer (en) | GTA 6 laptop password and where to find Auntie's Place in La Perle -- Solve Vanessa's laptop puzzle in the Vice City apartment.
[1278] 2026-11-23 11:25 | PC Gamer (en) | GTA 6 Kit Car locations: How to restore the fourth car for Wyman -- Repair the Reynolds Kit Car by finding some wrecks.
[1279] 2026-11-23 01:11 | PC Gamer (en) | Binding of Isaac DLC adds AI music in GTA 6-parodying update despite not having an AI disclosure, Steam reviews drop to Overwhelmingly Negative, music gets pulled -- "Woke up to a shit show," Edmund McMillen wrote.
[1280] 2026-11-23 00:07 | PC Gamer (en) | SNEG has re-released three more venerable PC strategy games including Warlords 4: Heroes of Etheria -- Let the war begin.
[1281] 2026-11-22 22:31 | PC Gamer (en) | Nvidia released a hotfix for its driver hammering your SSD, but it doesn't actually work -- AI isn't great at fixing problems AI created.
[1282] 2026-11-22 21:58 | PC Gamer (en) | GTA 6 The End of the Road: Should you swim for it, or choose no surrender? -- A choice (or two!) can lead to drastically different endings.
[1283] 2026-11-22 20:33 | PC Gamer (en) | Unreal Engine 6 to jettison key scripting feature earlier than planned in favor of AI coding -- Bye-bye, Blueprints.
[1284] 2026-11-22 20:16 | PC Gamer (en) | GTA 6 needed a dedicated team to make sure all its different body types could share animations: 'It was a huge team effort' -- Making sure your animations don't break while you're cultivating mass.
[1285] 2026-11-22 20:09 | PC Gamer (en) | New Dota 2 patch tries to help games end faster, adds an item that summons super creeps in every lane at once -- It's awfully reminiscent of ideas you can find in other, more fast-paced MOBAs.
[1286] 2026-11-22 18:43 | PC Gamer (en) | GTA 6 discourse is suffering from the flat earther effect -- Didn't you hear? You can't even steal cars anymore.
[1287] 2026-11-22 18:00 | PC Gamer (en) | Which GTA 6 radio station is your favourite so far? -- The music of Leonida is so damn good.
