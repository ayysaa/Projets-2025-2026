#include "ui.h"
#include "taquin.h"
#include <graph.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/* Utilitaires généraux pour gérer les images et les boutons */

Image image_create(int x, int y, int xx, int yy, int w, int h, char *file) {
    Image img;
    img.x = x; img.y = y; img.xx = xx; img.yy = yy; img.w = w; img.h = h;
    strcpy(img.name, file);
    return img;
}

Menu menu_create(void) {
    Menu m;
    m.elements = 0; m.capacity = 2;
    m.table = (Button*)malloc(sizeof(Button) * m.capacity);

    m.img_count = 0; m.img_capacity = 2;
    m.images = (Image*)malloc(sizeof(Image) * m.img_capacity);

    return m;
}

void menu_add_button(Menu *m, Button b) {
    if (m->elements >= m->capacity) {
        m->capacity += 2;
        m->table = (Button*)realloc(m->table, sizeof(Button) * m->capacity);
    }
    m->table[m->elements++] = b;
}

void menu_add_image(Menu *m, Image i) {
    if (m->img_count >= m->img_capacity) {
        m->img_capacity *= 2;
        m->images = (Image*)realloc(m->images, sizeof(Image) * m->img_capacity);
    }
    m->images[m->img_count++] = i;
}

void menu_free(Menu *m) {
    free(m->table);
    free(m->images);
}

/* Ajuste l'image principale si elle dépasse la taille maximale autorisée */
void menu_update_image(Menu *m, char *filename, GameContext *ctx) {
    int final_w = 0, final_h = 0;
    int px, py;
    int rw, rh, ratio;
    Image img;

    if (!get_jpeg_size(filename, &final_w, &final_h)) {
        printf("Erreur: Impossible de lire %s\n", filename);
        final_w = 400;
        final_h = 400;
    }

    /* Redimensionnement proportionnel si trop grande */
    if (final_w > MAX_IMG_WIDTH || final_h > MAX_IMG_HEIGHT) {
        rw = (float)MAX_IMG_WIDTH / final_w;
        rh = (float)MAX_IMG_HEIGHT / final_h;
        ratio = (rw < rh) ? rw : rh;
        final_w *= ratio;
        final_h *= ratio;
    }

    ctx->img_w = final_w;
    ctx->img_h = final_h;

    /* Centre l'image */
    px = (WINDOW_WIDTH - final_w) / 2;
    py = (WINDOW_HEIGHT - final_h) / 2 - 50;

    /* Grille du taquin sur la gauche */
    ctx->grid_x = 300 - (final_w / 2);
    ctx->grid_y = py;

    img = image_create(px, py, 0, 0, final_w, final_h, filename);

    if (m->img_count > 0) {
        m->images[0] = img;
    } else {
        menu_add_image(m, img);
    }
}

/* --- Dessin des composants UI --- */

static void draw_button(Button *b, int mx, int my) {
    int t, w, h, px, py, best_t = 0, tw = 0, th = 0;

    /* Ombre */
    ChoisirCouleurDessin(CouleurParNom("gray"));
    RemplirRectangle(b->x + 5, b->y + 5, b->w, b->h);

    /* Couleur du bouton */
    ChoisirCouleurDessin(CouleurParComposante(220, 220, 220));
    RemplirRectangle(b->x, b->y, b->w, b->h);

    /* Contour noir */
    ChoisirCouleurDessin(CouleurParNom("black"));
    DessinerRectangle(b->x, b->y, b->w, b->h);

    /* Ajuste automatiquement la taille du texte */
    for (t = 2; t >= 0; t--) {
        w = TailleChaineEcran(b->label, t);
        h = TailleSupPolice(t) + TailleInfPolice(t);

        if (w < b->w - 10 && h < b->h - 10) {
            best_t = t;
            tw = w;
            th = h;
            break;
        }
    }

    px = b->x + (b->w - tw) / 2;
    py = b->y + (b->h - th) / 2 + TailleSupPolice(best_t);

    EcrireTexte(px, py, b->label, best_t);
}

/* Affiche la version finie de l'image taquin */
static void draw_preview(GameContext *ctx, char *filename) {
    int px = 900 - (ctx->img_w / 2);
    int py = ctx->grid_y;

    ChoisirCouleurDessin(CouleurParComposante(245, 245, 240));
    RemplirRectangle(px, py - 30, 200, 30);

    ChoisirCouleurDessin(CouleurParNom("black"));
    EcrireTexte(px, py - 15, "Objectif :", 2);

    ChargerImage(filename, px, py, 0, 0, ctx->img_w, ctx->img_h);
    DessinerRectangle(px, py, ctx->img_w, ctx->img_h);
}

