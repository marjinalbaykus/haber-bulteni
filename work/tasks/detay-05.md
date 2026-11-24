# Görev: detay-05

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-05.json`
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

HEADLINE: NariQ, Qwen3.8-27B modelini BF16 kalitesinde yaklaşık 10 GB'lık dosyaya sığdırdı; 27B model 16 GB VRAM'e rahatça, 32 GB'a bolca sığıyor.

RELATED ITEMS:
[728] 2026-11-23 13:42 | huggingface.co (en) | Hacker News 163 puan, 34 yorum | NariQ: BF16-level Qwen3.8-27B in a 10 GB file

ARTICLE 1 (huggingface.co, en):
NariQ: Qwen3.8-27B in a 10 GB file, BF16-level on long-horizon agentic tasks
(Nov 20, 2026) We are releasing NariQ v1 GGUFs of Qwen3.8-27B at 3, 2.5 and 2 bits per weight. Run them on our llama.cpp fork (required).
- The 3-bit and 2.5-bit files match BF16 on long-horizon agent tasks, math and image understanding. The 2-bit file scores lower.
- NariQ is a trellis-coded post-training quantization (PTQ), calibrated on real coding-agent traces and stored in a layout that runs on a single GPU.
- More details are in the tech report.
Each cell gives a percentage and a count in parentheses. For DeepSWE, the count is the mean number of fully solved tasks per round. Across the same three rounds, the mean share of each task's new tests that passed was 83.1% (BF16), 82.9% (3-bit), 83.2% (2.5-bit) and 77.2% (2-bit). One BF16 verifier timeout in round 3 counts as zero (338 of 339 trials graded). Each NariQ variant had all 339 trials graded. Terminal-Bench counts solved trials, and the remaining columns count correct answers. The agent runs use the Claude Code CLI (2.1.309, thinking on, effort max) driving the local model, with a 262,144-token context and the model's thinking-mode sampling (temperature 1.0, top-p 0.95, top-k 20).
KL divergence from the BF16 model on held-out coding-agent traces, against file size. Every point was measured by us with the same code. Lower is better.
DeepSWE fully solved tasks out of 113, against file size. Our NariQ points and solid BF16 line average three rounds with Claude Code. Our GSQ-RCO points use one round. The hollow diamonds show approximate averages from Unsloth's published Dynamic V3.1 chart (mini-swe-agent). Compare each point with the BF16 line for its harness. Higher is better.
Terminal-Bench 2.1 score over 82 tasks × 3 attempts, against file size. Filled circles are our runs (Claude Code); hollow diamonds are Unsloth's reported results (mini-swe-agent), read from its published Dynamic V3.1 chart (hence ≈; its scores are averages over several passes). The two harnesses differ, so compare each point with the BF16 line of its own harness (solid: ours, dashed: Unsloth's). Higher is better.
Quickstart
One command on Linux with an NVIDIA GPU and driver 570 or newer (downloads the fork's binaries and the files, checks them, and fits the context to your GPU):
curl -fsSLO https://github.com/nari-labs/nari-llama.cpp/releases/download/b12431-nariv0/run.sh
bash run.sh 3.0   # or 2.5, 2.0
Or download and serve yourself, with llama-server from the fork:
hf download nari-labs/Qwen3.8-27B-NariQ-GGUF \
    Qwen3.8-27B-NariQ-3.0bpw.gguf Qwen3.8-27B-NariQ-mmproj-BF16.gguf --local-dir .
llama-server -m Qwen3.8-27B-NariQ-3.0bpw.gguf --mmproj Qwen3.8-27B-NariQ-mmproj-BF16.gguf \
    -ngl 99 -c 131072 --jinja --port 8080 \
    --temp 1.0 --top-p 0.95 --top-k 20 --min-p 0
The sampling flags are the thinking-mode settings our scores used. -c 131072 fits a 24 GB GPU with little to spare;
lower it if a display shares the GPU, or let run.sh pick the largest context that fits. Use -c 262144 (the model's
full context) on 32 GB or more. GPU memory with the vision projector:
Prebuilt binaries, the Docker image, supported GPUs and build steps: the fork's README.
Decode speed on one NVIDIA RTX PRO 6000 (Blackwell), single session, 512 greedy output tokens, tokens/s:
NariQ on our llama.cpp fork with -fa on -ctk bf16 -ctv bf16 -t 1 -tb 16 --backend-sampling --samplers temperature (greedy sampling on the GPU, BF16 KV cache). UD-IQ3_XXS is Unsloth's file on llama.cpp, measured earlier on the same GPU type.
Known limits and future work
- Requires a NariQ-aware runtime: for now, our llama.cpp fork. Stock llama.cpp, Ollama and LM Studio can't load the new tensor types yet; we hope to merge support into upstream llama.cpp. Our internal SGLang plugin and our inference engine built on Resin are a work in progress for potential public release.
- The llama.cpp fork needs more work across hardware. The current fork is focused on SM120 performance (e.g. RTX PRO 6000). Ampere, Ada and Hopper GPUs and Apple M-series GPUs are slower than comparable GGUFs for now; we would love open-source contributions for speedups, as this has been a solo effort. GPUs older than Ampere fall back to slow CPU execution, and AMD and Intel GPUs are not supported yet. GeForce cards have not been tested yet; tested GPUs are RTX PRO 6000, H100, L4 and A10.
- Apple M-series (Metal): build from source; on an M3 Pro (18 GB) decode is about 3.8 tok/s with the 3.0bpw file (4.9 with 2.0bpw), far slower than CUDA. Other Macs are untested.
- The 2-bit file is weaker overall; use the 2.5-bit or 3-bit for higher overall performance.
- The vision tower stays in BF16; only the language model is quantized. The MTP (multi-token prediction) module is not included yet; it is still to be quantized.
- KV-cache and activation quantization are next, so the files fit long contexts on 16 GB consumer GPUs and run faster. Today the KV cache and activations stay in BF16.
Contact us
For technical consulting, compute sponsorships, or partnership inquiries, please contact founders@narilabs.com.
License
These files inherit the base model's Apache-2.0 license; see NOTICE.
Acknowledgements
Thanks to the Qwen team for the amazing Qwen3.8-27B, and to the Unsloth, ExLlamaV3, GSQ-RCO (IST Austria DASLab) and GGML teams, whose open-source work this release builds on. NariQ's trellis codes build on QTIP (Tseng et al., 2024).
Citation
If you use NariQ, please cite the technical report:
@techreport{kim2026nariq,
  title       = {NariQ: Sub-3-Bit Quantization for Long-Horizon Agentic Tasks},
  author      = {Doyeob (Toby) Kim},
  institution = {Nari Labs},
  year        = {2026},
  month       = nov,
  eprint      = {2611.26157},
  archivePrefix = {arXiv},
  url         = {https://arxiv.org/abs/2611.26157}
}
Qwen3.8-27B
Following the widespread community adoption of the Qwen3.5 and Qwen3.6 series, we are pleased to introduce Qwen3.8, the most capable generation in the
