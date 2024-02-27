## How to run auth-server
1. If you don't already have a Firebase project, you need to create one in the Firebase console[https://console.firebase.google.com/](https://console.firebase.google.com/).

2. Then create one web app under firebase project which you have just created, then you'll get firebase project config like this:-
```
const firebaseConfig = {
  apiKey: apiKey,
  authDomain: authDomain,
  databaseURL: databaseURL,
  projectId: projectId,
  storageBucket: storageBucket,
  messagingSenderId: messagingSenderId,
  appId: appId,
  measurementId: measurementId
};
```

3. Once you have created a Firebase project, you can initialize the SDK with Google Application Default Credentials[https://cloud.google.com/docs/authentication#service-accounts](https://cloud.google.com/docs/authentication#service-accounts). Because default credentials lookup is fully automated in Google environments, with no need to supply environment variables or other configuration, this way of initializing the SDK is strongly recommended for applications running in Google environments such as Cloud Run, App Engine, and Cloud Functions.

Use the gcloud CLI to set up Application Default Credentials (ADC).
```
command
       gcloud auth application-default login
```

A sign-in screen appears. After you sign in, your credentials are stored in the local credential file used by ADC.

4. Replace PROJECTID with your firebase project id in InitializeFirebaseRunner class.

5. Then run the project
   Access the Swagger UI at [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html).
