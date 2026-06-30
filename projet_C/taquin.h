#ifndef TAQUIN_H
#define TAQUIN_H

#include "types.h"

void taquin_alloc(Taquin *t, int row, int col);
void taquin_fill(Taquin *t);
void taquin_shuffle(Taquin *t, int amount);
int taquin_move(Taquin *t, int r, int c);
int taquin_check_win(Taquin *t);
void taquin_free(Taquin *t);
void taquin_print_debug(Taquin *t);

#endif