# Görev: detay-21

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-21.json`
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

TOPIC: CS2

CRITERIA:
Önemli: Major ve büyük turnuvaların (IEM, BLAST, ESL Pro League, PGL) sonuçları ve finalleri,
büyük kadro değişiklikleri (yıldız oyuncu transferleri), Valve'ın büyük oyun güncellemeleri
ve kural değişiklikleri, Türk oyuncu ve takımlarının büyük başarıları.
Önemsiz: küçük turnuva elemeleri, sıradan kasa/skin haberleri, kısa süreli deneme oyuncuları.

HEADLINE: PGL Major Singapore 2026 Stage 1 başladı; turnuvanın ilk İsviçre sistemi maçları oynanmaya başlandı.

RELATED ITEMS:
[948] 2026-11-23 20:11 | Players (en) | PGL Major Singapore 2026 Starts Tomorrow — Schedule, Ukrainians and Tournament Format
[949] 2026-11-23 17:20 | Esports.gg (en) | Stage 1 kicks off CS2 Singapore Major 2026: Results from the early Swiss clashes
[953] 2026-11-23 11:38 | EGamersWorld (en) | PGL Major Singapore 2026 Stage 1 Preview: Teams, Schedule and Format
[954] 2026-11-23 21:49 | Pley.gg (en) | PGL Major Singapore 2026
[955] 2026-11-23 12:20 | TalkEsport (en) | CS2 Singapore Major 2026 Starts Today: Stage 1 Teams, Schedule, and Where to Watch
[946] 2026-11-23 14:40 | Esports.gg (en) | Pick’Ems for CS2 Singapore Major 2026: Prediction ideas and bracket analysis
[947] 2026-11-23 10:42 | GosuGamers (en) | CS2 PGL Major Singapore 2026 - All participating teams, format, prize pool, and more
[982] 2026-11-23 10:10 | HLTV.org (en) | PGL Major Singapore teams, format, schedule, talent, fantasy
[952] 2026-11-23 21:20 | Legal Sports Betting (en) | CS2 Singapore Major Stage 1 Betting Analysis For Opening BO1s
[961] 2026-11-23 16:00 | Pley.gg (en) | Who can win the Singapore Major? Five teams, one favourite
[962] 2026-11-23 16:57 | Pley.gg (en) | Singapore Major Pick'em: what pros and creators picked
[972] 2026-11-23 20:03 | Freetips.com (en) | PGL Major Singapore 2026 Tournament Betting Preview & Guide – MOUZ Can Deliver in Singapore
