#include "interactor_lib.h"

int main(int argc, char **argv) {
  Interactor interactor(argc, argv);
  // code goes here
  interactor.finalize_ok(0);  // player 0 wins
}
