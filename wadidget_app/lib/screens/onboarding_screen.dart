import 'package:flutter/material.dart';
import '../main.dart';
import 'permissions_screen.dart';

class OnboardingScreen extends StatefulWidget {
  const OnboardingScreen({super.key});
  @override
  State<OnboardingScreen> createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends State<OnboardingScreen> {
  final controller = PageController();
  int page = 0;

  @override
  Widget build(BuildContext context) {
    final dark = Theme.of(context).brightness == Brightness.dark;
    return Scaffold(
      body: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('تخطّ',
                      style: TextStyle(
                          color: dark ? Colors.white70 : Colors.black54,
                          fontSize: 14)),
                  Text('الخطوة ${page + 1} من 4',
                      style: TextStyle(
                          color: dark ? Colors.white70 : Colors.black54,
                          fontSize: 14)),
                  IconButton(
                    icon: const Icon(Icons.arrow_forward),
                    onPressed: page < 3
                        ? () => controller.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInOut)
                        : null,
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Row(
                children: List.generate(
                    4,
                    (i) => Expanded(
                          child: Container(
                            height: 4,
                            margin: const EdgeInsets.symmetric(horizontal: 4),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(4),
                              color: i <= page
                                  ? const Color(0xFFFF7A1A)
                                  : (dark ? Colors.white12 : Colors.black12),
                            ),
                          ),
                        )),
              ),
            ),
            Expanded(
              child: PageView(
                controller: controller,
                onPageChanged: (v) => setState(() => page = v),
                children: const [
                  _LanguageStep(),
                  _WidgetStyleStep(),
                  _CategoryStep(),
                  _ThemeStep(),
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            SizedBox(
              width: double.infinity,
              height: 56,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: dark ? Colors.white : const Color(0xFF10141B),
                  foregroundColor: dark ? Colors.black : Colors.white,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(28)),
                ),
                onPressed: () {
                  if (page < 3) {
                    controller.nextPage(
                        duration: const Duration(milliseconds: 300),
                        curve: Curves.easeInOut);
                  } else {
                    Navigator.of(context).pushReplacement(
                      MaterialPageRoute(builder: (_) => const PermissionsScreen()),
                    );
                  }
                },
                child: const Text('متابعة',
                    style:
                        TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
              ),
            ),
            const SizedBox(height: 8),
            TextButton(
              onPressed: () {},
              child: Text('تخطَّ واستخدم الإعدادات المقترحة',
                  style: TextStyle(
                      color: dark ? Colors.white38 : Colors.black38)),
            ),
          ],
        ),
      ),
    );
  }
}

// ---------- Step 1: Language ----------
class _LanguageStep extends StatefulWidget {
  const _LanguageStep();
  @override
  State<_LanguageStep> createState() => _LanguageStepState();
}

class _LanguageStepState extends State<_LanguageStep> {
  String lang = 'ar';
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(24),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          CircleAvatar(
              radius: 56,
              backgroundColor: Colors.white10,
              child: const Icon(Icons.translate, size: 48, color: Colors.white)),
          const SizedBox(height: 24),
          const Text('اختر لغتك',
              style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          Text('يظهر بها التطبيق كله.',
              style: TextStyle(color: Colors.grey.shade500)),
          const SizedBox(height: 32),
          _optionTile('العربية', lang == 'ar', () => setState(() => lang = 'ar')),
          _optionTile('English', lang == 'en', () => setState(() => lang = 'en')),
        ],
      ),
    );
  }

  Widget _optionTile(String title, bool selected, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 6),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 18),
        decoration: BoxDecoration(
          color: Colors.white10,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          children: [
            Text(title,
                style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            const Spacer(),
            selected
                ? const Icon(Icons.check_circle, color: Color(0xFF2EE6A8), size: 30)
                : const Icon(Icons.radio_button_off,
                    color: Colors.white24, size: 30),
          ],
        ),
      ),
    );
  }
}

// ---------- Step 2: Widget style ----------
class _WidgetStyleStep extends StatefulWidget {
  const _WidgetStyleStep();
  @override
  State<_WidgetStyleStep> createState() => _WidgetStyleStepState();
}

class _WidgetStyleStepState extends State<_WidgetStyleStep> {
  bool grid = true;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(24),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('اختر شكل الويدجتس',
              style: TextStyle(fontSize: 26, fontWeight: FontWeight.bold)),
          const SizedBox(height: 24),
          _tile(
              'ويدجتس',
              'كل ويدجتس في بطاقته.',
              grid,
              Column(
                children: [
                  Row(children: [
                    Expanded(child: _miniCard('الطقس', 'التاريخ والمعلومات')),
                    const SizedBox(width: 8),
                    Expanded(child: _miniCard('ساعات رقمية', 'الساعات')),
                  ]),
                ],
              ), () => setState(() => grid = true)),
          _tile('تصنيفات', 'بطاقة لكل تصنيف.', !grid,
              Column(children: [
                _listRow('ساعات رقمية', '23'),
                _listRow('الطقس', '29'),
              ]), () => setState(() => grid = false)),
        ],
      ),
    );
  }

  Widget _tile(String title, String sub, bool selected, Widget preview, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 8),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
            color: Colors.white10, borderRadius: BorderRadius.circular(20)),
        child: Column(
          children: [
            Row(children: [
              Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                Text(title,
                    style: const TextStyle(
                        fontSize: 20, fontWeight: FontWeight.bold)),
                Text(sub, style: TextStyle(color: Colors.grey.shade500)),
              ]),
              const Spacer(),
              Icon(selected ? Icons.check_circle : Icons.radio_button_off,
                  color: selected ? const Color(0xFF2EE6A8) : Colors.white24, size: 28),
            ]),
            const SizedBox(height: 12),
            preview,
          ],
        ),
      ),
    );
  }

  Widget _miniCard(String t, String s) => Container(
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
            color: Colors.black26, borderRadius: BorderRadius.circular(12)),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Text(t,
              style:
                  const TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
          Text(s, style: const TextStyle(fontSize: 10, color: Colors.grey)),
        ]),
      );

  Widget _listRow(String t, String n) => Container(
        margin: const EdgeInsets.symmetric(vertical: 4),
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
        decoration: BoxDecoration(
            color: Colors.black26, borderRadius: BorderRadius.circular(12)),
        child: Row(children: [
          Text(t, style: const TextStyle(fontWeight: FontWeight.bold)),
          const Spacer(),
          Text(n, style: const TextStyle(color: Colors.grey)),
        ]),
      );
}

