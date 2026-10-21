<div align="center">

# ShapeCraft

**Clone en Java du jeu de logistique Shapez.io : extraire, transformer et assembler des formes et des couleurs**

![Status](https://img.shields.io/badge/status-termin%C3%A9-brightgreen)
![Projet](https://img.shields.io/badge/projet-LIFPOO%202025--2026-blue)
![Dernière mise à jour](https://img.shields.io/github/last-commit/farahbouh/Shapez.io?label=derni%C3%A8re%20mise%20%C3%A0%20jour)

![Java](https://img.shields.io/badge/Java-Swing-ED8B00?logo=openjdk&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ%20IDEA-IDE-000000?logo=intellijidea&logoColor=white)
![Architecture](https://img.shields.io/badge/architecture-Mod%C3%A8le%20%2F%20Vue-lightgrey)

[Installation](#mise-en-place) · [Machines](#machines-impl%C3%A9ment%C3%A9es) · [Signaler un bug](../../issues)

<img src="images/demo.png" width="700" alt="Aperçu de ShapeCraft">
<img src="images/demo2.png" width="700" alt="Aperçu de ShapeCraft">
</div>

---

## Le projet

ShapeCraft reprend le principe de [Shapez.io](https://github.com/tobspr-games/shapez.io) : on place des machines sur une grille et on les relie par des tapis roulants pour fabriquer les formes demandées par le Hub. Les objectifs se corsent de niveau en niveau.

Projet universitaire réalisé en binôme dans le cadre de l'UE LIFPOO (2025-2026).

---

## Stack technique

| | |
|---|---|
| **Langage** | Java |
| **Interface** | Swing (grille de 16 × 16 cases, barre de machines en bas de fenêtre) |
| **Architecture** | Modèle / Vue-Contrôleur, avec le patron Observateur pour rafraîchir la vue |
| **Concurrence** | Chaque machine tourne dans son propre thread, selon un schéma maître / esclave (`Machine` et ses `Worker`) |
| **Outils** | IntelliJ IDEA, GitLab CI/CD |

__*Pourquoi cette architecture ?*__

Séparer le modèle (plateau, machines, items) de la vue permet de faire évoluer l'interface sans toucher à la logique du jeu. Les machines travaillent en parallèle, comme dans le jeu original : chacune traite ses items à son rythme et les passe à la suivante.

---

## Fonctionnalités

- Placer et orienter des machines sur la grille (clic, glisser pour poser une ligne de tapis, touche `R` pour tourner)
- Des gisements de formes et de couleurs exploités par des mines
- Un Hub qui valide des objectifs, avec 4 niveaux de difficulté croissante
- Formes composées de quarts de cercle, que l'on peut découper, faire pivoter et colorer

### Machines implémentées

| Machine | Fonction |
|:---|:---|
| Mine | Extraction de formes / couleurs |
| Tapis roulant | Transport d'items |
| Rotater | Rotation horaire / antihoraire |
| Découpeuse | Simple (×2) ou Quad (×4) |
| Balancer | Croisement de flux |
| Mixer | Mélange de couleurs |
| Painter | Coloration de formes |
| Hub | Validation des objectifs |
| Poubelle | Destruction d'items |

---

## Mise en place

Il vous faut un JDK récent (le projet est configuré avec OpenJDK 26 dans IntelliJ).

```bash
git clone https://github.com/farahbouh/Shapez.io.git
```

**Avec IntelliJ IDEA :**

1. Ouvrez le dossier `ShapeCraft/ShapeCraft`.
2. Ouvrez la classe `src/Main.java`.
3. Cliquez sur **Run** (ou `Shift + F10`) : le jeu s'ouvre dans une nouvelle fenêtre.

**En ligne de commande (Linux / macOS) :**

```bash
cd Shapez.io/ShapeCraft/ShapeCraft
javac -d out $(find src -name '*.java')
java -cp out Main
```

Lancez toujours le jeu depuis le dossier `ShapeCraft/ShapeCraft` : les sprites sont chargés depuis le chemin relatif `./data/sprites`.

<details>
<summary>Structure du projet</summary>

```bash
ShapeCraft/ShapeCraft
├── data
│   └── sprites            # images des machines et des items
└── src
    ├── Main.java
    ├── modele
    │   ├── item           # formes et couleurs
    │   ├── jeu            # boucle de jeu
    │   └── plateau        # grille, machines, niveaux, Hub
    └── vuecontroleur      # interface Swing
```

</details>

<details>
<summary>Un problème au lancement ?</summary>

- **Les images n'apparaissent pas** : lancez le jeu depuis le dossier `ShapeCraft/ShapeCraft`, et non depuis la racine du dépôt.
- **Version de Java** : si IntelliJ signale un JDK introuvable, choisissez-en un autre dans *File -> Project Structure -> SDK*.

</details>

---

## Ce que ce projet m'a appris

- Appliquer l'architecture Modèle / Vue-Contrôleur et le patron Observateur sur un vrai projet
- Faire travailler plusieurs machines en parallèle avec des threads
- Modéliser des objets métier en programmation orientée objet (machines, items, plateau, niveaux)
- Travailler à deux sur un même code avec Git et GitLab

---

## Crédits

Projet inspiré de [Shapez.io](https://github.com/tobspr-games/shapez.io) de tobspr Games, dont sont issus les sprites.

---

<div align="center">

Réalisé par [Farah](https://github.com/farahbouh) et [Stan](https://github.com/Stan-dev69)

Contact : [LinkedIn](https://www.linkedin.com/in/farahbouh) · [Email](mailto:farah.bouhnaida@gmail.com)

</div>