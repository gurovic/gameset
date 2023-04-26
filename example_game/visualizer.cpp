#include <iostream>
#include <cmath>
#include <vector>

#include "../interactor_lib/EasyBMP.hpp"

// elements count separated by space in the input sequence, i.e, `1 => 10 7` --> `1`, `=>`, `10`, `7` (eq 4)
#define ELEMENTS_COUNT 4
// max length of the input sequence, i.e, `-1 => 15 15` <- the length of this string is 11 (max)
#define INPUT_MAX_LENGTH (11 + 1)

static const uint8_t           FIELD_SIZE = 15;
static const uint16_t          HEIGHT     = 1024;
static const uint16_t          WIDTH      = 1024;
static const EasyBMP::RGBColor blackColor(0, 0, 0);
static const EasyBMP::RGBColor whiteColor(255, 255, 255);

// pos_x, pos_y - [0; FIELD_SIZE]
void drawX(EasyBMP::Image &img, const uint8_t pos_x, const uint8_t pos_y,
           const uint8_t size = 20, const uint8_t thickness = 7,
           const EasyBMP::RGBColor &color = whiteColor) {
    uint16_t x = WIDTH / FIELD_SIZE * (pos_x - .55);
    uint16_t y = WIDTH / FIELD_SIZE * (pos_y - .5);

    for (uint8_t i = 0; i < thickness; ++i) {
        img.DrawLine(x - size + i, y - size, x + size + i, y + size, color);
        img.DrawLine(x - size + i, y + size, x + size + i, y - size, color);
    }
}

// pos_x, pos_y - [0; FIELD_SIZE]
void drawO(EasyBMP::Image &img, const uint8_t pos_x, const uint8_t pos_y,
           const uint8_t r = 20, const uint8_t thickness = 7,
           const EasyBMP::RGBColor &color = whiteColor) {
    uint16_t x = WIDTH / FIELD_SIZE * (pos_x - .5);
    uint16_t y = WIDTH / FIELD_SIZE * (pos_y - .5);

    for (uint8_t i = 0; i < thickness; ++i)
        img.DrawCircle(x, y, r + i, color);
}

std::vector<char *> tokenize(char *s, const char *delim) {
    std::vector<char *> out;
    char                *saveptr;

    for (uint8_t i = 0; i < ELEMENTS_COUNT; ++i, s = nullptr) {
        char *token = strtok_r(s, delim, &saveptr);
        out.emplace_back(token);
    }

    return out;
}

int main(int argc, char **argv) {
    if (argc != 3) {
        std::cout << "Usage: visualizer <path to log> <path to images>" << std::endl;
        return 1;
    }

    static const char *sourceFilePath = argv[1];
    static const char *imagesDirPath  = argv[2];

    std::cout << "Using log file: " << sourceFilePath << std::endl;
    std::cout << "Using images directory: " << imagesDirPath << std::endl;

    if (!std::filesystem::exists(imagesDirPath)) {
        std::cout << "Creating images directory: " << imagesDirPath << std::endl;
        std::filesystem::create_directory(imagesDirPath);
    }

    EasyBMP::Image img(WIDTH, HEIGHT, "base_image.bmp", blackColor);

    // Draw bottom and right outer field lines
    img.DrawLine(0, HEIGHT - 1, WIDTH, HEIGHT - 1, whiteColor);
    img.DrawLine(WIDTH - 1, 0, WIDTH - 1, HEIGHT, whiteColor);

    // Draw the other field lines
    for (uint8_t i = 0; i < FIELD_SIZE; ++i) {
        int rowPos = std::round(i * (HEIGHT / FIELD_SIZE));
        img.DrawLine(0, rowPos, WIDTH, rowPos, whiteColor);

        int colPos = std::round(i * (WIDTH / FIELD_SIZE));
        img.DrawLine(colPos, 0, colPos, HEIGHT, whiteColor);
    }

    std::ifstream log(sourceFilePath, std::ios::in);

    static uint32_t counter = 0;

    while (!log.eof()) {
        std::string filename = std::string(imagesDirPath) + "/" + std::to_string(counter) + ".bmp";
        img.SetFileName(filename.c_str());
        std::cout << "Generating image: " << filename << std::endl;

        char *input;
        log.getline(input, INPUT_MAX_LENGTH);
        std::cout << input << std::endl;

        std::vector<char *> split = tokenize(input, " ");
        std::cout << split.size() << std::endl;

        if (split.size() != ELEMENTS_COUNT) {
            std::cout << "Invalid input: " << input << std::endl;
            continue;
        }
        *split[0] == '1'
        ? drawX(img, strtol(split[2], nullptr, 10), strtol(split[3], nullptr, 10))
        : drawO(img, strtol(split[2], nullptr, 10), strtol(split[3], nullptr, 10));

        img.Write();
        counter++;
    }

    log.close();

    return 0;
}
