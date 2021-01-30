# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

# libpersonhealth.so
include $(CLEAR_VARS)
APP_ALLOW_MISSING_DEPS=true
LOCAL_LDLIBS    := -lm -llog -ljnigraphics
LOCAL_MODULE    := libgpuimage-library
LOCAL_C_INCLUDES := $(JNI_H_INCLUDE)
LOCAL_SRC_FILES := yuv-decoder.c \
                   bitmap_operation.cpp

LOCAL_PRELINK_MODULE := false
LOCAL_MODULE_TAGS := optional
#LOCAL_LDFLAGS := $(LOCAL_PATH)/libvideodecryptkey.a
LOCAL_SHARED_LIBRARIES := \
				liblog 
include $(BUILD_SHARED_LIBRARY)

#TARGET_CPU_API := armeabi armeabi-v7a  arm64-v8a
#APP_ABI := armeabi armeabi-v7a
