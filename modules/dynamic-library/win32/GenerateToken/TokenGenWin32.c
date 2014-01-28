#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "TokenGenWin32.h"

char* genToken(const char* password, const char* timestamp) {
    char* temp = malloc(61);

    int i;
    for (i = 0; i < 60; ++i) {
        if (i % 2 == 0) {
            temp[i] = password[i];
        } else {
            temp[i] = timestamp[i];
        }
    }

    temp[60] = '\0';

    char* charResult = malloc(20);
    charResult = memcpy(charResult, &temp[10], 30);
    charResult[20] = '\0';

    return charResult;
}

JNIEXPORT jint JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeCheckToken
(JNIEnv *env, jobject obj, jstring jtoken, jstring jpassword, jstring jtimestamp) {

    const char* password = (*env)->GetStringUTFChars(env, jpassword, NULL);
    const char* timestamp = (*env)->GetStringUTFChars(env, jtimestamp, NULL);
    const char* currentToken = (*env)->GetStringUTFChars(env, jtoken, NULL);

    printf(" ");
    
    const char* token = genToken(password, timestamp);
    int compare = strcmp(token, currentToken);

    jint result = compare == 0 ? 1 : 0;
    return result;
}

JNIEXPORT jstring JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeGenToken
(JNIEnv *env, jobject obj, jstring jpassword, jstring jtimestamp) {

    const char* password = (*env)->GetStringUTFChars(env, jpassword, NULL);
    const char* timestamp = (*env)->GetStringUTFChars(env, jtimestamp, NULL);

    //int lPassword = strlen(password);
    //int lTimestamp = strlen(timestamp);
    /*
        char* temp = malloc(61);

        int i;
        for (i = 0; i < 60; ++i) {
            if (i % 2 == 0) {
                temp[i] = password[i];
            } else {
                temp[i] = timestamp[i];
            }
        }
    
        temp[60] = '\0';
    
        char* charResult = malloc(20);
        charResult = memcpy(charResult, &temp[10], 30);
        charResult[20] = '\0';*/

    const char* charResult = genToken(password, timestamp);

    jstring result = (*env)->NewStringUTF(env, charResult); // C style string to Java String    
    return result;
}