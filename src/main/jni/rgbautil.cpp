#include "rgbautil.h"
#include <memory.h>
#include <array>
#include <vector>
#include <cstring>
#include <cstdio>

#include <setjmp.h>

extern "C" {
#include "jpeglib.h"
}

using namespace std;
template <typename T>
void safeDelete(T& t) {
  if (t != nullptr) {
    delete t;
    t = nullptr;
  }
}

template <typename T>
void safeDeleteArray(T& t) {
  if (t != nullptr) {
    delete[] t;
    t = nullptr;
  }
}


