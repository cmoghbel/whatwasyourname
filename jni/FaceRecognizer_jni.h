/*
 * FaceRecognizer_jni.h
 *
 * Copyright (c) 2011-2012, opencv dev team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Author: Christopher Moghbel
 *
 * This class is derived and modified from work available at http://docs.opencv.org/trunk/modules/contrib/doc/facerec/facerec_tutorial.html.
 */
#include <jni.h>

//#ifndef FACERECOGNIZER_JNI_H_
//#define FACERECOGNIZER_JNI_H_

#ifndef _Included_org_opencv_samples_fd_FaceRecognizer
#define _Included_org_opencv_samples_fd_FaceRecognizer
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeTrain
	(JNIEnv *, jclass);

JNIEXPORT jboolean JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict
	(JNIEnv *, jclass, jlong);

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetConfidence
	(JNIEnv *, jclass, jdouble);

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetThreshold
	(JNIEnv *, jclass, jdouble threshold);

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetComparison
	(JNIEnv *, jclass, jboolean gt);


#ifdef __cplusplus
}
#endif
#endif /* FACERECOGNIZER_JNI_H_ */
