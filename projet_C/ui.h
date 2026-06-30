#ifndef UI_H
#define UI_H

#include "types.h"
#include <graph.h>

Menu menu_create(void);
void menu_add_button(Menu *m, Button b);
void menu_add_image(Menu *m, Image i);
void menu_free(Menu *m);

void menu_update_image(Menu *m, char *filename, GameContext *ctx);

void menu_display(Menu *m, GameContext *ctx);
int menu_button_clicked(Button *b, int mx, int my);

Image image_create(int x, int y, int xx, int yy, int w, int h, char *file);
void screen_clear(void);
int get_jpeg_size(const char* nom, int *w, int *h);
void draw_single_cell(GameContext *ctx, int row, int col);
#endif