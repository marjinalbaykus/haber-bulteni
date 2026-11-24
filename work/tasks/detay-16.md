# Görev: detay-16

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-16.json`
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

HEADLINE: DeepSeek V4 model ailesinin tamamı yayına girdi; DeepSeek Harness geliştirici önizleme sürümü test için açıldı.

RELATED ITEMS:
[808] 2026-11-23 21:28 | 财联社 (zh) | V4全系列模型上线后，DeepSeek Harness开发者预览版开放测试
