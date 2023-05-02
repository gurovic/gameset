CXX = g++
CXXFLAGS = -std=c++20 -Wall -g #-O3 -Wextra -Werror -pedantic -Wno-unused-result -Wno-unused-function -Wno-unused-variable -Wno-unused-but-set-variable -Wno-sign-compare -Wno-maybe-uninitialized -Wno-unknown-pragmas -Wno-psabi -Wno-attributes -Wno-unknown-warning-option -Wno-ignored-attributes

SRC_DIR = example_game

VISUALIZER = visualizer
VISUALIZER_DEPS = interactor_lib/EasyBMP.hpp

TARGETS = interactor negamax_depth_limited mtdf_depth_limited
TARGETS_DEPS = interactor_lib/interactor_lib.h

PIPES := $(foreach TARGET,$(filter-out interactor,$(TARGETS)),in_$(TARGET).pipe out_$(TARGET).pipe)

all: $(PIPES) $(TARGETS) $(VISUALIZER)

game: $(PIPES) $(TARGETS)

$(TARGETS): $(patsubst %,$(SRC_DIR)/%.cpp,$(TARGETS)) $(TARGETS_DEPS)
	$(CXX) $(SRC_DIR)/$@.cpp -o $@ $(CXXFLAGS)

$(VISUALIZER): $(patsubst %,$(SRC_DIR)/%.cpp,$(VISUALIZER)) $(VISUALIZER_DEPS)
	$(CXX) $(SRC_DIR)/$@.cpp -o $@ $(CXXFLAGS)

$(PIPES):
	if [ ! -p $@ ]; then mkfifo -m a=rw $@; fi;

.PHONY: run-game
run-game: $(TARGETS)
	$(foreach TARGET,$(filter-out interactor,$(TARGETS)),./$(TARGET) < in_$(TARGET).pipe > out_$(TARGET).pipe&)
	./$< $(foreach TARGET,$(filter-out interactor,$(TARGETS)),in_$(TARGET).pipe:out_$(TARGET).pipe)

.PHONY: run-visualizer
run-visualizer: $(VISUALIZER)
	./$(VISUALIZER) match.log images


.PHONY: docker
docker:
	docker build . -f game.dockerfile -t "game:game"
	docker run game:game

.PHONY: clean
clean:
	-rm $(TARGETS)
	-rm $(VISUALIZER)
	-rm $(PIPES)
	-rm -rf *.dSYM
