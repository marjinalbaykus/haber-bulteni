# Görev: detay-04

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-04.json`
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

HEADLINE: Z.ai, ilk loop transformer modeli GLM-5.4'ü açık ağırlıklı olarak yayımladı; 750 milyar parametreli model 4-bit ile bile 16 GB veya 32 GB VRAM'e sığmaz, açık modellerin en güçlüsü olup Opus 5.5'e yüzde 5'ten az fark atıyor.

RELATED ITEMS:
[146] 2026-11-23 12:06 | Pasquale Pillitteri (en) | Z.ai launches GLM-5.4, its first loop transformer with 750 billion parameters
[163] 2026-11-23 11:10 | www.anfangnews.com (zh) | 智谱GLM-5.4大模型发布：仍是开源最强、比Opus 5.5差距5%内_人工智能
[139] 2026-11-23 14:28 | Fello AI (en) | GLM 5.4: Z.ai’s Loop Transformer, Access and Real Cost
[732] 2026-11-23 10:06 | z.ai (en) | Hacker News 1114 puan, 362 yorum | GLM-5.4: Toward generalized coding with our first loop transformer
[144] 2026-11-23 11:15 | explainx.ai (en) | GLM-5.4 Coding Plan: +50% Quota, Vision (Nov 2026)
