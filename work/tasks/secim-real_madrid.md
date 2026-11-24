# Görev: secim-real_madrid

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `work/out/secim-real_madrid.json`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

You are the editor of a personal daily news bulletin for a Turkish reader.
You receive candidate news items for ONE topic from roughly the last 30 hours, in several languages
(Turkish, English, Spanish, Chinese). Pick only the genuinely important stories according to the topic
criteria, merge items that cover the same event, and write a one-line Turkish bullet for each.

Rules:
- Use ONLY the given items. Never add facts that are not in the titles or snippets.
- Merge items about the same event into one story and list all of their ids.
- Score importance 1-10 for THIS reader using the criteria: 10 = historic, 8 = big news, 7 = clearly worth
  knowing today, 5 = minor, 3 = noise. Be strict; most items are noise.
- Importance signals: the same event covered by many independent sources, Hacker News points,
  Hugging Face trend scores. A single clickbait or rumor source is weak evidence.
- Skip stories that were already reported on previous days (list given) unless there is a significant
  new development; in that case the headline must state what is new.
- If market data is given and a threshold is exceeded, treat that move as a story candidate even if no
  news item explains it (use ids: []).
- headline: Turkish, one sentence, at most about 25 words, concrete (who did what, numbers if present).
  No clickbait, no emojis.
- Return at most 8 stories, most important first. An empty list is fine and expected on quiet days.

Output JSON: {"stories": [{"ids": [int], "importance": int, "headline": str, "reason": str}]}
"reason" is a short Turkish note on why it matters.

## GİRDİ

TOPIC: Real Madrid ve Arda Güler

CRITERIA:
Kullanıcı Real Madrid taraftarı ve özellikle Arda Güler'i takip ediyor.
Önemli: Arda Güler'in gol/asist/maç performansı, ilk 11'de oynayıp oynamadığı, sakatlık,
sözleşme veya transfer gelişmeleri, teknik direktörün onunla ilgili net kararları ve açıklamaları.
Real Madrid için: maç sonuçları (özellikle El Clasico, Şampiyonlar Ligi), büyük transferler,
teknik direktör değişikliği, ciddi sakatlıklar, kupa/şampiyonluk gelişmeleri.
Önemsiz: dedikodu niteliğinde "iddia" haberleri, aynı maçın onlarca ön izleme haberi,
tek bir taraftar yorumundan haber çıkarma, Arda Güler'in adı sadece geçtiği alakasız haberler.

