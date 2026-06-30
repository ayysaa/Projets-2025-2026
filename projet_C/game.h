#ifndef GAME_H
#define GAME_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <graph.h>

#define WINDOW_WIDTH 1200
#define WINDOW_HEIGHT 1000
#define MAX_IMG_WIDTH 1000
#define MAX_IMG_HEIGHT 650

/* Forward declaration */
struct GameState;

/* Type pour les actions des boutons */
typedef void (*ActionFunc)(struct GameState *state);

/* --- STRUCTURES --- */

/* Contient tout l'état du programme (remplace les variables globales) */
typedef struct GameState {
    int nb_cols;
    int nb_rows;
    int current_img_index;
    int run;            /* 1 pour continuer, 0 pour quitter */
    char menu_name[50]; /* Menu actuel */
    char **liste_images;
} GameState;

typedef struct {
    int x, y, xx, yy, w, h;
    char name[100];
    int part;
} Image;

typedef struct {
    int x, y, w, h, text_size;
    char menu[20];
    char label[20];
    char key[20];
    ActionFunc action;
} Button;

typedef struct {
    int elements, capacity;
    Button* table;
    int img_count, img_capacity;
    Image* images;
} Menu;

#endif