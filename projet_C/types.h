#ifndef TYPES_H
#define TYPES_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/* Dimensions de la fenêtre */
#define WINDOW_WIDTH 1200
#define WINDOW_HEIGHT 1000

/* On réduit la taille max pour permettre l'affichage côte à côte */
#define MAX_IMG_WIDTH 550  
#define MAX_IMG_HEIGHT 550

/* --- Structure du Taquin (Modèle) --- */
typedef struct {
    int **grid;
    int row;
    int col;
    int empty_cell[2];
} Taquin;

/* --- Contexte Global du Jeu --- */
typedef struct {
    int run;
    char current_menu[50];
    
    int nb_rows;
    int nb_cols;
    
    int current_img_index;
    int total_images;
    
    /* Dimensions de l'image chargée */
    int img_w;
    int img_h;
    
    /* Position de la grille (Partie Gauche) */
    int grid_x;
    int grid_y;

    Taquin taquin; 
    int redraw;
    int win;
} GameContext;

/* --- UI Structures --- */
typedef void (*ActionFunc)(GameContext *ctx);

typedef struct {
    int x, y, xx, yy, w, h;
    char name[100];
} Image;

typedef struct {
    int x, y, w, h, text_size;
    char menu[20];
    char label[20];
    int key_code;      
    ActionFunc action;
} Button;

typedef struct {
    int elements, capacity;
    Button* table;
    int img_count, img_capacity;
    Image* images;
} Menu;

extern char *LISTE_IMAGES[];

#endif