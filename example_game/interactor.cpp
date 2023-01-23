/* Player 0: negamax, player 1: mtdf */
#include "interactor_lib.h"
#include <algorithm>
#include <numeric>

constexpr int FIELD_SIZE = 15;
constexpr int NEED_TO_WIN = 5;
int field[FIELD_SIZE][FIELD_SIZE];  // 0 by default, 1 for X, -1 for O
int range[NEED_TO_WIN];  // [0; NEED_TO_WIN)

int winner() {
    // column, row
    for (int i = 0; i < FIELD_SIZE; ++i) {
        for (int j = 0; j <= FIELD_SIZE - NEED_TO_WIN; ++j) {
            if (field[i][j] && std::all_of(field[i] + j, field[i] + j + NEED_TO_WIN, [i, j](int v) { return v == field[i][j]; })) {
                std::cerr << "-1 => 1 " << i << ' ' << j << '\n';
                return field[i][j];
            }
            if (field[j][i] && std::all_of(range, range + NEED_TO_WIN, [i, j](int add) { return field[j][i] == field[j + add][i]; })) {
                std::cerr << "-1 => 2 " << i << ' ' << j << '\n';
                return field[j][i];
            }
        }
    }
    for (int i = 0; i <= FIELD_SIZE - NEED_TO_WIN; ++i) {
        for (int j = 0; j <= FIELD_SIZE - NEED_TO_WIN; ++j) {
            if (field[i][j] && std::all_of(range, range + NEED_TO_WIN, [i, j](int add) { return field[i][j] == field[i + add][j + add]; })) {
                std::cerr << "-1 => 3 " << i << ' ' << j << '\n';
                return field[i][j];
            }
        }
    }
    for (int i = NEED_TO_WIN - 1; i < FIELD_SIZE; ++i) {
        for (int j = 0; j <= FIELD_SIZE - NEED_TO_WIN; ++j) {
            if (field[i][j] && std::all_of(range, range + NEED_TO_WIN, [i, j](int add) { return field[i][j] == field[i - add][j + add]; })) {
                std::cerr << "-1 => 4 " << i << ' ' << j << '\n';
                return field[i][j];
            }
        }
    }
    return 0;
}

int player_sign[] = {-1, 1};

bool ok_coordinate(int x) {
    return 0 <= x && x < FIELD_SIZE;
}

int main(int argc, char ** argv) {
    Interactor interactor(argc, argv);
    std::iota(range, range + NEED_TO_WIN, 0);
    // first auto-step: O is placed to 7,7
    field[7][7] = 1;
    std::cerr << "1 => 7 7\n";
    for (int step = 0;; ++step) {
        int player = step % 2;
        int cx, cy;
        interactor.processes[player] >> cx >> cy;
        std::cerr << player << " => " << cx << ' ' << cy << '\n';
        if (!interactor.processes[player].is_alive() || field[cx][cy] || !ok_coordinate(cx) || !ok_coordinate(cy)) {
            interactor.processes[player] << "-1 -1" << std::endl;
            interactor.processes[player ^ 1] << "-1 -1" << std::endl;
            interactor.finalize_ok(player ^ 1);
        }
        field[cx][cy] = player_sign[player];
        interactor.processes[player ^ 1] << cx << ' ' << cy << std::endl;
        if (winner() == player_sign[player] || !interactor.processes[player ^ 1].is_alive()) {
            interactor.processes[player] << "-1 -1" << std::endl;
            interactor.processes[player ^ 1] << "-1 -1" << std::endl;
            interactor.finalize_ok(player);
        }
    }
}
