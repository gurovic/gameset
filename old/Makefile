CXX = g++
CXXFLAGS = -std=c++20 -Wall -g

DEPS = interactor_lib/interactor_lib.h
SRC_DIR = example_game

TARGETS = interactor negamax_depth_limited mtdf_depth_limited
PIPES := $(foreach TARGET,$(filter-out interactor,$(TARGETS)),in_$(TARGET).pipe out_$(TARGET).pipe)

all: $(PIPES) $(TARGETS)

$(TARGETS): $(patsubst %,$(SRC_DIR)/%.cpp,$(TARGETS)) $(DEPS)
	$(CXX) $(SRC_DIR)/$@.cpp -o $@ $(CXXFLAGS)

$(PIPES):
	if [ ! -p $@ ]; then mkfifo $@; fi;

.PHONY: run
run: $(TARGETS)
	$(foreach TARGET,$(filter-out interactor,$(TARGETS)),./$(TARGET) < in_$(TARGET).pipe > out_$(TARGET).pipe&)
	./$< $(foreach TARGET,$(filter-out interactor,$(TARGETS)),in_$(TARGET).pipe:out_$(TARGET).pipe)

.PHONY: docker
docker:
	docker build . -f game.dockerfile -t "game:game"
	docker run game:game

.PHONY: clean
clean:
	-rm $(TARGETS)
	-rm $(PIPES)
	-rm -rf *.dSYM
