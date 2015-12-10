#pragma once

#include <memory>
#include <functional>

namespace rgbautil{
int compress(unsigned char* inBuf,unsigned char* outBuf,size_t outBufCapacity,std::function<void(size_t)> flush, int quality);
}
