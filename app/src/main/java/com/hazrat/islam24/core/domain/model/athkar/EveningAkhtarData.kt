package com.hazrat.islam24.core.domain.model.athkar

data class EveningAkhtarData(
    val number: Int,
    val transliteration: String,
    val translation: String,
    val count: Int = 1
)


val eveningAkhtar = listOf(
    EveningAkhtarData(
        number = 1,
        transliteration = "Allāhu lā ilāha illā Huwa-l-Ḥayyu-l-Qayyūm, lā ta’khudhuhū sinatuw-wa lā nawm, lahū mā fi-s-samāwāti wa mā fi-l-arḍ, man dhā’lladhī yashfaʿu ʿindahū illā bi-idhnih, yaʿlamu mā bayna aydīhim wa mā khalfahum, wa lā yuḥīṭūna bi-shay’im-min ʿilmihī illā bi-mā shā’, wasiʿa kursiyyuhu-s-samāwāti wa-l-arḍ, wa lā ya’ūduhū ḥifẓuhumā wa Huwa-l-ʿAlliyu-l-ʿAẓīm.",
        translation = "Allah! There is no god ˹worthy of worship˺ except Him, the Ever-Living, All-Sustaining. Neither drowsiness nor sleep overtakes Him. To Him belongs whatever is in the heavens and whatever is on the earth. Who could possibly intercede with Him without His permission? He ˹fully˺ knows what is ahead of them and what is behind them, but no one can grasp any of His knowledge—except what He wills ˹to reveal˺. His Seat1 encompasses the heavens and the earth, and the preservation of both does not tire Him. For He is the Most High, the Greatest."
    ),
    EveningAkhtarData(
        number = 2,
        transliteration = "Bismi-llāhi-r-Raḥmāni-r-Raḥīm. Qul Huwa-llāhu Aḥad. Allāhu-ṣ-Ṣamad. Lam yalid wa lam yūlad. Wa lam yakul-lahū kufuwan aḥad.",
        translation = "In the name of Allah, the All-Merciful, the Very Merciful. Say, He is Allah, the One, the Self-Sufficient Master, Who has not given birth and was not born, and to Whom no one is equal. (112)",
        count = 3
    ),
    EveningAkhtarData(
        number = 3,
        transliteration = "Bismi-llāhi-r-Raḥmāni-r-Raḥīm. Qul aʿūdhu bi-Rabbi-l-falaq. Min sharri mā khalaq. Wa min sharri ghāsiqin idhā waqab. Wa min sharri-n-naffāthāti fi-l-ʿuqad. Wa min sharri ḥāsidin idhā ḥasad.",
        translation = "In the name of Allah, the All-Merciful, the Very Merciful. Say, I seek protection of the Lord of the daybreak, from the evil of what He has created, and from the evil of the darkening night when it settles, and from the evil of the blowers in knots, and from the evil of the envier when he envies. (113)",
        count = 3
    ),
    EveningAkhtarData(
        number = 4,
        transliteration = "Bismi-llāhi-r-Raḥmāni-r-Raḥīm. Qul aʿūdhu bi-Rabbi-n-nās. Maliki-n-nās. Ilāh-hin-nās. Min sharri-l-waswāsi-l-khannās. Al-ladhī yuwaswisu fī ṣudūri-n-nās. Mina-l-jinnati wa-n-nās.",
        translation = "In the name of Allah, the All-Merciful, the Very Merciful. Say, I seek protection of the Lord of mankind, the King of mankind, the God of mankind, from the evil of the whisperer who withdraws, who whispers in the hearts of mankind, whether they be Jinn or people. (114)",
        count = 3
    ),
    EveningAkhtarData(
        number = 5,
        transliteration = "Allāhumma Anta Rabbī, lā ilāha illā Ant, khalaqtanī wa ana ʿabduk, wa ana ʿalā ʿahdika wa waʿdika mā’staṭaʿt, aʿūdhu bika min sharri mā ṣanaʿt, abū’u laka bi niʿmatika ʿalayya wa abū’u laka bi-dhambī fa-ghfir lī fa-innahū lā yaghfiru-dh-dhunūba illā Ant.",
        translation = "O Allah, You are my Lord. There is no god worthy of worship except You. You have created me, and I am Your slave, and I am under Your covenant and pledge (to fulfil it) to the best of my ability. I seek Your protection from the evil that I have done. I acknowledge the favours that You have bestowed upon me, and I admit my sins. Forgive me, for none forgives sins but You."
    ),
    EveningAkhtarData(
        number = 6,
        transliteration = "Allāhumma innī aʿūdhu bika min-l-hammi wa-l-ḥazan, wa aʿūdhu bika min-l-ʿajzi wa-l-kasal, wa aʿūdhu bika min-l-jubni wa-l-bukhl, wa aʿūdhu bika min ghalabati-d-dayni wa qahri-r-rijāl.",
        translation = "O Allah, I seek Your protection from anxiety and grief. I seek Your protection from inability and laziness. I seek Your protection from cowardice and miserliness, and I seek Your protection from being overcome by debt and being overpowered by men."
    ),
    EveningAkhtarData(
        number = 7,
        transliteration = "aAllāhumma innī as’aluka-l-ʿāfiyata fi-d-dunyā wa-l-ākhirah. Allāhumma innī as’aluka-l-ʿafwa wa-l-ʿāfiyata fī dīnī wa dunyāya wa ahlī wa mālī, Allāhumma-stur ʿawrātī wa āmin rawʿātī. Allāhumma-ḥfaẓnī mim bayni yadayya wa min khalfī, wa ʿay-yamīnī wa ʿan shimālī wa min fawqī, wa aʿūdhu bi-ʿaẓamatika an ughtāla min taḥtī.",
        translation = "O Allah, I ask You for well-being in this world and the next. O Allah, I ask You for forgiveness and well-being in my religion, in my worldly affairs, in my family and in my wealth. O Allah, conceal my faults and calm my fears. O Allah, guard me from in front of me and behind me, from my right, and from my left, and from above me. I seek protection in Your Greatness from being unexpectedly destroyed from beneath me."
    ),
    EveningAkhtarData(
        number = 8,
        transliteration = "Allāhumma ʿālima-l-ghaybi wa-sh-shahādah, fāṭir-as-samāwāti wa-l-arḍi, rabba kulli shay’iw-wa malīkah, ash-hadu al-lā ilāha illā Anta, aʿūdhu bika min sharri nafsī wa min sharri-sh-shayṭāni wa shirkihī wa an aqtarifa ʿalā nafsī sū’an aw ajurrahū ilā muslim.",
        translation = "O Allah, Knower of the unseen and the seen, Creator of the heavens and the earth, the Lord and Sovereign of everything; I bear witness that there is no god worthy of worship but You. I seek Your protection from the evil of my own self, from the evil of Shayṭān and from the evil of polytheism to which he calls,and from inflicting evil on myself, or bringing it upon a Muslim."
    ),
    EveningAkhtarData(
        number = 9,
        transliteration = "Yā Ḥayyu yā Qayyūm, bi-raḥmatika astaghīth, aṣliḥ lī sha’nī kullah, wa lā takilnī ilā nafsī ṭarfata ʿayn.",
        translation = "O The Ever Living, The Sustainer of all. ; I seek assistance through Your mercy. Rectify all of my affairs and do not entrust me to myself for the blink of an eye."
    ),
    EveningAkhtarData(
        number = 10,
        transliteration = "Allāhumma mā amsā bī min niʿmatin aw bi-aḥadin min khalqik, fa-minka waḥdaka lā sharīka lak, fa laka-l-ḥamdu wa laka-sh-shukr.",
        translation = "O Allah, all the favours that I or anyone from Your creation has received in the evening, are from You Alone. You have no partner. To You Alone belong all praise and all thanks."
    ),
    EveningAkhtarData(
        number = 11,
        transliteration = "Amsaynā ʿalā fiṭrati-l-islām, wa ʿalā kalimati-l-ikhlāṣ, wa ʿalā dīni Nabiyyinā Muḥammadin ṣallallāhu ʿalayhi wa sallam, wa ʿalā millati abīnā Ibrāhīma ḥanīfam-muslima, wa mā kāna mina-l-mushrikīn.",
        translation = "We have entered the evening upon the natural religion of Islam, the statement of pure faith (i.e. Shahādah), the religion of our Prophet Muhammad ﷺ and upon the way of our father Ibrāhīm, who turned away from all that is false, having surrendered to Allah, and he was not of the polytheists. (Nasā’ī)\n"
    ),
    EveningAkhtarData(
        number = 12,
        transliteration = "Amsaytu uthnī ʿalayka ḥamdā, wa ash-hadu al-lā ilāha illāl-llāh.",
        translation = "I have entered the evening praising You, and I bear witness that there is no god worthy of worship but Allah.",
        count = 1
    ),
    EveningAkhtarData(
        number = 13,
        transliteration = "Amsaynā wa amsa-l-mulku li-llāh, wa-l-ḥamdu li-llāh, lā ilāha illa-llāhu waḥdahū lā sharīka lah, lahu-l-mulku wa lahu-l-ḥamd, wa huwa ʿalā kulli shay’in Qadīr, Rabbi as’aluka khayra mā fī hādhihi-l-laylati wa khayra mā baʿdah, wa aʿūdhu bika min sharri mā fī hādhihi-l-laylati wa sharri mā baʿdah. Rabbi aʿūdhu bika mina-l-kasali wa sū’i-l-kibar, Rabbi aʿūdhu bika min ʿadhābin fi-n-nāri wa ʿadhābin fi-l-qabr.",
        translation = "We have entered the evening and at this very time the whole kingdom belongs to Allah. All praise is due to Allah. There is no god worthy of worship except Allah, the One; He has no partner with Him. The entire kingdom belongs solely to Him, to Him is all praise due, and He is All-Powerful over everything. My Lord, I ask You for the good that is in this night and the good that follows it, and I seek Your protection from the evil that is in this night and from the evil that follows it. My Lord, I seek Your protection from laziness and the misery of old age. My Lord, I seek Your protection from the torment of the Hell-fire and the punishment of the grave.",
        count = 1
    ),
    EveningAkhtarData(
        number = 14,
        transliteration = "Amsaynā wa amsa-l-mulku li-llāhi Rabbi-l-ʿālamīn, Allāhumma innī as’aluka khayra hādhihi-l-laylah, fatḥahā wa naṣrahā wa nūrahā wa barakatahā wa hudāhā, wa aʿūdhu bika min sharri mā fīhā wa sharri mā baʿdahā.",
        translation = "We have entered the evening and at this very time the whole kingdom belongs to Allah, Lord of the Worlds. O Allah, I ask You for the goodness of this day/night: its victory, its help, its light, and its blessings and guidance. I seek Your protection from the evil that is in it and from the evil that follows it.",
        count = 1
    ),
    EveningAkhtarData(
        number = 15,
        transliteration = "Allāhumma innī amsaytu ush-hiduka, wa ush-hidu ḥamlata ʿarshika, wa malā’ikatika, wa jamī’a khalqika, annaka Anta-llāhu lā ilāha illā Anta waḥdak, lā sharīka lak, wa an-na Muḥammadan ʿabduka wa rasūluk.",
        translation = "O Allah, I have entered the evening, and I call upon You, the bearers of Your Throne, Your angels and all creation, to bear witness that surely You are Allah. There is no god worthy of worship except You Alone. You have no partners, and that Muḥammad ﷺ is Your slave and Your Messenger.",
        count = 1
    ),
    EveningAkhtarData(
        number = 16,
        transliteration = "Allāhumma bika amsaynā wa bika aṣbaḥnā wa bika naḥyā wa bika namūtu wa ilayka-l-maṣīr.",
        translation = "O Allah, by You we have entered the evening and by You we enter upon the morning. By You, we live and we die, and to You is the resurrection/return.",
        count = 1
    ),
    EveningAkhtarData(
        number = 17,
        transliteration = "Allāhumma ʿāfinī fī badanī, Allāhumma ʿāfinī fī samʿī, Allāhumma ʿāfinī fī baṣarī, lā ilāha illā Ant, Allāhumma innī aʿūdhu bika mina-l-kufri wa-l-faqr, wa aʿūdhu bika min ʿadhābi-l-qabr, lā ilāha illā Ant.",
        translation = "O Allah, grant me well-being in my body. O Allah, grant me well-being in my hearing. O Allah, grant me well-being in my sight. There is no god worthy of worship except You. O Allah, I seek Your protection from disbelief and poverty and I seek Your protection from the punishment of the grave. There is no god worthy of worship except You.",
        count = 1
    ),
    EveningAkhtarData(
        number = 18,
        transliteration = "Ḥasbiya-Allāhu lā ilāha illā Huwa, ʿalayhi tawakkaltu, wa Huwa Rabbu-l-ʿArshi-l-ʿaẓīm.",
        translation = "Allah is sufficient for me. There is no god worthy of worship except Him. I have placed my trust in Him only and He is the Lord of the Magnificent Throne.",
        count = 1
    ),
    EveningAkhtarData(
        number = 19,
        transliteration = "Raḍītu bi-llāhi Rabbā, wa bi-l-islāmi dīnā, wa bi Muḥammadin-Nabiyyā.",
        translation = "I am pleased with Allah as my Lord, with Islām as my religion and with Muhammad ﷺ as my Prophet.",
        count = 3
    ),
    EveningAkhtarData(
        number = 20,
        transliteration = "Bismi-llāhi-lladhī lā yaḍurru maʿsmihi shay’un fi-l-arḍi wa lā fi-s-samā’, wa Huwa-s-Samīʿu-l-ʿAlīm.",
        translation = "In the Name of Allah, with whose Name nothing can harm in the earth nor in the sky. He is The All-Hearing and All-Knowing.",
        count = 3
    ),
    EveningAkhtarData(
        number = 21,
        transliteration = "Subḥāna-llāhi wa bi ḥamdih.",
        translation = "Allah is free from imperfection, and all praise is due to Him.",
        count = 100
    ),
    EveningAkhtarData(
        number = 22,
        transliteration = "Lā ilāha illā-Allāh, waḥdahū lā sharīka lah, lahu-l-mulk, wa lahu-l-ḥamd, wa Huwa ʿalā kulli shay’in Qadīr.",
        translation = "There is no god but Allah. He is Alone and He has no partner whatsoever. To Him Alone belong all sovereignty and all praise. He is over all things All-Powerful.",
        count = 100
    ),
    EveningAkhtarData(
        number = 23,
        transliteration = "Subḥāna-llāh. Alḥamdu li-llāh, Allāhu akbar.",
        translation = "Allah is free from imperfection. All praise be to Allah. Allah is the Greatest.",
        count = 33
    ),
    EveningAkhtarData(
        number = 24,
        transliteration = "Allāhumma ṣalli ʿalā Muḥammad wa ʿalā āli Muḥammad, kamā ṣallayta ʿalā Ibrāhīma wa ʿalā āli Ibrāhīm, innaka Ḥamīdu-m-Majīd, Allāhumma bārik ʿalā Muḥammad, wa ʿalā āli Muḥammad, kamā bārakta ʿalā Ibrāhīma wa ʿalā āli Ibrāhīm, innaka Ḥamīdu-m-Majīd.",
        translation = "O Allah, honour and have mercy upon Muhammad and the family of Muhammad as You have honoured and had mercy upon Ibrāhīm and the family of Ibrāhīm. Indeed, You are the Most Praiseworthy, the Most Glorious. O Allah, bless Muhammad and the family of Muhammad as You have blessed Ibrāhīm and the family of Ibrāhīm. Indeed, You are the Most Praiseworthy, the Most Glorious.",
        count = 10
    ),
)