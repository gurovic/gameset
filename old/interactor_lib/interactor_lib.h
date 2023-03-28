#ifndef GAMESET_INTERACTOR_LIB_H
#define GAMESET_INTERACTOR_LIB_H

#include <vector>
#include <fstream>
#include <cstring>
#include <csignal>
#include <iostream>
#include <math.h>

namespace {
    bool error_write() {
        return errno == EPIPE;
    }
}

class Process {
    std::ofstream input;
    std::ifstream output;
    bool alive = true;

public:
    Process(char *input_filename, char *output_filename) : input(input_filename), output(output_filename) {}

    bool is_alive() const {
        return alive;
    }

    template<typename T>
    Process &operator<<(const T &object) {
        input << object;
        if (error_write()) {
            alive = false;
        }
        return *this;
    }

    Process &operator<<(std::basic_ostream<char, std::char_traits<char>> &manipulator(
            std::basic_ostream<char, std::char_traits<char>> &)) {
        input << manipulator;
        if (error_write()) {
            alive = false;
        }
        return *this;
    }

    template<typename T>
    Process &operator>>(T &object) {
        output >> object;
        if (output.fail()) {
            alive = false;
        }
        return *this;
    }

    Process &operator>>(std::basic_istream<char, std::char_traits<char>> &manipulator(
            std::basic_istream<char, std::char_traits<char>> &)) {
        output >> manipulator;
        if (output.fail()) {
            alive = false;
        }

        return *this;
    }
};


struct Interactor {
    std::vector <Process> processes;

    Interactor(int argc, char **argv, int starting_player) {
        std::signal(SIGPIPE, SIG_IGN);  // we use errno to determine I/O errors
        processes.reserve(argc - 1);
        for (int arg_i = 1; arg_i < argc; ++arg_i) {
            char *split = strchr(argv[arg_i], ':');
            if (!split) {
                throw std::runtime_error("Malformed process list entry; pass argv and argc to Interactor constructor");
            }
            *split = '\0';
            processes.emplace_back(argv[arg_i], split + 1);
            // define which player to make the first move
            processes[arg_i - 1] << pow(-1, arg_i + (starting_player == 1)) << std::endl;
        }
    }

    void finalize_ok(int winner) const {
        std::cout << winner << '\n';
        exit(0);
    }
};

#endif //GAMESET_INTERACTOR_LIB_H
