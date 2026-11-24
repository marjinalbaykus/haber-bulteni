# Görev: detay-06

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-06.json`
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

HEADLINE: Beyaz Saray, Nvidia için yeni çip kısıtlamasına gerek olmadığını söylerken Trump Çin'e görece eski çip satmak istediğini belirtti.

RELATED ITEMS:
[367] 2026-11-23 17:08 | Cheddar (en) | White House Says No More Nvidia Chip Restrictions Needed
[291] 2026-11-23 17:03 | 东方财富网 (zh) | 特朗普称只想向中国出售相对落后的芯片 外交部回应
[295] 2026-11-23 13:40 | 美国之音 (zh) | 贝森特：政府仍在等待英伟达就其芯片流入中国问题作出解释
