# Pomodoro Android (Samsung S23)

Cette configuration permet de générer un APK **release signé** installable sur un Samsung Galaxy S23.

## 1) Générer une clé de signature (une seule fois)

```bash
keytool -genkeypair -v \
  -keystore release-keystore.jks \
  -alias pomodoro \
  -keyalg RSA -keysize 2048 -validity 10000
```

## 2) Configurer les secrets localement

Créer `keystore.properties` à la racine du projet :

```properties
storeFile=../release-keystore.jks
storePassword=VOTRE_MOT_DE_PASSE
keyAlias=pomodoro
keyPassword=VOTRE_MOT_DE_PASSE
```

> Ne pas committer ce fichier.

## 3) Générer l'APK release

```bash
./gradlew assembleRelease
```

APK généré :

- `app/build/outputs/apk/release/app-release.apk`

## 4) Installer sur Samsung S23

### Option A — USB (recommandé)

1. Activer **Options développeur** puis **Débogage USB** sur le téléphone.
2. Brancher le S23.
3. Installer :

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

### Option B — téléchargement direct

1. Envoyer `app-release.apk` par Drive/email.
2. Sur S23, activer l'autorisation *Installer des apps inconnues* pour l'application utilisée (Fichiers, Chrome, etc.).
3. Ouvrir l'APK et installer.
