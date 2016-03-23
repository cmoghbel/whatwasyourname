/*
 * FaceRecognizer_jni.cpp
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
#include <FaceRecognizer_jni.h>

#include <android/log.h>

#define LOG_TAG "FaceRecognition/FaceRecognizer"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__))

#include "opencv2/core/core.hpp"
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/highgui/highgui.hpp"

#include <iostream>
#include <fstream>
#include <sstream>

using namespace cv;
using namespace std;

Ptr<FaceRecognizer> MODEL = createLBPHFaceRecognizer();
string FILES_DIR = "/data/data/edu.ucla.cs213a.whatwasyourname/files/";
double THRESHOLD = 100.0;
double CONFIDENCE = 50.0;
bool GT = true;
bool RETRAIN = true;
bool TRAINED = false;

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeTrain
	(JNIEnv *, jclass)
{
	LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeTrain enter");

	string modelFile = FILES_DIR + "face_model.xml";
	ifstream infile(modelFile.c_str());
	if (infile.good() && !RETRAIN)
	{
		LOGW("Loading model from file.");
		MODEL->load(FILES_DIR + "face_model.xml");
		TRAINED = true;
	}
	else
	{
		LOGW("Retraining model...");

		Mat image1 = imread(FILES_DIR + "train0.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image2 = imread(FILES_DIR + "train1.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image3 = imread(FILES_DIR + "train2.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image4 = imread(FILES_DIR + "train3.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image5 = imread(FILES_DIR + "train4.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image6 = imread(FILES_DIR + "train5.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image7 = imread(FILES_DIR + "train6.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image8 = imread(FILES_DIR + "train7.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image9 = imread(FILES_DIR + "train8.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image10 = imread(FILES_DIR + "train9.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image11 = imread(FILES_DIR + "train10.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image12 = imread(FILES_DIR + "train11.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image13 = imread(FILES_DIR + "train12.pgm", CV_LOAD_IMAGE_GRAYSCALE);
		Mat image14 = imread(FILES_DIR + "train13.pgm", CV_LOAD_IMAGE_GRAYSCALE);

		vector<Mat> images;
		vector<int> labels;

		if (image1.empty()) LOGW("IMAGE 1 EMPTY!");
		else { images.push_back(image1); labels.push_back(1); }
		if (image2.empty()) LOGW("IMAGE 2 EMPTY!");
		else { images.push_back(image2); labels.push_back(1); }
		if (image3.empty()) LOGW("IMAGE 3 EMPTY!");
		else { images.push_back(image3); labels.push_back(1); }
		if (image4.empty()) LOGW("IMAGE 4 EMPTY!");
		else { images.push_back(image4); labels.push_back(1); }
		if (image5.empty()) LOGW("IMAGE 5 EMPTY!");
		else { images.push_back(image5); labels.push_back(1); }
		if (image6.empty()) LOGW("IMAGE 6 EMPTY!");
		else { images.push_back(image6); labels.push_back(1); }
		if (image7.empty()) LOGW("IMAGE 7 EMPTY!");
		else { images.push_back(image7); labels.push_back(1); }
		if (image8.empty()) LOGW("IMAGE 8 EMPTY!");
		else { images.push_back(image8); labels.push_back(1); }
		if (image9.empty()) LOGW("IMAGE 9 EMPTY!");
		else { images.push_back(image9); labels.push_back(1); }
		if (image10.empty()) LOGW("IMAGE 10 EMPTY!");
		else { images.push_back(image10); labels.push_back(1); }
		if (image11.empty()) LOGW("IMAGE 11 EMPTY!");
		else { images.push_back(image11); labels.push_back(1); }
		if (image12.empty()) LOGW("IMAGE 12 EMPTY!");
		else { images.push_back(image12); labels.push_back(1); }
		if (image13.empty()) LOGW("IMAGE 13 EMPTY!");
		else { images.push_back(image13); labels.push_back(1); }
		if (image14.empty()) LOGW("IMAGE 14 EMPTY!");
		else { images.push_back(image14); labels.push_back(1); }

		LOGD("Images read in succesfully!");

		if(images.size() <= 1)
		{
			LOGW("Not enough images, abandoning training!");
			return;
		}

		MODEL->train(images, labels);

		LOGW("Model trained!");

		MODEL->save(FILES_DIR + "face_model.xml");
		TRAINED = true;
		LOGD("Model trained successfully!");
	}
	LOGW("Java_org_opencv_samples_fd_FaceRecognizer_nativeTrain exit");
}

JNIEXPORT jboolean JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict
	(JNIEnv *, jclass, jlong faceImage)
{
	if (!TRAINED)
	{
		LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict not trained, return -1");
		return false;
	}
	LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict entered");
	MODEL->set("threshold", THRESHOLD);
	int predictLabel = -1;
	double confidence = 0.0;
	LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict about to predict");
	MODEL->predict(*((Mat*)faceImage), predictLabel, confidence);

	std::ostringstream ss;
	ss << confidence;
	std:string confStr (ss.str());

	LOGW(confStr.c_str());
	if (confidence >= 0.0 && confidence < 20.0) LOGW("Confidence between 0 and 20");
	else if (confidence >= 20.0 && confidence < 40.0) LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict Confidence between 20 and 40");
	else if (confidence >= 40.0 && confidence < 60.0) LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict Confidence between 40 and 60");
	else if (confidence >= 60.0 && confidence < 80.0) LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict Confidence between 60 and 80");
	else if (confidence >= 80.0 && confidence <= 100.0) LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict Confidence between 80 and 100");
	else LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict Confidence something else?");
	LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict predicted");
	LOGW("Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativePredict exited");
	if (GT)
		return predictLabel == 1 && confidence > CONFIDENCE;
	else
		return predictLabel == 1 && confidence < CONFIDENCE;
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetConfidence
	(JNIEnv *, jclass, jdouble confidence)
{
	CONFIDENCE = confidence;
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetThreshold
	(JNIEnv *, jclass, jdouble threshold)
{
	THRESHOLD = threshold;
}

JNIEXPORT void JNICALL Java_edu_ucla_cs213a_whatwasyourname_FaceRecognizer_nativeSetComparison
	(JNIEnv *, jclass, jboolean gt)
{
	GT = gt;
}