/*Affiche la grille du taquin*/

static void draw_taquin_grid(GameContext *ctx, char *filename) {
    int i, j, val, cell_w, cell_h;
    int src_x, src_y, dest_x, dest_y;
    int txt_w, bx, by;

    cell_w = ctx->img_w / ctx->nb_cols;
    cell_h = ctx->img_h / ctx->nb_rows;

    ChoisirCouleurDessin(CouleurParComposante(245, 245, 240));
    RemplirRectangle(ctx->grid_x - 5, ctx->grid_y - 5, ctx->img_w + 10, ctx->img_h + 10);

    ChoisirCouleurDessin(CouleurParNom("black"));
    DessinerRectangle(ctx->grid_x - 2, ctx->grid_y - 2, ctx->img_w + 4, ctx->img_h + 4);

    for (i = 0; i < ctx->nb_rows; i++) {
        for (j = 0; j < ctx->nb_cols; j++) {
            val = ctx->taquin.grid[i][j];
            dest_x = ctx->grid_x + j * cell_w;
            dest_y = ctx->grid_y + i * cell_h;

            if (val == 0 && !ctx->win) {
                ChoisirCouleurDessin(CouleurParComposante(40, 40, 40));
                RemplirRectangle(dest_x, dest_y, cell_w, cell_h);
            } else {
                src_x = (val % ctx->nb_cols) * cell_w;
                src_y = (val / ctx->nb_cols) * cell_h;
                ChargerImage(filename, dest_x, dest_y, src_x, src_y, cell_w, cell_h);
                
                ChoisirCouleurDessin(CouleurParNom("white"));
                DessinerRectangle(dest_x, dest_y, cell_w, cell_h);
            }
        }
    }

    /* Si victoire */
    if (ctx->win) {
        txt_w = TailleChaineEcran("VICTOIRE !", 2);
        bx = (WINDOW_WIDTH - txt_w) / 2 - 20;
        by = ctx->grid_y + ctx->img_h + 30;
        ChoisirCouleurDessin(CouleurParNom("white"));
        RemplirRectangle(bx, by - 30, txt_w + 40, 50);
        ChoisirCouleurDessin(CouleurParNom("black"));
        DessinerRectangle(bx, by - 30, txt_w + 40, 50);
        ChoisirCouleurDessin(CouleurParNom("red"));
        EcrireTexte(bx + 20, by, "VICTOIRE !", 2);
    }
}

