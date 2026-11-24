# Görev: detay-14

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-14.json`
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

TOPIC: Oyunlar (Soulslike ve Aksiyon-RPG)

CRITERIA:
Kullanıcı özellikle soulslike ve aksiyon-RPG oyunlarını seviyor.
Önemli: bu türlerde yeni oyun duyuruları, çıkış tarihleri, büyük fragmanlar, inceleme puanları
(Metacritic/OpenCritic), büyük DLC'ler, FromSoftware ve benzeri stüdyolardan haberler,
oyun sektöründe büyük olaylar (The Game Awards kazananları, büyük stüdyo kapanışları/satın almaları).
Önemsiz: oyun rehberleri, "en iyi 10 oyun" listeleri, indirimler, küçük yamalar,
kullanıcının türüyle ilgisiz küçük oyunlar.

HEADLINE: Aksiyon-RPG Queen's Domain Eurogamer'da 'hayranlık uyandıran' incelemesi aldı ve 300 bin satışı geçti.

RELATED ITEMS:
[1228] 2026-11-23 10:04 | Eurogamer.net (en) | Queen's Domain review - a stunning action-RPG of genuine wonder
[1233] 2026-11-23 19:51 | VGChartz (en) | Queen's Domain Sales Top 300,000 Units
[1268] 2026-11-23 16:11 | PC Gamer (en) | This fan-made map shows why Queen's Domain is the true GOTY, and you can even upload your save to it to track the things you have left to find -- Cartographic miracles.

ARTICLE 1 (PC Gamer, en):
This fan-made map shows why Queen's Domain is the true GOTY, and you can even upload your save to it to track the things you have left to find
Cartographic miracles.
The world wants me to write more about Grand Theft Auto 6 and I bravely answer "No". Last week, I obtained 50% of the endings available in Queen's Domain, the best videogame to release in the year 2026. The next 50% require, well, a lot of nook-scouring and cranny-uncovering—many more hours of gameplay which I am preserving for myself as a Christmas holiday treat.
And when I do, I suspect I might make a little (just a little) use of this rad Queen's Domain map by a creator called Leny15. Barely a week ago I wrote that "I suspect a cross-section of [Queen's Domain's] goliath, entwined map would make for a killer bit of wall art". Well, I was right. Here it is and it looks rad as hell.
No, really. Even if you're not a Queen's Domain player—or maybe especially if you're not—take a look at this thing. It's one thing to wander these maps as a player, it's another to get a totally flexible birdseye view of how dense, twisty and interconnected they are. I will never stop being blown away by how knotty and elaborate this game is; I love it.
I will also never believe I have actually explored all of it. I'll be honest, gang, I really feel like I've plumbed the depths of this game, but this map has entries for areas I ain't even heard of, and even those areas that I have heard of seem to contain secret sub-areas I've barely touched.
My favourite feature of this thing has got to be the ability to upload your save to it, at which point it will give you a completion percentage and tick off the collectibles and secrets you've already attained (reminder that you can download your Steam cloud saves right in your browser). I was heartened to read that I have, it seems, done in about 70% of this game already. I was even more heartened to learn that the remaining 30% seems even stranger, more complicated, and more baffling than all the rest. I can't wait to check it out.
2026 games: All the upcoming games
Best PC games: Our all-time favorites
Free PC games: Freebie fest
Best FPS games: Finest gunplay
Best RPGs: Grand adventures
Best co-op games: Better together
Keep up to date with the most important stories and the best deals, as picked by the PC Gamer team.
One of Josh's first memories is of playing Quake 2 on the family computer when he was much too young to be doing that, and he's been irreparably game-brained ever since. His writing has been featured in Vice, Fanbyte, and the Financial Times. He'll play pretty much anything, and has written far too much on everything from visual novels to Assassin's Creed. His most profound loves are for CRPGs, immersive sims, and any game whose ambition outstrips its budget. He thinks you're all far too mean about Deus Ex: Invisible War.
You must confirm your public display name before commenting
Please logout and then login again, you will then be prompted to enter your display name.
