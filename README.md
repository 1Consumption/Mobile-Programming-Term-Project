# Seobang; Cooking helper Application Project
An Application that helps you to cook with what you have. <br>

## 
#### Gradle
```java
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:27.1.0'
    implementation 'com.android.support:design:27.1.0'
    implementation 'com.android.support:support-core-utils:24.2.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    implementation 'com.android.support:support-v4:27.1.1'
    testImplementation 'junit:junit:4.12'
    testImplementation 'org.hamcrest:java-hamcrest:2.0.0.0'
    testImplementation 'com.android.support.test:rules:0.5'
    testImplementation 'com.android.support.test:runner:0.5'

    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    implementation 'com.android.support:recyclerview-v7:27.1.0'
    implementation 'com.github.paolorotolo:appintro:4.1.0'
    implementation 'com.github.skydoves:powermenu:2.0.4'
    implementation 'com.github.skydoves:baserecyclerviewadapter:0.1.0'
    implementation 'de.hdodenhof:circleimageview:2.2.0'
    implementation 'com.github.bumptech.glide:glide:4.7.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
    implementation 'com.android.support:recyclerview-v7:27.1.0'
    implementation 'com.github.medyo:android-about-page:1.2.4'
    implementation 'com.android.support:palette-v7:27.1.1'
    implementation 'com.southernbox:RippleLayout:0.1.0'
    implementation 'com.github.yalantis:jellytoolbar:v1.0'
    implementation 'com.github.Yalantis:pull-to-make-soup:1.0.2'
    implementation 'com.github.developer-shivam:Crescento:1.2.1'
    implementation 'com.github.PhilJay:MpAndroidChart:v3.0.2'

    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support:support-annotations:25.1.0'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:2.2.2'
    androidTestImplementation 'com.android.support.test:runner:0.5'
}

```



## Functions
### Core functions
Users can select recipe, ingredient, history. 
From the recipe select, users can select a recipe among 517 of recipes. Once user select the recipe, we present recipe preview for users to have an idea of what the food will be like and what the user need for cooking.

From the ingredient select, users can select what they have or search from the search bar. Once ingredients the user have are selected, from the special algorithm we developed, few possible recipes are drawn unless there is no possible recipe. Once user select one of them, user can follow the steps. From the step, we put auto timer for the users. The auto timer is set from the users’ point of view for the UI. 

From the history select, user’s data which is the cooked foods are stored and are shown with list view or pi chart. 

### Supplimentary functions
User can adjust the accuracy of what they choose with the possible recipes’ ingredients. If the accuracy is 100 percent, it could be hard to find the recipe on the other hand, from the 30~70% of accuracy, users can enjoy the possible recipes.

From the fragment of the step of the recipe, when the timer is needed, auto timer is popped by the floating button for blocking the overlapping with the text.

### Development environment
up to Android Studio 3.0.1



# License
```xml
Copyright 2018 Mobile team(Hayeon, Hanseop, Minuk)
```
