#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "TokenGen.h"

char* genToken(const char a[30], const char b[30]) {
  if (strlen(a) < 30 || strlen(b) < 30) {
    return "";
  }

  char *result = malloc(61);
  result[60] = '\0';


  int i, j;

  for (j = 0, i = 0; i < 30; ++i, j += 2) {

    result[j] = a[i];
  }

  for (j = 1, i = 0; i < 30; ++i, j += 2) {
    result[j] = b[i];
  }

  return result;
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
    
    printf("%s\n%s", password, timestamp);
    printf("\n%d %d", strlen(password), strlen(timestamp));
    
    const char* charResult = genToken(password, timestamp);

    jstring result = (*env)->NewStringUTF(env, charResult);     
    return result;
}

/*
JNIEXPORT jstring JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeGenToken
(JNIEnv *env, jobject obj, jstring jpassword, jstring jtimestamp) {

    const char* password = (*env)->GetStringUTFChars(env, jpassword, NULL);
    const char* timestamp = (*env)->GetStringUTFChars(env, jtimestamp, NULL);    
 
    
    char* res = genToken(password, timestamp);
    
    jstring result = (*env)->NewStringUTF(env, res); // C style string to Java String     
  
    return result;
}*/
