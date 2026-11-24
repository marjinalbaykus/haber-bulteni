# Görev: detay-07

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-07.json`
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

TOPIC: PC Donanım, GPU ve Çipler

CRITERIA:
Kullanıcının bilgisayarı: AMD Ryzen 7 9800X3D işlemci ve AMD Radeon RX 7800 XT (16 GB) ekran kartı.
Önemli: yeni GPU/CPU duyuruları ve çıkışları (AMD, NVIDIA, Intel), büyük fiyat değişimleri,
RAM/VRAM fiyatlarında sert hareketler, AMD sürücülerinde önemli güncellemeler veya sorunlar
(özellikle RDNA3 / RX 7000 serisini etkileyen), FSR gibi teknolojilerde büyük yenilikler,
TSMC, ihracat kısıtları, çip sektöründe büyük olaylar, Çinli GPU/çip üreticilerinde büyük adımlar.
Önemsiz: indirim/fırsat haberleri, sıradan aksesuar incelemeleri, küçük söylentiler,
tek bir kasa/klavye/fare ürünü tanıtımı.

HEADLINE: TSMC, Teksas'ın Brazos County bölgesinde 22,6 milyar dolarlık yeni çip fabrikası için başvuru yaptı.

RELATED ITEMS:
[337] 2026-11-23 22:51 | kcentv.com (en) | TSMC files application for proposed $22.6 billion chip plant in Brazos County
[330] 2026-11-23 22:40 | KBTX News 3 (en) | World’s leading semiconductor manufacturer eyes Brazos County for 3,200+ acre facility
