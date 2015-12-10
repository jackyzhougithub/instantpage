LOCAL_PATH:= $(call my-dir)

#RGBA->ARGB(JPEG)
include $(CLEAR_VARS)

LOCAL_CFLAGS := -std=c++11
LOCAL_NDK_STL_VARIANT := c++_static
LOCAL_LDFLAGS := -llog -ldl
LOCAL_MODULE := libjin_agrbutil
LOCAL_SRC_FILES := rgbautil.cpp rgbautilnative.cpp

LOCAL_C_INCLUDES += external/jpeg
LOCAL_CFLAGS    += -ffast-math -O3 -funroll-loops
LOCAL_ARM_MODE := arm

include $(BUILD_SHARED_LIBRARY)