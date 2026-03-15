# Débogage SaveAt — identifier un crash

## Voir les logs sur le téléphone (avec Android Studio)

1. **Connecte ton Samsung** en USB (mode Débogage USB activé).
2. Ouvre **Android Studio** → onglet **Logcat** (en bas).
3. Dans le filtre, tape : **`SaveAtDebug`**
4. Lance l’app sur le téléphone (icône Play ou depuis le téléphone).
5. Quand l’app se ferme, regarde les **dernières lignes** dans Logcat :
   - Les messages `[1/1]`, `[2/2]`, `[3/3]`, etc. indiquent jusqu’où le démarrage est allé.
   - En cas de crash, tu verras **`========== CRASH SaveAt ==========`** suivi de la **stack trace** (fichier + numéro de ligne).

## Ordre normal des logs au démarrage

- `[1/1] Application.onCreate() démarré`
- `[1/1] Application.onCreate() terminé`
- `[2/2] MainActivity.onCreate() démarré`
- `[2/2] MainActivity.onCreate() setContentView OK`
- `[3/3] SplashFragment.onViewCreated()`
- `[3/3] Splash Handler exécuté (800ms)`
- Puis soit `[3/3] Navigation -> onboardingFragment` soit `-> loginFragment`
- Si onboarding : `[4/4] OnboardingFragment.onViewCreated()` puis au clic `[4/4] Bouton Commencer cliqué` et `[4/4] Navigation -> loginFragment`
- Si login : `[5/5] LoginFragment.onCreateView()` puis `[5/5] LoginFragment.onViewCreated()`

**Le dernier message affiché avant le crash** indique l’endroit du problème. Envoie ce message + la stack trace (bloc rouge) pour qu’on corrige.
