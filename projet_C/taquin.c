#include "taquin.h"
#include <time.h>
#include <stdlib.h>

void taquin_alloc(Taquin *t, int row, int col) {
    int i;
    t->row = row;
    t->col = col;

    t->grid = (int **) malloc(t->row * sizeof(int *));
    if (t->grid == NULL) {
        exit(1);
    }

    t->grid[0] = (int *) malloc(t->row * t->col * sizeof(int));
    if (t->grid[0] == NULL){
        exit(1);
    }

    for (i = 1; i < t->row; i++) {
        t->grid[i] = t->grid[0] + i * t->col;
    }
}

void taquin_fill(Taquin *t) {
    int i, j, count = 0;
    /* On remplit la grille : 0, 1, 2, 3... 
       Le 0 sera placé en (0,0) au début */
    for (i = 0; i < t->row; i++) {
        for (j = 0; j < t->col; j++) {
            t->grid[i][j] = count++;
        }
    }
    t->empty_cell[0] = 0; /* Ligne 0 */
    t->empty_cell[1] = 0; /* Col 0 */
}

static int is_legal(Taquin *t, int r, int c) {
    int er = t->empty_cell[0];
    int ec = t->empty_cell[1];

    if (r < 0 || r >= t->row || c < 0 || c >= t->col){
        return 0;
    }
    
    /* On ne peut pas bouger la case vide sur elle-même */
    if (r == er && c == ec){
        return 0;
    }

    /* Distance de Manhattan doit être de 1 (Haut, Bas, Gauche ou Droite) */
    if ((r == er && abs(c - ec) == 1) || (c == ec && abs(r - er) == 1)) {
        return 1;
    }

    return 0;
}

int taquin_move(Taquin *t, int r, int c) {
    int tmp;
    /* Vérifie si le mouvement est légal */
    if (!is_legal(t, r, c)){
        return 0;
    }

    /* Echange la case cible avec la case vide */
    tmp = t->grid[r][c];
    t->grid[r][c] = 0; /* Le 0 représente le vide */
    t->grid[t->empty_cell[0]][t->empty_cell[1]] = tmp;

    /* Met à jour les coordonnées du vide */
    t->empty_cell[0] = r;
    t->empty_cell[1] = c;
    return 1;
}

/* Vérifie que le mélange ne sort pas de la grille */
static int shuffle_legal(Taquin *t, int move, int x, int y) {
    if (y == 0 && move == 3){
        return 0; 
    } /* gauche */
    if (y == t->col - 1 && move == 1){
        return 0; 
    } /* droite */
    if (x == 0 && move == 0){
        return 0; 
    } /* haut */
    if (x == t->row - 1 && move == 2){
        return 0; 
    }/* bas */
    return 1;
}

void taquin_shuffle(Taquin *t, int amount) {
    int count = 0, tmp;
    int x, y, move, new_x, new_y;
    int er, ec;
    
    srand(time(NULL));
    x = t->empty_cell[0];
    y = t->empty_cell[1];

    /* 1. MÉLANGE ALÉATOIRE (Random Walk) */
    while (count < amount) {
        move = rand() % 4; /* 0:Haut, 1:Droite, 2:Bas, 3:Gauche */
        if (shuffle_legal(t, move, x, y)) {
            new_x = x; new_y = y;
            if (move == 0){
                new_x--;
            }
            else if (move == 1){
                new_y++;
            }
            else if (move == 2){
                new_x++;
            }
            else if (move == 3){
                new_y--;
            }

            /* Echange direct (plus rapide que taquin_move pour ce cas) */
            tmp = t->grid[new_x][new_y];
            t->grid[new_x][new_y] = t->grid[x][y]; /* t->grid[x][y] vaut 0 */
            t->grid[x][y] = tmp;

            x = new_x; y = new_y;
            t->empty_cell[0] = x;
            t->empty_cell[1] = y;
            count++;
        }
    }


    /* Tant que le vide n'est pas sur la ligne 0, on le monte */
    while (t->empty_cell[0] > 0) {
        er = t->empty_cell[0];
        ec = t->empty_cell[1];
        /* On échange avec la case du DESSUS (er - 1) */
        taquin_move(t, er - 1, ec);
    }

    /* Tant que le vide n'est pas sur la colonne 0, on le décale à gauche */
    while (t->empty_cell[1] > 0) {
        er = t->empty_cell[0];
        ec = t->empty_cell[1];
        /* On échange avec la case de GAUCHE (ec - 1) */
        taquin_move(t, er, ec - 1);
    }
}

int taquin_check_win(Taquin *t) {
    int i, j, count = 0;
    for (i = 0; i < t->row; i++){
        for (j = 0; j < t->col; j++){
            if (t->grid[i][j] != count){
                return 0;
            }
            count++;
        }
    }
    return 1;
}


void taquin_free(Taquin *t) {
    if (t->grid){
        free(t->grid[0]);
        free(t->grid);
    }
}

void taquin_print_debug(Taquin *t) {
    int i, j;
    for (i = 0; i < t->row; i++) {
        printf("[");
        for (j = 0; j < t->col; j++) {
            printf("%3d", t->grid[i][j]);
            if (j < t->col - 1) {
                printf(", ");
            }
        }
        printf("]\n");
    }
}