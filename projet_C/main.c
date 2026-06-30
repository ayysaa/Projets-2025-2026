#include "types.h"
#include "ui.h"
#include "actions.h"
#include "taquin.h"
#include <graph.h>
#include <string.h> 
#include <stdlib.h> 

/* Liste des chemins vers les images disponibles pour le Taquin. */
char *LISTE_IMAGES[] = {
    "images/zelda.jpeg",
    "images/flash.jpeg",
    "images/nyx.jpeg",
    "images/chien.jpeg",
    "images/yo.jpeg"
};

/* Configuration de TOUS les boutons du jeu (position, taille, texte, action). */
Button BUTTONS_CONFIG[] = {
    /* MENU PRINCIPAL (start_menu) */
    {400, 350, 400, 100, 2, "start_menu", "JOUER", XK_Return, action_play},
    {400, 500, 400, 100, 2, "start_menu", "QUITTER", XK_Escape, action_quit},
    {1100, 50, 50, 50, 2, "start_menu", "i", XK_h, action_help}, /* Bouton d'aide (info) */

    /* MENU AIDE (help_menu) */
    {400, 850, 400, 80, 2, "help_menu", "RETOUR MENU", XK_Escape, action_back_main},

    /* CHOIX IMAGE (image_choose) - Navigation */
    {30, 30, 200, 50, 1, "image_choose", "RETOURNER", XK_Escape, action_return},
    {900, 900, 250, 60, 2, "image_choose", "LANCER !", XK_Return, action_start_game},
    
    /* CHOIX IMAGE - Modification de la grille (lignes/colonnes) */
    {350, 880, 50, 50, 2, "image_choose", "-", XK_Down, action_dec_row}, /* Moins de lignes */
    {410, 880, 50, 50, 2, "image_choose", "+", XK_Up, action_add_row}, /* Plus de lignes */
    {740, 880, 50, 50, 2, "image_choose", "-", XK_Page_Down, action_dec_col}, /* Moins de colonnes */
    {800, 880, 50, 50, 2, "image_choose", "+", XK_Page_Up, action_add_col}, /* Plus de colonnes */

    /* CHOIX IMAGE - Flèches pour changer d'image */
    {50, 450, 60, 100, 2, "image_choose", "<", XK_Left, action_prev_img},
    {1090, 450, 60, 100, 2, "image_choose", ">", XK_Right, action_next_img},

    /* JEU (game) - Bouton pour revenir au menu */
    {30, 900, 200, 60, 2, "game", "MENU", XK_Escape, action_return}
};

/* Distribue les boutons de la CONFIGURATION_BOUTONS dans la bonne structure Menu. */
void init_menus(Menu *start, Menu *choice, Menu *game, Menu *help) {
    int i, count = sizeof(BUTTONS_CONFIG) / sizeof(Button);
    for (i = 0; i < count; i++) {
        Button b = BUTTONS_CONFIG[i];
        if (strcmp(b.menu, "start_menu") == 0) {
            menu_add_button(start, b);
        }
        else if (strcmp(b.menu, "image_choose") == 0) {
            menu_add_button(choice, b);
        }
        else if (strcmp(b.menu, "game") == 0) {
            menu_add_button(game, b);
        }
        else if (strcmp(b.menu, "help_menu") == 0) {
            menu_add_button(help, b);
        }
    }
}

/* Gère un clic de souris pendant la partie de jeu. */
void handle_game_click(GameContext *ctx, int mx, int my) {
    int col, row, cell_w, cell_h;
    /* On garde en mémoire où était la case vide AVANT le clic. */
    int old_empty_r = ctx->taquin.empty_cell[0];
    int old_empty_c = ctx->taquin.empty_cell[1];

    if (ctx->win) {
        return; /* Ne rien faire si on a gagné. */
    }

    /* Vérifie si le clic est bien dans la zone de l'image du Taquin. */
    if (mx >= ctx->grid_x && mx <= ctx->grid_x + ctx->img_w &&
        my >= ctx->grid_y && my <= ctx->grid_y + ctx->img_h) {
        
        /* Calcule la taille d'une cellule et trouve la ligne/colonne cliquée. */
        cell_w = ctx->img_w / ctx->nb_cols;
        cell_h = ctx->img_h / ctx->nb_rows;
        col = (mx - ctx->grid_x) / cell_w;
        row = (my - ctx->grid_y) / cell_h;

        /* Tente de déplacer la pièce cliquée vers la case vide. */
        if (taquin_move(&ctx->taquin, row, col)) {
            draw_single_cell(ctx, old_empty_r, old_empty_c);
            draw_single_cell(ctx, row, col);
            
            /* Vérifie si la partie est terminée. */
            if (taquin_check_win(&ctx->taquin)) {
                ctx->win = 1;
                ctx->redraw = 1; /* On force le redessinage complet pour afficher le message de victoire. */
            }
        }
    }
}

