import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final dark = Theme.of(context).brightness == Brightness.dark;
    final cardColor = dark ? const Color(0xFF161B24) : Colors.white;
    final textSub = dark ? Colors.white38 : Colors.black38;

    return Scaffold(
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(children: [
            // Banner
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                  color: cardColor, borderRadius: BorderRadius.circular(16)),
              child: Row(children: [
                Container(
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                      color: const Color(0xFF153323),
                      borderRadius: BorderRadius.circular(12)),
                  child: const Icon(Icons.workspace_premium,
                      color: Color(0xFF2EE6A8)),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                    Text('خصم أول الشهر 19 جنيه بدل 79 جنيه',
                        style: const TextStyle(fontWeight: FontWeight.bold)),
                    Text('كافئ نفسك', style: TextStyle(color: textSub)),
                  ]),
                ),
                TextButton(
                    onPressed: () {},
                    child: const Text('الحق الخصم',
                        style: TextStyle(
                            color: Color(0xFF2EE6A8),
                            fontWeight: FontWeight.bold))),
              ]),
            ),
            const SizedBox(height: 16),
            // Header
            Row(children: [
              Container(
                width: 44, height: 44,
                decoration: BoxDecoration(
                    color: const Color(0xFF153323), shape: BoxShape.circle),
                child: const Center(
                    child: Text('W',
                        style: TextStyle(
                            color: Color(0xFF2EE6A8),
                            fontWeight: FontWeight.w900,
                            fontStyle: FontStyle.italic,
                            fontSize: 22))),
              ),
              const SizedBox(width: 10),
              const Text('Wadidget',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
              const Spacer(),
              IconButton(
                  icon: const Icon(Icons.search),
                  onPressed: () {}),
              IconButton(
                  icon: const Icon(Icons.favorite_border),
                  onPressed: () {}),
            ]),
            const SizedBox(height: 12),
            // Supporters row
            Row(children: [
              for (var i = 0; i < 4; i++)
                Align(
                  widthFactor: 0.7,
                  child: CircleAvatar(
                    radius: 16,
                    backgroundColor: Colors.primaries[i * 3 % Colors.primaries.length].shade300,
                    child: const Icon(Icons.person, size: 16, color: Colors.white),
                  ),
                ),
              const SizedBox(width: 14),
              Text('6 داعم <', style: TextStyle(color: textSub)),
              const SizedBox(width: 12),
              Text('|', style: TextStyle(color: textSub)),
              const SizedBox(width: 12),
              const Text('272', style: TextStyle(fontWeight: FontWeight.bold)),
              Text(' ويدجت', style: TextStyle(color: textSub)),
              const SizedBox(width: 12),
              const Text('177', style: TextStyle(fontWeight: FontWeight.bold)),
              Text(' مجاني', style: TextStyle(color: textSub)),
            ]),
            const SizedBox(height: 20),
            // Tabs
            Row(children: [
              Container(
                padding:
                    const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
                decoration: BoxDecoration(
                    color: const Color(0xFFFF7A1A),
                    borderRadius: BorderRadius.circular(16)),
                child: const Text('الأبرز',
                    style: TextStyle(fontWeight: FontWeight.bold)),
              ),
              const SizedBox(width: 12),
              Text('عرض الكل', style: TextStyle(color: textSub)),
            ]),
            const SizedBox(height: 16),
            // Widgets row: match + prayer clock
            Row(children: [
              Expanded(
                child: Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                      color: cardColor, borderRadius: BorderRadius.circular(20)),
                  child: Column(children: [
                    Row(children: [
                      Text('الدوري الإنجليزي الممتاز',
                          style: TextStyle(color: textSub, fontSize: 11)),
                      const Spacer(),
                      Text('الجمعة، 2 أكتوبر 2026',
                          style: TextStyle(color: textSub, fontSize: 10)),
                    ]),
                    const SizedBox(height: 12),
                    Row(
                        mainAxisAlignment: MainAxisAlignment.spaceAround,
                        children: [
                          Column(children: [
                            const Icon(Icons.shield, color: Colors.red),
                            Text('ليفربول',
                                style: TextStyle(fontSize: 11, color: textSub)),
                          ]),
                          const Column(children: [
                            Text('2 - 1',
                                style: TextStyle(
                                    fontSize: 22, fontWeight: FontWeight.bold)),
                            Text("78'",
                                style:
                                    TextStyle(color: Colors.orange, fontSize: 11)),
                          ]),
                          Column(children: [
                            const Icon(Icons.shield, color: Colors.red),
                            Text('مان يونايتد',
                                style: TextStyle(fontSize: 11, color: textSub)),
                          ]),
                        ]),
                    const SizedBox(height: 10),
                    Row(
                        mainAxisAlignment: MainAxisAlignment.spaceAround,
                        children: [
                          Text('ليفربول', style: TextStyle(color: textSub, fontSize: 10)),
                          Text("56', 12'", style: TextStyle(color: textSub, fontSize: 10)),
                          Text("78'", style: TextStyle(color: textSub, fontSize: 10)),
                          Text('مان يونايتد', style: TextStyle(color: textSub, fontSize: 10)),
                        ]),
                  ]),
                ),
              ),
              const SizedBox(width: 10),
              Expanded(child: _prayerClock(cardColor, textSub)),
            ]),
            const SizedBox(height: 14),
            // Big prayer clock
            _prayerClock(cardColor, textSub, big: true),
          ]),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: dark ? const Color(0xFF10141B) : Colors.white,
        selectedItemColor: const Color(0xFFFF7A1A),
        unselectedItemColor: textSub,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home_filled), label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.sports_soccer), label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.grid_view), label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.image_outlined), label: ''),
          BottomNavigationBarItem(icon: Icon(Icons.person_outline), label: ''),
        ],
      ),
    );
  }

  Widget _prayerClock(Color cardColor, Color textSub, {bool big = false}) {
    return Container(
      padding: EdgeInsets.all(big ? 20 : 14),
      decoration: BoxDecoration(
          color: cardColor, borderRadius: BorderRadius.circular(20)),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Row(children: [
          Text('السبت', style: TextStyle(fontSize: big ? 20 : 14, color: textSub)),
          const Spacer(),
          Text('9 محرم 1448',
              style: TextStyle(fontSize: 11, color: textSub)),
        ]),
        Text('24 يونيو 2026', style: TextStyle(fontSize: 11, color: textSub)),
        const SizedBox(height: 6),
        RichText(
          text: TextSpan(children: [
            TextSpan(
                text: '06:',
                style: TextStyle(
                    fontSize: big ? 44 : 26,
                    fontWeight: FontWeight.w900,
                    color: Colors.white)),
            TextSpan(
                text: '58',
                style: TextStyle(
                    fontSize: big ? 44 : 26,
                    fontWeight: FontWeight.w900,
                    color: const Color(0xFFFF7A1A))),
          ]),
        ),
        const SizedBox(height: 6),
        Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          for (final e in [
            ['الفجر', '4:10'],
            ['الشروق', '5:06'],
            ['الظهر', '12:58'],
            ['العصر', '16:33'],
            ['المغرب', '20:00'],
            ['العشاء', '21:33'],
          ])
            Column(children: [
              Text(e[0], style: TextStyle(fontSize: 10, color: textSub)),
              Text(e[1],
                  style: TextStyle(
                      fontSize: 11,
                      fontWeight: FontWeight.bold,
                      color: e[0] == 'الظهر' ? const Color(0xFFFF7A1A) : Colors.white)),
            ]),
        ]),
        if (big) ...[
          const SizedBox(height: 10),
          Text('لونان', style: TextStyle(fontSize: 12, color: textSub)),
          Text('خط الثلث', style: TextStyle(fontSize: 12, color: textSub)),
        ],
      ]),
    );
  }
}