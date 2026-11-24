# Görev: detay-12

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/detay-12.json`
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

TOPIC: Genel Teknoloji

CRITERIA:
Bu geniş bir başlık; sadece gerçekten büyük olaylar girsin.
Önemli: dev şirketlerin büyük hamleleri, büyük satın almalar, önemli ürün çıkışları (telefon, araç, platform),
elektrikli araç devlerinin (BYD, Tesla vb.) çığır açan adımları, büyük güvenlik açıkları veya sızıntılar,
büyük düzenlemeler, uzay alanında önemli olaylar.
Önemsiz: sıradan ürün incelemeleri, indirimler, küçük uygulama güncellemeleri, sıradan araba haberleri.
Yapay zeka ve PC donanımı ayrı başlıklarda; burada onlara ancak çok büyükse yer ver.

HEADLINE: CrowdStrike, bulut güvenliği şirketi Zscaler'ı yaklaşık 44 milyar dolara satın alıyor.

RELATED ITEMS:
[916] 2026-11-23 16:20 | TechCrunch (en) | CrowdStrike to acquire Zscaler for $44 billion -- The cybersecurity giant said that the deal to buy Zscaler, which provides zero-trust products to large enterprises, will better equip their customers to use AI agents.
[1450] 2026-11-23 11:48 | cnBeta (zh) | CrowdStrike将以440亿美元收购云安全公司Zscaler -- 美国科技集团CrowdStrike已同意以约440亿美元的价格收购云安全公司Zscaler。这笔交易预计将缔造一家网络安全巨头。此次全股票收购正值全球企业和政府面临新一轮技术系统故障之际。近几周，包括微软和美国联邦航空管理局（FAA）在内的多个机构的在线系统因技术故障而瘫痪。 阅读全文

ARTICLE 1 (TechCrunch, en):
Cybersecurity giant CrowdStrike has agreed to buy Zscaler in a $43.9 billion all-stock deal, the companies announced Monday.
CrowdStrike and Zscaler said that the deal will create the “largest single addressable market” for cybersecurity, which will enable the companies to better position themselves for the AI era.
Zscaler is a San Jose, California-based tech company that provides cloud cybersecurity products to about 8,000 enterprise companies. The company offers zero-trust technology, a security feature that requires employees to authenticate each and every time they access an internal system or resource. The aim is to protect against hackers from stealing credentials and using them to gain unfettered access to a company’s network.
CrowdStrike, which secures the computers, laptops, phones, and other devices connected to a company’s network, said the deal will unite its endpoint protection software with Zscaler’s zero-trust platform to better defend their customers against data breaches. By combining the two products, CrowdStrike and Zscaler say that they can offer “superior protection” to their customers.
In remarks on Monday, the companies said that the acquisition will help customers safely adopt AI.
The deal, which is subject to regulatory approval, is expected to close around this time next year.

ARTICLE 2 (cnBeta, zh):
ç¾å½ç§æéå¢CrowdStrikeå·²åæä»¥çº¦440äº¿ç¾å çä»·æ ¼æ¶è´äºå®å ¨å ¬å¸Zscalerãè¿ç¬äº¤æé¢è®¡å°ç¼é ä¸å®¶ç½ç»å®å ¨å·¨å¤´ãæ¤æ¬¡å ¨è¡ç¥¨æ¶è´æ£å¼å ¨çä¼ä¸åæ¿åºé¢ä¸´æ°ä¸è½®ææ¯ç³»ç»æ éä¹é ãè¿å å¨ï¼å æ¬å¾®è½¯åç¾å½èé¦èªç©ºç®¡çå±ï¼FAAï¼å¨å çå¤ä¸ªæºæçå¨çº¿ç³»ç»å ææ¯æ éèç«çªã
ç½ç»å®å ¨ä¸å®¶è¡¨ç¤ºï¼è®¸å¤ç³»ç»æ éçåçï¼æ¯ç±äºä¼ä¸æ¥äºä¿®å¤äººå·¥æºè½åç°çè½¯ä»¶æ¼æ´ã
CrowdStrikeé¦å¸æ§è¡å®å ¼åå§äººä¹æ²»Â·åºå°è¨ï¼George Kurtzï¼è¡¨ç¤ºï¼âè¿å»ï¼ç½ç»å®å ¨çéç¹æ¯å°æ»å»è æä¹é¨å¤ãå¦ä»ï¼è¦è®©AIè¿å ¥ç³»ç»ï¼å°±å¿ é¡»ç¡®ä¿å®å ¨ãâ
CrowdStrikeè¡¨ç¤ºï¼æ¤æ¬¡æ¶è´é¢è®¡å°ä½¿å ¶å°2029å¹´çæ½å¨å¸åºè§æ¨¡æ©å¤§é¾1850äº¿ç¾å ã
è¯¥äº¤æä»éè·å¾çç®¡æºæåZscalerè¡ä¸æ¹åï¼é¢è®¡å°å¨CrowdStrikeæªè³1æç2028è´¢å¹´ä¸åå¹´å®æã
