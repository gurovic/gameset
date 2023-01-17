cmake_minimum_required(VERSION 3.0)
project(Gomoku_ai)

set(CMAKE_CXX_STANDARD 14)

add_executable (Gomoku_ai "negamax_depth_limited.cpp" "negamax_depth_limited.h")
