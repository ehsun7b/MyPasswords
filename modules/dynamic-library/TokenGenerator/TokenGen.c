#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "TokenGen.h"

char* genToken(const char password[30], const char timestamp[30]) {
    char* temp = malloc(61);
    
    if (strlen(password) < 30 || strlen(timestamp) < 30) {
        printf("Invalid length of password/timestamp.");
    }
    
    int i;
    for (i = 0; i < 30; ++i) {
        if (i % 2 == 0) {
            temp[i] = password[i];
        } else {
            temp[i] = timestamp[i];
        }
    }

    temp[60] = '\0';

    char* charResult = malloc(15);
    charResult = memcpy(charResult, &temp[3], 17);
    charResult[15] = '\0';
    
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

/*
JNIEXPORT jstring JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeGenToken
(JNIEnv *env, jobject obj, jstring jpassword, jstring jtimestamp) {

    const char* password = (*env)->GetStringUTFChars(env, jpassword, NULL);
    const char* timestamp = (*env)->GetStringUTFChars(env, jtimestamp, NULL);    
    
    printf("%s\n%s", password, timestamp);
    printf("\n%d %d", strlen(password), strlen(timestamp));
    
    const char* charResult = genToken(password, timestamp);

    jstring result = (*env)->NewStringUTF(env, charResult); // C style string to Java String 
    (*env)->DeleteLocalRef(env, obj);
    return result;
}*/

JNIEXPORT jstring JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeGenToken
(JNIEnv *env, jobject obj, jstring jpassword, jstring jtimestamp) {

    const char* password = (*env)->GetStringUTFChars(env, jpassword, NULL);
    const char* timestamp = (*env)->GetStringUTFChars(env, jtimestamp, NULL);    
 
    
    char* res = genToken(password, timestamp);
    
    jstring result = (*env)->NewStringUTF(env, res); // C style string to Java String     
  
    return result;
}