CANDIDATE ITEMS:
[800] 2026-11-23 22:14 | Bolu Gündem (tr) | Arda Güler'in 14 yaşındayken Bolu'da bayramlaştığı görüntüler ortaya çıktı
[67] 2026-11-23 23:48 | Sporting News (es) | La alineación del Real Madrid: El XI para el partido vs. PSV por la Champions League
[95] 2026-11-23 23:22 | roundtable.io (en) | Real Madrid Star Jude Bellingham Set to Commit Future to the Club
[489] 2026-11-23 23:29 | Flashscore.es (es) | Real Madrid - PSV
[245] 2026-11-23 23:43 | FanDuel Sportsbook (en) | Manchester City (RESISTANCE) v Real Madrid (CATALYST) Odds
[118] 2026-11-23 16:43 | Sözcü Gazetesi (tr) | Arda Güler o listede zirveye çıktı! Galatasaraylı Batrakov takipte -- CIES'in 32 Avrupa ligindeki 22 yaş altı futbolcuları 90 dakika başına asist beklentisine göre sıraladığı araştırmada Arda Güler birinci oldu. Real Madridli oyuncuyu Lamine Yamal ve Lutfi Dalipi izlerk ...
[766] 2026-11-23 21:30 | NTVSpor (tr) | Arda Güler’in zirveye çıktığı liste açıklandı: Galatasaraylı Batrakov tırmanışta
[69] 2026-11-23 21:38 | Crónica Global (es) | Hansi Flick recibe una gran alegría tras ganar al Villarreal: fichaje de lujo tras el partido de Champions en Bakú
[89] 2026-11-23 21:07 | Haberler.com (en) | Critical warning to Arda Güler from the older brother of Emre Mor, who has taken a break from football: My brother did it, don't you do it.
[445] 2026-11-23 23:18 | Real Madrid CF | Web Oficial (es) | Horarios de la quinta jornada de la Champions
[248] 2026-11-23 23:43 | FanDuel Sportsbook (en) | Real Madrid (CATALYST) v FC Bayern Munchen (EDEN) Odds
[114] 2026-11-23 13:26 | STAR (tr) | Mourinho'dan Arda Güler kararı! Real Madrid'de tüm hesaplar değişti -- Avrupa futbolunun kulüpler düzeyindeki 1 numaralı organizasyonu olan Şampiyonlar Ligi'nde sezonun 5. haftası yarın başlayacak. Devler Ligi'nde 4'te 4 yapan Real Madrid, PSV'yi konuk edeceği maçta seri ...
[807] 2026-11-23 21:04 | ABC Haber (tr) | Emre Mor'un ağabeyinden Arda Güler'e uyarı: “Kardeşim yaptı, sen yapma”
[68] 2026-11-23 21:16 | Cubadebate (es) | Se fortalecen las columnas del Bernabéu
[107] 2026-11-23 21:00 | inkl (en) | Real Madrid Predicted Lineup vs. PSV: Mourinho Makes Changes in Midfield
[449] 2026-11-23 21:47 | Diario AS (es) | Alineación posible del Real Madrid contra el PSV Eindhoven en Champions League
[249] 2026-11-23 23:19 | FanDuel Sportsbook (en) | Paris SG (GRIMACE) v Real Madrid (CATALYST) Odds
[112] 2026-11-23 12:53 | Milliyet (tr) | Real Madrid-PSV Eindhoven Şampiyonlar Ligi maçı bu akşam saat kaçta hangi kanalda? Arda Güler ilk 11'de başlayacak mı? -- Şampiyonlar Ligi 5. hafta maçında milli oyuncumuz Arda Güler'in formasını giydiği Real Madrid sahasında PSV Eindhoven'ı konuk edecek. Geride kalan 4 maçta 4 galibiyet elde eden Eflatun Beyazlılar, PSV ...
[799] 2026-11-23 20:52 | Haberler (tr) | Futbola ara veren Emre Mor'un ağabeyinden Arda Güler'e hayati uyarı: Kardeşim yaptı, sen yapma
[62] 2026-11-23 21:00 | OkDiario (es) | Alineación del Real Madrid contra el PSV: Vinicius y 10 más
[96] 2026-11-23 20:42 | Fanatik (en) | Thibaut Courtois heaps praise on Arda Güler! 'We're playing better'
[482] 2026-11-23 21:45 | El Mundo (es) | El trauma del Real Madrid en Eindhoven: un haptoterapeuta para Van Breukelen y unos bóxer de la mala suerte para Mendoza
[255] 2026-11-23 23:14 | CBS Sports (en) | Real Madrid vs. PSV PREVIEW: Can Mourinho's Madrid Stay Perfect in Europe? - Scoreline Stream of Soccer
[115] 2026-11-23 12:19 | Haberler.com (tr) | Emre Mor'un ağabeyinden Fenerbahçe'ye ağır suçlama! Arda Güler'i de uyardı -- Hollanda Eredivisie ekiplerinden NEC Nijmegen forması giyen Emre Mor'un yaşadığı sağlık sorunları nedeniyle futbola ara verdiğini açıklamasının ardından ağabeyi Hüseyin Mor'dan çarpıcı açıklamalar gel ...
[768] 2026-11-23 18:53 | Milliyet (tr) | Courtois'ten Arda Güler sözleri! Bellingham kıyaslamalarına cevap - Futbol - Spor Haberleri
[64] 2026-11-23 19:49 | Diario de México (es) | Endrick será baja en el Real Madrid para el partido de Champions League
[84] 2026-11-23 20:22 | Yahoo Sports (en) | Jose Mourinho talks Guler, Bellingham, Vinicius, Comesana ahead of Real Madrid vs PSV
[467] 2026-11-23 21:20 | OkDiario (es) | Tebas vuelve a la carga: «La presión a los árbitros del Real Madrid es muy diferente a la del Barcelona»
[217] 2026-11-23 22:29 | Sports Illustrated (en) | ‘Amnesia’—Jose Mourinho Takes Swipe at Real Madrid Foe Over Favoritism Accusation
[113] 2026-11-23 11:44 | ODATV (tr) | Emre Mor'un ağabeyinden Arda Güler'e uyarı: 'Kariyerini Fenerbahçe bitirdi' -- Futbola ara veren Emre Mor'un ağabeyi Hüseyin Mor'a atfedilen sözler gündem oldu. İddialara göre Hüseyin Mor, kardeşinin kariyerinin Fenerbahçe döneminde bittiğini savunarak genç yıldız Arda Güler'e " ...
[790] 2026-11-23 18:36 | KARAR (tr) | Arda Güler için sürpriz karar
[70] 2026-11-23 18:38 | Caracol Radio (es) | Real Madrid 2-1 Celta: resultado, resumen y goles
[94] 2026-11-23 20:17 | Get Spanish Football News (en) | Real Madrid star will earn ‘significantly’ more than €15m-per-year as part of contract renewal
[470] 2026-11-23 20:02 | UEFA.com (es) | Los datos y estadísticas del Real Madrid - PSV de la jornada 5 de la UEFA Champions League
[252] 2026-11-23 22:18 | FanDuel Sportsbook (en) | Real Madrid (CATALYST) v Manchester City (RESISTANCE) Odds
[119] 2026-11-23 08:47 | Milli Gazete (tr) | Arda Güler'e kötü haber! Mourinho'dan yeni karar -- LaLiga'da Celta Vigo karşısında aldığı galibiyetle moral bulan Real Madrid'de gözler Şampiyonlar Ligi'ndeki PSV karşılaşmasına çevrildi. Avrupa ...
[762] 2026-11-23 18:05 | 61SAAT (tr) | Hami Mandıralı anlattı! “Arda Güler bana imzalı forma gönderdi”
[63] 2026-11-23 18:20 | La Provincia (es) | El Real Madrid busca mantener el pleno y el Betis seguir en la pomada del 'Top 8' de la Champions.
[87] 2026-11-23 18:01 | Haberler.com (en) | Surprise gift from Arda Güler to his teammate
[491] 2026-11-23 19:36 | DAZN (es) | Fabrizio Romano detalla en DAZN el futuro de Endrick en el Real Madrid: "Mourinho no quiere cambiar ni un jugador..."
[256] 2026-11-23 21:21 | FanDuel Sportsbook (en) | Real Madrid (CATALYST) v Paris SG (GRIMACE) Odds
[116] 2026-11-23 07:36 | Haberler.com on MSN (tr) | Mourinho yine ortalığı karıştırdı! Arda'ya büyük şok -- Real Madrid'de forma giyen milli futbolcu Arda Güler için Şampiyonlar Ligi'ndeki PSV karşılaşması öncesinde dikkat çeken bir gelişme yaşandı. Celta Vigo maçının ardından Arda Güler ve Jude Bellingham' ...
[765] 2026-11-23 17:55 | Gazete3 (tr) | Arda Güler’den İbrahim Akonate’ye Afyon sucuğu ikramı!
[82] 2026-11-23 16:34 | ABC Noticias (es) | Real Madrid vs. PSV: horario en México y posibles alineaciones del partido de Champions League
[111] 2026-11-23 17:30 | Six Sports (en) | Thibaut Courtois agrees new Real Madrid deal until 2028
[442] 2026-11-23 19:15 | ESPN Deportes (es) | Real Madrid: posible alineación y probabilidades vs PSV
[258] 2026-11-23 21:21 | FanDuel Sportsbook (en) | FC Bayern Munchen (CASTLE) v Real Madrid (ORACLE) Odds
[120] 2026-11-23 05:56 | Türkiye Gazetesi (tr) | Arda Güler'in 14 yaşındaki bayram namazı görüntüleri ortaya çıktı -- Real Madrid forması giyen Arda Güler’in, henüz 14 yaşındayken memleketi Bolu Mengen'e bağlı Hayranlar Köyü'nde bayram namazının ardından ailesi ve köy sakinleriyle bayramlaştığı görüntüler gündem oldu ...
[804] 2026-11-23 17:39 | Saray Medya (tr) | Arda Güler'in 14 yaşındaki bayram namazı görüntüleri
[60] 2026-11-23 16:24 | UEFA.com (es) | Fantasy Football de la Champions League: la plantilla del ojeador de la jornada 5
[109] 2026-11-23 15:45 | Tribuna.com (en) | Thibaut Courtois on goalkeeper Ballon d'Or hopes: 'Everything starts with a top goalkeeper'
[493] 2026-11-23 19:06 | FOX Deportes (es) | Thibaut Courtois sueña con terminar su carrera en Real Madrid
[221] 2026-11-23 20:55 | Yahoo Sports (en) | UEFA Champions League Matchday 5 Predictions Including PSV vs Real Madrid
[121] 2026-11-23 05:00 | En Son Haber (tr) | Arda Güler'in 14 yaşındaki görüntüleri ortaya çıktı -- Real Madrid forması giyen milli futbolcu Arda Güler'in henüz 14 yaşındayken çekilen görüntüleri ortaya çıktı. 2019 yılında Bolu'nun Mengen ilçesine bağlı Hayranlar köyünde bayram namazına katılan genç ...
[801] 2026-11-23 17:38 | kayserihaber.com (tr) | İspanyollar Jose Mourinho’nun Arda Güler kararını konuşuyor! PSV maçı öncesi belli oldu
[73] 2026-11-23 16:00 | Defensa Central (es) | Diomande, Cucurella, Valverde y Arda Güler deben potenciar el arsenal ofensivo del Madrid desde las bandas
[90] 2026-11-23 15:16 | Haberler.com (en) | Mourinho announced his decision on Arda Güler
[452] 2026-11-23 17:24 | Real Madrid CF | Web Oficial (es) | Peter Bosz: “El Real Madrid es un equipo muy fuerte y con determinación”
[207] 2026-11-23 20:30 | Sports Illustrated (en) | Real Madrid Rocked by Double Injury Blow Ahead of Crucial Champions League Push
[117] 2026-11-23 02:16 | En Son Haber (tr) | Dünyaca ünlü oyuncu Edward Norton ve oğlunun Arda Güler hayranlığı -- Hollywood'un efsane oyuncularından Edward Norton,, kendisinin ve 14 yaşındaki oğlunun milli futbolcu Arda Güler’e hayran olduğunu açıkladı. Bu açıklamanın ardından Arda Güler’den de anlamlı bir jest g ...
[769] 2026-11-23 17:33 | Fanatik (tr) | Thibaut Courtois'ten Arda Güler'e büyük övgü! 'Daha iyi oynuyoruz'
[66] 2026-11-23 14:31 | UEFA.com (es) | Alineaciones probables de la Champions League: noticias de los equipos, lesionados y sancionados en la jornada 5
[86] 2026-11-23 15:05 | Mundo Deportivo (en) | Endrick ruled out for three weeks
[459] 2026-11-23 16:00 | Diario AS (es) | ¿A qué hora es el Real Madrid - PSV? Canal TV, horario, dónde y cómo ver hoy la Champions online
[235] 2026-11-23 20:00 | Yahoo Sports (en) | Three talking points ahead of Real Madrid vs PSV | UEFA Champions League
[784] 2026-11-23 16:55 | Odakgazetesi (tr) | Cumhuriyet dünya liginde
[80] 2026-11-23 14:26 | Bernabéu Digital (es) | Convocatoria del Real Madrid ante el PSV: Endrick, la única novedad respecto al Celta
[100] 2026-11-23 14:05 | BeSoccer Livescore (en) | Real Madrid vs PSV compositions - Ligue des Champions 2026/27
[458] 2026-11-23 15:54 | Real Madrid CF | Web Oficial (es) | Juvenil A-PSV: a seguir líderes de la Youth League, en directo por Realmadrid TV
[234] 2026-11-23 19:58 | Yahoo Sports (en) | Official: Real Madrid squad for the UCL game against PSV
[760] 2026-11-23 16:52 | Notbir (tr) | Arda Güler Konate'ye sucuk hediye etti
[65] 2026-11-23 13:50 | HolaNews (es) | Un Madrid en alza recibe al PSV, líder de la Eredivisie, en busca de la décima consecutiva
[101] 2026-11-23 13:40 | Tribuna.com (en) | Mbappe in top-3: Current Real Madrid squad players with most goal involvements revealed
[456] 2026-11-23 12:11 | Real Madrid CF | Web Oficial (es) | Mourinho: "Los 12 puntos nos dan tranquilidad, pero no queremos jugar el playoff de la Champions"
[230] 2026-11-23 19:09 | Yahoo Sports (en) | Bayern Munich not pursuing move for struggling Real Madrid defender – report
[797] 2026-11-23 16:29 | Nefes Gazetesi (tr) | Arda Güler'in takım arkadaşına hediyesine bakın. Türkiye'den özel olarak göndertti
[81] 2026-11-23 13:50 | Tribuna.com (es) | Mbappé en el top-3: Se revelan los jugadores de la plantilla actual del Real Madrid con más participaciones de gol
[93] 2026-11-23 13:28 | 101 Great Goals (en) | Jose Mourinho says Arda Guler and Jude Bellingham are two of the 'good liars' in his Real Madrid squad
[488] 2026-11-23 11:37 | Real Madrid CF | Web Oficial (es) | El equipo de la Fundación participó en la primera fase de la Liga Genuine
[239] 2026-11-23 18:59 | Yahoo Sports (en) | Real Madrid in advanced talks with key superstar over renewal; announcement to follow soon
[763] 2026-11-23 15:15 | Afyon Postası (tr) | Arda Güler Real Madrid'de Afyon sucuğu dağıtıyor!
[75] 2026-11-23 13:00 | Defensa Central (es) | Los dos candidatos de Mourinho para dar descanso a Arda Güler no le terminan de convencer: Bernardo Silva juega más atrás y Thiago Pitarch apenas tiene minutos
[91] 2026-11-23 12:50 | Haberler.com (en) | Bad news for Arda Güler! Mourinho is doing what he said.
[450] 2026-11-23 11:07 | Real Madrid CF | Web Oficial (es) | Convocatoria del Real Madrid ante el PSV
[220] 2026-11-23 18:45 | The New York Times (en) | Jude Bellingham in advanced talks over Real Madrid long-term contract renewal
[772] 2026-11-23 15:14 | Haber 7 (tr) | Mourinho maç sonu açıklamıştı! Arda Güler için kararını verdi
[77] 2026-11-23 13:00 | Defensa Central (es) | Guille Uzquiano, analista deportivo: "A Vinicius se le vio con la chispa que parecía haber perdido, Mourinho hace bien en darle la titularidad ante el PSV, pero hay que ver si es por Diomande, Bernardo Silva o Güler"
[88] 2026-11-23 11:45 | BeSoccer Livescore (en) | Real Madrid vs PSV Lineups - Champions League 2026/27
[475] 2026-11-23 09:09 | Real Madrid CF | Web Oficial (es) | Mourinho y un jugador comparecerán en rueda de prensa después del entrenamiento
[214] 2026-11-23 17:24 | Real Madrid CF | Web Oficial (en) | Peter Bosz: “Real Madrid is a very strong team with determination”
[802] 2026-11-23 15:06 | Haberler (tr) | Mourinho, Arda Güler için kararını duyurdu
[72] 2026-11-23 12:54 | FútbolFantasy (es) | Mourinho habla de Vinícius, Bernardo Silva, Espí, Güler, Bellingham, el PSV, el calendario y las remontadas
[99] 2026-11-23 11:25 | Foot-Africa.com (en) | Real Madrid to start Brahim Díaz against PSV Eindhoven
[453] 2026-11-23 09:07 | Real Madrid CF | Web Oficial (es) | El Real Madrid-Osasuna se jugará el domingo, 13 de diciembre, a las 21:00 h
[257] 2026-11-23 16:46 | Diario AS (en) | Real Madrid: Kings of chaos
[788] 2026-11-23 14:23 | Onedio (tr) | Arda Güler'in Takım Arkadaşı Konate'ye Gönderdiği Paket Sosyal Medyaya DÜştü
[76] 2026-11-23 12:19 | Defensa Central (es) | Courtois (34 años): “En un equipo con Bellingham, Mbappé, Vinicius, Diomande o Arda Güler es muy difícil que alguien me vaya a votar a mí para el Balón de Oro”
[238] 2026-11-23 15:54 | Real Madrid CF | Web Oficial (en) | Juvenil A-PSV: aiming to stay on top of the Youth League, live on Realmadrid TV
[767] 2026-11-23 14:20 | Odatv (tr) | Arda Güler takım arkadaşına sucuk hediye etti
[242] 2026-11-23 15:47 | Official FC Barcelona Website (en) | Real Madrid | La Liga Matchday 10 - FC Barcelona
[778] 2026-11-23 13:55 | Afyon Şehir (tr) | Arda Güler’den Afyonkarahisar’ın Meşhur Sucuğuyla Sürpriz Jest
[254] 2026-11-23 15:21 | Managing Madrid (en) | Real Madrid announce squad for Champions League match against PSV Eindhoven
[803] 2026-11-23 13:39 | SuperHaber (tr) | 5 yıllık sözleşmesi bulunuyordu: Real Madrid'de Mourinho'dan Arda Güler'i kararı
[246] 2026-11-23 14:42 | Yahoo Sports (en) | Confirmed: Real Madrid squad list to face PSV Eindhoven | UEFA Champions League
[780] 2026-11-23 13:36 | Fanatik (tr) | Arda Güler gave Real Madrid's star a gift of sucuk! He shared it on Instagram: "Bro, I love you."
[244] 2026-11-23 13:44 | Yahoo Sports (en) | Real Madrid blow as Endrick suffers fresh injury setback ahead of Champions League clash
[764] 2026-11-23 13:17 | Gazete Vatan (tr) | Kayserispor’da rota Avrupa! Yeni Arda Güler için harekete geçildi - Sampiy10 Spor Haberleri
[223] 2026-11-23 12:11 | Real Madrid CF | Web Oficial (en) | Mourinho: ‘The 12 points give us some breathing space, but we don’t want to be in the Champions League play-offs’
[771] 2026-11-23 13:09 | Ege Saati (tr) | Arda Güler'e kötü haber! Mourinho'dan dinlendirme kararı
[229] 2026-11-23 11:30 | Real Madrid CF | Web Oficial (en) | Medical Report: Endrick
[770] 2026-11-23 12:57 | Fanatik (tr) | Arda Güler, Real Madrid'in yıldızına sucuk hediye etti! Instagram'dan paylaştı: Kardeşim seni seviyorum
[213] 2026-11-23 11:07 | Real Madrid CF | Web Oficial (en) | Real Madrid squad list against PSV
[777] 2026-11-23 12:48 | Yeni Şafak (tr) | Arda Güler'in takım arkadaşı Konate'ye hediyesi gündem oldu
[761] 2026-11-23 12:43 | beinsports.com.tr (tr) | Jose Mourinho dan Arda Güler e kötü haber PSV maçında
[782] 2026-11-23 12:00 | Odatv (tr) | Yıllar sonra ortaya çıktı: Küçük Arda'nın 'bayram' mesaisi
[787] 2026-11-23 11:49 | Onedio (tr) | Arda Güler'in 8 Yıldaki Değişimi Ortaya Çıktı
[776] 2026-11-23 11:45 | Köroğlu Gazetesi (tr) | Arda Güler'in, Bolu'da bayramlaşma görüntüleri ortaya çıktı!
[759] 2026-11-23 11:05 | CNN Türk (tr) | Arda Güler'in yıllar önceki görüntüsü şimdi ortaya çıktı
[779] 2026-11-23 10:38 | Notbir (tr) | Edward Norton ve oğlu Arda Güler hayranı çıktı: Milli yıldızdan sürpriz
[785] 2026-11-23 10:37 | Kars Manşet (tr) | Arda Güler’in 14 yaşındayken Bolu’da bayram namazına katıldığı görüntüler ortaya çıktı
[774] 2026-11-23 10:17 | Odatv (tr) | Mourinho'dan sürpriz Arda Güler kararı
[786] 2026-11-23 10:05 | BirGün Gazetesi (tr) | La Liga’da haftanın görünümü: Barcelona geri döndü, Real Madrid’den üst üste altıncı galibiyet
[775] 2026-11-23 09:35 | Notbir (tr) | Jose Mourinho'dan Arda Güler kararı
[783] 2026-11-23 09:02 | Sabah (tr) | Jose Mourinho’dan Arda Güler kararı! Celta Vigo maçında sinyali vermişti...
[789] 2026-11-23 09:01 | NirvanaHaber (tr) | Acun Ilıcalı'yı duygulandıran dev maç: "Kalbimiz Fenerbahçe ile ama Arda..."
