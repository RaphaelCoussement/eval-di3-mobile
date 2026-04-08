# Rick and Morty Locations - Kotlin Multiplatform

Cette application Kotlin Multiplatform (KMP) cible **Android** et **Desktop (JVM)**.
Elle a été développée en respectant strictement les principes de la **Clean Architecture** et du **MVI (Model-View-Intent)**.

## Intention du projet

L'application permet d'explorer les lieux (Locations) de l'univers de Rick and Morty en exploitant l'API officielle.
Le projet met un point d'honneur à séparer les responsabilités, découpler la logique métier de l'interface, et exploiter intelligemment les spécificités des plateformes via `expect/actual`.

---

## Architecture & Structure du projet

Pour garantir une modularité exemplaire tout en maintenant la stabilité du build (règle absolue de l'évaluation), le projet est architecturé par **Packages (Package by Layer)** au sein du module partagé `commonMain`.

* **`domain/` :** Totalement indépendant. Contient les modèles de données (`Location`) avec leurs informations dérivées (ex: `residentCount`) et les contrats (`LocationRepository`). Aucune dépendance technique n'y est autorisée.
* **`data/` :** Gère la stratégie de données. Sépare clairement le réseau (`RemoteDataSource` via Ktor) du stockage local (`LocalDataSource` via un cache en mémoire).
* **`presentation/` :** Construit autour de Jetpack Compose. Les composants sont conçus comme des briques LEGO (séparation *Stateful / Stateless*).
* **`di/` :** Configuration de **Koin** pour l'assemblage de l'application et le découplage des couches.
* **`crossnative/` :** Contient le code nécessitant des implémentations spécifiques aux plateformes (ex: gestion du son).

---

## Choix Techniques & Arbitrages (Data Flow & MVI)

### 1. Le mécanisme de Fetch (Couche Data)
Le `LocationRepositoryImpl` joue le rôle de chef d'orchestre. Il implémente un mécanisme de **fetch à deux sources** :
1.  Il interroge d'abord le cache local (`LocalDataSource`).
2.  Si la donnée est absente, il interroge l'API (`RemoteDataSource` via Ktor).
3.  Il sauvegarde la donnée distante en local pour les futurs appels, avant de la mapper en modèle `Domain` pur.

### 2. Le pattern UDF / MVI (Couche Presentation)
L'UI respecte un flux de données unidirectionnel (Unidirectional Data Flow) :
* **UiState :** Représente l'état immuable de l'écran (`Loading`, `Success`, `Error`).
* **UiAction :** Représente les intentions de l'utilisateur (`OnLocationClicked`, `OnRetryClicked`).
* **ViewModel :** Seul autorisé à modifier l'état interne. Il expose un `StateFlow` en lecture seule à la vue.

---

## Spécificités Cross-Native

Le projet ne se contente pas de partager du code, il s'adapte à la plateforme cible :

* **Adaptation UI (Responsive Design) :** L'application utilise `BoxWithConstraints` dans son point d'entrée.
  * Sur mobile (largeur < 800dp), la navigation se fait écran par écran via un `Navigator` centralisé.
  * Sur Desktop (largeur ≥ 800dp), l'application bascule automatiquement en affichage **Master-Detail** (liste à gauche, détail de la location à droite sur le même écran).
* **Gestion du Son (`expect/actual`) :** Un `SoundManager` est défini dans le code partagé pour jouer un son lors du clic sur une location.
  * L'implémentation Android (`actual`) utilise une **fonction d'extension de Context** (`Context.playNotificationSound()`).
  * L'implémentation Desktop (`actual`) utilise les outils natifs de la JVM (`Toolkit.getDefaultToolkit().beep()`).

---

## Comment lancer le projet

Pour construire et lancer l'application en développement, utilisez les configurations d'exécution de votre IDE (Android Studio / IntelliJ) ou lancez ces commandes depuis le terminal :

### 📱 Android Application
* **macOS / Linux :** `./gradlew :composeApp:assembleDebug`
* **Windows :** `.\gradlew.bat :composeApp:assembleDebug`

### 💻 Desktop (JVM) Application
* **macOS / Linux :** `./gradlew :composeApp:run`
* **Windows :** `.\gradlew.bat :composeApp:run`