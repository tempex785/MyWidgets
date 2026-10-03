import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'screens/splash_screen.dart';

void main() => runApp(const WadidgetApp());

class AppState extends ChangeNotifier {
  bool darkTheme = true;
  bool gridMode = true;
  void toggleTheme(bool v) { darkTheme = v; notifyListeners(); }
  void setGrid(bool v) { gridMode = v; notifyListeners(); }
}

final appState = AppState();

class WadidgetApp extends StatelessWidget {
  const WadidgetApp({super.key});

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: appState,
      builder: (context, _) => MaterialApp(
        title: 'Wadidget',
        debugShowCheckedModeBanner: false,
        locale: const Locale('ar'),
        supportedLocales: const [Locale('ar'), Locale('en')],
        localizationsDelegates: const [
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        theme: ThemeData.light().copyWith(
          scaffoldBackgroundColor: const Color(0xFFF3F1F7),
          colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFFFF7A1A)),
        ),
        darkTheme: ThemeData.dark().copyWith(
          scaffoldBackgroundColor: const Color(0xFF0C0F14),
          colorScheme: ColorScheme.fromSeed(
              seedColor: const Color(0xFFFF7A1A), brightness: Brightness.dark),
        ),
        themeMode: appState.darkTheme ? ThemeMode.dark : ThemeMode.light,
        home: const SplashScreen(),
      ),
    );
  }
}