/* Gère une pression de touche clavier (flèches) pendant la partie de jeu. */
void handle_game_key(GameContext *ctx, int key) {
    int r = ctx->taquin.empty_cell[0];
    int c = ctx->taquin.empty_cell[1];
    /* Coordonnées de la pièce qui va bouger vers le vide. */
    int target_r = r, target_c = c;
    int move_possible = 0;

    if (ctx->win) {
        return; /* Ne rien faire si on a gagné. */
    }

    /* Détermine quelle pièce l'utilisateur essaie de déplacer. */
    if (key == XK_Left && c > 0) {
        target_c = c - 1; 
        move_possible = 1; 
    } /* Déplace la pièce à gauche du vide. */
    else if (key == XK_Right && c < ctx->nb_cols - 1) { 
        target_c = c + 1; 
        move_possible = 1; 
    } /* Déplace la pièce à droite du vide. */
    else if (key == XK_Up && r > 0) { 
        target_r = r - 1; 
        move_possible = 1; 
    } /* Déplace la pièce au-dessus du vide. */
    else if (key == XK_Down && r < ctx->nb_rows - 1) { 
        target_r = r + 1; 
        move_possible = 1; 
    } /* Déplace la pièce en dessous du vide. */

    if (move_possible) {
        /* Effectue le mouvement (échange de position) dans la structure de données. */
        taquin_move(&ctx->taquin, target_r, target_c);
        
        /* Mise à jour graphique optimisée des deux cases. */
        /* r, c était l'ancienne case vide (maintenant remplie). */
        draw_single_cell(ctx, r, c);
        /* target_r, target_c est la nouvelle case vide. */
        draw_single_cell(ctx, target_r, target_c);

        /* Vérifie la victoire. */
        if (taquin_check_win(&ctx->taquin)) {
            ctx->win = 1;
            ctx->redraw = 1;
        }
    }
}

int main(void) {
    int x, y, i, k;
    int last_img_idx = -1;
    GameContext ctx;
    /* Les différents menus du jeu. */
    Menu m_start, m_choice, m_game, m_help; 
    Menu *m_actuel = NULL, *m_prev = NULL;

    /* Initialisation des variables de jeu par défaut. */
    ctx.run = 1;
    ctx.nb_rows = 3;
    ctx.nb_cols = 3;
    ctx.current_img_index = 0;
    ctx.total_images = sizeof(LISTE_IMAGES) / sizeof(char*);
    ctx.redraw = 1;
    ctx.win = 0;
    strcpy(ctx.current_menu, "start_menu"); /* Commence par le menu principal. */

    /* Initialisation de la librairie graphique. */
    InitialiserGraphique();
    CreerFenetre(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);

    /* Création des structures Menu. */
    m_start = menu_create();
    m_choice = menu_create();
    m_game = menu_create();
    m_help = menu_create();

    /* Remplissage des menus avec leurs boutons respectifs. */
    init_menus(&m_start, &m_choice, &m_game, &m_help);
    
    /* Charge la première image par défaut pour le menu de choix. */
    menu_update_image(&m_choice, LISTE_IMAGES[ctx.current_img_index], &ctx);
    
    last_img_idx = ctx.current_img_index;
    m_actuel = &m_start;

    /* BOUCLE PRINCIPALE du jeu. */
    while (ctx.run) {
        /* Met à jour le pointeur vers le menu actif selon le contexte. */
        if (strcmp(ctx.current_menu, "start_menu") == 0) {
            m_actuel = &m_start;
        }
        else if (strcmp(ctx.current_menu, "image_choose") == 0) {
            m_actuel = &m_choice;
        }
        else if (strcmp(ctx.current_menu, "game") == 0){
            m_actuel = &m_game;
        }
        else if (strcmp(ctx.current_menu, "help_menu") == 0) {
            m_actuel = &m_help;
        }

        /* Si l'utilisateur a changé d'image, on la recharge. */
        if (last_img_idx != ctx.current_img_index) {
            menu_update_image(&m_choice, LISTE_IMAGES[ctx.current_img_index], &ctx);
            last_img_idx = ctx.current_img_index;
            ctx.redraw = 1; /* Force le redessinage du menu de choix. */
        }

        /* Affichage: si le menu a changé ou si 'redraw' est actif, on efface et on redessine tout. */
        if (m_actuel != m_prev || ctx.redraw) {
            screen_clear(); /* Efface tout. */
            menu_display(m_actuel, &ctx); /* Dessine le contenu du menu actif. */
            m_prev = m_actuel;
            ctx.redraw = 0;
        }

        /* --- GESTION SOURIS (Clic) --- */
        if (SourisCliquee()) {
            x = _X; y = _Y;
            /* Vérifie si un bouton du menu actuel a été cliqué. */
            for (i = 0; i < m_actuel->elements; i++) {
                Button *b = &m_actuel->table[i];
                if (menu_button_clicked(b, x, y) && b->action) {
                    b->action(&ctx); /* Exécute l'action associée au bouton. */
                }
            }
            /* Gère les clics dans la grille si on est en jeu. */
            if (strcmp(ctx.current_menu, "game") == 0) {
                handle_game_click(&ctx, x, y);
            }
        }

        /* --- GESTION CLAVIER --- */
        if (ToucheEnAttente()){
            k = Touche();
            
            /* 1. Traite les raccourcis clavier associés aux boutons (Entrée, Échap, etc.). */
            for (i = 0; i < m_actuel->elements; i++) {
                Button *b = &m_actuel->table[i];
                if (b->key_code != 0 && b->key_code == k && b->action) {
                    b->action(&ctx);
                }
            }

            /* 2. Traite les touches de mouvement (Flèches) uniquement en jeu. */
            if (strcmp(ctx.current_menu, "game") == 0) {
                handle_game_key(&ctx, k);
            }
        }
    }

    /* Nettoyage et fin du programme. */
    FermerGraphique();
    /* Libère la mémoire des structures de menu. */
    menu_free(&m_start);
    menu_free(&m_choice);
    menu_free(&m_game);
    menu_free(&m_help);
    return EXIT_SUCCESS;
}