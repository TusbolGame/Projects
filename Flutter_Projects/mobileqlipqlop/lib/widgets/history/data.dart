import 'package:flutter/material.dart';

class Doodle {
  final String name;
  final String time;
  final String content;
  final String doodle;
  final Color iconBackground;
  final Icon icon;
  const Doodle(
      {this.name,
      this.time,
      this.content,
      this.doodle,
      this.icon,
      this.iconBackground});
}

const List<Doodle> doodles = [
  Doodle(
      name: "Jakarta - Bali",
      time: "2020/01/12-2020/01/15",
      content:
          "One of Al-Sufi's greatest works involved fact-checking the Greek astronomer Ptolemy's measurements of the brightness and size of stars. In the year 964 AD, Al-Sufi published his findings in a book titled Kitab al-Kawatib al-Thabita al-Musawwar, or The Book of Fixed Stars. In many cases, he confirmed Ptolemy’s discoveries, but he also improved upon his work by illustrating the constellations and correcting some of Ptolemy’s observations about the brightness of stars.",
      doodle:
          "https://lh3.googleusercontent.com/proxy/LcG0a0AIzp9T4PpHFu7Rs4qa3BWOng1txICcuN-0ZiL_rc1K1eSPL1b1jv7a83xbi6ybNFOyyK_Sofbf8lRKrXXlGXXy61JOiydRyrsBuJECU4oFNSPD3FEYKJSoxAC3-baLPlS9jHqw5LfyUdoDhQA6PMSQBxItUD1EtM_vCPgr=w336-h190-n-k-no",
      icon: Icon(Icons.star, color: Colors.white),
      iconBackground: Colors.cyan),
  Doodle(
      name: "Kuta - Komodo",
      time: "2020/01/30-2020/02/05",
      content:
          " Abu al-Wafa' is an innovator whose contributions to science include one of the first known introductions to negative numbers, and the development of the first quadrant, a tool used by astronomers to examine the sky. His pioneering work in spherical trigonometry was hugely influential for both mathematics and astronomy.",
      doodle:
          "https://lh5.googleusercontent.com/proxy/HwI00tUSt-S47pkYt9pVpIRj7_WdOmFLj4fvoB6V0_3oWd4A4Rs7TKnxQ9nB2_IRTCOgy-83wi29_mDfBYJQ7JWbZZF_Vqcma_TKf5qjHdYL6HcwlGpicu_lov0hVk0AdMbUle92Rfy2D527upiFGWEYD11LskNbA5I2mOp6QHOC=w336-h190-n-k-no",
      icon: Icon(
        Icons.exposure,
        color: Colors.white,
      ),
      iconBackground: Colors.redAccent),
  Doodle(
      name: "Lombok - Gili Islands",
      time: "2020/01/05-2020/02/15",
      content:
          "Ibn al-Haytham was the first to explain through experimentation that vision occurs when light bounces on an object and then is directed to one's eyes. He was also an early proponent of the concept that a hypothesis must be proved by experiments based on confirmable procedures or mathematical evidence—hence understanding the scientific method five centuries before Renaissance scientists.",
      doodle:
          "https://lh3.googleusercontent.com/proxy/iauhmROTGTHfs6iQLkkvn4eJFP9k5WWBw5hXB2zg4dQsXamg0ei6qmH67lcczHw3C6i3lLisVShPbJrQgGBj74Bex3a82div5Wm9LN6YhdzrDLlgO35yuycbIExBBE-n-IBtJtYJqsYg3zbvihN6uqSqn9ZefyKJ2pvx4sj4GEPR=w336-h190-n-k-no",
      icon: Icon(
        Icons.visibility,
        color: Colors.black87,
        size: 32.0,
      ),
      iconBackground: Colors.yellow),
  Doodle(
      name: "Yogyakarta - Denpasar",
      time: "2020/02/12-2020/02/18",
      content:
          "Biruni is regarded as one of the greatest scholars of the Golden Age of Muslim civilisation and was well versed in physics, mathematics, astronomy, and natural sciences, and also distinguished himself as a historian, chronologist and linguist. He studied almost all fields of science and was compensated for his research and strenuous work. Royalty and powerful members of society sought out Al-Biruni to conduct research and study to uncover certain findings.",
      doodle:
          "https://lh5.googleusercontent.com/proxy/cCq-ruBF-Iolt49P8bVl6AVNVM3cuSLx9MAFnv8maRSnCmY3gIBmej9VH-BimvWkRbTScmwKbncV7C0JaXtfcTLXpX4-fzt8sGEtfuwFS6b78O3PD22TKgcoQBRWI4A9DMo7Ssn0uFgY2II2oIw1ejyAauZR0z4ekgbPPMbkv3-3=w336-h190-n-k-no",
      icon: Icon(
        Icons.account_balance,
        color: Colors.black87,
      ),
      iconBackground: Colors.amber),
  Doodle(
      name: "Jimbaran - Nusa Lembongan",
      time: "2020/02/02-2020/02/05",
      content:
          "Avicenna (Ibn Sīnā) was a Persian polymath who is regarded as one of the most significant physicians, astronomers, thinkers and writers of the Islamic Golden Age. He has been described as the father of early modern medicine. Of the 450 works he is known to have written, around 240 have survived, including 150 on philosophy and 40 on medicine.\nHis most famous works are The Book of Healing, a philosophical and scientific encyclopedia, and The Canon of Medicine, a medical encyclopedia which became a standard medical text at many medieval universities and remained in use as late as 1650. In 1973, Avicenna's Canon Of Medicine was reprinted in New York.\nBesides philosophy and medicine, Avicenna's corpus includes writings on astronomy, alchemy, geography and geology, psychology, Islamic theology, logic, mathematics, physics and works of poetry.",
      doodle:
          "https://lh4.googleusercontent.com/proxy/5liFBdHxQ2mbdtKCn-rdlsomhQjTP0KGuuCzh1aokuqVFkM7xFVtLsmNDW9uBIptcgu4iMdFEAc8dYkhXCIboL7eOC7ZoJxyU3uj1K8HLUsmQZ55dN1v5P4jP0ww7_Oq7no63LHc79vPYfGWJC1W_q9aA1w-FUO_9i8baTQtl_Jv=w336-h190-n-k-no",
      icon: Icon(
        Icons.healing,
        color: Colors.white,
      ),
      iconBackground: Colors.green),
  Doodle(
      name: "Surabaya - Canggu",
      time: "2020/01/14-2020/01/15",
      content:
          "Averroes was an Andalusian philosopher and thinker who wrote about many subjects, including philosophy, theology, medicine, astronomy, physics, Islamic jurisprudence and law, and linguistics. His philosophical works include numerous commentaries on Aristotle, for which he was known in the West as The Commentator. He also served as a judge and a court physician for the Almohad Caliphate.",
      doodle:
          "https://lh5.googleusercontent.com/proxy/kzXWa43tV2E7YXmszwTE0-c7c5erMnYWIkMDOBHhZ_ZXEVBqGm97ifPco-3IlS_1hTGFosep17w-IA34CzZtYQCXWIsxWqSbihAKyqN3uppJEgcforhwsJgR0ZJnDn3N_BiEeGjD1S5F_lSvCxxzt1pVyAiCzKcEcPn9NsOE0EBg=w336-h190-n-k-no",
      icon: Icon(
        Icons.blur_circular,
        color: Colors.white,
      ),
      iconBackground: Colors.indigo),
  Doodle(
      name: "Kintamani - Bandung",
      time: "2020/01/12-2020/01/16",
      content:
          "Tusi was a Persian polymath, architect, philosopher, physician, scientist, and theologian. He is often considered the creator of trigonometry as a mathematical discipline in its own right. Ibn Khaldun (1332–1406) considered Al-Tusi to be the greatest of the later Persian scholars.",
      doodle:
          "https://lh4.googleusercontent.com/proxy/cwwVR24y6BcL1_pVJKOIWQxH8546snxoLhL7efYq5bQDhUlnmTeYISR6aBWE1w8NC247AC8Y2sz7F6TWaUr8rha5P1-K-LZ4AJbagaai-tRT0AzqP8vYRqvhUlfRPXUJhSzqJ738IXPMW8ngjePmZjGGJO7t1ovRrQtGJw8Xwjhq=w336-h190-n-k-no",
      icon: Icon(
        Icons.category,
        color: Colors.white,
      ),
      iconBackground: Colors.pinkAccent),
  Doodle(
      name: "Batam - Malang",
      time: "2020/01/10-2020/01/12",
      content:
          "Over a period of thirty years, Ibn Battuta visited most of the Islamic world and many non-Muslim lands, including North Africa, the Horn of Africa, West Africa, the Middle East, Central Asia, Southeast Asia, South Asia and China. Near the end of his life, he dictated an account of his journeys, titled A Gift to Those Who Contemplate the Wonders of Cities and the Marvels of Travelling (Tuḥfat an-Nuẓẓār fī Gharāʾib al-Amṣār wa ʿAjāʾib al-Asfār), usually simply referred to as The Travels (Rihla). This account of his journeys provides a picture of a medieval civilisation that is still widely consulted today.",
      doodle:
          "https://lh5.googleusercontent.com/proxy/3k4Ht_fbXqRJlcZH6LVS9rJIQ28dcww4oxtSFz_UkZ_Ze9E3VPB7G7x4YwGhf7rdunJ_tlQNWyIwPtK6viilwwefVrI8-q5HQBUbY9ynSoWuayoBfvmj-KX41nMWBDtPhwLNEYxy6TuZz5bpO3cTBIeoSM4n6K56Mit0zmNJo41j=w336-h190-n-k-no",
      icon: Icon(
        Icons.navigation,
        color: Colors.white,
        size: 32.0,
      ),
      iconBackground: Colors.deepPurpleAccent),
  Doodle(
      name: "Medan - Makassar",
      time: "2020/01/22-2020/01/30",
      content:
          "He is widely considered as a forerunner of the modern disciplines of historiography, sociology, economics, and demography.\nHe is best known for his book, the Muqaddimah or Prolegomena ('Introduction'). The book influenced 17th-century Ottoman historians like Kâtip Çelebi, Ahmed Cevdet Pasha and Mustafa Naima, who used the theories in the book to analyse the growth and decline of the Ottoman Empire. Also, 19th-century European scholars acknowledged the significance of the book and considered Ibn Khaldun to be one of the greatest philosophers of the Middle Ages.",
      doodle:
          "https://lh5.googleusercontent.com/p/AF1QipP43IdM17wA4BgjQtuDd8spSwoTrjrgPTracCzg=w336-h190-n-k-no",
      icon: Icon(
        Icons.supervised_user_circle,
        color: Colors.white,
      ),
      iconBackground: Colors.teal),
  Doodle(
      name: "Manado - Padang",
      time: "2020/01/15-2020/02/15",
      content:
          "He is primarily known today for his maps and charts collected in his Kitab-ı Bahriye (Book of Navigation), a book that contains detailed information on navigation, as well as very accurate charts (for their time) describing the important ports and cities of the Mediterranean Sea. He gained fame as a cartographer when a small part of his first world map (prepared in 1513) was discovered in 1929 at the Topkapı Palace in Istanbul. His world map is the oldest known Turkish atlas showing the New World, and one of the oldest maps of America still in existence anywhere (the oldest known map of America that is still in existence is the map drawn by Juan de la Cosa in 1500). Piri Reis' map is centered on the Sahara at the latitude of the Tropic of Cancer.",
      doodle:
          "https://lh3.googleusercontent.com/proxy/0YxORwHDI-RnNGSUt4HDnn2kO-tCix3yIa2ncADkfqPhGzGqAWPjGuEd1ZXWiCmdzq1dgeAthBuKOdcguyV1ZNvTaGhxFT9bOSkGinSJMk3kfg3Cvgk5gfywEdoNVJahaImY0xHuhOnl29gI1d_JuR8UpXQdECBikipTxsMgfjD_=w336-h190-n-k-no",
      icon: Icon(
        Icons.map,
        color: Colors.white,
        size: 32.0,
      ),
      iconBackground: Colors.blue),
];
