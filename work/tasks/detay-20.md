# Görev: detay-20

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-20.json`
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

HEADLINE: Queen's Domain 300 binden fazla adet sattı; Eurogamer incelemesi oyunu etkileyici bir aksiyon-RPG olarak övdü.

RELATED ITEMS:
[1228] 2026-11-23 10:04 | Eurogamer.net (en) | Queen's Domain review - a stunning action-RPG of genuine wonder
[1233] 2026-11-23 19:51 | VGChartz (en) | Queen's Domain Sales Top 300,000 Units

ARTICLE 1 (Eurogamer, en):
Queen's Domain review - a stunning action-RPG of genuine wonder
What lies beneath?
Where to start with Queen's Domain? It's tempting to begin with King's Field; after all, FromSoftware's seminal first-person action-RPG is an explicit inspiration for developer Freshly Baked Games. But 40 hours later, it feels like so much more. It's equally tempting to invoke another FromSoftware game: Dark Souls. Queen's Domain owes an obvious debt to that game's celebrated level design: the dazzling interconnectedness of its vertiginous world. But that's not quite right either. The deeper you delve, the more there's something of the spirit of The Legend of Zelda, a touch of Metroidvania too. Until finally you concede that Queen's Domain, with its remarkable world of densely packed secrets, is very much its own.
All this begins with an island. You, Prince Beruim, are a castaway, washed ashore on Neasied. Your father, the king, has disappeared, and your search has brought you, somewhat circuitously, here. As you clamber to your feet and survey the island, it makes an immediately striking impression: all strange peaks and crooked spires; a cramped, chaotic mass of landmarks lost amid the dense undergrowth. Pyramids perch atop steep plateaus; a vast, moss-tinged mausoleum sprawls across the land to one side while the other is dominated by an imposing tower. It's a world made all the stranger by Queen's Domain's retro aesthetic. Here, shimmering low-res textures and low-poly shapes converge to create elaborate spaces and structures that feel slightly at odds with the lo-fi vibe. It's not quite old, not quite new; instead, something of a world lost outside of time. It makes for an enigmatic, disorientating landscape. And when paired with a soundtrack that delights in discordance, as it swings from gentle ambience to pulsing synths, it's a mood that refuses to resolve.
There is so much to say about Neasied. And so much I absolutely will not spoil. But very early impressions don't do it justice at all. As you take your first tentative steps onto the island, presented with a forking path that eventually winds back in on itself, it's easy to mistake Queen's Domain for a first-person Dark Souls - especially if, like me, you're only passingly familiar with King's Field. As in most Soulslikes, early progress is defined by a punishing inch-by-inch crawl from one brutal encounter to the next; where death - via chicken-cricket hybrids, ladybird-rats, and other wonderfully odd things - comes fast and frequently, respawning you back at the last Crystal Monument you visited. And because its wilfully old-school design eschews modern-day niceties such as a combat lock-on or dedicated roll button, there's a looseness to fighting that takes a while to click. But it does, slowly, click.
This is a game that demands you lean into its limitations, encourages you to find ways to make them work. At first, those Souls-honed instincts are likely to be your undoing as you naturally scramble for a dodge button that isn't there. But soon a different sort of pattern emerges: you realise that to avoid a ladybird attack, you simply need to step back - but not so far you don't have time to nip in and strike while your opponent takes a pause. A charging enemy might require a different approach - a well-timed backstep and swivelling sideways strafe to put you in the right place for a counter. And in this way, combat becomes a compelling sort of puzzle as you learn each of the 100+ enemies' little ticks - including rock, paper, scissors-style weaknesses to different weapon types - and figure out how to respond.
There's more to consider. Your melee damage output, for instance, is directly tied to your stamina bar: the more stamina you have, the harder you'll hit; too low and it's a waste of a swing. That forces you to choose your moments carefully, moving out to safety while your bar refills. And there's a charge attack, also linked to your stamina, that does more damage still. Already combat has a distinctively deliberate rhythm, but then come your first throwing knives. These are most immediately useful against enemies ordinarily out of reach - like the laser-spewing foliage dogging your journey early on - but they're equally valuable for luring individual enemies away from the throng. Throwing weapons take a while to return to your pouch, meaning they too need to be deployed with purpose. And while knives are just the start - you'll acquire an increasingly exotic selection of throwables including poison-tipped darts and homing blades - they all fundamentally work the same way.
And the interplay of these various warm-ups and cooldowns creates an increasingly engaging rhythm as you learn to juggle between your variously equipped weapons to maximise their damage potential. You might, for instance, fire off a one-two-three volley of homing darts, then use a magic lantern to toss out a massive fireball that bounces destructively around the room. And it's there, in the ensuing chaos, you'll charge up your blade, letting rip then returning to your darts again. Combat might encourage a methodical pace, but that doesn't mean you can't fight fast, and there are some fascinating synergies among the various weapons, many having their own wonderfully distinct mechanisms. There's a brawling gauntlet that lifts you clean off the ground and a vampiric blade capable of stealing an enemy's life force. There are hammers, creatures, and an unexpected mid-game weapon class, utilising a third rechargeable power source, that I am definitely not spoiling. Queen's Domain's 100 or so weapons create ample space for experimentation and customisation, and the further from its cautious beginnings you stray, the brisker, the more exhilarating its combat becomes. In my current, late-game form, I'm a whirl of blades and blasts and up-close attacks that feels anything but Soulslike.
Queen's Domain isn't just a nippier sort of game, though. It quickly reveals itself to be fri
