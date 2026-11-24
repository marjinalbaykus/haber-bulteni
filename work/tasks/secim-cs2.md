# Görev: secim-cs2

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-cs2.json`
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

TOPIC: CS2

CRITERIA:
Önemli: Major ve büyük turnuvaların (IEM, BLAST, ESL Pro League, PGL) sonuçları ve finalleri,
büyük kadro değişiklikleri (yıldız oyuncu transferleri), Valve'ın büyük oyun güncellemeleri
ve kural değişiklikleri, Türk oyuncu ve takımlarının büyük başarıları.
Önemsiz: küçük turnuva elemeleri, sıradan kasa/skin haberleri, kısa süreli deneme oyuncuları.

CANDIDATE ITEMS:
[996] 2026-11-23 23:37 | HLTV.org (en) | Valve ranking of Counter-Strike Teams
[960] 2026-11-23 23:27 | Pley.gg (en) | CS2 update 24 November: Cobblestone loses unreachable bomb spot
[990] 2026-11-23 23:06 | HLTV.org (en) | Valve Team ranking for Europe on November 24th, 2026
[954] 2026-11-23 21:49 | Pley.gg (en) | PGL Major Singapore 2026
[986] 2026-11-23 22:27 | HLTV.org (en) | Aces team overview
[952] 2026-11-23 21:20 | Legal Sports Betting (en) | CS2 Singapore Major Stage 1 Betting Analysis For Opening BO1s
[988] 2026-11-23 20:12 | HLTV.org (en) | cs gigachad community?3 - HLTV
[948] 2026-11-23 20:11 | Players (en) | PGL Major Singapore 2026 Starts Tomorrow — Schedule, Ukrainians and Tournament Format
[987] 2026-11-23 18:39 | HLTV.org (en) | - HLTV.org
[972] 2026-11-23 20:03 | Freetips.com (en) | PGL Major Singapore 2026 Tournament Betting Preview & Guide – MOUZ Can Deliver in Singapore
[978] 2026-11-23 18:03 | HLTV.org (en) | HLTV.org - The home of competitive Counter-Strike
[950] 2026-11-23 18:03 | Dust2.us (en) | CS2 player count drops for 9th consecutive month
[981] 2026-11-23 16:02 | HLTV.org (en) | SunPayus opens up about early struggles in CS2, what didn't work in G2, and signing with FUT
[949] 2026-11-23 17:20 | Esports.gg (en) | Stage 1 kicks off CS2 Singapore Major 2026: Results from the early Swiss clashes
[979] 2026-11-23 14:17 | HLTV.org (en) | ztr's FOKUS loan ends
[962] 2026-11-23 16:57 | Pley.gg (en) | Singapore Major Pick'em: what pros and creators picked
[989] 2026-11-23 13:24 | HLTV.org (en) | Rolling schedule for Major?3 - HLTV
[961] 2026-11-23 16:00 | Pley.gg (en) | Who can win the Singapore Major? Five teams, one favourite
[980] 2026-11-23 12:58 | HLTV.org (en) | NAVI bag $466,521 after topping ESL's Annual Club Incentive leaderboard
[946] 2026-11-23 14:40 | Esports.gg (en) | Pick’Ems for CS2 Singapore Major 2026: Prediction ideas and bracket analysis
[983] 2026-11-23 11:37 | HLTV.org (en) | Short news: Week 46
[971] 2026-11-23 12:26 | Pley.gg (en) | Rare Atom’s ChildKing admits drinking before qualifier semi-final
[982] 2026-11-23 10:10 | HLTV.org (en) | PGL Major Singapore teams, format, schedule, talent, fantasy
[955] 2026-11-23 12:20 | TalkEsport (en) | CS2 Singapore Major 2026 Starts Today: Stage 1 Teams, Schedule, and Where to Watch
[969] 2026-11-23 11:48 | Pley.gg (en) | NAVI take largest share of ESL’s 2026 club incentive
[953] 2026-11-23 11:38 | EGamersWorld (en) | PGL Major Singapore 2026 Stage 1 Preview: Teams, Schedule and Format
[947] 2026-11-23 10:42 | GosuGamers (en) | CS2 PGL Major Singapore 2026 - All participating teams, format, prize pool, and more
[964] 2026-11-23 10:30 | Pley.gg (en) | PGL Major Singapore trophy design leaked
[970] 2026-11-23 10:13 | Pley.gg (en) | FACEIT keep half-time voice chat for Season 10
[963] 2026-11-23 09:00 | Pley.gg (en) | Quiz: Can you name every team that has lost a CS Major final?
