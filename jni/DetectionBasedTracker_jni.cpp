/*
 * Copyright (c) 2011-2012, opencv dev team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This class is derived and modified from work available at http://docs.opencv.org/trunk/modules/contrib/doc/facerec/facerec_tutorial.html.
 */
#include <DetectionBasedTracker_jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/contrib/detection_based_tracker.hpp>

#include <string>
#include <vector>

#include <android/log.h>

#define LOG_TAG "FaceDetection/DetectionBasedTracker"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

inline void vector_Rect_to_Mat(vector<Rect>& v_rect, Mat& mat)
{
    mat = Mat(v_rect, true);
}

JNIEXPORT jlong JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeCreateObject
(JNIEnv * jenv, jclass, jstring jFileName, jint faceSize)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeCreateObject enter");
    const char* jnamestr = jenv->GetStringUTFChars(jFileName, NULL);
    string stdFileName(jnamestr);
    jlong result = 0;

    try
    {
        DetectionBasedTracker::Parameters DetectorParams;
        if (faceSize > 0)
            DetectorParams.minObjectSize = faceSize;
        result = (jlong)new DetectionBasedTracker(stdFileName, DetectorParams);
    }
    catch(cv::Exception e)
    {
        LOGD("nativeCreateObject catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeCreateObject catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
        return 0;
    }

    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeCreateObject exit");
    return result;
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeDestroyObject
(JNIEnv * jenv, jclass, jlong thiz)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeDestroyObject enter");
    try
    {
        if(thiz != 0)
        {
            ((DetectionBasedTracker*)thiz)->stop();
            delete (DetectionBasedTracker*)thiz;
        }
    }
    catch(cv::Exception e)
    {
        LOGD("nativeestroyObject catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeDestroyObject catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeDestroyObject exit");
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeStart
(JNIEnv * jenv, jclass, jlong thiz)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeStart enter");
    try
    {
        ((DetectionBasedTracker*)thiz)->run();
    }
    catch(cv::Exception e)
    {
        LOGD("nativeStart catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeStart catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeStart exit");
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeStop
(JNIEnv * jenv, jclass, jlong thiz)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeStop enter");
    try
    {
        ((DetectionBasedTracker*)thiz)->stop();
    }
    catch(cv::Exception e)
    {
        LOGD("nativeStop catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeStop catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeStop exit");
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeSetFaceSize
(JNIEnv * jenv, jclass, jlong thiz, jint faceSize)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeSetFaceSize enter");
    try
    {
        if (faceSize > 0)
        {
            DetectionBasedTracker::Parameters DetectorParams = \
            ((DetectionBasedTracker*)thiz)->getParameters();
            DetectorParams.minObjectSize = faceSize;
            ((DetectionBasedTracker*)thiz)->setParameters(DetectorParams);
        }
    }
    catch(cv::Exception e)
    {
        LOGD("nativeStop catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeSetFaceSize catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeSetFaceSize exit");
}


JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_DetectionBasedTracker_nativeDetect
(JNIEnv * jenv, jclass, jlong thiz, jlong imageGray, jlong faces)
{
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeDetect enter");
    try
    {
        vector<Rect> RectFaces;
        ((DetectionBasedTracker*)thiz)->process(*((Mat*)imageGray));
        ((DetectionBasedTracker*)thiz)->getObjects(RectFaces);
        vector_Rect_to_Mat(RectFaces, *((Mat*)faces));
    }
    catch(cv::Exception e)
    {
        LOGD("nativeCreateObject catched cv::Exception: %s", e.what());
        jclass je = jenv->FindClass("org/opencv/core/CvException");
        if(!je)
            je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeDetect catched unknown exception");
        jclass je = jenv->FindClass("java/lang/Exception");
        jenv->ThrowNew(je, "Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
    }
    LOGD("Java_org_opencv_samples_fd_DetectionBasedTracker_nativeDetect exit");
}
