#ifndef ACTIONS_H
#define ACTIONS_H

#include "types.h"

void action_play(GameContext *ctx);
void action_quit(GameContext *ctx);
void action_return(GameContext *ctx);
void action_start_game(GameContext *ctx);
void action_add_row(GameContext *ctx);
void action_dec_row(GameContext *ctx);
void action_add_col(GameContext *ctx);
void action_dec_col(GameContext *ctx);
void action_next_img(GameContext *ctx);
void action_prev_img(GameContext *ctx);
void action_help(GameContext *ctx);
void action_back_main(GameContext *ctx);

#endif