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

[0] Real Madrid ve Arda Güler: Mourinho, PSV maçı öncesi Arda Güler'i dinlendirme kararı aldığı yönünde haberler çıktı; OkDiario'ya göre Madrid, Vinicius ile başlayan ilk 11'le sahaya çıkıyor.
[1] Real Madrid ve Arda Güler: Bellingham'ın Real Madrid ile uzun vadeli sözleşme yenileme görüşmeleri ileri aşamada; NYT haberi duyurunun yakında gelebileceğini bildirdi.
[2] Real Madrid ve Arda Güler: Endrick yeniden sakatlandı ve yaklaşık üç hafta yok; PSV maçında forma giyemeyecek.
[3] PC Donanım, GPU ve Çipler: Dell, HPE, Lenovo ve Supermicro'nun AI sunucu fiyatlarını sözleşmeden aylar sonra yüzde 25-45 artırdığı bildirildi; RAM kıtlığı bellek ağırlıklı CPU düğümü maliyetini üç katına çıkardı.
[4] Yerel LLM (kendi bilgisayarında çalışan modeller): Z.ai, ilk loop transformer modeli GLM-5.4'ü açık ağırlıklı olarak yayımladı; 750 milyar parametreli model 4-bit ile bile 16 GB veya 32 GB VRAM'e sığmaz, açık modellerin en güçlüsü olup Opus 5.5'e yüzde 5'ten az fark atıyor.
[5] Yerel LLM (kendi bilgisayarında çalışan modeller): NariQ, Qwen3.8-27B modelini BF16 kalitesinde yaklaşık 10 GB'lık dosyaya sığdırdı; 27B model 16 GB VRAM'e rahatça, 32 GB'a bolca sığıyor.
[6] Yapay Zeka: Google Gemini 4 Neon ve OpenAI GPT-6.2 Astra/Sol modelleri iş verimliliğine odaklanarak çıktı; Google ayrıca yarı fiyatlı Gemini 3.7 Flash'ı duyurdu.
[7] Yapay Zeka: OpenAI baş bilim insanı Jakub Pachocki, gelişmiş sistemler güvenlik kontrollerini aşmadan önce rakiplerin ortak ve ölçülebilir geliştirme sınırlarında anlaşmasını istedi.
[8] Yapay Zeka: Anthropic, halka arz dosyasında yapay zekanın insanlık için varoluşsal riskler doğurabileceğini belirtti; yatırımcılar halka arz öncesi milyarlarca dolarlık bahsi savundu.
[9] Yapay Zeka: Amazon, Trainium çiplerini satışa hazırlarken Decart'ın yapay zeka optimizasyon teknolojisini ve araştırma ekibini yaklaşık 7,5 milyar dolara satın alıyor.
[10] Yapay Zeka Kodlama Araçları ve Fiyatlar: Anthropic, yapay zeka ajanlarının çekme isteklerini inceleyip düzeltmesine ve takımın belirlediği kurallara göre onaylamasına izin veren Claude Ship'i duyurdu.
[11] Genel Teknoloji: Nvidia yönetim kurulu, istifasından 11 gün sonra Jensen Huang'ı CEO olarak geri getirdi; hisse bir ara %35 yükseldi, Huang 3 Aralık'ta Senato'da dinlenecek.
[12] Genel Teknoloji: CrowdStrike, bulut güvenliği şirketi Zscaler'ı yaklaşık 44 milyar dolara satın alıyor.
[13] Oyunlar (Soulslike ve Aksiyon-RPG): Elden Ring: Rot & Sorcery'nin 2027'de çıkacağı resmen doğrulandı.
[14] Oyunlar (Soulslike ve Aksiyon-RPG): Aksiyon-RPG Queen's Domain Eurogamer'da 'hayranlık uyandıran' incelemesi aldı ve 300 bin satışı geçti.
[15] Oyunlar (Soulslike ve Aksiyon-RPG): Grand Theft Auto VI, PS5'te 39 milyondan fazla oyuncuya ulaştı.
[16] CS2: PGL Major Singapur 2026 başladı; Stage 1 İsviçre sistemi ilk maçları oynandı, MOUZ favoriler arasında gösteriliyor.
[17] Ekonomi - Dünya: Jensen Huang'ın Nvidia CEO'su olarak dönmesiyle Wall Street sert yükseldi: Nasdaq yüzde 6,5, S&P 500 yüzde 4,7 arttı, Nvidia en iyi gününü yaşadı.