/* Affiche tout le menu en fonction du menu actif */
void menu_display(Menu *m, GameContext *ctx) {
    int i, k;
    char titre[50];
    int mx = _X, my = _Y;

    if (strcmp(ctx->current_menu, "game") == 0) {

        ChoisirCouleurDessin(CouleurParComposante(245, 245, 240));
        RemplirRectangle(50, 20, 300, 50);

        sprintf(titre, "Taquin %dx%d", ctx->nb_cols, ctx->nb_rows);
        ChoisirCouleurDessin(CouleurParNom("black"));
        EcrireTexte(50, 50, titre, 2);

        draw_taquin_grid(ctx, LISTE_IMAGES[ctx->current_img_index]);
        draw_preview(ctx, LISTE_IMAGES[ctx->current_img_index]);

    } else if (strcmp(ctx->current_menu, "help_menu") == 0) {

        ChoisirCouleurDessin(CouleurParNom("black"));
        EcrireTexte(450, 150, "AIDE & COMMANDES", 2);

        EcrireTexte(200, 300, "NAVIGATION GENERALE :", 2);
        EcrireTexte(250, 350, "- Souris : Cliquer sur les boutons", 1);
        EcrireTexte(250, 390, "- ECHAP : Retour / Quitter", 1);
        EcrireTexte(250, 430, "- ENTREE : Valider / Jouer", 1);

        EcrireTexte(200, 500, "PARAMETRAGE (Ecran Choix) :", 2);
        EcrireTexte(250, 550, "- Fleche GAUCHE / DROITE : Changer l'image", 1);
        EcrireTexte(250, 590, "- Fleche HAUT / BAS : LIGNES", 1);
        EcrireTexte(250, 630, "- PageUP / PageDOWN : COLONNES", 1);

        EcrireTexte(200, 700, "EN JEU (Taquin) :", 2);
        EcrireTexte(250, 750, "- Souris : Cliquer sur une case adjacente", 1);
        EcrireTexte(250, 790, "- Fleches CLAVIER : Deplacer la case vide", 1);

    } else {
        /* Ecrans de sélection */
        ChoisirCouleurDessin(CouleurParNom("black"));
        EcrireTexte(780, 80, "Appuyez sur h pour voir les commandes", 1);

        for (i = 0; i < m->img_count; i++) {
            Image *img = &m->images[i];

            ChoisirCouleurDessin(CouleurParNom("gray"));
            RemplirRectangle(img->x + 10, img->y + 10, img->w, img->h);

            ChargerImage(img->name, img->x, img->y, img->xx, img->yy, img->w, img->h);

            ChoisirCouleurDessin(CouleurParNom("black"));
            DessinerRectangle(img->x, img->y, img->w, img->h);

            if (strcmp(ctx->current_menu, "image_choose") == 0) {
                float sx = (float)img->w / ctx->nb_cols;
                float sy = (float)img->h / ctx->nb_rows;

                for (k = 1; k < ctx->nb_cols; k++){
                    DessinerSegment(img->x + (int)(k * sx), img->y,
                                    img->x + (int)(k * sx), img->y + img->h);
                }
                for (k = 1; k < ctx->nb_rows; k++){
                    DessinerSegment(img->x, img->y + (int)(k * sy),
                                    img->x + img->w, img->y + (int)(k * sy));
                }


                EcrireTexte(500, 850,
                            "Fleches HAUT/BAS: Lignes | PageUp/PageDOWN: Colonnes", 1);
            }
        }
    }

    /* Dessine les boutons */
    for (i = 0; i < m->elements; i++){
        draw_button(&(m->table[i]), mx, my);
    }
}

/* Met à jour uniquement une case (optimisation du redraw) */
void draw_single_cell(GameContext *ctx, int row, int col) {
    int val = ctx->taquin.grid[row][col];
    int cell_w = ctx->img_w / ctx->nb_cols;
    int cell_h = ctx->img_h / ctx->nb_rows;

    int dest_x = ctx->grid_x + col * cell_w;
    int dest_y = ctx->grid_y + row * cell_h;

    ChoisirCouleurDessin(CouleurParNom("white"));

    if (val == 0 && !ctx->win) {
        ChoisirCouleurDessin(CouleurParComposante(40, 40, 40));
        RemplirRectangle(dest_x, dest_y, cell_w, cell_h);
    } else {
        int src_x = (val % ctx->nb_cols) * cell_w;
        int src_y = (val / ctx->nb_cols) * cell_h;
        ChargerImage(LISTE_IMAGES[ctx->current_img_index],
                     dest_x, dest_y, src_x, src_y, cell_w, cell_h);

        DessinerRectangle(dest_x, dest_y, cell_w, cell_h);
    }
}
int menu_button_clicked(Button *b, int mx, int my) {
    return (mx >= b->x && mx <= b->x + b->w &&
            my >= b->y && my <= b->y + b->h);
}

void screen_clear(void) {
    ChoisirCouleurDessin(CouleurParComposante(245, 245, 240));
    RemplirRectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
}

/* Lecture des dimensions JPEG (algorithme adapté provenant d'internet)*/
int get_jpeg_size(const char* nom, int *w, int *h) {
    int marker, len;
    FILE *f = fopen(nom, "rb");
    if (!f) {
        return 0;
    }

    if (fgetc(f) != 0xFF || fgetc(f) != 0xD8) {
        fclose(f);
        return 0;
    }

    while (1) {
        marker = fgetc(f);
        while (marker != 0xFF) {
            marker = fgetc(f);
        }
        while (marker == 0xFF) {
            marker = fgetc(f);
        }

        if (marker == 0xC0 || marker == 0xC2) {
            fgetc(f); fgetc(f); fgetc(f);
            *h = (fgetc(f) << 8) + fgetc(f);
            *w = (fgetc(f) << 8) + fgetc(f);
            fclose(f);
            return 1;
        } else if (marker == 0xDA || marker == 0xD9) {
            fclose(f);
            return 0;
        } else {
            len = (fgetc(f) << 8) + fgetc(f);
            if (len < 2) {
                fclose(f);
                return 0;
            }
            fseek(f, len - 2, SEEK_CUR);
        }
    }
}