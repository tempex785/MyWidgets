import 'package:flutter/material.dart';
import 'home_screen.dart';

class PermissionsScreen extends StatefulWidget {
  const PermissionsScreen({super.key});
  @override
  State<PermissionsScreen> createState() => _PermissionsScreenState();
}

class _PermissionsScreenState extends State<PermissionsScreen> {
  int active = 0;

  @override
  Widget build(BuildContext context) {
    final dark = Theme.of(context).brightness == Brightness.dark;
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 8),
              const Text('اجعل الويدجتس تعمل بالكامل',
                  style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
              const SizedBox(height: 8),
              Text(
                'اضغط «السماح» عند كل واحد. يستغرق ذلك ثوانيَ قليلة.',
                style: TextStyle(color: Colors.grey.shade500),
              ),
              const SizedBox(height: 16),
              Row(
                children: List.generate(
                    3,
                    (i) => Expanded(
                          child: Container(
                            height: 4,
                            margin: const EdgeInsets.symmetric(horizontal: 4),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(4),
                              color: i < active
                                  ? const Color(0xFFFF7A1A)
                                  : Colors.white12,
                            ),
                          ),
                        )),
              ),
              const SizedBox(height: 8),
              Center(
                child: Text('$active من 3 مفعّلة',
                    style: TextStyle(color: Colors.grey.shade500)),
              ),
              const SizedBox(height: 16),
              _permCard(
                Icons.location_on_outlined,
                'تفعيل الموقع',
                'لتكون مواقيت الصلاة والطقس مطابقة تماماً لمكانك.',
              ),
              _permCard(
                Icons.notifications_outlined,
                'الإشعارات',
                'حتى تصلك التذكيرات والعادات والأذان في وقتها، حتى والتطبيق مغلق.',
              ),
              _permCard(
                Icons.flash_on_outlined,
                'التشغيل في الخلفية',
                'اسمح لـ Wadidget بالعمل في الخلفية، حتى لا يوقفه توفير الطاقة وتبقى الويدجتس محدّثة.',
              ),
              const Spacer(),
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
                  onPressed: () => Navigator.of(context).pushReplacement(
                    MaterialPageRoute(builder: (_) => const HomeScreen()),
                  ),
                  child: const Text('متابعة',
                      style: TextStyle(
                          fontSize: 18, fontWeight: FontWeight.bold)),
                ),
              ),
              const SizedBox(height: 8),
              Center(
                child: Text('يمكنك تفعيلها لاحقاً من الملف الشخصي.',
                    style: TextStyle(color: Colors.grey.shade500)),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _permCard(IconData icon, String title, String desc) {
    final granted = _granted(title);
    return Container(
      margin: const EdgeInsets.symmetric(vertical: 8),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
          color: Colors.white10, borderRadius: BorderRadius.circular(20)),
      child: Row(children: [
        CircleAvatar(
            radius: 24,
            backgroundColor: Colors.white10,
            child: Icon(icon, color: Colors.white)),
        const SizedBox(width: 12),
        Expanded(
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Text(title,
                style: const TextStyle(
                    fontSize: 17, fontWeight: FontWeight.bold)),
            const SizedBox(height: 4),
            Text(desc,
                style: TextStyle(color: Colors.grey.shade500, height: 1.4)),
          ]),
        ),
        const SizedBox(width: 8),
        if (granted)
          const Padding(
            padding: EdgeInsets.symmetric(horizontal: 6),
            child: Icon(Icons.check_circle, color: Color(0xFF2EE6A8), size: 26),
          )
        else
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.white,
              foregroundColor: Colors.black,
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20)),
            ),
            onPressed: () => setState(() => active++),
            child: const Text('السماح',
                style: TextStyle(fontWeight: FontWeight.bold)),
          ),
      ]),
    );
  }

  bool _granted(String title) {
    const order = ['تفعيل الموقع', 'الإشعارات', 'التشغيل في الخلفية'];
    return order.indexOf(title) < active;
  }
}