// ---------- Step 3: Category ----------
class _CategoryStep extends StatefulWidget {
  const _CategoryStep();
  @override
  State<_CategoryStep> createState() => _CategoryStepState();
}

class _CategoryStepState extends State<_CategoryStep> {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(height: 12),
          const Text('اختر التصنيفات التي تحبها',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          Text(
            'يستخدمها التطبيق ليعرف ذوقك ويعرض لك الويدجتس المناسبة لك. اضغط على التصنيف لإضافته، واضغط عليه مرة أخرى لإزالته.',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.grey.shade500, height: 1.5),
          ),
          const SizedBox(height: 24),
          Container(
            width: double.infinity,
            height: 100,
            alignment: Alignment.center,
            decoration: BoxDecoration(
                color: Colors.white10,
                borderRadius: BorderRadius.circular(20)),
            child: Text('اختر تصنيفاً لترى شكله هنا.',
                style: TextStyle(color: Colors.grey.shade500)),
          ),
          const SizedBox(height: 24),
          Align(
              alignment: Alignment.centerRight,
              child: Text('معاينة',
                  style: TextStyle(color: Colors.grey.shade500))),
          const SizedBox(height: 12),
          Expanded(
            child: GridView.count(
              crossAxisCount: 3,
              mainAxisSpacing: 12,
              crossAxisSpacing: 12,
              children: const [
                _CatItem(Icons.watch_later_outlined, 'أوجه الساعات'),
                _CatItem(Icons.view_agenda_outlined, 'ساعات طويلة'),
                _CatItem(Icons.schedule, 'ساعات رقمية'),
                _CatItem(Icons.notifications_none, 'المهام'),
                _CatItem(Icons.mosque_outlined, 'مواقيت'),
                _CatItem(Icons.circle_outlined, 'عام'),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _CatItem extends StatelessWidget {
  final IconData icon;
  final String label;
  const _CatItem(this.icon, this.label);

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          color: Colors.white10, borderRadius: BorderRadius.circular(20)),
      child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
        Icon(icon, color: Colors.white70, size: 32),
        const SizedBox(height: 8),
        Text(label, style: const TextStyle(fontWeight: FontWeight.bold)),
      ]),
    );
  }
}

// ---------- Step 4: Theme ----------
class _ThemeStep extends StatelessWidget {
  const _ThemeStep();

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(24),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            width: 130,
            height: 190,
            decoration: BoxDecoration(
              color: Colors.black,
              borderRadius: BorderRadius.circular(30),
              border: Border.all(color: Colors.white12),
            ),
            child: Column(
              children: [
                const SizedBox(height: 18),
                Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                  Container(width: 60, height: 8,
                      decoration: BoxDecoration(color: Colors.white24,
                          borderRadius: BorderRadius.circular(4))),
                ]),
                const SizedBox(height: 16),
                Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
                  _dot(Colors.green), _dot(Colors.blue), _dot(Colors.orange),
                ]),
                const SizedBox(height: 14),
                Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
                  _dot(Colors.purple), _dot(Colors.red), _dot(Colors.teal),
                ]),
              ],
            ),
          ),
          const SizedBox(height: 28),
          const Text('اختر المظهر',
              style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          Text('داكن أو فاتح، للتطبيق كله.',
              style: TextStyle(color: Colors.grey.shade500)),
          const SizedBox(height: 32),
          _themeTile(context, 'داكن', 'مريح للعين في الليل.', true),
          _themeTile(context, 'فاتح', 'ساطع وواضح في النهار.', false),
        ],
      ),
    );
  }

  Widget _dot(Color c) => Container(
      width: 34, height: 34,
      decoration: BoxDecoration(color: c, shape: BoxShape.circle));

  Widget _themeTile(BuildContext context, String title, String sub, bool dark) {
    final selected = appState.darkTheme == dark;
    return GestureDetector(
      onTap: () => appState.toggleTheme(dark),
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 6),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 18),
        decoration: BoxDecoration(
            color: Colors.white10, borderRadius: BorderRadius.circular(16)),
        child: Row(children: [
          Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Text(title,
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
            Text(sub, style: TextStyle(color: Colors.grey.shade500)),
          ]),
          const Spacer(),
          Icon(selected ? Icons.check_circle : Icons.radio_button_off,
              color: selected ? const Color(0xFF2EE6A8) : Colors.white24, size: 30),
        ]),
      ),
    );
  }
}