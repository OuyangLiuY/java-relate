#include <pthread.h>
#include <stdio.h>
#include <jni.h>
#include "concurrency_start_MyThread.h"
pthread_t pid;
void *thread_entity(void *arg)
{
     printf("I am Thread!\n");
}
JNIEXPORT void JNICALL Java_concurrency_start_MyThread_start0(JNIEnv *env, jobject obj)
{
    pthread_create(&pid, NULL, thread_entity, NULL);
      //在c代码里面调用java代码里面的方法
        // java 反射
        //1 . 找到java代码的 class文件
        //    jclass      (*FindClass)(JNIEnv*, const char*);
        jclass dpclazz = (*env)->FindClass(env,"concurrency/start/MyThread");
        if(dpclazz==0){
            printf("find class error");
            return;
        }
        printf("find class\n ");

        //2 寻找class里面的方法
        //   jmethodID   (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
        jmethodID method1 = (*env)->GetMethodID(env,dpclazz,"run","()V");
        if(method1==0){
            printf("find method1 error\n");
            return;
        }
         printf("find method1 !\n");
        //3 .调用这个方法
        //    void        (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
        (*env)->CallVoidMethod(env,obj,method1);
     printf("I am main Thread!\n");
}
int main()
{
    return 0;
}
//void LOGI(){}
