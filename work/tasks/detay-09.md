# Görev: detay-09

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-09.json`
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

HEADLINE: Açık ağırlıklı Inkling-2-Mini modeli yayımlandı ve Unsloth GGUF sürümleri yerel kullanıma açıldı; model boyutu haberlerde belirtilmiyor.

RELATED ITEMS:
[1075] 2026-11-17 00:26 | Hugging Face (en) | HF trend puanı 1158.5, 5583 beğeni | Hugging Face trend modeli: thinkingmachines/Inkling-2-Mini -- tür: image-text-to-text
[1076] 2026-11-20 13:09 | Hugging Face (en) | HF trend puanı 1021, 1158 beğeni | Hugging Face trend modeli: unsloth/Inkling-2-Mini-GGUF -- tür: image-text-to-text
[1085] 2026-11-21 18:46 | Hugging Face (en) | HF trend puanı 84, 100 beğeni | Hugging Face trend modeli: strata-engine/Inkling-2-Mini-Strata -- tür: image-text-to-text
[746] 2026-11-22 21:01 | gpupartner.com (en) | Hacker News 755 puan, 306 yorum | A Beginner's Guide to Local AI with Inkling, Qwen, and Unsloth
[145] 2026-11-23 12:12 | Pasquale Pillitteri (en) | What is a looped transformer and why Inkling 2 Mini uses one

ARTICLE 1 (gpupartner.com, en):
A beginner's guide to Local AI with Inkling, Qwen, and Unsloth
We have AI at home! Set up your own free personal ChatGPT on your PC with Unsloth and Inkling or Qwen. It's private, and the whole house can use it.
By Jerry James ·
With the launch of open-weight models like Qwen, cloud-based LLM providers seem like they're more of infrastructure or appliance businesses when it comes to most routine tasks that an AI model can handle.
OpenAI and Anthropic have displayed a pattern of dropping model quality in the 'latest' version and controlling exactly what you're allowed to run on their models. Add to this ballooning costs and privacy concerns, and we get a pretty good case for moving away from this 'convenient' cloud experience.
That's basically where I was too. I wanted to offload certain token-heavy research, data collection, or coding tasks to an AI model without staring at usage limits all day. So, as any reasonable human being would do, I decided to set up my own local AI: a Claude and ChatGPT replacement that runs at home on my own hardware.
Since this was my first attempt, my primary focus was keeping it simple and hosting it on Windows. If you're a beginner, you can follow along to get one going on your home network too.
Once you have the hardware sorted, it takes about 10-15 minutes (excluding download times).
The short version
Install Unsloth Desktop, download a quantized AI model that fits your GPU's VRAM, set your context size, and turn on LAN access so everyone at home can use it.
What Do You Need to Run a Local AI at Home?
Not much (other than the hardware). Here's a short list:
- A Windows PC with a graphics card (GPU). The more video memory (VRAM) it has, the bigger the model it can run. I'm using an RTX 5090 with 32GB VRAM for this setup.
- Unsloth Desktop. A free app that 'serves' the AI model to you through a chat interface. Get it from Unsloth.
- An AI model sized to fit your GPU. We'll pick one in the next section.
That's it. No terminal commands, Python environments, or subscriptions required.
Which AI Model Can Your GPU Run?
It depends on how much VRAM your graphics card has. As a rough guide, 8-12GB of VRAM handles smaller 9B-12B models, while 32GB comfortably runs 27B-35B models with plenty of room for long conversations.
On consumer-grade hardware, we'll need smaller models. The full-size Qwen model sits at around 56GB and won't really fit on a consumer graphics card unless you're running a Mac with unified memory. That's where quantization comes in.
What Is Quantization?
Quantization shrinks a model by compressing weights and moving to a lower precision. You basically trade some accuracy for a model that actually fits on your GPU and runs faster.
The same photo stored at 8, 4 and 2 bits per pixel. A model's weights lose detail the same way going from Q8 to Q4 to Q2.
If you're wondering whether this tradeoff is worth it, well, it depends. You'll find that certain types of workloads don't really care and give similar outputs, while others may be nearly unusable.
Which Model Should You Run for Your VRAM?
For this guide, we'll be going with Qwen3.8-27B with 4-bit quantization on my system with an RTX 5090. Going up a tier is an option thanks to its 32GB of VRAM, but I wanted to allow for a larger context window (how much of the conversation the model can keep in mind at once) rather than cramming in the largest model that fits.
Qwen3.8-27B at each precision with a 128K context, from our VRAM calculator. At 4-bit, the model and the context fit a 32 GB card with room to spare.
If you want to run a model of this caliber with excellent performance, a hilariously overpriced RTX 5090 (remember when $1,999 felt too high?) is unfortunately still not a terrible 'deal' these days. The other option is way more expensive enterprise-grade GPUs.
If you're on a GPU with less VRAM, you can still get something very usable going right away. The table below gives you a rough idea of where you can start.
Small
  8-12 GB
  UD-Q4_K_S or Q4_K_M
  8K-16K
Medium
  16 GB
  Qwen3-14B, gpt-oss-20b (MoE), Gemma 4 26B (MoE), Qwen3-Coder-30B (MoE), Qwen3.6-35B (MoE), Inkling 2 Mini (MoE)
  Q4_K_M or Q5_K_M
  16K-32K
Large
  24 GB
  UD-Q4_K_M or Q4_K_S
  16K-32K
Extra Large
  32 GB+
  UD-Q4_K_XL or Q6_K
  64K-128K+
Models marked MoE (mixture of experts) only run a few billion of their parameters for each token. They keep a decent speed on a smaller card with the rest held in system RAM, which is why a 35B model sits in the 16 GB row. On an RTX 5060 Ti 16GB, our calculator puts Qwen3.6-35B at about 47 tokens per second this way.
Feel free to experiment with several models until you find one that fits your needs. You can also use GPUPartner's VRAM Calculator to get an estimate.
Can You Run Inkling 2 on an RTX 5090?
Inkling 2 Mini, yes. Inkling 2 Small, no.
Inkling 2 Mini is the newest model in this size class. Thinking Machines Lab released it on November 17, 2026: 26.4B parameters, Apache 2.0, vision support and a 1M-token context. Unsloth's recommended file, UD-Q4_K_XL, is 17.6 GB. That is within 0.1 GB of the Qwen3.8-27B file in this guide, so it sits on a 32GB card the same way and leaves about 14GB for context.
Nothing else in this guide changes. Update Unsloth Desktop first, because Inkling 2 needs a recent build, then search for Inkling 2 Mini in the same drop-down in Step 2.
2-bit
  10-11 GB
  12 GB card
3-bit
  13-15 GB
  16 GB card
4-bit (recommended)
  17-18 GB
  24 GB card
8-bit (lossless)
  24 GB
  32 GB card
Those figures are Unsloth's. They count RAM and VRAM together and cover the model alone, so a long context needs more. Inkling 2 Mini is a mixture-of-experts model with 5.4B parameters active, so the 4-bit file also runs on a 16 GB card with the remainder in system RAM, a little slower.
Inkling 2 Small is small in name only. It is a 594B model, and the smallest file Unsloth publishes is 171.7 GB. That is unified-memory territory: a 256GB Mac Studio or a DGX Station, not a graphics ca
