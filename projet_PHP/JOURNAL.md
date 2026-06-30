Séance 1 - 20/05/2026 :

Installation de CodeIgniter 3.
Préparation des fichiers de bases inclus dans CodeIgniter 3.
Configuration de /application/config/database.php et config.php
Récupération du fichier game.sql

Utilisation de l'IA pour cette séance :
Nous avons utiliser l'IA pour avoir une idée de comment structurer le début de cette SAE et comprendre comment s'organiser pour cette SAE.
L'IA nous avait proposé une première suggestion que nous n'avons pas suivi car on ne comprenait pas ce qu'elle nous proposait et qu'elle n'incluait pas CodeIgniter 3.
Après demande d'utiliser CodeIgniter 3 comme base nous avons utiliser ses directives et avons installer tout les fichiers nécéssaire. 

Séance 2 - 27/05/2026 :

Travail réalisé :
- Développement de game.php pour récupérer les jeux depuis la base MariaDB.
- Création du contrôleur jeux.php pour gérer l'affichage de la liste.
- Création de index.php

Points à reprendre à la séance suivante :
- Implémenter les jointures SQL dans le modèle pour récupérer et afficher les genres et catégories associés à chaque jeu.
- Ajouter la fonctionnalité de tri par année de sortie.

Usage de l'IA :
Nous avons utiliser l'IA pour savoir la structure de nos fichiers et dans quel sens écrire le code. 

Hors séance :

Travail réalisé : Système de modification (CRUD) : Intégration complète des fonctionnalités d'ajout, de modification et de suppression des fiches de jeux directement depuis l'interface.

-Navigation dynamique : Création des vues dédiées pour filtrer et afficher les jeux par catégorie ou par genre en cliquant simplement sur les liens.
-Moteur de recherche et tris : Ajout d'une barre de recherche textuelle sur la page principale. Le système de tri (par titre ou par année) a été adapté pour rester fonctionnel même lorsqu'une recherche est active.
-Gestion des prix : Correction de la gestion des tarifs. La base de données stockant les prix en centimes, nous avons intégré la conversion automatique (division par 100 à l'affichage et multiplication à l'enregistrement) pour que l'utilisateur ne voie que des euros.
-Refonte visuelle : Nettoyage complet du design. Tout le CSS a été centralisé dans le header pour obtenir une interface moderne, aérée et structurée sous forme de cartes, sans surcharger le code.

Difficultés rencontrées & Décisions prises : La principale difficulté a été de faire cohabiter proprement les paramètres de tri et de recherche dans les URL pour éviter qu'un tri n'annule la recherche en cours. Nous avons également choisi de mutualiser le formulaire d'ajout et de modification dans une seule et même vue pour garder un code propre et facile à maintenir. 

Usage de l'IA : L'IA a été utilisé pour nous guider et vérifié ce qu'on a codé.