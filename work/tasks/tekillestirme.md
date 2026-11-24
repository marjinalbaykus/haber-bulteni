# Görev: tekillestirme

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/tekillestirme.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You get the selected stories of a daily bulletin, grouped under topics.
Find stories that describe the SAME real-world event under different topics (e.g. the same acquisition
listed under both "AI" and "PC hardware"). For each such group keep exactly one: the one whose topic fits
the event best (if equal, the more informative headline). Different events about the same company are NOT
duplicates.
Output JSON: {"groups": [{"keep": int, "drop": [int]}]} (empty list if there are no duplicates).

## GİRDİ

[0] Real Madrid ve Arda Güler: Haberlere göre Mourinho, PSV maçında Arda Güler'i dinlendirecek ve Brahim Díaz'ın ilk 11'de başlaması bekleniyor.
[1] Real Madrid ve Arda Güler: Real Madrid, La Liga'da Celta Vigo'yu 2-1 yenerek üst üste altıncı galibiyetini aldı.
[2] Real Madrid ve Arda Güler: Jude Bellingham'ın Real Madrid ile uzun vadeli sözleşme yenileme görüşmelerinin ileri aşamada olduğu bildiriliyor.
[3] PC Donanım, GPU ve Çipler: Nvidia yönetim kurulu Jensen Huang'ı CEO olarak yeniden atadı; hisseler sert yükseldi ve Trump'ın desteği dikkat çekti.
[4] PC Donanım, GPU ve Çipler: Kongre, Huang'ın geri dönüşüne tepki gösteriyor; Senato Bankacılık Komitesi 3 Aralık'ta ihracat kontrolleri konulu dinleme yapacak.
[5] PC Donanım, GPU ve Çipler: ABD, Nvidia çip skandalıyla bağlantılı Megaspeed ve bir Alibaba yan kuruluşunu kara listeye aldı.
[6] PC Donanım, GPU ve Çipler: Beyaz Saray, Nvidia için yeni çip kısıtlamasına gerek olmadığını söylerken Trump Çin'e görece eski çip satmak istediğini belirtti.
[7] PC Donanım, GPU ve Çipler: TSMC, Teksas'ın Brazos County bölgesinde 22,6 milyar dolarlık yeni çip fabrikası için başvuru yaptı.
[8] Yerel LLM (kendi bilgisayarında çalışan modeller): NariQ, Qwen3.8-27B modelini 10 GB'lık dosyaya sıkıştırarak BF16 düzeyinde kalite sunduğunu iddia ediyor; 16 GB VRAM'e sığar.
[9] Yerel LLM (kendi bilgisayarında çalışan modeller): Açık ağırlıklı Inkling-2-Mini modeli yayımlandı ve Unsloth GGUF sürümleri yerel kullanıma açıldı; model boyutu haberlerde belirtilmiyor.
[10] Yapay Zeka: Anthropic halka arz dosyasında yapay zekanın insanlık için 'varoluşsal riskler' doğurabileceği uyarısında bulundu.
[11] Yapay Zeka: OpenAI, gelişmiş yapay zeka sistemleri için rakiplerin ortak sınırlar üzerinde anlaşmasını ve ölçülebilir bir Perplexity skoru kullanılmasını önerdi.
[12] Yapay Zeka: Z.ai, döngüsel 'loop transformer' mimarisine sahip GLM-5.4 modelini tanıttı; şirket modelin 4 kat büyük modeller kadar düşünebildiğini iddia ediyor.
[13] Yapay Zeka: Google Gemini 4 Neon ve OpenAI GPT-6.2 Astra-Sol modelleri, daha büyük kurumsal iş yükleri ve verimlilik hedefiyle duyuruldu.
[14] Yapay Zeka Kodlama Araçları ve Fiyatlar: Anthropic, Claude Ship'i duyurdu; AI ajanları kod değişikliklerini inceleyip düzeltebilecek ve pull request'leri onaylayabilecek.
[15] Yapay Zeka Kodlama Araçları ve Fiyatlar: DeepSeek ücretsiz kullanım kotasını daralttı; yorumcular ücretsiz yapay zeka döneminin sona erip ermediğini tartışıyor.
[16] Yapay Zeka Kodlama Araçları ve Fiyatlar: DeepSeek V4 model ailesinin tamamı yayına girdi; DeepSeek Harness geliştirici önizleme sürümü test için açıldı.
[17] Yapay Zeka Kodlama Araçları ve Fiyatlar: Claude Code'a gerçek zamanlı maliyet kontrolleri geldi; Uber da Claude Code maliyet ayarlarının sonuçlarını paylaştı.
[18] Genel Teknoloji: CrowdStrike, bulut güvenliği şirketi Zscaler'ı yaklaşık 44 milyar dolara satın almak için anlaştı.
[19] Oyunlar (Soulslike ve Aksiyon-RPG): Elden Ring: Rot & Sorcery'nin 2027'de çıkacağı resmen doğrulandı.
[20] Oyunlar (Soulslike ve Aksiyon-RPG): Queen's Domain 300 binden fazla adet sattı; Eurogamer incelemesi oyunu etkileyici bir aksiyon-RPG olarak övdü.
[21] CS2: PGL Major Singapore 2026 Stage 1 başladı; turnuvanın ilk İsviçre sistemi maçları oynanmaya başlandı.
[22] Ekonomi - Dünya: Jensen Huang'ın Nvidia'ye dönmesiyle S&P 500 yaklaşık %4,7, Nasdaq %6,5 yükseldi; geçen haftaki sert düşüşün yarısı silindi.
