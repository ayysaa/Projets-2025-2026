#include "actions.h"
#include "taquin.h"
#include <stdio.h>

void action_play(GameContext *ctx) {
    strcpy(ctx->current_menu, "image_choose");
    ctx->redraw = 1;
}

void action_quit(GameContext *ctx) {
    ctx->run = 0;
}

void action_return(GameContext *ctx) {
    if (strcmp(ctx->current_menu, "game") == 0) {
        taquin_free(&ctx->taquin);
    }
    strcpy(ctx->current_menu, "start_menu");
    ctx->redraw = 1;
}

void action_start_game(GameContext *ctx) {
    int nb_cellules = ctx->nb_rows * ctx->nb_cols;
    
    int force_melange = nb_cellules * 10;

    printf("Lancement du Taquin %dx%d\n", ctx->nb_rows, ctx->nb_cols);
    printf("Mélange de (%d mouvements)\n", force_melange);
    
    taquin_alloc(&ctx->taquin, ctx->nb_rows, ctx->nb_cols);
    taquin_fill(&ctx->taquin);
    
    taquin_shuffle(&ctx->taquin, force_melange);
    
    ctx->win = 0;
    strcpy(ctx->current_menu, "game");
    ctx->redraw = 1;
}

void action_add_row(GameContext *ctx) {
    if (ctx->nb_rows < 8) { ctx->nb_rows++; ctx->redraw = 1; }
}

void action_dec_row(GameContext *ctx) {
    if (ctx->nb_rows > 3) { ctx->nb_rows--; ctx->redraw = 1; }
}

void action_add_col(GameContext *ctx) {
    if (ctx->nb_cols < 8) { ctx->nb_cols++; ctx->redraw = 1; }
}

void action_dec_col(GameContext *ctx) {
    if (ctx->nb_cols > 3) { ctx->nb_cols--; ctx->redraw = 1; }
}

void action_next_img(GameContext *ctx) {
    ctx->current_img_index++;
    if (ctx->current_img_index >= ctx->total_images) ctx->current_img_index = 0;
    ctx->redraw = 1;
}

void action_prev_img(GameContext *ctx) {
    ctx->current_img_index--;
    if (ctx->current_img_index < 0) ctx->current_img_index = ctx->total_images - 1;
    ctx->redraw = 1;
}

void action_help(GameContext *ctx) {
    strcpy(ctx->current_menu, "help_menu");
    ctx->redraw = 1;
}

void action_back_main(GameContext *ctx) {
    strcpy(ctx->current_menu, "start_menu");
    ctx->redraw = 1;
}