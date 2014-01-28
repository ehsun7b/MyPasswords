#include <jni.h>
#include <stdio.h>
#include "TokenGenWin32.h"

JNIEXPORT jstring JNICALL Java_com_mypasswords7_gui_embeddedweb_LoginProfile_nativeGenToken
(JNIEnv *env, jobject obj, jstring jpassword, jstring jtimestamp) {

    const char* msg = "Hi there";
    jstring result = (*env)->NewStringUTF(env, msg); // C style string to Java String
    (*env)->ReleaseStringUTFChars(env, result, msg);
    return result;        
}
